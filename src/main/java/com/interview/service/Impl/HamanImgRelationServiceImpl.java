package com.interview.service.Impl;

import com.interview.mapper.HamanImgRelationMapper;
import com.interview.service.HamanImgRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyu
 * @data 2023/11/4 15:14
 */
@Service
public class HamanImgRelationServiceImpl implements HamanImgRelationService {
    @Autowired
    private HamanImgRelationMapper hamanImgRelationMapper;
    @Override
    public int updateOne(String hamanId, String url) {
        return hamanImgRelationMapper.updateOne(hamanId,url);
    }
}
