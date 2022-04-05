package com.yi.psms.model.vo.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yi.psms.model.entity.node.StudentNode;
import lombok.Data;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentItemVO {

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

    public StudentItemVO(Long id, Integer studentId, String name, String sclass, String dormitory) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.sclass = sclass;
        this.dormitory = dormitory;
    }

    public static StudentItemVO buildFromNode(StudentNode studentNode) {
        return new StudentItemVO(studentNode.getId(), studentNode.getStudentId(), studentNode.getName(), studentNode.getSclass(), studentNode.getDormitory());
    }

    public static List<StudentItemVO> buildListFromNodeList(List<StudentNode> studentNodeList) {
        List<StudentItemVO> studentItemList = new ArrayList<>();

        for (val studentNode : studentNodeList) {
            StudentItemVO studentItem = buildFromNode(studentNode);
            studentItemList.add(studentItem);
        }

        return studentItemList;
    }

}
