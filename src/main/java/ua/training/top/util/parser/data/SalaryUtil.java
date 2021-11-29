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
    public static final Pattern patternMonetaryAmount = Pattern.compile(extract_salary);
    public static final float
            rate_pln_to_usd = 3.98f,
            rate_eur_to_usd = 0.86f,
            rate_gbp_to_usd = 0.73f,
            rate_byn_to_usd = 2.43f,
            rate_hrn_to_usd = 26.25f,
            rate_rub_to_usd = 71.78f,
            rate_kzt_to_usd = 426.74f,
            usd_one_to_one = 1.0f;

    public static Integer getToSalary(String originText) {
        if (isEmpty(originText) || !isMatch(allSalaries, originText)) {
            return 1;
        }
        String text = originText.replaceAll(",", ".").toLowerCase();
        String code = getCurrencyCode(text);
        List<String> values = getMonetaryAmount(text, code);
        return values.isEmpty() ? 1 : getAmount(values.get(0), code, text);
    }

    public static int getAmount(String valuesPart, String code, String text) {
        String value = valuesPart.replaceAll("\\.", "");
        int amount = 1;
        try {
            amount = (int) ((parseFloat(value) * getPeriod(text) / getRate(code) * getPoint(valuesPart))
                    * (text.matches(is_kilo) ? 1000 : 1));
        } catch (NumberFormatException e) {
            log.error(error, e, value);
        }
        return amount <= 10000000 ? amount : Math.min(amount / 12, 10000000);
    }

    public static List<String> getMonetaryAmount(String text, String code) {
        List<String> parts = new ArrayList<>();
        Matcher matcher = patternMonetaryAmount.matcher(getReplacementText(text, code));
        while (matcher.find()) {
            parts.add(matcher.group());
        }
        List<String>
                amounts = parts.stream().filter(p -> p.contains(code)).collect(Collectors.toList()),
                monetaryAmounts = new ArrayList<>();
        amounts.forEach(s -> {
            s = getReplace(s, wasteSalary, "");
            if (s.matches(is_number_format)) {
                monetaryAmounts.add(s);
            }
        });
        return monetaryAmounts;
    }

    public static float getPeriod(String text) {
        return isMatch(yearAria, text) ? 1.0f / 12.0f : isMatch(dayAria, text) ? 22.0f : isMatch(hourAria, text) ? 22.0f * 8.0f : 1.0f;
    }

    private static int getPoint(String str) {
        int decimalPoint = str.length() - str.lastIndexOf(".");
        return str.indexOf(".") == -1 ? 100 : decimalPoint == 3 ? 1 : decimalPoint == 2 ? 10 : 100;
    }

    public static String getCurrencyCode(String text) {
        return isMatch(usdAria, text) ? "$" : isMatch(hrnAria, text) ? "₴" : isMatch(eurAria, text) ?
                "€" : isMatch(bynAria, text) ? "฿" : isMatch(rubAria, text) ? "₽" : isMatch(plnAria, text) ?
                "₧" : isMatch(gbrAria, text) ? "£" : isMatch(kztAria, text) ? "₸" : "";
    }

    public static String getReplacementText(String text, String currencyCode) {
        text = text.replaceAll(monetary_amount_regex, currencyCode.equals("$") ? "\\$" : currencyCode);
        return switch (currencyCode) {
            case "$" -> getReplace(text, usdAria, "\\$");
            case "₴" -> getReplace(text, hrnAria, "₴");
            case "€" -> getReplace(text, eurAria, "€");
            case "₽" -> getReplace(text, rubAria, "₽");
            case "£" -> getReplace(text, gbrAria, "£");
            case "₧" -> getReplace(text, plnAria, "₧");
            case "₸" -> getReplace(text, kztAria, "₸");
            case "฿" -> getReplace(text, bynAria, "฿");
            default -> text;
        };
    }

    public static Float getRate(String currencyCode) {
        return switch (currencyCode) {
            case "₴" -> rate_hrn_to_usd;
            case "₧" -> rate_pln_to_usd;
            case "€" -> rate_eur_to_usd;
            case "£" -> rate_gbp_to_usd;
            case "₽" -> rate_rub_to_usd;
            case "₸" -> rate_kzt_to_usd;
            case "฿" -> rate_byn_to_usd;
            default -> usd_one_to_one;
        };
    }
}
