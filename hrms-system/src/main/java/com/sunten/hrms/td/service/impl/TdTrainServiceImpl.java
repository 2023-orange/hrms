package com.sunten.hrms.td.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.td.dao.TdTrainDao;
import com.sunten.hrms.td.domain.TdTrain;
import com.sunten.hrms.td.dto.TdTrainDTO;
import com.sunten.hrms.td.dto.TdTrainQueryCriteria;
import com.sunten.hrms.td.mapper.TdTrainMapper;
import com.sunten.hrms.td.service.TdTrainService;
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
 * 培训情况表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdTrainServiceImpl extends ServiceImpl<TdTrainDao, TdTrain> implements TdTrainService {
    private final TdTrainDao tdTrainDao;
    private final TdTrainMapper tdTrainMapper;

    public TdTrainServiceImpl(TdTrainDao tdTrainDao, TdTrainMapper tdTrainMapper) {
        this.tdTrainDao = tdTrainDao;
        this.tdTrainMapper = tdTrainMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdTrainDTO insert(TdTrain trainNew) {
        tdTrainDao.insertAllColumn(trainNew);

        return tdTrainMapper.toDto(trainNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdTrain train = new TdTrain();
        train.setId(id);
        train.setEnabledFlag(false);
        this.delete(train);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdTrain train) {
        //   确认删除前是否需要做检查,只失效，不删除
        tdTrainDao.updateEnableFlag(train);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdTrain trainNew) {
        TdTrain trainInDb = Optional.ofNullable(tdTrainDao.getByKey(trainNew.getId())).orElseGet(TdTrain::new);
        ValidationUtil.isNull(trainInDb.getId(), "Train", "id", trainNew.getId());
        trainNew.setId(trainInDb.getId());
        tdTrainDao.updateAllColumnByKey(trainNew);
    }

    @Override
    public TdTrainDTO getByKey(Long id) {
        TdTrain train = Optional.ofNullable(tdTrainDao.getByKey(id)).orElseGet(TdTrain::new);
        ValidationUtil.isNull(train.getId(), "Train", "id", id);
        return tdTrainMapper.toDto(train);
    }

    @Override
    public List<TdTrainDTO> listAll(TdTrainQueryCriteria criteria) {
        return tdTrainMapper.toDto(tdTrainDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdTrainQueryCriteria criteria, Pageable pageable) {
        Page<TdTrain> page = PageUtil.startPage(pageable);
        List<TdTrain> trains = tdTrainDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdTrainMapper.toDto(trains), page.getTotal());
    }

    @Override
    public void download(List<TdTrainDTO> trainDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdTrainDTO trainDTO : trainDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("培训名称", trainDTO.getTrainName());
            map.put("开始时间", trainDTO.getStartTime());
            map.put("结束时间", trainDTO.getEndTime());
            map.put("培训内容", trainDTO.getTrainContent());
            map.put("培训类型", trainDTO.getTrainType());
            map.put("讲师", trainDTO.getLecturer());
            map.put("课时", trainDTO.getTrainTime());
            map.put("培训地点", trainDTO.getTrainAddress());
            map.put("培训单位", trainDTO.getTrainCompany());
            map.put("所获证书", trainDTO.getCertificate());
            map.put("讲师信息", trainDTO.getLecturerInformation());
            map.put("备注", trainDTO.getRemarks());
            map.put("弹性域1", trainDTO.getAttribute1());
            map.put("弹性域2", trainDTO.getAttribute2());
            map.put("弹性域3", trainDTO.getAttribute3());
            map.put("有效标记默认值", trainDTO.getEnabledFlag());
            map.put("id", trainDTO.getId());
            map.put("创建时间", trainDTO.getCreateTime());
            map.put("创建人ID", trainDTO.getCreateBy());
            map.put("修改时间", trainDTO.getUpdateTime());
            map.put("修改人ID", trainDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
