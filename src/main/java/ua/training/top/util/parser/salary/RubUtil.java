package ua.training.top.util.parser.salary;

import static ua.training.top.util.parser.salary.CheckUtil.getCleaned;
import static ua.training.top.util.parser.salary.RateUtil.rubToUsd;

public class RubUtil {

    public static final String getRub(String temp) {
        String cleaned = getCleaned(temp);
        if(temp.contains("—")){
            if(cleaned.length() < 4) return "1";
            else return rubToUsd(cleaned.split("\\W")[0]).concat("—").concat(rubToUsd(cleaned.split("\\W")[1]));
        }
        else {
            cleaned = rubToUsd(cleaned);
            return temp.contains("от") ? cleaned.concat("—").concat("1") : "1".concat("—").concat(cleaned);
        }
    }
}
