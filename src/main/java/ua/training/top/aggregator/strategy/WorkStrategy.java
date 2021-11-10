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
import static java.lang.String.valueOf;
import static ua.training.top.aggregator.installation.Installation.reCall;
import static ua.training.top.aggregator.installation.PagesCallNumber.getMaxPages;
import static ua.training.top.util.parser.ElementUtil.getResumesWork;
import static ua.training.top.util.parser.data.DataUtil.get_resume;
import static ua.training.top.util.parser.data.DataUtil.work;
import static ua.training.top.util.parser.data.UrlUtil.getLevel;
import static ua.training.top.util.parser.data.WorkplaceUtil.getWork;

public class WorkStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(WorkStrategy.class);
    private final static String
            period = "period=5",
            URL = "https://www.work.ua/ru/resumes%s-%s/?%s%s%s%s";
//      https://www.work.ua/ru/resumes-kyiv-java/?employment=76&experience=0&page=2

    protected Document getDocument(String workspace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(URL, getPartUrlWork(getWork(workspace)), language, workspace.equals("remote") ?
                        "employment=76&" : "", level.equals("all") ? "" : "experience=" .concat(getLevel(work, level)),
                page.equals("1") ? period : period.concat("&"), page.equals("1") ? "" : "page=" .concat(page)));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), language = freshen.getLanguage();
        log.info(get_resume, workplace, language);
        Set<ResumeTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, language, freshen.getLevel(), valueOf(page));
            Elements elements = doc == null ?
                    null : doc.getElementsByClass("card card-hover resume-link card-visited wordwrap");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getResumesWork(elements, freshen));
            if (page < getMaxPages(work, workplace)) page++;
            else break;
        }
        reCall(set.size(), new WorkStrategy());
        return new ArrayList<>(set);
    }

    public static String getToAddressWork(String address) {
        return address.contains("·") ? address.substring(address.lastIndexOf("·") + 1).trim() : address;
    }

    public static String getPartUrlWork(String workplace) {
        return workplace.equals("all") ||workplace.equals("remote") || workplace.equals("ua") ? "" : "-" .concat(workplace);
    }
}
