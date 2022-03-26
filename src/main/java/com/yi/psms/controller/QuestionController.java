package com.yi.psms.controller;

import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/questions")
public class QuestionController extends BaseController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/question-id/{questionId}")
    public ResponseVO getQuestionByQuestionId(@PathVariable Integer questionId) {
        log.info("----- requested url: /questions/question-id/{} -----", questionId);
        return questionService.getQuestionByQuestionId(questionId);
    }

    @GetMapping
    public ResponseVO getAllQuestions() {
        log.info("----- requested url: /questions -----");
        return questionService.getAllQuestions();
    }

}
