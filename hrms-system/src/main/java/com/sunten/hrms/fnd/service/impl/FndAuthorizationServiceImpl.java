package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndAuthorizationDtsDao;
import com.sunten.hrms.fnd.domain.FndAuthorization;
import com.sunten.hrms.fnd.dao.FndAuthorizationDao;
import com.sunten.hrms.fnd.domain.FndAuthorizationDts;
import com.sunten.hrms.fnd.service.FndAuthorizationService;
import com.sunten.hrms.fnd.dto.FndAuthorizationDTO;
import com.sunten.hrms.fnd.dto.FndAuthorizationQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndAuthorizationMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * @since 2021-01-29
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndAuthorizationServiceImpl extends ServiceImpl<FndAuthorizationDao, FndAuthorization> implements FndAuthorizationService {
    private final FndAuthorizationDao fndAuthorizationDao;
    private final FndAuthorizationDtsDao fndAuthorizationDtsDao;
    private final FndAuthorizationMapper fndAuthorizationMapper;

    public FndAuthorizationServiceImpl(FndAuthorizationDao fndAuthorizationDao, FndAuthorizationDtsDao fndAuthorizationDtsDao, FndAuthorizationMapper fndAuthorizationMapper) {
        this.fndAuthorizationDao = fndAuthorizationDao;
        this.fndAuthorizationDtsDao = fndAuthorizationDtsDao;
        this.fndAuthorizationMapper = fndAuthorizationMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FndAuthorizationDTO insert(FndAuthorization authorizationNew) {
        if (authorizationNew.getAuthorizationBy() == null) throw new InfoCheckWarningMessException("未找到授权人信息");
        if (authorizationNew.getAuthorizationTo() == null) throw new InfoCheckWarningMessException("未找到被授权人信息");
        if (authorizationNew.getDepts() == null || authorizationNew.getDepts().size() <= 0) throw new InfoCheckWarningMessException("找不到授权部门");
        // 查询是否有相同授权
        FndAuthorizationQueryCriteria criteria = new FndAuthorizationQueryCriteria();
        criteria.setEnableFlag(true);
        criteria.setToEmployeeId(authorizationNew.getAuthorizationTo());
        criteria.setByEmployeeId(authorizationNew.getAuthorizationBy());
        List<FndAuthorization> checkList = fndAuthorizationDao.listAllByCriteria(criteria);
        if (checkList.size() > 0) throw new InfoCheckWarningMessException("已存在的授权信息，无法重复授权");
        fndAuthorizationDao.insertAllColumn(authorizationNew);
        List<FndAuthorizationDts> fndAuthorizationDtsList = new ArrayList<>();
        authorizationNew.getDepts().forEach(deptId -> {
            FndAuthorizationDts fndAuthorizationDts = new FndAuthorizationDts();
            fndAuthorizationDts.setAuthorizationId(authorizationNew.getId());
            fndAuthorizationDts.setDeptId(deptId);
            fndAuthorizationDts.setEnableFlag(true);
            fndAuthorizationDts.setCreateBy(authorizationNew.getCreateBy());
            fndAuthorizationDts.setCreateTime(authorizationNew.getCreateTime());
            fndAuthorizationDts.setUpdateBy(authorizationNew.getCreateBy());
            fndAuthorizationDts.setUpdateTime(authorizationNew.getCreateTime());
            fndAuthorizationDtsList.add(fndAuthorizationDts);
        });
        fndAuthorizationDtsDao.batchInsertAllColumn(fndAuthorizationDtsList);

        return fndAuthorizationMapper.toDto(authorizationNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 失效该记录
        FndAuthorization authorizationInDb = new FndAuthorization();
        authorizationInDb.setId(id);
        fndAuthorizationDao.updateEnableFlagByKey(authorizationInDb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndAuthorization authorization) {
        // TODO    确认删除前是否需要做检查
        fndAuthorizationDao.deleteByEntityKey(authorization);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FndAuthorization authorizationNew) {
        FndAuthorization authorizationInDb = Optional.ofNullable(fndAuthorizationDao.getByKey(authorizationNew.getId())).orElseGet(FndAuthorization::new);
        ValidationUtil.isNull(authorizationInDb.getId() ,"Authorization", "id", authorizationNew.getId());
        authorizationNew.setId(authorizationInDb.getId());
        fndAuthorizationDao.updateAllColumnByKey(authorizationNew);
    }

    @Override
    public FndAuthorizationDTO getByKey(Long id) {
        FndAuthorization authorization = Optional.ofNullable(fndAuthorizationDao.getByKey(id)).orElseGet(FndAuthorization::new);
        ValidationUtil.isNull(authorization.getId() ,"Authorization", "id", id);
        return fndAuthorizationMapper.toDto(authorization);
    }

    @Override
    public List<FndAuthorizationDTO> listAll(FndAuthorizationQueryCriteria criteria) {
        return fndAuthorizationMapper.toDto(fndAuthorizationDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndAuthorizationQueryCriteria criteria, Pageable pageable) {
        Page<FndAuthorization> page = PageUtil.startPage(pageable);
        List<FndAuthorization> authorizations = fndAuthorizationDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndAuthorizationMapper.toDto(authorizations), page.getTotal());
    }

    @Override
    public void download(List<FndAuthorizationDTO> authorizationDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndAuthorizationDTO authorizationDTO : authorizationDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("授权人", authorizationDTO.getAuthorizationBy());
            map.put("被授权人", authorizationDTO.getAuthorizationTo());
            map.put("授权类型", authorizationDTO.getAuthorizationType());
            map.put("授权起始日期", authorizationDTO.getBeginDate());
            map.put("授权截止日期", authorizationDTO.getEndDate());
            map.put("生效标识", authorizationDTO.getEnableFlag());
            map.put("id", authorizationDTO.getId());
            map.put("创建日期", authorizationDTO.getCreateTime());
            map.put("创建人", authorizationDTO.getCreateBy());
            map.put("修改日期", authorizationDTO.getUpdateTime());
            map.put("修改人", authorizationDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<FndAuthorizationDTO> listAllByCriteriaWithChild(FndAuthorizationQueryCriteria criteria) {
        return fndAuthorizationMapper.toDto(fndAuthorizationDao.listAllByCriteriaWithChild(criteria));
    }
}
