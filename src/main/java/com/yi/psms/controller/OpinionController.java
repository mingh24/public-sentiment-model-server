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

    @GetMapping("/overall/attitude-distribution/{questionId}")
    public ResponseVO getOverallAttitudeDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /overall/attitude-distribution/{} -----", questionId);
        return opinionService.getOverallAttitudeDistribution(questionId);
    }

    @GetMapping("/overall/price-option-distribution/{questionId}")
    public ResponseVO getOverallPriceOptionDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /overall/price-option-distribution/{} -----", questionId);
        return opinionService.getOverallPriceOptionDistribution(questionId);
    }

    @GetMapping("/overall/length-option-distribution/{questionId}")
    public ResponseVO getOverallLengthOptionDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /overall/length-option-distribution/{} -----", questionId);
        return opinionService.getOverallLengthOptionDistribution(questionId);
    }

    @GetMapping("/overall/opinion-distribution/{questionId}")
    public ResponseVO getOverallOpinionDistribution(@PathVariable Integer questionId) {
        log.info("----- requested url: /overall/opinion-distribution/{} -----", questionId);
        return opinionService.getOverallOpinionDistribution(questionId);
    }

}
