package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.DataUtil.*;

public class UrlUtil {

    public static String getToUrl(String site, String url){
        String prefix = switch (site) {
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

    public static String getLevel(String site, String level) {
        return switch (level) {
            case trainee -> switch (site) {
                case grc -> "&employment=probation";
                case habr -> "&qid=1";
                case djinni -> "&exp_years=0y";
                case rabota -> "%5B%220%22%5D";
                case work -> "0&";
                default -> "";
            };
            case junior -> switch (site) {
                case grc -> "&experience=noExperience";
                case habr -> "&qid=3";
                case djinni -> "&exp_years=1y";
                case rabota -> "%5B%221%22%5D";
                case work -> "1&";
                default -> "";
            };
            case middle -> switch (site) {
                case grc -> "&experience=between1And3";
                case habr -> "&qid=4";
                case djinni -> "&exp_years=2y";
                case rabota -> "%5B%222%22%2C%223%22%2C%224%22%2C%225%22%5D";
                case work -> "164+165+166&";
                default -> "";
            };
            case senior -> switch (site) {
                case grc -> "&experience=between3And6";
                case habr -> "&qid=5";
                case djinni -> "&exp_years=3y";
                case rabota -> "%5B%223%22%5D";
                case work -> "165&";
                default -> "";
            };
            case expert -> switch (site) {
                case grc -> "&experience=experience=moreThan6";
                case habr -> "&qid=6";
                case djinni -> "&exp_years=5y";
                case rabota -> "%5B%224%22%2C%225%22%5D";
                case work -> "166&";
                default -> "";
            };
            default -> switch (site) {
                case rabota -> "%5B%222%22%2C%223%22%2C%224%22%2C%225%22%5D";
                case work -> "0+1+164+165+166&";
                default -> "";
            };
        };
    }
}
