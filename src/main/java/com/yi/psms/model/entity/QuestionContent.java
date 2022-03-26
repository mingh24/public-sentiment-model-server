package com.yi.psms.model.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class QuestionContent {

    @SerializedName(value = "basicQuestion")
    BasicQuestion basicQuestion;

    @SerializedName(value = "priceQuestion")
    ExtraQuestion priceQuestion;

    @SerializedName(value = "lengthQuestion")
    ExtraQuestion lengthQuestion;

}
