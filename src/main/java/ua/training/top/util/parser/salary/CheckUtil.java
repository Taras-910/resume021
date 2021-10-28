package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CheckUtil {
    public static final Logger log = LoggerFactory.getLogger(CheckUtil.class);

    public static boolean checkSalary(String salary) {
        return (salary.contains("грн") || salary.contains("$") || salary.contains("usd") || salary.contains("eur")
                || salary.contains("€") || salary.contains("pln")|| salary.contains("uah")
                || salary.contains("gbp") || salary.contains("£") || salary.contains("₤")
                || salary.contains("salary:")) && salary.matches(".*?\\d.*\\n?");
    }

    public static String validateAndFormat(String salary) {
        salary = salary.toLowerCase();
        if (salary.isEmpty() || salary.matches("\\D*\\d\\D+") || !checkSalary(salary) || salary.contains("unpaid")) {
            return "1";
        }
        salary = salary.contains("от") && salary.contains("до") ?
                salary.replaceAll("до", "—").replaceAll("от","") : salary;
        salary = salary.replaceAll(" ", "").replaceAll(" ", "")
                .replaceAll("&nbsp;", "").replaceAll("b2b", "")
                .replaceAll("\\(uop\\)", "").replaceAll("[.]{2,}", "");
        salary = salary.replaceAll("–", "—").replaceAll("-", "—");
        salary = salary.contains("salary:") ? "salary:".concat(salary.split("salary:")[1]) : salary;
        salary = salary.contains("requirements:") ? salary.split("requirements:")[0] : salary;
        salary = salary.contains(",") ? salary.split(",")[0] : salary;
        salary = salary.contains("·") ? salary.split("·")[0] : salary;
        salary = salary.contains("·") ? salary.split("!")[0] : salary;

        if(salary.matches(".*?\\.\\d?\\dk.*?")){
            String temp1 = salary.replaceAll("[A-Za-jl-zа-я ·:\\/(),]","");
            List<String> list = Arrays.stream(temp1.split("—"))
                    .filter(s -> s.matches("\\d+\\.\\dk.*"))
                    .collect(Collectors.toList());
            for(String find : list) {
                find = find.substring(0, find.indexOf("k"));
                String newString = String.valueOf((int) (Float.parseFloat(find) * 1000));
                salary = salary.replace(find, newString).trim();
            }
        }
        return salary;
    }

    public static String getCleaned(String salary) {
        return salary.replaceAll("[^\\d+-]+—[^\\d+ ]+", "").replaceAll("[^—\\d]", "");
    }
}
