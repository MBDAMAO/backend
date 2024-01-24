package com.damao.utils;

import com.damao.result.exception.BaseException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.ciModel.auditing.*;
import com.qcloud.cos.region.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Data
@AllArgsConstructor
@Slf4j
public class CosUtil {

    private String baseUrl;
    private String accessKey;
    private String secretKey;
    private String regionName;
    private String bucketName;
    private String folderPrefix;

    /**
     * 文件上传
     *
     * @return String
     */

//    private COSClient cosClient;
    public String upload(File file, String objectName) {
        BasicCOSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        String key = folderPrefix + "/" + objectName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(baseUrl)
                .append(folderPrefix)
                .append("/")
                .append(objectName);
        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }

    public VideoAuditingResponse sendToAudit(String videoKey) {
        BasicCOSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);

        VideoAuditingRequest request = new VideoAuditingRequest();
        request.setBucketName(bucketName);
        request.getInput().setObject("files/" + videoKey);
        //request.getConf().setDetectType("Porn, Ads");
        request.getConf().getSnapshot().setCount("10");
        request.getConf().getSnapshot().setMode("Interval");
        request.getConf().getSnapshot().setTimeInterval("10");
        VideoAuditingResponse response = null;
        try {
            response = cosClient.createVideoAuditingJob(request);
        } catch (RuntimeException e) {
            throw new BaseException(e.getMessage());
        }

        while (response.getJobsDetail().getResult() == null) {
            try {
                Thread.sleep(10000); // 每十秒看一次结果
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            response = checkVideoStatus(response.getJobsDetail().getJobId());
            log.info("response{}", response);
        }
        return response;
    }

    public ImageAuditingResponse sendImageToAudit(String imageKey) {
        BasicCOSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);

        ImageAuditingRequest request = new ImageAuditingRequest();
        request.setBucketName(bucketName);
        request.setObjectKey("files/" + imageKey);
        ImageAuditingResponse response = null;
        try {
            response = cosClient.imageAuditing(request);
        } catch (RuntimeException e) {
            throw new BaseException(e.getMessage());
        }
        while (response.getResult() == null) {
            try {
                Thread.sleep(10000); // 每十秒看一次结果
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            response = checkImageStatus(response.getJobId());
            log.info("response{}", response);
        }
        return response;
    }

    public VideoAuditingResponse checkVideoStatus(String jobId) {
        BasicCOSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        VideoAuditingRequest request = new VideoAuditingRequest();
        request.setBucketName(bucketName);
        request.setJobId(jobId);
        return cosClient.describeAuditingJob(request);
    }

    public ImageAuditingResponse checkImageStatus(String jobId) {
        BasicCOSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        DescribeImageAuditingRequest request = new DescribeImageAuditingRequest();
        request.setBucketName(bucketName);
        request.setJobId(jobId);
        return cosClient.describeAuditingImageJob(request);
    }
}
