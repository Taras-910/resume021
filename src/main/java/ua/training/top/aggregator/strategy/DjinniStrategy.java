package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.aggregateUtil.DocumentUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.List.of;
import static ua.training.top.aggregator.Installation.reCall;
import static ua.training.top.util.InformUtil.get_resume;
import static ua.training.top.util.aggregateUtil.ElementUtil.getResumesDjinni;
import static ua.training.top.util.aggregateUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregateUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregateUtil.data.PageUtil.getMaxPages;
import static ua.training.top.util.aggregateUtil.data.PageUtil.getPage;
import static ua.training.top.util.aggregateUtil.data.WorkplaceUtil.getUA_en;

public class DjinniStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(DjinniStrategy.class);
    public static final String url = "https://djinni.co/developers/%s%s%s%s%s%s";
    //https://djinni.co/developers/?%s%s%s%s%s

    protected Document getDocument(String workplace, String language, String page, String level) {
        return DocumentUtil.getDocument(format(url,
                workplace.equals("all") && language.equals("all") && page.equals("1") && level.equals("all") ? "" : "?",
                getRegion(workplace),
                language.equals("all") ? "" : getJoin(workplace.equals("all") ? "" : "&", "title=", getUpperStart(language)),
                level.equals("all") ? "" : getJoin(language.equals("all") && workplace.equals("all") ? "" : "&", "keywords=", level, "&options=full_text" ),
                workplace.equals("all") || !isMatch(citiesUA, workplace) ? "" : getJoin("&location=", getUA_en(workplace).toLowerCase()),
                getPage(djinni, page)
               ));
    }

    @Override
    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        String language = freshen.getLanguage(), workplace = freshen.getWorkplace(), level=freshen.getLevel();
        log.info(get_resume, language, level, workplace);
        Set<ResumeTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, language, String.valueOf(page), freshen.getLevel());
            Elements elements = doc == null ? null : doc.getElementsByClass("card");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getResumesDjinni(elements, freshen));
            if (page < getMaxPages(djinni, workplace)) page++;
            else break;
        }
        reCall(set.size(), new DjinniStrategy());
        return new ArrayList<>(set);
    }

    private String getRegion(String workplace) {
        return (workplace.equals("all") ? "" :
                workplace.equals("remote") ? "employment=remote" :
                getJoin("region=", isMatches(of(uaAria, citiesUA), workplace) ?
                        "UKR" : isMatches(of(plAria, citiesPl), workplace) ?
                        "POL" : isMatches(of(deAria, citiesDe), workplace) ?
                        "DEU" : isMatch(foreignAria, workplace) ? "eu" : "other"));
    }

    public static String getDjinniDate(String date) {
        date = isContains(date,"хв") ? date.replaceAll("хв", " хв") :
                isContains(date,"год") ? date.replaceAll("год", " год") :
                        isContains(date,"Онлайн") ? date.replaceAll("Онлайн", "1 хв") : date;
        return date;
    }

    public static String[] getInfo(Element element, String workBefore) {
        List<String> info = getListInfo(element);
        StringBuilder sbAddress = new StringBuilder();
        StringBuilder sbSkills = new StringBuilder();
        boolean addr = true;
        for(int j = 0; j < info.size(); j++) {
            String e = info.get(j);
//            log.info("*{}*", e);
            if (e.matches(".*(місяц|рік|роки|років|досвід).*")){
                addr = false;
                workBefore = getJoin(e, ". ", workBefore);
                continue;
            }
            if (addr) {
                sbAddress.append(sbAddress.isEmpty() ? "" : ", ").append(e);
            } else {
                sbSkills.append(sbSkills.isEmpty() ? "" : ", ").append(e);
            }
        }
        return new String[]{sbAddress.toString(), workBefore, getJoin("English knowledge: ", sbSkills.toString())};
    }

    public static List<String> getListInfo(Element element) {
        return element.getElementsByTag("span").stream()
                .map(Element::ownText)
                .distinct()
                .filter(e -> !e.isEmpty() && e.length() > 1 && !e.matches(".*(Написати|Опубліковано).*"))
                .collect(Collectors.toList());
    }
}
