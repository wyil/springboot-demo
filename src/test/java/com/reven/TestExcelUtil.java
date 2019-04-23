package com.reven;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.reven.model.entity.DemoExcel;
import com.reven.uitl.ExcelUtil;

/**
 * 测试文件导出
 */
public class TestExcelUtil {

    public static void main(String[] args) throws Exception {
        // 准备数据
        List<DemoExcel> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            if (i < 500) {
                list.add(new DemoExcel(1 + i, "张三" + i, new Date(), "zhangsan" + i, true, ((float) 1.3 + i), 1.4 + i,
                        (long) 1.5 + i));
            } else {
                list.add(new DemoExcel(1 + i, "李四" + i, new Date(), "lisi" + i, true, ((float) 2.3 + i), 28899.8884 + i,
                        (long) 2.0005 + i));
            }
        }
        LinkedHashMap<String, String> filedMap = new LinkedHashMap<String, String>();
        filedMap.put("id", "ID");
        filedMap.put("name", "姓名");
        filedMap.put("date", "日期");
        filedMap.put("enName", "英文名");
        filedMap.put("numFloat", "float");
        filedMap.put("numDouble", "double");
        filedMap.put("numLong", "long");

        ExcelUtil.exportExcel("用户导出", 500, list, filedMap, new FileOutputStream("D:/test7.xlsx"),
                ExcelUtil.EXCEL_FILE_2007);
//        ExcelUtil.exportExcel("用户导出", 500, list, filedMap, new FileOutputStream("D:/test3.xls"),
//                ExcelUtil.EXCEL_FILE_2003);
    }
}