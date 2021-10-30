package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.util.parser.data.CommonUtil.*;

public class CorrectTitle {
    public static final Logger log = LoggerFactory.getLogger(CorrectTitle.class);

    public static String getCorrectTitle(String title) {
        if (!hasText(title)) {
            log.error("title value is null");
            return link;
        }
        title = title.contains("(ID") ? title.substring(0, title.indexOf("(ID")).trim() : correctJava_Script("Java Script");
        return getStartUpper(title);
    }
}
