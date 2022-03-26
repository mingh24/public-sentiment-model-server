package com.yi.psms.model.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BasicQuestion {

    @SerializedName(value = "content")
    private String content;

    @SerializedName(value = "option")
    private Option option;

    @Data
    public static class Option {

        @SerializedName(value = "min")
        private Integer min;

        @SerializedName(value = "max")
        private Integer max;

        @SerializedName(value = "marks")
        private String marks;

    }

}