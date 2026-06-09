package com.farmgame.server.common.web;

import com.farmgame.server.common.api.ApiResponse;
import com.farmgame.server.common.api.ErrorCode;
import com.farmgame.server.common.exception.BizException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBizException(BizException ex) {
        return ApiResponse.failure(ex.getErrorCode().code(), ex.getMessage());
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MissingRequestHeaderException.class
    })
    public ApiResponse<Void> handleBadRequest(Exception ex) {
        return ApiResponse.failure(ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception ex) {
        log.error("Unhandled server exception", ex);
        return ApiResponse.failure(ErrorCode.INTERNAL_ERROR);
    }
}
