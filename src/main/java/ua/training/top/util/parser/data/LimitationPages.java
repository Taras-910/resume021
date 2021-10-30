package ua.training.top.util.parser.data;

import static java.lang.Math.min;
import static ua.training.top.aggregator.installation.InstallationUtil.limitPages;

public class LimitationPages {
    public static int maxPagesGjinni(String city) {
        int pages;
        switch (city){
            case "all", "foreign" -> pages = 33;
            case "remote" -> pages = 31;
            case "украина" -> pages = 23;
            case "киев" -> pages = 12;
            case "санкт-петербург" -> pages = 5;
            case "минск" -> pages = 3;
            case "львов", "харьков" -> pages = 4;
            case "одесса", "днепр" -> pages = 2;
            default  -> pages = 1;
        }
        return min(limitPages, pages);
    }

    public static int maxPagesGrc(String city) {
        int pages;
        switch (city){
            case "казахстан", "беларусь", "all" -> pages = 21;
            case "remote" -> pages = 17;
            case "россия" -> pages = 16;
            case "москва" -> pages = 8;
            case "санкт-петербург" -> pages = 5;
            case "минск" -> pages = 3;
            case "украина", "краснодар" -> pages = 2;
            default  -> pages = 1;
        }
        return min(limitPages, pages);
    }

    public static int maxPagesWork(String city) {
        int pages;
        switch (city){
            case "all", "украина","україна", "ukraine", "ua"  -> pages = 20;
            case "київ", "киев", "kiev", "kyiv" -> pages = 10;
            case "remote" -> pages = 9;
            case "харків", "харьков", "дніпро", "днепр", "одеса", "одесса", "львів", "львов" -> pages = 3;
            default  -> pages = 1;
        }
        return min(limitPages, pages);
    }
}
