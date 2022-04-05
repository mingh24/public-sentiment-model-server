package com.yi.psms.service;

import com.google.gson.Gson;
import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.dao.QuestionNodeRepository;
import com.yi.psms.dao.StudentNodeRepository;
import com.yi.psms.model.entity.node.QuestionNode;
import com.yi.psms.model.entity.node.StudentNode;
import com.yi.psms.model.vo.question.LengthQuestionVO;
import com.yi.psms.model.entity.question.OptionQuestion;
import com.yi.psms.model.vo.question.PriceQuestionVO;
import com.yi.psms.model.vo.question.QuestionContentVO;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.questionnaire.FriendItem;
import com.yi.psms.model.vo.questionnaire.OpinionItem;
import com.yi.psms.model.vo.questionnaire.Submission;
import com.yi.psms.util.Neo4jHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseVO essential(Submission submission) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 判断填写人信息是否存在
        Integer studentId = submission.getStudentId();
        if (studentNodeRepository.findByStudentId(studentId) == null) {
            log.warn("student {} does not exist", studentId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", studentId));
        }

        // 判断好友信息是否存在
        List<FriendItem> friendItemList = submission.getFriendItemList();
        for (val friendItem : friendItemList) {
            List<StudentNode> studentNodeList = studentNodeRepository.findByName(friendItem.getName());
            if (studentNodeList.size() <= 0) {
                log.warn("student {} entered nonexistent friend, name: {}", studentId, friendItem.getName());
                return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", friendItem.getName()));
            }

            // 好友不能填自己
            for (val studentNode : studentNodeList) {
                if (studentNode.getStudentId().equals(studentId)) {
                    log.warn("student {} entered itself as a friend", studentId);
                    return failResponse(ResponseStatus.FAIL, "亲密好友不能填自己");
                }
            }
        }

        // 判断问题信息是否存在
        OpinionItem opinionItem = submission.getOpinionItem();
        Integer questionId = opinionItem.getQuestionId();
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("student {} answered nonexistent question, questionId: {}", studentId, questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 判断额外问题是否已填写
        Gson gson = new Gson();
        QuestionContentVO questionContent = gson.fromJson(questionNode.getContent(), QuestionContentVO.class);
        PriceQuestionVO priceQuestion = questionContent.getPriceQuestion();
        LengthQuestionVO lengthQuestion = questionContent.getLengthQuestion();

        if (opinionItem.getAttitude() > priceQuestion.getAttitudeThreshold()) {
            if (getOptionByOptionKey(priceQuestion.getOptionQuestion().getOption(), opinionItem.getPriceOptionKey()) == null) {
                log.warn("student {} picked nonexistent price option key, questionId: {}, price option key: {}", studentId, questionId, opinionItem.getPriceOptionKey());
                return failResponse(ResponseStatus.FAIL, String.format("价格问题选项有误：%s", opinionItem.getPriceOptionKey()));
            }
        }

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
            createdFriendCount += studentNodeRepository.setFriendIntimacy(studentId, friendItem.getName(), friendItem.getIntimacy(), currentDateTime);
            log.info("student {} created friend relationship count {} with student {}, intimacy: {}", studentId, createdFriendCount, friendItem.getName(), friendItem.getIntimacy());
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
    public ResponseVO advanced(Submission submission) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 判断填写人信息是否存在
        Integer studentId = submission.getStudentId();
        if (studentNodeRepository.findByStudentId(studentId) == null) {
            log.warn("student {} does not exist", studentId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", studentId));
        }

        // 判断问题信息是否存在
        OpinionItem opinionItem = submission.getOpinionItem();
        Integer questionId = opinionItem.getQuestionId();
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("student {} answered nonexistent question, questionId: {}", studentId, questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 判断额外问题是否已填写
        Gson gson = new Gson();
        QuestionContentVO questionContent = gson.fromJson(questionNode.getContent(), QuestionContentVO.class);
        PriceQuestionVO priceQuestion = questionContent.getPriceQuestion();
        LengthQuestionVO lengthQuestion = questionContent.getLengthQuestion();

        if (opinionItem.getAttitude() > priceQuestion.getAttitudeThreshold()) {
            if (getOptionByOptionKey(priceQuestion.getOptionQuestion().getOption(), opinionItem.getPriceOptionKey()) == null) {
                log.warn("student {} picked nonexistent price option key, questionId: {}, price option key: {}", studentId, questionId, opinionItem.getPriceOptionKey());
                return failResponse(ResponseStatus.FAIL, String.format("价格问题选项有误：%s", opinionItem.getPriceOptionKey()));
            }
        }

        if (opinionItem.getAttitude() > lengthQuestion.getAttitudeThreshold()) {
            if (getOptionByOptionKey(lengthQuestion.getOptionQuestion().getOption(), opinionItem.getLengthOptionKey()) == null) {
                log.warn("student {} picked nonexistent length option key, questionId: {}, length option key: {}", studentId, questionId, opinionItem.getLengthOptionKey());
                return failResponse(ResponseStatus.FAIL, String.format("时长问题选项有误：%s", opinionItem.getLengthOptionKey()));
            }
        }

        // TODO 针对后续阶段，判断看法问题是否已填写

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
