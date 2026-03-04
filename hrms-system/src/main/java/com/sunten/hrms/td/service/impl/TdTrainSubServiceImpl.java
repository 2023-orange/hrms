package com.sunten.hrms.td.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.td.dao.TdTrainSubDao;
import com.sunten.hrms.td.domain.TdTrainSub;
import com.sunten.hrms.td.dto.TdTrainSubDTO;
import com.sunten.hrms.td.dto.TdTrainSubQueryCriteria;
import com.sunten.hrms.td.mapper.TdTrainSubMapper;
import com.sunten.hrms.td.service.TdTrainSubService;
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
 * 参加培训人员情况 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdTrainSubServiceImpl extends ServiceImpl<TdTrainSubDao, TdTrainSub> implements TdTrainSubService {
    private final TdTrainSubDao tdTrainSubDao;
    private final TdTrainSubMapper tdTrainSubMapper;
    private final PmEmployeeService pmEmployeeService;

    public TdTrainSubServiceImpl(TdTrainSubDao tdTrainSubDao, TdTrainSubMapper tdTrainSubMapper, PmEmployeeService pmEmployeeService) {
        this.tdTrainSubDao = tdTrainSubDao;
        this.tdTrainSubMapper = tdTrainSubMapper;
        this.pmEmployeeService = pmEmployeeService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdTrainSubDTO insert(TdTrainSub trainSubNew) {
        tdTrainSubDao.insertAllColumn(trainSubNew);
        return tdTrainSubMapper.toDto(trainSubNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdTrainSub trainSub = new TdTrainSub();
        trainSub.setId(id);
        trainSub.setEnabledFlag(false);
        this.delete(trainSub);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdTrainSub trainSub) {
        // 确认删除前是否需要做检查,只失效，不删除
        tdTrainSubDao.updateEnableFlag(trainSub);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdTrainSub trainSubNew) {
        TdTrainSub trainSubInDb = Optional.ofNullable(tdTrainSubDao.getByKey(trainSubNew.getId())).orElseGet(TdTrainSub::new);
        ValidationUtil.isNull(trainSubInDb.getId(), "TrainSub", "id", trainSubNew.getId());
        trainSubNew.setId(trainSubInDb.getId());
        tdTrainSubDao.updateAllColumnByKey(trainSubNew);
    }

    @Override
    public TdTrainSubDTO getByKey(Long id) {
        TdTrainSub trainSub = Optional.ofNullable(tdTrainSubDao.getByKey(id)).orElseGet(TdTrainSub::new);
        ValidationUtil.isNull(trainSub.getId(), "TrainSub", "id", id);
        return tdTrainSubMapper.toDto(trainSub);
    }

    @Override
    public List<TdTrainSubDTO> listAll(TdTrainSubQueryCriteria criteria) {
        List<TdTrainSubDTO> trainSubDTOS = tdTrainSubMapper.toDto(tdTrainSubDao.listAllByCriteria(criteria));
//        for (TdTrainSubDTO tts : trainSubDTOS) {
//            setTrainSubEmployeeInfo(tts);
//        }
        return trainSubDTOS;
    }

    @Override
    public Map<String, Object> listAll(TdTrainSubQueryCriteria criteria, Pageable pageable) {
        Page<TdTrainSub> page = PageUtil.startPage(pageable);
        List<TdTrainSub> trainSubs = tdTrainSubDao.listAllByCriteriaPage(page, criteria);
        List<TdTrainSubDTO> trainSubDTOS = tdTrainSubMapper.toDto(trainSubs);
//        for (TdTrainSubDTO tts : trainSubDTOS) {
//            setTrainSubEmployeeInfo(tts);
//        }
        return PageUtil.toPage(trainSubDTOS, page.getTotal());
    }

    @Override
    public void download(List<TdTrainSubDTO> trainSubDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdTrainSubDTO trainSubDTO : trainSubDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("培训id", trainSubDTO.getTrain().getId());
            map.put("员工id", trainSubDTO.getEmployee().getId());
            map.put("签到时间", trainSubDTO.getSignTime());
            map.put("离开时间", trainSubDTO.getLeaveTime());
            map.put("备注", trainSubDTO.getRemarks());
            map.put("弹性域1", trainSubDTO.getAttribute1());
            map.put("弹性域2", trainSubDTO.getAttribute2());
            map.put("弹性域3", trainSubDTO.getAttribute3());
            map.put("有效标记默认值", trainSubDTO.getEnabledFlag());
            map.put("id", trainSubDTO.getId());
            map.put("创建时间", trainSubDTO.getCreateTime());
            map.put("创建人ID", trainSubDTO.getCreateBy());
            map.put("修改时间", trainSubDTO.getUpdateTime());
            map.put("修改人ID", trainSubDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

//    /**
//     * 获取员工的基本属性
//     */
//    public void setTrainSubEmployeeInfo(TdTrainSubDTO ttsDTO) {
//        Long employeeId = ttsDTO.getEmployee().getId();
//        PmEmployeeDTO pm = pmEmployeeService.getByKey(employeeId);
//        ttsDTO.setEmployee(pm);
//    }

    @Override
    public List<TdTrainSubDTO> batchInsert(List<TdTrainSub> tdTrainSubs) {
        if (tdTrainSubs != null && tdTrainSubs.size() > 0) {
            for (TdTrainSub tts : tdTrainSubs) {
                if (tts.getId() != null) {
                    if (tts.getId().equals(-1L)) {
                        this.insert(tts);
                    } else {
                        this.update(tts);
                    }
                } else {
                    throw new InfoCheckWarningMessException("培训人员子集ID不应为空");
                }
            }
        }

        return tdTrainSubMapper.toDto(tdTrainSubs);
    }
}
