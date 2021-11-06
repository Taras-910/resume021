package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static ua.training.top.util.parser.data.CommonDataUtil.getReplace;
import static ua.training.top.util.parser.data.CommonDataUtil.isEmpty;
import static ua.training.top.util.parser.date.CommonDateUtil.isContains;
import static ua.training.top.util.parser.salary.CommonSalaryUtil.*;

public class FormatUtil {
    public static final Logger log = LoggerFactory.getLogger(FormatUtil.class);

    public static String validateAndFormat(String salary) {
        salary = salary.toLowerCase();
        if (isEmpty(salary) || salary.matches("\\D*\\d\\D+") || !isSalary(salary) || salary.contains("unpaid")) {
            return "1";
        }
        salary = salary.contains("от") && salary.contains("до") ?
                salary.replaceAll("до", "—").replaceAll("от", "") : salary;
        salary = getReplace(getReplace(salary, wasteSalary, ""), unsuitableSigns, "—");
        salary = salary.contains("salary:") ?
                "salary:".concat(salary.split("salary:")[1]) : salary.contains("requirements:") ?
                salary.split("requirements:")[0] : salary.contains(",") ?
                salary.split(",")[0] : salary.contains("·") ? salary.split("·")[0] : salary.contains("·") ?
                salary.split("!")[0] : salary;
        if (salary.matches(".*?\\.\\d?\\dk.*?")) {
            String temp1 = salary.replaceAll("[A-Za-jl-zа-я ·:/(),]", "");
            List<String> list = Arrays.stream(temp1.split("—"))
                    .filter(s -> s.matches("\\d+\\.\\dk.*"))
                    .collect(Collectors.toList());
            for (String find : list) {
                find = find.substring(0, find.indexOf("k"));
                String newString = valueOf((int) (Float.parseFloat(find) * 1000));
                salary = salary.replace(find, newString).trim();
            }
        }
        return salary;
    }

    public static String getCleanedText(String temp, String moneyName) {
        return switch (moneyName) {
            case eur -> getCleaned(temp.substring(0, temp.contains("€") ? temp.indexOf("€") : temp.indexOf("eur")));
            case gbp -> getCleaned(temp.substring(0, temp.contains("£") ? temp.indexOf("£") : temp.contains("₤") ?
                    temp.indexOf("₤") : temp.indexOf("gbp")));
            case hrn -> getCleaned(temp.contains("k uah") || temp.contains("грн") ?
                    temp.substring(0, temp.indexOf(temp.contains("k uah") ? "k uah" : "грн")) : temp);
            case rub, kzt, byn -> getCleaned(temp);
            case pln -> temp.replaceAll("[^—\\d]", "");
            case usd -> correctIfYear(temp, getCleaned(temp.substring(0, temp.contains("$") ?
                    temp.indexOf("$") : temp.indexOf("usd"))));
            default -> temp;
        };
    }

    public static String getCleaned(String salary) {
        return salary.replaceAll("[^\\d+-]+—[^\\d+ ]+", "").replaceAll("[^—\\d]", "");
    }

    public static String correctIfYear(String temp, String cleaned) {
        return isContains(temp, List.of("рік", "год", "year")) ? (temp.contains("—") ?
                valueOf(parseInt(cleaned.split("\\W")[0]) / 12).concat("—")
                        .concat(valueOf(parseInt(cleaned.split("\\W")[1]) / 12)) :
                valueOf(parseInt(cleaned) / 12)) : cleaned;
    }
}
