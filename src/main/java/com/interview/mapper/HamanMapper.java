package com.interview.mapper;

import com.interview.entity.Haman;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhaoyu
 * @data 2023/11/4 15:15
 */
@Mapper
public interface HamanMapper {
    List<Haman> getBreakPriceUrls(String platform, int count, String batchNo);

}
