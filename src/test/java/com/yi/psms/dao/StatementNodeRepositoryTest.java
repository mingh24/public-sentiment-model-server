package com.yi.psms.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StatementNodeRepositoryTest {

    @Autowired
    private StatementNodeRepository statementNodeRepository;

    @Test
    void findStatementNodeByStatementId() {
        System.out.println(statementNodeRepository.findByStatementId(1));
    }

}