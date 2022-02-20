package com.yi.psms.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseVO {

    public static final Long serialVersionUID = 1L;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Object data;

    public ResponseVO(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
