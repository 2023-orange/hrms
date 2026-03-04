package com.sunten.hrms.cm.service.impl;

import com.sunten.hrms.cm.domain.CmDetailHistory;
import com.sunten.hrms.cm.dao.CmDetailHistoryDao;
import com.sunten.hrms.cm.service.CmDetailHistoryService;
import com.sunten.hrms.cm.dto.CmDetailHistoryDTO;
import com.sunten.hrms.cm.dto.CmDetailHistoryQueryCriteria;
import com.sunten.hrms.cm.mapper.CmDetailHistoryMapper;
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
 *  服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-02-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CmDetailHistoryServiceImpl extends ServiceImpl<CmDetailHistoryDao, CmDetailHistory> implements CmDetailHistoryService {
    private final CmDetailHistoryDao cmDetailHistoryDao;
    private final CmDetailHistoryMapper cmDetailHistoryMapper;

    public CmDetailHistoryServiceImpl(CmDetailHistoryDao cmDetailHistoryDao, CmDetailHistoryMapper cmDetailHistoryMapper) {
        this.cmDetailHistoryDao = cmDetailHistoryDao;
        this.cmDetailHistoryMapper = cmDetailHistoryMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CmDetailHistoryDTO insert(CmDetailHistory detailHistoryNew) {
        cmDetailHistoryDao.insertAllColumn(detailHistoryNew);
        return cmDetailHistoryMapper.toDto(detailHistoryNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        CmDetailHistory detailHistory = new CmDetailHistory();
        detailHistory.setId(id);
        this.delete(detailHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CmDetailHistory detailHistory) {
        // TODO    确认删除前是否需要做检查
        cmDetailHistoryDao.deleteByEntityKey(detailHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CmDetailHistory detailHistoryNew) {
        CmDetailHistory detailHistoryInDb = Optional.ofNullable(cmDetailHistoryDao.getByKey(detailHistoryNew.getId())).orElseGet(CmDetailHistory::new);
        ValidationUtil.isNull(detailHistoryInDb.getId() ,"DetailHistory", "id", detailHistoryNew.getId());
        detailHistoryNew.setId(detailHistoryInDb.getId());
        cmDetailHistoryDao.updateAllColumnByKey(detailHistoryNew);
    }

    @Override
    public CmDetailHistoryDTO getByKey(Long id) {
        CmDetailHistory detailHistory = Optional.ofNullable(cmDetailHistoryDao.getByKey(id)).orElseGet(CmDetailHistory::new);
        ValidationUtil.isNull(detailHistory.getId() ,"DetailHistory", "id", id);
        return cmDetailHistoryMapper.toDto(detailHistory);
    }

    @Override
    public List<CmDetailHistoryDTO> getByDetailId(Long detailId) {
        List<CmDetailHistory> details = cmDetailHistoryDao.getByDetailId(detailId);
        this.setCmDetailRemarks(details);
        return cmDetailHistoryMapper.toDto(details);
    }

    @Override
    public void insertIntoHistory(Long id) {
        cmDetailHistoryDao.insertIntoHistory(id);
    }

    @Override
    public List<CmDetailHistoryDTO> listAll(CmDetailHistoryQueryCriteria criteria) {
        List<CmDetailHistory> details=cmDetailHistoryDao.listAllByCriteria(criteria);
        return cmDetailHistoryMapper.toDto(details);
    }

    private void setCmDetailRemarks(List<CmDetailHistory> details) {
        StringBuilder remark;
        String remarkStr;
        for (CmDetailHistory cmDetail : details
        ) {
            remark = new StringBuilder();
            if (0 != cmDetail.getSuit1() || 0 != cmDetail.getSuit2() || 0 != cmDetail.getSuit3() || 0 != cmDetail.getSuit4()) {
                remark.append("西服（")
                        .append(0 != cmDetail.getSuit1() ? cmDetail.getSuit1().toString() + "件外套、" : "")
                        .append(0 != cmDetail.getSuit2() ? cmDetail.getSuit2().toString() + "件马甲、" : "")
                        .append(0 != cmDetail.getSuit3() ? cmDetail.getSuit3().toString() + "条西裤、" : "")
                        .append(0 != cmDetail.getSuit4() ? cmDetail.getSuit4().toString() + "条西裙、" : "");
                remarkStr = remark.toString().substring(0, remark.length() - 1);
                remark = new StringBuilder(remarkStr).append("）");
            }
            if (0 != cmDetail.getFamilyClothing1() || 0 != cmDetail.getFamilyClothing2() || 0 != cmDetail.getFamilyClothing3() || 0 != cmDetail.getFamilyClothing4()) {
                remark.append("科服（")
                        .append(0 != cmDetail.getFamilyClothing1() ? cmDetail.getFamilyClothing1().toString() + "件短衬衣、" : "")
                        .append(0 != cmDetail.getFamilyClothing2() ? cmDetail.getFamilyClothing2().toString() + "件长衬衣、" : "")
                        .append(0 != cmDetail.getFamilyClothing3() ? cmDetail.getFamilyClothing3().toString() + "条裤、" : "")
                        .append(0 != cmDetail.getFamilyClothing4() ? cmDetail.getFamilyClothing4().toString() + "条裙、" : "");
                remarkStr = remark.toString().substring(0, remark.length() - 1);
                remark = new StringBuilder(remarkStr).append("）");
            }
            if (0 != cmDetail.getSummerWorkingClothes1() || 0 != cmDetail.getSummerWorkingClothes2() || 0 != cmDetail.getSummerWorkingClothes3()) {
                remark.append("夏工服（")
                        .append(0 != cmDetail.getSummerWorkingClothes1() ? cmDetail.getSummerWorkingClothes1().toString() + "件夏牛仔短袖、" : "")
                        .append(0 != cmDetail.getSummerWorkingClothes2() ? cmDetail.getSummerWorkingClothes2().toString() + "件夏牛仔长袖、" : "")
                        .append(0 != cmDetail.getSummerWorkingClothes3() ? cmDetail.getSummerWorkingClothes3().toString() + "条夏裤、" : "");
                remarkStr = remark.toString().substring(0, remark.length() - 1);
                remark = new StringBuilder(remarkStr).append("）");
            }
            if (0 != cmDetail.getWinterWorkingClothes1() || 0 != cmDetail.getWinterWorkingClothes2()) {
                remark.append("冬工服（")
                        .append(0 != cmDetail.getWinterWorkingClothes1() ? cmDetail.getWinterWorkingClothes1().toString() + "件冬上衣、" : "")
                        .append(0 != cmDetail.getWinterWorkingClothes2() ? cmDetail.getWinterWorkingClothes2().toString() + "条冬裤、" : "");
                remarkStr = remark.toString().substring(0, remark.length() - 1);
                remark = new StringBuilder(remarkStr).append("）");
            }
            if (0 != cmDetail.getPostSaleClothing1() || 0 != cmDetail.getPostSaleClothing2() || 0 != cmDetail.getPostSaleClothing3() || 0 != cmDetail.getPostSaleClothing4()
                    || 0 != cmDetail.getPostSaleClothing5()) {
                remark.append("售后服装（")
                        .append(0 != cmDetail.getPostSaleClothing1() ? cmDetail.getPostSaleClothing1().toString() + "件反光马甲、" : "")
                        .append(0 != cmDetail.getPostSaleClothing2() ? cmDetail.getPostSaleClothing2().toString() + "件售后款上衣、" : "")
                        .append(0 != cmDetail.getPostSaleClothing3() ? cmDetail.getPostSaleClothing3().toString() + "条售后款裤、" : "")
                        .append(0 != cmDetail.getPostSaleClothing4() ? cmDetail.getPostSaleClothing4().toString() + "件售后款冬上衣、" : "")
                        .append(0 != cmDetail.getPostSaleClothing5() ? cmDetail.getPostSaleClothing5().toString() + "条售后款冬裤、" : "");
                remarkStr = remark.toString().substring(0, remark.length() - 1);
                remark = new StringBuilder(remarkStr).append("）");
            }
            if (0 != cmDetail.getDiningClothes1() || 0 != cmDetail.getDiningClothes2()) {
                remark.append("饭堂服装（")
                        .append(0 != cmDetail.getDiningClothes1() ? cmDetail.getDiningClothes1().toString() + "件长袖、" : "")
                        .append(0 != cmDetail.getDiningClothes2() ? cmDetail.getDiningClothes2().toString() + "件短袖、" : "");
                remarkStr = remark.toString().substring(0, remark.length() - 1);
                remark = new StringBuilder(remarkStr).append("）");
            }
            if (0 != cmDetail.getSecurityClothes1() || 0 != cmDetail.getSecurityClothes2() || 0 != cmDetail.getSecurityClothes3() ||
                    0 != cmDetail.getSecurityClothes4()) {
                remark.append("保安人员专用服（")
                        .append(0 != cmDetail.getSecurityClothes1() ? cmDetail.getSecurityClothes1().toString() + "件夏装短袖、" : "")
                        .append(0 != cmDetail.getSecurityClothes2() ? cmDetail.getSecurityClothes2().toString() + "件夏装裤、" : "")
                        .append(0 != cmDetail.getSecurityClothes3() ? cmDetail.getSecurityClothes3().toString() + "件冬装上衣、" : "")
                        .append(0 != cmDetail.getSecurityClothes4() ? cmDetail.getSecurityClothes4().toString() + "件冬装裤、" : "");
                remarkStr = remark.toString().substring(0, remark.length() - 1);
                remark = new StringBuilder(remarkStr).append("）");
            }
            cmDetail.setClothesRemark(remark.toString());
        }
    }


    @Override
    public Map<String, Object> listAll(CmDetailHistoryQueryCriteria criteria, Pageable pageable) {
        Page<CmDetailHistory> page = PageUtil.startPage(pageable);
        List<CmDetailHistory> detailHistorys = cmDetailHistoryDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(cmDetailHistoryMapper.toDto(detailHistorys), page.getTotal());
    }

    @Override
    public void download(List<CmDetailHistoryDTO> detailHistoryDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CmDetailHistoryDTO detailHistoryDTO : detailHistoryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", detailHistoryDTO.getId());
            map.put("detailId", detailHistoryDTO.getDetailId());
            map.put("employeeId", detailHistoryDTO.getEmployeeId());
            map.put("enabledFlag", detailHistoryDTO.getEnabledFlag());
            map.put("attribute1", detailHistoryDTO.getAttribute1());
            map.put("attribute2", detailHistoryDTO.getAttribute2());
            map.put("attribute3", detailHistoryDTO.getAttribute3());
            map.put("attribute4", detailHistoryDTO.getAttribute4());
            map.put("type", detailHistoryDTO.getType());
            map.put("year", detailHistoryDTO.getYear());
            map.put("dept", detailHistoryDTO.getDept());
            map.put("department", detailHistoryDTO.getDepartment());
            map.put("team", detailHistoryDTO.getTeam());
            map.put("job", detailHistoryDTO.getJob());
            map.put("costCenter", detailHistoryDTO.getCostCenterNum());
            map.put("status", detailHistoryDTO.getStatus());
            map.put("exportFlag", detailHistoryDTO.getExportFlag());
            map.put("suit1", detailHistoryDTO.getSuit1());
            map.put("suit2", detailHistoryDTO.getSuit2());
            map.put("suit3", detailHistoryDTO.getSuit3());
            map.put("suit4", detailHistoryDTO.getSuit4());
            map.put("familyClothing1", detailHistoryDTO.getFamilyClothing1());
            map.put("familyClothing2", detailHistoryDTO.getFamilyClothing2());
            map.put("familyClothing3", detailHistoryDTO.getFamilyClothing3());
            map.put("familyClothing4", detailHistoryDTO.getFamilyClothing4());
            map.put("summerWorkingClothes1", detailHistoryDTO.getSummerWorkingClothes1());
            map.put("summerWorkingClothes2", detailHistoryDTO.getSummerWorkingClothes2());
            map.put("summerWorkingClothes3", detailHistoryDTO.getSummerWorkingClothes3());
            map.put("winterWorkingClothes1", detailHistoryDTO.getWinterWorkingClothes1());
            map.put("winterWorkingClothes2", detailHistoryDTO.getWinterWorkingClothes2());
            map.put("postSaleClothing1", detailHistoryDTO.getPostSaleClothing1());
            map.put("postSaleClothing2", detailHistoryDTO.getPostSaleClothing2());
            map.put("postSaleClothing3", detailHistoryDTO.getPostSaleClothing3());
            map.put("postSaleClothing4", detailHistoryDTO.getPostSaleClothing4());
            map.put("postSaleClothing5", detailHistoryDTO.getPostSaleClothing5());
            map.put("diningClothes1", detailHistoryDTO.getDiningClothes1());
            map.put("diningClothes2", detailHistoryDTO.getDiningClothes2());
            map.put("securityClothes1", detailHistoryDTO.getSecurityClothes1());
            map.put("securityClothes2", detailHistoryDTO.getSecurityClothes2());
            map.put("securityClothes3", detailHistoryDTO.getSecurityClothes3());
            map.put("securityClothes4", detailHistoryDTO.getSecurityClothes4());
            map.put("createTime", detailHistoryDTO.getCreateTime());
            map.put("createBy", detailHistoryDTO.getCreateBy());
            map.put("updateTime", detailHistoryDTO.getUpdateTime());
            map.put("updateBy", detailHistoryDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
