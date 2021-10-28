package ua.training.top.util.parser.data;

import static ua.training.top.aggregator.installation.InstallationUtil.limitCallPages;

public class LimitationPages {
    public static int maxPagesGjinni(String city) {
        int page;
        switch (city){
            case "all", "foreign" -> page = 33;
            case "remote" -> page = 31;
            case "украина" -> page = 23;
            case "киев" -> page = 12;
            case "санкт-петербург" -> page = 5;
            case "минск" -> page = 3;
            case "львов", "харьков" -> page = 4;
            case "одесса", "днепр" -> page = 2;
            default  -> page = 1;
        }
        return Math.min(limitCallPages, page);
    }

    public static int maxPagesGrc(String city) {
        int page;
        switch (city){
            case "казахстан", "беларусь", "all" -> page = 21;
            case "remote" -> page = 17;
            case "россия" -> page = 16;
            case "москва" -> page = 8;
            case "санкт-петербург" -> page = 5;
            case "минск" -> page = 3;
            case "украина", "краснодар" -> page = 2;
            default  -> page = 1;
        }
        return Math.min(limitCallPages, page);
    }

    public static int maxPagesWork(String city) {
        int page;
        switch (city){
            case "all", "украина","україна", "ukraine", "ua"  -> page = 20; //ошибки
            case "київ", "киев", "kiev", "kyiv" -> page = 10; //ошибки
            case "remote" -> page = 9;
            case "харків", "харьков", "дніпро", "днепр", "одеса", "одесса", "львів", "львов" -> page = 3; //ошибки
            default  -> page = 1;
        }
        return Math.min(limitCallPages, page);
    }
}
