package com.reven.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.reven.controller.common.BaseController;
import com.reven.controller.common.JxlsExcelView;
import com.reven.controller.common.ResResult;
import com.reven.model.entity.Demo;
import com.reven.model.entity.DemoExcel;
import com.reven.service.IDemoService;
import com.reven.uitl.ExcelUtil;

/**
 * @ClassName: DemoController
 * @author reven
 */
@RestController
@RequestMapping("/demo")
public class DemoController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(DemoController.class);
    @Resource
    private IDemoService demoService;
    
    @RequestMapping(value = "/exportExcelUtil")
    public void exportExcel(HttpServletResponse response, @RequestParam("filename") String filename) throws Exception {
        // 准备数据或从db查询
        List<DemoExcel> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            if (i < 500) {
                list.add(new DemoExcel(1 + i, "张三" + i, new Date(), "zhangsan" + i, true, ((float) 1.3 + i), 1.4 + i,
                        (long) 1.5 + i));
            } else {
                list.add(new DemoExcel(1 + i, "李四" + i, new Date(), "lisi" + i, false, ((float) 2.3 + i), 28899.8884 + i,
                        (long) 2.0005 + i));
            }
        }
        LinkedHashMap<String, String> filedMap = new LinkedHashMap<String, String>();
        filedMap.put("id", "ID");
        filedMap.put("name", "姓名");
        filedMap.put("date", "日期");
        filedMap.put("enName", "英文名");
        filedMap.put("flag", "上班中");
        filedMap.put("numFloat", "float");
        filedMap.put("numDouble", "double");
        filedMap.put("numLong", "long");
        ExcelUtil.responseExcel(filename, "用户导出", 500, list, filedMap, response);
    }

    @RequestMapping(value = "/importExcel")
    public ResResult importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        LinkedHashMap<String, String> filedMap = new LinkedHashMap<String, String>();
        filedMap.put("id", "ID");
        filedMap.put("name", "姓名");
        filedMap.put("date", "日期");
        filedMap.put("enName", "英文名");
        filedMap.put("numFloat", "float");
        filedMap.put("numDouble", "double");
        filedMap.put("numLong", "long");
        List<DemoExcel> list = ExcelUtil.excelToList(file.getOriginalFilename(),file.getInputStream(), 0, 0, DemoExcel.class, filedMap);
        for (DemoExcel demoExcel : list) {
            System.out.println(demoExcel.toString());
        }
        return ResResult.success(list);
    }

    @GetMapping("/testException")
    public ResResult testException() {
        System.out.println(1 / 0);
        return ResResult.success();
    }

    @GetMapping("/testException1")
    public ResResult testException1() {
        Integer.parseInt("ssss");
        return ResResult.success();
    }

    /**
     * 测试参数绑定错误异常是否捕获
     * 
     * @param a
     * @return
     */
    @GetMapping("/testException2")
    public ResResult testException2(Date date) {
        System.out.println(date);
        return ResResult.success(date);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("/add")
    public ResResult add(Demo demo) {
        demoService.save(demo);
//        for (int i = 0; i < 10000; i++) {
//            demo.setAcDd("eeeeeeeee_"+i);
//            demo.setDate(new Date());
//            demo.setKey("keykkkkkkk_"+i);
//            demo.setName("namennnnnnnnn_"+i);
//            demoService.save(demo);
//        }
        return ResResult.success();
    }

    @PostMapping("/delete")
    public ResResult delete(@RequestParam Integer id) {
        demoService.deleteById(id);
        return ResResult.success();
    }

    @PostMapping("/update")
    public ResResult update(Demo demo) {
        demoService.update(demo);
        return ResResult.success();
    }

    @GetMapping("/detail")
    public ResResult detail(@RequestParam Integer id) {
        Demo demo = demoService.findById(id);
        return ResResult.success(demo);
    }

    @GetMapping("/list")
    public ResResult list(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        logger.info("ServerIp={}", getServerIp());
        logger.error("UserIp={}", getCliectIp());
        logger.warn("UserIp={}", getCliectIp());
        logger.info("UserIp={}", getCliectIp());
        logger.debug("UserIp={}", getCliectIp());
        logger.trace("UserIp={}", getCliectIp());
        PageInfo<Demo> pageInfo = demoService.findAll(page, size);
        return ResResult.success(pageInfo);
    }
    @Value("${deploy.datasource.username}")
    private String username;
    
    @Value("${deploy.datasource.password}")
    private String password;
    
	@RequestMapping(value = "/exportExcel")
    public ModelAndView export() {
    	
		System.out.println("用户名=" + username + "/n 密码：" + password);
        Map<String, Object> model = new HashMap<>(4);
        model.put("name", "Reven001");
        model.put("age", 18);
        // queryUser()为数据获取的方法
        List<Demo> list = demoService.findAll();
        if (list == null || list.size() == 0) {
            // list为空，会报错
            model.put("demoList", new ArrayList<Demo>(0));
        } else {
            model.put("demoList", list);
        }
        return new ModelAndView(new JxlsExcelView("static/excel/t_template.xls", "demo导出"), model);
    }

    @RequestMapping(value = "/echartData")
    @ResponseBody
    public ModelAndView echart() {
        Map<String, Object> model = new HashMap<>(4);
        model.put("name", "Reven001");
        model.put("age", 18);
        // queryUser()为数据获取的方法
        List<Demo> list = demoService.findAll();
        if (list == null || list.size() == 0) {
            // list为空，会报错
            model.put("demoList", new ArrayList<Demo>(0));
        } else {
            model.put("demoList", list);
        }
        return new ModelAndView(new JxlsExcelView("static/excel/t_template.xls", "demo导出"), model);
    }
}
