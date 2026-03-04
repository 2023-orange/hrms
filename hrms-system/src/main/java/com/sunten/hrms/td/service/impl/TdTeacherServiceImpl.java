package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.td.domain.TdTeacher;
import com.sunten.hrms.td.dao.TdTeacherDao;
import com.sunten.hrms.td.dto.TeachingReportQueryCriteria;
import com.sunten.hrms.td.service.TdTeacherService;
import com.sunten.hrms.td.dto.TdTeacherDTO;
import com.sunten.hrms.td.dto.TdTeacherQueryCriteria;
import com.sunten.hrms.td.mapper.TdTeacherMapper;
import com.sunten.hrms.td.vo.TeacherVo;
import com.sunten.hrms.td.vo.TeachingReportVo;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.format.DateTimeFormatter;
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
 * 培训讲师列表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-06-16
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdTeacherServiceImpl extends ServiceImpl<TdTeacherDao, TdTeacher> implements TdTeacherService {
    private final TdTeacherDao tdTeacherDao;
    private final TdTeacherMapper tdTeacherMapper;
    private final FndDeptService fndDeptService;

    public TdTeacherServiceImpl(TdTeacherDao tdTeacherDao, TdTeacherMapper tdTeacherMapper, FndDeptService fndDeptService) {
        this.tdTeacherDao = tdTeacherDao;
        this.tdTeacherMapper = tdTeacherMapper;
        this.fndDeptService = fndDeptService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdTeacherDTO insert(TdTeacher teacherNew) {
        TdTeacher tdTeacher = tdTeacherDao.getByEmployeeId(teacherNew.getPmEmployee().getId());
        if (null != tdTeacher) throw new InfoCheckWarningMessException("该讲师已存在，请勿重复新增");
        tdTeacherDao.insertAllColumn(teacherNew);
        return tdTeacherMapper.toDto(teacherNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdTeacher teacher = new TdTeacher();
        teacher.setId(id);
        this.delete(teacher);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdTeacher teacher) {
        // TODO    确认删除前是否需要做检查
        tdTeacherDao.deleteByEntityKey(teacher);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdTeacher teacherNew) {
        TdTeacher teacherInDb = Optional.ofNullable(tdTeacherDao.getByKey(teacherNew.getId())).orElseGet(TdTeacher::new);
        ValidationUtil.isNull(teacherInDb.getId() ,"Teacher", "id", teacherNew.getId());
        if (!teacherNew.getPmEmployee().getId().equals(teacherInDb.getPmEmployee().getId())) {
            TdTeacher tdTeacher = tdTeacherDao.getByEmployeeId(teacherNew.getPmEmployee().getId());
            if (null != tdTeacher) throw new InfoCheckWarningMessException("该讲师已存在，不能修改成此人");
        }
        teacherNew.setId(teacherInDb.getId());
        tdTeacherDao.updateAllColumnByKey(teacherNew);
    }

    @Override
    public TdTeacherDTO getByKey(Long id) {
        TdTeacher teacher = Optional.ofNullable(tdTeacherDao.getByKey(id)).orElseGet(TdTeacher::new);
        ValidationUtil.isNull(teacher.getId() ,"Teacher", "id", id);
        return tdTeacherMapper.toDto(teacher);
    }

    @Override
    public List<TdTeacherDTO> listAll(TdTeacherQueryCriteria criteria) {
        List<Long> depts = fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptAllId());
        criteria.setDeptIds(new HashSet<>(depts));
        return tdTeacherMapper.toDto(tdTeacherDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdTeacherQueryCriteria criteria, Pageable pageable) {
        List<Long> depts = fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptAllId());
        criteria.setDeptIds(new HashSet<>(depts));
        Page<TdTeacher> page = PageUtil.startPage(pageable);
        List<TdTeacher> teachers = tdTeacherDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdTeacherMapper.toDto(teachers), page.getTotal());
    }

    @Override
    public void download(List<TdTeacherDTO> teacherDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdTeacherDTO teacherDTO : teacherDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("讲师", teacherDTO.getPmEmployee().getName());
            map.put("课程主题", teacherDTO.getTitle());
            map.put("课程内容", teacherDTO.getContent());
            map.put("授课经验（年）: 他司+我司", teacherDTO.getTeachExperience());
            map.put("他司经验年限", teacherDTO.getOtherExperienceCompose());
            map.put("我司经验年限", teacherDTO.getMyExperienceCompose());
            map.put("曾授课课题", teacherDTO.getEverExperience());
            map.put("擅长领域", teacherDTO.getBeGoodAt());
            map.put("讲师等级", teacherDTO.getLevel());
            map.put("讲师分数", teacherDTO.getScore());
            map.put("认证时间", teacherDTO.getPassDate());
            map.put("来源：线上申请、线下录入", teacherDTO.getAttribute1());
            map.put("记录OA单号，如果是线上申请产生的", teacherDTO.getAttribute2());
            map.put("attribute5", teacherDTO.getAttribute5());
            map.put("attribute4", teacherDTO.getAttribute4());
            map.put("id", teacherDTO.getId());
            map.put("attribute3", teacherDTO.getAttribute3());
            map.put("创建时间", teacherDTO.getCreateTime());
            map.put("创建人", teacherDTO.getCreateBy());
            map.put("最后修改时间", teacherDTO.getUpdateTime());
            map.put("最后修改人", teacherDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<TeacherVo> listTeacherVo(TdTeacherQueryCriteria tdTeacherQueryCriteria) {
        return tdTeacherDao.listTeacherNoAuth(tdTeacherQueryCriteria);
    }

    @Override
    public void downloadReport(List<TeachingReportVo> teachingReportVos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (TeachingReportVo reportVo : teachingReportVos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("讲师姓名", reportVo.getTeacher().getPmEmployee().getName());
            map.put("讲师工号", reportVo.getTeacher().getPmEmployee().getWorkCard());
            map.put("所属部门", reportVo.getTeacher().getPmEmployee().getDeptName());
            map.put("所属科室", reportVo.getTeacher().getPmEmployee().getDepartment());
            map.put("授课课题", reportVo.getTdPlan().getTrainingName());
            map.put("授课时间从", formatter.format(reportVo.getPlanImplement().getBeginDate()));
            map.put("授课时间至", formatter.format(reportVo.getPlanImplement().getEndDate()));
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<TeachingReportVo> listTeachingByCriteria(TeachingReportQueryCriteria criteria) {
        return tdTeacherDao.listTeachingByCriteria(criteria);
    }
}
