package com.yi.psms.model.vo.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FriendItemVO {

    @JsonProperty("studentId")
    @NotNull(message = "好友学号不能为 null")
    private Integer studentId;

    @JsonProperty("intimacy")
    @NotNull(message = "好友亲密度不能为 null")
    private Integer intimacy;

}
