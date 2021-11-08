package ua.training.top.util.parser.data;

import static java.util.List.of;
import static ua.training.top.util.parser.data.CommonDataUtil.*;

public class AgeUtil {

    public static final Integer maxAge = 22;

    public static String getToAgeRabota(String age) {
        if (isEmpty(age)) {
            return link;
        }
        if (age.contains("$") && age.contains(" ")) {
            age = age.substring(age.indexOf(" "), age.indexOf("$")).trim();
        } else if (age.contains("грн.") && age.contains(" ")) {
            String[] ageParts = age.split(" ");
            if (ageParts.length > 2) {
                age = ageParts[1].concat(" ").concat(ageParts[2]);
            }
        }
        return getLinkIfEmpty(age);
    }

    public static String getToAgeWork(String age) {
        return isEmpty(age) || !age.contains("год") || !age.contains("лет") ? link : age;
    }

    public static String getToAgeHabr(String text){
        text = text.startsWith("Возраст")? text.replace("Возраст и стаж ", "") : text;
        return !isContains(text, of("год", "лет"))? link : text.contains("·") ? text.substring(0, text.indexOf("·")).trim() : text;
    }

    public static boolean isAgeAfter(String age) {
         return age.equals(link) || isEmpty(age) || !age.contains(" ") || !age.matches(".*\\d.*") ||
                 Integer.parseInt(age.substring(0, age.indexOf(" "))) >= maxAge;
    }

}
