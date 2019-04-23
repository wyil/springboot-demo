package com.reven.service;

import java.util.Date;

import com.reven.core.IBaseService;
import com.reven.core.ServiceException;
import com.reven.model.entity.SerialNumber;

public interface ISerialNumberService extends IBaseService<SerialNumber> {
	
	/**
	 * 根据模块code生成序列号，如果序列号规则包含日期相关的部分，默认根据当前时间生成序列号
	 * @param moduleCode 模块
	 * @return 流水号
	 */
	String  newSerialByCode(String moduleCode) throws ServiceException;
	 
	/**
	 * 根据模块code和指定的日期生成序列号。如果序列号规则包含日期相关的部分，根据指定的日期生成序列号
	 * @param moduleCode 模块
	 * @param date 指定日期
	 * @return 流水号
	 */
	String newSerialByCodeAndDate(String moduleCode,Date date) throws ServiceException;
	

}
