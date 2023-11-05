package com.interview.constraints;

/**
 * @author zhaoyu
 * @data 2023/11/5 10:01
 *
 * 静态常量配置
 */
public interface Interview02Constraints {
    // 图片保存到nginx目录下的地址
    public static final String SAVE_IMG_PATH = "D:/soft/nginx-1.23.1/html/image/";
    // nginx暴露出去的目录的url
    public static final String NGINX_EXPOSE_PATH = "http://localhost/image/";
    // 定时任务执行时间(秒)
    public static final int SCHEDULED_TASK_RUNTIME = 20;

}
