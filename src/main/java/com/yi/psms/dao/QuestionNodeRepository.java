package com.yi.psms.dao;

import com.yi.psms.model.entity.node.QuestionNode;
import org.neo4j.driver.internal.value.MapValue;
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

    @Async
    @Query("MATCH (s1:Student)-[r:FRIEND|ROOMMATE|CLASSMATE]->(s2:Student) WHERE s1.studentId = $studentId AND r.intimacy IS NOT NULL WITH s2, r ORDER BY r.intimacy DESC, s2.studentId ASC MATCH(s2)-[o:OPINION]->(q:Question) WHERE q.questionId = $questionId RETURN {studentId: s2.studentId, relationship: type(r), intimacy: r.intimacy, attitude: o.attitude, priceOption: o.priceOption, lengthOption: o.lengthOption, view: o.view}")
    CompletableFuture<List<MapValue>> getIntimateOpinionByStudentIdAndQuestionId(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId);

}
