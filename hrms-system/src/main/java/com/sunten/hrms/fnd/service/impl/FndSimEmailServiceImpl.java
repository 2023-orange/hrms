package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.dao.FndSimEmailDao;
import com.sunten.hrms.fnd.service.FndSimEmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndSimEmailServiceImpl implements FndSimEmailService {
    private final FndSimEmailDao fndSimEmailDao;

    public FndSimEmailServiceImpl(FndSimEmailDao fndSimEmailDao) {
        this.fndSimEmailDao = fndSimEmailDao;
    }

    @Override
    public void simEmailSend() {
        fndSimEmailDao.simEmailSend();
    }
}
