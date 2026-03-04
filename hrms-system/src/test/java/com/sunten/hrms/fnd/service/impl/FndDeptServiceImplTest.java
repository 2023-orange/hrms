package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.service.FndDeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FndDeptServiceImplTest {
    @Autowired
    FndDeptService fndDeptService;

    @Autowired
    FndDeptDao fndDeptDao;

    @Test
    public void insert() {
        FndDeptQueryCriteria criteria = new FndDeptQueryCriteria();
        criteria.setDeptCode("公司领导1");
        criteria.setDeleted(false);
        List<FndDept> fndDepts = fndDeptDao.listAllByCriteria(criteria);
        System.out.println(fndDepts);
        if (fndDepts != null && fndDepts.size() > 0) {
            System.out.println("返回非null");
        } else {
            System.out.println("返回null");
        }

    }

    @Test
    public void delete() {
    }

    @Test
    public void update() {
    }

    @Test
    public void getByKey() {
    }

    @Test
    public void listAll() {
    }

    @Test
    public void testListAll() {
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
//        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);
        FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
//        fndDeptQueryCriteria.setEndTime(LocalDateTime.now());
        fndDeptService.listAll(fndDeptQueryCriteria);
    }

    @Test
    public void download() {
    }

    @Test
    public void buildTree() {
    }

    @Test
    public void listByPid() {
    }

    @Test
    public void listByRoleId() {
    }

    @Test
    public void updateBatchSort() {
    }

    @Test
    public void updateDeptExtend() {
        Long id = 84L;
        List<FndDept> depts = fndDeptDao.listAllChildrenByPid(id);
        depts.forEach(dept -> System.out.println(dept));

        fndDeptDao.updateDeptExtend(id);

        depts = fndDeptDao.listAllChildrenByPid(id);
        depts.forEach(dept -> System.out.println(dept));
    }


    @Test
    public void listByJobId() {
        fndDeptService.listByJobId(1L);
    }
}
