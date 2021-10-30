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
import static ua.training.top.aggregator.installation.InstallationUtil.limitPages;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getResumesHabr;
import static ua.training.top.util.parser.data.CommonUtil.error_message;
import static ua.training.top.util.parser.data.CommonUtil.habr;
import static ua.training.top.util.parser.data.CorrectLevel.getLevel;
import static ua.training.top.util.parser.data.CorrectUrl.getPartUrlsHabr;
import static ua.training.top.util.parser.data.CorrectWorkplace.getHabr;

public class HabrStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(HabrStrategy.class);
    public static final int maxPages = 2;
    private static final String URL = "https://career.habr.com/resumes?%scurrency=USD&order=last_visited%s%s%s%s%s";

    protected Document getDocument(String workplace, String language, String level, String page) {
        String city = workplace.equals("all") ? "" : "city_ids[]=".concat(workplace).concat("&");
        return DocumentUtil.getDocument(format(URL, city, page.equals("1") ? "" : "&page=".concat(page),
                "&q=".concat(language), getLevel(habr ,level), workplace.equals("remote") ? "&remote=true" : "",
                getPartUrlsHabr(language)));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) {
        Set<ResumeTo> set = new LinkedHashSet<>();
        String workplace = getHabr(freshen.getWorkplace());
        if((workplace).equals("-1")){
            return new ArrayList<>();
        }
        try {
            int page = 1;
            while (true) {
                Document doc = getDocument(workplace, freshen.getLanguage(), freshen.getLevel(), String.valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("resume-card");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getResumesHabr(elements, freshen));
                if(page < Math.min(limitPages, maxPages)) page++;
                else break;
            }
        } catch (Exception e) {
            log.info("{} {} {}", this.getClass().getSimpleName(), error_message, e.getMessage());
            return new ArrayList<>(set);
        }
        reCall(set.size(), new HabrStrategy());
        return new ArrayList<>(set);
    }
}
