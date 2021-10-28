package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.parser.salary.RateUtil.plnToUsd;

public class PlnUtil {
    public static final Logger log = LoggerFactory.getLogger(PlnUtil.class);

    public static String getPln(String salary) {
        String temp = salary.replaceAll("–", "—");
        try {
            String cleaned = temp.replaceAll("[^—\\d]", "");
            if(cleaned.matches("\\d+\\W\\d+")){
                if(cleaned.length() < 4) return "1";
                if(salary.contains("year")) {
                    return plnToUsd(String.valueOf((int)(Float.parseFloat(cleaned.split("\\W")[0]) / 12)))
                            .concat("—").concat(plnToUsd(String.valueOf((int)(Float.parseFloat(cleaned.split("—")[1]) / 12))));
                }
                if(salary.contains("month")) {
                    String result = plnToUsd(cleaned.split("\\W")[0]).concat("—").concat(plnToUsd(cleaned.split("—")[1]));
                    return result;
                }
                if(salary.contains("day")) {
                    return plnToUsd(getString(cleaned.split("\\W")[0], 22)).concat("—")
                            .concat(plnToUsd(getString(cleaned.split("\\W")[1], 22)));
                }
                if(salary.contains("hour")) {
                    return plnToUsd(getString(cleaned.split("\\W")[0], 8 * 22)).concat("—")
                            .concat(plnToUsd(getString(cleaned.split("\\W")[1], 8 * 22)));
                }
                return plnToUsd(cleaned.split("\\W")[0]).concat("—").concat(plnToUsd(cleaned.split("—")[1]));
            } else{
                if(salary.contains("year")){
                    return  "1".concat("—").concat(plnToUsd(String.valueOf((int)(Float.parseFloat(cleaned) / 12))));
                }
                if(salary.contains("month")){
                    return  "1".concat("—").concat(plnToUsd(cleaned));
                }
                if(salary.contains("hour")){
                    return "1".concat("—").concat(plnToUsd(String.valueOf((int)(Float.parseFloat(cleaned) * 8 * 22))));
                }
                if(salary.contains("day")){
                    return  "1".concat("—").concat(plnToUsd(String.valueOf((int)(Float.parseFloat(cleaned) * 22))));
                }
                return  "1".concat("—").concat(plnToUsd(cleaned));
            }
        } catch (NumberFormatException e) {
            log.error("there is exception on getCorrectSalary method during parse line:\n{}\n", salary);
        }
        return salary;
    }

    private static String getString(String s, int days) {
        return String.valueOf((int)(Float.parseFloat(s) * days));
    }
}
