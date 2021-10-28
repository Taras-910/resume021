package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.parser.salary.CheckUtil.getCleaned;
import static ua.training.top.util.parser.salary.RateUtil.eurToUsd;

public class EurUtil {
    public static final Logger log = LoggerFactory.getLogger(EurUtil.class);

    public static String getEur(String temp){
        String cleaned = getCleaned(temp.substring(0, temp.contains("€") ? temp.indexOf("€") : temp.indexOf("eur")));
        if(temp.contains("—")){
            if(cleaned.length() < 4) return "1";
            if (temp.contains("year")) {
                cleaned = String.valueOf(Integer.parseInt(cleaned.split("\\W")[0]) / 12).concat("-")
                        .concat(String.valueOf(Integer.parseInt(cleaned.split("\\W")[1]) / 12));
            }
            return eurToUsd(cleaned.split("\\W")[0]).concat("—").concat(eurToUsd(cleaned.split("\\W")[1]));
        } else{
            return temp.contains("от") ? eurToUsd(cleaned).concat("—").concat("1") : "1".concat("—").concat(eurToUsd(cleaned));
        }
    }
}
