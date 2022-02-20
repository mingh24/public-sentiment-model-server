package com.yi.psms.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ArgumentErrorVO {

    @JsonProperty("fieldName")
    private String fieldName;

    @JsonProperty("message")
    private String message;

    public ArgumentErrorVO(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

}
