package com.damao.aspect;


import com.damao.constant.RedisNameConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class AutoIncreaseViewAspect {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Pointcut("execution(* com.damao.controller.VideoController.getContentById(..))")
    public void autoIncreaseViewPointCut(){

    }

    @After("autoIncreaseViewPointCut()")
    public void autoFill(JoinPoint joinPoint){
        Long id = (Long) joinPoint.getArgs()[0];
        //按照视频id对views进行计数，不存在则会在redis中新建一个
        redisTemplate.opsForZSet().incrementScore(RedisNameConstant.VIDEO_DAILY_VIEW_RANK,id,1);
        log.info("自动记录浏览量( •̀ ω •́ )✧...");
    }
}
