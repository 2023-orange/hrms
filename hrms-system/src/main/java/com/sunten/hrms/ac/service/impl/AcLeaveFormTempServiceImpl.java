package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcLeaveFormTempDao;
import com.sunten.hrms.ac.domain.AcLeaveFormTemp;
import com.sunten.hrms.ac.dto.AcLeaveFormTempDTO;
import com.sunten.hrms.ac.dto.AcLeaveFormTempQueryCriteria;
import com.sunten.hrms.ac.mapper.AcLeaveFormTempMapper;
import com.sunten.hrms.ac.service.AcLeaveFormTempService;
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
 * OA审批通过的请假条临时表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-20
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcLeaveFormTempServiceImpl extends ServiceImpl<AcLeaveFormTempDao, AcLeaveFormTemp> implements AcLeaveFormTempService {
    private final AcLeaveFormTempDao acLeaveFormTempDao;
    private final AcLeaveFormTempMapper acLeaveFormTempMapper;

    public AcLeaveFormTempServiceImpl(AcLeaveFormTempDao acLeaveFormTempDao, AcLeaveFormTempMapper acLeaveFormTempMapper) {
        this.acLeaveFormTempDao = acLeaveFormTempDao;
        this.acLeaveFormTempMapper = acLeaveFormTempMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcLeaveFormTempDTO insert(AcLeaveFormTemp leaveFormTempNew) {
        acLeaveFormTempDao.insertAllColumn(leaveFormTempNew);
        return acLeaveFormTempMapper.toDto(leaveFormTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcLeaveFormTemp leaveFormTemp = new AcLeaveFormTemp();
        leaveFormTemp.setId(id);
        this.delete(leaveFormTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcLeaveFormTemp leaveFormTemp) {
        // TODO    确认删除前是否需要做检查
        acLeaveFormTempDao.deleteByEntityKey(leaveFormTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcLeaveFormTemp leaveFormTempNew) {
        AcLeaveFormTemp leaveFormTempInDb = Optional.ofNullable(acLeaveFormTempDao.getByKey(leaveFormTempNew.getId())).orElseGet(AcLeaveFormTemp::new);
        ValidationUtil.isNull(leaveFormTempInDb.getId(), "LeaveFormTemp", "id", leaveFormTempNew.getId());
        leaveFormTempNew.setId(leaveFormTempInDb.getId());
        acLeaveFormTempDao.updateAllColumnByKey(leaveFormTempNew);
    }

    @Override
    public AcLeaveFormTempDTO getByKey(Long id) {
        AcLeaveFormTemp leaveFormTemp = Optional.ofNullable(acLeaveFormTempDao.getByKey(id)).orElseGet(AcLeaveFormTemp::new);
        ValidationUtil.isNull(leaveFormTemp.getId(), "LeaveFormTemp", "id", id);
        return acLeaveFormTempMapper.toDto(leaveFormTemp);
    }

    @Override
    public List<AcLeaveFormTempDTO> listAll(AcLeaveFormTempQueryCriteria criteria) {
        return acLeaveFormTempMapper.toDto(acLeaveFormTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcLeaveFormTempQueryCriteria criteria, Pageable pageable) {
        Page<AcLeaveFormTemp> page = PageUtil.startPage(pageable);
        List<AcLeaveFormTemp> leaveFormTemps = acLeaveFormTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acLeaveFormTempMapper.toDto(leaveFormTemps), page.getTotal());
    }

    @Override
    public void download(List<AcLeaveFormTempDTO> leaveFormTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcLeaveFormTempDTO leaveFormTempDTO : leaveFormTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("工牌号", leaveFormTempDTO.getWorkCard());
            map.put("开始日期时间", leaveFormTempDTO.getStartTime());
            map.put("结束日期时间", leaveFormTempDTO.getEndTime());
            map.put("id", leaveFormTempDTO.getId());
            map.put("创建时间", leaveFormTempDTO.getCreateTime());
            map.put("创建人ID", leaveFormTempDTO.getCreateBy());
            map.put("修改时间", leaveFormTempDTO.getUpdateTime());
            map.put("修改人ID", leaveFormTempDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
