package com.yi.psms.model.vo.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yi.psms.model.entity.node.StudentNode;
import lombok.Data;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentItemVO {

    @JsonProperty("studentId")
    private Integer studentId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sclass")
    private String sclass;

    public StudentItemVO(Integer studentId, String name, String sclass) {
        this.studentId = studentId;
        this.name = name;
        this.sclass = sclass;
    }

    public static StudentItemVO buildFromNode(StudentNode studentNode) {
        return new StudentItemVO(studentNode.getStudentId(), studentNode.getName(), studentNode.getSclass());
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
