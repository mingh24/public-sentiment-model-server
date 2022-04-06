package com.yi.psms.model.vo.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.yi.psms.model.entity.question.OptionQuestion;
import lombok.Data;

@Data
public class PriceQuestionVO {

    @SerializedName(value = "attitudeThreshold")
    @JsonProperty(value = "attitudeThreshold")
    private Integer attitudeThreshold;

    @SerializedName(value = "optionQuestion")
    @JsonProperty(value = "optionQuestion")
    private OptionQuestion optionQuestion;

}
