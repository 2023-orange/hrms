package com.sunten.erp.fnd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.erp.fnd.domain.TbaTest;
import com.sunten.hrms.fnd.domain.FndDept;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TbaTestService extends IService<TbaTest> {
    TbaTest insert(TbaTest tbaTest);

    List<TbaTest> insert(List<TbaTest> tbaTestList);

    void transaction(List<TbaTest> tbaTestList, List<FndDept> depts);

    List<TbaTest> listAll(Pageable pageable);
}
