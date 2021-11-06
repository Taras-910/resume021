package ua.training.top.util.parser.data;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.parser.data.CommonDataUtil.*;

public class SkillsUtil {
    public static final Logger log = LoggerFactory.getLogger(SkillsUtil.class);

    public static String getCorrectSkills(String skills) {
        getLinkIfEmpty(skills);
        return getLimitation(skills.replaceAll("Java Script", "JavaScript").trim(), limitText);
    }

    public static String getSkillsDjinni(String title, String profile, Element element) {
        String skills = profile.contains("$") ? profile.split("· \\$")[1] : profile;
        if(element.nextElementSiblings().size() < 1) {
            return title.concat(" · ").concat(skills.substring(skills.indexOf("·") + 1).trim());
        }
        return getCorrectSkills(element.nextElementSibling().nextElementSibling().ownText());
    }

    public static String getSkillsHabr(String skills) {
        skills = skills.contains(" ") ? skills.substring(skills.indexOf(" ")) : skills;
        return getCorrectSkills(skills);
    }
}
