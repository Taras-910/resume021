package ua.training.top.util.parser.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;
import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static ua.training.top.util.DateTimeUtil.print;
import static ua.training.top.util.parser.data.CommonDataUtil.*;
import static ua.training.top.util.parser.date.CommonDateUtil.*;
import static ua.training.top.util.xss.XssUtil.xssClear;

public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    public static String builderDateString(String dateTime) {
        if (isEmpty(dateTime)) {
            return now().minusWeeks(1).toString();
        }
        if (dateTime.split(" ").length < 2) {
            return print(getLocalDate(dateTime));
        }
        dateTime = dateTime.split(" ").length < 3 ? addYear(dateTime) : dateTime;
        String[] dateParts = dateTime.split(" ");
        String partDay = dateParts[1].matches("\\d+") ? dateParts[1] : dateParts[0];
        String partMonth = dateParts[1].matches("\\d+") ? dateParts[0] : dateParts[1];
        StringBuilder sb = new StringBuilder(dateParts[2]);
        sb.append("-").append(getMonth(partMonth)).append("-");
        return sb.append(partDay.length() == 2 ? partDay : "0" .concat(partDay)).toString();
    }

    public static LocalDate parseStringToLocalDate(String dateTime) {
        try {
            return LocalDate.parse(dateTime.contains("T") ? dateTime.substring(0, dateTime.indexOf("T")) : dateTime);
        } catch (Exception e) {
            return getDefaultDateAndMessage(e, dateTime);
        }
    }

    public static LocalDate getLocalDate(String dateTime) {
        if (isEmpty(dateTime)) {
            return now().minusDays(7);
        }
        try {
            dateTime = getReplace(dateTime.toLowerCase(), wasteDate, "");
            return isYesterday(dateTime) ? now().minusDays(1) : isMinuteAgo(dateTime) || isToday(dateTime) ?
                    now() : isHour(dateTime) ?
                    LocalDateTime.now().minusHours(getInt(dateTime)).toLocalDate() : isWeek(dateTime) ?
                    LocalDateTime.now().minusDays(getInt(dateTime) * 7L).toLocalDate() : isMonth(dateTime) ?
                    now().minusMonths(parseInt(dateTime.substring(0, 2).trim())).minusDays(1) : isDay(dateTime) ?
                    now().minusDays(parseInt(dateTime.trim().substring(0, 2).trim())) : !dateTime.matches(".*\\d.*") ?
                    now().minusDays(7) : parse(builderDateString(xssClear(dateTime)));
        } catch (NumberFormatException e) {
            return getDefaultDateAndMessage(e, dateTime);
        }
    }

    private static LocalDate getDefaultDateAndMessage(Exception e, String dateTime) {
        log.error(error, e.getMessage(), dateTime);
        return now().minusDays(7);
    }

    public static int getInt(String myDate) {
        return parseInt(myDate.replaceAll("\\W+", "").trim());
    }

    public static String getToDateGrc(String text) {
        String[] parts = text.split(" ");
        text = text.replaceAll("Обновлено ", "  ");
        String substringEnd = "";
        for (String p : parts) {
            if (p.contains(":")) {
                substringEnd = p;
                break;
            }
        }
        return text.substring(text.indexOf(" ") + 1, text.indexOf(substringEnd)).trim();
    }
}
