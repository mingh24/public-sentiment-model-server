package com.yi.psms.controller;

import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.model.vo.ResponseVO;

public class BaseController {

    protected ResponseVO response() {
        return new ResponseVO(ResponseStatus.SUCCESS, "success", null);
    }

    protected ResponseVO response(Object data) {
        return new ResponseVO(ResponseStatus.SUCCESS, "success", data);
    }

    protected ResponseVO response(String message, Object data) {
        return new ResponseVO(ResponseStatus.SUCCESS, message, data);
    }

    protected ResponseVO failResponse(Integer status, String message) {
        return new ResponseVO(status, message, null);
    }

    protected ResponseVO failResponse(String message) {
        return new ResponseVO(ResponseStatus.FAIL, message, null);
    }

}
