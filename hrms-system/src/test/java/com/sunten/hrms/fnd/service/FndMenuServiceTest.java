package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndMenu;
import com.sunten.hrms.fnd.dto.FndMenuDTO;
import com.sunten.hrms.fnd.dto.FndMenuQueryCriteria;
import com.sunten.hrms.fnd.dto.FndRoleSmallDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FndMenuServiceTest {
    @Autowired
    FndMenuService fndMenuService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void queryAll() {
        FndMenuQueryCriteria cc = new FndMenuQueryCriteria();
        cc.setStartTime(LocalDateTime.now());
        List<FndMenuDTO> fndMenuDTOS = fndMenuService.listAll(cc);
        for (FndMenuDTO fndMenuDTO : fndMenuDTOS
        ) {
            System.out.println(fndMenuDTO);
        }
    }

    @Test
    public void findById() {
        System.out.println(fndMenuService.getByKey((long) 1));
    }

    @Test
    public void create() {
        FndMenu fndMenu = new FndMenu();
        fndMenu.setIFrameFlag(false);
        fndMenu.setName("测试insert01124");
        fndMenu.setPid((long) 0);
        fndMenu.setSort((long) 9999);
        fndMenu.setIcon("menu");
        fndMenu.setPath("menu---1");
        fndMenu.setCacheFlag(false);
        fndMenu.setHiddenFlag(false);
        fndMenu.setBlankFlag(false);
        //fndMenu.setCreateTime(new Timestamp(new Date().getTime()));
        fndMenu.setType(0);
        System.out.println(fndMenu);
        System.out.println(fndMenuService.insert(fndMenu));
    }

    @Test
    public void update() {
    }

    @Test
    public void getDeleteMenus() {
    }

    @Test
    public void getMenuTree() {
    }

    @Test
    public void findByPid() {
    }

    @Test
    public void buildTree() {
        List<FndRoleSmallDTO> roles = new ArrayList<FndRoleSmallDTO>();
        FndRoleSmallDTO role = new FndRoleSmallDTO();
        role.setId((long) 1);
        roles.add(role);
        List<FndMenuDTO> menus = fndMenuService.listByRoles(roles);
        Map<String, Object> stringObjectMap = fndMenuService.buildTree(menus);
        System.out.println(stringObjectMap);
    }

    @Test
    public void findByRoles() {
        List<FndRoleSmallDTO> roles = new ArrayList<FndRoleSmallDTO>();
        FndRoleSmallDTO role = new FndRoleSmallDTO();
        role.setId((long) 1);
        roles.add(role);
        List<FndMenuDTO> menus = fndMenuService.listByRoles(roles);
        for (FndMenuDTO menu : menus) {
            System.out.println(menu);
        }
    }

    @Test
    public void buildMenus() {
        List<FndRoleSmallDTO> roles = new ArrayList<FndRoleSmallDTO>();
        FndRoleSmallDTO role = new FndRoleSmallDTO();
        role.setId((long) 1);
        roles.add(role);
        List<FndMenuDTO> menus = fndMenuService.listByRoles(roles);
        Map<String, Object> stringObjectMap = fndMenuService.buildTree(menus);
        Object content = fndMenuService.buildMenus((List<FndMenuDTO>) stringObjectMap.get("content"));
        ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(content, HttpStatus.OK);
        System.out.println(objectResponseEntity);
    }

    @Test
    public void findOne() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void download() {
    }
}
