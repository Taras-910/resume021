package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;

import static org.springframework.util.StringUtils.hasText;

public class CorrectTitle {
    public static final Logger log = LoggerFactory.getLogger(CorrectTitle.class);
    public static String getCorrectTitle(String title) {
        if (!hasText(title)) {
            log.error("there is title is null: see the card on the link");
            return "see the card";
        }
        title = title.contains("(ID") ? title.substring(0, title.indexOf("(ID")).trim() : title;
        title = title.length() < 2 ? title : title.substring(0, 1).toUpperCase().concat(title.substring(1));
        return title.contains("Java Script") ? title.replaceAll("Java Script", "JavaScript") : title;
    }
}
