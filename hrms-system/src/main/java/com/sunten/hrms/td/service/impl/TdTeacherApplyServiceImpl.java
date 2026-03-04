package com.sunten.hrms.td.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.td.dao.TdTeacherDao;
import com.sunten.hrms.td.domain.TdTeacher;
import com.sunten.hrms.td.domain.TdTeacherApply;
import com.sunten.hrms.td.dao.TdTeacherApplyDao;
import com.sunten.hrms.td.service.TdTeacherApplyService;
import com.sunten.hrms.td.dto.TdTeacherApplyDTO;
import com.sunten.hrms.td.dto.TdTeacherApplyQueryCriteria;
import com.sunten.hrms.td.mapper.TdTeacherApplyMapper;
import com.sunten.hrms.tool.dao.ToolLocalStorageDao;
import com.sunten.hrms.tool.domain.ToolLocalStorage;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.File;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 * 讲师身份（申请）表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-06-15
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdTeacherApplyServiceImpl extends ServiceImpl<TdTeacherApplyDao, TdTeacherApply> implements TdTeacherApplyService {
    @Value("${file.attestation}")
    private String attestation;
    @Value("${file.maxSize}")
    private long maxSize;

    private final TdTeacherApplyDao tdTeacherApplyDao;
    private final TdTeacherApplyMapper tdTeacherApplyMapper;
    private final ToolLocalStorageDao toolLocalStorageDao;
    private final TdTeacherDao tdTeacherDao;

    public TdTeacherApplyServiceImpl(TdTeacherApplyDao tdTeacherApplyDao, TdTeacherApplyMapper tdTeacherApplyMapper, ToolLocalStorageDao toolLocalStorageDao, TdTeacherDao tdTeacherDao) {
        this.tdTeacherApplyDao = tdTeacherApplyDao;
        this.tdTeacherApplyMapper = tdTeacherApplyMapper;
        this.toolLocalStorageDao = toolLocalStorageDao;
        this.tdTeacherDao = tdTeacherDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdTeacherApplyDTO insert(TdTeacherApply teacherApplyNew) {
        // TODO 插入前判断是否已经是讲师,或者是否已存在申请
        TdTeacher nowTeacher = tdTeacherDao.getByEmployeeId(teacherApplyNew.getPmEmployee().getId());
        if (nowTeacher != null) throw new InfoCheckWarningMessException("此人已经是讲师，无法再次申请");
        List<TdTeacherApply>  tdTeacherApply = tdTeacherApplyDao.listByEnable(teacherApplyNew.getPmEmployee().getId());
        if (tdTeacherApply.size() > 0) throw new InfoCheckWarningMessException("此人已存在审批中或审批通过的申请单，请勿重复申请");
        tdTeacherApplyDao.insertAllColumn(teacherApplyNew);
        return tdTeacherApplyMapper.toDto(teacherApplyNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdTeacherApply teacherApply = new TdTeacherApply();
        teacherApply.setId(id);
        this.delete(teacherApply);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdTeacherApply teacherApply) {
        // TODO    确认删除前是否需要做检查
        tdTeacherApplyDao.deleteByEntityKey(teacherApply);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdTeacherApply teacherApplyNew) {
        TdTeacherApply teacherApplyInDb = Optional.ofNullable(tdTeacherApplyDao.getByKey(teacherApplyNew.getId())).orElseGet(TdTeacherApply::new);
        ValidationUtil.isNull(teacherApplyInDb.getId() ,"TeacherApply", "id", teacherApplyNew.getId());
        teacherApplyNew.setId(teacherApplyInDb.getId());
        tdTeacherApplyDao.updateAllColumnByKey(teacherApplyNew);
    }

    @Override
    public TdTeacherApplyDTO getByKey(Long id) {
        TdTeacherApply teacherApply = Optional.ofNullable(tdTeacherApplyDao.getByKey(id)).orElseGet(TdTeacherApply::new);
        ValidationUtil.isNull(teacherApply.getId() ,"TeacherApply", "id", id);
        return tdTeacherApplyMapper.toDto(teacherApply);
    }

    @Override
    public List<TdTeacherApplyDTO> listAll(TdTeacherApplyQueryCriteria criteria) {
        return tdTeacherApplyMapper.toDto(tdTeacherApplyDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdTeacherApplyQueryCriteria criteria, Pageable pageable) {
        Page<TdTeacherApply> page = PageUtil.startPage(pageable);
        List<TdTeacherApply> teacherApplys = tdTeacherApplyDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdTeacherApplyMapper.toDto(teacherApplys), page.getTotal());
    }

    @Override
    public void download(List<TdTeacherApplyDTO> teacherApplyDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdTeacherApplyDTO teacherApplyDTO : teacherApplyDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("讲师", teacherApplyDTO.getPmEmployee().getName());
            map.put("课程主题", teacherApplyDTO.getTitle());
            map.put("课程内容", teacherApplyDTO.getContent());
            map.put("授课经验（年）: 他司+我司", teacherApplyDTO.getTeachExperience());
            map.put("他司经验年限", teacherApplyDTO.getOtherExperienceCompose());
            map.put("我司经验年限", teacherApplyDTO.getMyExperienceCompose());
            map.put("曾授课课题", teacherApplyDTO.getEverExperience());
            map.put("擅长领域", teacherApplyDTO.getBeGoodAt());
            map.put("部门意见", teacherApplyDTO.getDeptOpinion());
            map.put("部门审核人", teacherApplyDTO.getDeptBy());
            map.put("评审组意见：符合、不符合", teacherApplyDTO.getJudgeOpinion());
            map.put("经办人", teacherApplyDTO.getJudgeBy());
            map.put("提交标识，0为保存，1为提交审批", teacherApplyDTO.getSubmitFlag());
            map.put("讲师等级", teacherApplyDTO.getLevel());
            map.put("讲师分数", teacherApplyDTO.getScore());
            map.put("线下认证附件", teacherApplyDTO.getStorage().getName());
            map.put("部门审核日期", teacherApplyDTO.getDeptOpinionDate());
            map.put("评审组审核日期", teacherApplyDTO.getJudgeOpinionDate());
            map.put("attribute3", teacherApplyDTO.getAttribute3());
            map.put("id", teacherApplyDTO.getId());
            map.put("attribute1", teacherApplyDTO.getAttribute1());
            map.put("attribute4", teacherApplyDTO.getAttribute4());
            map.put("attribute5", teacherApplyDTO.getAttribute5());
            map.put("attribute2", teacherApplyDTO.getAttribute2());
            map.put("创建时间", teacherApplyDTO.getCreateTime());
            map.put("创建人", teacherApplyDTO.getCreateBy());
            map.put("最后修改时间", teacherApplyDTO.getUpdateTime());
            map.put("最后修改人", teacherApplyDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional
    public TdTeacherApplyDTO attestationTeacher(TdTeacherApply teacherApplyNew, MultipartFile multipartFile) {
        TdTeacherApply teacherApply = Optional.ofNullable(tdTeacherApplyDao.getByKey(teacherApplyNew.getId())).orElseGet(TdTeacherApply::new);
        ValidationUtil.isNull(teacherApply.getId() ,"TeacherApply", "id", teacherApplyNew.getId());
        teacherApply.setAttribute1(true);
        teacherApply.setScore(teacherApplyNew.getScore());
        teacherApply.setLevel(teacherApplyNew.getLevel());
        teacherApply.setAttribute3(teacherApplyNew.getAttribute3());

        if (null != teacherApply.getStorage() && teacherApply.getStorage().getId() != null) {
            // 当存在附件，删除原附件信息
            toolLocalStorageDao.deleteByKey(teacherApply.getStorage().getId());
        }

        ToolLocalStorage localStorage = null;
        if (StringUtils.isNotBlank(multipartFile.getOriginalFilename())) {
            FileUtil.checkSize(maxSize, multipartFile.getSize());
            String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
            String name = FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename());
            // 可自行选择方式
//        String type = FileUtil.getFileTypeByMimeType(suffix);
            String type = FileUtil.getFileType(suffix);
            File file = FileUtil.upload(multipartFile, attestation + type + File.separator);
            if (ObjectUtil.isNull(file)) {
                throw new BadRequestException("上传失败");
            }
            try {
                localStorage = new ToolLocalStorage(
                        file.getName(),
                        name,
                        suffix,
                        file.getPath(),
                        type,
                        FileUtil.getSize(multipartFile.getSize()),
                        SecurityUtils.getUsername()
                );
                toolLocalStorageDao.insertAllColumn(localStorage);
                teacherApply.setStorage(localStorage);
                tdTeacherApplyDao.updateAttestation(teacherApply);
                insertNowTeacher(teacherApply);
            } catch (Exception e) {
                FileUtil.del(file);
                throw e;
            }
        } else {
            localStorage = new ToolLocalStorage();
            teacherApply.setStorage(localStorage);
            tdTeacherApplyDao.updateAttestation(teacherApply);
            if (teacherApply.getAttribute3()) {
                insertNowTeacher(teacherApply);
            }
        }
        // 插入一条讲师信息
        return tdTeacherApplyMapper.toDto(teacherApply);
    }

    public void insertNowTeacher(TdTeacherApply teacherApply) {
        TdTeacher nowTeacher = tdTeacherDao.getByEmployeeId(teacherApply.getPmEmployee().getId());
        if (null != nowTeacher) {
            throw new InfoCheckWarningMessException("该讲师已存在，请勿重复认证");
        }
        TdTeacher tdTeacher = new TdTeacher();
        tdTeacher.setBeGoodAt(teacherApply.getBeGoodAt());
        tdTeacher.setContent(teacherApply.getContent());
        tdTeacher.setEverExperience(teacherApply.getEverExperience());
        tdTeacher.setLevel(teacherApply.getLevel());
        tdTeacher.setMyExperienceCompose(teacherApply.getMyExperienceCompose());
        tdTeacher.setOtherExperienceCompose(teacherApply.getOtherExperienceCompose());
        tdTeacher.setPassDate(teacherApply.getAttribute2());
        tdTeacher.setPmEmployee(teacherApply.getPmEmployee());
        tdTeacher.setScore(teacherApply.getScore());
        tdTeacher.setTeachExperience(teacherApply.getTeachExperience());
        tdTeacher.setTitle(teacherApply.getTitle());
        tdTeacher.setAttribute1("讲师申请");
        tdTeacher.setAttribute2(teacherApply.getOaOrder());
        tdTeacherDao.insertAllColumn(tdTeacher);
    }

    @Override
    public TdTeacherApplyDTO getByReqCode(String reqCode) {
        return tdTeacherApplyMapper.toDto(tdTeacherApplyDao.getByReqCode(reqCode));
    }

    @Override
    public void writeOaApprovalResult(TdTeacherApply teacherApplyNow) {
        TdTeacherApply teacherApply = Optional.ofNullable(tdTeacherApplyDao.getByKey(teacherApplyNow.getId())).orElseGet(TdTeacherApply::new);
        ValidationUtil.isNull(teacherApply.getId() ,"TeacherApply", "id", teacherApplyNow.getId());

        if ((null != teacherApplyNow.getJudgeOpinion() && !"".equals(teacherApplyNow.getJudgeOpinion())) ||
                (null != teacherApply.getApprovalResult() && teacherApply.getApprovalResult().equals("notPass"))) {
            // 评审组
            tdTeacherApplyDao.updateByApprovalEnd(teacherApplyNow);
        } else if (null != teacherApplyNow.getDeptOpinion() && !"".equals(teacherApplyNow.getDeptOpinion())) {
            tdTeacherApplyDao.updateByApprovalUnderwar(teacherApplyNow);
        } else {
            throw new InfoCheckWarningMessException("反写接口失败");
        }
    }
}
