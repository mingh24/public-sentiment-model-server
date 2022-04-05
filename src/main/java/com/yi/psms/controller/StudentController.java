package com.yi.psms.controller;

import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController extends BaseController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student-id/{studentId}")
    public ResponseVO getStudentByStudentId(@PathVariable("studentId") Integer studentId) {
        log.info("requested url: /students/student-id/{}", studentId);
        return studentService.getStudentByStudentId(studentId);
    }

    @GetMapping("/name/{name}")
    public ResponseVO getStudentByName(@PathVariable("name") String name) {
        log.info("requested url: /students/name/{}", name);
        return studentService.getStudentByName(name);
    }

}
