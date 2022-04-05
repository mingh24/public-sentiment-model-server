package com.yi.psms.model.vo.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

@Data
public class SubmissionVO {

    @JsonProperty("studentId")
    @NotNull(message = "学号不能为 null", groups = {Essential.class, Advanced.class})
    private Integer studentId;

    @JsonProperty("classmateIntimacy")
    @NotNull(message = "班级同学亲密度不能为 null", groups = {Essential.class})
    @Range(min = 1, max = 10, message = "班级同学亲密度值不合法", groups = {Essential.class})
    private Integer classmateIntimacy;

    @JsonProperty("roommateIntimacy")
    @NotNull(message = "宿舍同学亲密度不能为 null", groups = {Essential.class})
    @Range(min = 1, max = 10, message = "宿舍同学亲密度值不合法", groups = {Essential.class})
    private Integer roommateIntimacy;

    @JsonProperty("friendItemList")
    @NotEmpty(message = "朋友关系列表不能为 null 或 空值", groups = {Essential.class})
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
