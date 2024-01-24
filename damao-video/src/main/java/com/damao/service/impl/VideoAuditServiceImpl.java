package com.damao.service.impl;

import com.damao.constant.AuditConstant;
import com.damao.result.exception.BaseException;
import com.damao.utils.CosUtil;
import com.damao.mapper.VideoMapper;
import com.damao.pojo.entity.Video;
import com.damao.service.VideoAuditService;
import com.qcloud.cos.model.ciModel.auditing.ImageAuditingResponse;
import com.qcloud.cos.model.ciModel.auditing.VideoAuditingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VideoAuditServiceImpl implements InitializingBean, VideoAuditService {

    @Autowired
    private CosUtil cosUtil;

    @Autowired
    private VideoMapper videoMapper;

    protected ThreadPoolExecutor executor;
    private final int maximumPoolSize = 8;

    @Override
    public void audit(Video video) {
        executor.submit(() -> {
            try {
                String imageKey = video.getVideoCoverUrl();
                String videoKey = video.getVideoUrl();

                ImageAuditingResponse imageAuditingResponse = cosUtil.sendImageToAudit(imageKey);
                VideoAuditingResponse videoAuditingResponse = cosUtil.sendToAudit(videoKey);
                System.out.println(imageAuditingResponse);
                System.out.println(videoAuditingResponse);
                video.setAuditStatus(AuditConstant.JUDGE_OVER);

                String videoRes = videoAuditingResponse.getJobsDetail().getResult();
                String imageRes = imageAuditingResponse.getResult();

                if (imageRes.equals(AuditConstant.PASSED) && videoRes.equals(AuditConstant.PASSED)) {
                    video.setAuditResult(new Integer(AuditConstant.PASSED));
                } else {
                    video.setAuditResult(new Integer(AuditConstant.NOT_PASSED));
                }
                videoMapper.update(video);
            } catch (RuntimeException e){
                log.info("Error{}",e.getMessage());
                throw new BaseException(e.getMessage());
            }
        });

    }

    public boolean getAuditQueueState() {
        return executor.getTaskCount() < maximumPoolSize;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executor = new ThreadPoolExecutor(5, maximumPoolSize,
                60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
    }

}
