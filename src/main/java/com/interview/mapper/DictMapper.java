package com.interview.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhaoyu
 * @data 2023/11/3 14:03
 */
@Mapper
public interface DictMapper {
    String dictMate(String storeName);
}
