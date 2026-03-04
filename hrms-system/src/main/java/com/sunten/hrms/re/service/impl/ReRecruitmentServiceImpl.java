package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.re.dao.ReRecruitmentDao;
import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.dto.ReRecruitmentDTO;
import com.sunten.hrms.re.dto.ReRecruitmentQueryCriteria;
import com.sunten.hrms.re.mapper.ReRecruitmentMapper;
import com.sunten.hrms.re.service.*;
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
 * 招骋数据表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReRecruitmentServiceImpl extends ServiceImpl<ReRecruitmentDao, ReRecruitment> implements ReRecruitmentService {
    private final ReRecruitmentDao reRecruitmentDao;
    private final ReRecruitmentMapper reRecruitmentMapper;
    private final ReAwardService reAwardService;
    private final ReEducationService reEducationService;
    private final ReFamilyService reFamilyService;
    private final ReHobbyService reHobbyService;
    private final ReTitleService reTitleService;
    private final ReTrainService reTrainService;
    private final ReVocationalService reVocationalService;
    private final ReWorkhistoryService reWorkhistoryService;

    public ReRecruitmentServiceImpl(ReRecruitmentDao reRecruitmentDao, ReRecruitmentMapper reRecruitmentMapper, ReAwardService reAwardService,
                                    ReEducationService reEducationService, ReFamilyService reFamilyService, ReHobbyService reHobbyService,
                                    ReTitleService reTitleService, ReTrainService reTrainService, ReVocationalService reVocationalService,
                                    ReWorkhistoryService reWorkhistoryService) {
        this.reRecruitmentDao = reRecruitmentDao;
        this.reRecruitmentMapper = reRecruitmentMapper;
        this.reAwardService = reAwardService;
        this.reEducationService = reEducationService;
        this.reFamilyService = reFamilyService;
        this.reHobbyService = reHobbyService;
        this.reTitleService = reTitleService;
        this.reTrainService = reTrainService;
        this.reVocationalService = reVocationalService;
        this.reWorkhistoryService = reWorkhistoryService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReRecruitmentDTO insert(ReRecruitment recruitmentNew) {
        //资料表
        if (recruitmentNew.getId().equals(-1L)) {
            reRecruitmentDao.insertAllColumn(recruitmentNew);
        } else {
            this.update(recruitmentNew);
        }
        //奖罚情况
        if (recruitmentNew.getAwards() != null && recruitmentNew.getAwards().size() > 0)
            reAwardService.batchInsert(new ArrayList(recruitmentNew.getAwards()), recruitmentNew.getId());
        //教育经历
        if (recruitmentNew.getEducations() != null && recruitmentNew.getEducations().size() > 0)
            reEducationService.batchInsert(new ArrayList<>(recruitmentNew.getEducations()), recruitmentNew.getId());
        //家庭情况
        if (recruitmentNew.getFamilies() != null && recruitmentNew.getFamilies().size() > 0)
            reFamilyService.batchInsert(new ArrayList<>(recruitmentNew.getFamilies()), recruitmentNew.getId());
        //技术爱好
        if (recruitmentNew.getHobbies() != null && recruitmentNew.getHobbies().size() > 0)
            reHobbyService.batchInsert(new ArrayList<>(recruitmentNew.getHobbies()), recruitmentNew.getId());
        //职称
        if (recruitmentNew.getTitles() != null && recruitmentNew.getTitles().size() > 0)
            reTitleService.batchInsert(new ArrayList<>(recruitmentNew.getTitles()), recruitmentNew.getId());
        //培训记录
        if (recruitmentNew.getTrains() != null && recruitmentNew.getTrains().size() > 0)
            reTrainService.batchInsert(new ArrayList<>(recruitmentNew.getTrains()), recruitmentNew.getId());
        //工作经历
        if (recruitmentNew.getWorkhistories() != null && recruitmentNew.getWorkhistories().size() > 0)
            reWorkhistoryService.batchInsert(new ArrayList<>(recruitmentNew.getWorkhistories()), recruitmentNew.getId());
        //职业资格
        if (recruitmentNew.getVocationals() != null && recruitmentNew.getVocationals().size() > 0)
            reVocationalService.batchInsert(new ArrayList<>(recruitmentNew.getVocationals()), recruitmentNew.getId());

        return reRecruitmentMapper.toDto(recruitmentNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReRecruitment recruitment = new ReRecruitment();
        recruitment.setId(id);
        recruitment.setEnabledFlag(false);
        this.delete(recruitment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReRecruitment recruitment) {
        //  确认删除前是否需要做检查.只失效。不删除
        reRecruitmentDao.updateEnableFlag(recruitment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReRecruitment recruitmentNew) {
        ReRecruitment recruitmentInDb = Optional.ofNullable(reRecruitmentDao.getByKey(recruitmentNew.getId())).orElseGet(ReRecruitment::new);
        ValidationUtil.isNull(recruitmentInDb.getId(), "Recruitment", "id", recruitmentNew.getId());
        recruitmentNew.setId(recruitmentInDb.getId());
        reRecruitmentDao.updateAllColumnByKey(recruitmentNew);
    }

    @Override
    public ReRecruitmentDTO getByKey(Long id) {
        ReRecruitment recruitment = Optional.ofNullable(reRecruitmentDao.getByKey(id)).orElseGet(ReRecruitment::new);
        ValidationUtil.isNull(recruitment.getId(), "Recruitment", "id", id);
        return reRecruitmentMapper.toDto(recruitment);
    }

    @Override
    public List<ReRecruitmentDTO> listAll(ReRecruitmentQueryCriteria criteria) {
        return reRecruitmentMapper.toDto(reRecruitmentDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReRecruitmentQueryCriteria criteria, Pageable pageable) {
        Page<ReRecruitment> page = PageUtil.startPage(pageable);
        List<ReRecruitment> recruitments = reRecruitmentDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reRecruitmentMapper.toDto(recruitments), page.getTotal());
    }

    @Override
    public void download(List<ReRecruitmentDTO> recruitmentDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReRecruitmentDTO recruitmentDTO : recruitmentDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("岗位名称", recruitmentDTO.getJobName());
            map.put("部门科室名称", recruitmentDTO.getDeptName());
            map.put("期望年薪", recruitmentDTO.getExpectedSalary());
            map.put("性别", recruitmentDTO.getGender());
            map.put("身份证号", recruitmentDTO.getIdNumber());
            map.put("出生日期", recruitmentDTO.getBirthday());
            map.put("身高", recruitmentDTO.getHeight());
            map.put("体重", recruitmentDTO.getWeight());
            map.put("民族", recruitmentDTO.getNation());
            map.put("婚姻状态", recruitmentDTO.getMaritalStatus());
            map.put("政治面貌", recruitmentDTO.getPolitical());
            map.put("手机号", recruitmentDTO.getMobilePhone());
            map.put("邮箱", recruitmentDTO.getEmail());
            map.put("现在住址", recruitmentDTO.getAddress());
            map.put("现住邮编", recruitmentDTO.getZipcode());
            map.put("籍贯", recruitmentDTO.getNativePlace());
            map.put("是否有职业病", recruitmentDTO.getOccupationalDiseasesFlag());
            map.put("健康情况", recruitmentDTO.getHealthFlag());
            map.put("入职时间", recruitmentDTO.getEntryTime());
            map.put("是否录用", recruitmentDTO.getRecruitmentFlag());
            map.put("姓名", recruitmentDTO.getName());
            map.put("关系", recruitmentDTO.getRelationship());
            map.put("弹性域1", recruitmentDTO.getAttribute1());
            map.put("弹性域2", recruitmentDTO.getAttribute2());
            map.put("弹性域3", recruitmentDTO.getAttribute3());
            map.put("弹性域4", recruitmentDTO.getAttribute4());
            map.put("弹性域5", recruitmentDTO.getAttribute5());
            map.put("有效标记默认值", recruitmentDTO.getEnabledFlag());
            map.put("id", recruitmentDTO.getId());
            map.put("createBy", recruitmentDTO.getCreateBy());
            map.put("updateTime", recruitmentDTO.getUpdateTime());
            map.put("createTime", recruitmentDTO.getCreateTime());
            map.put("updateBy", recruitmentDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertByTempInterface(Long groupId) {
        reRecruitmentDao.insertByTempInterface(groupId);
    }
}
