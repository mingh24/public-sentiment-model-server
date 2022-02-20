package com.yi.psms.model.entity;

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

    @Property("content")
    private String content;

    public QuestionNode(Integer questionId, String content) {
        this.questionId = questionId;
        this.content = content;
    }

}
