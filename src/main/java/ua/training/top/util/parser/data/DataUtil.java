package ua.training.top.util.parser.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.List.of;
import static ua.training.top.aggregator.installation.InstallationUtil.limitText;
import static ua.training.top.aggregator.installation.InstallationUtil.maxAge;

public class DataUtil {
    public static final LocalDate defaultDate = LocalDate.now().minusMonths(1);
    public static final int limitAnchor = 125;
    public static final String
            is_date = "^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2}$",
            is_age = ".*[1-7]\\d\\s?[годалетрківи]\\s?.*",
            extract_age = "(?:[1-7]\\d)\\s([годалетрківи])+",
            extract_address = "(?:[а-яА-ЯіїєA-Za-z,\\s·]+)\\b",
            extract_date = "(?:\\d){1,2}\\s([а-яіїє])+|^[а-яіїє]{3,11}",
            document_user_agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15",
            middle = "middle", trainee = "trainee", junior = "junior", senior = "senior", expert = "expert",
            internet_connection_error = "There may be no internet connection or exception={} by url={} ",
            finish = "\nfinish is ok,\ncreated: {}\nupdated: {}\nFreshen: {}\n" + ":".repeat(125),
            error = "There is error \ne={}\n for parse \n{}",
            common_number = "Common number resumeTos = {}",
            get_resume = "GetResumes city={} language={}",
            error_parse = "Error parse field={} e={}",
            document_url = "GetDocument url={}\n",
            error_select = "Select error e {}",
            work = "WorkStrategyStrategy", rabota = "RabotaStrategy", djinni = "DjinniStrategy",
            habr = "HabrStrategy", grc = "GrcStrategy",
            local_date_field ="releaseDate", age_field = "age", address_field = "address",
            link = "see the card";
    public static final List<String>
            citiesUA = of("ukraine", "украина", "україна", "kyiv", "kiev", "київ", "киев", "дніпро", "днепр", "dnipro",
            "харків", "харьков", "kharkiv", "львів", "львов", "lviv", "mykolaiv", "одесса", "odesa", "одеса", "винница",
            "vinnitsia", "вінниця", "запоріжжя", "запорожье", "zaporizhzhya", "chernivtsi", "чернівці", "черновцы",
            "чернігів", "чернигов", "chernigiv", "іванофранківськ", "иванофранковск", "ivanofrankivsk",
            "івано-франківськ", "ужгород", "ивано-франковск", "ivano-frankivsk", "луцк", "луцьк", "миколаїв", "николаев",
            "камянецьподільський", "каменецподольский", "каменец-подольский", "камянець-подільський", "жовті-води",
            "жовтіводи", "кривой-рог", "кривойрог", "кривийріг", "кривий-ріг", "желтые-воды", "желтыеводы", "тернопіль",
            "тернополь", "кропивницкий", "кропивницький", "кировоград", "кіровоград", "желтые", "кривой"),
            citiesWorld = of("ізраїль", "израиль", "армения", "швейцарія", "оаэ", "швейцария", "франція", "франция",
                    "italy", "італія", "италия", "сінгапур", "turkey", "сингапур", "англія", "англия", "канада", "польща",
                    "польша", "молдова", "germany", "германия", "чехія", "чехия", "швеція", "швеция", "фінляндія",
                    "финляндия", "finland", "азербайджан", "norway", "poland", "singapore", "czechia", "france",
                    "киргизстан", "iran", "israel", "німеччина", "германия", "australia", "philippines", "uk", "estonia",
                    "netherlands", "узбекістан", "узбекистан", "білорусь", "беларусь", "казахстан", "foreign"),
            citiesRU = of("санктпетербург", "санкт-петербург", "москва", "россия", "новосибирск", "екатеринбург", "томск",
                    "нижний новгород", "казань", "краснодар", "пермь", "ростовнадону", "russia", "ростов-на-дону",
                    "самара", "ульяновск", "воронеж"),
            citiesBY = of("minsk", "минск", "гомель", "гродно", "брест", "витебск"),
            wasteWorkBefore = of("продав", "бармен", "ресто", "студент"),
            workersIT = of("develop", "engineer", "разработ", "розроб", "фронт", "front", "бэк", "backend", "web",
                    "веб", "фулстек", "microservice", "микросервис", "програм", "program", "git", "spring", "maven",
                    "sql", "docker", "postgre", "rest", "mvc", "pattern");

    public static boolean isDate(String text) { return text.matches(is_date); }

    public static boolean isEmpty(String text) { return text == null || text.trim().isEmpty() || text.trim().equals("•"); }

    public static boolean isCityUA(String text) {
        return citiesUA.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isCityRu(String text) {
        return citiesRU.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isCityBy(String text) {
        return citiesBY.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isCityWorld(String text) { return citiesWorld.stream().anyMatch(text.toLowerCase()::contains); }

    public static boolean isEquals(String workplace, List<String> list) {
        return list.stream().anyMatch(workplace.toLowerCase()::equals);
    }

    public static boolean isWorkerIT(String text) {
        return workersIT.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static String getLimitation(String text) { return getByLimit(getLinkIfEmpty(text), limitText); }

    public static boolean isAgeAfter(String age) {
        return isEmpty(age) || !age.matches(is_age) || Integer.parseInt(age.substring(0, age.indexOf(" "))) >= maxAge;
    }

    public static String getByLimit(String text, int limit) {
        return text.length() > limit ? text.substring(0, limit) : text;
    }

    public static String getLinkIfEmpty(String text) { return isEmpty(text) ? link : text; }

    public static String getUpperStart(String text) {
        return !isEmpty(text) && text.length() > 1 ? text.substring(0, 1).toUpperCase().concat(text.substring(1)) : link;
    }

    public static String getReplace(String text, List<String> wasteWords, String replacement) {
        for (String s : wasteWords) {
            text = text.replaceAll(s, replacement).trim();
        }
        return text;
    }

    public static String getMatch(String fieldName, String text) {
        getLinkIfEmpty(text);
        //https://stackoverflow.com/questions/63964529/a-regex-to-get-any-price-string
        Matcher m = Pattern.compile(getMatcher(fieldName), Pattern.CASE_INSENSITIVE).matcher(text);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list.size() > 0 ? list.get(0) : fieldName.equals(local_date_field) ? text : link;
    }

    private static String getMatcher(String fieldName) {
        return switch (fieldName) {
            case local_date_field -> extract_date;
            case address_field -> extract_address;
            case age_field -> extract_age;
            default -> "";
        };
    }
}
