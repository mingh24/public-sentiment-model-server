package com.yi.psms.controller;

import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.service.StatementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/statements")
public class StatementController extends BaseController {

    private final StatementService statementService;

    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @GetMapping("/statement-id/{statementId}")
    public ResponseVO getStatementByStatementId(@PathVariable("statementId") Integer statementId) {
        log.info("requested url: /statements/statement-id/{}", statementId);
        return statementService.getStatementByStatementId(statementId);
    }

}
