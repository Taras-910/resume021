package ua.training.top.util.parser.data;

import java.time.LocalDate;
import java.util.List;

import static java.util.List.of;
import static ua.training.top.aggregator.installation.Installation.maxAge;
import static ua.training.top.aggregator.installation.Installation.maxLengthText;

public class DataUtil {
    public static final LocalDate defaultDate = LocalDate.now().minusMonths(1);
    public static final String
            link = "see the card",
            extract_salary = "((?:[\\d,\\.[–до\\-k-  ]+\\s  &nbsp]+\\b)(\\s*)?[  ]?(\\p{Sc}|ƒ))|(" +
                    "(?:\\p{Sc}|ƒ)(\\s*)?[  ]?[\\d,\\.[–до\\-k-  ]\\s  &nbsp]+\\b)",
            monetary_amount_regex = "[-–—k(дот-]",
            is_date = "^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2}$",
            is_age = ".*[1-7]\\d\\s?[годалетрківи]{3,}.*",
            is_kilo = ".*\\d[\\d\\.-]+k.*",
            is_number_format = "[\\d\\.]+",
            is_period_work = "(.*\\d?\\d\\s?[годалетрківи]{3,})?\\s?и?\\s?1?\\d\\s?[месяцевіь]{3,}.*",
            date_period_extract = "((?:\\s?\\d?\\d)\\s?\\(?\\s?([летгодаyears])+)?(\\s?[иі,]?\\s?)?(?:\\s?\\d?\\d)\\s?\\(?\\s?([месяцеваmonths])+\\.*?",
            age_field_extract = "(?:[1-7]\\d)\\s([годалетрківи])+",
            address_field_extract = "(?:[а-яА-ЯіїєA-Za-z,\\s·]+)\\b",
            local_date_extract = "(?:\\d){1,2}\\s([а-яіїє])+|^[а-яіїє]{3,11}",
            document_user_agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15",
            internet_connection_error = "There may be no internet connection or exception={} by url={} ",
            finish_message = "\nfinish is ok,\ntime: {}ms\ncreated: {}\nupdated: {}\nFreshen: {}\n" + ":".repeat(125),
            error = "There is error \ne={}\n for parse \n{}",
            common_number = "Common number resumeTos = {}",
            get_resume = "GetResumes city={} language={}",
            error_parse = "Error parse field={} e={}",
            document_url = "GetDocument url={}\n",
            error_select = "Select error e {}",
            djinni = "DjinniStrategy", habr = "HabrStrategy",
            grc = "GrcStrategy", work = "WorkStrategyStrategy", rabota = "RabotaStrategy", age_field = "age",
            middle = "middle", trainee = "trainee", junior = "junior", senior = "senior", expert = "expert";

    public static final List<String>
            yearAria = of("год", "рік", "year"),
            dayAria = of("день", "day"),
            hourAria = of("час", "годину", "hour"),
            usdAria = of("usd", "$"),
            eurAria = of("eur", "€"),
            plnAria = of("pln", "zł"),
            gbrAria = of("gbp", "£", "₤"),
            kztAria = of("kzt", "тг", "₸"),
            hrnAria = of("hrn", "uah", "грн.", "грн", "₴"),
            rubAria = of("rub", "rur", "руб.", "руб", "₽"),
            bynAria = of("бел. руб.", "бел. руб", "бел руб", "br", "byn", "byr"),
            allSalaries = of("грн", "uah", "hrn", "₴", "$", "usd", "eur", "€", "pln",
                    "zł", "gbp", "£", "₤", "руб", "₽", "kzt", "тг", "₸", "br", "byn"),
            wasteSalary = of(" ", " ", "&nbsp;", "[.]{2,}", "(\\p{Sc}|ƒ)", "\\s+"),
            citiesUA = of("ukraine", "украина", "україна", "kyiv", "kiev", "київ", "киев", "дніпро", "днепр", "dnipro",
                    "харків", "харьков", "kharkiv", "львів", "львов", "lviv", "mykolaiv", "одесса", "odesa", "одеса",
                    "винница", "vinnitsia", "вінниця", "запоріжжя", "запорожье", "zaporizhzhya", "chernivtsi", "чернівці",
                    "черновцы", "чернігів", "чернигов", "chernigiv", "івано-франківськ", "ужгород", "ивано-франковск",
                    "ivano-frankivsk", "луцк", "луцьк", "миколаїв", "николаев", "жовті-води", "кривой-рог", "кривий-ріг",
                    "желтые-воды", "каменец-подольский", "камянець-подільський", "тернопіль", "тернополь", "кропивницкий",
                    "кропивницький", "кировоград", "кіровоград"),
            citiesWorld = of("ізраїль", "израиль", "армения", "швейцарія", "оаэ", "швейцария", "франція", "франция",
                    "italy", "італія", "италия", "сінгапур", "turkey", "сингапур", "англія", "англия", "канада", "польща",
                    "польша", "молдова", "germany", "германия", "чехія", "чехия", "швеція", "швеция", "фінляндія",
                    "финляндия", "finland", "азербайджан", "norway", "poland", "singapore", "czechia", "france",
                    "киргизстан", "iran", "israel", "німеччина", "германия", "australia", "philippines", "uk", "estonia",
                    "netherlands", "узбекістан", "узбекистан", "білорусь", "беларусь", "казахстан", "foreign"),
            citiesRU = of("санкт-петербург", "москва", "россия", "новосибирск", "екатеринбург", "томск", "краснодар",
                    "пермь", "russia", "ростов-на-дону", "нижний новгород", "казань", "самара", "ульяновск", "воронеж"),
            citiesBY = of("minsk", "минск", "гомель", "гродно", "брест", "витебск"),
            monthsOfYear = of("січня", "января", "лютого", "февраля", "березня", "марта", "квітня", "апреля", "травня",
                    "мая", "червня", "июня", "липня", "июля", "серпня", "августа", "вересня", "сентября", "жовтня",
                    "октября", "листопада", "ноября", "грудня", "декабря"),
            wasteWorkBefore = of("продав", "бармен", "ресто", "студент"),
            workersIT = of("develop", "engineer", "разработ", "розроб", "фронт", "front", "бэк", "backend", "web",
                    "веб", "фулстек", "microservice", "микросервис", "програм", "program", "git", "spring", "maven",
                    "sql", "docker", "postgre", "rest", "mvc", "pattern");

    public static boolean isMatch(List<String> area, String text) {
        return area.stream().anyMatch(a -> text.toLowerCase().indexOf(a) > -1);
    }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty() || text.trim().equals("•");
    }

    public static boolean isAge(String text) {
        return !isEmpty(text) && text.matches(is_age);
    }

    public static boolean isEquals(String workplace, List<String> list) {
        return list.stream().anyMatch(workplace.toLowerCase()::equals);
    }

    public static boolean isAgeValid(String age) {
        return age.equals(link) || Integer.parseInt(age.substring(0, age.indexOf(" "))) >= maxAge;
    }

    public static String getLimitation(String text) {
        return isEmpty(text) ? link : text.length() > maxLengthText ? text.substring(0, maxLengthText) : text;
    }

    public static String getUpperStart(String text) {
        return isEmpty(text) || text.length() <= 1 ? link : text.substring(0, 1).toUpperCase().concat(text.substring(1));
    }

    public static String getReplace(String text, List<String> wasteWords, String replacement) {
        for (String s : wasteWords) {
            text = text.replaceAll(s, replacement).trim();
        }
        return text;
    }
}
