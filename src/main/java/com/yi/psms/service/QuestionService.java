package com.yi.psms.service;

import com.yi.psms.dao.QuestionNodeRepository;
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
        return response(QuestionItem.buildFromNode(questionNodeRepository.findByQuestionId(questionId)));
    }

    public ResponseVO getAllQuestions() {
        return response(QuestionItem.buildListFromNode(questionNodeRepository.findAll()));
    }

}
