package com.yi.psms.controller;

import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.questionnaire.SubmissionVO;
import com.yi.psms.service.QuestionnaireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController extends BaseController {

    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @PostMapping("/essential")
    public ResponseVO essential(@Validated(value = {SubmissionVO.Essential.class}) @RequestBody SubmissionVO submission) {
        log.info("student {} requested url: /questionnaire/essential", submission.getStudentId());
        return questionnaireService.essential(submission);
    }

    @PostMapping("/advanced")
    public ResponseVO advanced(@Validated(value = {SubmissionVO.Advanced.class}) @RequestBody SubmissionVO submission) {
        log.info("student {} requested url: /questionnaire/advanced", submission.getStudentId());
        return questionnaireService.advanced(submission);
    }

}
