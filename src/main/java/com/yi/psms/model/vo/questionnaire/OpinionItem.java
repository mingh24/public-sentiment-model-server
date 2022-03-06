package com.yi.psms.model.vo.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class OpinionItem {

    @JsonProperty("questionId")
    @NotNull(message = "问题ID不能为 null")
    private Integer questionId;

    @JsonProperty("attitude")
    @NotNull(message = "态度值不能为 null")
    @Range(min = 0, max = 1, message = "态度值不合法")
    private Integer attitude;

    @JsonProperty("opinion")
    private String opinion;

}
