package com.yi.psms.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionNodeRepositoryTest {

    @Autowired
    private QuestionNodeRepository questionNodeRepository;

    @Test
    void findByQuestionId() {
        System.out.println(questionNodeRepository.findByQuestionId(1));
    }

    @Test
    void findAll() {
        System.out.println(questionNodeRepository.findAll());
    }

    @Test
    void countOpinionByAttitude() throws ExecutionException, InterruptedException {
        var f = questionNodeRepository.countOpinionByAttitude(1);
        System.out.println(f.get());
    }

    @Test
    void countOpinionByPriceOption() throws ExecutionException, InterruptedException {
        var f = questionNodeRepository.countOpinionByPriceOption(1);
        System.out.println(f.get());
    }

    @Test
    void countOpinionByLengthOption() throws ExecutionException, InterruptedException {
        var f = questionNodeRepository.countOpinionByLengthOption(1);
        System.out.println(f.get());
    }

    @Test
    void getOpinionView() {
        var l = questionNodeRepository.getOpinionView(3);
        System.out.println(l);
    }

    @Test
    void getIntimateOpinionByStudentIdAndQuestionId() throws ExecutionException, InterruptedException {
        var f = questionNodeRepository.getIntimateOpinionByStudentIdAndQuestionId(20210000, 1);
        System.out.println(f.get());
    }

}