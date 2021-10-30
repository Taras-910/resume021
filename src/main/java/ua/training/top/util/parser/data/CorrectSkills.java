package ua.training.top.util.parser.data;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.parser.data.CommonUtil.*;

public class CorrectSkills {
    public static final Logger log = LoggerFactory.getLogger(CorrectSkills.class);

    public static String getCorrectSkills(String skills) {
        if (isEmpty(skills)) {
            log.error("skills value is null");
            return "";
        }
        skills = correctJava_Script("Java Script");
        return skills.contains("Experience level:") ? skills.substring(skills.indexOf("Experience level:")) : skills;
    }

    public static String getSkillsDjinni(String title, String profile, Element element) {
        String skills = profile.contains("$") ? profile.split("· \\$")[1] : profile;
        if(element.nextElementSiblings().size() < 1) {
            return title.concat(" · ").concat(skills.substring(skills.indexOf("·") + 1).trim());
        }
        String text = element.nextElementSibling().nextElementSibling().ownText();
        return getLimitation(text);
    }

    public static String getSkillsHabr(String skills) {
        skills = skills.contains(" ") ? skills.substring(skills.indexOf(" ")) : skills;
        return getCorrectSkills(getLimitation(skills));
    }
}
