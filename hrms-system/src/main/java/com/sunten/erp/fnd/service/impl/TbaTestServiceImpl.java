package com.sunten.erp.fnd.service.impl;


//import com.baomidou.dynamic.datasource.annotation.DS;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.erp.fnd.dao.TbaTestDao;
import com.sunten.erp.fnd.domain.TbaTest;
import com.sunten.erp.fnd.service.TbaTestService;
import com.sunten.hrms.ac.dto.AcOvertimeQueryCriteria;
import com.sunten.hrms.ac.service.AcOvertimeService;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TbaTestServiceImpl extends ServiceImpl<TbaTestDao, TbaTest> implements TbaTestService {
    private final FndDeptService fndDeptService;
    private final FndDeptDao fndDeptDao;
    private final TbaTestDao tbaTestDao;
    private final AcOvertimeService acOvertimeService;
    @Autowired
    private TbaTestServiceImpl instance;

    public TbaTestServiceImpl(FndDeptService fndDeptService, FndDeptDao fndDeptDao, TbaTestDao tbaTestDao, AcOvertimeService acOvertimeService) {
        this.fndDeptService = fndDeptService;
        this.fndDeptDao = fndDeptDao;
        this.tbaTestDao = tbaTestDao;
        this.acOvertimeService = acOvertimeService;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public TbaTest insert(TbaTest tbaTest) {
        tbaTestDao.insertAllColumn(tbaTest);
        return tbaTest;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public List<TbaTest> insert(List<TbaTest> tbaTestList) {
        for (TbaTest test : tbaTestList) {
            this.insert(test);
        }
        return tbaTestList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transaction(List<TbaTest> tbaTestList, List<FndDept> depts) {
        for (FndDept dept : depts) {
            fndDeptDao.insertAllColumn(dept);
        }
        try{
            instance.insert(tbaTestList);
        } catch(Exception ex){
            ex.printStackTrace();
        }

//        for (int i = 0; i < 2; i++) {
//            fndDeptDao.insertAllColumn(depts.get(i));
//            instance.insert(tbaTestList.get(i));
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TbaTest> listAll(Pageable pageable) {
        Page<TbaTest> page = PageUtil.startPage(pageable);
        tbaTestDao.listAllPage(page);

        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        AcOvertimeQueryCriteria scr = new AcOvertimeQueryCriteria();
        scr.setStartDate(LocalDate.now());
        scr.setEndDate(LocalDate.now());
        Pageable pageable1 = PageRequest.of(0, 10, sort);
        acOvertimeService.listAll(scr, pageable1);

        FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
        fndDeptQueryCriteria.setEndTime(LocalDateTime.now());
        Map<String, Object> aa = fndDeptService.listAll(fndDeptQueryCriteria, pageable1);
        return tbaTestDao.listAllPage(page);
    }
}
