package com.sunten.hrms.wta.service.impl;

import com.sunten.hrms.wta.dao.WtaSyncChangeDao;
import com.sunten.hrms.wta.service.WtaSyncChangeService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WtaSyncChangeServiceImpl implements WtaSyncChangeService {
    private final WtaSyncChangeDao wtaSyncChangeDao;

    public WtaSyncChangeServiceImpl(WtaSyncChangeDao wtaSyncChangeDao) {
        this.wtaSyncChangeDao = wtaSyncChangeDao;
    }

    @Override
    public void autoSyncChange() {
        wtaSyncChangeDao.autoSyncChange();
    }

    @Override
    public void autoSyncFndChange() {
        wtaSyncChangeDao.autoSyncFndChange();
    }
}
