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
        log.info("----- requested url: /opinions/attitude/overall-distribution/{} -----", questionId);
        return opinionService.getAttitudeOverallDistribution(questionId);
    }

    @GetMapping("/price-option/overall-distribution/{questionId}")
    public ResponseVO getPriceOptionOverallDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /opinions/price-option/overall-distribution/{} -----", questionId);
        return opinionService.getPriceOptionOverallDistribution(questionId);
    }

    @GetMapping("/length-option/overall-distribution/{questionId}")
    public ResponseVO getLengthOptionOverallDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /opinions/length-option/overall-distribution/{} -----", questionId);
        return opinionService.getLengthOptionOverallDistribution(questionId);
    }

    @GetMapping("/view/overall-distribution/{questionId}")
    public ResponseVO getViewOverallDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /opinions/view/overall-distribution/{} -----", questionId);
        return opinionService.getViewOverallDistribution(questionId);
    }

    @GetMapping("/all/overall-distribution/{questionId}")
    public ResponseVO getAllOverallDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /opinions/all/overall-distribution/{} -----", questionId);
        return opinionService.getAllOverallDistribution(questionId);
    }

}
