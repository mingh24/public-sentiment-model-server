package com.yi.psms.model.vo.statement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yi.psms.model.entity.node.StatementNode;
import lombok.Data;

@Data
public class StatementItemVO {

    @JsonProperty("statementId")
    private Integer statementId;

    @JsonProperty("content")
    private String content;

    public StatementItemVO(Integer statementId, String content) {
        this.statementId = statementId;
        this.content = content;
    }

    public static StatementItemVO buildFromNode(StatementNode statementNode) {
        return new StatementItemVO(statementNode.getStatementId(), statementNode.getContent());
    }

}
