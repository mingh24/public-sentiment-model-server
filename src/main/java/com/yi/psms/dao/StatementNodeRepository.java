package com.yi.psms.dao;

import com.yi.psms.model.entity.node.StatementNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface StatementNodeRepository extends Neo4jRepository<StatementNode, Long> {

    StatementNode findByStatementId(Integer statementId);

}
