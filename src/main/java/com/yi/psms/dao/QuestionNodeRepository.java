package com.yi.psms.dao;

import com.yi.psms.model.entity.QuestionNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionNodeRepository extends Neo4jRepository<QuestionNode, Long> {

    @Query("MATCH (q:Question) WHERE q.questionId = $questionId RETURN q")
    QuestionNode findByQuestionId(@Param("questionId") Integer questionId);

    @Query("MATCH (q:Question) RETURN q")
    List<QuestionNode> findAll();

}
