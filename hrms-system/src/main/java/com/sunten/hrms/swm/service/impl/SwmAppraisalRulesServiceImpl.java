package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.swm.domain.SwmAppraisalRules;
import com.sunten.hrms.swm.dao.SwmAppraisalRulesDao;
import com.sunten.hrms.swm.service.SwmAppraisalRulesService;
import com.sunten.hrms.swm.dto.SwmAppraisalRulesDTO;
import com.sunten.hrms.swm.dto.SwmAppraisalRulesQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmAppraisalRulesMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * 考核规则 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmAppraisalRulesServiceImpl extends ServiceImpl<SwmAppraisalRulesDao, SwmAppraisalRules> implements SwmAppraisalRulesService {
    private final SwmAppraisalRulesDao swmAppraisalRulesDao;
    private final SwmAppraisalRulesMapper swmAppraisalRulesMapper;

    public SwmAppraisalRulesServiceImpl(SwmAppraisalRulesDao swmAppraisalRulesDao, SwmAppraisalRulesMapper swmAppraisalRulesMapper) {
        this.swmAppraisalRulesDao = swmAppraisalRulesDao;
        this.swmAppraisalRulesMapper = swmAppraisalRulesMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmAppraisalRulesDTO insert(SwmAppraisalRules appraisalRulesNew) {
        Integer count = swmAppraisalRulesDao.checkByGrade(appraisalRulesNew.getAssessmentGrade().trim());
        if (count > 0) {
            throw new InfoCheckWarningMessException("已存在该考核等级，不可重复定义");
        } else {
            swmAppraisalRulesDao.insertAllColumn(appraisalRulesNew);
            return swmAppraisalRulesMapper.toDto(appraisalRulesNew);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmAppraisalRules appraisalRules = new SwmAppraisalRules();
        appraisalRules.setId(id);
        this.delete(appraisalRules);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmAppraisalRules appraisalRules) {
        // TODO    确认删除前是否需要做检查
        swmAppraisalRulesDao.deleteByEntityKey(appraisalRules);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmAppraisalRules appraisalRulesNew) {
        SwmAppraisalRules appraisalRulesInDb = Optional.ofNullable(swmAppraisalRulesDao.getByKey(appraisalRulesNew.getId())).orElseGet(SwmAppraisalRules::new);
        ValidationUtil.isNull(appraisalRulesInDb.getId() ,"AppraisalRules", "id", appraisalRulesNew.getId());
        if (!appraisalRulesNew.getAssessmentGrade().equals(appraisalRulesInDb.getAssessmentGrade())) {
            Integer count = swmAppraisalRulesDao.checkByGrade(appraisalRulesNew.getAssessmentGrade().trim());
            if (count > 0 && appraisalRulesNew.getWorkFlag()) {
                throw new InfoCheckWarningMessException("已存在该考核等级，不可重复定义");
            }
        }
        swmAppraisalRulesDao.updateAllColumnByKey(appraisalRulesNew);
    }

    @Override
    public SwmAppraisalRulesDTO getByKey(Long id) {
        SwmAppraisalRules appraisalRules = Optional.ofNullable(swmAppraisalRulesDao.getByKey(id)).orElseGet(SwmAppraisalRules::new);
        ValidationUtil.isNull(appraisalRules.getId() ,"AppraisalRules", "id", id);
        return swmAppraisalRulesMapper.toDto(appraisalRules);
    }

    @Override
    public List<SwmAppraisalRulesDTO> listAll(SwmAppraisalRulesQueryCriteria criteria) {
        return swmAppraisalRulesMapper.toDto(swmAppraisalRulesDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmAppraisalRulesQueryCriteria criteria, Pageable pageable) {
        Page<SwmAppraisalRules> page = PageUtil.startPage(pageable);
        List<SwmAppraisalRules> appraisalRuless = swmAppraisalRulesDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmAppraisalRulesMapper.toDto(appraisalRuless), page.getTotal());
    }

    @Override
    public void download(List<SwmAppraisalRulesDTO> appraisalRulesDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmAppraisalRulesDTO appraisalRulesDTO : appraisalRulesDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("考核等级", appraisalRulesDTO.getAssessmentGrade());
            map.put("考核权", appraisalRulesDTO.getAssessmentRight());
            map.put("工资权", appraisalRulesDTO.getWageRight());
            map.put("是否生效", appraisalRulesDTO.getWorkFlag() ? "生效" : "不生效");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByWorkFlag(SwmAppraisalRules swmAppraisalRules) {
        swmAppraisalRulesDao.InvalidByEnabled(swmAppraisalRules);
    }

}
