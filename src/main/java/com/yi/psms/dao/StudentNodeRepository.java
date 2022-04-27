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

    @Query("MATCH (s:Student)-[r:CLASSMATE]->(:Student) WHERE s.studentId = $studentId\n" +
            "SET r.intimacy = $intimacy, r.updatedAt = $currentDateTime\n" +
            "RETURN count(r)")
    Integer setClassmateIntimacy(@Param("studentId") Integer studentId, @Param("intimacy") Integer intimacy, @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("MATCH (s:Student)-[r:ROOMMATE]->(:Student) WHERE s.studentId = $studentId\n" +
            "SET r.intimacy = $intimacy, r.updatedAt = $currentDateTime\n" +
            "RETURN count(r)")
    Integer setRoommateIntimacy(@Param("studentId") Integer studentId, @Param("intimacy") Integer intimacy, @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("MATCH (s:Student)-[r:FRIEND]->(:Student) WHERE s.studentId = $studentId DELETE r RETURN count(r)")
    Integer deleteFriend(@Param("studentId") Integer studentId);

    @Query("MATCH (s1:Student) WHERE s1.studentId = $studentId\n" +
            "MATCH (s2:Student) WHERE s2.studentId = $friendStudentId\n" +
            "CREATE (s1)-[r:FRIEND {intimacy: $intimacy, createdAt: $currentDateTime, updatedAt: $currentDateTime}]->(s2)\n" +
            "RETURN count(r)")
    Integer setFriendIntimacy(@Param("studentId") Integer studentId, @Param("friendStudentId") Integer friendStudentId, @Param("intimacy") Integer intimacy, @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("MATCH (s:Student)-[r:OPINION]->(q:Question) WHERE s.studentId = $studentId AND q.questionId = $questionId DELETE r RETURN count(r)")
    Integer deleteOpinion(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId);

    @Query("MATCH (s:Student) WHERE s.studentId = $studentId\n" +
            "MATCH (q:Question) WHERE q.questionId = $questionId\n" +
            "CREATE (s)-[r:OPINION {attitude: $attitude, priceOption: $priceOption, lengthOption: $lengthOption, view: $view, hasRequestedOverallDist: $hasRequestedOverallDist, hasRequestedIntimateDist: $hasRequestedIntimateDist, createdAt: $currentDateTime, updatedAt: $currentDateTime}]->(q)\n" +
            "RETURN count(r)")
    Integer setOpinion(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId, @Param("attitude") Integer attitude, @Param("priceOption") String priceOption, @Param("lengthOption") String lengthOption, @Param("view") String view, @Param("hasRequestedOverallDist") Boolean hasRequestedOverallDist, @Param("hasRequestedIntimateDist") Boolean hasRequestedIntimateDist, @Param("currentDateTime") LocalDateTime currentDateTime);

}
