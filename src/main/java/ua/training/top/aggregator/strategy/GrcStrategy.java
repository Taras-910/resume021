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
import static ua.training.top.util.parser.ElementUtil.getResumesGrc;
import static ua.training.top.util.parser.data.CommonDataUtil.get_resume;
import static ua.training.top.util.parser.data.CommonDataUtil.grc;
import static ua.training.top.util.parser.data.LevelUtil.getLevel;
import static ua.training.top.util.parser.data.PagesUtil.getMaxPages;
import static ua.training.top.util.parser.data.UrlUtil.getPageUrl;
import static ua.training.top.util.parser.data.WorkplaceUtil.getGrc;

public class GrcStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(GrcStrategy.class);
    public static final String
            relocation = "&relocation=living_or_relocation",
            url_foreign = "https://grc.ua/search/resume?text=java&logic=normal&pos=full_text&exp_period=all_time&education=none&order_by=relevance&search_period=365&items_on_page=100&no_magic=true&area=1001&area=27&area=74&area=85&area=199&area=37&area=45&area=38&area=21&area=94&area=65&area=101&area=33&area=108&area=6&area=7&area=236&relocation=living_or_relocation&schedule=remote",
            url = "https://grc.ua/search/resume?clusters=true&exp_period=all_time&items_on_page=100&logic=normal&no_magic=true&order_by=relevance&ored_clusters=true&pos=full_text&text=%s&search_period=365%s%s%s%s%s";
//https://grc.ua/search/resume?clusters=true&exp_period=all_time&items_on_page=100&logic=normal&no_magic=true&order_by=relevance&ored_clusters=true&pos=full_text&text=java&search_period=30&area=2&relocation=living_or_relocation&experience=between1And3&schedule=remote&page=1

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(url, language,
                workplace.equals("&schedule=remote") || workplace.equals("all")? "" : "&area=".concat(workplace),
                workplace.equals("all") ? "" : relocation, level.isEmpty()? "" : level,
                workplace.equals("&schedule=remote") ? workplace : "", getPageUrl(page)));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_resume, workplace, language);
        Set<ResumeTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = workplace.equals("foreign") ? DocumentUtil.getDocument(url_foreign) :
                    getDocument(getGrc(workplace), language, getLevel(grc, level), String.valueOf(page));
            Elements elements = doc == null ?
                    null : doc.getElementsByClass("resume-search-item__content-wrapper");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getResumesGrc(elements, freshen));
            if (page < getMaxPages(grc, workplace)) page++;
            else break;
        }
        return new ArrayList<>(set);
    }
}
