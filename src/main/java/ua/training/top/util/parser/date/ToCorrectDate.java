package ua.training.top.util.parser.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;
import static java.time.LocalDate.parse;
import static ua.training.top.util.parser.data.CommonUtil.isEmpty;
import static ua.training.top.util.parser.date.DateUtil.supportDate;
import static ua.training.top.util.xss.XssUtil.xssClear;

public class ToCorrectDate {
    private static final Logger log = LoggerFactory.getLogger(ToCorrectDate.class);

    public static LocalDate getCorrectDate(String myDate) {
        if (isEmpty(myDate)) {
            return LocalDate.now().minusDays(7);
        }
        myDate = myDate.toLowerCase().replace("обновлено ", "").replace("більше", "").trim();
        try {
            return myDate.length() > 1 && (myDate.contains("вчера") || myDate.contains("вчора")) ?
                    LocalDate.now().minusDays(1) :
                    (myDate.contains("мин") || myDate.contains("хв")) && myDate.matches(".*\\d.*") || myDate.contains("сьогодні")
                            || myDate.contains("сегодня") || myDate.contains("только что") ?
                            LocalDate.now() :
                            myDate.contains("ч") && myDate.matches(".*\\d.*") || myDate.contains("час") || myDate.contains("год") ?
                                    LocalDateTime.now().minusHours(getParseInt(myDate)).toLocalDate() :
                                    (myDate.contains("недел") || myDate.contains("тиж")) && myDate.matches(".*\\d.*") ?
                                            LocalDateTime.now().minusDays(getParseInt(myDate) * 7).toLocalDate() :
                                            myDate.length() > 1 && (myDate.contains("місяц") || myDate.contains("месяц")) ?
                                                    LocalDate.now().minusMonths(parseInt(myDate.substring(0, 2).trim())).minusDays(1) :
                                                    myDate.length() > 1 && (myDate.contains("день") || myDate.contains("дн") && myDate.matches(".*\\d.*")) ?
                                                            LocalDate.now().minusDays(parseInt(myDate.trim().substring(0, 2).trim())) :
                                                            !myDate.matches(".*\\d.*") ? LocalDate.now().minusDays(7) : parse(supportDate(xssClear(myDate)));
        } catch (NumberFormatException e) {
            log.error("Wrong data: \n{}\nException: {}\n", myDate, e.getMessage());
            return LocalDate.now().minusDays(7);
        }
    }

    private static int getParseInt(String myDate) { return parseInt(myDate.replaceAll("\\W+", "").trim()); }
}
