package com.yi.psms.dao;

import com.yi.psms.model.entity.node.ViewKeywordCountNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ViewKeywordCountNodeRepository extends Neo4jRepository<ViewKeywordCountNode, Long> {

    ViewKeywordCountNode findFirstByQuestionIdOrderByUpdatedAtDesc(Integer questionId);

}
