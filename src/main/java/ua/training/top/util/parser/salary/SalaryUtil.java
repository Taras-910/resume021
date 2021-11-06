package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.parser.data.CommonDataUtil.*;
import static ua.training.top.util.parser.salary.CommonSalaryUtil.*;
import static ua.training.top.util.parser.salary.FormatUtil.validateAndFormat;
import static ua.training.top.util.parser.salary.MoneyTypeUtil.*;

public class SalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryUtil.class);

    public static Integer getToSalary(String salary) {
        salary = getStringSalary(salary);
        String[] parts = salary.split("—");
        int result = 1;
        try {
            result = isEmpty(salary) ? result : salary.matches("\\d+\\W\\d+") ?
                    Integer.parseInt(salary.split("\\W")[parts[0].equals("1") ? 1 : 0]) :
                    parts[0].equals("1") ? result : Integer.parseInt(salary);
        } catch (NumberFormatException e) {
            log.error(error, e.getMessage(), salary);
        }
        return checkExceedResult(result, salary) ? 1 : result;
    }

    public static String getStringSalary(String salary) {
        String temp = validateAndFormat(salary);
        try {
            String moneyName = getMoneyName(temp);
            return switch (moneyName) {
                case hrn, rub, kzt, byn -> getHrnRub(temp, moneyName);
                case eur, gbp -> getEurGbp(temp, moneyName);
                case pln -> getPln(temp, moneyName);
                case usd -> getUsd(temp, moneyName);
                default -> "1"; };
        } catch (NumberFormatException e) {
            log.error(salary_error, temp);
            return "1";
        }
    }

    public static boolean checkExceedResult(Integer result, String salary) {
        boolean checkResult = result >= 10000000 || result < 1;
        if (checkResult) {
            log.info(error, result, salary);
        }
        return checkResult;
    }

    public static String getToSalaryRabota(String salary) {
        salary = salary.replaceAll("год", " ").replaceAll("лет", " ");
        return salary.contains(" ") ? salary.substring(salary.indexOf(" ")) : salary;
    }


}
