package com.interview.service.Impl;

import com.interview.mapper.DictMapper;
import com.interview.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyu
 * @data 2023/11/3 14:01
 */
@Service
public class DictServiceImpl implements DictService {
    @Autowired
    private DictMapper dictMapper;
    @Override
    public String dictMate(String storeName) {
        return dictMapper.dictMate(storeName);
    }
}
