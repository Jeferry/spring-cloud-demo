package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author mao
 */
public class DateTimeUtil {

    public static String getNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
