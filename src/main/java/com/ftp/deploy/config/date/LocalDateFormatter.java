package com.ftp.deploy.config.date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author ZERO
 */
public class LocalDateFormatter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(@NonNull String source) {
        if (source.trim().matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(source,dateTimeFormatter);
        }else{
            return null;
        }

    }
}