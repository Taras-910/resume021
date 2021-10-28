package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.parser.salary.CheckUtil.validateAndFormat;
import static ua.training.top.util.parser.salary.EurUtil.getEur;
import static ua.training.top.util.parser.salary.GbpUtil.getGbp;
import static ua.training.top.util.parser.salary.HrnUtil.getHrn;
import static ua.training.top.util.parser.salary.PlnUtil.getPln;
import static ua.training.top.util.parser.salary.RubUtil.getRub;
import static ua.training.top.util.parser.salary.UsdUtil.getUsd;

public class SalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryUtil.class);

    public static String getCorrectSalary(String salary){
        String temp = validateAndFormat(salary);
//        System.out.println("temp="+temp);
        try {
            if (temp.contains("$") || temp.contains("usd")) {
                return getUsd(temp);
            }
            else if ((temp.contains("salary:") || temp.contains("pln") || temp.contains("(uop)"))) {
                return getPln(temp);
            }
            else if (temp.contains("eur") || temp.contains("€")) {
                return getEur(temp);
            }
            else if (temp.contains("gbp") || temp.contains("£") || temp.contains("₤")) {
                return getGbp(temp);
            }
            else if (temp.contains("руб")) {
                return getRub(temp);
            }
            else if (temp.contains("грн")  || temp.contains("uah")) {
                return getHrn(temp);
            }
        } catch (NumberFormatException e) {
            log.error("Error: salary not contains at least one [salary:,pln,$,usd,eur,€,грн,gbp,£,₤,руб]={}", temp);
            return "1";
        }
        return temp;
    }
}
