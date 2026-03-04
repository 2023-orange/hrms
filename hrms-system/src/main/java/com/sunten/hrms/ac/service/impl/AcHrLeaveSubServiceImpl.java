package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.domain.AcLeaveApplicationLine;
import com.sunten.hrms.ac.dao.AcLeaveApplicationLineDao;
import com.sunten.hrms.ac.service.AcHrLeaveSubService;
import com.sunten.hrms.ac.dto.AcHrLeaveSubDTO;
import com.sunten.hrms.ac.dto.AcHrLeaveSubQueryCriteria;
import com.sunten.hrms.ac.mapper.AcHrLeaveSubMapper;
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
 * @author zouyp
 * @since 2023-05-30
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcHrLeaveSubServiceImpl extends ServiceImpl<AcLeaveApplicationLineDao, AcLeaveApplicationLine> implements AcHrLeaveSubService {
    private final AcLeaveApplicationLineDao AcLeaveApplicationLineDao;
    private final AcHrLeaveSubMapper acHrLeaveSubMapper;

    public AcHrLeaveSubServiceImpl(AcLeaveApplicationLineDao AcLeaveApplicationLineDao, AcHrLeaveSubMapper acHrLeaveSubMapper) {
        this.AcLeaveApplicationLineDao = AcLeaveApplicationLineDao;
        this.acHrLeaveSubMapper = acHrLeaveSubMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcHrLeaveSubDTO insert(AcLeaveApplicationLine hrLeaveSubNew) {
        AcLeaveApplicationLineDao.insertAllColumn(hrLeaveSubNew);
        return acHrLeaveSubMapper.toDto(hrLeaveSubNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        AcLeaveApplicationLine hrLeaveSub = new AcLeaveApplicationLine();
        hrLeaveSub.setId(id);
        this.delete(hrLeaveSub);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcLeaveApplicationLine hrLeaveSub) {
        // TODO    确认删除前是否需要做检查
        AcLeaveApplicationLineDao.deleteByEntityKey(hrLeaveSub);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcLeaveApplicationLine hrLeaveSubNew) {
        AcLeaveApplicationLine hrLeaveSubInDb = Optional.ofNullable(AcLeaveApplicationLineDao.getByKey(hrLeaveSubNew.getId())).orElseGet(AcLeaveApplicationLine::new);
        ValidationUtil.isNull(hrLeaveSubInDb.getId() ,"HrLeaveSub", "id", hrLeaveSubNew.getId());
        hrLeaveSubNew.setId(hrLeaveSubInDb.getId());
        AcLeaveApplicationLineDao.updateAllColumnByKey(hrLeaveSubNew);
    }

    @Override
    public AcHrLeaveSubDTO getByKey(Integer id) {
        AcLeaveApplicationLine hrLeaveSub = Optional.ofNullable(AcLeaveApplicationLineDao.getByKey(id)).orElseGet(AcLeaveApplicationLine::new);
        ValidationUtil.isNull(hrLeaveSub.getId() ,"HrLeaveSub", "id", id);
        return acHrLeaveSubMapper.toDto(hrLeaveSub);
    }

    @Override
    public List<AcHrLeaveSubDTO> listAll(AcHrLeaveSubQueryCriteria criteria) {
        return acHrLeaveSubMapper.toDto(AcLeaveApplicationLineDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcHrLeaveSubQueryCriteria criteria, Pageable pageable) {
        Page<AcLeaveApplicationLine> page = PageUtil.startPage(pageable);
        List<AcLeaveApplicationLine> hrLeaveSubs = AcLeaveApplicationLineDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acHrLeaveSubMapper.toDto(hrLeaveSubs), page.getTotal());
    }

    @Override
    public void download(List<AcHrLeaveSubDTO> hrLeaveSubDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcHrLeaveSubDTO hrLeaveSubDTO : hrLeaveSubDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键id", hrLeaveSubDTO.getId());
            map.put("申请单号", hrLeaveSubDTO.getRequisitionCode());
            map.put("请假类型", hrLeaveSubDTO.getLeaveType());
            map.put("开始日期时间", hrLeaveSubDTO.getStartTime());
            map.put("结束日期时间", hrLeaveSubDTO.getEndTime());
            map.put("请假天数", hrLeaveSubDTO.getNumber());
            map.put("版本号", hrLeaveSubDTO.getVersion());
            map.put("工作日天数", hrLeaveSubDTO.getWorkNumber());
            map.put("hr", hrLeaveSubDTO.getHrNickName());
            map.put("hr复核时间", hrLeaveSubDTO.getHrTime());
            map.put("attribute1", hrLeaveSubDTO.getAttribute1());
            map.put("attribute2", hrLeaveSubDTO.getAttribute2());
            map.put("attribute3", hrLeaveSubDTO.getAttribute3());
            map.put("attribute4", hrLeaveSubDTO.getAttribute4());
            map.put("创建人id", hrLeaveSubDTO.getCreateBy());
            map.put("修改时间", hrLeaveSubDTO.getUpdateTime());
            map.put("修改人id", hrLeaveSubDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
