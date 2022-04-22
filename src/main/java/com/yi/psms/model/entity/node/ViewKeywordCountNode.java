package com.yi.psms.model.entity.node;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDateTime;

@Data
@Node("ViewKeywordCount")
public class ViewKeywordCountNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property("questionId")
    private Integer questionId;

    @Property("keywordCount")
    private String keywordCount;

    @Property("createdAt")
    private LocalDateTime createdAt;

    @Property("updatedAt")
    private LocalDateTime updatedAt;

    public ViewKeywordCountNode(Integer questionId, String keywordCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.questionId = questionId;
        this.keywordCount = keywordCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
