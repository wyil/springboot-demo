package com.reven.service.impl;

import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.reven.core.AbstractService;
import com.reven.dao.DemoMapper;
import com.reven.model.entity.Demo;
import com.reven.service.IDemoService;

/**
 * @author reven
 */
@Service
@Transactional
//@CacheConfig(cacheNames = "demo-cache10min") TODO 默认全局?
public class DemoServiceImpl extends AbstractService<Demo> implements IDemoService {
    @Resource
    private DemoMapper demoMapper;

    @Override
    public void exportExcel(OutputStream out) {
        // TODO
    }

    //cacheNames 缓存的名称，也就是在缓存中有一个叫emp的名字来标识不同的缓存组件。?
    @Override
    // 保证不乱写cacheName，考虑定义枚举类
//    @Cacheable(value = "demo-cache10min", key = "targetClass+'.'+methodName+#page+#size")
    @Cacheable(cacheNames = { "demo-cache10min" }, key = "targetClass+'.'+methodName+#page+#size")
    public PageInfo<Demo> findAll(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Demo> list = super.findAll();
        PageInfo<Demo> pageInfo = new PageInfo<Demo>(list);
        return pageInfo;
    }

}
