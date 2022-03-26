package com.yi.psms.model.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ExtraQuestion {

    @SerializedName(value = "attitudeThreshold")
    private Integer attitudeThreshold;

    @SerializedName(value = "content")
    private String content;

    @SerializedName(value = "option")
    private List<Option> option;

    @Data
    public static class Option {

        @SerializedName(value = "optionKey")
        private String optionKey;

        @SerializedName(value = "optionValue")
        private String optionValue;

    }

}
