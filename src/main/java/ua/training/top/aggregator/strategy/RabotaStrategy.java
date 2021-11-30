package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.parser.DocumentUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.installation.Installation.reCall;
import static ua.training.top.util.parser.ElementUtil.getResumesRabota;
import static ua.training.top.util.parser.data.DataUtil.*;
import static ua.training.top.util.parser.data.PagesUtil.getMaxPages;
import static ua.training.top.util.parser.data.UrlUtil.getLevel;
import static ua.training.top.util.parser.data.WorkplaceUtil.getRabota;

public class RabotaStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(RabotaStrategy.class);
    private static final String
            periodPart = "period=%22Year%22",
            remotePart = "&scheduleIds=%5B%223%22%5D",
            URL = "https://rabota.ua/candidates/%s/%s?%s%s%s%s";

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(URL, language, getRabota(workplace), page.equals("1") ? "" :
                getBuild("pg=").append(page).append("&").toString(), periodPart, workplace.equals("remote") ?
                remotePart : "", getBuild("&experienceIds=").append(getLevel(rabota, level)).toString()));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), language = freshen.getLanguage();
        log.info(get_resume, workplace, language);
        if (isMatch(citiesRU, workplace)) {
            return new ArrayList<>();
        }
        Set<ResumeTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, language, freshen.getLevel(), String.valueOf(page));
            Elements elements = doc == null ?
                    null : doc.getElementsByAttributeValueStarting("class", "santa-outline-none");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getResumesRabota(elements, freshen));
            if (page < getMaxPages(rabota, workplace)) page++;
            else break;
        }
        reCall(set.size(), new RabotaStrategy());
        return new ArrayList<>(set);
    }
}
