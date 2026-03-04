package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.re.dao.ReWorkhistoryInterfaceDao;
import com.sunten.hrms.re.domain.ReWorkhistoryInterface;
import com.sunten.hrms.re.dto.ReWorkhistoryInterfaceDTO;
import com.sunten.hrms.re.dto.ReWorkhistoryInterfaceQueryCriteria;
import com.sunten.hrms.re.mapper.ReWorkhistoryInterfaceMapper;
import com.sunten.hrms.re.service.ReWorkhistoryInterfaceService;
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
 * 工作经历临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReWorkhistoryInterfaceServiceImpl extends ServiceImpl<ReWorkhistoryInterfaceDao, ReWorkhistoryInterface> implements ReWorkhistoryInterfaceService {
    private final ReWorkhistoryInterfaceDao reWorkhistoryInterfaceDao;
    private final ReWorkhistoryInterfaceMapper reWorkhistoryInterfaceMapper;

    public ReWorkhistoryInterfaceServiceImpl(ReWorkhistoryInterfaceDao reWorkhistoryInterfaceDao, ReWorkhistoryInterfaceMapper reWorkhistoryInterfaceMapper) {
        this.reWorkhistoryInterfaceDao = reWorkhistoryInterfaceDao;
        this.reWorkhistoryInterfaceMapper = reWorkhistoryInterfaceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReWorkhistoryInterfaceDTO insert(ReWorkhistoryInterface workhistoryInterfaceNew) {
        reWorkhistoryInterfaceDao.insertAllColumn(workhistoryInterfaceNew);
        return reWorkhistoryInterfaceMapper.toDto(workhistoryInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReWorkhistoryInterface workhistoryInterface = new ReWorkhistoryInterface();
        workhistoryInterface.setId(id);
        this.delete(workhistoryInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReWorkhistoryInterface workhistoryInterface) {
        // TODO    确认删除前是否需要做检查
        reWorkhistoryInterfaceDao.deleteByEntityKey(workhistoryInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReWorkhistoryInterface workhistoryInterfaceNew) {
        ReWorkhistoryInterface workhistoryInterfaceInDb = Optional.ofNullable(reWorkhistoryInterfaceDao.getByKey(workhistoryInterfaceNew.getId())).orElseGet(ReWorkhistoryInterface::new);
        ValidationUtil.isNull(workhistoryInterfaceInDb.getId(), "WorkhistoryInterface", "id", workhistoryInterfaceNew.getId());
        workhistoryInterfaceNew.setId(workhistoryInterfaceInDb.getId());
        reWorkhistoryInterfaceDao.updateAllColumnByKey(workhistoryInterfaceNew);
    }

    @Override
    public ReWorkhistoryInterfaceDTO getByKey(Long id) {
        ReWorkhistoryInterface workhistoryInterface = Optional.ofNullable(reWorkhistoryInterfaceDao.getByKey(id)).orElseGet(ReWorkhistoryInterface::new);
        ValidationUtil.isNull(workhistoryInterface.getId(), "WorkhistoryInterface", "id", id);
        return reWorkhistoryInterfaceMapper.toDto(workhistoryInterface);
    }

    @Override
    public List<ReWorkhistoryInterfaceDTO> listAll(ReWorkhistoryInterfaceQueryCriteria criteria) {
        return reWorkhistoryInterfaceMapper.toDto(reWorkhistoryInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReWorkhistoryInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<ReWorkhistoryInterface> page = PageUtil.startPage(pageable);
        List<ReWorkhistoryInterface> workhistoryInterfaces = reWorkhistoryInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reWorkhistoryInterfaceMapper.toDto(workhistoryInterfaces), page.getTotal());
    }

    @Override
    public void download(List<ReWorkhistoryInterfaceDTO> workhistoryInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReWorkhistoryInterfaceDTO workhistoryInterfaceDTO : workhistoryInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", workhistoryInterfaceDTO.getRecruitmentInterface().getId());
            map.put("单位", workhistoryInterfaceDTO.getCompany());
            map.put("职务", workhistoryInterfaceDTO.getPost());
            map.put("开始时间", workhistoryInterfaceDTO.getStartTime());
            map.put("结束时间", workhistoryInterfaceDTO.getEndTime());
            map.put("月薪", workhistoryInterfaceDTO.getSalaryOld());
            map.put("离职原因", workhistoryInterfaceDTO.getReasonsLeaving());
            map.put("证明人", workhistoryInterfaceDTO.getWitness());
            map.put("联系电话", workhistoryInterfaceDTO.getTele());
            map.put("备注", workhistoryInterfaceDTO.getRemarks());
            map.put("弹性域1", workhistoryInterfaceDTO.getAttribute1());
            map.put("弹性域2", workhistoryInterfaceDTO.getAttribute2());
            map.put("弹性域3", workhistoryInterfaceDTO.getAttribute3());
            map.put("有效标记默认值", workhistoryInterfaceDTO.getEnabledFlag());
            map.put("id", workhistoryInterfaceDTO.getId());
            map.put("createBy", workhistoryInterfaceDTO.getCreateBy());
            map.put("updateTime", workhistoryInterfaceDTO.getUpdateTime());
            map.put("createTime", workhistoryInterfaceDTO.getCreateTime());
            map.put("updateBy", workhistoryInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
