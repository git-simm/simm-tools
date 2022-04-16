package com.simm.web.advice;

import com.simm.common.model.BizException;
import com.simm.common.model.JsonResult;
import io.netty.util.internal.ThrowableUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author simm
 */
@ControllerAdvice
public class MyExceptionHandlerAdvice {
    /**
     * 处理业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    JsonResult handleBizException(BizException ex) {
        JsonResult result = new JsonResult(false);
        result.error(ex.getMessage());
        return result;
    }

    /**
     * 处理业务异常
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    JsonResult handleException(Exception ex) {
        JsonResult result = new JsonResult(false);
        result.error(ThrowableUtil.stackTraceToString(ex));
        return result;
    }
}