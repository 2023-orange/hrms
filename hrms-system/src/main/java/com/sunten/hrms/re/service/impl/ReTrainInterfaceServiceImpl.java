package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.re.dao.ReTrainInterfaceDao;
import com.sunten.hrms.re.domain.ReTrainInterface;
import com.sunten.hrms.re.dto.ReTrainInterfaceDTO;
import com.sunten.hrms.re.dto.ReTrainInterfaceQueryCriteria;
import com.sunten.hrms.re.mapper.ReTrainInterfaceMapper;
import com.sunten.hrms.re.service.ReTrainInterfaceService;
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
 * 培训记录临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReTrainInterfaceServiceImpl extends ServiceImpl<ReTrainInterfaceDao, ReTrainInterface> implements ReTrainInterfaceService {
    private final ReTrainInterfaceDao reTrainInterfaceDao;
    private final ReTrainInterfaceMapper reTrainInterfaceMapper;

    public ReTrainInterfaceServiceImpl(ReTrainInterfaceDao reTrainInterfaceDao, ReTrainInterfaceMapper reTrainInterfaceMapper) {
        this.reTrainInterfaceDao = reTrainInterfaceDao;
        this.reTrainInterfaceMapper = reTrainInterfaceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReTrainInterfaceDTO insert(ReTrainInterface trainInterfaceNew) {
        reTrainInterfaceDao.insertAllColumn(trainInterfaceNew);
        return reTrainInterfaceMapper.toDto(trainInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReTrainInterface trainInterface = new ReTrainInterface();
        trainInterface.setId(id);
        this.delete(trainInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReTrainInterface trainInterface) {
        // TODO    确认删除前是否需要做检查
        reTrainInterfaceDao.deleteByEntityKey(trainInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReTrainInterface trainInterfaceNew) {
        ReTrainInterface trainInterfaceInDb = Optional.ofNullable(reTrainInterfaceDao.getByKey(trainInterfaceNew.getId())).orElseGet(ReTrainInterface::new);
        ValidationUtil.isNull(trainInterfaceInDb.getId(), "TrainInterface", "id", trainInterfaceNew.getId());
        trainInterfaceNew.setId(trainInterfaceInDb.getId());
        reTrainInterfaceDao.updateAllColumnByKey(trainInterfaceNew);
    }

    @Override
    public ReTrainInterfaceDTO getByKey(Long id) {
        ReTrainInterface trainInterface = Optional.ofNullable(reTrainInterfaceDao.getByKey(id)).orElseGet(ReTrainInterface::new);
        ValidationUtil.isNull(trainInterface.getId(), "TrainInterface", "id", id);
        return reTrainInterfaceMapper.toDto(trainInterface);
    }

    @Override
    public List<ReTrainInterfaceDTO> listAll(ReTrainInterfaceQueryCriteria criteria) {
        return reTrainInterfaceMapper.toDto(reTrainInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReTrainInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<ReTrainInterface> page = PageUtil.startPage(pageable);
        List<ReTrainInterface> trainInterfaces = reTrainInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reTrainInterfaceMapper.toDto(trainInterfaces), page.getTotal());
    }

    @Override
    public void download(List<ReTrainInterfaceDTO> trainInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReTrainInterfaceDTO trainInterfaceDTO : trainInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋员工ID", trainInterfaceDTO.getRecruitmentInterface().getId());
            map.put("培训名称", trainInterfaceDTO.getTrainName());
            map.put("开始时间", trainInterfaceDTO.getStartTime());
            map.put("结束时间", trainInterfaceDTO.getEndTime());
            map.put("培训内容", trainInterfaceDTO.getTrainContent());
            map.put("培训类型", trainInterfaceDTO.getTrainType());
            map.put("课时", trainInterfaceDTO.getTrainTime());
            map.put("培训地点", trainInterfaceDTO.getTrainAddress());
            map.put("培训单位", trainInterfaceDTO.getTrainCompany());
            map.put("所获证书", trainInterfaceDTO.getCertificate());
            map.put("讲师", trainInterfaceDTO.getLecturer());
            map.put("讲师信息", trainInterfaceDTO.getLecturerInformation());
            map.put("备注", trainInterfaceDTO.getRemarks());
            map.put("弹性域1", trainInterfaceDTO.getAttribute1());
            map.put("弹性域2", trainInterfaceDTO.getAttribute2());
            map.put("弹性域3", trainInterfaceDTO.getAttribute3());
            map.put("有效标记默认值", trainInterfaceDTO.getEnabledFlag());
            map.put("id", trainInterfaceDTO.getId());
            map.put("updateTime", trainInterfaceDTO.getUpdateTime());
            map.put("createTime", trainInterfaceDTO.getCreateTime());
            map.put("updateBy", trainInterfaceDTO.getUpdateBy());
            map.put("createBy", trainInterfaceDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
