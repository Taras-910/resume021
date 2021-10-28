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
import static ua.training.top.aggregator.installation.InstallationUtil.limitCallPages;
import static ua.training.top.util.parser.ElementUtil.getResumesYandex;
import static ua.training.top.util.parser.data.CommonUtil.yandex;
import static ua.training.top.util.parser.data.CorrectWorkplace.getYandex;
import static ua.training.top.util.parser.data.CorrectLevel.*;

public class YandexStrategy implements Strategy{
    private final static Logger log = LoggerFactory.getLogger(YandexStrategy.class);
    public static final int maxPages = 5;
    private static final String URL = "https://rabota.yandex.ru/%s/vakansii%s/?text=%s%s%s&top_days=7%s";

// resume ru 53 https://o.yandex.ru/rossiya/rabota/it-internet-svyaz-telekom/
    protected Document getDocument(String city, String language, String page, String level) {
        return DocumentUtil.getDocument(format(URL, city.equals("remote") ? "ukraina" : city,
                city.equals("remote") ? "/rabota-udalennaya-i-na-domu" : "", language,
                level.equals("trainee") ? "%20intern" : "",
                page.equals("1") ? "" : "&page_num=".concat(page), getLevel(yandex, level)));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        Set<ResumeTo> set = new LinkedHashSet<>();
        String workplace = getYandex(freshen.getWorkplace());
        log.info("workplace={}", workplace);
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, freshen.getLanguage(), String.valueOf(page), freshen.getLevel());
//           log.info("document={}\n", doc);
            Elements elements = doc == null ? null : doc.getElementsByClass("serp-vacancy");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getResumesYandex(elements, freshen));
            if(page < Math.min(limitCallPages, maxPages)) page++;
            else break;
        }
//        reCall(set.size(), new YandexStrategy());
        return new ArrayList<>(set);
    }
}
