package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.td.domain.TdCredential;
import com.sunten.hrms.td.dao.TdCredentialDao;
import com.sunten.hrms.td.service.TdCredentialService;
import com.sunten.hrms.td.dto.TdCredentialDTO;
import com.sunten.hrms.td.dto.TdCredentialQueryCriteria;
import com.sunten.hrms.td.mapper.TdCredentialMapper;
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
 * 培训证书表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-30
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdCredentialServiceImpl extends ServiceImpl<TdCredentialDao, TdCredential> implements TdCredentialService {
    private final TdCredentialDao tdCredentialDao;
    private final TdCredentialMapper tdCredentialMapper;

    public TdCredentialServiceImpl(TdCredentialDao tdCredentialDao, TdCredentialMapper tdCredentialMapper) {
        this.tdCredentialDao = tdCredentialDao;
        this.tdCredentialMapper = tdCredentialMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdCredentialDTO insert(TdCredential credentialNew) {
        tdCredentialDao.insertAllColumn(credentialNew);
        return tdCredentialMapper.toDto(credentialNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdCredential credential = new TdCredential();
        credential.setId(id);
        this.delete(credential);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdCredential credential) {
        // TODO    确认删除前是否需要做检查
        tdCredentialDao.deleteByEntityKey(credential);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdCredential credentialNew) {
        TdCredential credentialInDb = Optional.ofNullable(tdCredentialDao.getByKey(credentialNew.getId())).orElseGet(TdCredential::new);
        ValidationUtil.isNull(credentialInDb.getId() ,"Credential", "id", credentialNew.getId());
        credentialNew.setId(credentialInDb.getId());
        tdCredentialDao.updateAllColumnByKey(credentialNew);
    }

    @Override
    public TdCredentialDTO getByKey(Long id) {
        TdCredential credential = Optional.ofNullable(tdCredentialDao.getByKey(id)).orElseGet(TdCredential::new);
        ValidationUtil.isNull(credential.getId() ,"Credential", "id", id);
        return tdCredentialMapper.toDto(credential);
    }

    @Override
    public List<TdCredentialDTO> listAll(TdCredentialQueryCriteria criteria) {
        return tdCredentialMapper.toDto(tdCredentialDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdCredentialQueryCriteria criteria, Pageable pageable) {
        Page<TdCredential> page = PageUtil.startPage(pageable);
        List<TdCredential> credentials = tdCredentialDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdCredentialMapper.toDto(credentials), page.getTotal());
    }

    @Override
    public void download(List<TdCredentialDTO> credentialDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdCredentialDTO credentialDTO : credentialDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("姓名", credentialDTO.getName());
            map.put("工号",credentialDTO.getWorkCard());
            map.put("部门",credentialDTO.getDeptName());
            map.put("科室",credentialDTO.getDepartment());
            map.put("班组",credentialDTO.getTeam());
            map.put("证书名称", credentialDTO.getCredentialName());
            map.put("证书类别", credentialDTO.getCredentialType());
            map.put("具体项目", credentialDTO.getSpecificProject());
            map.put("发证机构", credentialDTO.getGrantOrganization());
            map.put("发证日期", credentialDTO.getGrantDate());
            map.put("证书有效期", credentialDTO.getValidityDate());
            map.put("证书管理处", credentialDTO.getAdminAdress());
            map.put("证书存放处", credentialDTO.getStoreAdress());
            map.put("状态", credentialDTO.getAppraisalFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
