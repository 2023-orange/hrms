package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndInterfaceOperationRecordDao;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dao.SwmPersonalIncomeTaxDao;
import com.sunten.hrms.swm.domain.SwmPersonalIncomeTax;
import com.sunten.hrms.swm.domain.SwmPersonalIncomeTaxInterface;
import com.sunten.hrms.swm.dao.SwmPersonalIncomeTaxInterfaceDao;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxQueryCriteria;
import com.sunten.hrms.swm.service.SwmPersonalIncomeTaxInterfaceService;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxInterfaceQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmPersonalIncomeTaxInterfaceMapper;
import com.sunten.hrms.swm.service.SwmPersonalIncomeTaxService;
import com.sunten.hrms.swm.service.SwmPostSkillSalaryService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 个人所得税接口表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-01-14
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmPersonalIncomeTaxInterfaceServiceImpl extends ServiceImpl<SwmPersonalIncomeTaxInterfaceDao, SwmPersonalIncomeTaxInterface> implements SwmPersonalIncomeTaxInterfaceService {
    private final SwmPersonalIncomeTaxInterfaceDao swmPersonalIncomeTaxInterfaceDao;
    private final SwmPersonalIncomeTaxInterfaceMapper swmPersonalIncomeTaxInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final FndInterfaceOperationRecordDao fndInterfaceOperationRecordDao;
    private final FndUserService fndUserService;
    private final SwmPersonalIncomeTaxDao swmPersonalIncomeTaxDao;
    private final SwmPersonalIncomeTaxService swmPersonalIncomeTaxService;
    private final SwmPostSkillSalaryService swmPostSkillSalaryService;
    @Autowired
    private   SwmPersonalIncomeTaxInterfaceServiceImpl instance;


    public SwmPersonalIncomeTaxInterfaceServiceImpl(SwmPersonalIncomeTaxInterfaceDao swmPersonalIncomeTaxInterfaceDao, SwmPersonalIncomeTaxInterfaceMapper swmPersonalIncomeTaxInterfaceMapper,
                                                    FndInterfaceOperationRecordService fndInterfaceOperationRecordService, FndUserService fndUserService,
                                                    SwmPersonalIncomeTaxDao swmPersonalIncomeTaxDao, FndInterfaceOperationRecordDao fndInterfaceOperationRecordDao,
                                                    SwmPersonalIncomeTaxService swmPersonalIncomeTaxService, SwmPostSkillSalaryService swmPostSkillSalaryService) {
        this.swmPersonalIncomeTaxInterfaceDao = swmPersonalIncomeTaxInterfaceDao;
        this.swmPersonalIncomeTaxInterfaceMapper = swmPersonalIncomeTaxInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.fndUserService = fndUserService;
        this.swmPersonalIncomeTaxDao = swmPersonalIncomeTaxDao;
        this.fndInterfaceOperationRecordDao = fndInterfaceOperationRecordDao;
        this.swmPersonalIncomeTaxService = swmPersonalIncomeTaxService;
        this.swmPostSkillSalaryService = swmPostSkillSalaryService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmPersonalIncomeTaxInterfaceDTO insert(SwmPersonalIncomeTaxInterface personalIncomeTaxInterfaceNew) {
        swmPersonalIncomeTaxInterfaceDao.insertAllColumn(personalIncomeTaxInterfaceNew);
        return swmPersonalIncomeTaxInterfaceMapper.toDto(personalIncomeTaxInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmPersonalIncomeTaxInterface personalIncomeTaxInterface = new SwmPersonalIncomeTaxInterface();
        personalIncomeTaxInterface.setId(id);
        this.delete(personalIncomeTaxInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmPersonalIncomeTaxInterface personalIncomeTaxInterface) {
        // TODO    确认删除前是否需要做检查
        swmPersonalIncomeTaxInterfaceDao.deleteByEntityKey(personalIncomeTaxInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmPersonalIncomeTaxInterface personalIncomeTaxInterfaceNew) {
        SwmPersonalIncomeTaxInterface personalIncomeTaxInterfaceInDb = Optional.ofNullable(swmPersonalIncomeTaxInterfaceDao.getByKey(personalIncomeTaxInterfaceNew.getId())).orElseGet(SwmPersonalIncomeTaxInterface::new);
        ValidationUtil.isNull(personalIncomeTaxInterfaceInDb.getId() ,"PersonalIncomeTaxInterface", "id", personalIncomeTaxInterfaceNew.getId());
        personalIncomeTaxInterfaceNew.setId(personalIncomeTaxInterfaceInDb.getId());
        swmPersonalIncomeTaxInterfaceDao.updateAllColumnByKey(personalIncomeTaxInterfaceNew);
    }

    @Override
    public SwmPersonalIncomeTaxInterfaceDTO getByKey(Long id) {
        SwmPersonalIncomeTaxInterface personalIncomeTaxInterface = Optional.ofNullable(swmPersonalIncomeTaxInterfaceDao.getByKey(id)).orElseGet(SwmPersonalIncomeTaxInterface::new);
        ValidationUtil.isNull(personalIncomeTaxInterface.getId() ,"PersonalIncomeTaxInterface", "id", id);
        return swmPersonalIncomeTaxInterfaceMapper.toDto(personalIncomeTaxInterface);
    }

    @Override
    public List<SwmPersonalIncomeTaxInterfaceDTO> listAll(SwmPersonalIncomeTaxInterfaceQueryCriteria criteria) {
        return swmPersonalIncomeTaxInterfaceMapper.toDto(swmPersonalIncomeTaxInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmPersonalIncomeTaxInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<SwmPersonalIncomeTaxInterface> page = PageUtil.startPage(pageable);
        List<SwmPersonalIncomeTaxInterface> personalIncomeTaxInterfaces = swmPersonalIncomeTaxInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmPersonalIncomeTaxInterfaceMapper.toDto(personalIncomeTaxInterfaces), page.getTotal());
    }

    @Override
    public void download(List<SwmPersonalIncomeTaxInterfaceDTO> personalIncomeTaxInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmPersonalIncomeTaxInterfaceDTO personalIncomeTaxInterfaceDTO : personalIncomeTaxInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id主键", personalIncomeTaxInterfaceDTO.getId());
            map.put("数据分组id", personalIncomeTaxInterfaceDTO.getGroupId());
            map.put("员工id", personalIncomeTaxInterfaceDTO.getEmployeeId());
            map.put("税款所属期起", personalIncomeTaxInterfaceDTO.getTaxPeriodStart());
            map.put("税款所属期止", personalIncomeTaxInterfaceDTO.getTaxPeriodEnd());
            map.put("本期基本养老保险费", personalIncomeTaxInterfaceDTO.getCurrentBasicPensionInsurance());
            map.put("本期基本医疗保险费", personalIncomeTaxInterfaceDTO.getCurrentBasicMedical());
            map.put("本期失业保险费", personalIncomeTaxInterfaceDTO.getCurrentUnemployment());
            map.put("本期住房公积金", personalIncomeTaxInterfaceDTO.getCurrentHousingAccumulationFund());
            map.put("工牌号", personalIncomeTaxInterfaceDTO.getWorkCard());
            map.put("员工姓名", personalIncomeTaxInterfaceDTO.getEmployeeName());
            map.put("本月收入(本期收入)", personalIncomeTaxInterfaceDTO.getMonthIncome());
            map.put("累计收入", personalIncomeTaxInterfaceDTO.getAmountIncome());
            map.put("累计减除费用", personalIncomeTaxInterfaceDTO.getAmountDeductExpenses());
            map.put("累计应纳税所得额", personalIncomeTaxInterfaceDTO.getAmountTaxableIncome());
            map.put("税率", personalIncomeTaxInterfaceDTO.getTaxRate());
            map.put("速算扣除数", personalIncomeTaxInterfaceDTO.getQuickCalculationDeduction());
            map.put("累计应纳税额", personalIncomeTaxInterfaceDTO.getAmountTaxDue());
            map.put("累计已缴税额", personalIncomeTaxInterfaceDTO.getAmountTaxPaid());
            map.put("累计应补（退税额）", personalIncomeTaxInterfaceDTO.getAmountTaxRefund());
            map.put("操作码", personalIncomeTaxInterfaceDTO.getOperationCode());
            map.put("错误信息", personalIncomeTaxInterfaceDTO.getErrorMsg());
            map.put("数据状态", personalIncomeTaxInterfaceDTO.getDataStatus());
            map.put("创建时间", personalIncomeTaxInterfaceDTO.getCreateTime());
            map.put("创建人id", personalIncomeTaxInterfaceDTO.getCreateBy());
            map.put("修改时间", personalIncomeTaxInterfaceDTO.getUpdateTime());
            map.put("修改人id", personalIncomeTaxInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<SwmPersonalIncomeTaxInterfaceDTO> insertExcel(List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces, Boolean amountFlag, Boolean reImportFlag) {
        String incomePeriod = "";
        String taxPeriod = "";
        if (amountFlag) {
            // 自动累加
            for (SwmPersonalIncomeTaxInterface swm : swmPersonalIncomeTaxInterfaces
            ) {
                swm.setTaxPeriodStart(LocalDate.parse(swm.getTaxPeriodStartStr()));
                swm.setTaxPeriodEnd(LocalDate.parse(swm.getTaxPeriodEndStr()));
            }
            LocalDate date = swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriodStart().minusMonths(1L);
            incomePeriod = date.getYear() + "." +  (date.getMonthValue() > 9 ? date.getMonthValue() + "" : "0" + date.getMonthValue());
            taxPeriod = swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriodStart().getYear() + "." +
                    (swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriodStart().getMonthValue() > 9 ? swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriodStart().getMonthValue() + ""
                            : "0" + swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriodStart().getMonthValue());
            // 先删除旧的再导入
            swmPersonalIncomeTaxService.deleteByIncomePeriod(incomePeriod, true);
        } else {
            // 非自动累加
            incomePeriod = swmPersonalIncomeTaxInterfaces.get(0).getIncomePeriod();
            taxPeriod = swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriod();
            // 先删除旧的再导入
            swmPersonalIncomeTaxService.deleteByIncomePeriod(incomePeriod, false);
        }
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        if (amountFlag) {
            fndInterfaceOperationRecord.setOperationValue("insertPersonalIncomeTax");
            fndInterfaceOperationRecord.setOperationDescription("个税导入(自动累计)");
        } else {
            fndInterfaceOperationRecord.setOperationValue("insertPersonalIncomeTaxNotAmount");
            fndInterfaceOperationRecord.setOperationDescription("个税导入(非自动累计)");
        }
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        Long tempGroupId = fndInterfaceOperationRecord.getId();
        try {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            if (amountFlag) {
                instance.insertMainAndSon(swmPersonalIncomeTaxInterfaces, fndInterfaceOperationRecord.getId(), user.getId(), incomePeriod, taxPeriod, true, reImportFlag);
            } else {
                instance.insertMainAndSon(swmPersonalIncomeTaxInterfaces, fndInterfaceOperationRecord.getId(), user.getId(), swmPersonalIncomeTaxInterfaces.get(0).getIncomePeriod(),
                        swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriod(), false, reImportFlag);
            }
        } catch (Exception e) {
            fndInterfaceOperationRecord.setDataProcessingDescription(ThrowableUtil.getStackTrace(e));
            fndInterfaceOperationRecord.setSuccessFlag(false);
            fndInterfaceOperationRecordDao.updateAllColumnByKey(fndInterfaceOperationRecord);
            throw new InfoCheckWarningMessException("导入失败，请联系管理员");
        } finally {
            fndInterfaceOperationRecord.setDataProcessingDescription("导入正常");
            fndInterfaceOperationRecord.setSuccessFlag(true);
            fndInterfaceOperationRecordDao.updateAllColumnByKey(fndInterfaceOperationRecord);
            if (amountFlag) {
                // 将累计应补退税额更新到固定工资的扣除所得税
                swmPostSkillSalaryService.updateDeductIncomeTaxAfterTaxImport(incomePeriod);
                // 更新实发应发
                swmPostSkillSalaryService.updateWageAndNetAfterUpdateCol(incomePeriod);
            }
        }
        SwmPersonalIncomeTaxInterfaceQueryCriteria swmPersonalIncomeTaxInterfaceQueryCriteria = new SwmPersonalIncomeTaxInterfaceQueryCriteria();
        swmPersonalIncomeTaxInterfaceQueryCriteria.setGroupId(tempGroupId);
        swmPersonalIncomeTaxInterfaceQueryCriteria.setDataStatus("F");
        return instance.listAll(swmPersonalIncomeTaxInterfaceQueryCriteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces, Long groupId,
                                 Long userId, String incomePeriod, String taxPeriod, Boolean amountFlag, Boolean reImportFlag) {
        for (SwmPersonalIncomeTaxInterface swm : swmPersonalIncomeTaxInterfaces
             ) {
            swm.setUserId(userId);
            swm.setGroupId(groupId);
            swm.setIncomePeriod(incomePeriod);
            swm.setTaxPeriod(taxPeriod);
            if (reImportFlag) {
                swm.setErrorMsg("");
                swm.setDataStatus("F");
            }
            if (amountFlag) {
                // 自动累计
                swmPersonalIncomeTaxInterfaceDao.insertByInterface(swm);
            } else {
                // 非自动累计
                swmPersonalIncomeTaxInterfaceDao.insertByInterfaceWithNotAmount(swm);
            }
        }
        if (amountFlag) {
            // 自动累计
            swmPersonalIncomeTaxDao.interfaceToMain(groupId);
        } else {
            // 非自动累计
            swmPersonalIncomeTaxDao.interfaceToMainWithNotAmount(groupId);
        }
    }

    @Override
    public Boolean checkTaxWithNotAmount(List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces) {
        if (swmPersonalIncomeTaxInterfaces.size() <= 0) {
            throw new InfoCheckWarningMessException("Excel无内容， 不允许导入");
        }
        if (null == swmPersonalIncomeTaxInterfaces.get(0).getIncomePeriod()) {
            throw new InfoCheckWarningMessException("所得期间不能为空");
        }
        if (null == swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriod()) {
            throw new InfoCheckWarningMessException("税款所属起不能为空");
        }
        SwmPersonalIncomeTaxQueryCriteria swmPersonalIncomeTaxQueryCriteria = new SwmPersonalIncomeTaxQueryCriteria();
        swmPersonalIncomeTaxQueryCriteria.setEnabledFlag(true);
        swmPersonalIncomeTaxQueryCriteria.setAmountFlag(false);
        swmPersonalIncomeTaxQueryCriteria.setIncomePeriod(swmPersonalIncomeTaxInterfaces.get(0).getIncomePeriod());
        return swmPersonalIncomeTaxDao.listAllByCriteria(swmPersonalIncomeTaxQueryCriteria).size() <= 0;
    }

    @Override
    public Boolean checkTax(List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces) {
        if (swmPersonalIncomeTaxInterfaces.size() <= 0) {
            throw new InfoCheckWarningMessException("Excel无内容， 不允许导入");
        }
        try {
            swmPersonalIncomeTaxInterfaces.get(0).setTaxPeriodStart(LocalDate.parse(swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriodStartStr()));
        } catch(Exception e) {
            throw new InfoCheckWarningMessException("税款所属期格式错误，应为YYYY-MM-DD");
        }
//        Pattern patternRole = Pattern.compile("[0-9]{4}\\.[0-9]{2}$");
//        Matcher m  = patternRole.matcher(swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriodStart());
//        if (!m.find()) {
//            throw new InfoCheckWarningMessException("税款所属期的格式应为yyyy.MM");
//        }
//        List<String> incomePeriods = swmPersonalIncomeTaxInterfaces.stream().map(DateUtil.localDateToStr(SwmPersonalIncomeTaxInterface::getTaxPeriodStart)).collect(Collectors.toList());
//        if (incomePeriods.size() > 1 ) {
//            throw new InfoCheckWarningMessException("excel存在多个税款所属期，请按统一的税款所属期导入");
//        }
        // 所得期间等于税款所属期的上一个月
        LocalDate date = swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriodStart().minusMonths(1L);
        String incomePeriod = date.getYear() + "." + (date.getMonthValue() > 9 ? date.getMonthValue() + "" : "0" + date.getMonthValue());


        SwmPersonalIncomeTaxQueryCriteria swmPersonalIncomeTaxQueryCriteria = new SwmPersonalIncomeTaxQueryCriteria();
//        String[] s = swmPersonalIncomeTaxInterfaces.get(0).getTaxPeriodStart().split("\\.");
//        String incomePeriod = "";
//        if ((Integer.parseInt(s[1]) - 1) < 10 && !s[1].equals("01") ) {
//            incomePeriod = s[0] + "." + "0" +  (Integer.parseInt(s[1]) - 1);
//        }
//        if ((Integer.parseInt(s[1]) - 1) < 10 && s[1].equals("01")) {
//            incomePeriod = (Integer.parseInt(s[0]) - 1) + "." + s[1];
//        }
//        if ((Integer.parseInt(s[1]) - 1) >= 10) {
//            incomePeriod = s[0] + "." + (Integer.parseInt(s[1]) - 1);
//        }
        swmPersonalIncomeTaxQueryCriteria.setIncomePeriod(incomePeriod);
        swmPersonalIncomeTaxQueryCriteria.setEnabledFlag(true);
        List<SwmPersonalIncomeTax> swmPersonalIncomeTaxes = swmPersonalIncomeTaxDao.listAllByCriteria(swmPersonalIncomeTaxQueryCriteria);
        return swmPersonalIncomeTaxes.size() <= 0;
    }
}
