package com.yi.psms.model.vo.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yi.psms.model.entity.node.QuestionNode;
import lombok.Data;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionVO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("questionId")
    private Integer questionId;

    @JsonProperty("previousQuestionId")
    private Integer previousQuestionId;

    @JsonProperty("statementId")
    private Integer statementId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("phase")
    private Integer phase;

    @JsonProperty("questionContent")
    private QuestionContentVO questionContent;

    public QuestionVO(Long id, Integer questionId, Integer previousQuestionId, Integer statementId, String content, Integer phase, QuestionContentVO questionContent) {
        this.id = id;
        this.questionId = questionId;
        this.previousQuestionId = previousQuestionId;
        this.statementId = statementId;
        this.content = content;
        this.phase = phase;
        this.questionContent = questionContent;
    }

    public static QuestionVO buildFromNode(QuestionNode questionNode) {
        if (questionNode == null) {
            return null;
        }

        QuestionContentVO questionContent = QuestionContentVO.buildFromContentString(questionNode.getContent());
        return new QuestionVO(questionNode.getId(), questionNode.getQuestionId(), questionNode.getPreviousQuestionId(), questionNode.getStatementId(), questionNode.getContent(), questionNode.getPhase(), questionContent);
    }

    public static List<QuestionVO> buildListFromNode(List<QuestionNode> questionNodeList) {
        List<QuestionVO> questionList = new ArrayList<>();

        for (val questionNode : questionNodeList) {
            if (questionNode == null) {
                continue;
            }

            QuestionVO question = buildFromNode(questionNode);
            questionList.add(question);
        }

        return questionList;
    }

}
