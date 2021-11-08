package ua.training.top.util.parser.data;

import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.util.parser.data.CommonDataUtil.*;

public class AddressUtil {

    public static String getToAddressWork(String address) {
        return address.contains("·") ? address.substring(address.lastIndexOf("·") + 1).trim() : address;
    }

    public static String formatAddress(String address) {
        address = address.toLowerCase();
        return switch (address) {
            case "remote", "віддалена робота", "віддалено", "удаленно", "no location" -> "remote";
            case "украина", "україна", "ua", "ukraine" -> "Украина";
            case "киев", "київ", "kyiv", "kiev" -> "Киев";
            case "харьков", "харків", "kharkiv" -> "Харьков";
            case "одесса", "одеса", "odesa", "odessa" -> "Одесса";
            case "днепр", "дніпро", "dnipro" -> "Днепр";
            case "львів", "lviv" -> "Львов";
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
            case "желтые", "желтыеводы", "жовтіводи" -> "Желтые-Воды";
            case "камянецьподільський", "каменецподольский" -> "Каменец-Подольский";
            case "іванофранківськ", "ivanofrankivsk", "иванофранковск" -> "Ивано-Франковск";
            case "країни", "страны", "foreign", "other" -> "Foreign";
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
            case "philippines" -> "Филипины";
            case "казахстан" -> "Казахстан";
            case "canada" -> "Канада";
            case "города" -> "города";
            case "usa" -> "США";
            default -> isEmpty(address) ? "" : getStartUpper(address);
        };
    }

    public static String getToAddressFormat(String address) {
        if (address.equals("all") || isEmpty(address)) {
            return link;
        }
        List<String> partsAddress = List.of(getReplace(address, wasteAddress, "")
                .toLowerCase().split("[^A-Za-zА-Яа-яҐґЇїІі]"));
        String result = partsAddress.stream()
                .map(AddressUtil::formatAddress)
                .filter(s -> !isEmpty(s)).distinct()
                .collect(Collectors.joining(" · "));
        return isCityUA(address) && !result.contains("Украина") ?
                result.concat(" · Украина") : isCityRu(address) && !result.contains("Россия") ?
                result.concat(" · Россия") : isCityBy(address) && !result.contains("Беларусь") ?
                result.concat(" · Беларусь") : isCityPl(address) && !result.contains("Польша") ?
                result.concat(" · Польша") : result;
    }
}
