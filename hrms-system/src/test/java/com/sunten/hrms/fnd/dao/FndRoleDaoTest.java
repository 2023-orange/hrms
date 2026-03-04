package com.sunten.hrms.fnd.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.utils.PageUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FndRoleDaoTest {
    @Autowired
    FndRoleDao fndRoleDao;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void selectAll() {
        //List<Sort.Order> sortList = new ArrayList<>();
        List<String> so = new ArrayList<>();
        so.add("id");
        Sort sort = Sort.by(Sort.Direction.DESC, "name").and(Sort.by("id"));//.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(1, 1, sort);
        Page page = PageUtil.startPage(pageable);
        List<FndRole> roles = fndRoleDao.listAllByPage(page);
        System.out.println(page.getCurrent());
        //page.setRecords(new ArrayList<>(fndRoleDao.selectAllPage(page)));
        //List<FndRole> records = new ArrayList<>(fndRoleDao.selectAllPage(page));
        System.out.println(page.getTotal());
        for (FndRole role : roles
        ) {
            System.out.println(role);
        }
        
        System.out.println(fndRoleDao.selectById(1) );

    }
}