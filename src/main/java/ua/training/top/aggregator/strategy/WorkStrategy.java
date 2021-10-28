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
import static ua.training.top.aggregator.installation.InstallationUtil.*;
import static ua.training.top.util.parser.ElementUtil.getResumesWork;
import static ua.training.top.util.parser.data.CorrectWorkplace.*;
import static ua.training.top.util.parser.data.CorrectLevel.*;
import static ua.training.top.util.parser.data.CorrectUrl.getPartUrlWork;
import static ua.training.top.util.parser.data.LimitationPages.maxPagesWork;
import static ua.training.top.util.parser.data.CommonUtil.*;

public class WorkStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(WorkStrategy.class);
    private static final String URL = "https://www.work.ua/ru/resumes%s-%s/?%s%s%s";
// resume ua 150
// https://www.work.ua/ru/resumes-kyiv-java/?employment=76&experience=0&page=2

    protected Document getDocument(String workspace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(URL, getPartUrlWork(workspace), language, workspace.equals("remote") ?
                "employment=76&" : "", level.equals("all") ? "" : "experience=".concat(getLevel(work,level)),
                page.equals("1") ? "" : "&page=".concat(page)));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        Set<ResumeTo> set = new LinkedHashSet<>();
        String city = freshen.getWorkplace();
        if (city.equals("-1")) {
            return new ArrayList<>();
        }
        int page = 1;
        while (true) {
            Document doc = getDocument(getWork(city), freshen.getLanguage(), freshen.getLevel(), valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("card card-hover resume-link card-visited wordwrap");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getResumesWork(elements, freshen));
            if (page < maxPagesWork(city)) page++;
            else break;
        }
        reCall(set.size(), new WorkStrategy());
        return new ArrayList<>(set);
    }
}
