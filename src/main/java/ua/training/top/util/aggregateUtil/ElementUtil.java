package ua.training.top.util.aggregateUtil;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static ua.training.top.aggregator.Installation.reasonDateLoading;
import static ua.training.top.aggregator.strategy.DjinniStrategy.getDjinniDate;
import static ua.training.top.aggregator.strategy.DjinniStrategy.getInfo;
import static ua.training.top.aggregator.strategy.RecruitStrategy.getRecDate;
import static ua.training.top.aggregator.strategy.WorkStrategy.getInfoWork;
import static ua.training.top.util.InformUtil.error;
import static ua.training.top.util.ResumeUtil.isToValid;
import static ua.training.top.util.aggregateUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregateUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregateUtil.data.DateToUtil.getToLocalDate;
import static ua.training.top.util.aggregateUtil.data.SalaryUtil.getToSalaries;
import static ua.training.top.util.aggregateUtil.data.UrlUtil.getToUrl;
import static ua.training.top.util.xss.XssUtil.xssClear;

public class ElementUtil {
    public static final Logger log = LoggerFactory.getLogger(ElementUtil.class);

    public static List<ResumeTo> getResumesDjinni(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        AtomicInteger i = new AtomicInteger(1);
        elements.forEach(element -> {
            try {
                LocalDate localDate = getToLocalDate(getDjinniDate(xssClear(element.getElementsByTag("b").text())));
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, workBefore, title = getUpperStart(xssClear(element.getElementsByClass("profile").tagName("a").text()));
                    workBefore = getLimitation(xssClear(element.getElementsByClass("pb-3").text()));
                    String[] info = getInfo(element, workBefore);
                    skills = info[2];
                    if (isToValid(freshen, getJoin(title, workBefore, skills))) {
                        Integer[] salaries = getToSalaries(xssClear(element.getElementsByAttributeValueStarting("class", "order-2").text()));
                        ResumeTo r = new ResumeTo();
                        r.setTitle(title);
                        r.setName(link);
                        r.setAge(link);
                        r.setAddress(info[0]);
                        r.setSalary(Math.max(salaries[0], salaries[1]));
                        r.setWorkBefore(info[1]);
                        r.setUrl(getToUrl(djinni, xssClear(element.getElementsByTag("a").attr("href"))));
                        r.setSkills(skills);
                        r.setReleaseDate(localDate);
                        list.add(r);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        });
        return list;
    }

    public static List<ResumeTo> getResumesRecruit(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        elements.forEach(element -> {
            try {
                LocalDate localDate = getToLocalDate(getRecDate(xssClear(element.getElementsByClass("with-manage-icon").text())));
                if (localDate.isAfter(reasonDateLoading)) {
                    String address, skills, exp, lang, workBefore, title = getUpperStart(xssClear(element.getElementsByClass("candidate-title").text()));
                    workBefore = getLimitation(xssClear(element.getElementsByClass("candidate-info__workplaces").text()));
                    address = xssClear(element.getElementsByClass("candidate-info__cities").text().replace("— ", ""));
                    exp = xssClear(element.getElementsByClass("candidate-info__exp").text());
                    lang = xssClear(element.getElementsByClass("candidate-info__lang").text());
                    skills = xssClear(element.getElementsByClass("vacancy-tags-list").text());
                    if (isToValid(freshen, getJoin(title, workBefore, skills))) {
                        Integer[] salaries = getToSalaries(xssClear(element.getElementsByClass("candidate-info__salary").tagName("span").text()));
                        ResumeTo r = new ResumeTo();
                        r.setTitle(title);
                        r.setName(link);
                        r.setAge(link);
                        r.setAddress(address);
                        r.setSalary(Math.max(salaries[0], salaries[1]));
                        r.setWorkBefore(getJoin(exp, " ", workBefore));
                        r.setUrl(getToUrl(recruit, xssClear(element.getElementsByTag("a").attr("href"))));
                        r.setSkills(getJoin("Knowledge ", lang, ", ", skills));
                        r.setReleaseDate(localDate);
                        list.add(r);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        });
        return list;
    }

    public static List<ResumeTo> getResumesWork(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        int i = 1;
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByAttributeValueEnding("class", "row").text()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String workBefore, skills, age, title = getUpperStart(xssClear(element.getElementsByTag("a").first().text()));
                    skills = getLimitation(xssClear(element.getElementsByClass("add-bottom").tagName("div").text()));
                    workBefore = xssClear(element.getElementsByTag("ul").text());
                    String[] info = getInfoWork(element);
                    age = info[0];
                    if (isAgeValid(age) && isToValid(freshen, getJoin(title, workBefore, skills))) {
                        Integer[] salaries = getToSalaries(xssClear(element.getElementsByAttributeValueStarting("class", "nowrap").tagName("span").text()));
                        ResumeTo r = new ResumeTo();
                        r.setTitle(title);
                        r.setName(xssClear(element.getElementsByTag("b").text()));
                        r.setAge(age);
                        r.setAddress(info[1]);
                        r.setSalary(Math.max(salaries[0], salaries[1]));
                        r.setWorkBefore(getLimitation(workBefore));
                        r.setUrl(getToUrl(work, xssClear(element.getElementsByTag("a").attr("href"))));
                        r.setSkills(skills);
                        r.setReleaseDate(localDate);
                        list.add(r);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }
}
// djinni
/*
                        System.out.println(i.getAndIncrement() + "-".repeat(70));
//                        final String delim = "";
//                        Elements elementList = element.getElementsByTag("span");
//                        System.out.print("elementList: "+ elementList.size()+" \n");
//                        String info = elementList.stream()
//                                .map(Element::ownText)
//                                .distinct()
//                                .peek(e -> {
//                                    if (e.matches("місяц|рік|роки|років|досвід"))
//                                        delim[0] = e;
//                                })
//                                .filter(e -> !e.isEmpty() && e.length() > 1 && !e.equals("Написати"))
//                                .peek(e -> System.out.println("*"+ e + "*"))
//                                .collect(Collectors.joining(", "));
//
//                        System.out.println("info=" + info);
//                        System.out.println("delim=" + delim[0]);

                        System.out.println(".".repeat(30));

//                        System.out.println("dateString=      "+ element.getElementsByTag("b").text());
                        System.out.println("date=            "+r.getReleaseDate());
//                        System.out.println("titleString=     "+ xssClear(element.getElementsByClass("profile").tagName("a").text()));
                        System.out.println("title=           "+r.getTitle());

//                        System.out.println("nameString=      "+ link);
                        System.out.println("name=            "+r.getName());
//                        System.out.println("ageString=       "+ link);
                        System.out.println("age=             "+r.getAge());

//                        System.out.println("addressString =  "+ xssClear(element.getElementsByTag("span").first().nextElementSibling().text()));
//                        System.out.println("addressString1=  "+ xssClear(element.getElementsByTag("span").first().text()));
                    //    System.out.println("addressString2=  "+ xssClear(element.getElementsByTag("span").next().text()));
                    //    System.out.println("addressString3=  "+ xssClear(element.getElementsByTag("span").next().first().text()));
                        System.out.println("address=         "+r.getAddress());
//                        System.out.println("salaryString=    "+ xssClear(element.getElementsByAttributeValueStarting("class", "order-2").text()));
                        System.out.println("salary=          "+r.getSalary());

//                        System.out.println("workBeforeString="+ xssClear(element.getElementsByClass("pb-3").text()));
                        System.out.println("workBefore=      "+r.getWorkBefore());
//                        System.out.println("urlString=       "+ xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url=             "+r.getUrl());
//                        System.out.println("skillsString=    "+ xssClear(element.getElementsByClass("badge").text()));
                        System.out.println("skills=          "+r.getSkills());


*/
//recruit
/*
                        System.out.println(i.getAndIncrement() + "-".repeat(70));
                        Elements elementList = element.getElementsByClass("vacancy-tags-list");
                        System.out.print("elementList: "+ elementList.size()+" \n");
                        String info = elementList.stream()
                                .map(Element::ownText)
                                .distinct()

                                .filter(e -> !e.isEmpty() && e.length() > 1 && !e.equals("Написати"))
                                .peek(e -> System.out.println("*"+ e + "*"))
                                .collect(Collectors.joining(", "));

                        System.out.println("info=" + info);

                        System.out.println(".".repeat(30));

//                        System.out.println("dateString1=      "+ xssClear(element.getElementsByClass("with-manage-icon").text()));
//                        System.out.println("date=            "+r.getReleaseDate());
//                        System.out.println("titleString=     "+ xssClear(element.getElementsByClass("candidate-title").tagName("a").text()));
//                        System.out.println("title=           "+r.getTitle());

//                        System.out.println("nameString=      "+ link);
//                        System.out.println("name=            "+r.getName());
//                        System.out.println("ageString=       "+ link);
//                        System.out.println("age=             "+r.getAge());

//                        System.out.println("addressString =  "+ xssClear(element.getElementsByClass("candidate-info__cities").text()));
//                        System.out.println("address=         "+r.getAddress());

//                        System.out.println("salaryString =    "+ xssClear(element.getElementsByClass("candidate-info__salary").tagName("span").text()));
//                        System.out.println("salary       =          "+r.getSalary());
//
//                        System.out.println("workBeforeString="+ xssClear(element.getElementsByClass("candidate-info__workplaces").text()));
//                        System.out.println("workBefore=      "+r.getWorkBefore());
//                        System.out.println("urlString=       "+ xssClear(element.getElementsByTag("a").attr("href")));
//                        System.out.println("url=             "+r.getUrl());
//                        System.out.println("skillsString=    "+ xssClear(element.getElementsByClass("vacancy-tags-list").text()));
//                        System.out.println("skills=          "+r.getSkills());

*/
// work
/*

                        System.out.println(i++ + "-".repeat(70));
                        List<String> elementList = getListInfo(element);
                        System.out.print("elementList: "+ elementList.size()+" \n");
                        elementList.forEach(e -> System.out.println(" ::: "+ e + " ::: "));
                        System.out.println(".".repeat(70));

                        System.out.println("dateString="+ element.getElementsByAttributeValueEnding("class", "row").text());
                        System.out.println("date="+r.getReleaseDate());
                        System.out.println("titleString="+ xssClear(element.getElementsByTag("a").first().text()));
                        System.out.println("title="+r.getTitle());

                        System.out.println("nameString="+ xssClear(element.getElementsByTag("b").text()));
                        System.out.println("name="+r.getName());

                        System.out.println("age="+r.getAge());

                        System.out.println("address="+r.getAddress());

                        System.out.println("salaryString="+ xssClear(element.getElementsByAttributeValueStarting("class", "nowrap").tagName("span").text()));
                        System.out.println("salary="+r.getSalary());

                        System.out.println("workBeforeString="+ xssClear(element.getElementsByTag("ul").text()));
                        System.out.println("workBefore="+r.getWorkBefore());

                        System.out.println("urlString="+ xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url="+r.getUrl());
                        System.out.println("skillsString="+ xssClear(element.getElementsByClass("add-bottom").tagName("div").text()));
                        System.out.println("skills="+r.getSkills());

    private static List<String> getListInfo(Element element) {
        return element.getElementsByTag("span").next().stream()
                .map(Element::ownText)
                .distinct()
                .filter(e -> !e.isEmpty() && e.length() > 1 && !e.matches(".*(PRO|Файл).*"))
                .peek(e -> System.out.println(e))
                .collect(Collectors.toList());

    }



*/
/*
                        System.out.println(i++ + "-".repeat(70));

                        System.out.println("dateString="+ element.getElementsByAttributeValueEnding("class", "pull-right").text());
                        System.out.println("date="+rTo.getReleaseDate());
                        System.out.println("titleString="+ xssClear(element.getElementsByTag("a").first().text()));
                        System.out.println("title="+rTo.getTitle());
                        System.out.println("nameString="+ xssClear(element.getElementsByTag("b").text()));
                        System.out.println("name="+rTo.getName());
                        System.out.println("ageString="+ xssClear(element.getElementsByAttributeValueContaining("data-toggle", "popover").next().next().text()));
                        System.out.println("age="+rTo.getAge());
                        System.out.println("addressString="+ xssClear(element.getElementsByAttributeValueContaining("class", "add-bottom").prev().text()));
                        System.out.println("address="+rTo.getAddress());
                        System.out.println("salaryString="+ xssClear(element.getElementsByAttributeValueStarting("class", "nowrap").tagName("span").text()));
                        System.out.println("salary="+rTo.getSalary());

                        System.out.println("workBeforeString="+ xssClear(element.getElementsByTag("ul").text()));
                        System.out.println("workBefore="+rTo.getWorkBefore());
                        System.out.println("urlString="+ xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url="+rTo.getUrl());
                        System.out.println("skillsString="+ xssClear(element.getElementsByClass("add-bottom").tagName("div").text()));
                        System.out.println("skills="+rTo.getSkills());
*/
