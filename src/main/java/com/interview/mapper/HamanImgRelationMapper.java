package com.interview.mapper;

import com.interview.entity.Haman;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhaoyu
 * @data 2023/11/4 15:15
 */
@Mapper
public interface HamanImgRelationMapper {
    void insertHamanIdList(List<Haman> hamanList);

    void deleteByHamanId(List<Haman> list);

    int updateOne(String hamanId, String url);
}
