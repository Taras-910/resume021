package ua.training.top.util.parser.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.String.valueOf;
import static java.time.LocalDate.now;
import static java.util.List.of;

public class CommonDateUtil {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final List<String> wasteDate = of("обновлено", "більше");

    public static boolean isToday(String myDate) {
        return isContains(myDate, of("сьогодні", "сегодня", "только что"));
    }

    public static boolean isMonth(String myDate) {
        return myDate.length() > 1 && isContains(myDate, of("місяц", "месяц"));
    }

    public static boolean isYesterday(String myDate) {
        return myDate.length() > 1 && isContains(myDate, of("вчера", "вчора"));
    }

    public static boolean isWeek(String myDate) {
        return myDate.matches(".*\\d.*") && isContains(myDate, of("недел", "тиж"));
    }

    public static boolean isMinuteAgo(String myDate) {
        return myDate.matches(".*\\d.*") && isContains(myDate, of("мин", "хв"));
    }

    public static boolean isDay(String myDate) {
        return myDate.length() > 1 && myDate.matches(".*\\d.*") && isContains(myDate, of("день", "дн"));
    }

    public static boolean isHour(String myDate) {
        return myDate.contains("ч") && myDate.matches(".*\\d.*") || isContains(myDate, of("час", "год"));
    }

    public static boolean isContains(String myDate, List<String> list) {
        return list.stream().anyMatch(myDate.toLowerCase()::contains);
    }

    public static String thisYear() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date()).substring(0, 4);
    }

    public static String addYear(String dateTime) {
        return dateTime.concat(" ").concat(now().getMonth().toString().equals("JANUARY") && dateTime.contains("декабря") ?
                valueOf(now().minusYears(1).getYear()) : thisYear());
    }

    public static String getMonth(String month) {
        return switch (month) {
            case "Feb", "лютого", "февраля" -> "02";
            case "Mar", "березня", "марта" -> "03";
            case "Apr", "квітня", "апреля" -> "04";
            case "May", "травня", "мая" -> "05";
            case "Jun", "червня", "июня" -> "06";
            case "Jul", "липня", "июля" -> "07";
            case "Aug", "серпня", "августа" -> "08";
            case "Sep", "вересня", "сентября" -> "09";
            case "Oct", "жовтня", "октября" -> "10";
            case "Nov", "листопада", "ноября" -> "11";
            case "Dec", "грудня", "декабря" -> "12";
            default -> "01";
        };
    }

}
