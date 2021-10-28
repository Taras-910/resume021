package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.parser.salary.CheckUtil.getCleaned;
import static ua.training.top.util.parser.salary.RateUtil.hrnToUsd;

public class HrnUtil {
    public static final Logger log = LoggerFactory.getLogger(HrnUtil.class);

    public static String getHrn(String temp){
        String cleaned = getCleaned(temp.substring(0, temp.indexOf(temp.contains("uah") ? "k uah" : "грн"))) ;
        if(temp.contains("—")){
            if(cleaned.length() < 4) return "1";
            else return hrnToUsd(cleaned.split("\\W")[0]).concat("—").concat(hrnToUsd(cleaned.split("\\W")[1]));
        }
        else {
            cleaned = hrnToUsd(cleaned);
            return temp.contains("от") ? cleaned.concat("—").concat("1") : "1".concat("—").concat(cleaned);
        }
    }
}
