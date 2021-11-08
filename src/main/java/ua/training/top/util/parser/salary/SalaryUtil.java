package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static java.lang.Float.parseFloat;
import static ua.training.top.util.parser.data.CommonDataUtil.*;
import static ua.training.top.util.parser.salary.CommonSalaryUtil.*;

public class SalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryUtil.class);

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
}
