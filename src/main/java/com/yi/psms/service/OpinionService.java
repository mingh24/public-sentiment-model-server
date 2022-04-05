package com.yi.psms.service;

import com.google.gson.Gson;
import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.dao.QuestionNodeRepository;
import com.yi.psms.model.entity.node.QuestionNode;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.opinion.OpinionCountItem;
import com.yi.psms.model.vo.opinion.OpinionDistributionItem;
import com.yi.psms.model.vo.question.AttitudeQuestionVO;
import com.yi.psms.model.vo.question.LengthQuestionVO;
import com.yi.psms.model.vo.question.PriceQuestionVO;
import com.yi.psms.model.vo.question.QuestionContent;
import com.yi.psms.util.Neo4jHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OpinionService extends BaseService {

    private final QuestionNodeRepository questionNodeRepository;

    public OpinionService(QuestionNodeRepository questionNodeRepository) {
        this.questionNodeRepository = questionNodeRepository;
    }

    public ResponseVO getAttitudeOverallDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 判断上一阶段的问题信息是否存在
        Integer previousQuestionId = questionNode.getPreviousQuestionId();
        QuestionNode previousQuestionNode = questionNodeRepository.findByQuestionId(previousQuestionId);
        if (previousQuestionId == null) {
            log.warn("nonexistent previous question, questionId: {}", previousQuestionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应上一阶段问题信息：%s", previousQuestionId));
        }

        // 获取上一阶段观点支持度问题结果整体分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent previousQuestionContent = gson.fromJson(previousQuestionNode.getContent(), QuestionContent.class);
        AttitudeQuestionVO previousAttitudeQuestionVO = previousQuestionContent.getAttitudeQuestionVO();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachAttitudeOverallDistribution(opinionDistributionItem, previousQuestionId, previousAttitudeQuestionVO);
        if (attitudeDistFuture != null) {
            completableFutureList.add(attitudeDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistributionItem);
    }

    public ResponseVO getPriceOptionOverallDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 判断上一阶段的问题信息是否存在
        Integer previousQuestionId = questionNode.getPreviousQuestionId();
        QuestionNode previousQuestionNode = questionNodeRepository.findByQuestionId(previousQuestionId);
        if (previousQuestionId == null) {
            log.warn("nonexistent previous question, questionId: {}", previousQuestionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应上一阶段问题信息：%s", previousQuestionId));
        }

        // 获取上一阶段时长问题结果整体分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent previousQuestionContent = gson.fromJson(previousQuestionNode.getContent(), QuestionContent.class);
        PriceQuestionVO previousPriceQuestionVO = previousQuestionContent.getPriceQuestionVO();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var priceOptionDistFuture = attachPriceOptionOverallDistribution(opinionDistributionItem, previousQuestionId, previousPriceQuestionVO);
        if (priceOptionDistFuture != null) {
            completableFutureList.add(priceOptionDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistributionItem);
    }

    public ResponseVO getLengthOptionOverallDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 判断上一阶段的问题信息是否存在
        Integer previousQuestionId = questionNode.getPreviousQuestionId();
        QuestionNode previousQuestionNode = questionNodeRepository.findByQuestionId(previousQuestionId);
        if (previousQuestionId == null) {
            log.warn("nonexistent previous question, questionId: {}", previousQuestionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应上一阶段问题信息：%s", previousQuestionId));
        }

        // 获取上一阶段时长问题结果整体分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent previousQuestionContent = gson.fromJson(previousQuestionNode.getContent(), QuestionContent.class);
        LengthQuestionVO previousLengthQuestionVO = previousQuestionContent.getLengthQuestionVO();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var lengthOptionDistFuture = attachLengthOptionOverallDistribution(opinionDistributionItem, previousQuestionId, previousLengthQuestionVO);
        if (lengthOptionDistFuture != null) {
            completableFutureList.add(lengthOptionDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistributionItem);
    }

    public ResponseVO getViewOverallDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 判断上一阶段的问题信息是否存在
        Integer previousQuestionId = questionNode.getPreviousQuestionId();
        QuestionNode previousQuestionNode = questionNodeRepository.findByQuestionId(previousQuestionId);
        if (previousQuestionId == null) {
            log.warn("nonexistent previous question, questionId: {}", previousQuestionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应上一阶段问题信息：%s", previousQuestionId));
        }

        // TODO 获取看法结果整体分布

        return response();
    }

    public ResponseVO getAllOverallDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 判断上一阶段的问题信息是否存在
        Integer previousQuestionId = questionNode.getPreviousQuestionId();
        QuestionNode previousQuestionNode = questionNodeRepository.findByQuestionId(previousQuestionId);
        if (previousQuestionId == null) {
            log.warn("nonexistent previous question, questionId: {}", previousQuestionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应上一阶段问题信息：%s", previousQuestionId));
        }

        // 获取整体结果分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent previousQuestionContent = gson.fromJson(previousQuestionNode.getContent(), QuestionContent.class);
        AttitudeQuestionVO previousAttitudeQuestionVO = previousQuestionContent.getAttitudeQuestionVO();
        PriceQuestionVO previousPriceQuestionVO = previousQuestionContent.getPriceQuestionVO();
        LengthQuestionVO previousLengthQuestionVO = previousQuestionContent.getLengthQuestionVO();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachAttitudeOverallDistribution(opinionDistributionItem, questionId, previousAttitudeQuestionVO);
        if (attitudeDistFuture != null) {
            completableFutureList.add(attitudeDistFuture);
        }

        var priceOptionDistFuture = attachPriceOptionOverallDistribution(opinionDistributionItem, questionId, previousPriceQuestionVO);
        if (priceOptionDistFuture != null) {
            completableFutureList.add(priceOptionDistFuture);
        }

        var lengthOptionDistFuture = attachLengthOptionOverallDistribution(opinionDistributionItem, questionId, previousLengthQuestionVO);
        if (lengthOptionDistFuture != null) {
            completableFutureList.add(lengthOptionDistFuture);
        }

        // TODO 获取看法结果整体分布

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistributionItem);
    }

    public CompletableFuture<Void> attachAttitudeOverallDistribution(@NonNull OpinionDistributionItem opinionDistributionItem, Integer questionId, AttitudeQuestionVO attitudeQuestionVO) {
        if (attitudeQuestionVO == null) {
            return null;
        }

        val nbq = attitudeQuestionVO.getNumberBoundaryQuestion();
        if (nbq == null) {
            return null;
        }

        List<OpinionCountItem> result = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (var i = nbq.getMin(); i <= nbq.getMax(); i++) {
            val finalI = i;
            completableFutureList.add(questionNodeRepository.countByQuestionIdAndAttitude(questionId, i).whenComplete((count, throwable) -> {
                if (count > 0) {
                    var o = new OpinionCountItem(finalI.toString(), count);
                    result.add(o);
                }
            }));
        }

        return CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()])).whenComplete((v, throwable) -> {
            var sortedResult = result.stream().sorted(Comparator.comparing(OpinionCountItem::getCount, Comparator.reverseOrder()).thenComparing(OpinionCountItem::getName)).collect(Collectors.toList());
            opinionDistributionItem.setAttitudeOverallDist(sortedResult);
        });
    }

    public CompletableFuture<Void> attachPriceOptionOverallDistribution(@NonNull OpinionDistributionItem opinionDistributionItem, Integer questionId, PriceQuestionVO priceQuestionVO) {
        if (priceQuestionVO == null) {
            return null;
        }

        val oq = priceQuestionVO.getOptionQuestion();
        if (oq == null) {
            return null;
        }

        List<OpinionCountItem> result = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (val option : oq.getOption()) {
            completableFutureList.add(questionNodeRepository.countByQuestionIdAndPriceOption(questionId, Neo4jHelper.buildPriceOptionString(option.getOptionKey(), option.getOptionValue())).whenComplete((count, throwable) -> {
                if (count > 0) {
                    var o = new OpinionCountItem(option.getOptionValue(), count);
                    result.add(o);
                }
            }));
        }

        return CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()])).whenComplete((v, throwable) -> {
            var sortedResult = result.stream().sorted(Comparator.comparing(OpinionCountItem::getCount, Comparator.reverseOrder()).thenComparing(OpinionCountItem::getName)).collect(Collectors.toList());
            opinionDistributionItem.setPriceOptionOverallDist(sortedResult);
        });
    }

    public CompletableFuture<Void> attachLengthOptionOverallDistribution(@NonNull OpinionDistributionItem opinionDistributionItem, Integer questionId, LengthQuestionVO lengthQuestionVO) {
        if (lengthQuestionVO == null) {
            return null;
        }

        val oq = lengthQuestionVO.getOptionQuestion();
        if (oq == null) {
            return null;
        }

        List<OpinionCountItem> result = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (val option : oq.getOption()) {
            completableFutureList.add(questionNodeRepository.countByQuestionIdAndLengthOption(questionId, Neo4jHelper.buildLengthOptionString(option.getOptionKey(), option.getOptionValue())).whenComplete((count, throwable) -> {
                if (count > 0) {
                    var o = new OpinionCountItem(option.getOptionValue(), count);
                    result.add(o);
                }
            }));
        }

        return CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()])).whenComplete((v, throwable) -> {
            var sortedResult = result.stream().sorted(Comparator.comparing(OpinionCountItem::getCount, Comparator.reverseOrder()).thenComparing(OpinionCountItem::getName)).collect(Collectors.toList());
            opinionDistributionItem.setLengthOptionOverallDist(sortedResult);
        });
    }

    public CompletableFuture<Void> attachViewOverallDistribution() {
        // TODO 获取看法结果整体分布

        return null;
    }

}
