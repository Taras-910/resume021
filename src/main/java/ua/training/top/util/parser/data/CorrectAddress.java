package ua.training.top.util.parser.data;

import ua.training.top.model.Freshen;

import java.util.Arrays;
import java.util.stream.Collectors;
import static ua.training.top.util.parser.data.CommonUtil.*;

public class CorrectAddress {
    public static String getToAddressLinkedin(String address) {
        String[] addressParts = address.split(",");
        return addressParts.length > 1 && addressParts[0].trim().equalsIgnoreCase(addressParts[1].trim()) ?
                address.substring(address.indexOf(",") + 1).trim() : address;
    }

    public static String getToAddressRabota(String address) {
        return isEmpty(address) ? "see the card" : address.length() > 1 && address.contains(" ") ?
                address.substring(0, address.indexOf(" ")).trim() : address;
    }

    public static String getToAddressWork(String address) {
        return address.contains("·") ? address.substring(address.lastIndexOf("·") + 1).trim() : address;
    }

    public static String getToAddressYandex(String address, Freshen freshen) {
        address = address.equalsIgnoreCase(freshen.getWorkplace()) ? address : freshen.getWorkplace().concat(", ").concat(address).trim();
        if (address.contains(",")) {
            String[] addressParts = address.split(",");
            address = addressParts[0].equalsIgnoreCase(addressParts[1]) ? address.substring(address.indexOf("," + 1)).trim() : address;
        }
        return address;
    }

    // Linkedin, Yandex
    public static String getToAddress(String address) {
        address = address.contains("Агломерация") ? address.replace("Агломерация", "агломерация") : address;
        address = address.contains("VIP") ? address.substring(address.indexOf("P") + 3).trim() : address;
        return cityToFormatDB(address);
    }

    public static String cityToFormatDB(String address) {
        return switch (address.toLowerCase()) {
            case "київ", "kyiv", "kiev" -> "Киев";
            case "дніпро", "dnipro" -> "Днепр";
            case "харків", "kharkiv" -> "Харьков";
            case "миколаїв", "mykolaiv" -> "Николаев";
            case "вінниця", "vinnitsia" -> "Винница";
            case "чернігів", "chernigiv" -> "Чернигов";
            case "тернопіль", "тернополь" -> "Тернополь";
            case "чернівці", "chernivtsi" -> "Черновцы";
            case "запоріжжя", "zaporizhzhya" -> "Запорожье";
            case "кировоград", "кіровоград" -> "Кировоград";
            case "україна", "ua", "ukraine" -> "Украина";
            case "кривийріг", "кривой", "кривойрог" -> "Кривой-Рог";
            case "желтые", "желтыеводы","жовтіводи" -> "Желтые-Воды";
            case "камянецьподільський", "каменецподольский" -> "Каменец-Подольский";
            case "віддалена робота", "віддалено", "no location" -> "remote";
            case "іванофранківськ", "ivanofrankivsk", "иванофранковск" -> "Ивано-Франковск";
            case "одеса", "odesa" -> "Одесса";
            case "львів", "lviv" -> "Львов";
            case "uzhgorod" -> "Ужгород";
            case "moskow" -> "Москва";
            case "мінськ", "minsk" -> "Минск";
            case "sanktpeterburg" -> "Санкт-петербург";
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
            case "usa" -> "США";
            default -> isEmpty(address) ? "" : getStartUpper(address);
        };
    }

    public static String getToAddressFormat(String address) {
        if(address.equals("all")) {
            return "see the card";
        }
        address = address.replaceAll("-", "").toLowerCase();
        String result = Arrays.stream(address.split("[^A-Za-zА-Яа-яҐґЇїІі]"))
                .map(CorrectAddress::cityToFormatDB)
                .filter(s -> !isEmpty(s))
                .filter(s -> !s.equals("будь"))
                .filter(s -> !s.equals("еще"))
                .filter(s -> !s.equals("другие"))
                .filter(s -> !s.equals("города"))
                .filter(s -> !s.equals("Воды"))
                .filter(s -> !s.equals("Рог"))
                .distinct()
                .collect(Collectors.joining(" ·  "));
        if (isMatchesUA(address)) {
            result = result.contains("Украина") ? result : result.concat(" · Украина");
        } else if (isMatchesRu(address)) {
            result = result.contains("Россия") ? result : result.concat(" · Россия");
        } else if (isMatchesBy(address)) {
            result = result.contains("Беларусь") ? result : result.concat(" · Беларусь");
        } else if (isMatchesPl(address)) {
            result = result.toLowerCase().contains("Польша") ? result : result.concat(" · Польша");
        } else if (isMatchesWorld(address)) {
            result = result.toLowerCase().contains("foreign") ? result : result.concat(" · foreign");
        }
        return result;
    }
    /*public static String translateToUa(String city) {
        city = city.toLowerCase();
        switch (city) {
            case "киев", "kyiv" -> city = "київ";
            case "днепр", "dnipro" -> city = "дніпро";
            case "харьков", "kharkiv" -> city = "харьків";
            case "одесса", "odesa" -> city = "одеса";
            case "львов", "lviv" -> city = "львів";
            case "николаев", "mykolaiv" -> city = "миколаїв";
            case "винница", "vinnitsia" -> city = "вінниця";
            case "запорожье", "zaporizhzhya" -> city = "запоріжжжя";
            case "черновцы", "chernivtsi" -> city = "чорновці";
            case "чернигов", "chernigiv" -> city = "чернігів";
            case "ивано-франковск", "ivano-frankivsk" -> city = "івано-франківськ";
            case "uzhgorod" -> city = "ужгород";
            case "remote", "удаленно" -> city = "віддалено";
            case "минск", "minsk" -> city = "мінськ";
            case "moskow" -> city = "москва";
            case "sankt-peterburg" -> city = "санкт-петербург";
        }
        return city;
    }*/
    //(как Work)
    /*public static String translateToEn(String city) {
        city = city.toLowerCase();
        switch (city) {
            case "київ", "киев" -> city = "kyiv";
            case "дніпро", "днепр" -> city = "dnipro";
            case "харків", "харьков" -> city = "kharkiv";
            case "одеса", "одесса" -> city = "odesa";
            case "львів", "львов" -> city = "lviv";
            case "миколаїв", "николаев" -> city = "mykolaiv";
            case "вінниця", "винница" -> city = "vinnitsia";
            case "запоріжжя", "запорожье" -> city = "zaporizhzhya";
            case "чорновці", "черновцы" -> city = "chernivtsi";
            case "чернігів", "чернигов" -> city = "chernigiv";
            case "івано-франківськ", "ивано-франковск" -> city = "ivano-frankivsk";
            case "ужгород" -> city = "uzhgorod";
            case "віддалено", "удаленно" -> city = "remote";
            case "минск"-> city = "minsk";
            case "москва"-> city = "moskow";
            case "санкт-петербург"-> city = "sankt-petrburg";
        }
        return city;
    }*/
}
