package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDate.*;
import static ua.training.top.util.parser.data.DataUtil.*;

public class LocalDateUtil {
    private final static Logger log = LoggerFactory.getLogger(LocalDateUtil.class);

    public static LocalDate getToLocalDate(String originText) {
        String preText = formatOneToNumbAndWord(originText);
        String text = getMatch(local_date_field, preText);
        if (isEmpty(preText) || !preText.contains(" ") && !isDate(preText) || !text.contains(" ")) {
            return defaultDate;
        }
        try {
            int number = Integer.parseInt(text.split(" ")[0]);
            String name = text.split(" ")[1];
            return isDate(text) ?
                    parse(text.contains("T") ? text.substring(0, text.indexOf("T")) : text) : getLocalDate(number, name);
        } catch (Exception e) {
            log.error(error, e.getMessage(), originText);
            return defaultDate;
        }
    }

    private static LocalDate getLocalDate(int number, String name) {
        return switch (name) {
            case "сейчас", "минуту", "минуты", "минут" -> LocalDateTime.now().minusMinutes(number).toLocalDate();
            case "час", "часа", "часов" -> LocalDateTime.now().minusHours(number).toLocalDate();
            case "день", "дня", "дней", "сьогодні", "сегодня" -> now().minusDays(number);
            case "неделя", "недели", "неделю" -> now().minusWeeks(number);
            case "месяц", "месяца" -> now().minusMonths(number);
            case "jan", "січня", "января" -> of(getYear(name), 1, number);
            case "feb", "лютого", "февраля" -> of(getYear(name), 2, number);
            case "mar", "березня", "марта" -> of(getYear(name), 3, number);
            case "apr", "квітня", "апреля" -> of(getYear(name), 4, number);
            case "may", "травня", "мая" -> of(now().getYear(), 5, number);
            case "jun", "червня", "июня" -> of(now().getYear(), 6, number);
            case "jul", "липня", "июля" -> of(now().getYear(), 7, number);
            case "aug", "серпня", "августа" -> of(now().getYear(), 8, number);
            case "sep", "вересня", "сентября" -> of(now().getYear(), 9, number);
            case "oct", "жовтня", "октября" -> of(now().getYear(), 10, number);
            case "nov", "листопада", "ноября" -> of(now().getYear(), 11, number);
            case "dec", "грудня", "декабря" -> of(now().getYear(), 12, number);
            default -> defaultDate;
        };
    }

    private static int getYear(String text) {
        return now().getMonth().toString().equals("JANUARY") && isEquals(text, List.of("сентября", "октября", "ноября", "декабря"))
                || now().getMonth().toString().equals("FEBRUARY") && isEquals(text, List.of("октября", "ноября", "декабря"))
                || now().getMonth().toString().equals("MARCH") && isEquals(text, List.of("ноября", "декабря"))
                || now().getMonth().toString().equals("APRIL") && text.equalsIgnoreCase("декабря")
                ? now().minusYears(1).getYear() : now().getYear();
    }

    private static String formatOneToNumbAndWord(String originText) {
        return originText.replaceAll("сейчас", "0 минут")
                .replaceAll("сьогодні", "0 сьогодні").replaceAll("сегодня", "0 сегодня")
                .replaceAll("вчора", "1 сьогодні").replaceAll("вчера", "1 день");
    }
}
