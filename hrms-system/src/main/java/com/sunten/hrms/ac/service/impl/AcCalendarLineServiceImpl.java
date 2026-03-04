package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcCalendarHeaderDao;
import com.sunten.hrms.ac.dao.AcCalendarLineDao;
import com.sunten.hrms.ac.domain.AcCalendarHeader;
import com.sunten.hrms.ac.domain.AcCalendarHeaderAndYear;
import com.sunten.hrms.ac.domain.AcCalendarLine;
import com.sunten.hrms.ac.dto.AcCalendarLineDTO;
import com.sunten.hrms.ac.dto.AcCalendarLineQueryCriteria;
import com.sunten.hrms.ac.mapper.AcCalendarLineMapper;
import com.sunten.hrms.ac.service.AcCalendarLineService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 日历详细表 服务实现类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcCalendarLineServiceImpl extends ServiceImpl<AcCalendarLineDao, AcCalendarLine> implements AcCalendarLineService {
    private final AcCalendarLineDao acCalendarLineDao;
    private final AcCalendarLineMapper acCalendarLineMapper;
    private final AcCalendarHeaderDao acCalendarHeaderDao;

    public AcCalendarLineServiceImpl(AcCalendarLineDao acCalendarLineDao, AcCalendarLineMapper acCalendarLineMapper,AcCalendarHeaderDao acCalendarHeaderDao) {
        this.acCalendarLineDao = acCalendarLineDao;
        this.acCalendarLineMapper = acCalendarLineMapper;
        this.acCalendarHeaderDao = acCalendarHeaderDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AcCalendarLineDTO> insert(AcCalendarHeaderAndYear acCalendarHeaderAndYear) {
        // 插入前需要检测当前年份是否已经生成
        List<AcCalendarLine> testAcCalendarLines = acCalendarLineDao.listAllByYear(acCalendarHeaderAndYear.getYear().getYear(), acCalendarHeaderAndYear.getCalendarHeaderId());
        if (testAcCalendarLines.size() > 0) {
            throw new InfoCheckWarningMessException("该年份的日历已经生成，请不要重复生成");
        }
        List<AcCalendarLine> acCalendarLineDTOS = this.generateAcCalendarLines(acCalendarHeaderAndYear.getYear(), acCalendarHeaderAndYear.getCalendarHeaderId());
        // 参数过多分三次批量插入
        // 批量插入时最多可插入2100个参数，分批插入，分三批插入，每批130条
        List<AcCalendarLine> acCalendarLines1 = acCalendarLineDTOS.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() <= 130).collect(Collectors.toList());
        acCalendarLineDao.insertCollection(acCalendarLines1);
        List<AcCalendarLine> acCalendarLines2 = acCalendarLineDTOS.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() <= 260 && AcCalendarLine.getOrder() >130).collect(Collectors.toList());
        acCalendarLineDao.insertCollection(acCalendarLines2);
        List<AcCalendarLine> acCalendarLines3 = acCalendarLineDTOS.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() > 260).collect(Collectors.toList());
        acCalendarLineDao.insertCollection(acCalendarLines3);
        return acCalendarLineMapper.toDto(acCalendarLineDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcCalendarLine calendarLine = new  AcCalendarLine();
        calendarLine.setId(id);
        this.delete(calendarLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcCalendarLine calendarLine) {
        // TODO    确认删除前是否需要做检查
        acCalendarLineDao.deleteByEntityKey(calendarLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcCalendarLine calendarLineNew) {
        AcCalendarLine calendarLineInDb = Optional.ofNullable(acCalendarLineDao.getByKey(calendarLineNew.getId())).orElseGet(AcCalendarLine::new);
        ValidationUtil.isNull(calendarLineInDb.getId() ,"CalendarLine", "id", calendarLineNew.getId());
        calendarLineNew.setId(calendarLineInDb.getId());
        acCalendarLineDao.updateAllColumnByKey(calendarLineNew);
    }

    @Override
    public AcCalendarLineDTO getByKey(Long id) {
        AcCalendarLine calendarLine = Optional.ofNullable(acCalendarLineDao.getByKey(id)).orElseGet(AcCalendarLine::new);
        ValidationUtil.isNull(calendarLine.getId() ,"CalendarLine", "id", id);
        return acCalendarLineMapper.toDto(calendarLine);
    }

    @Override
    public List<AcCalendarLineDTO> listAll(AcCalendarLineQueryCriteria criteria) {
        return acCalendarLineMapper.toDto(acCalendarLineDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcCalendarLineQueryCriteria criteria, Pageable pageable) {
        Page<AcCalendarLine> page = PageUtil.startPage(pageable);
        List<AcCalendarLine> calendarLines = acCalendarLineDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acCalendarLineMapper.toDto(calendarLines), page.getTotal());
    }

    @Override
    public void download(List<AcCalendarLineDTO> calendarLineDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcCalendarLineDTO calendarLineDTO : calendarLineDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", calendarLineDTO.getId());
            map.put("日历主表id", calendarLineDTO.getCalendarHeaderId());
            map.put("日期", calendarLineDTO.getNowDate());
            map.put("星期", calendarLineDTO.getWeek());
            map.put("是否休息日", calendarLineDTO.getDayOffFlag());
            map.put("弹性域1", calendarLineDTO.getAttribute1());
            map.put("弹性域2", calendarLineDTO.getAttribute2());
            map.put("弹性域3", calendarLineDTO.getAttribute3());
            map.put("有效标记", calendarLineDTO.getEnabledFlag());
            map.put("创建人id", calendarLineDTO.getCreateBy());
            map.put("创建时间", calendarLineDTO.getCreateTime());
            map.put("修改人id", calendarLineDTO.getUpdateBy());
            map.put("修改时间", calendarLineDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    /**
     *  @author：liangjw
     *  @Date: 2020/9/24 14:16
     *  @Description: 生成日历lines
     *  @params: LocalDate 年份
     */
    public List<AcCalendarLine> generateAcCalendarLines(LocalDate now, Long acCalendarHeaderId) {
        List<AcCalendarLine> acCalendarLineList = new ArrayList<>();
        String[][] weekOfDay = {{"MONDAY", "星期一"}, {"TUESDAY", "星期二"}, {"WEDNESDAY", "星期三"}, {"THURSDAY", "星期四"}, {"FRIDAY", "星期五"}, {"SATURDAY ", "星期六"}, {"SUNDAY", "星期日"}};
        AcCalendarLine acCalendarLine;
        LocalDate line;
        for (int i = 1; i < 366; i++) {
            acCalendarLine = new AcCalendarLine();
            line = now.withDayOfYear(i);
            acCalendarLine.setNowDate(line);
            for (int j = 0; j < weekOfDay.length; j++) {
                if (weekOfDay[j][0].contains(String.valueOf(line.getDayOfWeek()))) {
                    acCalendarLine.setWeek(weekOfDay[j][1]);
                    if (j < 5) {
                        acCalendarLine.setDayOffFlag(false);
                    } else {
                        acCalendarLine.setDayOffFlag(true);
                    }
                }
            }
            acCalendarLine.setOrder(i);
            acCalendarLine.setEnabledFlag(true);
            acCalendarLine.setCalendarHeaderId(acCalendarHeaderId);
            acCalendarLineList.add(acCalendarLine);
        }
        return acCalendarLineList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoGenerateCalendarLines() { // 每年11月1号生成下一年的日历
        LocalDate now = LocalDate.now();
        AcCalendarHeader acCalendarHeader = acCalendarHeaderDao.getDefaultHeader();
        if (null == acCalendarHeader) {
            throw new InfoCheckWarningMessException("没有默认日历");
        }
        List<AcCalendarLine> testAcCalendarLines = acCalendarLineDao.listAllByYear(now.getYear() + 1, acCalendarHeader.getId());
        if (!(testAcCalendarLines.size() > 0)) {
            // 下一年的日历暂未生成，生成下一年的日历
            List<AcCalendarLine> acCalendarLineDTOS = this.generateAcCalendarLines(now.plusYears(1), acCalendarHeader.getId());
            // 参数过多分三次批量插入
            // 批量插入时最多可插入2100个参数，分批插入，分三批插入，每批130条
            List<AcCalendarLine> acCalendarLines1 = acCalendarLineDTOS.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() <= 130).collect(Collectors.toList());
            acCalendarLineDao.insertCollection(acCalendarLines1);
            List<AcCalendarLine> acCalendarLines2 = acCalendarLineDTOS.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() <= 260 && AcCalendarLine.getOrder() >130).collect(Collectors.toList());
            acCalendarLineDao.insertCollection(acCalendarLines2);
            List<AcCalendarLine> acCalendarLines3 = acCalendarLineDTOS.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() > 260).collect(Collectors.toList());
            acCalendarLineDao.insertCollection(acCalendarLines3);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCollection(List<AcCalendarLine> acCalendarLines) {
        acCalendarLineDao.insertCollection(acCalendarLines);
    }
}
