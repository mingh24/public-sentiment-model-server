package com.yi.psms.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.dao.QuestionNodeRepository;
import com.yi.psms.dao.StudentNodeRepository;
import com.yi.psms.dao.ViewKeywordCountNodeRepository;
import com.yi.psms.model.entity.node.QuestionNode;
import com.yi.psms.model.entity.node.ViewKeywordCountNode;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.opinion.IntimateOpinionItemVO;
import com.yi.psms.model.vo.opinion.OpinionCountVO;
import com.yi.psms.model.vo.opinion.OpinionDistributionVO;
import com.yi.psms.model.vo.opinion.ViewDistributionVO;
import com.yi.psms.util.HanLPHelper;
import com.yi.psms.util.Neo4jHelper;
import com.yi.psms.util.ObjectHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.neo4j.driver.internal.value.MapValue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OpinionService extends BaseService {

    private final StudentNodeRepository studentNodeRepository;

    private final QuestionNodeRepository questionNodeRepository;

    private final ViewKeywordCountNodeRepository viewKeywordCountNodeRepository;

    public OpinionService(StudentNodeRepository studentNodeRepository, QuestionNodeRepository questionNodeRepository, ViewKeywordCountNodeRepository viewKeywordCountNodeRepository) {
        this.studentNodeRepository = studentNodeRepository;
        this.questionNodeRepository = questionNodeRepository;
        this.viewKeywordCountNodeRepository = viewKeywordCountNodeRepository;
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

        // 获取价格问题结果整体分布
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

        // 获取看法问题结果整体分布
        OpinionDistributionVO opinionDistribution = new OpinionDistributionVO();
        List<CompletableFuture<List<MapValue>>> completableFutureList = new ArrayList<>();

        var viewDistFuture = attachViewOverallDistribution(opinionDistribution, questionId);
        if (viewDistFuture != null) {
            completableFutureList.add(viewDistFuture);
        }

        var allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        allFuture.join();

        return response(opinionDistribution);
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

        var viewDistFuture = attachViewOverallDistribution(opinionDistribution, questionId);
        if (viewDistFuture != null) {
            completableFutureList.add(viewDistFuture);
        }

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

        // 获取价格问题结果在亲密同学中的分布
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

            opinionCountList = opinionCountList.stream()
                    .filter(o -> o.getName() != null && o.getCount() != null)
                    .sorted(Comparator.comparing(OpinionCountVO::getCount, Comparator.reverseOrder())
                            .thenComparing(OpinionCountVO::getName, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
            opinionCountList.forEach(o -> o.setName(Neo4jHelper.parsePriceOptionString(o.getName())[1]));
            opinionDistribution.setPriceOptionOverallDist(opinionCountList);
        });
    }

    public CompletableFuture<List<MapValue>> attachLengthOptionOverallDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer questionId) {
        return questionNodeRepository.countOpinionByLengthOption(questionId).whenComplete((v, throwable) -> {
            List<OpinionCountVO> opinionCountList = new ArrayList<>();

            for (val mapValue : v) {
                opinionCountList.add(ObjectHelper.buildObjectFromMap(mapValue.asMap(), OpinionCountVO.class));
            }

            opinionCountList = opinionCountList.stream()
                    .filter(o -> o.getName() != null && o.getCount() != null)
                    .sorted(Comparator.comparing(OpinionCountVO::getCount, Comparator.reverseOrder())
                            .thenComparing(OpinionCountVO::getName, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
            opinionCountList.forEach(o -> o.setName(Neo4jHelper.parseLengthOptionString(o.getName())[1]));
            opinionDistribution.setLengthOptionOverallDist(opinionCountList);
        });
    }

    public CompletableFuture<List<MapValue>> attachViewOverallDistribution(@NonNull OpinionDistributionVO opinionDistribution, Integer questionId) {
        var viewKeywordCountNode = viewKeywordCountNodeRepository.findFirstByQuestionIdOrderByUpdatedAtDesc(questionId);
        if (viewKeywordCountNode == null) {
            return CompletableFuture.completedFuture(null);
        }

        var gson = new Gson();
        Map<String, Integer> counter = gson.fromJson(viewKeywordCountNode.getKeywordCount(), new TypeToken<HashMap<String, Integer>>() {
        }.getType());
        List<OpinionCountVO> keywordCount = new ArrayList<>();

        counter.forEach((key, value) -> {
            if (key != null) {
                var o = new OpinionCountVO(key, value);
                keywordCount.add(o);
            }
        });

        var sortedKeywordCount = keywordCount.stream().sorted(Comparator.comparing(OpinionCountVO::getCount).thenComparing(OpinionCountVO::getName)).collect(Collectors.toList());

        if (opinionDistribution.getViewOverallDist() == null) {
            opinionDistribution.setViewOverallDist(new ViewDistributionVO());
        }

        opinionDistribution.getViewOverallDist().setKeywordCount(sortedKeywordCount);
        return CompletableFuture.completedFuture(null);
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

    @Scheduled(cron = "0 0 1/2 * * *")
    public void routineDumpOpinionViewKeywordCount() {
        var sw = new StopWatch();

        sw.start("dump opinion view keyword count for question 3");
        dumpOpinionViewKeywordCount(3);
        sw.stop();

        log.info(sw.prettyPrint());
    }

    public void dumpOpinionViewKeywordCount(Integer questionId) {
        var counter = countOpinionViewKeyword(questionId);
        Map<String, Integer> finalCounter = new HashMap<>();

        counter.forEach((key, value) -> finalCounter.put(key, value[0]));

        var gson = new Gson();
        var keywordCountString = gson.toJson(finalCounter);
        var currentDateTime = LocalDateTime.now();
        var viewKeywordCountNode = new ViewKeywordCountNode(questionId, keywordCountString, currentDateTime, currentDateTime);
        viewKeywordCountNodeRepository.save(viewKeywordCountNode);
    }

    public Map<String, int[]> countOpinionViewKeyword(Integer questionId) {
        List<String> viewList = new ArrayList<>();
        var mapValueList = questionNodeRepository.getOpinionView(questionId);

        for (val m : mapValueList) {
            viewList.add(m.get("view").asString());
        }

        Map<String, int[]> keywordCounter = new HashMap<>();

        for (val view : viewList) {
            var keywordList = HanLPHelper.segmentStopWordRemoved(view);

            for (val keyword : keywordList) {
                int[] count = keywordCounter.get(keyword);
                if (count == null) {
                    keywordCounter.put(keyword, new int[]{1});
                } else {
                    count[0]++;
                }
            }
        }

        return keywordCounter;
    }

}
