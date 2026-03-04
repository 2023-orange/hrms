package com.sunten.hrms.re.service.impl;

import com.sunten.hrms.re.dao.ReDemandDao;
import com.sunten.hrms.re.dao.ReDemandJobDao;
import com.sunten.hrms.re.domain.ReDemandJob;
import com.sunten.hrms.re.domain.ReDemandJobDescribes;
import com.sunten.hrms.re.dao.ReDemandJobDescribesDao;
import com.sunten.hrms.re.dto.ReDemandJobQueryCriteria;
import com.sunten.hrms.re.service.ReDemandJobDescribesService;
import com.sunten.hrms.re.dto.ReDemandJobDescribesDTO;
import com.sunten.hrms.re.dto.ReDemandJobDescribesQueryCriteria;
import com.sunten.hrms.re.mapper.ReDemandJobDescribesMapper;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 岗位说明书表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-04-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReDemandJobDescribesServiceImpl extends ServiceImpl<ReDemandJobDescribesDao, ReDemandJobDescribes> implements ReDemandJobDescribesService {
    private final ReDemandJobDescribesDao reDemandJobDescribesDao;
    private final ReDemandJobDescribesMapper reDemandJobDescribesMapper;
    private final ReDemandJobDao reDemandJobDao;
    private final ReDemandDao reDemandDao;

    public ReDemandJobDescribesServiceImpl(ReDemandJobDescribesDao reDemandJobDescribesDao, ReDemandJobDescribesMapper reDemandJobDescribesMapper,
                                           ReDemandJobDao reDemandJobDao, ReDemandDao reDemandDao) {
        this.reDemandJobDescribesDao = reDemandJobDescribesDao;
        this.reDemandJobDescribesMapper = reDemandJobDescribesMapper;
        this.reDemandJobDao = reDemandJobDao;
        this.reDemandDao = reDemandDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReDemandJobDescribesDTO insert(ReDemandJobDescribes demandJobDescribesNew) {
        reDemandJobDescribesDao.insertAllColumn(demandJobDescribesNew);
        return reDemandJobDescribesMapper.toDto(demandJobDescribesNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReDemandJobDescribes demandJobDescribes = new ReDemandJobDescribes();
        demandJobDescribes.setId(id);
        this.delete(demandJobDescribes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReDemandJobDescribes demandJobDescribes) {
        // TODO    确认删除前是否需要做检查
        reDemandJobDescribesDao.deleteByEntityKey(demandJobDescribes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReDemandJobDescribes demandJobDescribesNew) {
        ReDemandJobDescribes demandJobDescribesInDb = Optional.ofNullable(reDemandJobDescribesDao.getByKey(demandJobDescribesNew.getId())).orElseGet(ReDemandJobDescribes::new);
        ValidationUtil.isNull(demandJobDescribesInDb.getId() ,"DemandJobDescribes", "id", demandJobDescribesNew.getId());
        demandJobDescribesNew.setId(demandJobDescribesInDb.getId());
        reDemandJobDescribesDao.updateAllColumnByKey(demandJobDescribesNew);
    }

    @Override
    public ReDemandJobDescribesDTO getByKey(Long id) {
        ReDemandJobDescribes demandJobDescribes = Optional.ofNullable(reDemandJobDescribesDao.getByKey(id)).orElseGet(ReDemandJobDescribes::new);
        ValidationUtil.isNull(demandJobDescribes.getId() ,"DemandJobDescribes", "id", id);
        return reDemandJobDescribesMapper.toDto(demandJobDescribes);
    }

    @Override
    public List<ReDemandJobDescribesDTO> listAll(ReDemandJobDescribesQueryCriteria criteria) {
        return reDemandJobDescribesMapper.toDto(reDemandJobDescribesDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReDemandJobDescribesQueryCriteria criteria, Pageable pageable) {
        Page<ReDemandJobDescribes> page = PageUtil.startPage(pageable);
        List<ReDemandJobDescribes> demandJobDescribess = reDemandJobDescribesDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reDemandJobDescribesMapper.toDto(demandJobDescribess), page.getTotal());
    }

    @Override
    public void download(List<ReDemandJobDescribesDTO> demandJobDescribesDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReDemandJobDescribesDTO demandJobDescribesDTO : demandJobDescribesDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("用人需求岗位子表id", demandJobDescribesDTO.getDemandJobId());
            map.put("职位名称", demandJobDescribesDTO.getJobName());
            map.put("所属部门", demandJobDescribesDTO.getDeptName());
            map.put("直接上级", demandJobDescribesDTO.getDirectlyUnder());
            map.put("职位概要", demandJobDescribesDTO.getJobDescribes());
            map.put("工作内容", demandJobDescribesDTO.getJobContent());
            map.put("任职资格", demandJobDescribesDTO.getWorkQualification());
            map.put("确认标记（true，信息不能修改）", demandJobDescribesDTO.getCheckFlag());
            map.put("生效标记", demandJobDescribesDTO.getEnabledFlag());
            map.put("id", demandJobDescribesDTO.getId());
            map.put("创建时间", demandJobDescribesDTO.getCreateTime());
            map.put("创建人ID", demandJobDescribesDTO.getCreateBy());
            map.put("修改时间", demandJobDescribesDTO.getUpdateTime());
            map.put("修改人ID", demandJobDescribesDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateColumnByValue(ReDemandJobDescribes demandJobDescribes) {
        ReDemandJobDescribes reDemandJobDescribesOld = reDemandJobDescribesDao.getByKey(demandJobDescribes.getId());
        reDemandJobDescribesDao.updateColumnByValue(demandJobDescribes);
        if (null != demandJobDescribes.getCheckFlag() && !demandJobDescribes.getCheckFlag().equals(reDemandJobDescribesOld.getCheckFlag())) {
            ReDemandJob reDemandJob = reDemandJobDao.getByKey(reDemandJobDescribesOld.getDemandJobId());
            Integer needCheckCount = reDemandJobDao.checkNeedCountAfterUpdate(reDemandJob.getDemandId());
            ReDemandJobQueryCriteria reDemandJobQueryCriteria = new ReDemandJobQueryCriteria();
            reDemandJobQueryCriteria.setDemandId(reDemandJob.getDemandId());
            reDemandJobQueryCriteria.setEnabledFlag(true);
            List<Long> jobIds = reDemandJobDao.listAllByCriteria(reDemandJobQueryCriteria).stream().map(ReDemandJob::getId).collect(Collectors.toList());
            Integer checkCount = reDemandJobDescribesDao.checkCheckFlagAfterUpdate(jobIds);
            if (checkCount.equals(needCheckCount)) {
                // 更新
                reDemandDao.updateAfterCompleteEditFlag(false, reDemandJob.getDemandId());
            } else {
                reDemandDao.updateAfterCompleteEditFlag(true, reDemandJob.getDemandId());
            }
        }
    }
}
