package com.yi.psms.dao;

import com.yi.psms.model.entity.node.StudentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StudentNodeRepository extends Neo4jRepository<StudentNode, Long> {

    @Query("MATCH (s:Student) WHERE s.studentId = $studentId RETURN s")
    StudentNode findByStudentId(@Param("studentId") Integer studentId);

    @Query("MATCH (s:Student) WHERE s.name = $name RETURN s")
    List<StudentNode> findByName(@Param("name") String name);

    @Query("MATCH (s:Student) WHERE s.studentId = $studentId AND s.name = $name RETURN s")
    StudentNode findByStudentIdAndName(@Param("studentId") Integer studentId, @Param("name") String name);

    @Query("MATCH (s:Student) RETURN s")
    List<StudentNode> findAll();

    @Query("MATCH (s:Student)-[r:CLASSMATE]->(:Student) WHERE s.studentId = $studentId SET r.intimacy = $intimacy, r.updatedAt = $currentDateTime RETURN count(r)")
    Integer setClassmateIntimacy(@Param("studentId") Integer studentId, @Param("intimacy") Integer intimacy, @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("MATCH (s:Student)-[r:ROOMMATE]->(:Student) WHERE s.studentId = $studentId SET r.intimacy = $intimacy, r.updatedAt = $currentDateTime RETURN count(r)")
    Integer setRoommateIntimacy(@Param("studentId") Integer studentId, @Param("intimacy") Integer intimacy, @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("MATCH (s:Student)-[r:FRIEND]->(:Student) WHERE s.studentId = $studentId DELETE r RETURN count(r)")
    Integer deleteFriend(@Param("studentId") Integer studentId);

    // TODO 可能有重名问题
    @Query("MATCH (s1:Student), (s2:Student) WHERE s1.studentId = $studentId AND s2.name = $friendName AND s1.name <> s2.name WITH s1, s2 ORDER BY s2.studentId ASC LIMIT 1 CREATE (s1)-[r:FRIEND {intimacy: $intimacy, createdAt: $currentDateTime, updatedAt: $currentDateTime}]->(s2) RETURN count(r)")
    Integer setFriendIntimacy(@Param("studentId") Integer studentId, @Param("friendName") String friendName, @Param("intimacy") Integer intimacy, @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("MATCH (s:Student)-[r:OPINION]->(q:Question) WHERE s.studentId = $studentId AND q.questionId = $questionId DELETE r RETURN count(r)")
    Integer deleteOpinion(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId);

    @Query("MATCH (s:Student), (q:Question) WHERE s.studentId = $studentId AND q.questionId = $questionId CREATE (s)-[r:OPINION {attitude: $attitude, priceOption: $priceOption, lengthOption: $lengthOption, view: $view, createdAt: $currentDateTime, updatedAt: $currentDateTime}]->(q) RETURN count(r)")
    Integer setOpinion(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId, @Param("attitude") Integer attitude, @Param("priceOption") String priceOption, @Param("lengthOption") String lengthOption, @Param("view") String view, @Param("currentDateTime") LocalDateTime currentDateTime);

}
