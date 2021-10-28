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
import static ua.training.top.aggregator.installation.InstallationUtil.*;
import static ua.training.top.util.parser.ElementUtil.getResumesDjinni;
import static ua.training.top.util.parser.data.CommonUtil.djinni;
import static ua.training.top.util.parser.data.CommonUtil.getStartUpper;
import static ua.training.top.util.parser.data.CorrectLevel.*;
import static ua.training.top.util.parser.data.CorrectWorkplace.*;
import static ua.training.top.util.parser.data.LimitationPages.maxPagesGjinni;

public class DjinniStrategy implements Strategy{
    private final static Logger log = LoggerFactory.getLogger(DjinniStrategy.class);
    public static final String REMOTE = "&employment=remote";
    private static final String URL_FORMAT = "https://djinni.co/developers/?title=%s%s%s%s%s%s";
//    https://djinni.co/developers/?title=Java&location=kyiv&region=ukraine&exp_years=5y&employment=remote&page=3

    protected Document getDocument(String workplace, String language, String page, String level) {
        return DocumentUtil.getDocument(format(URL_FORMAT, language, getLocationDjinni(workplace),
                getRegionDjinni(workplace), getLevel(djinni, level),
                workplace.equals("remote") ? REMOTE : "", page.equals("1") ? "" : "&page=".concat(page)));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        String language = getStartUpper(freshen.getLanguage());
        String city = freshen.getWorkplace();
        log.info("getVacancies city={} language={}", freshen.getWorkplace(), language);
        Set<ResumeTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(city, language, String.valueOf(page), freshen.getLevel());
            Elements elements = doc == null ? null : doc.select("span.candidate-header");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getResumesDjinni(elements, freshen));
            if(page < maxPagesGjinni(city)) page++;

            else break;
        }
        reCall(set.size(), new DjinniStrategy());
        return new ArrayList<>(set);
    }
}
