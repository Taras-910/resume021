package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Float.parseFloat;
import static ua.training.top.aggregator.installation.Installation.*;
import static ua.training.top.util.parser.data.DataUtil.*;

public class SalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryUtil.class);
    public static final Pattern patternMonetaryAmount =
            Pattern.compile("((?:[\\d,\\.\\s  &nbsp]+\\b)(\\s*)?(\\p{Sc}|ƒ))|((?:\\p{Sc}|ƒ)(\\s*)?[\\d,\\.\\s  &nbsp]+\\b)");

    public static int getToSalary(String originText) {
        originText = originText.replaceAll(",", ".").toLowerCase();
        String currencyCode = getCurrencyCode(originText);
        String amount = getMonetaryAmount(originText, currencyCode);
        if (!isEmpty(originText) && !isEmpty(amount) && isSalary(originText)) {
            try {
                return (int) (parseFloat(amount) * getPeriod(originText) / getRate(currencyCode) * 100);
            } catch (NumberFormatException e) {
                log.error(error, e, amount);
            }
        }
        return 1;
    }

    public static String getCurrencyCode(String originText) {
        return isUsd(originText) ? usd : isHrn(originText) ? hrn : isEur(originText) ? eur : isRub(originText) ? rub :
                isPln(originText) ? pln : isGbr(originText) ? gbp : isKzt(originText) ? kzt : isSByn(originText) ? byn : "";
    }

    public static String getMonetaryAmount(String originText, String currencyCode) {
        List<String> parts = new ArrayList<>();
        Matcher matcher = patternMonetaryAmount.matcher(getReplacementText(originText, currencyCode));
        while (matcher.find()) {
            parts.add(matcher.group());
        }
        String monetaryAmount = parts.stream().filter(p -> p.contains(getBadge(currencyCode))).findFirst().orElse("");
        return getReplace(monetaryAmount, wasteSalary, "");
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
}
