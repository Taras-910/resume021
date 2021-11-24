package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Float.parseFloat;
import static ua.training.top.util.parser.data.DataUtil.*;

public class SalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryUtil.class);
    public static final float
            rate_pln_to_usd = 3.98f,
            rate_eur_to_usd = 0.86f,
            rate_gbp_to_usd = 0.73f,
            rate_byn_to_usd = 2.43f,
            rate_hrn_to_usd = 26.25f,
            rate_rub_to_usd = 71.78f,
            rate_kzt_to_usd = 426.74f,
            usd_one_to_one = 1.0f;

    public static int getToSalary(String originText) {
        if (isEmpty(originText) || !isContains(allSalaries, originText.toLowerCase())) {
            return 1;
        }
        originText = originText.replaceAll(",", ".").toLowerCase();
        String currencyCode = getCurrencyCode(originText);
        String amount = getAmountMonetary(originText, currencyCode).get(0);
        if (!isEmpty(originText) && !isEmpty(amount) && isContains(allSalaries, originText)) {
            try {
                return (int) (parseFloat(amount) * getPeriod(originText) / getRate(currencyCode) * 100);
            } catch (NumberFormatException e) {
                log.error(error, e, amount);
            }
        }
        return 1;
    }

    public static String getCurrencyCode(String text) {
        return isContains(ariaUsd, text) ? usd : isContains(ariaHrn, text) ? hrn : isContains(ariaEur, text) ?
                eur : isContains(ariaByn, text) ? byn : isContains(ariaRub, text) ? rub : isContains(ariaPln, text) ?
                pln : isContains(ariaGbr, text) ? gbp : isContains(ariaKzt, text) ? kzt : "";
    }

    public static List<String> getAmountMonetary(String originText, String currencyCode) {
        List<String> parts = new ArrayList<>();
        Pattern patternMonetaryAmount = Pattern.compile(monetary_amount_regex);
        Matcher matcher = patternMonetaryAmount.matcher(getReplacementText(originText, currencyCode));
        while (matcher.find()) {
            parts.add(matcher.group());
        }
        List<String>
                amounts = parts.stream().filter(p -> p.contains(getBadge(currencyCode))).collect(Collectors.toList()),
                monetaryAmounts = new ArrayList<>();
        amounts.forEach(s -> {
            s = getReplace(s, wasteSalary, "");
            if (s.matches(is_number)) {
                monetaryAmounts.add(s);
            }
        });
        return monetaryAmounts;
    }

    public static float getPeriod(String text) {
        return text.contains(year) ? 1.0f / 12.0f : text.contains(month) ?
                1.0f : text.contains(day) ? 22.0f : text.contains(hour) ? 22.0f * 8.0f : 1.0f;
    }

    public static String getReplacementText(String text, String moneyName) {
        return switch (moneyName) {
            case usd -> getReplace(text, ariaUsd, "\\$");
            case hrn -> getReplace(text, ariaHrn, "₴");
            case eur -> getReplace(text, ariaEur, "€");
            case rub -> getReplace(text, ariaRub, "₽");
            case gbp -> getReplace(text, ariaGbr, "£");
            case pln -> getReplace(text, ariaPln, "₧");
            case kzt -> getReplace(text, ariaKzt, "₸");
            case byn -> getReplace(text, ariaByn, "฿");
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
