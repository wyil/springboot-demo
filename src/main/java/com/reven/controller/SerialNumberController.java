package com.reven.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reven.controller.common.ResResult;
import com.reven.service.ISerialNumberService;


/**
 * @ClassName:  SerialNumberController   
 * @Description: 序列号demo Controller 
 * @author reven
 * @date   2019年3月25日
 */
@RestController
@RequestMapping("/serial/number")
public class SerialNumberController {
    @Resource
    private ISerialNumberService serialNumberService;

    @RequestMapping("/getSn")
    public ResResult getSn() {
        String sn=serialNumberService.newSerialByCode("model_order");
        return ResResult.success(sn);
    }

    @RequestMapping("/getSnByDate")
    public ResResult getSnByDate() {
        String sn=serialNumberService.newSerialByCodeAndDate("model_order",DateUtils.addDays(new Date(), -30));
        return ResResult.success(sn);
    }
 
}
