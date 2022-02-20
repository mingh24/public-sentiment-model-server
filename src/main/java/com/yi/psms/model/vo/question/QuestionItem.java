package com.yi.psms.model.vo.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yi.psms.model.entity.QuestionNode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionItem {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("questionId")
    private Integer questionId;

    @JsonProperty("content")
    private String content;

    public QuestionItem(Long id, Integer questionId, String content) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
    }

    public static QuestionItem buildFromNode(QuestionNode questionNode) {
        return new QuestionItem(questionNode.getId(), questionNode.getQuestionId(), questionNode.getContent());
    }

    public static List<QuestionItem> buildListFromNode(List<QuestionNode> questionNodeList) {
        List<QuestionItem> questionItemList = new ArrayList<>();

        for (QuestionNode questionNode : questionNodeList) {
            QuestionItem questionItem = new QuestionItem(questionNode.getId(), questionNode.getQuestionId(), questionNode.getContent());
            questionItemList.add(questionItem);
        }

        return questionItemList;
    }

}
