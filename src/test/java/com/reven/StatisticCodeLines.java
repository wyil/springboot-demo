package com.reven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @ClassName: StatisticCodeLines
 * @Description: 代码行数统计
 * @author huangruiwen
 * @date 2018年9月7日
 */
public class StatisticCodeLines {

    public static int normalLines = 0; // 有效程序行数
    public static int whiteLines = 0; // 空白行数
    public static int commentLines = 0; // 注释行数
    /**   
     * @Fields ignoreDirectory : 包含该字符串的目录忽略
     */  
    public static String[] ignoreDirectory = { "\\target", "\\.svn", "\\.settings", "\\metadata.bak",
            "\\${env.RELEASE_HOME}", "\\logs", "\\js\\bootstrap", "\\js\\easyui", "\\js\\My97DatePicker",
            "\\js\\echarts" };

    /**   
     * @Fields fileExtension : 统计的文件后缀
     */  
    public static String[] fileExtension = { ".*\\.java", ".*\\.xml", ".*\\.html", ".*\\.shtml", ".*\\.js" };

    /**   
     * @Fields ignoreFile : 包含该字符串的目录忽略
     */  
    public static String[] ignoreFile = { "china.js", "echarts.js", "jquery.bgiframe.js", "jquery.common.js",
            "jquery.min.js", "jquery.nicescroll.js", "jquery.openscroll.js", "jquery.webui-popover.min.js" ,"scriptbreaker-multiple-accordion-1.js"};

    public static void main(String[] args) throws IOException {

        File file = new File("D:\\eclipse-photon-workspace\\cmop");
        if (file.exists()) {
            statistic(file);
        }
        System.out.println("总有效代码行数: " + normalLines);
        System.out.println("总空白行数：" + whiteLines);
        System.out.println("总注释行数：" + commentLines);
        System.out.println("总有效代码行数:+总注释行数=" + (normalLines + commentLines));
        System.out.println("总行数：" + (normalLines + whiteLines + commentLines));
    }

    private static void statistic(File file) throws IOException {
        if (file.isDirectory() && ignoreDirectory.length > 0) {
            for (int i = 0; i < ignoreDirectory.length; i++) {
                if (file.getPath().contains(ignoreDirectory[i])) {
                    return;
                }
            }
//            System.out.println(file.getPath());
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                statistic(files[i]);
            }
        }
        if (file.isFile() && fileExtension.length > 0) {
            // 统计扩展名为java的文件
            for (int i = 0; i < fileExtension.length; i++) {
                if (file.getName().matches(fileExtension[i])) {
                    
                    for (int j = 0; j < ignoreFile.length; j++) {
                        if (file.getName().equals(ignoreFile[j])) {
                            return;
                        }
                    }
                    
                    parse(file);
                }
            }
        }

    }

    public static void parse(File file) {
        BufferedReader br = null;
        // 判断此行是否为注释行
        boolean comment = false;
        int tempWhiteLines = 0;
        int tempCommentLines = 0;
        int tempNormalLines = 0;

        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.matches("^[\\s&&[^\\n]]*$")) {
                    // 空行
                    whiteLines++;
                    tempWhiteLines++;
                } else if (line.startsWith("/*") && !line.endsWith("*/")) {
                    // 判断此行为"/*"开头的注释行
                    commentLines++;
                    comment = true;
                } else if (comment == true && !line.endsWith("*/")) {
                    // 为多行注释中的一行（不是开头和结尾）
                    commentLines++;
                    tempCommentLines++;
                } else if (comment == true && line.endsWith("*/")) {
                    // 为多行注释的结束行
                    commentLines++;
                    tempCommentLines++;
                    comment = false;
                } else if (line.startsWith("//")) {
                    // 单行注释行
                    commentLines++;
                    tempCommentLines++;
                } else {
                    // 正常代码行
                    normalLines++;
                    tempNormalLines++;
                }
            }

            System.out.println("有效行数" + tempNormalLines + " ,空白行数" + tempWhiteLines + " ,注释行数" + tempCommentLines
                    + " ,总行数" + (tempNormalLines + tempWhiteLines + tempCommentLines) + "     " + file.getName());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}