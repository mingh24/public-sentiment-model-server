package com.yi.psms.model.entity.node;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@Node("Question")
public class QuestionNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property("questionId")
    private Integer questionId;

    @Property("previousQuestionId")
    private Integer previousQuestionId;

    @Property("content")
    private String content;

    @Property("phase")
    private Integer phase;

    public QuestionNode(Integer questionId, Integer previousQuestionId, String content, Integer phase) {
        this.questionId = questionId;
        this.previousQuestionId = previousQuestionId;
        this.content = content;
        this.phase = phase;
    }

}
