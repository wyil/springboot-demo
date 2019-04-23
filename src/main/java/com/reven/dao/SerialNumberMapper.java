package com.reven.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.reven.core.Mapper;
import com.reven.model.entity.SerialNumber;

public interface SerialNumberMapper extends Mapper<SerialNumber> {

    SerialNumber getByRuleId1(@Param("ruleId") Integer ruleId, @Param("year") int year, @Param("month") int month,
            @Param("day") int day);

    SerialNumber getByRuleId2(@Param("ruleId") Integer ruleId, @Param("year") int year, @Param("month") int month);

    SerialNumber getByRuleId3(@Param("ruleId") Integer ruleId, @Param("year") int year);

    SerialNumber getByRuleId4(@Param("ruleId") Integer ruleId);

    void updateSerialNumber(@Param("autoId") Integer autoId, @Param("updateTime") Date updateTime,
            @Param("newMaxSerial") Integer newMaxSerial, @Param("maxSerial") Integer maxSerial);
}