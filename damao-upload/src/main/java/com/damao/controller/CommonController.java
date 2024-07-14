package com.damao.controller;

import com.damao.result.Result;
import com.damao.utils.CosUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("/common")
@RestController
@Slf4j
public class CommonController {

    @Autowired
    private CosUtil cosUtil;

    /**
     * 上传文件到COS，形参名称即为前端传来的文件key
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传{}", file);
        try {
            String originalFilename = file.getOriginalFilename();
            // 截取后缀
            assert originalFilename != null;
            String[] filename = originalFilename.split("\\.");
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String objName = UUID.randomUUID() + extension;
            File input;
            input = File.createTempFile(filename[0], filename[1]);
            file.transferTo(input);
            String filePath = cosUtil.upload(input, objName);
            return Result.success(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件上传失败ಠ_ಠ");
        }
        return Result.error("文件上传失败");
    }
}