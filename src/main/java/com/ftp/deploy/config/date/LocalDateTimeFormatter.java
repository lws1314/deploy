package com.ftp.deploy.config.date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author - ZEROES
 */
public class LocalDateTimeFormatter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(@NonNull String source) {
        if (source.trim().matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(source,dateTimeFormatter);
        }else if (source.trim().matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}")){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(source,dateTimeFormatter);
        }else{
            return null;
        }
    }
}