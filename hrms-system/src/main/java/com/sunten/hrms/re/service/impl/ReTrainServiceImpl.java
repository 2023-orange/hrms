package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.re.dao.ReTrainDao;
import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.domain.ReTrain;
import com.sunten.hrms.re.dto.ReTrainDTO;
import com.sunten.hrms.re.dto.ReTrainQueryCriteria;
import com.sunten.hrms.re.mapper.ReTrainMapper;
import com.sunten.hrms.re.service.ReTrainService;
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
 * 培训记录表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReTrainServiceImpl extends ServiceImpl<ReTrainDao, ReTrain> implements ReTrainService {
    private final ReTrainDao reTrainDao;
    private final ReTrainMapper reTrainMapper;

    public ReTrainServiceImpl(ReTrainDao reTrainDao, ReTrainMapper reTrainMapper) {
        this.reTrainDao = reTrainDao;
        this.reTrainMapper = reTrainMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReTrainDTO insert(ReTrain trainNew) {
        reTrainDao.insertAllColumn(trainNew);
        return reTrainMapper.toDto(trainNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReTrain train = new ReTrain();
        train.setId(id);
        train.setEnabledFlag(false);
        this.delete(train);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReTrain train) {
        //   确认删除前是否需要做检查，只失效，不删除
        reTrainDao.updateEnableFlag(train);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReTrain trainNew) {
        ReTrain trainInDb = Optional.ofNullable(reTrainDao.getByKey(trainNew.getId())).orElseGet(ReTrain::new);
        ValidationUtil.isNull(trainInDb.getId(), "Train", "id", trainNew.getId());
        trainNew.setId(trainInDb.getId());
        reTrainDao.updateAllColumnByKey(trainNew);
    }

    @Override
    public ReTrainDTO getByKey(Long id) {
        ReTrain train = Optional.ofNullable(reTrainDao.getByKey(id)).orElseGet(ReTrain::new);
        ValidationUtil.isNull(train.getId(), "Train", "id", id);
        return reTrainMapper.toDto(train);
    }

    @Override
    public List<ReTrainDTO> listAll(ReTrainQueryCriteria criteria) {
        return reTrainMapper.toDto(reTrainDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReTrainQueryCriteria criteria, Pageable pageable) {
        Page<ReTrain> page = PageUtil.startPage(pageable);
        List<ReTrain> trains = reTrainDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reTrainMapper.toDto(trains), page.getTotal());
    }

    @Override
    public void download(List<ReTrainDTO> trainDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReTrainDTO trainDTO : trainDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋员工ID", trainDTO.getRecruitment().getId());
            map.put("培训名称", trainDTO.getTrainName());
            map.put("开始时间", trainDTO.getStartTime());
            map.put("结束时间", trainDTO.getEndTime());
            map.put("培训内容", trainDTO.getTrainContent());
            map.put("培训类型", trainDTO.getTrainType());
            map.put("课时", trainDTO.getTrainTime());
            map.put("培训地点", trainDTO.getTrainAddress());
            map.put("培训单位", trainDTO.getTrainCompany());
            map.put("所获证书", trainDTO.getCertificate());
            map.put("讲师", trainDTO.getLecturer());
            map.put("讲师信息", trainDTO.getLecturerInformation());
            map.put("备注", trainDTO.getRemarks());
            map.put("弹性域1", trainDTO.getAttribute1());
            map.put("弹性域2", trainDTO.getAttribute2());
            map.put("弹性域3", trainDTO.getAttribute3());
            map.put("有效标记默认值", trainDTO.getEnabledFlag());
            map.put("id", trainDTO.getId());
            map.put("updateTime", trainDTO.getUpdateTime());
            map.put("createTime", trainDTO.getCreateTime());
            map.put("updateBy", trainDTO.getUpdateBy());
            map.put("createBy", trainDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<ReTrainDTO> batchInsert(List<ReTrain> reTrains, Long reId) {
        for (ReTrain rt : reTrains) {
            if (reId != null) {
                if (rt.getRecruitment() == null) {
                    rt.setRecruitment(new ReRecruitment());
                }
                rt.getRecruitment().setId(reId);
            }
            if (rt.getRecruitment() == null || rt.getRecruitment().getId() == null) {
                throw new InfoCheckWarningMessException("招聘id不能为空");
            }
            if (rt.getId() != null) {
                if (rt.getId().equals(-1L)) {
                    reTrainDao.insertAllColumn(rt);
                } else {
                    reTrainDao.updateAllColumnByKey(rt);
                }
            } else {
                throw new InfoCheckWarningMessException("招聘模块批量新增培训记录时id不能为空");
            }
        }
        return reTrainMapper.toDto(reTrains);
    }
}
