package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.CommonUtil.getJoin;
import static ua.training.top.util.parser.data.ConstantsUtil.*;

public class ToUtil {

    public static String getToUrl(String site, String url){
        String prefix = switch (site) {
            case work -> "https://www.work.ua";
            case rabota -> "https://rabota.ua";
            case djinni -> "https://djinni.co";
            case recruit -> "https://recruitika.com";
            default -> link;
        };
        return getJoin(prefix, url);
    }


}
