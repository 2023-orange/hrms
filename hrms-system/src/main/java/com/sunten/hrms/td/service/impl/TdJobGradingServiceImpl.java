package com.sunten.hrms.td.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.td.dao.TdJobGradingDao;
import com.sunten.hrms.td.domain.TdJobGrading;
import com.sunten.hrms.td.dto.TdJobGradingDTO;
import com.sunten.hrms.td.dto.TdJobGradingQueryCriteria;
import com.sunten.hrms.td.mapper.TdJobGradingMapper;
import com.sunten.hrms.td.service.TdJobGradingService;
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
 * 岗位分级表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2022-03-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdJobGradingServiceImpl extends ServiceImpl<TdJobGradingDao, TdJobGrading> implements TdJobGradingService {
    private final TdJobGradingDao tdJobGradingDao;
    private final TdJobGradingMapper tdJobGradingMapper;

    public TdJobGradingServiceImpl(TdJobGradingDao tdJobGradingDao, TdJobGradingMapper tdJobGradingMapper) {
        this.tdJobGradingDao = tdJobGradingDao;
        this.tdJobGradingMapper = tdJobGradingMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdJobGradingDTO insert(TdJobGrading jobGradingNew) {
        //在2023年7月4日，人资提出“工序和认证岗位在保存时，不做必填项检查”
        //所以工序或者认证岗位不为空时，才做查重检查。
        if(!"".equals(jobGradingNew.getProcess()) || !"".equals(jobGradingNew.getCertificationJob())){
            TdJobGradingQueryCriteria criteria = new TdJobGradingQueryCriteria();
            if (jobGradingNew.getDeptId() == null) throw new InfoCheckWarningMessException("未找到部门信息");
//            if (jobGradingNew.getProcess() == null || "".equals(jobGradingNew.getProcess())) throw new InfoCheckWarningMessException("工序不能为空");
//            if (jobGradingNew.getCertificationJob() == null || "".equals(jobGradingNew.getCertificationJob())) throw new InfoCheckWarningMessException("认证岗位不能为空");
            criteria.setDeptId(jobGradingNew.getDeptId());
            criteria.setProcess(jobGradingNew.getProcess());
            criteria.setCertificationJob(jobGradingNew.getCertificationJob());
            List<TdJobGrading> exitList = tdJobGradingDao.listAllByCriteria(criteria);

            if (exitList.size() > 0)  throw new InfoCheckWarningMessException("该部门已存在相同工序的认证岗位!");
        }
        tdJobGradingDao.insertAllColumn(jobGradingNew);
        return tdJobGradingMapper.toDto(jobGradingNew);
    }
    @Override
    public TdJobGradingDTO supplementJobGrading(TdJobGrading jobGradingNew) {
        //所以工序或者认证岗位不为空时，才做查重检查。
        if(!"".equals(jobGradingNew.getProcess()) || !"".equals(jobGradingNew.getCertificationJob())){
            TdJobGradingQueryCriteria criteria = new TdJobGradingQueryCriteria();
            if (jobGradingNew.getDeptId() == null) {
                throw new InfoCheckWarningMessException("未找到部门信息");
            }
            Integer flag = tdJobGradingDao.checkInfo(jobGradingNew.getJobName(), jobGradingNew.getCertificationJob(),jobGradingNew.getDeptId());
            if (flag > 0) {
                throw new InfoCheckWarningMessException("该人事岗位下已存在改认证岗位，不能重复添加!");
            }
        }
        tdJobGradingDao.insertAllColumn(jobGradingNew);
        return tdJobGradingMapper.toDto(jobGradingNew);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdJobGrading jobGrading = new TdJobGrading();
        jobGrading.setId(id);
        this.delete(jobGrading);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdJobGrading jobGrading) {
        // TODO    确认删除前是否需要做检查
        tdJobGradingDao.deleteByEntityKey(jobGrading);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdJobGrading jobGradingNew) {
        TdJobGrading jobGradingInDb = Optional.ofNullable(tdJobGradingDao.getByKey(jobGradingNew.getId())).orElseGet(TdJobGrading::new);
        ValidationUtil.isNull(jobGradingInDb.getId() ,"JobGrading", "id", jobGradingNew.getId());
        TdJobGradingQueryCriteria criteria = new TdJobGradingQueryCriteria();

        if (jobGradingNew.getDeptId() == null) throw new InfoCheckWarningMessException("未找到部门信息");
        if (jobGradingNew.getProcess() == null || "".equals(jobGradingNew.getProcess())) throw new InfoCheckWarningMessException("工序不能为空");
        if (jobGradingNew.getCertificationJob() == null || "".equals(jobGradingNew.getCertificationJob())) throw new InfoCheckWarningMessException("认证岗位不能为空");
        criteria.setDeptId(jobGradingNew.getDeptId());
        criteria.setProcess(jobGradingNew.getProcess());
        criteria.setCertificationJob(jobGradingNew.getCertificationJob());
        List<TdJobGrading> exitList = tdJobGradingDao.listAllByCriteria(criteria);
        jobGradingNew.setId(jobGradingInDb.getId());
        //如果没有找到记录，则更新整条记录。
        if (exitList.size() == 0){
            tdJobGradingDao.updateAllColumnByKey(jobGradingNew);
        }
        tdJobGradingDao.updateAllColumnByKey(jobGradingNew);
        //如果找到一条记录，并且这条记录与当前待更新的记录是同一条记录，那么更新整条记录。
//        if (exitList.size() == 1){
//            for (TdJobGrading item :exitList) {
//                if(item.getId().equals(jobGradingNew.getId())){
//                    tdJobGradingDao.updateAllColumnByKey(jobGradingNew);
//                }else{
//                    throw new InfoCheckWarningMessException("本部门或科室，在同一个工序里，已经存在相同的认证岗位，请不要重复提交!");
//                }
//            }
//        }
        //如果找到多条记录，则抛出异常报错。
//        if (exitList.size() > 1){
//            throw new InfoCheckWarningMessException("本部门或科室，在同一个工序里存在多个认证岗位，请核查!");
//        }

    }

    @Override
    public TdJobGradingDTO getByKey(Long id) {
        TdJobGrading jobGrading = Optional.ofNullable(tdJobGradingDao.getByKey(id)).orElseGet(TdJobGrading::new);
        ValidationUtil.isNull(jobGrading.getId() ,"JobGrading", "id", id);
        return tdJobGradingMapper.toDto(jobGrading);
    }

    @Override
    public List<TdJobGradingDTO> listAll(TdJobGradingQueryCriteria criteria) {
        return tdJobGradingMapper.toDto(tdJobGradingDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdJobGradingQueryCriteria criteria, Pageable pageable) {
        Page<TdJobGrading> page = PageUtil.startPage(pageable);
        List<TdJobGrading> jobGradings = tdJobGradingDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdJobGradingMapper.toDto(jobGradings), page.getTotal());
    }

    @Override
    public void download(List<TdJobGradingDTO> jobGradingDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdJobGradingDTO jobGradingDTO : jobGradingDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
//            map.put("部门id", jobGradingDTO.getDeptId());
            map.put("车间/科室", jobGradingDTO.getDeptName());
            map.put("班组", jobGradingDTO.getTeamName());
            map.put("工序", jobGradingDTO.getProcess());
            map.put("人事岗位",jobGradingDTO.getJobName());
            map.put("认证岗位", jobGradingDTO.getJobName());
//            map.put("岗位id(暂时不与岗位挂钩)", jobGradingDTO.getJobId());
            map.put("岗位级别", jobGradingDTO.getJobLevel());
            map.put("工作内容", jobGradingDTO.getWorkContent());
//            map.put("有效标记", jobGradingDTO.getEnabledFlag());
//            map.put("id", jobGradingDTO.getId());
//            map.put("创建时间", jobGradingDTO.getCreateTime());
//            map.put("创建人", jobGradingDTO.getCreateBy());
//            map.put("最后修改时间", jobGradingDTO.getUpdateTime());
//            map.put("最后修改人", jobGradingDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public List<String> listForProcess(TdJobGradingQueryCriteria criteria) {
        return tdJobGradingDao.listForProcess(criteria);
    }

    @Override
    public List<TdJobGradingDTO> listForJob(TdJobGradingQueryCriteria criteria) {
        return tdJobGradingMapper.toDto(tdJobGradingDao.listForJob(criteria));
    }

    @Override
    public List<TdJobGradingDTO> getCertificationJobByProcess(TdJobGradingQueryCriteria criteria) {
        return tdJobGradingMapper.toDto(tdJobGradingDao.getCertificationJobByProcess(criteria));
    }


}
