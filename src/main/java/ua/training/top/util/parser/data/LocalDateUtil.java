package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.LocalDate.now;
import static java.util.List.of;
import static ua.training.top.util.parser.data.CommonDataUtil.*;

public class LocalDateUtil {
    private final static Logger log = LoggerFactory.getLogger(LocalDateUtil.class);

    public static LocalDate getToLocalDate(String originText) {
        String preText = oneToNumbAndWord(originText);
        if (isEmpty(preText) || !preText.contains(" ") && !isRuleDate(preText)) {
            return LocalDate.now().minusMonths(1);
        }
        try {
            String text = getBruteCleaning(preText);
            int number = text.contains(" ") ? Integer.parseInt(text.split(" ")[0]) : 1;
            String name = text.contains(" ") ? text.split(" ")[1] : "";
            return isRuleMonth(text) ? getByDayAndMonth(name, number) :
                    isRuleAgo(text) ? getByNumberAgo(name, number) : isRuleDate(text) ?
                    LocalDate.parse(text.contains("T") ? text.substring(0, text.indexOf("T")) : text) : now().minusMonths(1);
        } catch (Exception e) {
            log.error(error, e.getMessage(), originText);
            return now().minusMonths(1);
        }
    }

    public static LocalDate getByNumberAgo(String periodName, int number) {
        return switch (periodName) {
            case "сейчас"  -> LocalDate.now();
            case "минуту", "минуты", "минут" -> LocalDateTime.now().minusMinutes(number).toLocalDate();
            case "час", "часа", "часов" -> LocalDateTime.now().minusHours(number).toLocalDate();
            case "день", "дня", "дней" -> LocalDate.now().minusDays(number);
            case "неделя", "недели", "неделю" -> LocalDate.now().minusWeeks(number);
            case "месяц", "месяца" -> LocalDate.now().minusMonths(number);
            default -> LocalDate.now().minusMonths(1);
        };
    }

    public static LocalDate getByDayAndMonth(String monthName, int number) {
        return switch (monthName) {
            case "сьогодні", "сегодня" -> LocalDate.now().minusDays(number);
            case "jan", "січня", "января" -> LocalDate.of(getYear(monthName), 1, number);
            case "feb", "лютого", "февраля" -> LocalDate.of(getYear(monthName), 2, number);
            case "mar", "березня", "марта" -> LocalDate.of(getYear(monthName), 3, number);
            case "apr", "квітня", "апреля" -> LocalDate.of(getYear(monthName), 4, number);
            case "may", "травня", "мая" -> LocalDate.of(now().getYear(), 5, number);
            case "jun", "червня", "июня" -> LocalDate.of(now().getYear(), 6, number);
            case "jul", "липня", "июля" -> LocalDate.of(now().getYear(), 7, number);
            case "aug", "серпня", "августа" -> LocalDate.of(now().getYear(), 8, number);
            case "sep", "вересня", "сентября" -> LocalDate.of(now().getYear(), 9, number);
            case "oct", "жовтня", "октября" -> LocalDate.of(now().getYear(), 10, number);
            case "nov", "листопада", "ноября" -> LocalDate.of(now().getYear(), 11, number);
            case "dec", "грудня", "декабря" -> LocalDate.of(now().getYear(), 12, number);
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
        Matcher m = Pattern.compile(regex_number_and_word, Pattern.CASE_INSENSITIVE).matcher(text);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list.size() > 0 ? list.get(0) : text;
    }

    private static String oneToNumbAndWord(String originText) {
        return originText.replaceAll("сьогодні", "0 сьогодні")
                .replaceAll("вчора", "1 сьогодні").replaceAll("вчера", "1 день");
    }
}
