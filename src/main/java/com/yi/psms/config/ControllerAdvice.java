package com.yi.psms.config;

import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.model.vo.ArgumentErrorVO;
import com.yi.psms.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice implements RequestBodyAdvice {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.OK)
    public ResponseVO handle(HttpRequestMethodNotSupportedException exception) {
        log.warn("request method unsupported: {}", exception.getMethod());
        return new ResponseVO(ResponseStatus.INVALID_PARAMETER, exception.getMessage(), null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.OK)
    public ResponseVO handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.warn("request invalid: {}", exception.getMessage());
        return new ResponseVO(ResponseStatus.INVALID_PARAMETER, exception.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.OK)
    public ResponseVO handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ArgumentErrorVO> argumentErrorVOList = new ArrayList<>();
        List<ObjectError> objectErrorList = exception.getBindingResult().getAllErrors();

        for (ObjectError objectError : objectErrorList) {
            String fieldName = ((FieldError) objectError).getField();
            String message = objectError.getDefaultMessage();
            argumentErrorVOList.add(new ArgumentErrorVO(fieldName, message));
        }

        StringBuilder responseMessage = new StringBuilder("请求参数无效");

        if (argumentErrorVOList.size() > 0) {
            argumentErrorVOList.sort(Comparator.comparing(ArgumentErrorVO::getFieldName));
            ArgumentErrorVO firstError = argumentErrorVOList.get(0);
            responseMessage.append("：").append(firstError.getFieldName()).append("-").append(firstError.getMessage());
        }

        log.warn("request argument error: {}, responseMessage: {}", argumentErrorVOList, responseMessage);
        return new ResponseVO(ResponseStatus.INVALID_PARAMETER, responseMessage.toString(), null);
    }

    @ExceptionHandler(Exception.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.OK)
    public ResponseVO handleException(Exception exception) {
        log.warn("exception: {}", exception.toString());
        return new ResponseVO(ResponseStatus.INTERNAL_SERVER_ERROR, "服务器内部错误", null);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("----- afterBodyRead -----");
        log.info("class name: {}, method name: {}", parameter.getDeclaringClass().getSimpleName(), parameter.getMethod().getName());
        log.info("request parameter: {}", body.toString());
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("----- handleEmptyBody -----");
        return body;
    }
}
