package com.yi.psms.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentNodeRepositoryTest {

    @Autowired
    private StudentNodeRepository studentNodeRepository;

    @Test
    void findByStudentId() {
        System.out.println(studentNodeRepository.findByStudentId(20210000));
    }

    @Test
    void findByName() {
        System.out.println(studentNodeRepository.findByName("test"));
    }

    @Test
    void findByStudentIdAndName() {
        System.out.println(studentNodeRepository.findByStudentIdAndName(20210000, "test"));
    }

    @Test
    void findAll() {
        System.out.println(studentNodeRepository.findAll());
    }

    @Test
    void setClassmateIntimacy() {
        System.out.println(studentNodeRepository.setClassmateIntimacy(20210000, 5, LocalDateTime.now()));
    }

    @Test
    void setRoommateIntimacy() {
        System.out.println(studentNodeRepository.setRoommateIntimacy(20210000, 5, LocalDateTime.now()));
    }

    @Test
    void deleteFriend() {
        System.out.println(studentNodeRepository.deleteFriend(20210000));
    }

    @Test
    void setFriendIntimacy() {
        System.out.println(studentNodeRepository.setFriendIntimacy(20210000, "testFriend", 11, LocalDateTime.now()));
    }

    @Test
    void deleteOpinion() {
        System.out.println(studentNodeRepository.deleteOpinion(20210000, 1));
    }

    @Test
    void setOpinion() {
        System.out.println(studentNodeRepository.setOpinion(20210000, 1, 5, null, null, null, LocalDateTime.now()));
    }

}