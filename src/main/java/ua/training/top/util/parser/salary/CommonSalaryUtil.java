package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.lang.String.valueOf;
import static java.util.List.of;

public class CommonSalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(CommonSalaryUtil.class);
    public static final String examples = "₳ ₢ ₰ ₯ ₠ ₤ Lm ₶ ₥ ₧ ₷";
    public static final float
            rate_pln_to_usd = 3.98f,
            rate_eur_to_usd = 0.86f,
            rate_gbp_to_usd = 0.73f,
            rate_byn_to_usd = 2.43f,
            rate_hrn_to_usd = 26.25f,
            rate_rub_to_usd = 71.78f,
            rate_kzt_to_usd = 426.74f,
            usd_one_to_one = 1.0f;

    public static final String
            hrn = "hrn", eur = "eur", gbp = "gbp", pln = "pln", rub = "rub", byn = "byn",
            kzt = "kzt", usd = "usd", year = "year", hour = "hour", month = "month", day = "day";

    public static final List<String>
            salaryUsd = of("usd", "$"),
            salaryEur = of("eur", "€"),
            salaryGbr = of("gbp", "£", "₤"),
            salaryKzt = of("kzt", "тг", "₸"),
            salaryByn = of("br", "byn", "byr"),
            salaryHrn = of("hrn", "uah", "грн", "₴"),
            salaryRub = of("rub", "rur", "руб", "₽"),
            salaryPln = of("pln", "salary:", "(uop)", "zł"),
            allSalaries = of("грн", "uah", "hrn", "₴", "$", "usd", "eur", "€",
                    "pln", "zł", "gbp", "£", "₤", "руб", "₽", "kzt", "тг", "₸", "br", "byn"),
            wasteSalary = of(" ", " ", "&nbsp;", "b2b", "\\(uop\\)", "[.]{2,}"),
            unsuitableSigns = of("–", "-");

    public static Float getRate(String moneyName) {
        return switch (moneyName) {
            case hrn -> rate_hrn_to_usd;
            case pln -> rate_pln_to_usd;
            case eur -> rate_eur_to_usd;
            case gbp -> rate_gbp_to_usd;
            case rub -> rate_rub_to_usd;
            case kzt -> rate_kzt_to_usd;
            case byn -> rate_byn_to_usd;
            default -> usd_one_to_one;
        };
    }

    public static String getPeriod(String temp) {
        return temp.contains(year) ? year : temp.contains(month) ? month : temp.contains(day) ? day :
                temp.contains(hour) ? hour : month;
    }

    public static String getMoneyName(String temp) {
        return isUsd(temp) ? usd : isPln(temp) ? pln : isEur(temp) ? eur : isGbr(temp) ? gbp :
                isRub(temp) ? rub : isKzt(temp) ? kzt : isSByn(temp) ? byn : isHrn(temp) ? hrn : "";
    }

    public static String getValidSalary(int value) {
        return value > 40000000 ? valueOf(value / 100) : valueOf(value);
    }

    public static String toUsd(String value, String moneyName) throws NumberFormatException {
        return getValidSalary((int) ((Float.parseFloat(value) / getRate(moneyName)) * 100));
    }

    public static boolean isHrn(String text) {
        return salaryHrn.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isGbr(String text) {
        return salaryGbr.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isPln(String text) {
        return salaryPln.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isUsd(String text) {
        return salaryUsd.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isEur(String text) {
        return salaryEur.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isRub(String text) {
        return salaryRub.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isKzt(String text) {
        return salaryKzt.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isSByn(String text) {
        return salaryByn.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isSalary(String salary) {
        return (allSalaries.stream().anyMatch(salary::contains) ||
                salary.contains("salary:")) && salary.matches(".*?\\d.*\\n?");
    }
}
