package com.ftp.deploy.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZTEN
 * @Date 2019-6-22 下午2:12
 * @Description LocalDateTimeUtils is used to Java8中的时间类
 */
public class LocalDateTimeUtils {

    //获取当前时间的LocalDateTime对象
    //LocalDateTime.now();

    //根据年月日构建LocalDateTime
    //LocalDateTime.of();

    //比较日期先后
    //LocalDateTime.now().isBefore(),
    //LocalDateTime.now().isAfter(),

    /**
     * Date转换为LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param time
     * @return
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获取指定日期的毫秒
     *
     * @param time
     * @return
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     *
     * @param time
     * @return
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取指定时间的指定格式
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String formatTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前时间的指定格式
     *
     * @param pattern
     * @return
     */
    public static String formatNow(String pattern) {
        return formatTime(LocalDateTime.now(), pattern);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     *
     * @param time
     * @param number
     * @param field
     * @return
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     *
     * @param time
     * @param number
     * @param field
     * @return
     */
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     *
     * @param startTime
     * @param endTime
     * @param field     单位(年月日时分秒)
     * @return
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
            return period.getYears();
        }
        if (field == ChronoUnit.MONTHS) {
            return period.getYears() * 12 + period.getMonths();
        }
        return field.between(startTime, endTime);
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static long betweenTwoDate(LocalDate startTime, LocalDate endTime) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        return ChronoUnit.DAYS.between(startTime, endTime);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     *
     * @param time
     * @param number
     * @return
     */
    public static LocalDate plusDate(LocalDate time, long number) {
        return time.plus(number, ChronoUnit.DAYS);
    }

    /**
     * 把LocalDateTime 转换成毫秒
     *
     * @param time 时间
     * @return 毫秒
     */
    public static long transformMilli(LocalDateTime time) {
        return time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 当前时间转换成毫秒
     *
     * @return 毫秒
     */
    public static long currentTimeToMilli() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取一天的开始时间，2017,7,22 00:00
     *
     * @param time
     * @return
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取一天的结束时间，2017,7,22 23:59:59.999999999
     *
     * @param time
     * @return
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }


    public static void main(String[] args) {
        System.out.println(LocalDateTimeUtils.formatNow("yyyy年MM月dd日 HH:mm:ss"));
        long sc = 1800;
        long se = 60;
        LocalDateTime start = LocalDateTime.of(2019, 5, 23, 10, 30, 10);
        System.out.println(LocalDateTimeUtils.transformMilli(start));
        LocalDateTime end = LocalDateTime.of(2019, 5, 23, 10, 30, 20);
        System.out.println(LocalDateTimeUtils.transformMilli(end));
//        LocalDateTime end = LocalDateTimeUtils.plus(LocalDateTime.of(2019, 5, 23, 10, 0,59),30, ChronoUnit.MINUTES);//LocalDateTime.of(2019, 5, 23, 22, 35,30);
        System.out.println("年:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.YEARS));
        System.out.println("月:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MONTHS));
        System.out.println("日:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.DAYS));
        System.out.println("半日:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.HALF_DAYS));
        System.out.println("小时:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.HOURS));
        System.out.println("分钟:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MINUTES));
        System.out.println("秒:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.SECONDS));
        System.out.println("毫秒:" + LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MILLIS));

        long l = transformMilli(start);
        long l1 = transformMilli(end);


        System.out.println("ddd:" + (l1 - l) / 1000);


        long s = 60;

        long hours = LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.HOURS);
        long minutes = LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MINUTES);
        long seconds = LocalDateTimeUtils.betweenTwoTime(start, end, ChronoUnit.SECONDS);

        System.out.println("ddd" + (transformMilli(end) - transformMilli(start)));

        System.out.println("剩余小时:" + hours);
        System.out.println("剩余分钟:" + (minutes - (hours * s)));
        System.out.println("剩余秒:" + (seconds - (minutes * s)));


        //增加二十分钟
        System.out.println(LocalDateTimeUtils.formatTime(LocalDateTimeUtils.plus(LocalDateTime.now(), 20, ChronoUnit.MINUTES), "yyyy年MM月dd日 HH:mm"));
        //增加两年
        System.out.println(LocalDateTimeUtils.formatTime(LocalDateTimeUtils.plus(LocalDateTime.now(), 2, ChronoUnit.YEARS), "yyyy年MM月dd日 HH:mm"));
        //增加一个小时
        System.out.println(LocalDateTimeUtils.formatTime(LocalDateTimeUtils.plus(LocalDateTime.now(), 1, ChronoUnit.HOURS), "yyyy年MM月dd日 HH:mm"));

        System.out.println(getDayStart(LocalDateTime.now()));
        System.out.println(getDayEnd(LocalDateTime.now()));

    }

    public static List<String> getSpecificTimeDifference(LocalDate startTime, LocalDate endTime) {
        Period next = Period.between(startTime, endTime);
        String start = startTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        String end = endTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        int days = next.getDays();
        List<String> list = new ArrayList<>();
        if (days > 2) {
            for (int i = days; i >= 0; i--) {
                LocalDate plus = startTime.plus(i, ChronoUnit.DAYS);
                list.add(plus.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
            }
        } else if (days == 1) {
            list.add(start);
            list.add(end);
        } else if (days == 0) {
            list.add(start);
        }
        return list;
    }
}
