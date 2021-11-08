package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.parser.DocumentUtil;

import java.io.IOException;
import java.util.*;

import static java.lang.String.format;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.data.CommonDataUtil.get_resume;
import static ua.training.top.util.parser.data.CommonDataUtil.linkedin;
import static ua.training.top.util.parser.data.PagesUtil.getMaxPages;
import static ua.training.top.util.parser.data.UrlUtil.getPageUrl;
import static ua.training.top.util.parser.data.WorkplaceUtil.getLinkedin;

public class LinkedinStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(LinkedinStrategy.class);
    public static final Random random = new Random();
    private static final String url = "https://www.linkedin.com/search/results/people/?keywords=resume%s%s&origin=CLUSTER_EXPANSION%s%s";
//               https://www.linkedin.com/search/results/people/?keywords=resume%20java%20remote&origin=CLUSTER_EXPANSION&page=4&sid=XeP

//https://www.linkedin.com/search/results/people/?keywords=resume%s%s&origin=CLUSTER_EXPANSION%s&sid=%s
//    kiev 4. ukraine 5. all 100x10
//
//kiev 4. ukraine 5. all 100x10
//
//language (%20 + language)
//workplace(remote:  %20remote     workplace , all, foreign: “” )
//page(1: «»  2:« &page=»page)
//part (&sid=   random 40 - 126)

    protected Document getDocument(String city, String language, String page, String part) {
        return DocumentUtil.getDocument(format(url, "%20".concat(language), city.equals("remote") ?
                "%20remote" : "", getPageUrl(page), "&sid=".concat(part)));
    }

    public static String getPart() {
        List<String> list = List.of("rcG", "CxC", "z8~", "ck~", "XeP" );
        return list.get(random.nextInt(5));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        String city = freshen.getWorkplace(), language = freshen.getLanguage();
        log.info(get_resume, city, language);
        String part = getPart();
        Set<ResumeTo> set = new LinkedHashSet<>();
            int page = 1;
            while(true) {
                Document doc = getDocument(getLinkedin(city), language, String.valueOf(page), part);
                Elements elements = doc == null ? null : doc.select("div.entity-result ");

                System.out.println("////////////////////////////////////////////////////////////////////////////////");
                System.out.println("doc="+doc);
                System.out.println("elements="+elements.size());

                if (elements == null || elements.size() == 0) break;
//                set.addAll(getResumesLinkedin(elements, freshen));
                if(page < getMaxPages(linkedin, city)) page++;
                else break;
            }
        reCall(set.size(), new LinkedinStrategy());
        return new ArrayList<>(set);
    }
}
