package com.yi.psms.service;

import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.dao.QuestionNodeRepository;
import com.yi.psms.dao.StudentNodeRepository;
import com.yi.psms.model.entity.node.QuestionNode;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.opinion.IntimateOpinionItemVO;
import com.yi.psms.model.vo.opinion.OpinionCountVO;
import com.yi.psms.model.vo.opinion.OpinionDistributionVO;
import com.yi.psms.model.vo.opinion.ViewDistributionVO;
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
        List<CompletableFuture<List<MapValue>>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachAttitudeOverallDistribution(opinionDistribution, questionId);
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
        List<CompletableFuture<List<MapValue>>> completableFutureList = new ArrayList<>();

        var priceOptionDistFuture = attachPriceOptionOverallDistribution(opinionDistribution, questionId);
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
        List<CompletableFuture<List<MapValue>>> completableFutureList = new ArrayList<>();

        var lengthOptionDistFuture = attachLengthOptionOverallDistribution(opinionDistribution, questionId);
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
        List<CompletableFuture<List<MapValue>>> completableFutureList = new ArrayList<>();

        var attitudeDistFuture = attachAttitudeOverallDistribution(opinionDistribution, questionId);
        if (attitudeDistFuture != null) {
            completableFutureList.add(attitudeDistFuture);
        }

        var priceOptionDistFuture = attachPriceOptionOverallDistribution(opinionDistribution, questionId);
        if (priceOptionDistFuture != null) {
            completableFutureList.add(priceOptionDistFuture);
        }

        var lengthOptionDistFuture = attachLengthOptionOverallDistribution(opinionDistribution, questionId);
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
            log.warn("student {} lacks the intimates opinion distribution of question {}", studentId, questionId);
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
            log.warn("student {} lacks the intimates opinion distribution of question {}", studentId, questionId);
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
            log.warn("student {} lacks the intimates opinion distribution of question {}", studentId, questionId);
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

        // 获取看法问题结果在亲密同学中的分布
        OpinionDistributionVO opinionDistribution = new OpinionDistributionVO();
        List<CompletableFuture<List<MapValue>>> completableFutureList = new ArrayList<>();

        var viewDistFuture = attachViewIntimateDistribution(opinionDistribution, studentId, questionId);
        if (viewDistFuture != null) {
            completableFutureList.add(viewDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        if (opinionDistribution.getViewIntimateDist() == null) {
            log.warn("student {} lacks the intimates opinion distribution of question {}", studentId, questionId);
            return response("由于您或与您亲密度较高的同学/舍友/班级同学未填写前置问卷，此项暂无数据", opinionDistribution);
        }

        return response(opinionDistribution);
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

        var viewDistFuture = attachViewIntimateDistribution(opinionDistribution, studentId, questionId);
        if (viewDistFuture != null) {
            completableFutureList.add(viewDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        if (opinionDistribution.getAttitudeIntimateDist().size() == 0 || opinionDistribution.getPriceOptionIntimateDist().size() == 0 || opinionDistribution.getLengthOptionIntimateDist().size() == 0 || opinionDistribution.getViewIntimateDist() == null) {
            log.warn("student {} lacks the intimates opinion distribution of question {}", studentId, questionId);
            return response("由于您或与您亲密度较高的同学/舍友/班级同学未填写前置问卷，此项暂无数据", opinionDistribution);
        }

        return response(opinionDistribution);
    }

    public CompletableFuture<List<MapValue>> attachAttitudeOverallDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer questionId) {
        return questionNodeRepository.countOpinionByAttitude(questionId).whenComplete((v, throwable) -> {
            List<OpinionCountVO> opinionCountList = new ArrayList<>();

            for (val mapValue : v) {
                opinionCountList.add(ObjectHelper.buildObjectFromMap(mapValue.asMap(), OpinionCountVO.class));
            }

            opinionCountList = opinionCountList.stream().filter(o -> o.getName() != null && o.getCount() != null).sorted(Comparator.comparing(OpinionCountVO::getCount, Comparator.reverseOrder()).thenComparing(OpinionCountVO::getName, Comparator.reverseOrder())).collect(Collectors.toList());
            opinionDistribution.setAttitudeOverallDist(opinionCountList);
        });
    }

    public CompletableFuture<List<MapValue>> attachPriceOptionOverallDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer questionId) {
        return questionNodeRepository.countOpinionByPriceOption(questionId).whenComplete((v, throwable) -> {
            List<OpinionCountVO> opinionCountList = new ArrayList<>();

            for (val mapValue : v) {
                opinionCountList.add(ObjectHelper.buildObjectFromMap(mapValue.asMap(), OpinionCountVO.class));
            }

            opinionCountList = opinionCountList.stream().filter(o -> o.getName() != null && o.getCount() != null).sorted(Comparator.comparing(OpinionCountVO::getCount, Comparator.reverseOrder()).thenComparing(OpinionCountVO::getName, Comparator.reverseOrder())).collect(Collectors.toList());
            opinionDistribution.setPriceOptionOverallDist(opinionCountList);
        });
    }

    public CompletableFuture<List<MapValue>> attachLengthOptionOverallDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer questionId) {
        return questionNodeRepository.countOpinionByLengthOption(questionId).whenComplete((v, throwable) -> {
            List<OpinionCountVO> opinionCountList = new ArrayList<>();

            for (val mapValue : v) {
                opinionCountList.add(ObjectHelper.buildObjectFromMap(mapValue.asMap(), OpinionCountVO.class));
            }

            opinionCountList = opinionCountList.stream().filter(o -> o.getName() != null && o.getCount() != null).sorted(Comparator.comparing(OpinionCountVO::getCount, Comparator.reverseOrder()).thenComparing(OpinionCountVO::getName, Comparator.reverseOrder())).collect(Collectors.toList());
            opinionDistribution.setLengthOptionOverallDist(opinionCountList);
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

    public CompletableFuture<List<MapValue>> attachViewIntimateDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer studentId, Integer questionId) {
        return questionNodeRepository.getIntimateOpinionByStudentIdAndQuestionId(studentId, questionId).whenComplete((v, throwable) -> {
            List<String> viewList = new ArrayList<>();
            List<IntimateOpinionItemVO> rawOpinionList = new ArrayList<>();

            for (val mapValue : v) {
                rawOpinionList.add(ObjectHelper.buildObjectFromMap(mapValue.asMap(), IntimateOpinionItemVO.class));
            }

            var finalOpinionList = extractIntimateTopOpinionItem(rawOpinionList);

            for (val opinion : finalOpinionList) {
                if (opinion.getView() != null) {
                    viewList.add(opinion.getView().trim());
                }
            }

            if (opinionDistribution.getViewIntimateDist() == null) {
                opinionDistribution.setViewIntimateDist(new ViewDistributionVO());
            }

            opinionDistribution.getViewIntimateDist().setViewList(viewList);
        });
    }

    private List<IntimateOpinionItemVO> extractIntimateTopOpinionItem(List<IntimateOpinionItemVO> rawOpinionList) {
        var dedupedOpinionList = rawOpinionList.stream().sorted(IntimateOpinionItemVO::compareTo).filter(ObjectHelper.distinctByKey(IntimateOpinionItemVO::getStudentId)).collect(Collectors.toList());
        var validOpinionCount = dedupedOpinionList.size();
        var needCount = Math.ceil(validOpinionCount * 0.2);
        return dedupedOpinionList.stream().limit((int) needCount).collect(Collectors.toList());
    }

}
