package com.yi.psms.controller;

import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.service.OpinionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/attitude/intimate-distribution/{studentId}/{questionId}")
    public ResponseVO getAttitudeIntimateDistribution(@PathVariable Integer studentId, @PathVariable Integer questionId) {
        log.info("----- requested url: /opinions/attitude/intimate-distribution/{}/{} -----", studentId, questionId);
        return opinionService.getAttitudeIntimateDistribution(studentId, questionId);
    }

    @GetMapping("/price-option/intimate-distribution/{studentId}/{questionId}")
    public ResponseVO getPriceOptionIntimateDistribution(@PathVariable Integer studentId, @PathVariable Integer questionId) {
        log.info("----- requested url: /opinions/price-option/intimate-distribution/{}/{} -----", studentId, questionId);
        return opinionService.getPriceOptionIntimateDistribution(studentId, questionId);
    }

    @GetMapping("/length-option/intimate-distribution/{studentId}/{questionId}")
    public ResponseVO getLengthOptionIntimateDistribution(@PathVariable Integer studentId, @PathVariable Integer questionId) {
        log.info("----- requested url: /opinions/length-option/intimate-distribution/{}/{} -----", studentId, questionId);
        return opinionService.getLengthOptionIntimateDistribution(studentId, questionId);
    }

    @GetMapping("/view/intimate-distribution/{studentId}/{questionId}")
    public ResponseVO getViewIntimateDistribution(@PathVariable Integer studentId, @PathVariable Integer questionId) {
        log.info("----- requested url: /opinions/view/intimate-distribution/{}/{} -----", studentId, questionId);
        return opinionService.getViewIntimateDistribution(studentId, questionId);
    }

    @GetMapping("/all/intimate-distribution/{studentId}/{questionId}")
    public ResponseVO getAllIntimateDistribution(@PathVariable Integer studentId, @PathVariable Integer questionId) {
        log.info("----- requested url: /opinions/all/intimate-distribution/{}/{} -----", studentId, questionId);
        return opinionService.getAllIntimateDistribution(studentId, questionId);
    }

    @PostMapping("/dump-opinion-view-keyword-count/{questionId}")
    public ResponseVO dumpOpinionViewKeywordCount(@PathVariable Integer questionId) {
        log.info("----- requested url: /opinions/dump-opinion-view-keyword-count/{} -----", questionId);
        opinionService.dumpOpinionViewKeywordCount(questionId);
        return response();
    }

}
