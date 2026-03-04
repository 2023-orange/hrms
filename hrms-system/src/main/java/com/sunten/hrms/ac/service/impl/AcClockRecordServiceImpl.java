package com.sunten.hrms.ac.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcCalendarLineDao;
import com.sunten.hrms.ac.dao.AcClockRecordDao;
import com.sunten.hrms.ac.domain.AcCalendarLine;
import com.sunten.hrms.ac.domain.AcClockRecord;
import com.sunten.hrms.ac.dto.AcClockRecordDTO;
import com.sunten.hrms.ac.dto.AcClockRecordQueryCriteria;
import com.sunten.hrms.ac.mapper.AcClockRecordMapper;
import com.sunten.hrms.ac.service.AcClockRecordService;
import com.sunten.hrms.ac.vo.TempEmployeeClockRecordVo;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.border.Border;
import javax.swing.text.DateFormatter;
import javax.swing.text.html.StyleSheet;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 打卡记录表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcClockRecordServiceImpl extends ServiceImpl<AcClockRecordDao, AcClockRecord> implements AcClockRecordService {
    private final AcClockRecordDao acClockRecordDao;
    private final AcClockRecordMapper acClockRecordMapper;
    private final AcCalendarLineDao acCalendarLineDao;

    public AcClockRecordServiceImpl(AcClockRecordDao acClockRecordDao,
                                    AcClockRecordMapper acClockRecordMapper,
                                    AcCalendarLineDao acCalendarLineDao) {
        this.acClockRecordDao = acClockRecordDao;
        this.acClockRecordMapper = acClockRecordMapper;
        this.acCalendarLineDao = acCalendarLineDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcClockRecordDTO insert(AcClockRecord clockRecordNew) {
        acClockRecordDao.insertAllColumn(clockRecordNew);
        return acClockRecordMapper.toDto(clockRecordNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcClockRecord clockRecord = new AcClockRecord();
        clockRecord.setId(id);
        this.delete(clockRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcClockRecord clockRecord) {
        // TODO    确认删除前是否需要做检查
        acClockRecordDao.deleteByEntityKey(clockRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcClockRecord clockRecordNew) {
        AcClockRecord clockRecordInDb = Optional.ofNullable(acClockRecordDao.getByKey(clockRecordNew.getId())).orElseGet(AcClockRecord::new);
        ValidationUtil.isNull(clockRecordInDb.getId(), "ClockRecord", "id", clockRecordNew.getId());
        clockRecordNew.setId(clockRecordInDb.getId());
        acClockRecordDao.updateAllColumnByKey(clockRecordNew);
    }

    @Override
    public AcClockRecordDTO getByKey(Long id) {
        AcClockRecord clockRecord = Optional.ofNullable(acClockRecordDao.getByKey(id)).orElseGet(AcClockRecord::new);
        ValidationUtil.isNull(clockRecord.getId(), "ClockRecord", "id", id);
        return acClockRecordMapper.toDto(clockRecord);
    }

    @Override
    public List<AcClockRecordDTO> listAll(AcClockRecordQueryCriteria criteria) {
        return acClockRecordMapper.toDto(acClockRecordDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcClockRecordQueryCriteria criteria, Pageable pageable) {
//        Sort sort = Sort.by(Sort.Direction.DESC, "date").and(Sort.by(Sort.Direction.DESC, "clock_time"));//.by(Sort.Direction.ASC, "id");
//        pageable = PageRequest.of(1, 100, sort);
        Page<AcClockRecord> page = PageUtil.startPage(pageable);
        List<AcClockRecord> clockRecords = acClockRecordDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acClockRecordMapper.toDto(clockRecords), page.getTotal());
    }

    @Override
    public void download(List<AcClockRecordDTO> clockRecordDTOS, HttpServletResponse response) throws IOException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fmtDetail = DateTimeFormatter.ofPattern("HH:mm:ss");
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcClockRecordDTO clockRecordDTO : clockRecordDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("工牌号", clockRecordDTO.getEmployee().getWorkCard());
            map.put("姓名", clockRecordDTO.getEmployee().getName());
            map.put("部门", clockRecordDTO.getEmployee().getDeptName());
            map.put("科室", clockRecordDTO.getEmployee().getDepartment());
            map.put("打卡时间", fmtDetail.format(clockRecordDTO.getClockTime()));
            map.put("日期", fmt.format(clockRecordDTO.getDate()));
            map.put("考勤机说明", clockRecordDTO.getCardMachineNo());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     *  @author liangjw
     *  @since 2021/10/8 14:13
     *  每月一号再次为employee_id为-1的数据更新一次employee_id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmployeeIdMonthly() {
        acClockRecordDao.updateEmployeeIdMonthly();
    }

    /**
     *  @author liangjw
     *  @since 2021/12/21 10:23
     *  存在username 返回1 否则0，针对fake打卡记录
     */
    @Override
    public Boolean getFakeRecordSetting(String userName) {
        return acClockRecordDao.getFakeRecordSetting(userName);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClockForOutRest(AcClockRecord clockRecord) {
        acClockRecordDao.updateClockForOutRest(clockRecord);
    }

    /**
     *  @author liangjw
     *  @since 2022/5/11 10:16
     *  临工打卡记录判断
     */
    @Override
    public void exportTempEmployeeRecordList(HttpServletResponse response) throws IOException {
        String tempPath =System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        BigExcelWriter writer= ExcelUtil.getBigWriter(file);
//        SXSSFWorkbook workbook = new SXSSFWorkbook(9999);
//        SXSSFSheet styleSheet = workbook.createSheet();
//        styleSheet.createRow(0);
//        StyleSet styleSet = new StyleSet(workbook);
//        styleSet.setWrapText();
//        writer.setStyleSet(styleSet);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalDate beginDate = LocalDate.of(LocalDate.now().minusMonths(1).getYear(), LocalDate.now().minusMonths(1).getMonthValue(), 1);
        LocalDate endDate = LocalDate.of(LocalDate.now().minusMonths(1).getYear(), LocalDate.now().minusMonths(1).getMonthValue(),
                LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1).minusDays(1).getDayOfMonth()
                );
        List<AcCalendarLine> acCalendarLines = acCalendarLineDao.listDefaultCalendarLineByDate(beginDate, endDate);
        int count = acCalendarLines.size() + 2;
        List<TempEmployeeClockRecordVo> employeeClockRecordVos = acClockRecordDao.listTempEmployeeClockRecordList(beginDate, endDate);
        writer.writeCellValue(0, writer.getCurrentRow(), "姓名");
        writer.writeCellValue(1, writer.getCurrentRow(), "工号");
        writer.writeCellValue(2, writer.getCurrentRow(), "部门");
        writer.writeCellValue(3, writer.getCurrentRow(), "科室");
        writer.writeCellValue(4, writer.getCurrentRow(), "班组");
        int calendarLineNum = 5;
        for (AcCalendarLine acCalendarLine : acCalendarLines) {
            writer.setColumnWidth(calendarLineNum, 12);
            writer.writeCellValue(calendarLineNum++, writer.getCurrentRow(), dateFormatter.format(acCalendarLine.getNowDate()));
        }
        String tempWorkCard = "";
        String result = "";
        int number = 0;
        for (TempEmployeeClockRecordVo tempEmployeeClockRecordVo : employeeClockRecordVos) {
            if (null != tempEmployeeClockRecordVo.getMinClockTime() && null != tempEmployeeClockRecordVo.getMaxClockTime()
                && tempEmployeeClockRecordVo.getMaxClockTime().equals(tempEmployeeClockRecordVo.getMinClockTime())
            ) {
                tempEmployeeClockRecordVo.setMaxClockTime(null);
            }

            if (!tempWorkCard.equals(tempEmployeeClockRecordVo.getWorkCard())) {
                writer.setCurrentRow(writer.getCurrentRow() + 1);
                writer.writeCellValue(0 , writer.getCurrentRow(), tempEmployeeClockRecordVo.getName());
                tempWorkCard = tempEmployeeClockRecordVo.getWorkCard();
                result = setResultString(tempEmployeeClockRecordVo);
                if (result.equals("正常") || result.equals("已离职") || result.equals("未入职")) {
                    writer.writeCellValue(5, writer.getCurrentRow(), (null != tempEmployeeClockRecordVo.getMinClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMinClockTime()) + "\n" : "") +
                            (null != tempEmployeeClockRecordVo.getMaxClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMaxClockTime()) + "\n" : "") + result);
                    createMyCellStyle(5, writer.getCurrentRow(), writer, false);
                } else {
                    writer.writeCellValue(5, writer.getCurrentRow(), (null != tempEmployeeClockRecordVo.getMinClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMinClockTime()) + "\n" : "") +
                            (null != tempEmployeeClockRecordVo.getMaxClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMaxClockTime()) + "\n" : "") + result);
                    createMyCellStyle(5, writer.getCurrentRow(), writer, true);
                }
                number = 1;
            } else {
                if (number == 1) {
                    writer.writeCellValue(number, writer.getCurrentRow(), tempEmployeeClockRecordVo.getWorkCard());
                    writer.writeCellValue(2, writer.getCurrentRow(), tempEmployeeClockRecordVo.getDeptName());
                    writer.writeCellValue(3, writer.getCurrentRow(), tempEmployeeClockRecordVo.getDepartmentName());
                    writer.writeCellValue(4, writer.getCurrentRow(), tempEmployeeClockRecordVo.getTeamName());
                    result = setResultString(tempEmployeeClockRecordVo);
                    if (result.equals("正常") || result.equals("已离职") || result.equals("未入职")) {
                        writer.writeCellValue(6, writer.getCurrentRow(), (null != tempEmployeeClockRecordVo.getMinClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMinClockTime()) + "\n" : "") +
                                (null != tempEmployeeClockRecordVo.getMaxClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMaxClockTime()) + "\n" : "") + result);
                        createMyCellStyle(6, writer.getCurrentRow(), writer, false);
                    } else {
                        writer.writeCellValue(6, writer.getCurrentRow(), (null != tempEmployeeClockRecordVo.getMinClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMinClockTime()) + "\n" : "") +
                                (null != tempEmployeeClockRecordVo.getMaxClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMaxClockTime()) + "\n" : "") + result);
                        createMyCellStyle(6, writer.getCurrentRow(), writer, true);
                    }
                    number = 7;
                    continue;
                }
                if (number >= 7) {
                    result = setResultString(tempEmployeeClockRecordVo);
                    if (result.equals("正常") || result.equals("已离职") || result.equals("未入职")) {
                        writer.writeCellValue(number, writer.getCurrentRow(), (null != tempEmployeeClockRecordVo.getMinClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMinClockTime())+ "\n" : "") +
                                (null != tempEmployeeClockRecordVo.getMaxClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMaxClockTime()) + "\n" : "") + result);
                        createMyCellStyle(number, writer.getCurrentRow(), writer, false);
                    } else {
                        writer.writeCellValue(number, writer.getCurrentRow(), (null != tempEmployeeClockRecordVo.getMinClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMinClockTime())+ "\n" : "") +
                                (null != tempEmployeeClockRecordVo.getMaxClockTime() ? fmt.format(tempEmployeeClockRecordVo.getMaxClockTime()) + "\n" : "") + result);
                        createMyCellStyle(number, writer.getCurrentRow(), writer, true);
                    }
                    number ++;
                }
            }
            writer.setRowHeight(writer.getCurrentRow(), 60);
        }
//        TempEmployeeClockRecordVo temp = employeeClockRecordVos.get(employeeClockRecordVos.size() - 1);
//        setWriter(writer, fmt, count, temp);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=file.xlsx");
        ServletOutputStream out=response.getOutputStream();
        // 终止后删除临时文件
        file.deleteOnExit();
        writer.flush(out, true);
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    private void createMyCellStyle(int x, int y, ExcelWriter writer, Boolean errorFlag) {
        CellStyle cellStyle = writer.createCellStyle(x, y);
        cellStyle.setWrapText(true);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = writer.createFont();
        if (errorFlag) {
            font.setColor(Font.COLOR_RED);
        } else {
            font.setColor(Font.COLOR_NORMAL);
        }
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
    }


    private String setResultString(TempEmployeeClockRecordVo tempEmployeeClockRecordVo) {
        String result;
        if (tempEmployeeClockRecordVo.getLeaveFlag()) {
            result = "已离职";
            return result;
        } else {
            if (!tempEmployeeClockRecordVo.getWorkFlag()) {
                result = "未入职";
                return  result;
            }
        }
        if (!tempEmployeeClockRecordVo.getDayOffFlag()) {
            if (null == tempEmployeeClockRecordVo.getMinClockTime() && null == tempEmployeeClockRecordVo.getMaxClockTime()) {
                result = "没打卡";
            }
            else if (null == tempEmployeeClockRecordVo.getMinClockTime() || null == tempEmployeeClockRecordVo.getMaxClockTime()) {
                result = "非正常";
            }
            else if (tempEmployeeClockRecordVo.getMinClockTime().isAfter(LocalTime.of(8,15))
                    || tempEmployeeClockRecordVo.getMaxClockTime().isBefore(LocalTime.of(17,0))) {
                result = "非正常";
            }
            else {
                result = "正常";
            }
        } else {
            if (null == tempEmployeeClockRecordVo.getMinClockTime() && null == tempEmployeeClockRecordVo.getMaxClockTime()) {
                result = "没打卡或正常";
            } else if (null == tempEmployeeClockRecordVo.getMinClockTime() || null == tempEmployeeClockRecordVo.getMaxClockTime()){
                result = "非正常";
            } else if (tempEmployeeClockRecordVo.getMinClockTime().isAfter(LocalTime.of(8,15))
                    || tempEmployeeClockRecordVo.getMaxClockTime().isBefore(LocalTime.of(17,0))) {
                result = "非正常";
            } else {
                result = "正常";
            }
        }
        return result;
    }
}
