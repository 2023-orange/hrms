package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.domain.AcOvertimeApplicationLine;
import com.sunten.hrms.ac.dao.AcOvertimeApplicationLineDao;
import com.sunten.hrms.ac.service.AcOvertimeApplicationLineService;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationLineDTO;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationLineQueryCriteria;
import com.sunten.hrms.ac.mapper.AcOvertimeApplicationLineMapper;
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
 * @since 2023-10-16
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcOvertimeApplicationLineServiceImpl extends ServiceImpl<AcOvertimeApplicationLineDao, AcOvertimeApplicationLine> implements AcOvertimeApplicationLineService {
    private final AcOvertimeApplicationLineDao acOvertimeApplicationLineDao;
    private final AcOvertimeApplicationLineMapper acOvertimeApplicationLineMapper;

    public AcOvertimeApplicationLineServiceImpl(AcOvertimeApplicationLineDao acOvertimeApplicationLineDao, AcOvertimeApplicationLineMapper acOvertimeApplicationLineMapper) {
        this.acOvertimeApplicationLineDao = acOvertimeApplicationLineDao;
        this.acOvertimeApplicationLineMapper = acOvertimeApplicationLineMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcOvertimeApplicationLineDTO insert(AcOvertimeApplicationLine overtimeApplicationLineNew) {
        acOvertimeApplicationLineDao.insertAllColumn(overtimeApplicationLineNew);
        return acOvertimeApplicationLineMapper.toDto(overtimeApplicationLineNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        AcOvertimeApplicationLine overtimeApplicationLine = new AcOvertimeApplicationLine();
        overtimeApplicationLine.setId(id);
        this.delete(overtimeApplicationLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcOvertimeApplicationLine overtimeApplicationLine) {
        // TODO    确认删除前是否需要做检查
        acOvertimeApplicationLineDao.deleteByEntityKey(overtimeApplicationLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcOvertimeApplicationLine overtimeApplicationLineNew) {
        AcOvertimeApplicationLine overtimeApplicationLineInDb = Optional.ofNullable(acOvertimeApplicationLineDao.getByKey(overtimeApplicationLineNew.getId())).orElseGet(AcOvertimeApplicationLine::new);
        ValidationUtil.isNull(overtimeApplicationLineInDb.getId() ,"OvertimeApplicationLine", "id", overtimeApplicationLineNew.getId());
        overtimeApplicationLineNew.setId(overtimeApplicationLineInDb.getId());
        acOvertimeApplicationLineDao.updateAllColumnByKey(overtimeApplicationLineNew);
    }

    @Override
    public AcOvertimeApplicationLineDTO getByKey(Integer id) {
        AcOvertimeApplicationLine overtimeApplicationLine = Optional.ofNullable(acOvertimeApplicationLineDao.getByKey(id)).orElseGet(AcOvertimeApplicationLine::new);
        ValidationUtil.isNull(overtimeApplicationLine.getId() ,"OvertimeApplicationLine", "id", id);
        return acOvertimeApplicationLineMapper.toDto(overtimeApplicationLine);
    }

    @Override
    public List<AcOvertimeApplicationLineDTO> listAll(AcOvertimeApplicationLineQueryCriteria criteria) {
        return acOvertimeApplicationLineMapper.toDto(acOvertimeApplicationLineDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcOvertimeApplicationLineQueryCriteria criteria, Pageable pageable) {
        Page<AcOvertimeApplicationLine> page = PageUtil.startPage(pageable);
        List<AcOvertimeApplicationLine> overtimeApplicationLines = acOvertimeApplicationLineDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acOvertimeApplicationLineMapper.toDto(overtimeApplicationLines), page.getTotal());
    }

    @Override
    public void download(List<AcOvertimeApplicationLineDTO> overtimeApplicationLineDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcOvertimeApplicationLineDTO overtimeApplicationLineDTO : overtimeApplicationLineDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", overtimeApplicationLineDTO.getId());
            map.put("OA申请单号", overtimeApplicationLineDTO.getOaOrder());
            map.put("加班人姓名", overtimeApplicationLineDTO.getNickName());
            map.put("加班人工号", overtimeApplicationLineDTO.getUserName());
            map.put("岗位", overtimeApplicationLineDTO.getPosition());
            map.put("开始时间", overtimeApplicationLineDTO.getStartTime());
            map.put("结束时间", overtimeApplicationLineDTO.getEndTime());
            map.put("休息时间", overtimeApplicationLineDTO.getTotalRestTime());
            map.put("加班时数", overtimeApplicationLineDTO.getHours());
            map.put("当月已加班时数", overtimeApplicationLineDTO.getMonthHours());
            map.put("复核开始时间", overtimeApplicationLineDTO.getReviewStarttime());
            map.put("复核结束时间", overtimeApplicationLineDTO.getReviewEndtime());
            map.put("复核休息时间", overtimeApplicationLineDTO.getReviewTotalRestTime());
            map.put("复核加班时数", overtimeApplicationLineDTO.getReviewHours());
            map.put("复核人姓名", overtimeApplicationLineDTO.getReviewerNickName());
            map.put("复核人工号", overtimeApplicationLineDTO.getReviewerUserName());
            map.put("复核时间", overtimeApplicationLineDTO.getReviewTime());
            map.put("备注", overtimeApplicationLineDTO.getRemarks());
            map.put("有效标记", overtimeApplicationLineDTO.getEnabledFlag());
            map.put("弹性域1", overtimeApplicationLineDTO.getAttribute1());
            map.put("弹性域2", overtimeApplicationLineDTO.getAttribute2());
            map.put("弹性域3", overtimeApplicationLineDTO.getAttribute3());
            map.put("弹性域4", overtimeApplicationLineDTO.getAttribute4());
            map.put("弹性域5", overtimeApplicationLineDTO.getAttribute5());
            map.put("创建人", overtimeApplicationLineDTO.getCreateBy());
            map.put("创建时间", overtimeApplicationLineDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
