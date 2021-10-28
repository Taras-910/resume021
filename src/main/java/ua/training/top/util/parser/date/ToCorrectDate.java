package ua.training.top.util.parser.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;
import static java.time.LocalDate.parse;
import static ua.training.top.util.parser.date.DateUtil.supportDate;
import static ua.training.top.util.xss.XssUtil.xssClear;

public class ToCorrectDate {
    private static final Logger log = LoggerFactory.getLogger(ToCorrectDate.class);

    public static LocalDate getCorrectDate(String myDate){
        if(myDate == null || myDate.isEmpty()){
            return LocalDate.now().minusDays(7);
        }
        myDate = myDate.toLowerCase().trim();
        myDate = myDate.replaceAll("обновлено ", "");
        try {
            if(myDate.length() > 1 && (myDate.contains("вчера") || myDate.contains("вчора"))){
                return LocalDate.now().minusDays(1);
            }
            if((myDate.contains("мин") || myDate.contains("хв")) && myDate.matches(".*\\d.*")|| myDate.contains("сьогодні")
                    || myDate.contains("сегодня") || myDate.contains("только что")){
                return LocalDate.now();
            }
            if(myDate.contains("ч") && myDate.matches(".*\\d.*") || myDate.contains("час") || myDate.contains("год")){
                return LocalDateTime.now().minusHours(getParseInt(myDate)).toLocalDate();
            }
            if((myDate.contains("недел") || myDate.contains("тиж")) && myDate.matches(".*\\d.*")){
                return LocalDateTime.now().minusDays(getParseInt(myDate) * 7).toLocalDate();
            }
            if(myDate.length() > 1 && (myDate.contains("місяц") || myDate.contains("месяц"))){
                myDate = myDate.contains("більше") ? myDate.replace("більше", "").trim() : myDate;
                return LocalDate.now().minusMonths(parseInt(myDate.substring(0, 2).trim())).minusDays(1);
            }
            if(myDate.length() > 1 && (myDate.contains("день") || myDate.contains("дн") && myDate.matches(".*\\d.*"))){
                return LocalDate.now().minusDays(parseInt(myDate.trim().substring(0, 2).trim()));
            }
            if(!myDate.matches(".*\\d.*")){
                return LocalDate.now().minusDays(7);
            }
            return parse(supportDate(xssClear(myDate)));
        } catch (NumberFormatException e) {
            log.error("Wrong data: \n{}\nException: {}\n", myDate, e.getMessage());
            return LocalDate.now().minusDays(7);
        }
    }

    private static int getParseInt(String myDate) {
        return parseInt(myDate.replaceAll("\\W+", "").trim());
    }
}
