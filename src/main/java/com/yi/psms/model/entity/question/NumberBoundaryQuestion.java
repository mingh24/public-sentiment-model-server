package com.yi.psms.model.entity.question;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NumberBoundaryQuestion extends BaseQuestion {

    @SerializedName(value = "min")
    private Integer min;

    @SerializedName(value = "max")
    private Integer max;

    @SerializedName(value = "marks")
    private String marks;

}