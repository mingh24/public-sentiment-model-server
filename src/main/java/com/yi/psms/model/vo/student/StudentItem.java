package com.yi.psms.model.vo.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yi.psms.model.entity.node.StudentNode;
import lombok.Data;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentItem {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("studentId")
    private Integer studentId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sclass")
    private String sclass;

    @JsonProperty("dormitory")
    private String dormitory;

    public StudentItem(Long id, Integer studentId, String name, String sclass, String dormitory) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.sclass = sclass;
        this.dormitory = dormitory;
    }

    public static StudentItem buildFromNode(StudentNode studentNode) {
        return new StudentItem(studentNode.getId(), studentNode.getStudentId(), studentNode.getName(), studentNode.getSclass(), studentNode.getDormitory());
    }

    public static List<StudentItem> buildListFromNodeList(List<StudentNode> studentNodeList) {
        List<StudentItem> studentItemList = new ArrayList<>();

        for (val studentNode : studentNodeList) {
            StudentItem studentItem = new StudentItem(studentNode.getId(), studentNode.getStudentId(), studentNode.getName(), studentNode.getSclass(), studentNode.getDormitory());
            studentItemList.add(studentItem);
        }

        return studentItemList;
    }

}
