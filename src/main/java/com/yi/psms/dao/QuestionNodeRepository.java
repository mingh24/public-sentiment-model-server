package com.yi.psms.dao;

import com.yi.psms.model.entity.QuestionNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface QuestionNodeRepository extends Neo4jRepository<QuestionNode, Long> {

    @Query("MATCH (q:Question) WHERE q.questionId = $questionId RETURN q")
    QuestionNode findByQuestionId(@Param("questionId") Integer questionId);

    @Query("MATCH (q:Question) RETURN q")
    List<QuestionNode> findAll();

    @Async
    @Query("MATCH (:Student)-[r:OPINION]->(q:Question) WHERE q.questionId = $questionId AND r.attitude = $attitude RETURN count(*)")
    CompletableFuture<Integer> countByQuestionIdAndAttitude(@Param("questionId") Integer questionId, @Param("attitude") Integer attitude);

    @Async
    @Query("MATCH (:Student)-[r:OPINION]->(q:Question) WHERE q.questionId = $questionId AND r.priceOption = $priceOption RETURN count(*)")
    CompletableFuture<Integer> countByQuestionIdAndPriceOption(@Param("questionId") Integer questionId, @Param("priceOption") String priceOption);

    @Async
    @Query("MATCH (:Student)-[r:OPINION]->(q:Question) WHERE q.questionId = $questionId AND r.lengthOption = $lengthOption RETURN count(*)")
    CompletableFuture<Integer> countByQuestionIdAndLengthOption(@Param("questionId") Integer questionId, @Param("lengthOption") String lengthOption);

}
