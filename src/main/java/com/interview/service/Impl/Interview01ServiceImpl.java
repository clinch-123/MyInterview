package com.interview.service.Impl;

/**
 * @author zhaoyu
 * @data 2023/11/2 15:02
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.interview.constraints.Interview01Constraints;
import com.interview.service.DictService;
import com.interview.service.Interview01Service;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;


@Service
public class Interview01ServiceImpl implements Interview01Service {
    @Autowired
    private  DictService dictService;
    // 防止重复列表
    private  ArrayList<String> arrayList = new ArrayList<String>();
    @Override
    public void parse(String csvFilePath, int chunkSize, HttpServletResponse response) {
        //初始化输出文件
        initOutFileCsv();
        //解析simple csv
        parseInFileCsv(csvFilePath,chunkSize);
        //释放掉list
        arrayList.clear();
        //输出解析后生成的csv文件
        outFileToWeb(response);


    }

    private void outFileToWeb(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=exported_data.csv");

        File csvFile = new File("exported_data.csv");  // 替换为实际的CSV文件路径

        try (FileInputStream fileInputStream = new FileInputStream(csvFile);
             OutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void parseInFileCsv(String csvFilePath, int chunkSize) {
        try (RandomAccessFile file = new RandomAccessFile(csvFilePath, "r");
             FileChannel channel = file.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(chunkSize);
            StringBuilder currentLine = new StringBuilder();
            boolean skipFirstLine = true;  // 用于跳过表头

            while (channel.read(buffer) > 0) {
                buffer.flip();
                Charset charset = Charset.forName("UTF-8"); // 使用指定的字符编码
                CharBuffer charBuffer = charset.decode(buffer);
                while (charBuffer.hasRemaining()) {
                    char c = charBuffer.get();
                    if (c == '\n') {
                        if (skipFirstLine) {
                            skipFirstLine = false;
                        } else {
                            System.out.println("===line==");
                            processCSVLine(currentLine.toString());
                        }
                        currentLine.setLength(0);
                    } else {
                        currentLine.append(c);
                    }
                }

                buffer.clear();
            }

            if (currentLine.length() > 0) {
                processCSVLine(currentLine.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化最终结果的csv文件
     */
    private void initOutFileCsv() {
        // 创建导出csv
        String[] headers = {"taskId", "storeName", "storeId", "tag"};
        //如果存在exported_data.csv文件则先删除
        String csvFileName = Interview01Constraints.EXPORTED_CSV;
        File file = new File(csvFileName);
        file.delete();
        try (FileWriter fileWriter = new FileWriter(csvFileName);
             PrintWriter writer = new PrintWriter(fileWriter)) {
            // 写入CSV文件头
            for (int i = 0; i < headers.length; i++) {
                writer.print(headers[i]);
                if (i < headers.length - 1) {
                    writer.print(",");
                } else {
                    writer.println();
                }
            }
            System.out.println("CSV 文件已成功导出到 " + csvFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将每一行进行解析
     * @param line
     * @throws IOException
     */
    private  void processCSVLine(String line) throws IOException {

        // 使用逗号分割CSV行
        String[] fields = splitCSVLine(line);
        String taskId = fields[1];
        String dataString = fields[4];

        String replace = dataString.replace("\"\"", "\"");
        String result = replace.substring(1, replace.length() - 1);

        JsonObject dataJson = JsonParser.parseString(result).getAsJsonObject();
        if (dataJson.get("result") == null)return;
        JsonArray dataArray = dataJson.get("result").getAsJsonObject().get("data").getAsJsonArray();
        for (JsonElement element : dataArray) {
            if (element == null ||
                    element.getAsJsonObject().get("data") == null ||
                    element.getAsJsonObject().get("data").getAsJsonObject().get("storeName") == null ||
                    element.getAsJsonObject().get("data").getAsJsonObject().get("storeId") == null)continue;
            String storeName = element.getAsJsonObject().get("data").getAsJsonObject().get("storeName").getAsString();
            String storeId = element.getAsJsonObject().get("data").getAsJsonObject().get("storeId").getAsString();
            boolean isContains = arrayList.contains(storeId);
            if (isContains){
                // 如果已经存在，那么就跳过
                continue;
            }else {
                //如果不存在，那么就添加到list里，执行业务逻辑
                arrayList.add(storeId);
                // 做字典匹配
                String tag = dictService.dictMate(storeName);
                System.out.println("taskId:"+taskId
                                + "\t\t\t"+ "storeName:" + storeName
                        + "\t\t\t" + "storeId:"+storeId
                        + "\t\t\t" + "tag:"+tag);
                //添加到csv文件中
                addDataToOutFile(taskId,storeName,storeId,tag);
            }

        }


//        System.out.println("task_id: " + taskId + ',' + "data: " + dataString);
    }

    private void addDataToOutFile(String taskId, String storeName, String storeId, String tag) {
        // 要追加到的CSV文件
        String csvFileName = "exported_data.csv";

        // 要追加的数据
        String[] newData = {taskId, storeName, storeId, tag};

        try (FileWriter fileWriter = new FileWriter(csvFileName, true);
             PrintWriter writer = new PrintWriter(fileWriter)) {
            for (int i = 0; i < newData.length; i++) {
                writer.print(newData[i]);
                if (i < newData.length - 1) {
                    writer.print(",");
                } else {
                    writer.println();
                }
            }

            System.out.println("数据已成功追加到 " + csvFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义CSV拆分函数
     * @param line
     * @return
     */
    private  String[] splitCSVLine(String line) {
        String[] fields = new String[30];
        int index = 0;
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                currentField.append(c);
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields[index] = currentField.toString();
                currentField.setLength(0);
                index++;
            } else {
                currentField.append(c);
            }
        }

        if (index < 30) {
            fields[index] = currentField.toString();
        }

        return fields;
    }



}
