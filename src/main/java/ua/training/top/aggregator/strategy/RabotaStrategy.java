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
import static ua.training.top.aggregator.installation.InstallationUtil.limitPages;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getResumesRabota;
import static ua.training.top.util.parser.data.CommonUtil.isMatchesRu;
import static ua.training.top.util.parser.data.CommonUtil.rabota;
import static ua.training.top.util.parser.data.CorrectLevel.getLevel;
import static ua.training.top.util.parser.data.CorrectWorkplace.getRabota;

public class RabotaStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(RabotaStrategy.class);
    public static final int maxPages = 50;
    public static final String part = "period=%22Month%22";
    private static final String URL = "https://rabota.ua/candidates/%s/%s?%s%s%s%s";
//resume ua 5(264)
// https://rabota.ua/candidates/java/вся_украина?pg=2&period=%22Month%22&scheduleIds=%5B%223%22%5D&experienceIds=%5B%222%22%5D

    protected Document getDocument(String city, String language, String level, String page) {

        return DocumentUtil.getDocument(format(URL, language, getRabota(city),
                page.equals("1") ? "":"pg=".concat(page).concat("&"),
                part, city.equals("remote") ? "&scheduleIds=%5B%223%22%5D" : "",
                level.equals("all") ? "" : "&experienceIds=".concat(getLevel(rabota, level))));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        log.info("city={} language={}", freshen.getWorkplace(), freshen.getLanguage());
        String city = freshen.getWorkplace();
        if (isMatchesRu(city)) {
            return new ArrayList<>();
        }
        Set<ResumeTo> set = new LinkedHashSet<>();
        int page = 1;
        while(true) {
            Document doc = getDocument(city, freshen.getLanguage(), freshen.getLevel(), String.valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByAttributeValueStarting("class","santa-outline-none");
            System.out.println("doc page "+ page);
            if (elements == null || elements.size() == 0) break;
            set.addAll(getResumesRabota(elements, freshen));
            if(page < Math.min(limitPages, getRabota(city).equals("украина") ? maxPages : 1)) page++;
            else break;
        }
        reCall(set.size(), new RabotaStrategy());
        return new ArrayList<>(set);
    }
}
