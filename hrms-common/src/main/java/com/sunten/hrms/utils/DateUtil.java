package com.sunten.hrms.utils;


import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具
 *
 * @author batan
 * @since 2020-01-03
 */

public class DateUtil {
    // 为什么这个拿不到
//    @Value(value = "${sunten.date-formatter}")
    private static final String dateFormatter = "yyyy-MM-dd";
    private static final String timeFormatter = "HH:mm:ss";
    private static final String dateTimeFormatter = "yyyy-MM-dd HH:mm:ss";
    private static final ZoneId zoneId = ZoneId.systemDefault();

    public static Long asLong(LocalDateTime localDateTime) {
        Long timestamp = localDateTime.atZone(zoneId).toInstant().toEpochMilli();
        return timestamp;
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(zoneId).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(zoneId).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(zoneId).toLocalDateTime();
    }

    public static Timestamp asTimestamp(LocalDate localDate) {
        Long timestamp = localDate.atStartOfDay().atZone(zoneId).toInstant().toEpochMilli();
        return new Timestamp(timestamp);
    }

    public static Timestamp asTimestamp(LocalDateTime localDateTime) {
        long timestamp = localDateTime.atZone(zoneId).toInstant().toEpochMilli();
        return new Timestamp(timestamp);
    }

    public static LocalDate asLocalDate(Timestamp timestamp) {
        return Instant.ofEpochMilli(timestamp.getTime()).atZone(zoneId).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Timestamp timestamp) {
        return Instant.ofEpochMilli(timestamp.getTime()).atZone(zoneId).toLocalDateTime();
    }

    public static String localDateToStr(LocalDate date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFormatter);
        return date.format(fmt);
    }

    public static String localDateTimeToStr(LocalDateTime dateTime) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateTimeFormatter);
        return dateTime.format(fmt);
    }

    public static String localTimeToStr(LocalTime time) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(timeFormatter);
        return time.format(fmt);
    }

    public static LocalDate strToLocalDate(String date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFormatter);
        return LocalDate.parse(date, fmt);
    }

    public static LocalDateTime strToLocalDateTime(String date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateTimeFormatter);
        return LocalDateTime.parse(date, fmt);
    }

    public static LocalTime strToLocalTime(String date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(timeFormatter);
        return LocalTime.parse(date, fmt);
    }
}
