package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.parser.data.CommonDataUtil.getLinkIfEmpty;
import static ua.training.top.util.parser.data.CommonDataUtil.getStartUpper;

public class TitleUtil {
    public static final Logger log = LoggerFactory.getLogger(TitleUtil.class);

    public static String getCorrectTitle(String title) {
         title = title.contains("(ID") ? title.substring(0, title.indexOf("(ID")).trim() : title;
        return formatJavaScript(getStartUpper(getLinkIfEmpty(title)));
    }
    public static String formatJavaScript(String text) {
        return text.replaceAll("Java Script", "JavaScript");
    }
}
