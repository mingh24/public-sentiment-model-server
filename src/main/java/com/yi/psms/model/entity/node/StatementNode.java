package com.yi.psms.model.entity.node;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDateTime;

@Data
@Node("Statement")
public class StatementNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property("statementId")
    private Integer statementId;

    @Property("content")
    private String content;

    @Property("createdAt")
    private LocalDateTime createdAt;

    @Property("updatedAt")
    private LocalDateTime updatedAt;

}
