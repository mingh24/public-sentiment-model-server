package com.yi.psms.model.vo.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.yi.psms.model.entity.question.NumberBoundaryQuestion;
import lombok.Data;

@Data
public class RoommateIntimacyQuestion {

    @SerializedName(value = "numberBoundaryQuestion")
    @JsonProperty(value = "numberBoundaryQuestion")
    private NumberBoundaryQuestion numberBoundaryQuestion;

}