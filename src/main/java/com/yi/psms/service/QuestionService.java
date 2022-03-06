package com.yi.psms.service;

import com.yi.psms.constant.ResponseStatus;
import com.yi.psms.dao.QuestionNodeRepository;
import com.yi.psms.model.entity.QuestionNode;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.question.QuestionItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QuestionService extends BaseService {

    private final QuestionNodeRepository questionNodeRepository;

    public QuestionService(QuestionNodeRepository questionNodeRepository) {
        this.questionNodeRepository = questionNodeRepository;
    }

    public ResponseVO getQuestionByQuestionId(Integer questionId) {
        // 判断问题信息是否存在
        QuestionNode questionNode = questionNodeRepository.findByQuestionId(questionId);
        if (questionNode == null) {
            log.warn("nonexistent question, questionId: {}", questionId);
            return failResponse(ResponseStatus.FAIL, String.format("无对应问题信息：%s", questionId));
        }

        return response(QuestionItem.buildFromNode(questionNode));
    }

    public ResponseVO getAllQuestions() {
        return response(QuestionItem.buildListFromNode(questionNodeRepository.findAll()));
    }

}
