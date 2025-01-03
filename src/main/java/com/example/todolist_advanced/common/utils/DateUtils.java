package com.example.todolist_advanced.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {
    public static LocalDateTime getStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    public static LocalDateTime getEndOfDay(LocalDate date) {
        return date.atTime(23, 59, 59);
    }
}
