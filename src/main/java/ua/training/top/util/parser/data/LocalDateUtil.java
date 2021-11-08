package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.LocalDate.now;
import static java.util.List.of;
import static ua.training.top.util.parser.data.CommonDataUtil.*;

public class LocalDateUtil {
    private final static Logger log = LoggerFactory.getLogger(LocalDateUtil.class);

    public static LocalDate getToLocalDate(String siteName, String originText) {
        try {
            String text = getBruteCleaning(originText);
            return switch (siteName) {
                case djinni, grc -> getByDayAndMonth(text);
                case rabota, work -> getByTimeAgo(text);
                case habr -> LocalDate.parse(text.contains("T") ? text.substring(0, text.indexOf("T")) : text);
                default -> now().minusMonths(1);
            };
        } catch (Exception e) {
            log.error(error, e.getMessage(), originText);
            return now().minusMonths(1);
        }
    }

    public static LocalDate getByTimeAgo(String text) {
        text = text.replaceAll("вчера", "1 день");
        if (isEmpty(text) || !text.contains(" ")) {
            return LocalDate.now().minusMonths(1);
        }
        String[] parts = text.split(" ");
        int number = Integer.parseInt(parts[0]);
        String dayName = parts[1];
        return switch (dayName) {
            case "сейчас", "минуту", "минуты", "минут", "час", "часа", "часов" -> LocalDate.now();
            case "день", "дня", "дней" -> LocalDate.now().minusDays(number);
            case "неделя", "недели", "неделю" -> LocalDate.now().minusWeeks(number);
            case "месяц", "месяца" -> LocalDate.now().minusMonths(number);
            default -> LocalDate.now().minusMonths(1);
        };
    }

    public static LocalDate getByDayAndMonth(String text) {
        text = text.replaceAll("сьогодні", "0 сегодня").replaceAll("вч[ое]ра", "1 сегодня");
        if (isEmpty(text) || !text.contains(" ")) {
            return LocalDate.now().minusMonths(1);
        }
        String[] parts = text.split(" ");
        int number = Integer.parseInt(parts[0]);
        String monthName = parts[1];
        return switch (monthName) {
            case "сегодня" -> LocalDate.now().minusDays(number);
            case "Jan", "січня", "января" -> LocalDate.of(getYear(monthName), 1, number);
            case "Feb", "лютого", "февраля" -> LocalDate.of(getYear(monthName), 2, number);
            case "Mar", "березня", "марта" -> LocalDate.of(getYear(monthName), 3, number);
            case "Apr", "квітня", "апреля" -> LocalDate.of(getYear(monthName), 4, number);
            case "May", "травня", "мая" -> LocalDate.of(now().getYear(), 5, number);
            case "Jun", "червня", "июня" -> LocalDate.of(now().getYear(), 6, number);
            case "Jul", "липня", "июля" -> LocalDate.of(now().getYear(), 7, number);
            case "Aug", "серпня", "августа" -> LocalDate.of(now().getYear(), 8, number);
            case "Sep", "вересня", "сентября" -> LocalDate.of(now().getYear(), 9, number);
            case "Oct", "жовтня", "октября" -> LocalDate.of(now().getYear(), 10, number);
            case "Nov", "листопада", "ноября" -> LocalDate.of(now().getYear(), 11, number);
            case "Dec", "грудня", "декабря" -> LocalDate.of(now().getYear(), 12, number);
            default -> LocalDate.now().minusMonths(1);
        };
    }

    public static int getYear(String text) {
        return now().getMonth().toString().equals("JANUARY") && isEquals(text, of("сентября", "октября", "ноября", "декабря"))
                || now().getMonth().toString().equals("FEBRUARY") && isEquals(text, of("октября", "ноября", "декабря"))
                || now().getMonth().toString().equals("MARCH") && isEquals(text, of("ноября", "декабря"))
                || now().getMonth().toString().equals("APRIL") && text.contains("декабря")
                ? now().minusYears(1).getYear() : now().getYear();
    }

    public static String getBruteCleaning(String text) {
        Matcher m = Pattern.compile("(?:\\d){1,2}\\s([а-я])+|^[а-яіїє]{3,10}", Pattern.CASE_INSENSITIVE).matcher(text);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list.size() > 0 ? list.get(0) : text;
    }
}
