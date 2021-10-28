package ua.training.top.util.parser.data;

import java.util.Arrays;
import java.util.List;
import static ua.training.top.util.parser.data.CommonUtil.*;

public class CorrectWorkplace {

    public static final List<String> UA_CITIES = Arrays.asList("ukraine", "украина", "україна", "kyiv", "kiev", "київ",
            "киев", "дніпро", "днепр", "dnipro", "харків", "харьков", "kharkiv", "одесса", "odesa", "львів", "львов",
            "lviv", "mykolaiv", "vinnitsia", "zaporizhzhya", "chernivtsi", "камянецьподільський","миколаїв", "николаев",
            "вінниця", "винница", "запоріжжя", "запорожье", "чернівці", "черновцы", "чернігів", "чернигов", "chernigiv",
            "іванофранківськ", "иванофранковск", "ivanofrankivsk","івано-франківськ","ивано-франковск", "луцк", "луцьк",
            "ivano-frankivsk", "ужгород", "одеса", "каменецподольский", "каменец-подольский","жовті-води", "жовтіводи",
            "камянець-подільський", "кривойрог", "кривийріг", "кривой-рог", "кривий-ріг", "желтые-воды", "желтыеводы",
            "тернопіль", "тернополь", "кропивницкий", "кропивницький", "кировоград", "кіровоград", "желтые", "кривой");
    public static final List<String> RU_CITIES = Arrays.asList("санктпетербург", "санкт-петербург", "москва","россия",
            "новосибирск", "нижний новгород", "казань", "екатеринбург", "краснодар", "пермь", "ростовнадону", "russia",
            "ростов-на-дону", "томск", "самара", "ульяновск", "воронеж");
    public static final List<String> BY_CITIES = Arrays.asList("minsk", "минск", "гомель","гродно", "брест", "витебск");
    public static final List<String> PL_CITIES = Arrays.asList("wroclaw", "вроцлав", "krakow", "краков", "варшава",
            "warszawa", "warsaw");
    public static final List<String> WORLD_CITIES = Arrays.asList("ізраїль", "израиль", "армения", "швейцарія", "оаэ",
            "швейцария", "франція", "франция", "італія", "италия", "сінгапур", "turkey", "сингапур", "англія", "англия",
            "канада", "польща", "польша", "молдова", "германия", "чехія", "чехия", "швеція", "швеция", "фінляндія",
            "финляндия", "finland", "азербайджан", "germany", "norway", "poland", "singapore", "czechia", "france",
            "киргизстан", "німеччина", "германия", "iran", "israel", "australia", "philippines","uk","estonia", "italy",
            "netherlands", "узбекістан", "узбекистан", "білорусь", "беларусь", "казахстан");

    public static String getRegionDjinni(String workplace) {
        return workplace.equals("all") ||workplace.equals("санкт-петербург") ||workplace.equals("remote") ||
                !isMatchesUA(workplace) ?
                "" : (isMatchesUA(workplace) || workplace.equals("украина") || workplace.equals("україна") || workplace.equals("ukraine")) ?
                "&region=ukraine" : isMatchesBy(workplace) ? "&region=belarus" : isMatchesRu(workplace) ?
                "&region=belarus" : "&region=other";
    }

    public static String getLocationDjinni(String workplace) {
        return workplace.equals("all") ||workplace.equals("украина") ||workplace.equals("foreign") ||workplace.equals("remote") ?
                "" : isMatchesUA(workplace) || workplace.equals("москва") ?
                "&location=".concat(translateUACitiesToEn(workplace)) : workplace.equals("санкт-петербург") ?
                "&keywords=санкт-петербург" : "&keywords=".concat(workplace);
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
            default -> workplace.equals("remote") ? "&schedule=remote" : "&area=" .concat(workplace);
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

    public static String getLinkedin(String workplace) {
        return switch (workplace) {
            case "київ", "киев" -> "90010216";
            case "україна", "украина" -> "102264497";
            case "вінниця", "винница" -> "90010222";
            case "харьків", "харьков" -> "90010217";
            case "дніпро", "днепр" -> "90010219";
            case "одеса", "одесса" -> "90010220";
            case "львів", "львов" -> "104983263";
            case "ужгород" -> "90010233";
            case "полтава" -> "102507522";
            case "івано-франківск", "ивано-франковск" -> "109800298";
            case "запоріжжя", "запорожье" -> "104184784";
            case "швейцарія", "швейцария" -> "106693272";
            case "сінгапур", "сингапур" -> "102454443";
            case "франція", "франция" -> "105015875";
            case "ізраїль", "израиль" -> "101620260";
            case "італія", "италия" -> "103350119";
            case "англія", "англия" -> "101165590";
            case "оаэ", "оае" -> "104305776";
            case "канада" -> "101174742";
            case "чехія", "чехия" -> "104508036";
            case "швеція", "швеция" -> "105117694";
            case "польща", "польша" -> "105072130";
            case "німеччина", "германия" -> "101282230";
            case "фінляндія","финляндия" -> "100456013";
            case "санкт-петербург" -> "90010184";
            case "нижний новгород" -> "104043205";
            case "мінськ", "минск" -> "105415465";
            case "ростов-на-дону" -> "102450862";
            case "екатеринбург" -> "90010185";
            case "новосибирск" -> "90010146";
            case "краснодар" -> "106273043";
            case "ульяновск" -> "107078222";
            case "казань" -> "101631519";
            case "россия" -> "101728296";
            case "москва" -> "111154941";
            case "пермь" -> "103472036";
            case "томск" -> "90010159";
            case "самара" -> "106843614";
            case "воронеж" -> "102084685";
            default -> "103644278";
        };
    }

    public static String getRabota(String workplace) {
        workplace = isMatchesWorld(workplace) || workplace.equals("foreign") || workplace.equals("россия") ? "другие_страны" : workplace;
        return switch (workplace) {
            case "київ", "киев" -> "киев";
            case "львів", "львов" -> "львов";
            case "дніпро", "днепр" -> "днепр";
            case "одеса", "одесса" -> "одесса";
            case "харьків", "харьков" -> "харьков";
            default -> workplace.equals("другие_страны") ? workplace : "вся_украина";
        };
    }
    public static String translateUACitiesToEn(String workplace) {
        workplace = workplace.toLowerCase();
        return switch (workplace) {
            case "київ", "киев", "kiev" -> "kyiv";
            case "дніпро", "днепр", "dnepr"-> "dnipro";
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
            case "минск"-> "minsk";
            case "москва"-> "moskow";
            default -> workplace;
        };
    }

    public static String getWork(String workplace) {
        workplace = isMatchesRu(workplace) ? "-1" : workplace;
        return switch (workplace) {
            case "україна", "украина" -> "ua";
            case "київ", "киев", "kiev" -> "kyiv";
            case "запоріжжя","запорожье"-> "zaporizhzhya";
            case "миколаїв", "николаев" -> "mykolaiv";
            case "чорновці", "черновцы" -> "chernivtsi";
            case "чернігів", "чернигов" -> "chernigiv";
            case "вінниця", "винница" -> "vinnitsia";
            case "харків", "харьков" -> "kharkiv";
            case "дніпро", "днепр" -> "dnipro";
            case "одеса", "одесса" -> "odesa";
            case "львів", "львов" -> "lviv";
            case "івано-франківськ",
                    "ивано-франковск" -> "ivano-frankivsk";
            case "ужгород" -> "uzhgorod";
            case "remote" -> "remote";
            case "all" -> "all";
            default -> workplace.equals("-1") ? workplace : "other";
        };
    }

    public static String getYandex(String workplace) {
        return switch (workplace) {
            case "remote" -> "remote";
            case "киев", "київ", "kyiv" -> "kiev";
            case "санкт-петербург" -> "sankt-peterburg";
            case "ростов-на-дону" -> "rostov-na-donu";
            case "нижний новгород" -> "nizhniy_novgorod";
            case "екатеринбург" -> "ekaterinburg";
            case "краснодар" -> "krasnodar";
            case "ульяновск" -> "ulyanovsk";
            case "россия" -> "rossiya";
            case "москва" -> "moskva";
            case "казань" -> "kazan";
            case "пермь" -> "perm";
            case "томск" -> "tomsk";
            case "самара" -> "samara";
            case "минск" -> "minsk";
            default -> "-1";
        };
    }
}
