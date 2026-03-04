package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.domain.FndServerInformation;
import com.sunten.hrms.fnd.dao.FndServerInformationDao;
import com.sunten.hrms.fnd.service.FndServerInformationService;
import com.sunten.hrms.fnd.dto.FndServerInformationDTO;
import com.sunten.hrms.fnd.dto.FndServerInformationQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndServerInformationMapper;
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
 * 服务器信息表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2024-06-06
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndServerInformationServiceImpl extends ServiceImpl<FndServerInformationDao, FndServerInformation> implements FndServerInformationService {
    private final FndServerInformationDao fndServerInformationDao;
    private final FndServerInformationMapper fndServerInformationMapper;

    public FndServerInformationServiceImpl(FndServerInformationDao fndServerInformationDao, FndServerInformationMapper fndServerInformationMapper) {
        this.fndServerInformationDao = fndServerInformationDao;
        this.fndServerInformationMapper = fndServerInformationMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FndServerInformationDTO insert(FndServerInformation serverInformationNew) {
        fndServerInformationDao.insertAllColumn(serverInformationNew);
        return fndServerInformationMapper.toDto(serverInformationNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndServerInformation serverInformation = new FndServerInformation();
        serverInformation.setId(id);
        this.delete(serverInformation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndServerInformation serverInformation) {
        // TODO    确认删除前是否需要做检查
        fndServerInformationDao.deleteByEntityKey(serverInformation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FndServerInformation serverInformationNew) {
        FndServerInformation serverInformationInDb = Optional.ofNullable(fndServerInformationDao.getByKey(serverInformationNew.getId())).orElseGet(FndServerInformation::new);
        ValidationUtil.isNull(serverInformationInDb.getId() ,"ServerInformation", "id", serverInformationNew.getId());
        serverInformationNew.setId(serverInformationInDb.getId());
        fndServerInformationDao.updateAllColumnByKey(serverInformationNew);
    }

    @Override
    public FndServerInformationDTO getByKey(Long id) {
        FndServerInformation serverInformation = Optional.ofNullable(fndServerInformationDao.getByKey(id)).orElseGet(FndServerInformation::new);
        ValidationUtil.isNull(serverInformation.getId() ,"ServerInformation", "id", id);
        return fndServerInformationMapper.toDto(serverInformation);
    }

    @Override
    public List<FndServerInformationDTO> listAll(FndServerInformationQueryCriteria criteria) {
        return fndServerInformationMapper.toDto(fndServerInformationDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndServerInformationQueryCriteria criteria, Pageable pageable) {
        Page<FndServerInformation> page = PageUtil.startPage(pageable);
        List<FndServerInformation> serverInformations = fndServerInformationDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndServerInformationMapper.toDto(serverInformations), page.getTotal());
    }

    @Override
    public void download(List<FndServerInformationDTO> serverInformationDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndServerInformationDTO serverInformationDTO : serverInformationDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", serverInformationDTO.getId());
            map.put("服务名", serverInformationDTO.getServerName());
            map.put("描述", serverInformationDTO.getDescription());
            map.put("前端uri", serverInformationDTO.getFrontendUri());
            map.put("后端uri", serverInformationDTO.getBackendUri());
            map.put("数据库uri", serverInformationDTO.getDatabaseUri());
            map.put("有效标记默认值", serverInformationDTO.getEnabledFlag());
            map.put("备注", serverInformationDTO.getRemarks());
            map.put("弹性域1", serverInformationDTO.getAttribute1());
            map.put("弹性域2", serverInformationDTO.getAttribute2());
            map.put("弹性域3", serverInformationDTO.getAttribute3());
            map.put("弹性域4", serverInformationDTO.getAttribute4());
            map.put("弹性域5", serverInformationDTO.getAttribute5());
            map.put("创建时间", serverInformationDTO.getCreateTime());
            map.put("创建人ID", serverInformationDTO.getCreateBy());
            map.put("修改时间", serverInformationDTO.getUpdateTime());
            map.put("修改人ID", serverInformationDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
