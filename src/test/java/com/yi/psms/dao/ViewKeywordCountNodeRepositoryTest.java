package com.yi.psms.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ViewKeywordCountNodeRepositoryTest {

    @Autowired
    private ViewKeywordCountNodeRepository viewKeywordCountNodeRepository;

    @Test
    void findByQuestionId() {
        System.out.println(viewKeywordCountNodeRepository.findFirstByQuestionIdOrderByUpdatedAtDesc(3));
    }

}