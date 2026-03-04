package com.sunten.erp.fnd.service.impl;

import com.sunten.erp.fnd.domain.TbaTest;
import com.sunten.erp.fnd.service.TbaTestService;
import com.sunten.erp.fnd.service.TransactionService;
import com.sunten.hrms.fnd.service.FndDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TransactionServiceImpl implements TransactionService {
    private final FndDeptService fndDeptService;
    private final TbaTestService tbaTestService;

    public TransactionServiceImpl(FndDeptService fndDeptService, TbaTestService tbaTestService) {
        this.fndDeptService = fndDeptService;
        this.tbaTestService = tbaTestService;
    }

    @Override
    public void transaction(List<TbaTest> tbaTestList) {
        fndDeptService.listByPid(1);
        tbaTestService.insert(tbaTestList);
    }
}
