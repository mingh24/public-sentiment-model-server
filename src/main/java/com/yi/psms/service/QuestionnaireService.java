package com.yi.psms.service;

import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.dao.QuestionNodeRepository;
import com.yi.psms.dao.StudentNodeRepository;
import com.yi.psms.model.entity.node.QuestionNode;
import com.yi.psms.model.vo.question.*;
import com.yi.psms.model.entity.question.OptionQuestion;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.questionnaire.FriendItemVO;
import com.yi.psms.model.vo.questionnaire.OpinionItemVO;
import com.yi.psms.model.vo.questionnaire.SubmissionVO;
import com.yi.psms.util.ListHelper;
import com.yi.psms.util.Neo4jHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionnaireService extends BaseService {

    private final StudentNodeRepository studentNodeRepository;

    private final QuestionNodeRepository questionNodeRepository;

    public QuestionnaireService(StudentNodeRepository studentNodeRepository, QuestionNodeRepository questionNodeRepository) {
        this.studentNodeRepository = studentNodeRepository;
        this.questionNodeRepository = questionNodeRepository;
    }

    @Transactional
    public ResponseVO essential(SubmissionVO submission) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 判断填写人信息是否存在
        Integer studentId = submission.getStudentId();
        if (studentNodeRepository.findByStudentId(studentId) == null) {
            log.warn("student {} does not exist", studentId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", studentId));
        }

        // 判断问题信息是否存在
        OpinionItemVO opinionItem = submission.getOpinionItem();
        Integer questionId = opinionItem.getQuestionId();
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("student {} answered nonexistent question, questionId: {}", studentId, questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 校验班级同学亲密度问题
        QuestionContentVO questionContent = QuestionContentVO.buildFromContentString(questionNode.getContent());
        ClassmateIntimacyQuestion classmateIntimacyQuestion = questionContent.getClassmateIntimacyQuestion();
        if (submission.getClassmateIntimacy() < classmateIntimacyQuestion.getNumberBoundaryQuestion().getMin() || submission.getClassmateIntimacy() > classmateIntimacyQuestion.getNumberBoundaryQuestion().getMax()) {
            log.warn("student {} set invalid classmate intimacy, questionId: {}, intimacy: {}", studentId, questionId, submission.getClassmateIntimacy());
            return failResponse(ResponseStatus.FAIL, String.format("班级同学亲密度问题亲密度有误：%s", submission.getClassmateIntimacy()));
        }

        // 校验舍友亲密度问题
        RoommateIntimacyQuestion roommateIntimacyQuestion = questionContent.getRoommateIntimacyQuestion();
        if (submission.getRoommateIntimacy() < roommateIntimacyQuestion.getNumberBoundaryQuestion().getMin() || submission.getRoommateIntimacy() > roommateIntimacyQuestion.getNumberBoundaryQuestion().getMax()) {
            log.warn("student {} set invalid roommate intimacy, questionId: {}, intimacy: {}", studentId, questionId, submission.getRoommateIntimacy());
            return failResponse(ResponseStatus.FAIL, String.format("舍友亲密度问题亲密度有误：%s", submission.getRoommateIntimacy()));
        }

        // 校验好友亲密度问题
        FriendIntimacyQuestion friendIntimacyQuestion = questionContent.getFriendIntimacyQuestion();
        List<FriendItemVO> friendItemList = submission.getFriendItemList();
        var duplicateFriendItemList = ListHelper.extractDuplicateElements(friendItemList, FriendItemVO::getStudentId);
        if (duplicateFriendItemList.size() > 0) {
            List<String> studentIdStringList = duplicateFriendItemList.stream()
                    .map(f -> f.getStudentId().toString())
                    .collect(Collectors.toList());
            log.warn("student {} entered duplicate friends, duplicate friends: {}", studentId, String.join("、", studentIdStringList));
            return failResponse(ResponseStatus.FAIL, String.format("好友重复：%s", String.join("、", studentIdStringList)));
        }

        for (val friendItem : friendItemList) {
            // 好友不能填自己
            if (friendItem.getStudentId().equals(studentId)) {
                log.warn("student {} entered itself as a friend", studentId);
                return failResponse(ResponseStatus.FAIL, "好友不能为自己");
            }

            // 好友必须存在
            if (studentNodeRepository.findByStudentId(friendItem.getStudentId()) == null) {
                log.warn("student {} entered nonexistent friend, studentId: {}", studentId, friendItem.getStudentId());
                return failResponse(ResponseStatus.FAIL, String.format("无好友的学生信息：%d", friendItem.getStudentId()));
            }

            // 校验好友亲密度
            if (friendItem.getIntimacy() < friendIntimacyQuestion.getNumberBoundaryQuestion().getMin() || friendItem.getIntimacy() > friendIntimacyQuestion.getNumberBoundaryQuestion().getMax()) {
                log.warn("student {} set invalid friend intimacy, questionId: {}, intimacy: {}", studentId, questionId, friendItem.getIntimacy());
                return failResponse(ResponseStatus.FAIL, String.format("好友亲密度有误：%s", friendItem.getIntimacy()));
            }
        }

        // 校验观点支持度问题
        AttitudeQuestionVO attitudeQuestion = questionContent.getAttitudeQuestion();
        if (opinionItem.getAttitude() < attitudeQuestion.getNumberBoundaryQuestion().getMin() || opinionItem.getAttitude() > attitudeQuestion.getNumberBoundaryQuestion().getMax()) {
            log.warn("student {} set invalid attitude, questionId: {}, attitude: {}", studentId, questionId, opinionItem.getAttitude());
            return failResponse(ResponseStatus.FAIL, String.format("观点支持度问题支持度有误：%s", opinionItem.getAttitude()));
        }

        // 校验价格问题
        PriceQuestionVO priceQuestion = questionContent.getPriceQuestion();
        if (opinionItem.getAttitude() > priceQuestion.getAttitudeThreshold()) {
            if (getOptionByOptionKey(priceQuestion.getOptionQuestion().getOption(), opinionItem.getPriceOptionKey()) == null) {
                log.warn("student {} picked nonexistent price option key, questionId: {}, price option key: {}", studentId, questionId, opinionItem.getPriceOptionKey());
                return failResponse(ResponseStatus.FAIL, String.format("价格问题选项有误：%s", opinionItem.getPriceOptionKey()));
            }
        }

        // 校验时长问题
        LengthQuestionVO lengthQuestion = questionContent.getLengthQuestion();
        if (opinionItem.getAttitude() > lengthQuestion.getAttitudeThreshold()) {
            if (getOptionByOptionKey(lengthQuestion.getOptionQuestion().getOption(), opinionItem.getLengthOptionKey()) == null) {
                log.warn("student {} picked nonexistent length option key, questionId: {}, length option key: {}", studentId, questionId, opinionItem.getLengthOptionKey());
                return failResponse(ResponseStatus.FAIL, String.format("时长问题选项有误：%s", opinionItem.getLengthOptionKey()));
            }
        }

        // 设置班级同学亲密度
        Integer affectedClassmateCount = studentNodeRepository.setClassmateIntimacy(studentId, submission.getClassmateIntimacy(), currentDateTime);
        log.info("student {} affected classmate relationship count: {}, intimacy: {}", studentId, affectedClassmateCount, submission.getClassmateIntimacy());

        // 设置舍友亲密度
        Integer affectedRoommateCount = studentNodeRepository.setRoommateIntimacy(studentId, submission.getRoommateIntimacy(), currentDateTime);
        log.info("student {} affected roommate relationship count: {}, intimacy: {}", studentId, affectedRoommateCount, submission.getRoommateIntimacy());

        // 删除已存在的好友关系
        Integer deletedFriendCount = studentNodeRepository.deleteFriend(studentId);
        log.info("student {} deleted friend relationship count: {}", studentId, deletedFriendCount);

        // 设置好友亲密度
        Integer createdFriendCount = 0;
        for (val friendItem : friendItemList) {
            createdFriendCount += studentNodeRepository.setFriendIntimacy(studentId, friendItem.getStudentId(), friendItem.getIntimacy(), currentDateTime);
            log.info("student {} created friend relationship number {} with student {}, intimacy: {}", studentId, createdFriendCount, friendItem.getStudentId(), friendItem.getIntimacy());
        }

        // 删除已存在的意见
        Integer deletedOpinionCount = studentNodeRepository.deleteOpinion(studentId, questionId);
        log.info("student {} deleted opinion relationship count {}, question id: {}", studentId, deletedOpinionCount, questionId);

        // 设置意见
        String priceOption = buildPriceOption(priceQuestion.getOptionQuestion().getOption(), opinionItem.getPriceOptionKey());
        String lengthOption = buildLengthOption(lengthQuestion.getOptionQuestion().getOption(), opinionItem.getLengthOptionKey());
        Integer createdOpinionCount = studentNodeRepository.setOpinion(studentId, questionId, opinionItem.getAttitude(), priceOption, lengthOption, opinionItem.getView(), currentDateTime);
        log.info("student {} created opinion relationship count {} with question {}, attitude: {}, price option: {}, length option: {}, view: {}", studentId, createdOpinionCount, questionId, opinionItem.getAttitude(), priceOption, lengthOption, opinionItem.getView());

        return response();
    }

    @Transactional
    public ResponseVO advanced(SubmissionVO submission) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 判断填写人信息是否存在
        Integer studentId = submission.getStudentId();
        if (studentNodeRepository.findByStudentId(studentId) == null) {
            log.warn("student {} does not exist", studentId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", studentId));
        }

        // 判断问题信息是否存在
        OpinionItemVO opinionItem = submission.getOpinionItem();
        Integer questionId = opinionItem.getQuestionId();
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("student {} answered nonexistent question, questionId: {}", studentId, questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 校验观点支持度问题
        QuestionContentVO questionContent = QuestionContentVO.buildFromContentString(questionNode.getContent());
        AttitudeQuestionVO attitudeQuestion = questionContent.getAttitudeQuestion();
        if (opinionItem.getAttitude() < attitudeQuestion.getNumberBoundaryQuestion().getMin() || opinionItem.getAttitude() > attitudeQuestion.getNumberBoundaryQuestion().getMax()) {
            log.warn("student {} set invalid attitude, questionId: {}, attitude: {}", studentId, questionId, opinionItem.getAttitude());
            return failResponse(ResponseStatus.FAIL, String.format("观点支持度问题支持度有误：%s", opinionItem.getAttitude()));
        }

        // 校验价格问题
        PriceQuestionVO priceQuestion = questionContent.getPriceQuestion();
        if (opinionItem.getAttitude() > priceQuestion.getAttitudeThreshold()) {
            if (getOptionByOptionKey(priceQuestion.getOptionQuestion().getOption(), opinionItem.getPriceOptionKey()) == null) {
                log.warn("student {} picked nonexistent price option key, questionId: {}, price option key: {}", studentId, questionId, opinionItem.getPriceOptionKey());
                return failResponse(ResponseStatus.FAIL, String.format("价格问题选项有误：%s", opinionItem.getPriceOptionKey()));
            }
        }

        // 校验时长问题
        LengthQuestionVO lengthQuestion = questionContent.getLengthQuestion();
        if (opinionItem.getAttitude() > lengthQuestion.getAttitudeThreshold()) {
            if (getOptionByOptionKey(lengthQuestion.getOptionQuestion().getOption(), opinionItem.getLengthOptionKey()) == null) {
                log.warn("student {} picked nonexistent length option key, questionId: {}, length option key: {}", studentId, questionId, opinionItem.getLengthOptionKey());
                return failResponse(ResponseStatus.FAIL, String.format("时长问题选项有误：%s", opinionItem.getLengthOptionKey()));
            }
        }

        // 删除已存在的意见
        Integer deletedOpinionCount = studentNodeRepository.deleteOpinion(studentId, questionId);
        log.info("student {} deleted opinion relationship count {}, question id: {}", studentId, deletedOpinionCount, questionId);

        // 设置意见
        String priceOption = buildPriceOption(priceQuestion.getOptionQuestion().getOption(), opinionItem.getPriceOptionKey());
        String lengthOption = buildLengthOption(lengthQuestion.getOptionQuestion().getOption(), opinionItem.getLengthOptionKey());
        Integer createdOpinionCount = studentNodeRepository.setOpinion(studentId, questionId, opinionItem.getAttitude(), priceOption, lengthOption, opinionItem.getView(), currentDateTime);
        log.info("student {} created opinion relationship count {} with question {}, attitude: {}, price option: {}, length option: {}, view: {}", studentId, createdOpinionCount, questionId, opinionItem.getAttitude(), priceOption, lengthOption, opinionItem.getView());

        return response();
    }

    private OptionQuestion.Option getOptionByOptionKey(List<OptionQuestion.Option> optionList, String optionKey) {
        for (val o : optionList) {
            if (o.getOptionKey().equals(optionKey)) {
                return o;
            }
        }

        return null;
    }

    private String buildPriceOption(List<OptionQuestion.Option> optionList, String optionKey) {
        var o = getOptionByOptionKey(optionList, optionKey);
        if (o == null) {
            return null;
        }

        return Neo4jHelper.buildPriceOptionString(o.getOptionKey(), o.getOptionValue());
    }

    private String buildLengthOption(List<OptionQuestion.Option> optionList, String optionKey) {
        var o = getOptionByOptionKey(optionList, optionKey);
        if (o == null) {
            return null;
        }

        return Neo4jHelper.buildLengthOptionString(o.getOptionKey(), o.getOptionValue());
    }

}
