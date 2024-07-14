package com.damao.handler;


import com.damao.result.Result;
import com.damao.result.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;


/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     */
    @ExceptionHandler
    public Result<?> exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     *  捕获参数异常@RequestBody
     */
    @ExceptionHandler
    public Result<?> exceptionHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String message = bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));
        log.error("异常信息：{}", message);
        return Result.error(message);
    }

    /**
     * 捕获参数异常
     */
    @ExceptionHandler(value = BindException.class)
    public Result<?> exceptionHandler(BindException e){
        String message = e.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));
        log.error("异常信息：{}", message);
        return Result.error(message);
    }

    /**
     * 捕获参数异常@RequestParam
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public Result<?> methodArgumentNotValid(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        log.error("异常信息：{}", message);
        return Result.error(message);
    }

}
