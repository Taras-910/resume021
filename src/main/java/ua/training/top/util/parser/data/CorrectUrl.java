package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.CommonUtil.*;

public class CorrectUrl {

    public static String getPartUrlWork(String workplace) {
        return workplace.equals("all") ||workplace.equals("remote") || workplace.equals("ua") ? "" : "-" .concat(workplace);
    }

    public static String getCorrectUrlYandex(String url) {
        url = url.replaceAll("amp;", "");
        return url;
    }

    public static String getCorrectUrl(String text, String url){
        String prefix = switch (text) {
            case work -> "https://www.work.ua";
            case rabota -> "https://rabota.ua";
            case grc -> "https://grc.ua";
            case habr -> "https://career.habr.com";
            case djinni -> "https://djinni.co";
            default -> link;
        };
        StringBuilder sb = new StringBuilder(prefix);
        return sb.append(url).toString();
    }

    public static String skillsHabr(String language) {
        String skills = switch (language) {
            case "php" -> "1005";
            case "ruby" -> "1081";
            case "javascript" -> "264";
            case "kotlin" -> "239";
            case "c#" -> "706";
            case "typescript" -> "245";
            case "c++" -> "172";
            default -> "1012";
        };
        return "&skills[]=".concat(skills);
    }
}
