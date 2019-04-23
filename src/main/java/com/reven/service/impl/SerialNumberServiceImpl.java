package com.reven.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.reven.core.AbstractService;
import com.reven.core.ServiceException;
import com.reven.dao.SerialNumberMapper;
import com.reven.dao.SerialRuleMapper;
import com.reven.model.entity.SerialNumber;
import com.reven.model.entity.SerialRule;
import com.reven.service.ISerialNumberService;

@Service("serialNumberService")
@Transactional(rollbackFor = Exception.class)
public class SerialNumberServiceImpl extends AbstractService<SerialNumber> implements ISerialNumberService {

    @Resource
    private SerialNumberMapper serialNumberMapper;

    @Resource
    private SerialRuleMapper serialRuleMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String newSerialByCode(String moduleCode) throws ServiceException {
        return newSerialByCodeAndDate(moduleCode, new Date());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized String newSerialByCodeAndDate(String moduleCode, Date date) throws ServiceException {
        String number = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);// 获取年份
        int month = cal.get(Calendar.MONTH) + 1;// 获取月份
        int day = cal.get(Calendar.DATE);// 获取日
        number = generatePrepareSerialNumbers(moduleCode, year, month, day);

        if (StringUtils.isEmpty(number)) {
//			throw new ServiceException("error", "没有生成任何序列号，请检查是否在数据库中设置相应类型");
            throw new ServiceException("没有生成任何序列号，请检查是否在数据库中设置相应类型");
        }
        return number;
    }

    private String generatePrepareSerialNumbers(String moduleCode, int year, int month, int day)
            throws ServiceException {
        String result;
        // 当前序列号
        SerialNumber serialNumber = null;
        SerialRule rule = serialRuleMapper.getByCodeAndDate1(moduleCode, 1, 1, 1);
        if (rule != null) {
            serialNumber = serialNumberMapper.getByRuleId1(rule.getSerialRuleId(), year, month, day);
        }
        if (rule == null) {
            rule = serialRuleMapper.getByCodeAndDate2(moduleCode, 1, 1);
            if (rule != null) {
                serialNumber = serialNumberMapper.getByRuleId2(rule.getSerialRuleId(), year, month);
            }
        }
        if (rule == null) {
            rule = serialRuleMapper.getByCodeAndDate3(moduleCode, 1);
            if (rule != null){
                serialNumber = serialNumberMapper.getByRuleId3(rule.getSerialRuleId(), year);
            }
        }
        if (rule == null) {
            rule = serialRuleMapper.getByCodeAndDate4(moduleCode);
            if (rule != null){
                serialNumber = serialNumberMapper.getByRuleId4(rule.getSerialRuleId());
            }
        }

        if (rule == null) {
//                throw new ServiceException("not_setting", "没有相应的序列号规则设置");
            throw new ServiceException("没有模块编码=" + moduleCode + "，相应的序列号规则设置");
        }

        int currentSerial = 0;
        if (serialNumber == null) {
            serialNumber = new SerialNumber();
            serialNumber.setSerialRuleId(rule.getSerialRuleId());
            if (rule.getWithYear() != null && rule.getWithYear()) {
                serialNumber.setSerialYear(year);
            }
            if (rule.getWithMonth() != null && rule.getWithMonth()) {
                serialNumber.setSerialMonth(month);
            }
            if (rule.getWithDay() != null && rule.getWithDay()) {
                serialNumber.setSerialDay(day);
            }
            serialNumber.setCurrentSerial(1);
            serialNumber.setUpdateTime(new Date());
            // 不能防止分布式集群时同时插入
            serialNumberMapper.insert(serialNumber);
        } else {
            serialNumberMapper.updateSerialNumber(serialNumber.getAutoId(), new Date(), serialNumber.getCurrentSerial() + 1,
                    serialNumber.getCurrentSerial());
            currentSerial = serialNumber.getCurrentSerial(); // 存储当前最大值
        }

        int serialLength = rule.getSerialLength(); // 存储当前最大值
        currentSerial = currentSerial + 1;
        if ((currentSerial + "").length() > serialLength) {
            // throw new ServiceException("length_error", "流水号超过长度,请考虑增加长度或者重置流水号");
            throw new ServiceException("流水号超过长度,请考虑增加长度或者重置流水号");
            // currentSerial = 1;// 如果动态数字长度大于模板中的长度 例：模板CF000 currentSerialInt
            // 1000,重置为1
            // 更新数据，重置currentSerial为1
            // 。。。
        }
        StringBuffer serialNum = new StringBuffer();
        if (!StringUtils.isEmpty(rule.getPrefix())) {
            serialNum = serialNum.append(rule.getPrefix());
        }
        if (serialNumber.getSerialYear() != null) {
            String yearStr = year + "";
            if (rule.getYearLength() != null && rule.getYearLength() == 2) {
                serialNum = serialNum.append(yearStr.substring(yearStr.length() - 2, yearStr.length()));
            } else {
                serialNum = serialNum.append(yearStr);
            }
        }
        if (serialNumber.getSerialMonth() != null) {
            serialNum = serialNum.append(String.format("%02d", month));
        }
        if (serialNumber.getSerialDay() != null) {
            serialNum = serialNum.append(String.format("%02d", day));
        }

        String numStr = String.format("%0" + rule.getSerialLength() + "d", currentSerial);
        result = serialNum.append(numStr).toString();
        return result;
    }
}

