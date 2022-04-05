package com.yi.psms.model.entity.question;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OptionQuestion extends BaseQuestion{

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
