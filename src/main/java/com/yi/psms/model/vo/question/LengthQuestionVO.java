package com.yi.psms.model.vo.question;

import com.google.gson.annotations.SerializedName;
import com.yi.psms.model.entity.question.OptionQuestion;
import lombok.Data;

@Data
public class LengthQuestionVO {

    @SerializedName(value = "attitudeThreshold")
    private Integer attitudeThreshold;

    @SerializedName(value = "optionQuestion")
    private OptionQuestion optionQuestion;

}
