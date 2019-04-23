package com.reven.dao;

import org.apache.ibatis.annotations.Param;

import com.reven.core.Mapper;
import com.reven.model.entity.SerialRule;

public interface SerialRuleMapper extends Mapper<SerialRule> {

    SerialRule getByCodeAndDate1(@Param("moduleCode") String moduleCode, @Param("year") int year,
            @Param("month") int month, @Param("day") int day);

    SerialRule getByCodeAndDate2(@Param("moduleCode") String moduleCode, @Param("year") int year,
            @Param("month") int month);

    SerialRule getByCodeAndDate3(@Param("moduleCode") String moduleCode, @Param("year") int year);

    SerialRule getByCodeAndDate4(@Param("moduleCode") String moduleCode);
}