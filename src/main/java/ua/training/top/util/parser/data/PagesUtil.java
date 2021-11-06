package ua.training.top.util.parser.data;

import static java.lang.Math.min;
import static ua.training.top.aggregator.installation.InstallationUtil.limitPages;
import static ua.training.top.util.parser.data.CommonDataUtil.*;

public class PagesUtil {

    public static int getMaxPages(String site, String city) {
        int pages;
        pages = switch (city) {
            case "all" -> switch (site) {
                case djinni, grc -> 49;
                case rabota -> 6;
                case work -> 15;
                default -> 1;
            };
            case "foreign" -> switch (site) {
                case djinni -> 49;
                case grc -> 2;
                case habr -> 0;
                default -> 1;
            };
            case "remote" -> switch (site) {
                case grc -> 49;
                case rabota -> 3;
                case linkedin -> 5;
                case work -> 12;
                default -> 1;
            };
            case "украина" -> switch (site) {
                case djinni -> 32;
                case grc -> 4;
                case rabota -> 6;
                case linkedin -> 5;
                case work -> 30;
                default -> 1;
            };
            case "киев" -> switch (site) {
                case djinni, work  -> 15;
                case rabota -> 3;
                default -> 1;
            };
            case "санкт-петербург" -> switch (site) {
                case djinni, grc -> 5;
                case habr -> 2;
                default -> 1;
            };
            case "одесса" -> switch (site) {
                case djinni -> 3;
                case rabota -> 2;
                case work -> 4;
                default -> 1;
            };
            case "харьков" -> switch (site) {
                case djinni, work -> 5;
                case rabota -> 2;
                default -> 1;
            };
            case "минск" -> switch (site) {
                case grc -> 10;
                case rabota -> 6;
                default -> 1;
            };
            case "москва" -> switch (site) {
                case grc -> 34;
                default -> 1;
            };
            case "львов" -> switch (site) {
                case djinni -> 6;
                case rabota -> 8;
                case work -> 2;
                default -> 1;
            };
            default -> 1;
        };
        return min(limitPages, pages);
    }
}
