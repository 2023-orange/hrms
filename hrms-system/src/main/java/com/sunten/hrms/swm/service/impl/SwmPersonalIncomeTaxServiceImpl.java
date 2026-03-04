package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.swm.domain.SwmPersonalIncomeTax;
import com.sunten.hrms.swm.dao.SwmPersonalIncomeTaxDao;
import com.sunten.hrms.swm.service.SwmPersonalIncomeTaxService;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxDTO;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmPersonalIncomeTaxMapper;
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
 * 个人所得税表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmPersonalIncomeTaxServiceImpl extends ServiceImpl<SwmPersonalIncomeTaxDao, SwmPersonalIncomeTax> implements SwmPersonalIncomeTaxService {
    private final SwmPersonalIncomeTaxDao swmPersonalIncomeTaxDao;
    private final SwmPersonalIncomeTaxMapper swmPersonalIncomeTaxMapper;
    private final SwmPostSkillSalaryServiceImpl swmPostSkillSalaryServiceImpl;

    public SwmPersonalIncomeTaxServiceImpl(SwmPersonalIncomeTaxDao swmPersonalIncomeTaxDao, SwmPostSkillSalaryServiceImpl swmPostSkillSalaryServiceImpl,SwmPersonalIncomeTaxMapper swmPersonalIncomeTaxMapper) {
        this.swmPersonalIncomeTaxDao = swmPersonalIncomeTaxDao;
        this.swmPersonalIncomeTaxMapper = swmPersonalIncomeTaxMapper;
        this.swmPostSkillSalaryServiceImpl = swmPostSkillSalaryServiceImpl;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmPersonalIncomeTaxDTO insert(SwmPersonalIncomeTax personalIncomeTaxNew) {
        swmPersonalIncomeTaxDao.insertAllColumn(personalIncomeTaxNew);
        return swmPersonalIncomeTaxMapper.toDto(personalIncomeTaxNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmPersonalIncomeTax personalIncomeTax = new SwmPersonalIncomeTax();
        personalIncomeTax.setId(id);
        this.delete(personalIncomeTax);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmPersonalIncomeTax personalIncomeTax) {
        // TODO    确认删除前是否需要做检查
        swmPersonalIncomeTaxDao.deleteByEntityKey(personalIncomeTax);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmPersonalIncomeTax personalIncomeTaxNew) {
        SwmPersonalIncomeTax personalIncomeTaxInDb = Optional.ofNullable(swmPersonalIncomeTaxDao.getByKey(personalIncomeTaxNew.getId())).orElseGet(SwmPersonalIncomeTax::new);
        ValidationUtil.isNull(personalIncomeTaxInDb.getId() ,"PersonalIncomeTax", "id", personalIncomeTaxNew.getId());
        personalIncomeTaxNew.setId(personalIncomeTaxInDb.getId());
        swmPersonalIncomeTaxDao.updateAllColumnByKey(personalIncomeTaxNew);
    }

    @Override
    public SwmPersonalIncomeTaxDTO getByKey(Long id) {
        SwmPersonalIncomeTax personalIncomeTax = Optional.ofNullable(swmPersonalIncomeTaxDao.getByKey(id)).orElseGet(SwmPersonalIncomeTax::new);
        ValidationUtil.isNull(personalIncomeTax.getId() ,"PersonalIncomeTax", "id", id);
        return swmPersonalIncomeTaxMapper.toDto(personalIncomeTax);
    }

    @Override
    public List<SwmPersonalIncomeTaxDTO> listAll(SwmPersonalIncomeTaxQueryCriteria criteria) {
        criteria.setEnabledFlag(true);
        return swmPersonalIncomeTaxMapper.toDto(swmPersonalIncomeTaxDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmPersonalIncomeTaxQueryCriteria criteria, Pageable pageable) {
        criteria.setEnabledFlag(true);
        Page<SwmPersonalIncomeTax> page = PageUtil.startPage(pageable);
        List<SwmPersonalIncomeTax> personalIncomeTaxs = swmPersonalIncomeTaxDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmPersonalIncomeTaxMapper.toDto(personalIncomeTaxs), page.getTotal());
    }

    @Override
    public void download(List<SwmPersonalIncomeTaxDTO> personalIncomeTaxDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmPersonalIncomeTaxDTO personalIncomeTaxDTO : personalIncomeTaxDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("所得期间（格式：年.月）", personalIncomeTaxDTO.getIncomePeriod());
            map.put("税款所属期", personalIncomeTaxDTO.getTaxPeriod());
            map.put("工牌号", personalIncomeTaxDTO.getWorkCard());
            map.put("员工姓名", personalIncomeTaxDTO.getEmployeeName());
            map.put("本月收入", personalIncomeTaxDTO.getMonthIncome());
            map.put("累计收入", personalIncomeTaxDTO.getAmountIncome());
            map.put("累计社保减除", personalIncomeTaxDTO.getAmountSocialInsuranceDeduct());
            map.put("累计公积金减除", personalIncomeTaxDTO.getAmountAccumulationFundDeduct());
            map.put("累计专项附加减除", personalIncomeTaxDTO.getAmountSpecialAdditionalDeduct());
            map.put("累计起征点减除", personalIncomeTaxDTO.getAmountStartingPointDeduct());
            map.put("累计减除费用", personalIncomeTaxDTO.getAmountDeductExpenses());
            map.put("累计应纳税所得额", personalIncomeTaxDTO.getAmountTaxableIncome());
            map.put("税率", personalIncomeTaxDTO.getTaxRate());
            map.put("速算扣除数", personalIncomeTaxDTO.getQuickCalculationDeduction());
            map.put("累计应缴税额", personalIncomeTaxDTO.getAmountTaxDue());
            map.put("累计已缴税额", personalIncomeTaxDTO.getAmountTaxPaid());
            map.put("累计应补（退税额）", personalIncomeTaxDTO.getAmountTaxRefund());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIncomePeriod(String incomePeriod, Boolean amountFlag) {
        swmPersonalIncomeTaxDao.deleteByIncomePeriod(incomePeriod, amountFlag);
    }

    @Override
    public List<SwmPersonalIncomeTaxDTO> getTaxListByUserId() {
        List<SwmPersonalIncomeTax> tempList = swmPersonalIncomeTaxDao.getTaxListByUserId(SecurityUtils.getUserId());
        if (tempList.size() < 1) {
            throw new InfoCheckWarningMessException("暂无数据");
        } else {
            return swmPersonalIncomeTaxMapper.toDto(tempList);
        }
    }

    @Override
    public List<String> generatePeriodList() {
        String topPeriod = swmPersonalIncomeTaxDao.selectTop2Period();
        return swmPostSkillSalaryServiceImpl.getPeriodList(topPeriod);
    }
}
