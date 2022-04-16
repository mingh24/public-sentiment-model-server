package com.yi.psms.service;

import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.dao.QuestionNodeRepository;
import com.yi.psms.dao.StudentNodeRepository;
import com.yi.psms.model.entity.node.QuestionNode;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.opinion.IntimateOpinionItemVO;
import com.yi.psms.model.vo.opinion.OpinionCountVO;
import com.yi.psms.model.vo.opinion.OpinionDistributionVO;
import com.yi.psms.model.vo.question.AttitudeQuestionVO;
import com.yi.psms.model.vo.question.LengthQuestionVO;
import com.yi.psms.model.vo.question.PriceQuestionVO;
import com.yi.psms.model.vo.question.QuestionContentVO;
import com.yi.psms.util.Neo4jHelper;
import com.yi.psms.util.ObjectHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.neo4j.driver.internal.value.MapValue;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OpinionService extends BaseService {

    private final StudentNodeRepository studentNodeRepository;

    private final QuestionNodeRepository questionNodeRepository;

    public OpinionService(StudentNodeRepository studentNodeRepository, QuestionNodeRepository questionNodeRepository) {
        this.studentNodeRepository = studentNodeRepository;
        this.questionNodeRepository = questionNodeRepository;
    }

    public ResponseVO getAttitudeOverallDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 获取观点支持度问题结果整体分布
        OpinionDistributionVO opinionDistribution = new OpinionDistributionVO();
        QuestionContentVO questionContent = QuestionContentVO.buildFromContentString(questionNode.getContent());
        AttitudeQuestionVO attitudeQuestion = questionContent.getAttitudeQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachAttitudeOverallDistribution(opinionDistribution, questionId, attitudeQuestion);
        if (attitudeDistFuture != null) {
            completableFutureList.add(attitudeDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistribution);
    }

    public ResponseVO getPriceOptionOverallDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 获取时长问题结果整体分布
        OpinionDistributionVO opinionDistribution = new OpinionDistributionVO();
        QuestionContentVO questionContent = QuestionContentVO.buildFromContentString(questionNode.getContent());
        PriceQuestionVO priceQuestion = questionContent.getPriceQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var priceOptionDistFuture = attachPriceOptionOverallDistribution(opinionDistribution, questionId, priceQuestion);
        if (priceOptionDistFuture != null) {
            completableFutureList.add(priceOptionDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistribution);
    }

    public ResponseVO getLengthOptionOverallDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 获取时长问题结果整体分布
        OpinionDistributionVO opinionDistribution = new OpinionDistributionVO();
        QuestionContentVO questionContent = QuestionContentVO.buildFromContentString(questionNode.getContent());
        LengthQuestionVO lengthQuestion = questionContent.getLengthQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var lengthOptionDistFuture = attachLengthOptionOverallDistribution(opinionDistribution, questionId, lengthQuestion);
        if (lengthOptionDistFuture != null) {
            completableFutureList.add(lengthOptionDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistribution);
    }

    public ResponseVO getViewOverallDistribution(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
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

        // 获取结果整体分布
        OpinionDistributionVO opinionDistribution = new OpinionDistributionVO();
        QuestionContentVO questionContent = QuestionContentVO.buildFromContentString(questionNode.getContent());
        AttitudeQuestionVO attitudeQuestion = questionContent.getAttitudeQuestion();
        PriceQuestionVO priceQuestionVO = questionContent.getPriceQuestion();
        LengthQuestionVO lengthQuestionVO = questionContent.getLengthQuestion();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachAttitudeOverallDistribution(opinionDistribution, questionId, attitudeQuestion);
        if (attitudeDistFuture != null) {
            completableFutureList.add(attitudeDistFuture);
        }

        var priceOptionDistFuture = attachPriceOptionOverallDistribution(opinionDistribution, questionId, priceQuestionVO);
        if (priceOptionDistFuture != null) {
            completableFutureList.add(priceOptionDistFuture);
        }

        var lengthOptionDistFuture = attachLengthOptionOverallDistribution(opinionDistribution, questionId, lengthQuestionVO);
        if (lengthOptionDistFuture != null) {
            completableFutureList.add(lengthOptionDistFuture);
        }

        // TODO 获取看法结果整体分布

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistribution);
    }

    public ResponseVO getAttitudeIntimateDistribution(Integer studentId, Integer questionId) {
        // 判断填写人信息是否存在
        if (studentNodeRepository.findByStudentId(studentId) == null) {
            log.warn("student {} does not exist", studentId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", studentId));
        }

        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // TODO 判断当前同学是否完成了第一轮填写

        // 获取观点支持度问题结果在亲密同学中的分布
        OpinionDistributionVO opinionDistribution = new OpinionDistributionVO();
        List<CompletableFuture<List<MapValue>>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachAttitudeIntimateDistribution(opinionDistribution, studentId, questionId);
        if (attitudeDistFuture != null) {
            completableFutureList.add(attitudeDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        if (opinionDistribution.getAttitudeIntimateDist().size() == 0) {
            return response("由于您或与您亲密度较高的同学/舍友/班级同学未填写前置问卷，此项暂无数据", opinionDistribution);
        }

        return response(opinionDistribution);
    }

    public ResponseVO getPriceOptionIntimateDistribution(Integer studentId, Integer questionId) {
        // 判断填写人信息是否存在
        if (studentNodeRepository.findByStudentId(studentId) == null) {
            log.warn("student {} does not exist", studentId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", studentId));
        }

        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 获取时长问题结果在亲密同学中的分布
        OpinionDistributionVO opinionDistribution = new OpinionDistributionVO();
        List<CompletableFuture<List<MapValue>>> completableFutureList = new ArrayList<>();

        var priceOptionDistFuture = attachPriceOptionIntimateDistribution(opinionDistribution, studentId, questionId);
        if (priceOptionDistFuture != null) {
            completableFutureList.add(priceOptionDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        if (opinionDistribution.getPriceOptionIntimateDist().size() == 0) {
            return response("由于您或与您亲密度较高的同学/舍友/班级同学未填写前置问卷，此项暂无数据", opinionDistribution);
        }

        return response(opinionDistribution);
    }

    public ResponseVO getLengthOptionIntimateDistribution(Integer studentId, Integer questionId) {
        // 判断填写人信息是否存在
        if (studentNodeRepository.findByStudentId(studentId) == null) {
            log.warn("student {} does not exist", studentId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", studentId));
        }

        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 获取时长问题结果在亲密同学中的分布
        OpinionDistributionVO opinionDistribution = new OpinionDistributionVO();
        List<CompletableFuture<List<MapValue>>> completableFutureList = new ArrayList<>();

        var lengthOptionDistFuture = attachLengthOptionIntimateDistribution(opinionDistribution, studentId, questionId);
        if (lengthOptionDistFuture != null) {
            completableFutureList.add(lengthOptionDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        if (opinionDistribution.getLengthOptionIntimateDist().size() == 0) {
            return response("由于您或与您亲密度较高的同学/舍友/班级同学未填写前置问卷，此项暂无数据", opinionDistribution);
        }

        return response(opinionDistribution);
    }

    public ResponseVO getViewIntimateDistribution(Integer studentId, Integer questionId) {
        // 判断填写人信息是否存在
        if (studentNodeRepository.findByStudentId(studentId) == null) {
            log.warn("student {} does not exist", studentId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", studentId));
        }

        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // TODO 获取看法结果在亲密同学中的分布

        return response();
    }

    public ResponseVO getAllIntimateDistribution(Integer studentId, Integer questionId) {
        // 判断填写人信息是否存在
        if (studentNodeRepository.findByStudentId(studentId) == null) {
            log.warn("student {} does not exist", studentId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", studentId));
        }

        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 获取全部问题的结果在亲密同学中的分布
        OpinionDistributionVO opinionDistribution = new OpinionDistributionVO();
        List<CompletableFuture<List<MapValue>>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachAttitudeIntimateDistribution(opinionDistribution, studentId, questionId);
        if (attitudeDistFuture != null) {
            completableFutureList.add(attitudeDistFuture);
        }

        var priceOptionDistFuture = attachPriceOptionIntimateDistribution(opinionDistribution, studentId, questionId);
        if (priceOptionDistFuture != null) {
            completableFutureList.add(priceOptionDistFuture);
        }

        var lengthOptionDistFuture = attachLengthOptionIntimateDistribution(opinionDistribution, studentId, questionId);
        if (lengthOptionDistFuture != null) {
            completableFutureList.add(lengthOptionDistFuture);
        }

        // TODO 获取看法结果在亲密同学中的分布

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        if (opinionDistribution.getAttitudeIntimateDist().size() == 0 ||
                opinionDistribution.getPriceOptionIntimateDist().size() == 0 ||
                opinionDistribution.getLengthOptionIntimateDist().size() == 0) {
            return response("由于您或与您亲密度较高的同学/舍友/班级同学未填写前置问卷，此项暂无数据", opinionDistribution);
        }

        return response(opinionDistribution);
    }

    public CompletableFuture<Void> attachAttitudeOverallDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer questionId, AttitudeQuestionVO attitudeQuestion) {
        if (attitudeQuestion == null) {
            return null;
        }

        val nbq = attitudeQuestion.getNumberBoundaryQuestion();
        if (nbq == null) {
            return null;
        }

        List<OpinionCountVO> result = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (var i = nbq.getMin(); i <= nbq.getMax(); i++) {
            val finalI = i;
            completableFutureList.add(questionNodeRepository.countByQuestionIdAndAttitude(questionId, i).whenComplete((count, throwable) -> {
                if (count > 0) {
                    var o = new OpinionCountVO(finalI.toString(), count);
                    result.add(o);
                }
            }));
        }

        return CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()])).whenComplete((v, throwable) -> {
            var sortedResult = result.stream().sorted(Comparator.comparing(OpinionCountVO::getCount, Comparator.reverseOrder()).thenComparing(OpinionCountVO::getName)).collect(Collectors.toList());
            opinionDistribution.setAttitudeOverallDist(sortedResult);
        });
    }

    public CompletableFuture<Void> attachPriceOptionOverallDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer questionId, PriceQuestionVO priceQuestion) {
        if (priceQuestion == null) {
            return null;
        }

        val oq = priceQuestion.getOptionQuestion();
        if (oq == null) {
            return null;
        }

        List<OpinionCountVO> result = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (val option : oq.getOption()) {
            completableFutureList.add(questionNodeRepository.countByQuestionIdAndPriceOption(questionId, Neo4jHelper.buildPriceOptionString(option.getOptionKey(), option.getOptionValue())).whenComplete((count, throwable) -> {
                if (count > 0) {
                    var o = new OpinionCountVO(option.getOptionValue(), count);
                    result.add(o);
                }
            }));
        }

        return CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()])).whenComplete((v, throwable) -> {
            var sortedResult = result.stream().sorted(Comparator.comparing(OpinionCountVO::getCount, Comparator.reverseOrder()).thenComparing(OpinionCountVO::getName)).collect(Collectors.toList());
            opinionDistribution.setPriceOptionOverallDist(sortedResult);
        });
    }

    public CompletableFuture<Void> attachLengthOptionOverallDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer questionId, LengthQuestionVO lengthQuestion) {
        if (lengthQuestion == null) {
            return null;
        }

        val oq = lengthQuestion.getOptionQuestion();
        if (oq == null) {
            return null;
        }

        List<OpinionCountVO> result = new ArrayList<>();
        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (val option : oq.getOption()) {
            completableFutureList.add(questionNodeRepository.countByQuestionIdAndLengthOption(questionId, Neo4jHelper.buildLengthOptionString(option.getOptionKey(), option.getOptionValue())).whenComplete((count, throwable) -> {
                if (count > 0) {
                    var o = new OpinionCountVO(option.getOptionValue(), count);
                    result.add(o);
                }
            }));
        }

        return CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()])).whenComplete((v, throwable) -> {
            var sortedResult = result.stream().sorted(Comparator.comparing(OpinionCountVO::getCount, Comparator.reverseOrder()).thenComparing(OpinionCountVO::getName)).collect(Collectors.toList());
            opinionDistribution.setLengthOptionOverallDist(sortedResult);
        });
    }

    public CompletableFuture<Void> attachViewOverallDistribution() {
        // TODO 获取看法结果整体分布

        return null;
    }

    public CompletableFuture<List<MapValue>> attachAttitudeIntimateDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer studentId, Integer questionId) {
        return questionNodeRepository.getIntimateOpinionByStudentIdAndQuestionId(studentId, questionId).whenComplete((v, throwable) -> {
            List<OpinionCountVO> opinionCountList = new ArrayList<>();
            List<IntimateOpinionItemVO> rawOpinionList = new ArrayList<>();

            for (val mapValue : v) {
                rawOpinionList.add(ObjectHelper.buildObjectFromMap(mapValue.asMap(), IntimateOpinionItemVO.class));
            }

            var finalOpinionList = extractIntimateTopOpinionItem(rawOpinionList);
            Map<Integer, int[]> attitudeCounter = new HashMap<>();

            for (val opinion : finalOpinionList) {
                int[] count = attitudeCounter.get(opinion.getAttitude());
                if (count == null) {
                    attitudeCounter.put(opinion.getAttitude(), new int[]{1});
                } else {
                    count[0]++;
                }
            }

            attitudeCounter.forEach((key, value) -> {
                var o = new OpinionCountVO(key.toString(), value[0]);
                opinionCountList.add(o);
            });

            opinionDistribution.setAttitudeIntimateDist(opinionCountList);
        });
    }

    public CompletableFuture<List<MapValue>> attachPriceOptionIntimateDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer studentId, Integer questionId) {
        return questionNodeRepository.getIntimateOpinionByStudentIdAndQuestionId(studentId, questionId).whenComplete((v, throwable) -> {
            List<OpinionCountVO> opinionCountList = new ArrayList<>();
            List<IntimateOpinionItemVO> rawOpinionList = new ArrayList<>();

            for (val mapValue : v) {
                rawOpinionList.add(ObjectHelper.buildObjectFromMap(mapValue.asMap(), IntimateOpinionItemVO.class));
            }

            var finalOpinionList = extractIntimateTopOpinionItem(rawOpinionList);
            Map<String, int[]> priceOptionCounter = new HashMap<>();

            for (val opinion : finalOpinionList) {
                int[] count = priceOptionCounter.get(opinion.getPriceOption());
                if (count == null) {
                    priceOptionCounter.put(opinion.getPriceOption(), new int[]{1});
                } else {
                    count[0]++;
                }
            }

            priceOptionCounter.forEach((key, value) -> {
                if (key != null) {
                    var o = new OpinionCountVO(Neo4jHelper.parsePriceOptionString(key)[1], value[0]);
                    opinionCountList.add(o);
                }
            });

            opinionDistribution.setPriceOptionIntimateDist(opinionCountList);
        });
    }

    public CompletableFuture<List<MapValue>> attachLengthOptionIntimateDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer studentId, Integer questionId) {
        return questionNodeRepository.getIntimateOpinionByStudentIdAndQuestionId(studentId, questionId).whenComplete((v, throwable) -> {
            List<OpinionCountVO> opinionCountList = new ArrayList<>();
            List<IntimateOpinionItemVO> rawOpinionList = new ArrayList<>();

            for (val mapValue : v) {
                rawOpinionList.add(ObjectHelper.buildObjectFromMap(mapValue.asMap(), IntimateOpinionItemVO.class));
            }

            var finalOpinionList = extractIntimateTopOpinionItem(rawOpinionList);
            Map<String, int[]> LengthOptionCounter = new HashMap<>();

            for (val opinion : finalOpinionList) {
                int[] count = LengthOptionCounter.get(opinion.getLengthOption());
                if (count == null) {
                    LengthOptionCounter.put(opinion.getLengthOption(), new int[]{1});
                } else {
                    count[0]++;
                }
            }

            LengthOptionCounter.forEach((key, value) -> {
                if (key != null) {
                    var o = new OpinionCountVO(Neo4jHelper.parseLengthOptionString(key)[1], value[0]);
                    opinionCountList.add(o);
                }
            });

            opinionDistribution.setLengthOptionIntimateDist(opinionCountList);
        });
    }

    public CompletableFuture<Void> attachViewIntimateDistribution() {
        // TODO 获取看法结果在亲密同学中的分布

        return null;
    }

    private List<IntimateOpinionItemVO> extractIntimateTopOpinionItem(List<IntimateOpinionItemVO> rawOpinionList) {
        var dedupedOpinionList = rawOpinionList.stream().sorted(IntimateOpinionItemVO::compareTo).filter(ObjectHelper.distinctByKey(IntimateOpinionItemVO::getStudentId)).collect(Collectors.toList());
        var validOpinionCount = dedupedOpinionList.size();
        var needCount = Math.ceil(validOpinionCount * 0.2);
        return dedupedOpinionList.stream().limit((int) needCount).collect(Collectors.toList());
    }

}
