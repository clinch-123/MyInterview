package com.interview.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author zhaoyu
 * @data 2023/11/2 15:02
 */

public interface Interview01Service {
    void parse(String csvFilePath, int chunkSize, HttpServletResponse response);






}
