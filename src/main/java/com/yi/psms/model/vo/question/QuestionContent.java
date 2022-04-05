package com.yi.psms.model.vo.question;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class QuestionContent {

    @SerializedName(value = "attitudeQuestion")
    AttitudeQuestionVO attitudeQuestionVO;

    @SerializedName(value = "priceQuestion")
    PriceQuestionVO priceQuestionVO;

    @SerializedName(value = "lengthQuestion")
    LengthQuestionVO lengthQuestionVO;

    // TODO 看法问题

}
