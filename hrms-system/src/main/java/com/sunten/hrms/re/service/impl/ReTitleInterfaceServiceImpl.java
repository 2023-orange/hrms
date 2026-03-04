package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.re.dao.ReTitleInterfaceDao;
import com.sunten.hrms.re.domain.ReTitleInterface;
import com.sunten.hrms.re.dto.ReTitleInterfaceDTO;
import com.sunten.hrms.re.dto.ReTitleInterfaceQueryCriteria;
import com.sunten.hrms.re.mapper.ReTitleInterfaceMapper;
import com.sunten.hrms.re.service.ReTitleInterfaceService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 职称情况临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReTitleInterfaceServiceImpl extends ServiceImpl<ReTitleInterfaceDao, ReTitleInterface> implements ReTitleInterfaceService {
    private final ReTitleInterfaceDao reTitleInterfaceDao;
    private final ReTitleInterfaceMapper reTitleInterfaceMapper;

    public ReTitleInterfaceServiceImpl(ReTitleInterfaceDao reTitleInterfaceDao, ReTitleInterfaceMapper reTitleInterfaceMapper) {
        this.reTitleInterfaceDao = reTitleInterfaceDao;
        this.reTitleInterfaceMapper = reTitleInterfaceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReTitleInterfaceDTO insert(ReTitleInterface titleInterfaceNew) {
        reTitleInterfaceDao.insertAllColumn(titleInterfaceNew);
        return reTitleInterfaceMapper.toDto(titleInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReTitleInterface titleInterface = new ReTitleInterface();
        titleInterface.setId(id);
        this.delete(titleInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReTitleInterface titleInterface) {
        // TODO    确认删除前是否需要做检查
        reTitleInterfaceDao.deleteByEntityKey(titleInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReTitleInterface titleInterfaceNew) {
        ReTitleInterface titleInterfaceInDb = Optional.ofNullable(reTitleInterfaceDao.getByKey(titleInterfaceNew.getId())).orElseGet(ReTitleInterface::new);
        ValidationUtil.isNull(titleInterfaceInDb.getId(), "TitleInterface", "id", titleInterfaceNew.getId());
        titleInterfaceNew.setId(titleInterfaceInDb.getId());
        reTitleInterfaceDao.updateAllColumnByKey(titleInterfaceNew);
    }

    @Override
    public ReTitleInterfaceDTO getByKey(Long id) {
        ReTitleInterface titleInterface = Optional.ofNullable(reTitleInterfaceDao.getByKey(id)).orElseGet(ReTitleInterface::new);
        ValidationUtil.isNull(titleInterface.getId(), "TitleInterface", "id", id);
        return reTitleInterfaceMapper.toDto(titleInterface);
    }

    @Override
    public List<ReTitleInterfaceDTO> listAll(ReTitleInterfaceQueryCriteria criteria) {
        return reTitleInterfaceMapper.toDto(reTitleInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReTitleInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<ReTitleInterface> page = PageUtil.startPage(pageable);
        List<ReTitleInterface> titleInterfaces = reTitleInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reTitleInterfaceMapper.toDto(titleInterfaces), page.getTotal());
    }

    @Override
    public void download(List<ReTitleInterfaceDTO> titleInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReTitleInterfaceDTO titleInterfaceDTO : titleInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", titleInterfaceDTO.getRecruitmentInterface().getId());
            map.put("职称级别", titleInterfaceDTO.getTitleLevel());
            map.put("职称名称", titleInterfaceDTO.getTitleName());
            map.put("评定时间", titleInterfaceDTO.getEvaluationTime());
            map.put("是否最高职称", titleInterfaceDTO.getNewTitleFlag());
            map.put("弹性域1", titleInterfaceDTO.getAttribute1());
            map.put("弹性域2", titleInterfaceDTO.getAttribute2());
            map.put("弹性域3", titleInterfaceDTO.getAttribute3());
            map.put("有效标记默认值", titleInterfaceDTO.getEnabledFlag());
            map.put("id", titleInterfaceDTO.getId());
            map.put("updateTime", titleInterfaceDTO.getUpdateTime());
            map.put("createTime", titleInterfaceDTO.getCreateTime());
            map.put("updateBy", titleInterfaceDTO.getUpdateBy());
            map.put("createBy", titleInterfaceDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
