package ua.training.top.util.parser.data;

import static java.util.List.of;
import static ua.training.top.util.parser.data.DataUtil.*;

public class WorkplaceUtil {

    public static String getDjinni(String workplace) {
        workplace = workplace.toLowerCase();
        return switch (workplace) {
            case "київ", "киев", "kiev" -> "kyiv";
            case "дніпро", "днепр", "dnepr" -> "dnipro";
            case "харків", "харьков" -> "kharkiv";
            case "одеса", "одесса" -> "odesa";
            case "львів", "львов" -> "lviv";
            case "миколаїв", "николаев" -> "mykolaiv";
            case "вінниця", "винница" -> "vinnitsia";
            case "запоріжжя", "запорожье" -> "zaporizhzhya";
            case "чорновці", "черновцы" -> "chernivtsi";
            case "чернігів", "чернигов" -> "chernigiv";
            case "івано-франківськ", "ивано-франковск" -> "ivano-frankivsk";
            case "ужгород" -> "uzhgorod";
            case "минск" -> "minsk";
            case "москва" -> "moskow";
            default -> workplace;
        };
    }

    public static String getGrc(String workplace) {
        return switch (workplace) {
            case "київ", "киев" -> "115";
            case "дніпро", "днепр" -> "2126";
            case "одеса", "одесса" -> "2188";
            case "львів", "львов" -> "2180";
            case "россия" -> "113";
            case "москва" -> "1";
            case "казань" -> "1624";
            case "пермь" -> "1317";
            case "томск" -> "1255";
            case "самара" -> "1586";
            case "воронеж" -> "1844";
            case "краснодар" -> "1438";
            case "ульяновск" -> "1614";
            case "харьків", "харьков" -> "2206";
            case "санкт-петербург" -> "2";
            case "нижний новгород" -> "1679";
            case "мінськ", "минск" -> "1002";
            case "ростов-на-дону" -> "1530";
            case "новосибирск" -> "1202";
            case "екатеринбург" -> "1261";
            case "германія", "германия" -> "27";
            case "норвегия", "норвегія" -> "207";
            case "україна", "украина" -> "5";
            case "ізраїль", "израиль" -> "33";
            case "швеція", "швеция" -> "149";
            case "польща", "польша" -> "74";
            case "сша" -> "85";
            case "all" -> "all";
            default -> workplace.equals("remote") ?
                    "&schedule=remote" : getBuild("&area=").append(workplace).toString();
        };
    }

    public static String getHabr(String workplace) {
        return switch (workplace) {
            case "київ", "киев" -> "908";
            case "новосибирск" -> "717";
            case "санкт-петербург" -> "679";
            case "нижний новгород" -> "715";
            case "минск", "мінськ" -> "713";
            case "ростов-на-дону" -> "726";
            case "екатеринбург" -> "693";
            case "краснодар" -> "707";
            case "ульяновск" -> "739";
            case "воронеж" -> "692";
            case "москва" -> "678";
            case "казань" -> "698";
            case "самара" -> "728";
            case "пермь" -> "722";
            case "томск" -> "736";
            case "remote", "all" -> "all";
            default -> "-1";
        };
    }

    public static String getRabota(String workplace) {
        return switch (workplace) {
            case "київ", "киев" -> "киев";
            case "львів", "львов" -> "львов";
            case "дніпро", "днепр" -> "днепр";
            case "одеса", "одесса" -> "одесса";
            case "харків", "харьков" -> "харьков";
            default -> isEquals(workplace, of("foreign", "россия", "минск")) || isMatch(citiesWorld, workplace) ?
                    "другие_страны" : "вся_украина";
        };
    }

    public static String getWork(String workplace) {
        return switch (workplace) {
            case "україна", "украина" -> "ua";
            case "київ", "киев", "kiev" -> "kyiv";
            case "запоріжжя", "запорожье" -> "zaporizhzhya";
            case "миколаїв", "николаев" -> "mykolaiv";
            case "чорновці", "черновцы" -> "chernivtsi";
            case "чернігів", "чернигов" -> "chernigiv";
            case "вінниця", "винница" -> "vinnitsia";
            case "харків", "харьков" -> "kharkiv";
            case "дніпро", "днепр" -> "dnipro";
            case "одеса", "одесса" -> "odesa";
            case "львів", "львов" -> "lviv";
            case "ужгород" -> "uzhgorod";
            case "remote" -> "remote";
            case "all" -> "all";
            case "івано-франківськ", "ивано-франковск" -> "ivano-frankivsk";
            default -> "other";
        };
    }
}
