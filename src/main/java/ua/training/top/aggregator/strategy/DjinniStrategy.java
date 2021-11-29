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
import static java.util.List.of;
import static ua.training.top.aggregator.installation.Installation.reCall;
import static ua.training.top.util.parser.ElementUtil.getResumesDjinni;
import static ua.training.top.util.parser.data.DataUtil.*;
import static ua.training.top.util.parser.data.PagesUtil.getMaxPages;
import static ua.training.top.util.parser.data.UrlUtil.getLevel;
import static ua.training.top.util.parser.data.UrlUtil.getPageUrl;
import static ua.training.top.util.parser.data.WorkplaceUtil.getDjinni;

public class DjinniStrategy implements Strategy{
    private final static Logger log = LoggerFactory.getLogger(DjinniStrategy.class);
    public static final String url = "https://djinni.co/developers/?title=%s%s%s%s%s%s";
//    https://djinni.co/developers/?title=Java&location=kyiv&region=ukraine&exp_years=5y&employment=remote&page=3

    protected Document getDocument(String workplace, String language, String page, String level) {
        return DocumentUtil.getDocument(format(url, language, getLocationDjinni(workplace), getRegionDjinni(workplace),
                getLevel(djinni, level), workplace.equals("remote") ? "&employment=remote" : "", getPageUrl(page)));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        String language = getUpperStart(freshen.getLanguage()), workplace = freshen.getWorkplace();
        log.info(get_resume, freshen.getWorkplace(), language);
        Set<ResumeTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, language, String.valueOf(page), freshen.getLevel());
            Elements elements = doc == null ? null : doc.select("span.candidate-header");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getResumesDjinni(elements, freshen));
            if(page < getMaxPages(djinni, workplace)) page++;
            else break;
        }
        reCall(set.size(), new DjinniStrategy());
        return new ArrayList<>(set);
    }

    public static String getRegionDjinni(String workplace) {
        return !isMatch(citiesUA, workplace) || isEquals(workplace, of("all", "санкт-петербург", "remote")) ?
                "" : isMatch(citiesUA, workplace) || isEquals(workplace, of("украина", "україна", "ukraine")) ?
                "&region=ukraine" : isMatch(citiesBY, workplace) ? "&region=belarus" : isMatch(citiesRU, workplace) ?
                "&region=russia" : "&region=other";
    }

    public static String getLocationDjinni(String workplace) {
        return isEquals(workplace, of("all", "украина", "foreign", "remote")) ?
                "" : isMatch(citiesUA, workplace) || workplace.equals("москва") ? "&location=".concat(getDjinni(workplace)) :
                workplace.equals("санкт-петербург") ? "&keywords=санкт-петербург" : "&keywords=".concat(workplace);
    }

    public static String getDjinniAddr(String address) {
        return address.indexOf(" · Будь") > -1 ? address.replaceAll(" · Будь", "") : address;
    }
}
