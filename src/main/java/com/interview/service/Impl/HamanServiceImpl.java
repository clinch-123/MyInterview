package com.interview.service.Impl;

import com.interview.entity.Haman;
import com.interview.mapper.HamanImgRelationMapper;
import com.interview.mapper.HamanMapper;
import com.interview.service.HamanService;
import com.interview.utils.MyScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhaoyu
 * @data 2023/11/4 15:13
 */
@Service
public class HamanServiceImpl implements HamanService {
    @Autowired
    private HamanMapper hamanMapper;
    @Autowired
    private HamanImgRelationMapper hamanImgRelationMapper;

    @Autowired
    private MyScheduledTask myScheduledTask;

    @Override
    public List<Haman> getBreakPriceUrls(String platform, int count, String batchNo) {
        List<Haman> hamanList = hamanMapper.getBreakPriceUrls(platform,count,batchNo);
        // 更新关联表
        hamanImgRelationMapper.insertHamanIdList(hamanList);
        //创建一个定时任务，30分钟后释放掉还未上传图片的haman
        myScheduledTask.triggerScheduledTaskWithDelay(hamanList);
        return hamanList;
    }
}
