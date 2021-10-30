package ua.training.top.util.parser.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static ua.training.top.util.DateTimeUtil.print;
import static ua.training.top.util.parser.data.CommonUtil.isEmpty;
import static ua.training.top.util.parser.date.MonthUtil.getMonth;
import static ua.training.top.util.parser.date.ToCorrectDate.getCorrectDate;

public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static String getCurrentYear() { return new SimpleDateFormat(DATE_PATTERN).format(new Date()).substring(0,4); }

    public static String prepareGrc(String text){
        String[] parts = text.split(" ");
        text = text.replaceAll("Обновлено ", "  ");
        String substringEnd = "";
        for(String p: parts){
            if(p.contains(":")){
                substringEnd = p;
                break;
            }
        }
        return text.substring(text.indexOf(" ") + 1 , text.indexOf(substringEnd)).trim();
    }

    public static String supportDate(String dateTime){
        if (isEmpty(dateTime)) {
            return LocalDate.now().minusWeeks(1).toString();
        }
        if (dateTime.split(" ").length < 2) {
            return print(getCorrectDate(dateTime));
        }
        dateTime = dateTime.split(" ").length < 3 ? toAddYear(dateTime) : dateTime;
        String[] dateParts = dateTime.split(" ");
        String partDay = dateParts[1].matches("\\d+") ? dateParts[1] : dateParts[0];
        String partMonth = dateParts[1].matches("\\d+") ? dateParts[0] : dateParts[1];

        StringBuilder sb = new StringBuilder(dateParts[2]);
        sb.append("-").append(getMonth(partMonth)).append("-");
        return sb.append(partDay.length() == 2 ? partDay : "0" .concat(partDay)).toString();
    }

    private static String toAddYear(String dateTime) {
        return LocalDate.now().getMonth().toString().equals("JANUARY") && dateTime.contains("декабря") ?
                dateTime.concat(" ").concat(String.valueOf(LocalDate.now().minusYears(1).getYear())) :
                dateTime.concat(" ").concat(getCurrentYear());
    }

    public static LocalDate parseCustom(String dateTime) {
        dateTime = dateTime.contains("T") ? dateTime.substring(0, dateTime.indexOf("T")) : dateTime;
        try {
            return LocalDate.parse(dateTime);
        } catch (Exception e) {
            log.error("there is error for parse {}", dateTime);
            return LocalDate.now().minusDays(7);
        }
    }
}
