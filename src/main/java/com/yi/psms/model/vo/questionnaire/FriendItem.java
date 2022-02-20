package com.yi.psms.model.vo.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FriendItem {

    @JsonProperty("name")
    @NotBlank(message = "朋友姓名不能为 null 或 空值")
    private String name;

    @JsonProperty("intimacy")
    @NotNull(message = "朋友亲密度不能为 null")
    @Range(min = 1, max = 10, message = "朋友亲密度值不合法")
    private Integer intimacy;

}
