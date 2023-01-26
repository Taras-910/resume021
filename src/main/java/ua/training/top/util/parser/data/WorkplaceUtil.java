package ua.training.top.util.parser.data;

import static java.util.List.of;
import static ua.training.top.util.parser.data.CommonUtil.isEquals;
import static ua.training.top.util.parser.data.CommonUtil.isMatch;
import static ua.training.top.util.parser.data.ConstantsUtil.all;
import static ua.training.top.util.parser.data.ConstantsUtil.otherAria;

public class WorkplaceUtil {

    public static String getRabota(String workplace) {
        return switch (workplace) {
            case "київ", "киев" -> "киев";
            case "львів", "львов" -> "львов";
            case "дніпро", "днепр" -> "днепр";
            case "одеса", "одесса" -> "одесса";
            case "харків", "харьков" -> "харьков";
            default -> isEquals(workplace, of("foreign", "россия", "минск")) || isMatch(otherAria, workplace) ?
                    "другие_страны" : "вся_украина";
        };
    }

    public static String getRec(String workplace) {
        return switch (workplace) {
            case "київ", "киев", "kiev" -> "kyiv";
            case "дніпро", "днепр", "dnepr" -> "dnepr";
            case "харків", "харьков" -> "harkiv";
            case "одеса", "одесса" -> "odessa";
            case "львів", "львов" -> "lvov";
            case "миколаїв", "николаев" -> "mykolaiv";
            case "вінниця", "винница" -> "vinnitsia";
            case "запоріжжя", "запорожье" -> "zaporizhia";
            case "чорновці", "черновцы" -> "chernivtsi";
            case "чернігів", "чернигов" -> "chernihiv";
            case "івано-франківськ", "ивано-франковск" -> "ivano-frankоvsk";
            case "ужгород" -> "uzhgorod";
            case "минск" -> "minsk";
            case "алмаата", "almaty" -> "almaty";
            case "берлин", "берлін", "berlin", "германія", "германия" -> "berlin";
            case "варшава", "varshava", "польша", "польща" -> "varshava";
            case "житомир", "zhitomir" -> "zhitomir";
            case "тернопіль", "тернополь", "ternopil" -> "ternopil";
            case "хмельницький", "хмельницкий", "hmelnitskii" -> "hmelnitskii";
            default -> all;
        };
    }

    public static String getUA_en(String workplace) {
        return switch (workplace) {
            case "київ", "киев", "kiev", "kyiv" -> "Kyiv";
            case "запоріжжя", "запорожье", "zaporizhzhya" -> "Zaporizhzhya";
            case "миколаїв", "николаев", "mykolaiv" -> "Mykolaiv";
            case "чорновці", "черновцы", "chernivtsi" -> "Chernivtsi";
            case "чернігів", "чернигов", "chernigiv" -> "Chernigiv";
            case "вінниця", "винница", "vinnitsia" -> "Vinnitsia";
            case "харків", "харьков", "kharkiv" -> "Kharkiv";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "Dnipro";
            case "одеса", "одесса", "odessa" -> "Odesa";
            case "львів", "львов", "lviv" -> "Lviv";
            case "ужгород", "uzhgorod" -> "Uzhgorod";
            case "івано-франківськ", "ивано-франковск" -> "Ivano-Frankivsk";
            case "тернопіль", "тернополь", "ternopil" -> "Ternopil";
            case "минск", "мінськ", "minsk" -> "minsk";
            default -> "";
        };
    }



}
