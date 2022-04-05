package com.yi.psms.model.vo.question;

import com.google.gson.annotations.SerializedName;
import com.yi.psms.model.entity.question.NumberBoundaryQuestion;
import lombok.Data;

@Data
public class AttitudeQuestionVO {

    @SerializedName(value = "numberBoundaryQuestion")
    private NumberBoundaryQuestion numberBoundaryQuestion;

}
