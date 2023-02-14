package ua.training.top.util.aggregateUtil.data;

import static ua.training.top.util.aggregateUtil.data.CommonUtil.getJoin;
import static ua.training.top.util.aggregateUtil.data.ConstantsUtil.*;

public class UrlUtil {

    public static String getToUrl(String site, String url){
        String prefix = switch (site) {
            case work -> "https://www.work.ua";
            case djinni -> "https://djinni.co";
            case recruit -> "https://recruitika.com";
            default -> link;
        };
        return getJoin(prefix, url);
    }
}
