package ua.training.top.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String DAY_AND_TIME_PATTERN = "HH:mm d MMM yyyy";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final DateTimeFormatter DAY_AND_TIME_FORMATTER = DateTimeFormatter.ofPattern(DAY_AND_TIME_PATTERN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final LocalDateTime tomorrow = LocalDate.now().plusDays(1).atStartOfDay();
    public static final LocalDateTime yesterday = LocalDate.now().minusDays(1).atStartOfDay();
    public static final LocalDate testDate = LocalDate.of(2020, 7, 30);
    public static final LocalDate thisDay = LocalDate.now();

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static String print(LocalDate ldt) {
        return ldt == null ? "" : ldt.format(DATE_FORMATTER);
    }

    public static @Nullable LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static @Nullable LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static @Nullable LocalDateTime parseLocalDateTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDateTime.parse(str);
    }
}
