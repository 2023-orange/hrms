package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcCalendarHeaderDao;
import com.sunten.hrms.ac.dao.AcCalendarLineDao;
import com.sunten.hrms.ac.dao.AcDeptAttendanceDao;
import com.sunten.hrms.ac.domain.AcCalendarHeader;
import com.sunten.hrms.ac.domain.AcCalendarLine;
import com.sunten.hrms.ac.domain.AcDeptAttendance;
import com.sunten.hrms.ac.dto.AcCalendarHeaderDTO;
import com.sunten.hrms.ac.dto.AcCalendarHeaderQueryCriteria;
import com.sunten.hrms.ac.dto.AcDeptAttendanceQueryCriteria;
import com.sunten.hrms.ac.mapper.AcCalendarHeaderMapper;
import com.sunten.hrms.ac.service.AcCalendarHeaderService;
import com.sunten.hrms.ac.service.AcCalendarLineService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
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
 * 日历主表 服务实现类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcCalendarHeaderServiceImpl extends ServiceImpl<AcCalendarHeaderDao, AcCalendarHeader> implements AcCalendarHeaderService {
    private final AcCalendarHeaderDao acCalendarHeaderDao;
    private final AcCalendarHeaderMapper acCalendarHeaderMapper;
    private final AcCalendarLineService acCalendarLineService;
    private final AcDeptAttendanceDao acDeptAttendanceDao;


    public AcCalendarHeaderServiceImpl(AcCalendarHeaderDao acCalendarHeaderDao, AcCalendarHeaderMapper acCalendarHeaderMapper,
                                       AcCalendarLineService acCalendarLineService,
                                       AcDeptAttendanceDao acDeptAttendanceDao) {
        this.acCalendarHeaderDao = acCalendarHeaderDao;
        this.acCalendarHeaderMapper = acCalendarHeaderMapper;
        this.acCalendarLineService = acCalendarLineService;
        this.acDeptAttendanceDao = acDeptAttendanceDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcCalendarHeaderDTO insert(AcCalendarHeader calendarHeaderNew) {
        acCalendarHeaderDao.insertAllColumn(calendarHeaderNew);
        AcCalendarHeaderDTO acCalendarHeaderDTO = acCalendarHeaderMapper.toDto(calendarHeaderNew);
        // 生成日历行
        LocalDate now = LocalDate.now();
        List<AcCalendarLine> acCalendarLines =  acCalendarLineService.generateAcCalendarLines(now, acCalendarHeaderDTO.getId());
        // 批量插入时最多可插入2100个参数，分批插入，分三批插入，每批130条
        List<AcCalendarLine> acCalendarLines1 = acCalendarLines.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() <= 130).collect(Collectors.toList());
        acCalendarLineService.insertCollection(acCalendarLines1);
        List<AcCalendarLine> acCalendarLines2 = acCalendarLines.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() <= 260 && AcCalendarLine.getOrder() >130).collect(Collectors.toList());
        acCalendarLineService.insertCollection(acCalendarLines2);
        List<AcCalendarLine> acCalendarLines3 = acCalendarLines.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() > 260).collect(Collectors.toList());
        acCalendarLineService.insertCollection(acCalendarLines3);
        return acCalendarHeaderMapper.toDto(calendarHeaderNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcCalendarHeader calendarHeader = new  AcCalendarHeader();
        calendarHeader.setId(id);
        this.delete(calendarHeader);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcCalendarHeader calendarHeader) {
        // TODO    确认删除前是否需要做检查
        acCalendarHeaderDao.deleteByEntityKey(calendarHeader);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcCalendarHeader calendarHeaderNew) {
        AcCalendarHeader calendarHeaderInDb = Optional.ofNullable(acCalendarHeaderDao.getByKey(calendarHeaderNew.getId())).orElseGet(AcCalendarHeader::new);
        ValidationUtil.isNull(calendarHeaderInDb.getId() ,"CalendarHeader", "id", calendarHeaderNew.getId());
        calendarHeaderNew.setId(calendarHeaderInDb.getId());
        // 失效时对关系作处理
        if (!calendarHeaderNew.getEnabledFlag()) {
            // 找关系
            AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
            acDeptAttendanceQueryCriteria.setCalendarHeaderId(calendarHeaderNew.getId());
            acDeptAttendanceQueryCriteria.setEnabled(true);
            acDeptAttendanceQueryCriteria.setNowAndAfter(true); //查当前及往后
            List<AcDeptAttendance> acDeptAttendances = acDeptAttendanceDao.baseListByCriteria(acDeptAttendanceQueryCriteria);
            if (acDeptAttendances.size() > 0) {
                throw new InfoCheckWarningMessException("该日历已被" + acDeptAttendances.get(0).getFndDept().getDeptName() + "使用且不到失效期限，不允许失效");
            }
        }
        // 对默认标志作判
        if (calendarHeaderNew.getDefaultFlag()) {
            List<AcCalendarHeader> acCalendarHeaders = this.getDefaultAcCalendarHeader();
            if (acCalendarHeaders.size() > 0) {
                for (AcCalendarHeader changeAcCalendarHeader : acCalendarHeaders) {
                    changeAcCalendarHeader.setDefaultFlag(false);
                    acCalendarHeaderDao.updateAllColumnByKey(changeAcCalendarHeader);
                }
            }
        }
        //更新
        acCalendarHeaderDao.updateAllColumnByKey(calendarHeaderNew);
    }

    @Override
    public AcCalendarHeaderDTO getByKey(Long id) {
        AcCalendarHeader calendarHeader = Optional.ofNullable(acCalendarHeaderDao.getByKey(id)).orElseGet(AcCalendarHeader::new);
        ValidationUtil.isNull(calendarHeader.getId() ,"CalendarHeader", "id", id);
        return acCalendarHeaderMapper.toDto(calendarHeader);
    }

    @Override
    public List<AcCalendarHeaderDTO> listAll(AcCalendarHeaderQueryCriteria criteria) {
        return acCalendarHeaderMapper.toDto(acCalendarHeaderDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcCalendarHeaderQueryCriteria criteria, Pageable pageable) {
        Page<AcCalendarHeader> page = PageUtil.startPage(pageable);
        List<AcCalendarHeader> calendarHeaders = acCalendarHeaderDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acCalendarHeaderMapper.toDto(calendarHeaders), page.getTotal());
    }

    @Override
    public void download(List<AcCalendarHeaderDTO> calendarHeaderDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcCalendarHeaderDTO calendarHeaderDTO : calendarHeaderDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", calendarHeaderDTO.getId());
            map.put("日历名称", calendarHeaderDTO.getCalendarName());
            map.put("是否默认日历", calendarHeaderDTO.getDefaultFlag());
            map.put("弹性域1", calendarHeaderDTO.getAttribute1());
            map.put("弹性域2", calendarHeaderDTO.getAttribute2());
            map.put("弹性域3", calendarHeaderDTO.getAttribute3());
            map.put("有效标记", calendarHeaderDTO.getEnabledFlag());
            map.put("创建人id", calendarHeaderDTO.getCreateBy());
            map.put("创建时间", calendarHeaderDTO.getCreateTime());
            map.put("修改人id", calendarHeaderDTO.getUpdateBy());
            map.put("修改时间", calendarHeaderDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    List<AcCalendarHeader> getDefaultAcCalendarHeader(){
        AcCalendarHeaderQueryCriteria acCalendarHeaderQueryCriteria = new AcCalendarHeaderQueryCriteria();
        acCalendarHeaderQueryCriteria.setDefaultFlag(true);
        acCalendarHeaderQueryCriteria.setEnabled(true);
        return acCalendarHeaderDao.listAllByCriteria(acCalendarHeaderQueryCriteria);
    }
}
