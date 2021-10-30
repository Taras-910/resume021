package ua.training.top.util.parser.data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.util.parser.data.CommonUtil.*;

public class CorrectAddress {

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
            "netherlands", "узбекістан", "узбекистан", "білорусь", "беларусь", "казахстан", "foreign");
    public static final List<String> unfit = List.of("будь", "другие", "города", "еще", "-", "Воды", "Рог");

    public static String getToAddressLinkedin(String address) {
        String[] addressParts = address.split(",");
        return addressParts.length > 1 && addressParts[0].trim().equalsIgnoreCase(addressParts[1].trim()) ?
                address.substring(address.indexOf(",") + 1).trim() : address;
    }

    public static String getToAddressRabota(String address) {
        return isEmpty(address) ? link : address.length() > 1 && address.contains(" ") ?
                address.substring(0, address.indexOf(" ")).trim() : address;
    }

    public static String getToAddressWork(String address) {
        return address.contains("·") ? address.substring(address.lastIndexOf("·") + 1).trim() : address;
    }

    public static String formatAddress(String address) {
        address = address.toLowerCase();
        return switch (address) {
            case "украина", "україна", "ua", "ukraine" -> "Украина";
            case "foreign" -> "foreign";
            case "remote" -> "remote";
            case "киев","київ", "kyiv", "kiev" -> "Киев";
            case "одесса","одеса", "odesa" -> "Одесса";
            case "харьков", "харків", "kharkiv" -> "Харьков";
            case "днепр", "дніпро", "dnipro" -> "Днепр";
            case "sanktpeterburg" -> "Санкт-петербург";
            case "минск", "мінськ", "minsk" -> "Минск";
            case "москва", "moskow" -> "Москва";
            case "миколаїв", "mykolaiv" -> "Николаев";
            case "вінниця", "vinnitsia" -> "Винница";
            case "чернігів", "chernigiv" -> "Чернигов";
            case "тернопіль", "тернополь" -> "Тернополь";
            case "чернівці", "chernivtsi" -> "Черновцы";
            case "запоріжжя", "zaporizhzhya" -> "Запорожье";
            case "кировоград", "кіровоград" -> "Кировоград";
            case "кривийріг", "кривой", "кривойрог" -> "Кривой-Рог";
            case "желтые", "желтыеводы","жовтіводи" -> "Желтые-Воды";
            case "камянецьподільський", "каменецподольский" -> "Каменец-Подольский";
            case "віддалена робота", "віддалено", "no location" -> "remote";
            case "іванофранківськ", "ivanofrankivsk", "иванофранковск" -> "Ивано-Франковск";
            case "львів", "lviv" -> "Львов";
            case "uzhgorod" -> "Ужгород";
            case "russia", "росія" -> "Россия";
            case "uk", "англія" -> "Англия";
            case "iran", "Іран" -> "Іран";
            case "czechia", "чехія" -> "Чехия";
            case "turkey", "турція" -> "Турция";
            case "israel", "ізраїль" -> "Израиль";
            case "norway", "норвеія" -> "Норвегия";
            case "france", "франція" -> "Франция";
            case "estonia", "естонія" -> "Эстония";
            case "germany", "німеччина" -> "Германия";
            case "finland", "фінляндія" -> "Финляндия";
            case "singapore", "сінгапур" -> "Сингапур";
            case "australia", "австралія" -> "Австралия";
            case "switzerland", "швейцарія" -> "Швейцария";
            case "узбекістан", "узбекистан" -> "Узбекистан";
            case "білорусь", "беларусь" -> "Беларусь";
            case "вірменія", "армения" -> "Армения";
            case "країни", "страны" -> "Foreign";
            case "philippines" -> "Филипины";
            case "казахстан" -> "Казахстан";
            case "canada" -> "Канада";
            case "города" -> "города";
            case "usa" -> "США";
            default -> isEmpty(address) ? "" : getStartUpper(address);
        };
    }

    public static List<String> getAddressClearedSet(String address) {
        for(String s : unfit){
            address = address.toLowerCase().replaceAll(s, "");
        }
        return List.of(address.split("[^A-Za-zА-Яа-яҐґЇїІі]"));
    }

    public static String getToAddressFormat(String address) {
        if (address.equals("all") || isEmpty(address)) {
            return link;
        }
        String result = getAddressClearedSet(address).stream()
                .map(CorrectAddress::formatAddress)
                .filter(s -> !isEmpty(s)).distinct()
                .collect(Collectors.joining(" · "));

        return isMatchesUA(address) && !result.contains("Украина") ? result.concat(" · Украина") :
                isMatchesRu(address) && !result.contains("Россия") ? result.concat(" · Россия") :
                        isMatchesBy(address) && !result.contains("Беларусь") ? result.concat(" · Беларусь") :
                                isMatchesPl(address) && !result.contains("Польша") ? result.concat(" · Польша") :
                                        isMatchesWorld(address)
                                                && !result.contains("foreign") ? result.concat(" · foreign") : result;
    }
}
