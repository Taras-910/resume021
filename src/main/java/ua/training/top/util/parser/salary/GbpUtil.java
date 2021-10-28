package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.parser.salary.CheckUtil.getCleaned;
import static ua.training.top.util.parser.salary.RateUtil.gbpToUsd;

public class GbpUtil {
    public static final Logger log = LoggerFactory.getLogger(EurUtil.class);

    public static String getGbp(String temp){
            String cleaned = getCleaned(temp.substring(0, temp.contains("£") ? temp.indexOf("£") :
                    temp.contains("₤") ? temp.indexOf("₤") : temp.indexOf("gbp")));
        if(temp.contains("—")){
            if(cleaned.length() < 4) return "1";
            if (temp.contains("year")) {
                cleaned = String.valueOf(Integer.parseInt(cleaned.split("\\W")[0]) / 12).concat("-")
                        .concat(String.valueOf(Integer.parseInt(cleaned.split("\\W")[1]) / 12));
            }
            return gbpToUsd(cleaned.split("\\W")[0]).concat("—").concat(gbpToUsd(cleaned.split("\\W")[1]));
        } else{
            return temp.contains("от") ? gbpToUsd(cleaned).concat("—").concat("1") : "1".concat("—").concat(gbpToUsd(cleaned));
        }
    }
}
