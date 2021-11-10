package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.DataUtil.*;

public class UrlUtil {

    public static String getToUrl(String text, String url){
        String prefix = switch (text) {
            case work -> "https://www.work.ua";
            case rabota -> "https://rabota.ua";
            case grc -> "https://grc.ua";
            case habr -> "https://career.habr.com";
            case djinni -> "https://djinni.co";
            default -> link;
        };
        return prefix + url;
    }

    public static String getPageUrl(String page) {
        return page.equals("1") ? "" : "&page=".concat(page);
    }
}
