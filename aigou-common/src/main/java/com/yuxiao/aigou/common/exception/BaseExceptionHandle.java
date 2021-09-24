package com.yuxiao.aigou.common.exception;

import com.yuxiao.aigou.common.entry.Result;
import com.yuxiao.aigou.common.entry.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 公共异常处理类
 *  使用@ControllerAdvice注解  会作用在所有@RequestMapping注解的方法上
 */
@ControllerAdvice
public class BaseExceptionHandle {
    /**
     * 异常处理  作用在所有@RequestMapping注解的方法上
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }
}
