package com.yi.psms.model.vo.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.util.List;

@Data
public class SubmissionVO {

    @JsonProperty("studentId")
    @NotNull(message = "学号不能为 null", groups = {Essential.class, Advanced.class})
    private Integer studentId;

    @JsonProperty("classmateIntimacy")
    @NotNull(message = "班级同学亲密度不能为 null", groups = {Essential.class})
    private Integer classmateIntimacy;

    @JsonProperty("roommateIntimacy")
    @NotNull(message = "舍友亲密度不能为 null", groups = {Essential.class})
    private Integer roommateIntimacy;

    @JsonProperty("friendItemList")
    @NotEmpty(message = "好友列表不能为 null 或 空值", groups = {Essential.class})
    @Size(min = 1, max = 3, message = "好友个数必须在 1-3 之间")
    private List<@Valid FriendItemVO> friendItemList;

    @JsonProperty("opinionItem")
    @NotNull(message = "意见不能为 null", groups = {Essential.class, Advanced.class})
    @Valid
    private OpinionItemVO opinionItem;

    public interface Essential extends Default {
    }

    public interface Advanced extends Default {
    }

}
