package com.interview.service;

import com.interview.entity.Haman;

import java.util.List;

/**
 * @author zhaoyu
 * @data 2023/11/4 15:13
 */
public interface HamanService {
    List<Haman> getBreakPriceUrls(String platform, int count, String batchNo);
}
