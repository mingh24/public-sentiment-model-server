package com.yi.psms.service;

import com.yi.psms.dao.StatementNodeRepository;
import com.yi.psms.model.vo.ResponseVO;
import com.yi.psms.model.vo.statement.StatementItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatementService extends BaseService {

    private final StatementNodeRepository statementNodeRepository;

    public StatementService(StatementNodeRepository statementNodeRepository) {
        this.statementNodeRepository = statementNodeRepository;
    }

    public ResponseVO getStatementByStatementId(Integer statementId) {
        return response(StatementItemVO.buildFromNode(statementNodeRepository.findByStatementId(statementId)));
    }

}
