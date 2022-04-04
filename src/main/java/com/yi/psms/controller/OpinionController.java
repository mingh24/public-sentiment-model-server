package com.yi.psms.controller;

import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.service.OpinionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/opinions")
public class OpinionController extends BaseController {

    private final OpinionService opinionService;

    public OpinionController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @GetMapping("/attitude/overall-distribution/{questionId}")
    public ResponseVO getAttitudeOverallDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /attitude/overall-distribution/{} -----", questionId);
        return opinionService.getAttitudeOverallDistribution(questionId);
    }

    @GetMapping("/price-option/overall-distribution/{questionId}")
    public ResponseVO getPriceOptionOverallDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /price-option/overall-distribution/{} -----", questionId);
        return opinionService.getPriceOptionOverallDistribution(questionId);
    }

    @GetMapping("/length-option/overall-distribution/{questionId}")
    public ResponseVO getLengthOptionOverallDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /length-option/overall-distribution/{} -----", questionId);
        return opinionService.getLengthOptionOverallDistribution(questionId);
    }

    @GetMapping("/opinion/overall-distribution/{questionId}")
    public ResponseVO getOpinionOverallDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /opinion/overall-distribution/{} -----", questionId);
        return opinionService.getOpinionOverallDistribution(questionId);
    }

    @GetMapping("/all/overall-distribution/{questionId}")
    public ResponseVO getAllOverallDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /all/overall-distribution/{} -----", questionId);
        return opinionService.getAllOverallDistribution(questionId);
    }

}
