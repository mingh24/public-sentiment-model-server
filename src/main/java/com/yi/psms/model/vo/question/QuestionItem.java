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

    @JsonProperty("phase")
    private Integer phase;

    public QuestionItem(Long id, Integer questionId, String content, Integer phase) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.phase = phase;
    }

    public static QuestionItem buildFromNode(QuestionNode questionNode) {
        if (questionNode == null) {
            return null;
        }

        return new QuestionItem(questionNode.getId(), questionNode.getQuestionId(), questionNode.getContent(), questionNode.getPhase());
    }

    public static List<QuestionItem> buildListFromNode(List<QuestionNode> questionNodeList) {
        List<QuestionItem> questionItemList = new ArrayList<>();

        for (QuestionNode questionNode : questionNodeList) {
            if (questionNode == null) {
                continue;
            }

            QuestionItem questionItem = new QuestionItem(questionNode.getId(), questionNode.getQuestionId(), questionNode.getContent(), questionNode.getPhase());
            questionItemList.add(questionItem);
        }

        return questionItemList;
    }

}
