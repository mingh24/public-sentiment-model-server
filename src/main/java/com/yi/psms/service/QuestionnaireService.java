package com.yi.psms.service;

import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.dao.QuestionNodeRepository;
import com.yi.psms.dao.StudentNodeRepository;
import com.yi.psms.model.entity.StudentNode;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.questionnaire.FriendItem;
import com.yi.psms.model.vo.questionnaire.OpinionItem;
import com.yi.psms.model.vo.questionnaire.Submission;
import lombok.extern.slf4j.Slf4j;
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
        Integer studentId = submission.getStudentId();
        String name = submission.getName();

        // 判断填写人信息是否存在
        if (studentNodeRepository.findByStudentIdAndName(studentId, name) == null) {
            log.warn("student {}-{} does not exists", studentId, name);
            return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s-%s", studentId, name));
        }

        // 设置班级同学亲密度
        Integer affectedClassmateCount = studentNodeRepository.setClassmateIntimacy(studentId, submission.getClassmateIntimacy(), currentDateTime);
        log.info("student {}-{} affected classmate relationship count: {}, intimacy: {}", studentId, name, affectedClassmateCount, submission.getClassmateIntimacy());

        // 设置舍友亲密度
        Integer affectedRoommateCount = studentNodeRepository.setRoommateIntimacy(studentId, submission.getRoommateIntimacy(), currentDateTime);
        log.info("student {}-{} affected roommate relationship count: {}, intimacy: {}", studentId, name, affectedRoommateCount, submission.getRoommateIntimacy());

        // 删除已存在的好友关系
        Integer deletedFriendCount = studentNodeRepository.deleteFriend(studentId);
        log.info("student {}-{} deleted friend relationship count: {}", studentId, name, deletedFriendCount);

        List<FriendItem> friendItemList = submission.getFriendItemList();

        // 判断好友信息是否存在
        for (FriendItem friendItem : friendItemList) {
            List<StudentNode> studentNodeList = studentNodeRepository.findByName(friendItem.getName());
            if (studentNodeList.size() <= 0) {
                log.warn("student {}-{} entered nonexistent friend, name: {}", studentId, name, friendItem.getName());
                return failResponse(ResponseStatus.FAIL, String.format("无对应学生信息：%s", friendItem.getName()));
            }

            // 好友不能填自己
            for (StudentNode studentNode : studentNodeList) {
                if (studentNode.getStudentId().equals(studentId) && studentNode.getName().equals(name)) {
                    log.warn("student {}-{} entered itself as a friend", studentId, name);
                    return failResponse(ResponseStatus.FAIL, "亲密好友不能填自己");
                }
            }
        }

        Integer createdFriendCount = 0;

        // 设置好友亲密度
        for (FriendItem friendItem : friendItemList) {
            createdFriendCount += studentNodeRepository.setFriendIntimacy(studentId, friendItem.getName(), friendItem.getIntimacy(), currentDateTime);
            log.info("student {}-{} created friend relationship count {} with student {}, intimacy: {}", studentId, name, createdFriendCount, friendItem.getName(), friendItem.getIntimacy());
        }

        OpinionItem opinionItem = submission.getOpinionItem();
        Integer questionId = opinionItem.getQuestionId();

        // 判断问题信息是否存在
        if (questionNodeRepository.findByQuestionId(questionId) == null) {
            log.warn("student {}-{} answered nonexistent question, questionId: {}", studentId, name, questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        // 删除已存在的意见
        Integer deletedOpinionCount = studentNodeRepository.deleteOpinion(studentId, questionId);
        log.info("student {}-{} deleted opinion relationship count {}, question id: {}", studentId, name, deletedOpinionCount, questionId);

        // 设置意见
        Integer createdOpinionCount = studentNodeRepository.setOpinion(studentId, questionId, opinionItem.getAttitude(), opinionItem.getOpinion(), currentDateTime);
        log.info("student {}-{} created opinion relationship count {} with question {}, attitude: {}, opinion: {}", studentId, name, createdOpinionCount, questionId, opinionItem.getAttitude(), opinionItem.getOpinion());

        return response();
    }

    @Transactional
    public ResponseVO advanced(Submission submission) {
        // TODO

        return response();
    }

}
