package com.reven.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

public class HashMapCache {

    private static Map<String, Object> dictionaryCache = new ConcurrentHashMap<>();

//    @Resource
//    private BusinessDictionaryMapper businessDictionaryMapper;

    public Object findByModule(String module) throws Exception {
        if (StringUtils.isEmpty(module)) {
            return null;
        }
        if (!dictionaryCache.containsKey(module)) {
//            List<BusinessDictionary> list = businessDictionaryMapper.findByModule(module);
            String testCache = module+"_sss";
            dictionaryCache.put(module, testCache);
        }

        return dictionaryCache.get(module);
    }

    public static void main(String[] args) throws Exception {
        HashMapCache a=new HashMapCache();
        a.findByModule("01");
        HashMapCache b=new HashMapCache();
        b.findByModule("02");
        
        System.out.println(a.findByModule("02"));
        System.out.println(b.findByModule("01"));
        
    }
}