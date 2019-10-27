package com.ftp.deploy.config.date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author - ZEROES
 */
public class LocalTimeFormatter implements Converter<String, LocalTime> {

    @Override
    public LocalTime convert(@NonNull String source) {
        if (source.trim().matches("^\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            return LocalTime.parse(source, formatter);
        }else{
            return null;
        }

    }
}