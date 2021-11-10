package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.List.of;
import static ua.training.top.util.parser.data.DataUtil.getReplace;

public class CommonSalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(CommonSalaryUtil.class);
    public static final Pattern patternMonetaryAmount =
            Pattern.compile("((?:[\\d,\\.\\s  &nbsp]+\\b)(\\s*)?(\\p{Sc}|ƒ))|((?:\\p{Sc}|ƒ)(\\s*)?[\\d,\\.\\s  &nbsp]+\\b)");

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
            salaryHrn = of("hrn", "uah", "грн.", "грн", "₴"),
            salaryRub = of("rub", "rur", "руб.", "руб", "₽"),
            salaryPln = of("pln", "salary:", "(uop)", "zł"),
            allSalaries = of("грн", "uah", "hrn", "₴", "$", "usd", "eur", "€", "pln",
                    "zł", "gbp", "£", "₤", "руб", "₽", "kzt", "тг", "₸", "br", "byn"),
            wasteSalary = of(" ", " ", "&nbsp;", "[.]{2,}", "(\\p{Sc}|ƒ)", "\\s+");

    public static String getBadge(String moneyName) {
        return switch (moneyName) {
            case usd -> "$";
            case hrn -> "₴";
            case eur -> "€";
            case rub -> "₽";
            case gbp -> "£";
            case pln -> "₧";
            case kzt -> "₸";
            default -> "฿";
        };
    }

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

    public static String getReplacementText(String text, String moneyName) {
        return switch (moneyName) {
            case usd -> getReplace(text, salaryUsd, "\\$");
            case hrn -> getReplace(text, salaryHrn, "₴");
            case eur -> getReplace(text, salaryEur, "€");
            case rub -> getReplace(text, salaryRub, "₽");
            case gbp -> getReplace(text, salaryGbr, "£");
            case pln -> getReplace(text, salaryPln, "₧");
            case kzt -> getReplace(text, salaryKzt, "₸");
            case byn -> getReplace(text, salaryByn, "฿");
            default -> text;
        };
    }

    public static int getPeriod(String text) {
        return text.contains(year) ?
                1/12 : text.contains(month) ? 1 : text.contains(day) ? 22 : text.contains(hour) ? 8 * 22 : 1;
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

    public static boolean isUsd(String text) { return salaryUsd.stream().anyMatch(text.toLowerCase()::contains); }

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

    public static boolean isSalary(String salary) { return (allSalaries.stream().anyMatch(salary::contains)); }
}
