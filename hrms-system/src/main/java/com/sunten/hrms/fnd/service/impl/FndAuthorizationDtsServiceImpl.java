package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.domain.FndAuthorizationDts;
import com.sunten.hrms.fnd.dao.FndAuthorizationDtsDao;
import com.sunten.hrms.fnd.service.FndAuthorizationDtsService;
import com.sunten.hrms.fnd.dto.FndAuthorizationDtsDTO;
import com.sunten.hrms.fnd.dto.FndAuthorizationDtsQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndAuthorizationDtsMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-02-02
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndAuthorizationDtsServiceImpl extends ServiceImpl<FndAuthorizationDtsDao, FndAuthorizationDts> implements FndAuthorizationDtsService {
    private final FndAuthorizationDtsDao fndAuthorizationDtsDao;
    private final FndAuthorizationDtsMapper fndAuthorizationDtsMapper;

    public FndAuthorizationDtsServiceImpl(FndAuthorizationDtsDao fndAuthorizationDtsDao, FndAuthorizationDtsMapper fndAuthorizationDtsMapper) {
        this.fndAuthorizationDtsDao = fndAuthorizationDtsDao;
        this.fndAuthorizationDtsMapper = fndAuthorizationDtsMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FndAuthorizationDtsDTO insert(FndAuthorizationDts authorizationDtsNew) {
        fndAuthorizationDtsDao.insertAllColumn(authorizationDtsNew);
        return fndAuthorizationDtsMapper.toDto(authorizationDtsNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndAuthorizationDts authorizationDts = new FndAuthorizationDts();
        authorizationDts.setId(id);
        this.delete(authorizationDts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndAuthorizationDts authorizationDts) {
        // TODO    确认删除前是否需要做检查
        fndAuthorizationDtsDao.deleteByEntityKey(authorizationDts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FndAuthorizationDts authorizationDtsNew) {
        FndAuthorizationDts authorizationDtsInDb = Optional.ofNullable(fndAuthorizationDtsDao.getByKey(authorizationDtsNew.getId())).orElseGet(FndAuthorizationDts::new);
        ValidationUtil.isNull(authorizationDtsInDb.getId() ,"AuthorizationDts", "id", authorizationDtsNew.getId());
        authorizationDtsNew.setId(authorizationDtsInDb.getId());
        fndAuthorizationDtsDao.updateAllColumnByKey(authorizationDtsNew);
    }

    @Override
    public FndAuthorizationDtsDTO getByKey(Long id) {
        FndAuthorizationDts authorizationDts = Optional.ofNullable(fndAuthorizationDtsDao.getByKey(id)).orElseGet(FndAuthorizationDts::new);
        ValidationUtil.isNull(authorizationDts.getId() ,"AuthorizationDts", "id", id);
        return fndAuthorizationDtsMapper.toDto(authorizationDts);
    }

    @Override
    public List<FndAuthorizationDtsDTO> listAll(FndAuthorizationDtsQueryCriteria criteria) {
        return fndAuthorizationDtsMapper.toDto(fndAuthorizationDtsDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndAuthorizationDtsQueryCriteria criteria, Pageable pageable) {
        Page<FndAuthorizationDts> page = PageUtil.startPage(pageable);
        List<FndAuthorizationDts> authorizationDtss = fndAuthorizationDtsDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndAuthorizationDtsMapper.toDto(authorizationDtss), page.getTotal());
    }

    @Override
    public void download(List<FndAuthorizationDtsDTO> authorizationDtsDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndAuthorizationDtsDTO authorizationDtsDTO : authorizationDtsDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("授权表ID", authorizationDtsDTO.getAuthorizationId());
            map.put("授权部门ID", authorizationDtsDTO.getDeptId());
            map.put("生效标记", authorizationDtsDTO.getEnableFlag());
            map.put("id", authorizationDtsDTO.getId());
            map.put("创建人", authorizationDtsDTO.getCreateBy());
            map.put("创建日期", authorizationDtsDTO.getCreateTime());
            map.put("修改人", authorizationDtsDTO.getUpdateBy());
            map.put("修改日期", authorizationDtsDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public List<Long> getAuthorizationDeptsByToEmployee(Long toEmployeeId) {
        return fndAuthorizationDtsDao.getAuthorizationDeptsByToEmployee(toEmployeeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(FndAuthorizationDts authorizationDts) {
        Long userId;
        LocalDateTime nowDate = LocalDateTime.now();
        try{
            userId= SecurityUtils.getUserId();
        }catch (Exception ex){
            userId= Long.valueOf(-1);
        }
        if (authorizationDts.getAddList() != null && authorizationDts.getAddList().size() > 0) {
            List<FndAuthorizationDts> addList = new ArrayList<>();
            for(Long deptId : authorizationDts.getAddList()) {
                FndAuthorizationDts fndAuthorizationDts = new FndAuthorizationDts();
                fndAuthorizationDts.setAuthorizationId(authorizationDts.getAuthorizationId());
                fndAuthorizationDts.setDeptId(deptId);
                fndAuthorizationDts.setEnableFlag(true);
                fndAuthorizationDts.setCreateBy(userId);
                fndAuthorizationDts.setCreateTime(nowDate);
                fndAuthorizationDts.setUpdateBy(userId);
                fndAuthorizationDts.setUpdateTime(nowDate);
                addList.add(fndAuthorizationDts);
            }
            fndAuthorizationDtsDao.batchInsertAllColumn(addList);
        }
        if (authorizationDts.getDelList() != null && authorizationDts.getDelList().size() > 0) {
            authorizationDts.setUpdateTime(nowDate);
            authorizationDts.setUpdateBy(userId);
            authorizationDts.setEnableFlag(false);
            fndAuthorizationDtsDao.batchUpdateEnalbleFlag(authorizationDts);
        }
    }
}
