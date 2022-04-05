package com.yi.psms.service;

import com.google.gson.Gson;
import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.dao.QuestionNodeRepository;
import com.yi.psms.model.entity.BasicQuestion;
import com.yi.psms.model.entity.ExtraQuestion;
import com.yi.psms.model.entity.QuestionContent;
import com.yi.psms.model.entity.QuestionNode;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.opinion.OpinionCountItem;
import com.yi.psms.model.vo.opinion.OpinionDistributionItem;
import com.yi.psms.util.Neo4jHelper;
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

        // 获取上一阶段观点支持度问题整体观点分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent previousQuestionContent = gson.fromJson(previousQuestionNode.getContent(), QuestionContent.class);
        BasicQuestion previousBasicQuestion = previousQuestionContent.getBasicQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachAttitudeOverallDistribution(opinionDistributionItem, previousQuestionId, previousBasicQuestion);
        completableFutureList.add(attitudeDistFuture);

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

        // 获取上一阶段时长问题整体观点分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent previousQuestionContent = gson.fromJson(previousQuestionNode.getContent(), QuestionContent.class);
        ExtraQuestion previousPriceQuestion = previousQuestionContent.getPriceQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var priceOptionDistFuture = attachPriceOptionOverallDistribution(opinionDistributionItem, previousQuestionId, previousPriceQuestion);
        completableFutureList.add(priceOptionDistFuture);

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

        // 获取上一阶段时长问题整体观点分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent previousQuestionContent = gson.fromJson(previousQuestionNode.getContent(), QuestionContent.class);
        ExtraQuestion previousLengthQuestion = previousQuestionContent.getLengthQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var lengthOptionDistFuture = attachLengthOptionOverallDistribution(opinionDistributionItem, previousQuestionId, previousLengthQuestion);
        completableFutureList.add(lengthOptionDistFuture);

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistributionItem);
    }

    public ResponseVO getOpinionOverallDistribution(Integer questionId) {
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

        // TODO 获取观点表达整体分布

        return response();
    }

    public ResponseVO getAllOverallDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 获取整体观点分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent questionContent = gson.fromJson(questionNode.getContent(), QuestionContent.class);
        BasicQuestion basicQuestion = questionContent.getBasicQuestion();
        ExtraQuestion priceQuestion = questionContent.getPriceQuestion();
        ExtraQuestion lengthQuestion = questionContent.getLengthQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachAttitudeOverallDistribution(opinionDistributionItem, questionId, basicQuestion);
        completableFutureList.add(attitudeDistFuture);

        var priceOptionDistFuture = attachPriceOptionOverallDistribution(opinionDistributionItem, questionId, priceQuestion);
        completableFutureList.add(priceOptionDistFuture);

        var lengthOptionDistFuture = attachLengthOptionOverallDistribution(opinionDistributionItem, questionId, lengthQuestion);
        completableFutureList.add(lengthOptionDistFuture);

        // TODO 获取观点表达整体分布

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistributionItem);
    }

    public CompletableFuture<Void> attachAttitudeOverallDistribution(OpinionDistributionItem opinionDistributionItem, Integer questionId, BasicQuestion basicQuestion) {
        List<OpinionCountItem> result = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (var i = basicQuestion.getOption().getMin(); i <= basicQuestion.getOption().getMax(); i++) {
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

    public CompletableFuture<Void> attachPriceOptionOverallDistribution(OpinionDistributionItem opinionDistributionItem, Integer questionId, ExtraQuestion priceQuestion) {
        List<OpinionCountItem> result = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (val option : priceQuestion.getOption()) {
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

    public CompletableFuture<Void> attachLengthOptionOverallDistribution(OpinionDistributionItem opinionDistributionItem, Integer questionId, ExtraQuestion lengthOption) {
        List<OpinionCountItem> result = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (val option : lengthOption.getOption()) {
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

    public CompletableFuture<Void> attachOpinionOverallDistribution() {
        // TODO 获取观点表达整体分布

        return null;
    }

}
