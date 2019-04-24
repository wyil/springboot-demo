package com.reven.uitl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.reven.core.ServiceException;

/**
 * @ClassName: ExportExcelUtil
 * @Description: excel通用导出工具类
 * @date 2019年3月14日
 * @param <T>
 */
public class ExcelUtil {
    // 2007 版本以上 最大支持1048576行
    public static final String EXCEL_FILE_2007 = "2007";
    // 2003 版本 最大支持65536 行
    public static final String EXCEL_FILE_2003 = "2003";

    private static Pattern NUM_PATTERN = Pattern.compile("^//d+(//.//d+)?$");

    private ExcelUtil() {
        super();
    }

    /**
     * @Title: responseExcel
     * @Description: 导出Excel（导出到浏览器，可以自定义工作表的大小）
     * @param fileName  导出的文件名
     * @param sheetName 表格标题
     * @param sheetSize 每个工作表中放入记录的最大行数
     * @param list      数据源
     * @param fieldMap  类的英文属性和Excel中的中文列名的对应关系
     * @param response  使用response可以导出到浏览器
     * @throws IOException 
     * @throws IntrospectionException 
     * @throws ServiceException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws Exception
     */
    public static <T> void responseExcel(String fileName, String sheetName, int sheetSize, List<T> list,
            Map<String, String> fieldMap, HttpServletResponse response) throws IOException, IllegalAccessException,
            InvocationTargetException, ServiceException, IntrospectionException {
        if (StringUtils.isEmpty(fileName)) {
            // 设置默认文件名为当前时间：年月日时分秒
            fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        }
        // 设置response头信息
        response.reset();
        response.setContentType("application/msexcel; charset=GBK"); // 改成输出Excel文件
        // 使用2007格式
        fileName += ".xlsx";
        // 去除空格
        fileName = fileName.replaceAll(" ", "");
        String downloadFileName = new String(fileName.getBytes("GBK"), "iso-8859-1");// 设置编码
        response.setHeader("Content-disposition", "attachment; filename=" + downloadFileName);

        // 创建工作簿并发送到浏览器
        OutputStream out = response.getOutputStream();
        exportExcel(sheetName, sheetSize, list, fieldMap, out, EXCEL_FILE_2007);

    }

    /**
     * 导出list数据到excel
     * 
     * @param sheetName 表格标题
     * @param sheetSize 每个工作表中放入记录的最大行数
     * @param list      数据集合
     * @param fieldMap  类的英文属性和Excel中的中文列名的对应关系 ,<br>
     *                  如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式,<br>
     *                  如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写fieldMap.put("college.collegeName","学院名称")
     * @param out       输出流
     * @param version   2003 或者 2007，不传时默认生成2007版本
     * @throws ServiceException 
     * @throws IOException 
     * @throws IntrospectionException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws Exception
     */
    public static <T> void exportExcel(String sheetName, int sheetSize, List<T> list, Map<String, String> fieldMap,
            OutputStream out, String version) throws ServiceException, IllegalAccessException,
            InvocationTargetException, IntrospectionException, IOException {
        if (list == null || list.isEmpty()) {
            /**throw new ServiceException("NO_DATA", "数据源中没有任何数据");*/
            throw new ServiceException("数据源中没有任何数据");
        }

        if (sheetSize > 65535 || sheetSize < 1) {
            sheetSize = 65535;
        }

        if (StringUtils.isEmpty(version) || EXCEL_FILE_2007.equals(version.trim())) {
            exportExcel2007(sheetName, sheetSize, list, fieldMap, out, "yyyy-MM-dd HH:mm:ss");
        } else {
            exportExcel2003(sheetName, sheetSize, list, fieldMap, out, "yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     * <p>
     * 通用Excel导出方法,利用反射机制遍历对象的字段，将数据写入Excel文件中 <br>
     * 此版本生成2007以上版本的文件 (文件后缀：xlsx)
     * </p>
     * @param sheetName 表格标题名
     * @param sheetSize 每个工作表中放入记录的最大行数
     * @param list      需要显示的数据集合,集合中一定要放置符合JavaBean风格的类的对象。此方法支持的
     *                  JavaBean属性的数据类型有基本数据类型及String,Date
     * @param fieldMap  类的英文属性和Excel中的中文列名的对应关系 ,<br>
     *                  如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式,<br>
     *                  如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写fieldMap.put("college.collegeName","学院名称")
     * @param out       与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern   如果有时间数据，设定输出格式。默认为"yyyy-MM-dd hh:mm:ss"
     * @throws IntrospectionException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IOException 
     * @throws Exception
     */
    public static <T> void exportExcel2007(String sheetName, Integer sheetSize, List<T> list,
            Map<String, String> fieldMap, OutputStream out, String pattern)
            throws IllegalAccessException, InvocationTargetException, IntrospectionException, IOException {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            // 生成一个样式
            XSSFCellStyle styleHead = workbook.createCellStyle();
            // 设置这些样式
            styleHead.setFillForegroundColor(new XSSFColor(java.awt.Color.gray));
            styleHead.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleHead.setBorderBottom(BorderStyle.THIN);
            styleHead.setBorderLeft(BorderStyle.THIN);
            styleHead.setBorderRight(BorderStyle.THIN);
            styleHead.setBorderTop(BorderStyle.THIN);
            styleHead.setAlignment(HorizontalAlignment.CENTER);
            styleHead.setVerticalAlignment(VerticalAlignment.CENTER);
            // 生成一个字体
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setFontName("宋体");
            font.setColor(new XSSFColor(java.awt.Color.BLACK));
            font.setFontHeightInPoints((short) 11);
            // 把字体应用到当前的样式
            styleHead.setFont(font);

            // 生成并设置另一个样式
            XSSFCellStyle styleData = workbook.createCellStyle();
            styleData.setFillForegroundColor(new XSSFColor(java.awt.Color.WHITE));
            styleData.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleData.setBorderBottom(BorderStyle.THIN);
            styleData.setBorderLeft(BorderStyle.THIN);
            styleData.setBorderRight(BorderStyle.THIN);
            styleData.setBorderTop(BorderStyle.THIN);
            styleData.setAlignment(HorizontalAlignment.CENTER);
            styleData.setVerticalAlignment(VerticalAlignment.CENTER);
            // 生成另一个字体
            XSSFFont font2 = workbook.createFont();
            font2.setBold(false);
            // 把字体应用到当前的样式
            styleData.setFont(font2);

            // 1.计算一共有多少个工作表
            double sheetNum = Math.ceil(list.size() / sheetSize.doubleValue());

            // 2.创建相应的工作表，并向其中填充数据
            for (int i = 0; i < sheetNum; i++) {
                // 如果只有一个工作表的情况
                if (1 == sheetNum) {
                    // 生成一个表格
                    XSSFSheet sheet = workbook.createSheet(sheetName);
                    fillSheet2007(sheet, list, fieldMap, pattern, styleHead, styleData);
                    // 有多个工作表的情况
                } else {
                    XSSFSheet sheet = workbook.createSheet(sheetName + (i + 1));
                    // 获取开始索引和结束索引
                    int firstIndex = i * sheetSize;
                    int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list.size() - 1
                            : (i + 1) * sheetSize - 1;
                    List<T> listCopy = new ArrayList<>(lastIndex - firstIndex + 1);
                    listCopy.addAll(list.subList(firstIndex, lastIndex + 1));
                    // 填充工作表
                    fillSheet2007(sheet, listCopy, fieldMap, pattern, styleHead, styleData);
                }
            }
            workbook.write(out);
        } finally {
            workbook.close();
        }
    }

    private static <T> void fillSheet2007(XSSFSheet sheet, List<T> list, Map<String, String> fieldMap, String pattern,
            XSSFCellStyle styleHead, XSSFCellStyle styleData)
            throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);

        // 定义存放英文字段名和中文字段名的数组
        String[] enFields = new String[fieldMap.size()];
        String[] cnFields = new String[fieldMap.size()];

        // 填充数组
        int count = 0;
        for (Entry<String, String> entry : fieldMap.entrySet()) {
            enFields[count] = entry.getKey();
            cnFields[count] = entry.getValue();
            count++;
        }
        // 填充表头
        XSSFRow row = sheet.createRow(0);
        XSSFCell cellHeader;
        for (int i = 0; i < cnFields.length; i++) {
            cellHeader = row.createCell(i);
            cellHeader.setCellStyle(styleHead);
            cellHeader.setCellValue(new XSSFRichTextString(cnFields[i]));
        }

        // 遍历集合数据，产生数据行
        int rowIndex = 0;
        T t;
        XSSFCell cell;
        Object value;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        for (int index = 0; index < list.size(); index++) {
            rowIndex++;
            row = sheet.createRow(rowIndex);
            t = list.get(index);

            for (int i = 0; i < enFields.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(styleData);
                value = getFieldValueByNameSequence(enFields[i], t);
                setCellValue(cell, value, sdf);
            }
        }

    }

    private static void setCellValue(XSSFCell cell, Object value, SimpleDateFormat sdf) {
        XSSFRichTextString richString;
        Matcher matcher;
        String textValue;
        // 判断值的类型后进行强制类型转换
        textValue = null;
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Float) {
            textValue = String.valueOf((Float) value);
            cell.setCellValue(textValue);
        } else if (value instanceof Double) {
            textValue = String.valueOf((Double) value);
            cell.setCellValue(textValue);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        if (value instanceof Boolean) {
            textValue = "是";
            if (!(Boolean) value) {
                textValue = "否";
            }
        } else if (value instanceof Date) {
            textValue = sdf.format((Date) value);
        } else {
            // 其它数据类型都当作字符串简单处理
            if (value != null) {
                textValue = value.toString();
            }
        }

        if (textValue != null) {
            matcher = NUM_PATTERN.matcher(textValue);
            if (matcher.matches()) {
                // 是数字当作double处理
                cell.setCellValue(Double.parseDouble(textValue));
            } else {
                richString = new XSSFRichTextString(textValue);
                cell.setCellValue(richString);
            }
        }
    }

    /**
     * 2007版本已经普及不推荐使用2003
     * 
     * 通用Excel导出方法,利用反射机制遍历对象的所有字段，将数据写入Excel文件中 <br>
     * 此方法生成2003版本的excel,文件名后缀：xls <br>
     * 
     * @param sheetName 表格标题名
     * @param list      需要显示的数据集合,集合中一定要放置符合JavaBean风格的类的对象。此方法支持的
     *                  JavaBean属性的数据类型有基本数据类型及String,Date
     * @param fieldMap  类的英文属性和Excel中的中文列名的对应关系 ,<br>
     *                  如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式,<br>
     *                  如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写fieldMap.put("college.collegeName","学院名称")
     * @param out       与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern   如果有时间数据，设定输出格式。默认为"yyyy-MM-dd hh:mm:ss"
     * @throws IntrospectionException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IOException
     */
    public static <T> void exportExcel2003(String sheetName, Integer sheetSize, List<T> list,
            Map<String, String> fieldMap, OutputStream out, String pattern)
            throws IllegalAccessException, InvocationTargetException, IntrospectionException, IOException {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
            // 生成一个样式
            HSSFCellStyle styleHead = workbook.createCellStyle();
            // 设置这些样式
            styleHead.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            styleHead.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleHead.setBorderBottom(BorderStyle.THIN);
            styleHead.setBorderLeft(BorderStyle.THIN);
            styleHead.setBorderRight(BorderStyle.THIN);
            styleHead.setBorderTop(BorderStyle.THIN);
            styleHead.setAlignment(HorizontalAlignment.CENTER);
            styleHead.setVerticalAlignment(VerticalAlignment.CENTER);
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setFontName("宋体");
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 11);
            // 把字体应用到当前的样式
            styleHead.setFont(font);
            // 生成并设置另一个样式
            HSSFCellStyle styleData = workbook.createCellStyle();
            styleData.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleData.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleData.setBorderBottom(BorderStyle.THIN);
            styleData.setBorderLeft(BorderStyle.THIN);
            styleData.setBorderRight(BorderStyle.THIN);
            styleData.setBorderTop(BorderStyle.THIN);
            styleData.setAlignment(HorizontalAlignment.CENTER);
            styleData.setVerticalAlignment(VerticalAlignment.CENTER);
            // 生成另一个字体
            HSSFFont font2 = workbook.createFont();
            font2.setBold(true);
            // 把字体应用到当前的样式
            styleData.setFont(font2);
            // 1.计算一共有多少个工作表
            double sheetNum = Math.ceil(list.size() / sheetSize.doubleValue());

            // 2.创建相应的工作表，并向其中填充数据
            for (int i = 0; i < sheetNum; i++) {
                // 如果只有一个工作表的情况
                if (1 == sheetNum) {
                    // 生成一个表格
                    HSSFSheet sheet = workbook.createSheet(sheetName);
                    fillSheet2003(sheet, list, fieldMap, pattern, styleHead, styleData);
                    // 有多个工作表的情况
                } else {
                    HSSFSheet sheet = workbook.createSheet(sheetName + (i + 1));
                    // 获取开始索引和结束索引
                    int firstIndex = i * sheetSize;
                    int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list.size() - 1
                            : (i + 1) * sheetSize - 1;
                    List<T> listCopy = new ArrayList<>(lastIndex - firstIndex + 1);
                    listCopy.addAll(list.subList(firstIndex, lastIndex + 1));
                    // 填充工作表
                    fillSheet2003(sheet, listCopy, fieldMap, pattern, styleHead, styleData);
                }
            }

            workbook.write(out);
        } finally {
            workbook.close();
        }
    }

    private static <T> void fillSheet2003(HSSFSheet sheet, List<T> list, Map<String, String> fieldMap, String pattern,
            HSSFCellStyle styleHead, HSSFCellStyle styleData)
            throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);

        // 定义存放英文字段名和中文字段名的数组
        String[] enFields = new String[fieldMap.size()];
        String[] cnFields = new String[fieldMap.size()];

        // 填充数组
        int count = 0;
        for (Entry<String, String> entry : fieldMap.entrySet()) {
            enFields[count] = entry.getKey();
            cnFields[count] = entry.getValue();
            count++;
        }
        // 填充表头
        HSSFRow row = sheet.createRow(0);
        HSSFCell cellHeader;
        for (int i = 0; i < cnFields.length; i++) {
            cellHeader = row.createCell(i);
            cellHeader.setCellStyle(styleHead);
            cellHeader.setCellValue(new HSSFRichTextString(cnFields[i]));
        }

        // 遍历集合数据，产生数据行
        int rowIndex = 0;
        T t;

        HSSFCell cell;
        Object value;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        for (int index = 0; index < list.size(); index++) {
            rowIndex++;
            row = sheet.createRow(rowIndex);
            t = list.get(index);

            for (int i = 0; i < enFields.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(styleData);
                value = getFieldValueByNameSequence(enFields[i], t);
                setCellValue(cell, value, sdf);
            }
        }

    }

    private static void setCellValue(HSSFCell cell, Object value, SimpleDateFormat sdf) {
        HSSFRichTextString richString;
        Matcher matcher;
        String textValue;
        // 判断值的类型后进行强制类型转换
        textValue = null;
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Float) {
            textValue = String.valueOf((Float) value);
            cell.setCellValue(textValue);
        } else if (value instanceof Double) {
            textValue = String.valueOf((Double) value);
            cell.setCellValue(textValue);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        if (value instanceof Boolean) {
            textValue = "是";
            if (!(Boolean) value) {
                textValue = "否";
            }
        } else if (value instanceof Date) {
            textValue = sdf.format((Date) value);
        } else {
            // 其它数据类型都当作字符串简单处理
            if (value != null) {
                textValue = value.toString();
            }
        }

        if (textValue != null) {
            matcher = NUM_PATTERN.matcher(textValue);
            if (matcher.matches()) {
                // 是数字当作double处理
                cell.setCellValue(Double.parseDouble(textValue));
            } else {
                richString = new HSSFRichTextString(textValue);
                cell.setCellValue(richString);
            }
        }
    }

    /**
     * @MethodName : getFieldValueByName
     * @Description : 根据字段名获取字段值
     * @param fieldName 字段名
     * @param o         对象
     * @return 字段值
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws IntrospectionException 
     * @throws InvocationTargetException 
     */
    private static Object getFieldValueByName(String fieldName, Object o)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {

        Object value = null;
        Field field = getFieldByName(fieldName, o.getClass());

        if (field != null) {
            field.setAccessible(true);
            value = field.get(o);
        }

        if (value == null) {
            PropertyDescriptor pd = new PropertyDescriptor(fieldName, o.getClass());
            Method readMethod = pd.getReadMethod();
            if (readMethod != null) {
                value = readMethod.invoke(o);
            }
        }

        return value;
    }

    /**
     * @MethodName : getFieldByName
     * @Description : 根据字段名获取字段
     * @param fieldName 字段名
     * @param clazz     包含该字段的类
     * @return 字段
     */
    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        // 拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        // 如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        // 否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }

        // 如果本类和父类都没有，则返回空
        return null;
    }

    /**
     * @MethodName : getFieldValueByNameSequence
     * @Description : 根据带路径或不带路径的属性名获取属性值
     *              即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等
     * 
     * @param fieldNameSequence 带路径的属性名或简单属性名
     * @param o                 对象
     * @return 属性值
     * @throws IntrospectionException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws Exception
     */
    private static Object getFieldValueByNameSequence(String fieldNameSequence, Object o)
            throws IllegalAccessException, InvocationTargetException, IntrospectionException {

        Object value = null;

        // 将fieldNameSequence进行拆分
        String[] attributes = fieldNameSequence.split("\\.");
        if (attributes.length == 1) {
            value = getFieldValueByName(fieldNameSequence, o);
        } else {
            // 根据属性名获取属性对象
            Object fieldObj = getFieldValueByName(attributes[0], o);
            String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf('.') + 1);
            value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
        }
        return value;

    }

    /**
     * @MethodName : setFieldValueByName
     * @Description : 根据字段名给对象的字段赋值
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param o          对象
     * @param dateFormat
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws ParseException 
     */
    private static void setFieldValue(String fieldName, Object fieldValue, Object o, DateFormat dateFormat)
            throws ServiceException, IllegalAccessException, ParseException {
        Field field = getFieldByName(fieldName, o.getClass());
        if (field != null) {
            field.setAccessible(true);
            // 获取字段类型
            Class<?> fieldType = field.getType();
            String fieldValueStr = fieldValue.toString();

            // 根据字段类型给字段赋值
            if (String.class == fieldType) {
                field.set(o, String.valueOf(fieldValue));
            } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                field.set(o, Integer.parseInt(fieldValueStr));
            } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                field.set(o, Long.valueOf(fieldValueStr));
            } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                field.set(o, Float.valueOf(fieldValueStr));
            } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                field.set(o, Short.valueOf(fieldValueStr));
            } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                field.set(o, Double.valueOf(fieldValueStr));
            } else if (Date.class == fieldType) {
                field.set(o, dateFormat.parse(fieldValueStr));
            } else if (Character.TYPE == fieldType) {
                if (fieldValueStr.length() > 0) {
                    field.set(o, Character.valueOf(fieldValueStr.charAt(0)));
                }
            } else if (Boolean.TYPE == fieldType || Boolean.class == fieldType) {
                fieldValueStr = fieldValueStr.toLowerCase();
                if ("是".equals(fieldValueStr) || "1".equals(fieldValueStr) || "true".equals(fieldValueStr)
                        || "yes".equals(fieldValueStr)) {
                    field.set(o, true);
                } else if ("否".equals(fieldValueStr) || "0".equals(fieldValueStr) || "false".equals(fieldValueStr)
                        || "no".equals(fieldValueStr)) {
                    field.set(o, false);
                } else {
                    field.set(o, fieldValue);
                }
            } else {
                field.set(o, fieldValue);
            }
        } else {
            /**throw new ServiceException("NOT_EXIST_FIELD", o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);*/
            throw new ServiceException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
        }
    }

    /**   
     * 将Excel转化为List
     * @param originalFilename 文件的原始名，推荐使用file.getOriginalFilename()
     * @param in 承载着Excel的输入流
     * @param sheetIndex 要导入的工作表序号
     * @param beginRow 数据开始的行号（含表头）
     * @param entityClass List中对象的类型（Excel中的每一行都要转化为该类型的对象）
     * @param fieldMap Excel中的中文列头和类的英文属性的对应关系Map,key=英文属性名称，value=中文列头
     * @return
     * @throws IOException 
     * @throws ServiceException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ParseException 
     * @throws Exception      
     */
    public static <T> List<T> excelToList(String originalFilename, InputStream in, int sheetIndex, int beginRow,
            Class<T> entityClass, Map<String, String> fieldMap)
            throws ServiceException, IOException, InstantiationException, IllegalAccessException, ParseException {
        // 定义要返回的list
        List<T> resultList = new ArrayList<>();
        // 日期字段的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // 根据Excel数据源创建WorkBook
        Workbook wb = getWorkBook(originalFilename, in);
        // 导出页签
        Sheet sheet = wb.getSheetAt(sheetIndex);

        // 1、 获取工作表的有效数据行数
        int realRows = 0;
        int[] realIndex = new int[sheet.getLastRowNum()];
        realRows = getRealIndex(beginRow, sheet, realRows, realIndex);

        // 2、读取表头
        LinkedHashMap<String, Integer> colMap = readHead(beginRow, fieldMap, sheet);

        // 将sheet转换为list
        for (int i = 0; i < realRows; i++) {
            // 新建要转换的对象
            T entity = entityClass.newInstance();

            // 给对象中的字段赋值
            for (Entry<String, String> entry : fieldMap.entrySet()) {
                // 获取中文字段名
                String cnNormalName = entry.getValue();
                // 获取英文字段名
                String enNormalName = entry.getKey();
                // 根据中文字段名获取列号
                int col = colMap.get(cnNormalName);

                if (sheet.getRow(realIndex[i]) != null) {
                    // Cannot get a STRING value from a NUMERIC cell,解决方法是在读取数据前设置cell的type
                    Cell cell = sheet.getRow(realIndex[i]).getCell(col);
                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        // 获取当前单元格中的内容
                        String content = sheet.getRow(realIndex[i]).getCell(col).getStringCellValue();
                        if (StringUtils.isNotEmpty(content)) {
                            content = content.trim();
                            // 给对象赋值
                            setFieldValue(enNormalName, content, entity, sdf);
                        }
                    }
                }
            }

            resultList.add(entity);
        }
        return resultList;
    }

    private static LinkedHashMap<String, Integer> readHead(int beginRow, Map<String, String> fieldMap, Sheet sheet)
            throws ServiceException {
        Row firstRow = sheet.getRow(beginRow);
        String[] excelFieldNames = new String[firstRow.getPhysicalNumberOfCells()];

        // 获取Excel中的列名
        for (int i = 0; i < excelFieldNames.length; i++) {
            excelFieldNames[i] = firstRow.getCell(i).getStringCellValue().trim();
        }

        // 判断需要的字段在Excel中是否都存在
        boolean isExist = true;
        List<String> excelFieldList = Arrays.asList(excelFieldNames);
        StringBuilder notExitField = new StringBuilder();
        for (String cnName : fieldMap.values()) {
            if (!excelFieldList.contains(cnName)) {
                isExist = false;
                notExitField.append("[");
                notExitField.append(cnName);
                notExitField.append("]");
            }
        }

        // 如果有列名不存在，则抛出异常，提示错误
        if (!isExist) {
            /**throw new ServiceException("ERROR_FIELD", "Excel中缺少必要的字段，或字段名称有误");*/
            throw new ServiceException("Excel中缺少必要的字段" + notExitField.toString());
        }

        // 3、 将列名和列号放入Map中,这样通过列名就可以拿到列号
        LinkedHashMap<String, Integer> colMap = new LinkedHashMap<>();
        for (int i = 0; i < excelFieldNames.length; i++) {
            colMap.put(excelFieldNames[i], i);
        }
        return colMap;
    }

    private static int getRealIndex(int beginRow, Sheet sheet, int realRows, int[] realIndex) throws ServiceException {
        for (int i = beginRow + 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(beginRow);
            int nullCols = 0;
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                Cell currentCell = row.getCell(j);
                currentCell.setCellType(CellType.STRING);
                if ("".equals(currentCell.getStringCellValue())) {
                    nullCols++;
                }
            }
            // 空行不处理，直接跳过
            if (nullCols != row.getPhysicalNumberOfCells()) {
                realIndex[realRows++] = i;
            }
        }
        // 如果Excel中没有数据则提示错误
        if (realRows <= 0) {
            /**throw new ServiceException("NO_DATA", "Excel文件中没有任何数据");*/
            throw new ServiceException("Excel文件中没有任何数据");
        }
        return realRows;
    }

    private static Workbook getWorkBook(String originalFilename, InputStream is) throws ServiceException, IOException {
        // 创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        // 获取excel文件的io流
        // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
        if (originalFilename.endsWith("xls")) {
            // 2003
            workbook = new HSSFWorkbook(is);
        } else if (originalFilename.endsWith("xlsx")) {
            // 2007
            workbook = new XSSFWorkbook(is);
        }
        if (workbook == null) {
            throw new ServiceException("只支持xls、xlsx");
        }
        return workbook;
    }
}