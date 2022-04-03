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
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class OpinionService extends BaseService {

    private final QuestionNodeRepository questionNodeRepository;

    public OpinionService(QuestionNodeRepository questionNodeRepository) {
        this.questionNodeRepository = questionNodeRepository;
    }

    public ResponseVO getOverallAttitudeDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 获取整体观点支持度分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent questionContent = gson.fromJson(questionNode.getContent(), QuestionContent.class);
        BasicQuestion basicQuestion = questionContent.getBasicQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachOverallAttitudeDistribution(opinionDistributionItem, questionId, basicQuestion);
        completableFutureList.add(attitudeDistFuture);

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistributionItem);
    }

    public ResponseVO getOverallPriceOptionDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 获取整体价钱问题观点分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent questionContent = gson.fromJson(questionNode.getContent(), QuestionContent.class);
        ExtraQuestion priceQuestion = questionContent.getPriceQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var priceOptionDistFuture = attachOverallPriceOptionDistribution(opinionDistributionItem, questionId, priceQuestion);
        completableFutureList.add(priceOptionDistFuture);

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistributionItem);
    }

    public ResponseVO getOverallLengthOptionDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 获取整体时长问题观点分布
        OpinionDistributionItem opinionDistributionItem = new OpinionDistributionItem();
        Gson gson = new Gson();
        QuestionContent questionContent = gson.fromJson(questionNode.getContent(), QuestionContent.class);
        ExtraQuestion lengthQuestion = questionContent.getLengthQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var lengthOptionDistFuture = attachOverallLengthOptionDistribution(opinionDistributionItem, questionId, lengthQuestion);
        completableFutureList.add(lengthOptionDistFuture);

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistributionItem);
    }

    public ResponseVO getOverallOpinionDistribution(Integer questionId) {
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

        var attitudeDistFuture = attachOverallAttitudeDistribution(opinionDistributionItem, questionId, basicQuestion);
        completableFutureList.add(attitudeDistFuture);

        var priceOptionDistFuture = attachOverallPriceOptionDistribution(opinionDistributionItem, questionId, priceQuestion);
        completableFutureList.add(priceOptionDistFuture);

        var lengthOptionDistFuture = attachOverallLengthOptionDistribution(opinionDistributionItem, questionId, lengthQuestion);
        completableFutureList.add(lengthOptionDistFuture);

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistributionItem);
    }

    public CompletableFuture<Void> attachOverallAttitudeDistribution(OpinionDistributionItem opinionDistributionItem, Integer questionId, BasicQuestion basicQuestion) {
        List<OpinionCountItem> attitudeCountItemList = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (var i = basicQuestion.getOption().getMin(); i <= basicQuestion.getOption().getMax(); i++) {
            val finalI = i;
            completableFutureList.add(questionNodeRepository.countByQuestionIdAndAttitude(questionId, i).whenComplete((count, throwable) -> {
                var o = new OpinionCountItem(finalI.toString(), count);
                attitudeCountItemList.add(o);
            }));
        }

        return CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()])).whenComplete((v, throwable) -> {
            attitudeCountItemList.sort(Comparator.comparing(OpinionCountItem::getName));
            opinionDistributionItem.setOverallAttitudeDist(attitudeCountItemList);
        });
    }

    public CompletableFuture<Void> attachOverallPriceOptionDistribution(OpinionDistributionItem opinionDistributionItem, Integer questionId, ExtraQuestion priceQuestion) {
        List<OpinionCountItem> priceOptionCountItemList = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (val option : priceQuestion.getOption()) {
            completableFutureList.add(questionNodeRepository.countByQuestionIdAndPriceOption(questionId, String.format("%s@%s", option.getOptionKey(), option.getOptionValue())).whenComplete((count, throwable) -> {
                var o = new OpinionCountItem(option.getOptionValue(), count);
                priceOptionCountItemList.add(o);
            }));
        }

        return CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()])).whenComplete((v, throwable) -> {
            priceOptionCountItemList.sort(Comparator.comparing(OpinionCountItem::getName));
            opinionDistributionItem.setOverallPriceOptionDist(priceOptionCountItemList);
        });
    }

    public CompletableFuture<Void> attachOverallLengthOptionDistribution(OpinionDistributionItem opinionDistributionItem, Integer questionId, ExtraQuestion lengthOption) {
        List<OpinionCountItem> lengthOptionCountItemList = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (val option : lengthOption.getOption()) {
            completableFutureList.add(questionNodeRepository.countByQuestionIdAndLengthOption(questionId, String.format("%s@%s", option.getOptionKey(), option.getOptionValue())).whenComplete((count, throwable) -> {
                var o = new OpinionCountItem(option.getOptionValue(), count);
                lengthOptionCountItemList.add(o);
            }));
        }

        return CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()])).whenComplete((v, throwable) -> {
            lengthOptionCountItemList.sort(Comparator.comparing(OpinionCountItem::getName));
            opinionDistributionItem.setOverallLengthOptionDist(lengthOptionCountItemList);
        });
    }

}
