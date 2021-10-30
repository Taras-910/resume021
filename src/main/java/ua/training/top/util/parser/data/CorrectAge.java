package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.CommonUtil.*;

public class CorrectAge {

    public static String getToAgeRabota(String age) {
        if (isEmpty(age)) {
            return link;
        }
        if(age.contains("$") && age.contains(" ")) {
            age = age.substring(age.indexOf(" "), age.indexOf("$")).trim();
        } else if(age.contains("грн.") && age.contains(" ")) {
            String[] ageParts = age.split(" ");
            if(ageParts.length > 2) {
                age = ageParts[1].concat(" ").concat(ageParts[2]);
            }
        }
        return getMessageIfEmpty(age);
    }

    public static String getToAgeWork(String age) {
        return isEmpty(age) || !age.matches(".*\\d.*") ?  link : age;
    }
}
