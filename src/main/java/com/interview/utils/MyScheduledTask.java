package com.interview.utils;

import com.interview.constraints.Interview02Constraints;
import com.interview.entity.Haman;
import com.interview.mapper.HamanImgRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Service
public class MyScheduledTask {
    @Autowired
    private HamanImgRelationMapper hamanImgRelationMapper;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public void triggerScheduledTaskWithDelay(List<Haman> list) {
        scheduledExecutorService.schedule(() -> {
            // 在这里执行需要触发的定时任务的逻辑
            System.out.println("===开始删除");
            hamanImgRelationMapper.deleteByHamanId(list);
            System.out.println("手动触发定时任务，"+ Interview02Constraints.SCHEDULED_TASK_RUNTIME +"秒后执行"+ list);
        }, Interview02Constraints.SCHEDULED_TASK_RUNTIME, TimeUnit.SECONDS);
    }

    // 记得在应用程序关闭时关闭ScheduledExecutorService
    public void shutdownScheduledExecutorService() {
        scheduledExecutorService.shutdown();
    }
}
