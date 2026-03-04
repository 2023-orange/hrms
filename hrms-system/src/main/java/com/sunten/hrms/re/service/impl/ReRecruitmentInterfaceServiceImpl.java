package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.re.dao.ReRecruitmentInterfaceDao;
import com.sunten.hrms.re.domain.ReRecruitmentInterface;
import com.sunten.hrms.re.dto.ReRecruitmentInterfaceDTO;
import com.sunten.hrms.re.dto.ReRecruitmentInterfaceQueryCriteria;
import com.sunten.hrms.re.mapper.ReRecruitmentInterfaceMapper;
import com.sunten.hrms.re.service.ReRecruitmentInterfaceService;
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
 * 招骋数据临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReRecruitmentInterfaceServiceImpl extends ServiceImpl<ReRecruitmentInterfaceDao, ReRecruitmentInterface> implements ReRecruitmentInterfaceService {
    private final ReRecruitmentInterfaceDao reRecruitmentInterfaceDao;
    private final ReRecruitmentInterfaceMapper reRecruitmentInterfaceMapper;

    public ReRecruitmentInterfaceServiceImpl(ReRecruitmentInterfaceDao reRecruitmentInterfaceDao, ReRecruitmentInterfaceMapper reRecruitmentInterfaceMapper) {
        this.reRecruitmentInterfaceDao = reRecruitmentInterfaceDao;
        this.reRecruitmentInterfaceMapper = reRecruitmentInterfaceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReRecruitmentInterfaceDTO insert(ReRecruitmentInterface recruitmentInterfaceNew) {
        reRecruitmentInterfaceDao.insertAllColumn(recruitmentInterfaceNew);
        return reRecruitmentInterfaceMapper.toDto(recruitmentInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReRecruitmentInterface recruitmentInterface = new ReRecruitmentInterface();
        recruitmentInterface.setId(id);
        this.delete(recruitmentInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReRecruitmentInterface recruitmentInterface) {
        // TODO    确认删除前是否需要做检查
        reRecruitmentInterfaceDao.deleteByEntityKey(recruitmentInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReRecruitmentInterface recruitmentInterfaceNew) {
        ReRecruitmentInterface recruitmentInterfaceInDb = Optional.ofNullable(reRecruitmentInterfaceDao.getByKey(recruitmentInterfaceNew.getId())).orElseGet(ReRecruitmentInterface::new);
        ValidationUtil.isNull(recruitmentInterfaceInDb.getId(), "RecruitmentInterface", "id", recruitmentInterfaceNew.getId());
        recruitmentInterfaceNew.setId(recruitmentInterfaceInDb.getId());
        reRecruitmentInterfaceDao.updateAllColumnByKey(recruitmentInterfaceNew);
    }

    @Override
    public ReRecruitmentInterfaceDTO getByKey(Long id) {
        ReRecruitmentInterface recruitmentInterface = Optional.ofNullable(reRecruitmentInterfaceDao.getByKey(id)).orElseGet(ReRecruitmentInterface::new);
        ValidationUtil.isNull(recruitmentInterface.getId(), "RecruitmentInterface", "id", id);
        return reRecruitmentInterfaceMapper.toDto(recruitmentInterface);
    }

    @Override
    public List<ReRecruitmentInterfaceDTO> listAll(ReRecruitmentInterfaceQueryCriteria criteria) {
        return reRecruitmentInterfaceMapper.toDto(reRecruitmentInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReRecruitmentInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<ReRecruitmentInterface> page = PageUtil.startPage(pageable);
        List<ReRecruitmentInterface> recruitmentInterfaces = reRecruitmentInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reRecruitmentInterfaceMapper.toDto(recruitmentInterfaces), page.getTotal());
    }

    @Override
    public void download(List<ReRecruitmentInterfaceDTO> recruitmentInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReRecruitmentInterfaceDTO recruitmentInterfaceDTO : recruitmentInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("岗位名称", recruitmentInterfaceDTO.getJobName());
            map.put("部门科室名称", recruitmentInterfaceDTO.getDeptName());
            map.put("期望年薪", recruitmentInterfaceDTO.getExpectedSalary());
            map.put("性别", recruitmentInterfaceDTO.getGender());
            map.put("身份证号", recruitmentInterfaceDTO.getIdNumber());
            map.put("出生日期", recruitmentInterfaceDTO.getBirthday());
            map.put("身高", recruitmentInterfaceDTO.getHeight());
            map.put("体重", recruitmentInterfaceDTO.getWeight());
            map.put("民族", recruitmentInterfaceDTO.getNation());
            map.put("婚姻状态", recruitmentInterfaceDTO.getMaritalStatus());
            map.put("政治面貌", recruitmentInterfaceDTO.getPolitical());
            map.put("手机号", recruitmentInterfaceDTO.getMobilePhone());
            map.put("邮箱", recruitmentInterfaceDTO.getEmail());
            map.put("户口性质", recruitmentInterfaceDTO.getHouseholdCharacter());
            map.put("现在住址", recruitmentInterfaceDTO.getAddress());
            map.put("现住邮编", recruitmentInterfaceDTO.getZipcode());
            map.put("户口地址", recruitmentInterfaceDTO.getHouseholdAddress());
            map.put("户口邮编", recruitmentInterfaceDTO.getHouseholdZipcode());
            map.put("是否集体户口", recruitmentInterfaceDTO.getCollectiveHouseholdFlag());
            map.put("集体户口所在地", recruitmentInterfaceDTO.getCollectiveAddress());
            map.put("籍贯", recruitmentInterfaceDTO.getNativePlace());
            map.put("是否解除原劳动合同", recruitmentInterfaceDTO.getTerminateContractFlag());
            map.put("是否有职业病", recruitmentInterfaceDTO.getOccupationalDiseasesFlag());
            map.put("是否解除原单位协议限制", recruitmentInterfaceDTO.getTerminationAgreementFlag());
            map.put("健康情况", recruitmentInterfaceDTO.getHealthFlag());
            map.put("入职时间", recruitmentInterfaceDTO.getEntryTime());
            map.put("是否录用", recruitmentInterfaceDTO.getRecruitmentFlag());
            map.put("备注", recruitmentInterfaceDTO.getRemarks());
            map.put("姓名", recruitmentInterfaceDTO.getName());
            map.put("关系", recruitmentInterfaceDTO.getRelationship());
            map.put("单位", recruitmentInterfaceDTO.getCompany());
            map.put("职务", recruitmentInterfaceDTO.getPost());
            map.put("电话", recruitmentInterfaceDTO.getTele());
            map.put("弹性域1", recruitmentInterfaceDTO.getAttribute1());
            map.put("弹性域2", recruitmentInterfaceDTO.getAttribute2());
            map.put("弹性域3", recruitmentInterfaceDTO.getAttribute3());
            map.put("弹性域4", recruitmentInterfaceDTO.getAttribute4());
            map.put("弹性域5", recruitmentInterfaceDTO.getAttribute5());
            map.put("有效标记默认值", recruitmentInterfaceDTO.getEnabledFlag());
            map.put("是否在厂工作", recruitmentInterfaceDTO.getInFactoryFlag());
            map.put("id", recruitmentInterfaceDTO.getId());
            map.put("createBy", recruitmentInterfaceDTO.getCreateBy());
            map.put("updateTime", recruitmentInterfaceDTO.getUpdateTime());
            map.put("createTime", recruitmentInterfaceDTO.getCreateTime());
            map.put("updateBy", recruitmentInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
