package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcClockRecordRestDao;
import com.sunten.hrms.ac.dao.AcLeaveApplicationDao;
import com.sunten.hrms.ac.domain.AcClockRecordRest;
import com.sunten.hrms.ac.domain.AcLeaveApplication;
import com.sunten.hrms.ac.dto.AcClockRecordDTO;
import com.sunten.hrms.ac.dto.AcClockRecordRestDTO;
import com.sunten.hrms.ac.dto.AcClockRecordRestQueryCriteria;
import com.sunten.hrms.ac.mapper.AcClockRecordRestMapper;
import com.sunten.hrms.ac.service.AcClockRecordRestService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Zouyp
 */
@Service
public class AcClockRecordRestControllerServiceImpl extends ServiceImpl<AcClockRecordRestDao, AcClockRecordRest> implements AcClockRecordRestService {

    @Autowired
    private AcClockRecordRestDao acClockRecordRestDao;
    @Resource
    private AcClockRecordRestMapper acClockRecordRestMapper;
    @Override
    public Map<String, Object> listAll(AcClockRecordRestQueryCriteria criteria, Pageable pageable) {
//        Page<AcClockRecordRest> page = PageUtil.startPage(pageable);
//        System.out.println("pageable.getPageNumber(): " + pageable.getPageNumber());
//        System.out.println("pageable.getOffset(): " + pageable.getOffset());
//        System.out.println("pageable.getPageSize(): " + pageable.getPageSize());
        List<AcClockRecordRest> ClockRecordRests = acClockRecordRestDao.listAll(criteria, (int) pageable.getPageNumber(), pageable.getPageSize());
        List<AcClockRecordRest> clockRecordRestsNew = ClockRecordRests.stream()
                .peek(record -> {
                    Map<String, Object> info = acClockRecordRestDao.getInfo(record.getEmployeeId(), record.getDate());
                    record.setConsecutiveDays((Integer) info.get("ConsecutiveDays"));
                    record.setLatestClockDate((Date) info.get("LatestClockDate"));
                    record.setOvertime((Double) info.get("Overtime"));
                })
                .collect(Collectors.toList());
//        System.out.println("ClockRecordRests: " + ClockRecordRests);
//        List<AcClockRecordRest> ClockRecordRests2 = acClockRecordRestDao.listAll2(criteria, (int) pageable.getOffset(), pageable.getPageSize());
// 使用Stream API进行去重
//        List<AcClockRecordRest> distinctRecords = ClockRecordRests2.stream()
//                .collect(Collectors.collectingAndThen(
//                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(AcClockRecordRest::getDate)
//                                .thenComparing(AcClockRecordRest::getWorkCard))),
//                        ArrayList::new));
//        System.out.println("---ClockRecordRests---: " + distinctRecords);
        return PageUtil.toPage(acClockRecordRestMapper.toDto(clockRecordRestsNew), acClockRecordRestDao.getCount());
    }

    @Override
    public void download(List<AcClockRecordRestDTO> acClockRecordRestDTOS, HttpServletResponse response) throws IOException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fmtDetail = DateTimeFormatter.ofPattern("HH:mm:ss");
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcClockRecordRestDTO clockRecordDTO : acClockRecordRestDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("连续打卡天数", clockRecordDTO.getConsecutiveDays());
            map.put("休息日加班时数", clockRecordDTO.getOvertime());
            map.put("工牌号", clockRecordDTO.getWorkCard());
            map.put("姓名", clockRecordDTO.getName());
            map.put("性别", clockRecordDTO.getGender());
            map.put("部门", clockRecordDTO.getExtDeptName());
            map.put("科室", clockRecordDTO.getExtDepartmentName());
            map.put("班组", clockRecordDTO.getExtTeamName());
            map.put("打卡时间", clockRecordDTO.getClockTimes());
            map.put("打卡日期", fmt.format(clockRecordDTO.getDate()));
            map.put("考勤机说明", clockRecordDTO.getAttendanceMachine());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<AcClockRecordRestDTO> downloadListAll(AcClockRecordRestQueryCriteria criteria, Pageable pageable) {
        Page<AcClockRecordRest> page = PageUtil.startPage(pageable);
//        System.out.println("pageable.getPageNumber(): " + pageable.getPageNumber());
//        System.out.println("pageable.getOffset(): " + pageable.getOffset());
//        System.out.println("pageable.getPageSize(): " + pageable.getPageSize());
        List<AcClockRecordRest> ClockRecordRests = acClockRecordRestDao.listAllDownload(criteria, (int) pageable.getPageNumber(), pageable.getPageSize());
        List<AcClockRecordRest> clockRecordRestsNew = ClockRecordRests.stream()
                .peek(record -> {
                    Map<String, Object> info = acClockRecordRestDao.getInfo(record.getEmployeeId(), record.getDate());
                    record.setConsecutiveDays((Integer) info.get("ConsecutiveDays"));
                    record.setLatestClockDate((Date) info.get("LatestClockDate"));
                    record.setOvertime((Double) info.get("Overtime"));
                })
                .collect(Collectors.toList());
        return acClockRecordRestMapper.toDto(clockRecordRestsNew);
    }

}
