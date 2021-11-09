package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.List.of;

public class CommonDataUtil {
    public static final Logger log = LoggerFactory.getLogger(CommonDataUtil.class);

    public static final int
            limitText = 300,
            limitAnchor = 125;
    public static final String
            is_age = ".*[1-7]\\d\\s?[годалетрківи]\\s?.*",
            extract_age = "(?:[1-7]\\d)\\s([годалетрківи])+",
            date_regex_number_and_word = "(?:\\d){1,2}\\s([а-яіїє])+|^[а-яіїє]{3,11}",
            matcher_date = "^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2}$",
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
            linkedin = "LinkedinStrategy",
            work = "WorkStrategyStrategy",
            rabota = "RabotaStrategy",
            djinni = "DjinniStrategy",
            yandex = "YandexStrategy",
            habr = "HabrStrategy",
            grc = "GrcStrategy",
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
            citiesPL = of("wroclaw", "вроцлав", "krakow", "краков", "варшава", "warszawa", "warsaw"),
            citiesBY = of("minsk", "минск", "гомель", "гродно", "брест", "витебск"),
            wasteAddress = of("будь", "другие", "города", "еще", "-", "Воды", "Рог", "городов", "який"),
            wasteWorkBefore = of("продав", "бармен", "ресто", "студент"),
            workersIT = of("develop", "engineer", "разработ", "розроб", "фронт", "front", "бэк", "backend", "web",
                    "веб", "фулстек", "microservice", "микросервис", "програм", "program", "git", "spring", "maven",
                    "sql", "docker", "postgre", "rest", "mvc", "pattern"),
            ruleDaysAndMonth = of("січня", "лютого", "березня", "квітня", "травня", "червня", "липня", "серпня", "вересня",
                    "жовтня", "листопада", "грудня", "января", "февраля", "марта", "апреля", "июня", "июля", "августа",
                    "сентября", "октября", "ноября", "декабря", "сьогодні", "вчора"),
            ruleAgo = of("сейчас", "только что", "минуту", "минуты", "минут", "час", "часа", "часов", "назад", "вчера",
                    "день", "дня", "дней", "неделя", "недели", "месяц", "месяца");

    public static boolean isRuleMonth(String text) { return ruleDaysAndMonth.stream().anyMatch(text.toLowerCase()::contains); }

    public static boolean isRuleAgo(String text) { return ruleAgo.stream().anyMatch(text.toLowerCase()::contains); }

    public static boolean isRuleDate(String text) { return text.matches(matcher_date); }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty() || text.trim().equals("•");
    }

    public static boolean isCityUA(String text) {
        return citiesUA.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isCityRu(String text) {
        return citiesRU.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isCityBy(String text) {
        return citiesBY.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isCityPl(String text) {
        return citiesPL.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isCityWorld(String text) {
        return citiesWorld.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isEquals(String workplace, List<String> list) {
        return list.stream().anyMatch(workplace.toLowerCase()::equals);
    }

    public static String getLimitation(String text, int limit) {
        return text.length() > limit ? text.substring(0, limit) : text;
    }

    public static String getLinkIfEmpty(String text) {
        return isEmpty(text) ? link : text;
    }

    public static String getStartUpper(String text) {
        return !isEmpty(text) && text.length() > 1 ? text.substring(0, 1).toUpperCase().concat(text.substring(1)) : text;
    }

    public static String getReplace(String text, List<String> wasteWords, String replacement) {
        for (String s : wasteWords) {
            text = text.replaceAll(s, replacement).trim();
        }
        return text;
    }

    public static boolean isToValid(Freshen f, String text) {
        String temp = text.toLowerCase();
        return (temp.contains(f.getLanguage()) || isWorkerIT(temp)) && wasteWorkBefore.stream().noneMatch(temp::contains);
    }

    public static boolean isWorkerIT(String text) {
        return workersIT.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static List<String> getMatcherGroups(String text, String matcher) {
        Matcher m = Pattern.compile(matcher, Pattern.CASE_INSENSITIVE).matcher(text);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }
}
