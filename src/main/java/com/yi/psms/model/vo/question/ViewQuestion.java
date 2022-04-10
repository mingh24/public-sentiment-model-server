package com.yi.psms.model.vo.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.yi.psms.model.entity.question.TextFillingQuestion;
import lombok.Data;

@Data
public class ViewQuestion {

    @SerializedName(value = "textFillingQuestion")
    @JsonProperty(value = "textFillingQuestion")
    private TextFillingQuestion textFillingQuestion;

}
