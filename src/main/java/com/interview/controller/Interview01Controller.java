package com.interview.controller;


import com.interview.common.R;
import com.interview.constraints.Interview01Constraints;
import com.interview.service.Interview01Service;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;


/**
 * @author zhaoyu
 * @data 2023/10/30 21:28
 *
 * 解析店铺名称&匹配店铺名称
 */
@Controller
@RequestMapping("interview01")
public class Interview01Controller {
    @Autowired
    private Interview01Service interview01Service;

    @GetMapping("interview01")
    public String interview01(HttpServletResponse response) throws IOException {
        String csvFilePath = Interview01Constraints.SAMPLE_CSV_FILE_PATH;  // 替换为实际文件路径
        int chunkSize = Interview01Constraints.CHUNK_SIZE;  // 设置每块大小（字节），可以根据需要调整
        interview01Service.parse(csvFilePath, chunkSize,response);

        return "解析成功，请查看下载的文件：" + Interview01Constraints.EXPORTED_CSV;
    }
}
