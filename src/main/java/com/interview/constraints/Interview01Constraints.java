package com.interview.constraints;

/**
 * @author zhaoyu
 * @data 2023/11/5 10:01
 *
 * 静态常量配置
 */
public interface Interview01Constraints {
    // sample.csv 文件路径
    public static final String SAMPLE_CSV_FILE_PATH = "D:\\myProject\\tjise\\interview\\interview\\src\\main\\resources\\file\\sample.csv";
    // 设置每块大小（字节），可以根据需要调整
    public static final int CHUNK_SIZE = 1024 * 1024 * 10;
    // 最后生成的csv文件名
    public static final String EXPORTED_CSV = "exported_data.csv";



}
