package com.interview.controller;

import com.interview.common.R;
import com.interview.constraints.Interview02Constraints;
import com.interview.entity.Haman;
import com.interview.entity.ImageVo;
import com.interview.service.HamanImgRelationService;
import com.interview.service.HamanService;
import icu.xuyijie.base64utils.Base64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

/**
 * @author zhaoyu
 * @data 2023/11/4 14:22
 */
@RestController
@RequestMapping("interview02")
public class Interview02Controller {
    @Autowired
    private HamanService hamanService;
    @Autowired
    private HamanImgRelationService hamanImgRelationService;
    @GetMapping("/breakPriceUrls")
    public R interview01(String platform,int count,String batchNo){
        //获取haman数据
        List<Haman> hamanList = hamanService.getBreakPriceUrls(platform,count,batchNo);

        return R.ok(hamanList);
    }

    @PostMapping("/uploadImg")
    public R uploadImage(@RequestBody ImageVo imageVo) {
        // 在这里处理接收到的数据
        String imgBase64 = imageVo.getImg();
        // 破价链接的id
        String hamanId = imageVo.getId();

        // 这里可以根据需要处理图片和ID
        String savePath = Interview02Constraints.SAVE_IMG_PATH + System.currentTimeMillis() ;
        // 图片保存的路径
        String path = Base64Utils.generateFile(imgBase64, savePath);
        //拼接一个nginx图片访问地址
        File localImgFile = new File(path);
        String localImgFileName = localImgFile.getName();
        String url =  Interview02Constraints.NGINX_EXPOSE_PATH + localImgFileName;
        // 地址落库
        int result = hamanImgRelationService.updateOne(hamanId,url);
        if (result == 0){
            //删除上传的img
            localImgFile.delete();
            //hamanId已经过期，需要重新拉取
            return R.errorMsg("上传失败，已经过期，下次拉取后请在30分钟之内上传图片，需要重新拉取");
        }
        // 返回适当的响应
        return R.ok(url);
    }


}
