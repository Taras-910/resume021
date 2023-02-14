package ua.training.top.util;

import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static ua.training.top.util.aggregateUtil.data.CommonUtil.isEmpty;

public class DateTimeUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final LocalDate
            testDate = LocalDate.of(2020, 7, 30),
            thisDay = LocalDate.now();

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static @Nullable
    LocalTime parseLocalTime(@Nullable String str) {
        return isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static @Nullable
    LocalDate parseLocalDate(@Nullable String str) {
        return isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static @Nullable
    LocalDateTime parseLocalDateTime(@Nullable String str) { return isEmpty(str) ? null : LocalDateTime.parse(str); }
}
