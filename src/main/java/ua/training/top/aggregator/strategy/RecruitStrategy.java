package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.parser.DocumentUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.Installation.reCall;
import static ua.training.top.util.InformUtil.get_resume;
import static ua.training.top.util.parser.ElementUtil.getResumesRecruit;
import static ua.training.top.util.parser.data.CommonUtil.*;
import static ua.training.top.util.parser.data.ConstantsUtil.recruit;
import static ua.training.top.util.parser.data.LevelUtil.getLevel;
import static ua.training.top.util.parser.data.PageUtil.getMaxPages;
import static ua.training.top.util.parser.data.PageUtil.getPage;
import static ua.training.top.util.parser.data.WorkplaceUtil.getRec;

public class RecruitStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(RecruitStrategy.class);
    public static final String url = "https://recruitika.com/candidates/%s%s%s%s";
    //https://recruitika.com/candidates/page/2/?tags=java-developer&cities=kyiv&experience=1
    //https://recruitika.com/candidates/%s%s%s%s

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(url,
                getPage(recruit, page),
                language.equals("all") ? "" : getJoin("?tags=", getLanguage(language)),
                workplace.equals("all") ? "" : getJoin(language.equals("all") ? "?" : "&", getJoin("cities=", getRec(workplace))),
                level.equals("all") ? "" :
                        getJoin(language.equals("all") && workplace.equals("all") ? "?" : "&", getLevel(recruit, level))));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_resume, language, level, workplace);
        Set<ResumeTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, language, level, String.valueOf(page));
            Elements elements = doc == null ?
                    null : doc.getElementsByClass("candidate-row-outer"); //vacancy-row candidate-row
            if (elements == null || elements.size() == 0) break;
            set.addAll(getResumesRecruit(elements, freshen));
            if (page < getMaxPages(recruit, workplace)) page++;
            else break;
        }
        reCall(set.size(), new RecruitStrategy());
        return new ArrayList<>(set);
    }

    private String getLanguage(String language) {
        return isMatch(List.of("php", "java"), language) ? getJoin(language, "-developer") :
                isMatch(List.of("c", "c++"), language) ? "c—c" : language;
    }

    public static String getRecDate(String date) {
        String[] temp;
        if(isContains(date, ":")) {
            temp = date.split(":");
            return (LocalDateTime.now().minusHours(Integer.parseInt(temp[0])).minusMinutes(Integer.parseInt(temp[1]))).toLocalDate().toString();
        }
        if(isContains(date, ".")) {
            temp = date.split("\\.");
            return getJoin("20", temp[2], "-", temp[1], "-", temp[0]);
        }
        return "1";
    }
}
