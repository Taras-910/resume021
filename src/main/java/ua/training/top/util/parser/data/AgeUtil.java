package ua.training.top.util.parser.data;

import java.util.List;

import static ua.training.top.aggregator.installation.InstallationUtil.maxAge;
import static ua.training.top.util.parser.data.CommonDataUtil.*;

public class AgeUtil {

    public static String getToAge(String text) {
        List<String> list = getMatcherGroups(text, extract_age);
        return list.size() > 0 ? list.get(0) : link;
    }

    public static boolean isAgeAfter(String age) {
         return age.equals(link) || isEmpty(age) || !age.contains(" ") || !age.matches(".*\\d.*") ||
                 Integer.parseInt(age.substring(0, age.indexOf(" "))) >= maxAge;
    }
}
