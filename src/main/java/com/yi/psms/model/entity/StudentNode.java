package com.yi.psms.model.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("Student")
public class StudentNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property("studentId")
    private Integer studentId;

    @Property("name")
    private String name;

    @Property("class")
    private String sclass;

    @Property("dormitory")
    private String dormitory;

    public StudentNode(Integer studentId, String name, String sclass, String dormitory) {
        this.studentId = studentId;
        this.name = name;
        this.sclass = sclass;
        this.dormitory = dormitory;
    }

    @Relationship(type = "CLASSMATE", direction = Relationship.Direction.OUTGOING)
    private Set<StudentNode> classmates = new HashSet<>();

    @Relationship(type = "ROOMMATE", direction = Relationship.Direction.OUTGOING)
    private Set<StudentNode> roommates = new HashSet<>();

    @Relationship(type = "FRIEND", direction = Relationship.Direction.OUTGOING)
    private Set<StudentNode> friends = new HashSet<>();

    @Relationship(type = "OPINION", direction = Relationship.Direction.OUTGOING)
    private Set<QuestionNode> opinions = new HashSet<>();

}
