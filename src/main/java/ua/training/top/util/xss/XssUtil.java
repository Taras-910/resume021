package ua.training.top.util.xss;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.util.StringUtils;

public class XssUtil {

    public static String xssClear(String unsafe) {
        return StringUtils.hasText(unsafe) ? Jsoup.clean(unsafe, Whitelist.basic()) : unsafe; }
}
