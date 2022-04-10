package com.yi.psms.model.vo.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class QuestionContentVO {

    @SerializedName(value = "attitudeQuestion")
    @JsonProperty(value = "attitudeQuestion")
    AttitudeQuestionVO attitudeQuestion;

    @SerializedName(value = "priceQuestion")
    @JsonProperty(value = "priceQuestion")
    PriceQuestionVO priceQuestion;

    @SerializedName(value = "lengthQuestion")
    @JsonProperty(value = "lengthQuestion")
    LengthQuestionVO lengthQuestion;

    @SerializedName(value = "viewQuestion")
    @JsonProperty(value = "viewQuestion")
    ViewQuestion viewQuestion;

    public static QuestionContentVO buildFromContentString(String content) {
        Gson gson = new Gson();
        return gson.fromJson(content, QuestionContentVO.class);
    }

}
