package ua.training.top.util.aggregateUtil.data;

import static java.lang.Math.min;
import static ua.training.top.aggregator.Installation.limitPages;
import static ua.training.top.util.aggregateUtil.data.CommonUtil.getJoin;
import static ua.training.top.util.aggregateUtil.data.ConstantsUtil.*;

public class PageUtil {

    public static String getPage(String site, String page) {
        return switch (site) {
            case djinni -> page.equals("1") ? "" : getJoin("&page=", page);
            case recruit -> page.equals("1") ? "" : getJoin("page/", page, "/");
            case work -> page.equals("1") ? "" : getJoin("page=", page);
            default -> "";
        };
    }

    public static int getMaxPages(String site, String workplace) {
        int pages = switch (workplace) {
            case "all" -> switch (site) {
                case djinni -> 49;
                case work -> 15;
                default -> 1;
            };
            case "foreign" -> switch (site) {
                case djinni -> 49;
                default -> 1;
            };
            case "remote" -> switch (site) {
                case work -> 12;
                default -> 1;
            };
            case "украина" -> switch (site) {
                case djinni -> 32;
                case work -> 30;
                default -> 1;
            };
            case "киев" -> switch (site) {
                case djinni, work  -> 15;
                default -> 1;
            };
            case "одесса" -> switch (site) {
                case djinni -> 3;
                case work -> 4;
                default -> 1;
            };
            case "харьков" -> switch (site) {
                case djinni, work -> 5;
                default -> 1;
            };
            case "минск" -> switch (site) {
                default -> 1;
            };
            case "львов" -> switch (site) {
                case djinni -> 6;
                case work -> 2;
                default -> 1;
            };
            default -> 1;
        };
        return min(limitPages, pages);
    }
}
