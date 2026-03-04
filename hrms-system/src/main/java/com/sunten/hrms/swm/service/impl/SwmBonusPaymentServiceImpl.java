package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.domain.SwmBonusPayment;
import com.sunten.hrms.swm.dao.SwmBonusPaymentDao;
import com.sunten.hrms.swm.service.SwmBonusPaymentService;
import com.sunten.hrms.swm.dto.SwmBonusPaymentDTO;
import com.sunten.hrms.swm.dto.SwmBonusPaymentQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmBonusPaymentMapper;
import com.sunten.hrms.swm.vo.SelfBonusListVo;
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
 * 奖金发放表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmBonusPaymentServiceImpl extends ServiceImpl<SwmBonusPaymentDao, SwmBonusPayment> implements SwmBonusPaymentService {
    private final SwmBonusPaymentDao swmBonusPaymentDao;
    private final SwmBonusPaymentMapper swmBonusPaymentMapper;
    private final FndUserService fndUserService;

    public SwmBonusPaymentServiceImpl(SwmBonusPaymentDao swmBonusPaymentDao, SwmBonusPaymentMapper swmBonusPaymentMapper,FndUserService fndUserService) {
        this.swmBonusPaymentDao = swmBonusPaymentDao;
        this.swmBonusPaymentMapper = swmBonusPaymentMapper;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmBonusPaymentDTO insert(SwmBonusPayment bonusPaymentNew) {
        swmBonusPaymentDao.insertAllColumn(bonusPaymentNew);
        return swmBonusPaymentMapper.toDto(bonusPaymentNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmBonus swmBonus = new SwmBonus();
        swmBonus.setId(id);
        swmBonusPaymentDao.deleteByBonusId(swmBonus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmBonusPayment bonusPayment) {
        // TODO    确认删除前是否需要做检查
        swmBonusPaymentDao.deleteByEntityKey(bonusPayment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmBonusPayment bonusPaymentNew) {
        SwmBonusPayment bonusPaymentInDb = Optional.ofNullable(swmBonusPaymentDao.getByKey(bonusPaymentNew.getId())).orElseGet(SwmBonusPayment::new);
        ValidationUtil.isNull(bonusPaymentInDb.getId() ,"BonusPayment", "id", bonusPaymentNew.getId());
        bonusPaymentNew.setId(bonusPaymentInDb.getId());
        swmBonusPaymentDao.updateAllColumnByKey(bonusPaymentNew);
    }

    @Override
    public SwmBonusPaymentDTO getByKey(Long id) {
        SwmBonusPayment bonusPayment = Optional.ofNullable(swmBonusPaymentDao.getByKey(id)).orElseGet(SwmBonusPayment::new);
        ValidationUtil.isNull(bonusPayment.getId() ,"BonusPayment", "id", id);
        return swmBonusPaymentMapper.toDto(bonusPayment);
    }

    @Override
    public List<SwmBonusPaymentDTO> listAll(SwmBonusPaymentQueryCriteria criteria) {
        return swmBonusPaymentMapper.toDto(swmBonusPaymentDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmBonusPaymentQueryCriteria criteria, Pageable pageable) {
        Page<SwmBonusPayment> page = PageUtil.startPage(pageable);
        List<SwmBonusPayment> bonusPayments = swmBonusPaymentDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmBonusPaymentMapper.toDto(bonusPayments), page.getTotal());
    }

    @Override
    public Map<String, Object> selfListAll(SwmBonusPaymentQueryCriteria criteria, Pageable pageable) {
        Page<SelfBonusListVo> page = PageUtil.startPage(pageable);
        List<SelfBonusListVo> selfBonusListVos = swmBonusPaymentDao.listSelfByCriteriaPage(page, criteria);
        return PageUtil.toPage(selfBonusListVos, page.getTotal());
    }

    @Override
    public void download(List<SwmBonusPaymentDTO> bonusPaymentDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmBonusPaymentDTO bonusPaymentDTO : bonusPaymentDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("奖金名称", bonusPaymentDTO.getBonusName());
            map.put("工牌号", bonusPaymentDTO.getWorkCard());
            map.put("姓名", bonusPaymentDTO.getEmployeeName());
            map.put("入职时间", bonusPaymentDTO.getEntryTime());
            map.put("部门", bonusPaymentDTO.getDepartment());
            map.put("科室", bonusPaymentDTO.getAdministrativeOffice());
            map.put("银行账户", bonusPaymentDTO.getBankAccount());
            map.put("银行名称", bonusPaymentDTO.getBankName());
            map.put("应发金额", bonusPaymentDTO.getPayableAmount());
            map.put("实发金额_税前", bonusPaymentDTO.getAmountPreTax());
            map.put("扣除所得税", bonusPaymentDTO.getDeductIncomeTax());
            map.put("实发金额_税后", bonusPaymentDTO.getAmountAfterTax());
            map.put("成本中心号", bonusPaymentDTO.getCostCenterNum());
            map.put("成本中心", bonusPaymentDTO.getCostCenter());
            map.put("服务部门", bonusPaymentDTO.getServiceDepartment());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SwmBonusPaymentDTO> generateBonusPayment(SwmBonus swmBonus) {
        if (swmBonusPaymentDao.countByBonusId(swmBonus.getId()) > 0) {
            throw new InfoCheckWarningMessException("该奖金已存在奖金发放, 请先生成再发放");
        } else {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            swmBonus.setUserEmpId(user.getEmployee() != null ? user.getEmployee().getId() : -1);
            swmBonusPaymentDao.generateBonusPayment(swmBonus);
            SwmBonusPaymentQueryCriteria swmBonusPaymentQueryCriteria = new SwmBonusPaymentQueryCriteria();
            swmBonusPaymentQueryCriteria.setBonusId(swmBonus.getId());
            return swmBonusPaymentMapper.toDto(swmBonusPaymentDao.listAllByCriteria(swmBonusPaymentQueryCriteria));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantAllBonus(Long bonusId, Boolean grantFlag) {
        swmBonusPaymentDao.grantAllBonus(bonusId, grantFlag);
    }
}
