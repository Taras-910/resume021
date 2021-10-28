package ua.training.top.util.xss;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.util.StringUtils;

public class XssUtil {

    public static String xssClear(String unsafe) {
        return StringUtils.hasText(unsafe) ? Jsoup.clean(unsafe, Whitelist.basic()) : unsafe; }
}

// safe from xss
        /*String unsafe =
                "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
        String safe = Jsoup.clean(unsafe, Whitelist.basic());
        // now: <p><a href="http://example.com/" rel="nofollow">Link</a></p>
        System.out.println(safe);*/

