package com.yi.psms.model.vo.opinion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpinionCountVO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("count")
    private Integer count;

    public OpinionCountVO(String name, Integer count) {
        this.name = name;
        this.count = count;
    }

}
