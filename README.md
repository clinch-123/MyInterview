# 验证方法

## 一、解析店铺名称&匹配店铺名称

1、导入interview.sql，我将标签词库1026.xlsx，这个字典导入到mysql里了，因为我认为字典的变更程度很小。并且数据很大，需要做快速检索，就放到mysql数据库里了。

![image-20231105110154886](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133220.png)

2、我是采用文件流的形式 分块每次10M~100M读取sample.csv文件，因为这个文件可能有30G

![image-20231105110245855](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133222.png)

3、先逐行读取sample.csv，得到一行后使用逗号分割CSV行，得到每列的数据，然后用gson给data中的json做解析

```
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
```

4、店铺去重我用的是一个list，将每次解析出来的店铺id放list里，每次先判断是否存在这个id，如果存在就说明已经存在这个店铺了，就跳过，不存在就添加到list，防止下次添加重复的店铺，这个list最后在程序结束要做一个clear

![image-20231105110347699](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133223.png)

![image-20231105110436529](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133224.png)

4、字典匹配我写了一个sql做的匹配，保证先匹配3个关键词都有的 再匹配2个关键词都有的

5、然后创建了一个新的exported_data.csv，最后再把它导到浏览器上

![image-20231105110854882](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133225.png)

6、启动springboot

7、浏览器访问

http://localhost:8080/interview01/interview01

会下载最后生成的csv结果文件

![image-20231105111637677](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133226.png)

![image-20231105111655069](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133228.png)

## 二、拉取破价链接&截图上传

1、导入interview.sql，我将haman_2023102801.xlsx导入到数据库了，因为做列表提取更方便写sql，还创建了一个，haman_img_relation表

![image-20231105111744781](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133229.png)

![image-20231105111853077](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133230.png)

2、思路就是，每次拉取10条haman，将这10条的hamanid插入到haman_img_relation，然后每次拉取的时候就判断如果haman_img_relation里已经存在了hamanid，就不在拉取了，但是因为会出现有的截图没有上传，那么就要进行释放，我采用的时候每次设置一个定时任务，30分钟以后，删除掉当时拉去的10条对应到haman_img_relation表且img_url不为空的数据，把这些数据就清空即可。

3、开启本地nginx，暴露一个目录

![image-20231105112240403](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133231.png)

4、这个参数可以设置定时任务清空haman_img_relation的时间，因为要验证，我就把时间设置了20秒，这就是说客户端需要20秒之内把图片都上传，如果没有上传那么未上传图片的haman就会释放掉，别人就可以拉取了。可以根据实际进行设计

![image-20231105112331613](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133232.png)

5、启动springboot

6、拉取破价链接

访问

http://localhost:8080/interview02/breakPriceUrls?platform=%E4%BA%AC%E4%B8%9C&count=10&batchNo=2023102801

![image-20231105112647971](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133233.png)

可以多次访问，返回不同的破价链接

![image-20231105112728190](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133234.png)

![image-20231105113408023](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051134666.png)

![image-20231105113418891](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051134667.png)

7、上传图片postman，这里我随机选了张图片转成了base64用作测试

localhost:8080/interview02/uploadImg

![image-20231105112938570](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133236.png)

访问这个返回的链接，可以访问

![image-20231105112957058](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133237.png)

查看nginx暴露的目录

![image-20231105113206646](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133238.png)

查看数据库，已经保存上了，那么下次拉取破价链接就不会再拉取这个haman了

![image-20231105113105718](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133239.png)

如果已经过了上传图片的有效时间

![image-20231105113314631](https://typora-images-1307135242.cos.ap-beijing.myqcloud.com/images/202311051133240.png)