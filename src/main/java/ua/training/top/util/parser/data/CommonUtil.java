package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.CorrectAddress.*;

public class CommonUtil {
    public static final String document_user_agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15";
    public static final String internet_connection_error = "There may be no internet connection or exception={} by url={} ";
    public static final String error = "There is error \ne={}\n for parse element \n{}";
    public static final String error_message = "Attention! There is error due to";
    public static final String linkedin = "LinkedinStrategy";
    public static final String work = "WorkStrategyStrategy";
    public static final String rabota = "RabotaStrategy";
    public static final String djinni = "DjinniStrategy";
    public static final String yandex = "YandexStrategy";
    public static final String link = "see the card";
    public static final String habr = "HabrStrategy";
    public static final String grc = "GrcStrategy";

    public static String getStartUpper(String city) {
        return !isEmpty(city) && city.length() > 1 ? city.substring(0, 1).toUpperCase().concat(city.substring(1)) : city;
    }

    public static String getLimitation(String text) {
        return text.length() > 300 ? text.substring(0, 300) : text;
    }

    public static boolean isEmpty(String text) { return text == null || text.trim().equals("") || text.isEmpty(); }

    public static String getMessageIfEmpty(String text) { return isEmpty(text) ? link : text; }

    public static boolean isMatchesUA(String text) {
        return UA_CITIES.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isMatchesRu(String text) {
        return RU_CITIES.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isMatchesBy(String text) {
        return BY_CITIES.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isMatchesPl(String text) {
        return PL_CITIES.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isMatchesWorld(String text) { return WORLD_CITIES.stream().anyMatch(text.toLowerCase()::contains); }

    public static String correctJava_Script(String text) { return text.replaceAll("Java Script", "JavaScript"); }
}
