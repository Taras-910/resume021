package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.util.parser.data.CommonDataUtil;

import static java.lang.Float.parseFloat;
import static java.lang.String.valueOf;
import static ua.training.top.util.parser.salary.CommonSalaryUtil.*;
import static ua.training.top.util.parser.salary.FormatUtil.getCleanedText;

public class MoneyTypeUtil {
    public static final Logger log = LoggerFactory.getLogger(MoneyTypeUtil.class);

    public static String getEurGbp(String temp, String moneyName) {
        String cleaned = getCleanedText(temp, moneyName);
        if (temp.contains("—")) {
            if (cleaned.length() < 4) return "1";
            if (temp.contains(year)) {
                cleaned = valueOf(Integer.parseInt(cleaned.split("\\W")[0]) / 12).concat("-")
                        .concat(valueOf(Integer.parseInt(cleaned.split("\\W")[1]) / 12));
            }
            return toUsd(cleaned.split("\\W")[0], moneyName).concat("—")
                    .concat(toUsd(cleaned.split("\\W")[1], moneyName));
        } else {
            return temp.contains("от") ? toUsd(cleaned, moneyName).concat("—").concat("1")
                    : "1".concat("—").concat(toUsd(cleaned, moneyName));
        }
    }

    public static String getUsd(String temp, String moneyName) {
        temp = temp.replace("$", "").replace("usd", "").concat("$");
        String cleaned = getCleanedText(temp, moneyName);
        if (!temp.contains("грн")) {
            return cleaned.contains("—") ? cleaned.replace("—", "00—").concat("00") :
                    temp.contains("от") ? cleaned.concat("00—").concat("1") : "1".concat("—").concat(cleaned).concat("00");
        } else {
            return temp.contains("—") ?
                    cleaned.substring(temp.indexOf("$") + 1).replace("—", "00—").concat("00") :
                    temp.contains("·") ? "1".concat("—").concat(temp.substring(temp.indexOf("$") + 1)).concat("00") : cleaned;
        }
    }


    public static String getHrnRub(String temp, String moneyName) {
        String cleaned = getCleanedText(temp, moneyName);
        if (temp.contains("—")) {
            return cleaned.length() < 4 ? "1" : toUsd(cleaned.split("\\W")[0], moneyName).concat("—")
                    .concat(toUsd(cleaned.split("\\W")[1], moneyName));
        } else {
            cleaned = toUsd(cleaned, moneyName);
            return temp.contains("от") ? cleaned.concat("—").concat("1") : "1".concat("—").concat(cleaned);
        }
    }

    public static String getPln(String temp, String moneyName) {
        temp = temp.replaceAll("–", "—");
        try {
            String cleaned = getCleanedText(temp, moneyName);
            if (cleaned.matches("\\d+\\W\\d+")) {
                return cleaned.length() < 4 ? "1" : switch (getPeriod(temp)) {
                    case year -> toUsd(valueOf((int) (parseFloat(cleaned.split("\\W")[0]) / 12)), moneyName).concat("—")
                            .concat(toUsd(valueOf((int) (parseFloat(cleaned.split("—")[1]) / 12)), moneyName));
                    case day -> toUsd(getString(cleaned.split("\\W")[0], 22), moneyName).concat("—")
                            .concat(toUsd(getString(cleaned.split("\\W")[1], 22), moneyName));
                    case hour -> toUsd(getString(cleaned.split("\\W")[0], 8 * 22), moneyName).concat("—")
                            .concat(toUsd(getString(cleaned.split("\\W")[1], 8 * 22), moneyName));
                    default -> toUsd(cleaned.split("\\W")[0],
                            moneyName).concat("—").concat(toUsd(cleaned.split("—")[1], moneyName)); //usd
                };

            } else {
                return switch (getPeriod(temp)) {
                    case year -> "1".concat("—").concat(toUsd(valueOf((int) (parseFloat(cleaned) / 12)), moneyName));
                    case hour -> "1".concat("—").concat(toUsd(valueOf((int) (parseFloat(cleaned) * 8 * 22)), moneyName));
                    case day -> "1".concat("—").concat(toUsd(valueOf((int) (parseFloat(cleaned) * 22)), moneyName));
                    default -> "1".concat("—").concat(toUsd(cleaned, moneyName));
                };
            }

        } catch (NumberFormatException e) {
            log.info(CommonDataUtil.error, e, temp);
        }
        return temp;
    }

    private static String getString(String s, int days) {
        return valueOf((int) (parseFloat(s) * days));
    }
}
