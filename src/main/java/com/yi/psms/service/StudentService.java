package com.yi.psms.service;

import com.yi.psms.dao.StudentNodeRepository;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.student.StudentItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentService extends BaseService {

    private final StudentNodeRepository studentNodeRepository;

    public StudentService(StudentNodeRepository studentNodeRepository) {
        this.studentNodeRepository = studentNodeRepository;
    }

    public ResponseVO getStudentByStudentId(Integer studentId) {
        return response(StudentItem.buildFromNode(studentNodeRepository.findByStudentId(studentId)));
    }

    public ResponseVO getStudentByName(String name) {
        return response(StudentItem.buildListFromNodeList(studentNodeRepository.findByName(name)));
    }

//    public ResponseVO getAllStudents() {
//        return response(studentNodeRepository.findAll());
//    }

}
