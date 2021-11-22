package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static ua.training.top.util.AggregatorUtil.getMatch;
import static ua.training.top.util.parser.data.DataUtil.*;

public class ReleaseDateUtil {
    private final static Logger log = LoggerFactory.getLogger(ReleaseDateUtil.class);

    public static LocalDate getToLocalDate(String originText) {
        if (isEmpty(originText)) {
            return defaultDate;
        }
        String preText = formatToNumAndWord(originText);
        String text = getMatch(local_date_extract, preText);
        if (isEmpty(preText) || !text.contains(" ")&& !isNumberFormat(preText)) {
            return defaultDate;
        }
        try {
            if (!isNumberFormat(preText)) {
                int number = Integer.parseInt(text.split(" ")[0]);
                String name = text.split(" ")[1];
                LocalDate localDate = getLocalDate(number, name);
                return localDate.isBefore(now()) ? localDate : localDate.minusYears(1);
            }
            return parse(text.contains("T") ? text.substring(0, text.indexOf("T")) : text);
        } catch (Exception e) {
            log.error(error, e.getMessage(), originText);
            return defaultDate;
        }
    }

    static LocalDate getLocalDate(int number, String name) {
        return isMonth(name) ? LocalDate.of(now().getYear(), getMonth(name), number) :
                switch (name) {
                    case "сейчас", "минуту", "минуты", "минут" -> LocalDateTime.now().minusMinutes(number).toLocalDate();
                    case "час", "часа", "часов" -> LocalDateTime.now().minusHours(number).toLocalDate();
                    case "день", "дня", "дней", "сьогодні", "сегодня" -> now().minusDays(number);
                    case "неделя", "недели", "неделю" -> now().minusWeeks(number);
                    case "месяц", "месяца" -> now().minusMonths(number);
                    default -> defaultDate;
                };
    }

    static String formatToNumAndWord(String originText) {
        return originText.replaceAll("сейчас", "0 минут").replaceAll("только что", "0 минут")
                .replaceAll("сьогодні", "0 сьогодні").replaceAll("сегодня", "0 сегодня")
                .replaceAll("вчора", "1 сьогодні").replaceAll("вчера", "1 день");
    }

    private static int getMonth(String name) {
        return switch (name) {
            case "лютого", "февраля" -> 2;
            case "березня", "марта" -> 3;
            case "квітня", "апреля" -> 4;
            case "травня", "мая" -> 5;
            case "червня", "июня" -> 6;
            case "липня", "июля" -> 7;
            case "серпня", "августа" -> 8;
            case "вересня", "сентября" -> 9;
            case "жовтня", "октября" -> 10;
            case "листопада", "ноября" -> 11;
            case "грудня", "декабря" -> 12;
            default -> 1;
        };
    }
}

