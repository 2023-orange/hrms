package com.sunten.hrms.cm.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.sunten.hrms.cm.domain.CmDetail;
import com.sunten.hrms.cm.dao.CmDetailDao;
import com.sunten.hrms.cm.mapper.CmDetailDTOMapper;
import com.sunten.hrms.cm.service.CmDetailService;
import com.sunten.hrms.cm.dto.CmDetailDTO;
import com.sunten.hrms.cm.dto.CmDetailQueryCriteria;
import com.sunten.hrms.cm.mapper.CmDetailMapper;
import com.sunten.hrms.cm.vo.CmExcelDataVo;
import com.sunten.hrms.cm.vo.CmExcelVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.File;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
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
 * 工衣明细表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2022-03-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CmDetailServiceImpl extends ServiceImpl<CmDetailDao, CmDetail> implements CmDetailService {
    private final CmDetailDao cmDetailDao;
    private final CmDetailMapper cmDetailMapper;
    private final CmDetailDTOMapper cmDetailDTOMapper;

    public CmDetailServiceImpl(CmDetailDao cmDetailDao, CmDetailMapper cmDetailMapper, CmDetailDTOMapper cmDetailDTOMapper) {
        this.cmDetailDao = cmDetailDao;
        this.cmDetailMapper = cmDetailMapper;
        this.cmDetailDTOMapper = cmDetailDTOMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CmDetailDTO insert(CmDetail detailNew) {
        cmDetailDao.insertAllColumn(detailNew);
        return cmDetailMapper.toDto(detailNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        CmDetail detail = new CmDetail();
        detail.setId(id);
        this.delete(detail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CmDetail detail) {
        // TODO    确认删除前是否需要做检查
        cmDetailDao.deleteByEnabledFlag(detail.getId());
//        cmDetailDao.deleteByEntityKey(detail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CmDetail detailNew) {
        CmDetail detailInDb = Optional.ofNullable(cmDetailDao.getByKey(detailNew.getId())).orElseGet(CmDetail::new);
        ValidationUtil.isNull(detailInDb.getId(), "Detail", "id", detailNew.getId());
        detailNew.setId(detailInDb.getId());
        cmDetailDao.updateAllColumnByKey(detailNew);
    }

    @Override
    public CmDetailDTO getByKey(Long id) {
        CmDetail detail = Optional.ofNullable(cmDetailDao.getByKey(id)).orElseGet(CmDetail::new);
        ValidationUtil.isNull(detail.getId(), "Detail", "id", id);
        return cmDetailMapper.toDto(detail);
    }

    @Override
    public List<CmDetailDTO> listAll(CmDetailQueryCriteria criteria) {
        List<CmDetail> details = cmDetailDao.listAllByCriteria(criteria);
        return cmDetailMapper.toDto(details);
    }

    @Override
    public List<CmDetailDTO> listAllForSearch(CmDetailQueryCriteria criteria) {
        List<CmDetail> details = cmDetailDao.listAllByCriteria(criteria);
        for (CmDetail detail : details) {
            detail.setSuit1(detail.getSuit1() == 0 ? null : detail.getSuit1());
            detail.setSuit2(detail.getSuit2() == 0 ? null : detail.getSuit2());
            detail.setSuit3(detail.getSuit3() == 0 ? null : detail.getSuit3());
            detail.setSuit4(detail.getSuit4() == 0 ? null : detail.getSuit4());
            detail.setFamilyClothing1(detail.getFamilyClothing1() == 0 ? null : detail.getFamilyClothing1());
            detail.setFamilyClothing2(detail.getFamilyClothing2() == 0 ? null : detail.getFamilyClothing2());
            detail.setFamilyClothing3(detail.getFamilyClothing3() == 0 ? null : detail.getFamilyClothing3());
            detail.setFamilyClothing4(detail.getFamilyClothing4() == 0 ? null : detail.getFamilyClothing4());
            detail.setSummerWorkingClothes1(detail.getSummerWorkingClothes1() == 0 ? null : detail.getSummerWorkingClothes1());
            detail.setSummerWorkingClothes2(detail.getSummerWorkingClothes2() == 0 ? null : detail.getSummerWorkingClothes2());
            detail.setSummerWorkingClothes3(detail.getSummerWorkingClothes3() == 0 ? null : detail.getSummerWorkingClothes3());
            detail.setWinterWorkingClothes1(detail.getWinterWorkingClothes1() == 0 ? null : detail.getWinterWorkingClothes1());
            detail.setWinterWorkingClothes2(detail.getWinterWorkingClothes2() == 0 ? null : detail.getWinterWorkingClothes2());
            detail.setPostSaleClothing1(detail.getPostSaleClothing1() == 0 ? null : detail.getPostSaleClothing1());
            detail.setPostSaleClothing2(detail.getPostSaleClothing2() == 0 ? null : detail.getPostSaleClothing2());
            detail.setPostSaleClothing3(detail.getPostSaleClothing3() == 0 ? null : detail.getPostSaleClothing3());
            detail.setPostSaleClothing4(detail.getPostSaleClothing4() == 0 ? null : detail.getPostSaleClothing4());
            detail.setPostSaleClothing5(detail.getPostSaleClothing5() == 0 ? null : detail.getPostSaleClothing5());
            detail.setDiningClothes1(detail.getDiningClothes1() == 0 ? null : detail.getDiningClothes1());
            detail.setDiningClothes2(detail.getDiningClothes2() == 0 ? null : detail.getDiningClothes2());
            detail.setSecurityClothes1(detail.getSecurityClothes1() == 0 ? null : detail.getSecurityClothes1());
            detail.setSecurityClothes2(detail.getSecurityClothes2() == 0 ? null : detail.getSecurityClothes2());
            detail.setSecurityClothes3(detail.getSecurityClothes3() == 0 ? null : detail.getSecurityClothes3());
            detail.setSecurityClothes4(detail.getSecurityClothes4() == 0 ? null : detail.getSecurityClothes4());
        }
        return cmDetailMapper.toDto(details);
    }




    @Override
    public Map<String, Object> listAll(CmDetailQueryCriteria criteria, Pageable pageable) {
        Page<CmDetail> page = PageUtil.startPage(pageable);
        List<CmDetail> details = cmDetailDao.listAllByCriteriaPage(page, criteria);
        this.setCmDetailRemarks(details);
        for (CmDetail detail : details) {
            detail.setExportTime(detail.getExportFlag() ? detail.getUpdateTime() : null);
        }
        return PageUtil.toPage(cmDetailMapper.toDto(details), page.getTotal());
    }

    private void setCmDetailRemarks(List<CmDetail> details) {
        StringBuilder remark;
        String remarkStr;
        for (CmDetail cmDetail : details
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
    public void download(List<CmDetailDTO> detailDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CmDetailDTO detailDTO : detailDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("人员id", detailDTO.getEmployeeId());
            map.put("有效标记", detailDTO.getEnabledFlag());
            map.put("生成类别", detailDTO.getType());
            map.put("年份", detailDTO.getYear());
            map.put("部门（毕竟属于历史记录，适当冗余）", detailDTO.getDept());
            map.put("科室", detailDTO.getDepartment());
            map.put("班组", detailDTO.getTeam());
            map.put("岗位", detailDTO.getJob());
            map.put("成本中心号", detailDTO.getCostCenterNum());
            map.put("审批状态（notCommit、documenter、leader、complete）", detailDTO.getStatus());
            map.put("导出标记", detailDTO.getExportFlag());
            map.put("西服外套", detailDTO.getSuit1());
            map.put("西服马甲", detailDTO.getSuit2());
            map.put("西服裤", detailDTO.getSuit3());
            map.put("西服裙", detailDTO.getSuit4());
            map.put("科服短衬衣", detailDTO.getFamilyClothing1());
            map.put("科服长衬衣", detailDTO.getFamilyClothing2());
            map.put("科服裤", detailDTO.getFamilyClothing3());
            map.put("科服裙", detailDTO.getFamilyClothing4());
            map.put("夏工服（夏牛仔短袖）", detailDTO.getSummerWorkingClothes1());
            map.put("夏工服（夏牛仔长袖）", detailDTO.getSummerWorkingClothes2());
            map.put("夏裤", detailDTO.getSummerWorkingClothes3());
            map.put("冬工服(东上衣)", detailDTO.getWinterWorkingClothes1());
            map.put("冬工服(冬裤)", detailDTO.getWinterWorkingClothes2());
            map.put("售后服装（反光马甲）", detailDTO.getPostSaleClothing1());
            map.put("售后服装（售后款上衣）", detailDTO.getPostSaleClothing2());
            map.put("售后服装（售后款裤）", detailDTO.getPostSaleClothing3());
            map.put("售后服装（售后款冬上衣）", detailDTO.getPostSaleClothing4());
            map.put("售后服装（售后款冬裤）", detailDTO.getPostSaleClothing5());
            map.put("饭堂服装（长袖）", detailDTO.getDiningClothes1());
            map.put("饭堂服装（短袖）", detailDTO.getDiningClothes2());
            map.put("id", detailDTO.getId());
            map.put("attribute2", detailDTO.getAttribute2());
            map.put("attribute3", detailDTO.getAttribute3());
            map.put("attribute1", detailDTO.getAttribute1());
            map.put("attribute4", detailDTO.getAttribute4());
            map.put("updateTime", detailDTO.getUpdateTime());
            map.put("createTime", detailDTO.getCreateTime());
            map.put("updateBy", detailDTO.getUpdateBy());
            map.put("createBy", detailDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadForYear(HttpServletResponse response) throws IOException {
        List<CmExcelVo> cmDetails = cmDetailDao.getCmExportList();
        System.out.println(cmDetails);
        // 利用hutool导出复合header的excel
        String tempPath = System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        BigExcelWriter writer = ExcelUtil.getBigWriter(file);
        writer.setRowHeight(0, 2);
        writer.merge(0, 1, 0, 0, "年份", true);
        writer.addHeaderAlias("year", "年份");
        writer.merge(0, 1, 1, 1, "工号", true);
        writer.addHeaderAlias("workCard", "工号");
        writer.merge(0, 1, 2, 2, "姓名", true);
        writer.addHeaderAlias("name", "姓名");
        writer.merge(0, 1, 3, 3, "部门", true);
        writer.addHeaderAlias("dept", "部门");
        writer.merge(0, 1, 4, 4, "科室", true);
        writer.addHeaderAlias("department", "科室");
        writer.merge(0, 1, 5, 5, "班组", true);
        writer.addHeaderAlias("team", "班组");
        writer.merge(0, 1, 6, 6, "岗位", true);
        writer.addHeaderAlias("job", "岗位");
        writer.merge(0, 1, 7, 7, "成本中心号", true);
        writer.addHeaderAlias("costCenterNum", "成本中心号");


        writer.merge(0, 0, 8, 11, "西服", true);
        writer.merge(0, 0, 12, 15, "科服", true);
        writer.merge(0, 0, 16, 18, "夏工服", true);
        writer.merge(0, 0, 19, 20, "冬工服", true);
        writer.merge(0, 0, 21, 25, "售后服装", true);
        writer.merge(0, 0, 26, 27, "饭堂服装", true);
        writer.merge(0, 0, 28, 31, "保安人员专用服", true);

        // 换一行
        writer.setCurrentRow(writer.getCurrentRow() + 1);
        writer.addHeaderAlias("suit1", "外套");
        writer.addHeaderAlias("suit2", "马甲");
        writer.addHeaderAlias("suit3", "西裤");
        writer.addHeaderAlias("suit4", "西裙");
        writer.addHeaderAlias("familyClothing1", "短衬衣");
        writer.addHeaderAlias("familyClothing2", "长衬衣");
        writer.addHeaderAlias("familyClothing3", "裤");
        writer.addHeaderAlias("familyClothing4", "裙");
        writer.addHeaderAlias("summerWorkingClothes1", "夏牛仔短袖");
        writer.addHeaderAlias("summerWorkingClothes2", "夏牛仔长袖");
        writer.addHeaderAlias("summerWorkingClothes3", "夏裤");
        writer.addHeaderAlias("winterWorkingClothes1", "冬上衣");
        writer.addHeaderAlias("winterWorkingClothes2", "冬裤");
        writer.addHeaderAlias("postSaleClothing1", "反光马甲");
        writer.addHeaderAlias("postSaleClothing2", "售后款上衣");
        writer.addHeaderAlias("postSaleClothing3", "售后款裤");
        writer.addHeaderAlias("postSaleClothing4", "售后款冬上衣");
        writer.addHeaderAlias("postSaleClothing5", "售后款冬裤");
        writer.addHeaderAlias("diningClothes1", "长袖");
        writer.addHeaderAlias("diningClothes2", "短袖");
        writer.addHeaderAlias("securityClothes1", "夏装短袖");
        writer.addHeaderAlias("securityClothes2", "夏装裤");
        writer.addHeaderAlias("securityClothes3", "冬装上衣");
        writer.addHeaderAlias("securityClothes4", "冬装裤");
        List<CmExcelVo> cmDetailList = cmDetailDao.getCmExportList();

        writer.write(cmDetailDao.getCmExportList(), true);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        ServletOutputStream out = response.getOutputStream();
        // 终止后删除临时文件
        file.deleteOnExit();
        writer.flush(out, true);
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
        // 做完后更新数据状态
        cmDetailDao.updateAfterExport(SecurityUtils.getUserId());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadForSearch(List<CmDetailDTO> detailDTOS, HttpServletResponse response) throws IOException {
        // 利用hutool导出复合header的excel
        String tempPath = System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        BigExcelWriter writer = ExcelUtil.getBigWriter(file);

        writer.setRowHeight(0, 2);
        writer.merge(0, 1, 0, 0, "年份", true);
        writer.addHeaderAlias("year", "年份");
        writer.merge(0, 1, 1, 1, "工号", true);
        writer.addHeaderAlias("workCard", "工号");
        writer.merge(0, 1, 2, 2, "姓名", true);
        writer.addHeaderAlias("name", "姓名");
        writer.merge(0, 1, 3, 3, "部门", true);
        writer.addHeaderAlias("dept", "部门");
        writer.merge(0, 1, 4, 4, "科室", true);
        writer.addHeaderAlias("department", "科室");
        writer.merge(0, 1, 5, 5, "班组", true);
        writer.addHeaderAlias("team", "班组");
        writer.merge(0, 1, 6, 6, "岗位", true);
        writer.addHeaderAlias("job", "岗位");


        writer.merge(0, 0, 7, 10, "西服", true);
        writer.merge(0, 0, 11, 14, "科服", true);
        writer.merge(0, 0, 15, 17, "夏工服", true);
        writer.merge(0, 0, 18, 19, "冬工服", true);
        writer.merge(0, 0, 20, 24, "售后服装", true);
        writer.merge(0, 0, 25, 26, "饭堂服装", true);
        writer.merge(0, 0, 27, 30, "保安人员专用服", true);

        // 换一行
        writer.setCurrentRow(writer.getCurrentRow() + 1);
        writer.addHeaderAlias("suit1", "外套");
        writer.addHeaderAlias("suit2", "马甲");
        writer.addHeaderAlias("suit3", "西裤");
        writer.addHeaderAlias("suit4", "西裙");
        writer.addHeaderAlias("familyClothing1", "短衬衣");
        writer.addHeaderAlias("familyClothing2", "长衬衣");
        writer.addHeaderAlias("familyClothing3", "裤");
        writer.addHeaderAlias("familyClothing4", "裙");
        writer.addHeaderAlias("summerWorkingClothes1", "夏牛仔短袖");
        writer.addHeaderAlias("summerWorkingClothes2", "夏牛仔长袖");
        writer.addHeaderAlias("summerWorkingClothes3", "夏裤");
        writer.addHeaderAlias("winterWorkingClothes1", "冬上衣");
        writer.addHeaderAlias("winterWorkingClothes2", "冬裤");
        writer.addHeaderAlias("postSaleClothing1", "反光马甲");
        writer.addHeaderAlias("postSaleClothing2", "售后款上衣");
        writer.addHeaderAlias("postSaleClothing3", "售后款裤");
        writer.addHeaderAlias("postSaleClothing4", "售后款冬上衣");
        writer.addHeaderAlias("postSaleClothing5", "售后款冬裤");
        writer.addHeaderAlias("diningClothes1", "长袖");
        writer.addHeaderAlias("diningClothes2", "短袖");
        writer.addHeaderAlias("securityClothes1", "夏装短袖");
        writer.addHeaderAlias("securityClothes2", "夏装裤");
        writer.addHeaderAlias("securityClothes3", "冬装上衣");
        writer.addHeaderAlias("securityClothes4", "冬装裤");

        List<CmExcelDataVo> cmExcelVos = cmDetailDTOMapper.toEntity(detailDTOS);
        writer.write(cmExcelVos, true);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        ServletOutputStream out = response.getOutputStream();
        // 终止后删除临时文件
        file.deleteOnExit();
        writer.flush(out, true);
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    @Override
    public Boolean checkBeforeDownloadCmReport() {
        return cmDetailDao.getCmExportList().size() > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cmBatchDisposeForDoc(List<CmDetail> cmDetails) {
        for (CmDetail cm : cmDetails
        ) {
            cmDetailDao.updateAllColumnByKey(cm);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateCmList() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", -1);
        map.put("resultStr", "");
        cmDetailDao.generateCmList(map);
        if (!map.get("resultStr").equals("SUCCESS")) {
            throw new InfoCheckWarningMessException((String) map.get("resultStr"));
        }
    }
}
