//package com.damao.controller;
//
//import io.minio.MinioClient;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/file")
//public class FileController {
//
//    @Autowired
//    private MinioClient minioClient;
//
//    @Value("${minio.bucketName}")
//    private String bucketName;
//
//    @PostMapping("/upload")
//    public String upload(@RequestParam("file") MultipartFile file) {
//        // 实现文件切片上传逻辑
//        // ...
//
//        return "Upload success!";
//    }
//}