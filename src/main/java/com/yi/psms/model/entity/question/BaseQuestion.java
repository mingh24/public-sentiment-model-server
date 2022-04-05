package com.yi.psms.model.entity.question;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BaseQuestion {

    @SerializedName(value = "content")
    private String content;

}
