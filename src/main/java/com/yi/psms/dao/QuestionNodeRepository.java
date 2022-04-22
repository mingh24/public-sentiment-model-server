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
    @Query("MATCH (:Student)-[r:OPINION]->(q:Question) WHERE q.questionId = $questionId WITH r.attitude AS attitude, count(*) AS count ORDER BY count DESC RETURN {name: attitude, count: count}")
    CompletableFuture<List<MapValue>> countOpinionByAttitude(@Param("questionId") Integer questionId);

    @Async
    @Query("MATCH (:Student)-[r:OPINION]->(q:Question) WHERE q.questionId = $questionId WITH r.priceOption AS priceOption, count(*) AS count ORDER BY count DESC RETURN {name: priceOption, count: count}")
    CompletableFuture<List<MapValue>> countOpinionByPriceOption(@Param("questionId") Integer questionId);

    @Async
    @Query("MATCH (:Student)-[r:OPINION]->(q:Question) WHERE q.questionId = $questionId WITH r.lengthOption AS lengthOption, count(*) AS count ORDER BY count DESC RETURN {name: lengthOption, count: count}")
    CompletableFuture<List<MapValue>> countOpinionByLengthOption(@Param("questionId") Integer questionId);

    @Query("MATCH (s:Student)-[r:OPINION]->(q:Question) WHERE q.questionId = $questionId AND r.view IS NOT NULL RETURN {studentId: s.studentId, view: r.view}")
    List<MapValue> getOpinionView(@Param("questionId") Integer questionId);

    @Async
    @Query("MATCH (s1:Student)-[r:FRIEND|ROOMMATE|CLASSMATE]->(s2:Student) WHERE s1.studentId = $studentId AND r.intimacy IS NOT NULL WITH s2, r ORDER BY r.intimacy DESC, s2.studentId ASC MATCH(s2)-[o:OPINION]->(q:Question) WHERE q.questionId = $questionId RETURN {studentId: s2.studentId, relationship: type(r), intimacy: r.intimacy, attitude: o.attitude, priceOption: o.priceOption, lengthOption: o.lengthOption, view: o.view}")
    CompletableFuture<List<MapValue>> getIntimateOpinionByStudentIdAndQuestionId(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId);

}
