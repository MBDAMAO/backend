package com.damao.aspect;

import com.damao.annotation.AutoFill;
import com.damao.constant.AutoFillConstant;
import com.damao.enumeration.OperationType;
import com.damao.aspect.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.damao.mapper.*.*(..)) && @annotation(com.damao.annotation.AutoFill)")
    public void autoFillPointCut(){

    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("填充公共字段( •̀ ω •́ )✧...");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0){
            return;
        }
        Object entity = args[0];
        log.info("sss{}",entity);
        LocalDateTime now = LocalDateTime.now();
        Class<BaseEntity> class_ = BaseEntity.class;
        if (operationType == OperationType.INSERT) {
            try {
                Method setCreateTime = class_.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = class_.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (operationType == OperationType.UPDATE) {
            try {
                Method setUpdateTime = class_.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                setUpdateTime.invoke(entity,now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
