package com.sunten.hrms.ac.util;

import com.sunten.hrms.ac.domain.AcAttendanceRecordTemp;
import com.sunten.hrms.ac.vo.AcRegimeTimeVo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 考勤模块公用方法
 *
 * @author batan
 * @since 2020-10-22
 */
public class AcUtil {
    public static List<AcRegimeTimeVo> transferAttendanceAltTimeToTimeList(AcAttendanceRecordTemp attendance, Boolean includeNull) {
        List<AcRegimeTimeVo> timeVos = new ArrayList<>();
        Boolean timeNotNull = attendance.getAltFirstTimeFrom() != null && attendance.getAltFirstTimeTo() != null;
        if (includeNull || timeNotNull) {
            AcRegimeTimeVo timeVo = new AcRegimeTimeVo();
            if (timeNotNull) {
                timeVo.setTimeFrom(attendance.getAltFirstTimeFrom());
                timeVo.setTimeTo(attendance.getAltFirstTimeTo());
                timeVo.setExtendTimeFlag(attendance.getAltExtend1TimeFlag());
            }
            timeVos.add(timeVo);
        }
        timeNotNull = attendance.getAltSecondTimeFrom() != null && attendance.getAltSecondTimeTo() != null;
        if (includeNull || timeNotNull) {
            AcRegimeTimeVo timeVo = new AcRegimeTimeVo();
            if (timeNotNull) {
                timeVo.setTimeFrom(attendance.getAltSecondTimeFrom());
                timeVo.setTimeTo(attendance.getAltSecondTimeTo());
                timeVo.setExtendTimeFlag(attendance.getAltExtend2TimeFlag());
            }
            timeVos.add(timeVo);
        }
        timeNotNull = (attendance.getAltThirdTimeFrom() != null) && (attendance.getAltThirdTimeTo() != null);
        if (includeNull || timeNotNull) {
            AcRegimeTimeVo timeVo = new AcRegimeTimeVo();
            if (timeNotNull) {
                timeVo.setTimeFrom(attendance.getAltThirdTimeFrom());
                timeVo.setTimeTo(attendance.getAltThirdTimeTo());
                timeVo.setExtendTimeFlag(attendance.getAltExtend3TimeFlag());
            }
            timeVos.add(timeVo);
        }
        return timeVos;
    }

    public static void setTimeListToAttendanceAltTime(List<AcRegimeTimeVo> timeVos, AcAttendanceRecordTemp attendance, Boolean includeNull) {
        if (timeVos == null) {
            setAttendanceRestDay(attendance);
        } else {
            if (!includeNull) {
                int size = timeVos.size();
                for (int i = 0; i < size; i++) {
                    timeVos.remove(new AcRegimeTimeVo());
                    timeVos.remove(null);
                }
            }
            switch (timeVos.size()) {
                case 0:
                    setAttendanceRestDay(attendance);
                    break;
                case 3:
                    setThirdTime(attendance, timeVos.get(2));
                case 2:
                    setSecondTime(attendance, timeVos.get(1));
                case 1:
                    setFirstTime(attendance, timeVos.get(0));
                    break;
                default:
                    attendance.setNoScheduling(true);
                    break;
            }
        }
    }

    public static void setAttendanceRestDay(AcAttendanceRecordTemp attendance) {
        AcRegimeTimeVo timeVo = new AcRegimeTimeVo();
        setFirstTime(attendance, timeVo);
        setSecondTime(attendance, timeVo);
        setThirdTime(attendance, timeVo);
        attendance.setAltRestDayFlag(true);
    }

    public static void setFirstTime(AcAttendanceRecordTemp attendance, AcRegimeTimeVo acRegimeTimeVo) {
        attendance.setAltFirstTimeFrom(acRegimeTimeVo.getTimeFrom());
        attendance.setAltFirstTimeTo(acRegimeTimeVo.getTimeTo());
        attendance.setAltExtend1TimeFlag(acRegimeTimeVo.getExtendTimeFlag());
    }

    public static void setSecondTime(AcAttendanceRecordTemp attendance, AcRegimeTimeVo acRegimeTimeVo) {
        attendance.setAltSecondTimeFrom(acRegimeTimeVo.getTimeFrom());
        attendance.setAltSecondTimeTo(acRegimeTimeVo.getTimeTo());
        attendance.setAltExtend2TimeFlag(acRegimeTimeVo.getExtendTimeFlag());
    }

    public static void setThirdTime(AcAttendanceRecordTemp attendance, AcRegimeTimeVo acRegimeTimeVo) {
        attendance.setAltThirdTimeFrom(acRegimeTimeVo.getTimeFrom());
        attendance.setAltThirdTimeTo(acRegimeTimeVo.getTimeTo());
        attendance.setAltExtend3TimeFlag(acRegimeTimeVo.getExtendTimeFlag());
    }

    public static List<LocalDateTime> altTimeByLeave(LocalDateTime timeFrom, LocalDateTime timeTo, LocalDateTime leaveFrom, LocalDateTime leaveTo) {
        List<LocalDateTime> result = new ArrayList<>();
        if (timeFrom.isAfter(leaveTo) || timeFrom.isEqual(leaveTo) || timeTo.isBefore(leaveFrom) || timeTo.isEqual(leaveFrom)) {
            // 排班起始时间 >= 休假结束时间  或  排班结束时间 <= 休假开始时间
            // 排班              |--------------|
            // 休假     ...------|              |------...
            // 休假   ...------|                   |------...
            // 排班时间没有被间隔
            result.add(timeFrom);
            result.add(timeTo);
        } else {
            if ((timeFrom.isAfter(leaveFrom) || timeFrom.equals(leaveFrom))) {
                // 排班起始时间 >= 休假开始时间
                // 排班    |--------------|
                // 休假    |----...
                // 休假  |------...、
                if (timeTo.isAfter(leaveTo)) {
                    // 排班时间中间间隔了休息的结束时间
                    // 排班    |--------------|
                    // 休假    |----------|
                    // 休假  |------------|
                    result.add(leaveTo);
                    result.add(timeTo);
                }
                // 排班时间中间间隔了休息的结束时间
                // 排班    |--------------|
                // 休假    |--------------|
                // 休假    |------------------|
                // 休假  |----------------|
                // 休假  |--------------------|
            } else {
                // 排班起始时间 < 休假开始时间
                // 排班    |--------------|
                // 休假      |----...
                if (timeTo.isAfter(leaveTo)) {
                    // 排班时间中间间隔了休息的起始时间、结束时间
                    // 排班    |--------------|
                    // 休假      |----------|
                    result.add(timeFrom);
                    result.add(leaveFrom);
                    result.add(leaveTo);
                    result.add(timeTo);
                } else {
                    // 排班时间中间间隔了休息的起始时间
                    // 排班    |--------------|
                    // 休假      |------------|
                    // 休假      |----------------|
                    result.add(timeFrom);
                    result.add(leaveFrom);
                }
            }
        }
        return result;
    }

    public static AcRegimeTimeVo createAcRegimeTimeVo(AcAttendanceRecordTemp acAttendacneTemp, List<LocalDateTime> times, int i, int i2) {
        AcRegimeTimeVo temp = new AcRegimeTimeVo();
        temp.setTimeFrom(times.get(i).toLocalTime());
        temp.setTimeTo(times.get(i2).toLocalTime());
        temp.setExtendTimeFlag(times.get(i2).toLocalDate().isAfter(acAttendacneTemp.getDate()));
        return temp;
    }

    //
//
//
//        TimeTemp timeFromTT = new TimeTemp("timeFrom", 1, timeFrom);
//        TimeTemp timeToTT = new TimeTemp("timeTo", -1, timeTo);
//        TimeTemp leaveFromTT = new TimeTemp("leaveFrom", 0, leaveFrom);
//        TimeTemp leaveToTT = new TimeTemp("leaveTo", 0, leaveTo);
//        List<TimeTemp> altTimes = new ArrayList<>();
//        altTimes.add(timeFromTT);
//        altTimes.add(timeToTT);
//        altTimes.add(leaveFromTT);
//        altTimes.add(leaveToTT);
//
//        altTimes.sort(Comparator.comparingLong((TimeTemp tt) -> DateUtil.asLong(tt.getDateTime()) * 10 + tt.getSort()));
//
//
//        List<LocalDateTime> result = new ArrayList<>();
//        for (TimeTemp tt : altTimes) {
//            System.out.println(tt);
//            result.add(tt.getDateTime());
//        }
//    public static class TimeTemp {
//        private String type;
//        private int sort;
//
//        @Override
//        public String toString() {
//            return "TimeTemp{" +
//                    "type='" + type + '\'' +
//                    ", sort=" + sort +
//                    ", dateTime=" + dateTime +
//                    '}';
//        }
//
//        private LocalDateTime dateTime;
//
//        public TimeTemp(String type, int sort, LocalDateTime dateTime) {
//            this.type = type;
//            this.sort = sort;
//            this.dateTime = dateTime;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public int getSort() {
//            return sort;
//        }
//
//        public void setSort(int sort) {
//            this.sort = sort;
//        }
//
//        public LocalDateTime getDateTime() {
//            return dateTime;
//        }
//
//        public void setDateTime(LocalDateTime dateTime) {
//            this.dateTime = dateTime;
//        }
//    }
}
