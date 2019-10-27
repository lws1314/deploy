package com.ftp.deploy.config.date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 全局handler前日期统一处理
 * @author zhanghang
 * @date 2018/1/11
 */

public class DateConverterConfig implements Converter<String, Date> {

    private static final List<String> FORMAT = new ArrayList<>(4);
    static{
        FORMAT.add("yyyy-MM");
        FORMAT.add("yyyy-MM-dd");
        FORMAT.add("yyyy-MM-dd HH:mm");
        FORMAT.add("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public Date convert(@NonNull String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        String value = source.trim();
        if(value.matches("^\\d{4}-\\d{1,2}$")){
            return parseDate(value, FORMAT.get(0));
        }else if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            return parseDate(value, FORMAT.get(1));
        }else if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$")){
            return parseDate(value, FORMAT.get(2));
        }else if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$")){
            return parseDate(value, FORMAT.get(3));
        }else {
            return null;
        }
    }

    /**
     * 格式化日期
     * @param dateStr String 字符型日期
     * @param format String 格式
     * @return Date 日期
     */
    private  Date parseDate(String dateStr, String format) {
        Date date=null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

}