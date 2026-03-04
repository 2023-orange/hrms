package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.re.dao.ReWorkhistoryDao;
import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.domain.ReWorkhistory;
import com.sunten.hrms.re.dto.ReWorkhistoryDTO;
import com.sunten.hrms.re.dto.ReWorkhistoryQueryCriteria;
import com.sunten.hrms.re.mapper.ReWorkhistoryMapper;
import com.sunten.hrms.re.service.ReWorkhistoryService;
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
 * 工作经历表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReWorkhistoryServiceImpl extends ServiceImpl<ReWorkhistoryDao, ReWorkhistory> implements ReWorkhistoryService {
    private final ReWorkhistoryDao reWorkhistoryDao;
    private final ReWorkhistoryMapper reWorkhistoryMapper;

    public ReWorkhistoryServiceImpl(ReWorkhistoryDao reWorkhistoryDao, ReWorkhistoryMapper reWorkhistoryMapper) {
        this.reWorkhistoryDao = reWorkhistoryDao;
        this.reWorkhistoryMapper = reWorkhistoryMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReWorkhistoryDTO insert(ReWorkhistory workhistoryNew) {
        reWorkhistoryDao.insertAllColumn(workhistoryNew);
        return reWorkhistoryMapper.toDto(workhistoryNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReWorkhistory workhistory = new ReWorkhistory();
        workhistory.setId(id);
        workhistory.setEnabledFlag(false);
        this.delete(workhistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReWorkhistory workhistory) {
        // 确认删除前是否需要做检查,只失效，不删除
        reWorkhistoryDao.updateEnableFlag(workhistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReWorkhistory workhistoryNew) {
        ReWorkhistory workhistoryInDb = Optional.ofNullable(reWorkhistoryDao.getByKey(workhistoryNew.getId())).orElseGet(ReWorkhistory::new);
        ValidationUtil.isNull(workhistoryInDb.getId(), "Workhistory", "id", workhistoryNew.getId());
        workhistoryNew.setId(workhistoryInDb.getId());
        reWorkhistoryDao.updateAllColumnByKey(workhistoryNew);
    }

    @Override
    public ReWorkhistoryDTO getByKey(Long id) {
        ReWorkhistory workhistory = Optional.ofNullable(reWorkhistoryDao.getByKey(id)).orElseGet(ReWorkhistory::new);
        ValidationUtil.isNull(workhistory.getId(), "Workhistory", "id", id);
        return reWorkhistoryMapper.toDto(workhistory);
    }

    @Override
    public List<ReWorkhistoryDTO> listAll(ReWorkhistoryQueryCriteria criteria) {
        return reWorkhistoryMapper.toDto(reWorkhistoryDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReWorkhistoryQueryCriteria criteria, Pageable pageable) {
        Page<ReWorkhistory> page = PageUtil.startPage(pageable);
        List<ReWorkhistory> workhistorys = reWorkhistoryDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reWorkhistoryMapper.toDto(workhistorys), page.getTotal());
    }

    @Override
    public void download(List<ReWorkhistoryDTO> workhistoryDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReWorkhistoryDTO workhistoryDTO : workhistoryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", workhistoryDTO.getRecruitment().getId());
            map.put("单位", workhistoryDTO.getCompany());
            map.put("职务", workhistoryDTO.getPost());
            map.put("开始时间", workhistoryDTO.getStartTime());
            map.put("结束时间", workhistoryDTO.getEndTime());
            map.put("月薪", workhistoryDTO.getSalaryOld());
            map.put("离职原因", workhistoryDTO.getReasonsLeaving());
            map.put("证明人", workhistoryDTO.getWitness());
            map.put("联系电话", workhistoryDTO.getTele());
            map.put("备注", workhistoryDTO.getRemarks());
            map.put("弹性域1", workhistoryDTO.getAttribute1());
            map.put("弹性域2", workhistoryDTO.getAttribute2());
            map.put("弹性域3", workhistoryDTO.getAttribute3());
            map.put("有效标记默认值", workhistoryDTO.getEnabledFlag());
            map.put("id", workhistoryDTO.getId());
            map.put("createBy", workhistoryDTO.getCreateBy());
            map.put("updateTime", workhistoryDTO.getUpdateTime());
            map.put("createTime", workhistoryDTO.getCreateTime());
            map.put("updateBy", workhistoryDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ReWorkhistoryDTO> batchInsert(List<ReWorkhistory> reWorkhistories, Long reId) {
        for (ReWorkhistory rw : reWorkhistories) {
            if (reId != null) {
                if (rw.getRecruitment() == null) {
                    rw.setRecruitment(new ReRecruitment());
                }
                rw.getRecruitment().setId(reId);
            }
            if (rw.getRecruitment() == null || rw.getRecruitment().getId() == null) {
                throw new InfoCheckWarningMessException("招聘id不能为空");
            }
            if (rw.getId() != null) {
                if (rw.getId().equals(-1L)) {
                    reWorkhistoryDao.insertAllColumn(rw);
                } else {
                    reWorkhistoryDao.updateAllColumnByKey(rw);
                }
            } else {
                throw new InfoCheckWarningMessException("招聘模块批量新增工作经历时id不能为空");
            }
        }
        return reWorkhistoryMapper.toDto(reWorkhistories);
    }
}
