package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.parser.DocumentUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.installation.Installation.reCall;
import static ua.training.top.util.parser.ElementUtil.getResumesHabr;
import static ua.training.top.util.parser.data.DataUtil.*;
import static ua.training.top.util.parser.data.PagesUtil.getMaxPages;
import static ua.training.top.util.parser.data.UrlUtil.getLevel;
import static ua.training.top.util.parser.data.UrlUtil.getPageUrl;
import static ua.training.top.util.parser.data.WorkplaceUtil.getHabr;

public class HabrStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(HabrStrategy.class);
    private static final String url = "https://career.habr.com/resumes?%scurrency=USD&order=last_visited%s%s%s%s%s";

    protected Document getDocument(String workplace, String language, String level, String page) {
        String city = workplace.equals("all") ? "" : getBuild("city_ids[]=").append(workplace).append("&").toString();
        return DocumentUtil.getDocument(format(url, city, getPageUrl(page), getBuild("&q=").append(language).toString(),
                getLevel(habr, level), workplace.equals("remote") ? "&remote=true" : "", getPartUrlsHabr(language)));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) {
        String workplace = getHabr(freshen.getWorkplace()), language = freshen.getLanguage();
        Set<ResumeTo> set = new LinkedHashSet<>();
        log.info(get_resume, freshen.getWorkplace(), language);
        if ((workplace).equals("-1")) {
            return new ArrayList<>();
        }
        try {
            int page = 1;
            while (true) {
                Document doc = getDocument(workplace, language, freshen.getLevel(), String.valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("resume-card");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getResumesHabr(elements, freshen));
                if (page < getMaxPages(habr, workplace)) page++;
                else break;
            }
        } catch (Exception e) {
            log.info(error, e.getMessage(), this.getClass().getSimpleName());
            return new ArrayList<>(set);
        }
        reCall(set.size(), new HabrStrategy());
        return new ArrayList<>(set);
    }

    public static String getPartUrlsHabr(String language) {
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
        return getBuild("&skills[]=").append(skills).toString();
    }

    public static String getToSkillsHabr(String skills) {
        skills = skills.contains(" ") ? skills.substring(skills.indexOf(" ")) : skills;
        return getLimitation(skills);
    }

    public static String getToWorkBeforeHabr(String workBefore) {
        workBefore = workBefore.startsWith("От") ? workBefore.substring(workBefore.indexOf("·") + 1) : workBefore;
        return getLimitation(workBefore);
    }
}
