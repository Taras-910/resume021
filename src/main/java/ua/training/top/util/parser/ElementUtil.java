package ua.training.top.util.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;
import static ua.training.top.aggregator.installation.InstallationUtil.reasonDateToLoad;
import static ua.training.top.util.parser.data.AddressUtil.getToAddressFormat;
import static ua.training.top.util.parser.data.AddressUtil.getToAddressWork;
import static ua.training.top.util.parser.data.AgeUtil.*;
import static ua.training.top.util.parser.data.CommonDataUtil.*;
import static ua.training.top.util.parser.data.LocalDateUtil.getToLocalDate;
import static ua.training.top.util.parser.data.SkillsUtil.*;
import static ua.training.top.util.parser.data.TitleUtil.getCorrectTitle;
import static ua.training.top.util.parser.data.UrlUtil.getToUrl;
import static ua.training.top.util.parser.data.WorkBeforeUtil.*;
import static ua.training.top.util.parser.salary.SalaryUtil.getToSalary;
import static ua.training.top.util.xss.XssUtil.xssClear;

public class ElementUtil {
    public static final Logger log = LoggerFactory.getLogger(ElementUtil.class);

    public static List<ResumeTo> getResumesDjinni(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        elements.forEach(element -> {
            try {
                LocalDate localDate = getToLocalDate(djinni, xssClear(element.getElementsByTag("small").text().trim()));
                if (localDate.isAfter(reasonDateToLoad)) {
                    String skills, age, workBefore, title = getCorrectTitle(xssClear(element.getElementsByClass("profile").tagName("a").text().trim()));
                    workBefore = getToWorkBefore(xssClear(element.nextElementSibling().ownText()));
                    skills = getSkillsDjinni(title, xssClear(element.getElementsByAttributeValueStarting("class", "tiny-profile-details").text()), element);
                    age = link;
                    if (isToValid(freshen, join(title, workBefore, skills)) && isAgeAfter(age) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo(
                                title,
                                link,
                                age,
                                getToAddressFormat(xssClear(element.getElementsByAttributeValueStarting("class", "tiny-profile-details").text()).split("· \\$")[0]),
                                getToSalary(xssClear(element.getElementsByAttributeValueStarting("class", "profile-details-salary").text().trim())),
                                getLinkIfEmpty(workBefore),
                                getToUrl(djinni, xssClear(element.getElementsByTag("a").attr("href").trim())),
                                getLinkIfEmpty(skills), localDate);
                        list.add(rTo);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        });
        return list;
    }

    public static List<ResumeTo> getResumesGrc(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        elements.forEach(element -> {
            try {
                LocalDate localDate = getToLocalDate(grc, xssClear(element.getElementsByClass("resume-search-item__date").text().trim()));
                if (localDate.isAfter(reasonDateToLoad)) {
                    String workBefore, age, title = getCorrectTitle(xssClear(element.getElementsByClass("resume-search-item__name").text().trim()));
                    workBefore = getToWorkBefore(xssClear(element.getElementsByAttributeValueStarting("data-qa", "resume-serp_resume-item-content").text()));
                    age = getLinkIfEmpty(xssClear(element.getElementsByAttributeValueStarting("data-qa", "resume-serp__resume-age").text()));
                    if (isToValid(freshen, join(title, workBefore)) && isAgeAfter(age) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo(
                                title,
                                link,
                                age,
                                link,
//                                getSalary(getCorrectSalary(xssClear(element.getElementsByAttributeValueStarting("class", "profile-details-salary").text().trim())), null),
                                getToSalary(xssClear(element.getElementsByClass("bloko-text bloko-text_large bloko-text_strong").text().trim())),
                                getLinkIfEmpty(workBefore),
                                getToUrl(grc, xssClear(element.getElementsByClass("resume-search-item__name").attr("href").trim())),
                                link, localDate);
                        list.add(rTo);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        });
        return list;
    }

    public static List<ResumeTo> getResumesHabr(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(habr, xssClear(element.getElementsByClass("basic-date").attr("datetime")));
                if (localDate.isAfter(reasonDateToLoad)) {
                    String age = link, workBefore, skills, title = getCorrectTitle(xssClear(element.getElementsByClass("resume-card__specialization").text()));
                    skills = getSkillsHabr(xssClear(element.getElementsByClass("link-comp link-comp--appearance-dark").text().trim()));
                    workBefore = getToWorkBeforeHabr(xssClear(element.getElementsByClass("inline-list").text()));
                    try {
                        age = getToAgeHabr(xssClear(element.getElementsByClass("content-section content-section--appearance-card-section").addClass("inline-list").first().text()));
                    } catch (Exception e) {
                        log.error(error_parse, "age", e.getMessage());
                    }
                    if (isToValid(freshen, join(title, workBefore, skills)) && isAgeAfter(age) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo(
                                title,
                                xssClear(element.getElementsByClass("resume-card__title-link").text()),
                                age,
                                link,
                                getToSalary(xssClear(element.getElementsByClass("resume-card__offer").addClass("preserve-line").tagName("span").text().trim())),
                                workBefore,
                                getToUrl(habr, xssClear(element.getElementsByTag("a").attr("href").trim())),
                                skills, localDate);
                        list.add(rTo);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<ResumeTo> getResumesRabota(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(rabota, xssClear(element.getElementsByClass("santa-typo-additional santa-text-black-500 santa-mr-20").text().trim()));
                if (localDate.isAfter(reasonDateToLoad)) {
                    String age = link, workBefore, title = getCorrectTitle(xssClear(element.getElementsByClass("santa-m-0 santa-typo-h3 santa-pb-10").text().trim()));
                    workBefore = getToWorkBeforeRabota(xssClear(element.getElementsByAttributeValueStarting("class", "santa-mt-0").tagName("p").text().trim()));
//                    System.out.println("title="+title);
                    try {
//                    System.out.println("ageString1=");
//                        String ageString = element.getElementsByAttributeValueContaining("class", "santa-space-x-10").next().first().text().trim();
//                        System.out.println("ageString="+ageString);
                        age = getToAgeRabota(xssClear(element.getElementsByAttributeValueContaining("class", "santa-space-x-10").next().first().text().trim()));
                    } catch (Exception e) {
                        log.error(error_parse, "age", e.getMessage());
                    }
                    if (isToValid(freshen, join(title, workBefore)) && isAgeAfter(age) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo(
                                title,
                                xssClear(element.getElementsByAttributeValueStarting("class", "santa-pr-20 ").text()),
                                age,
                                getToAddressFormat(xssClear(element.getElementsByAttributeValueStarting("class", "santa-flex-shrink-0").next().text().trim())),
                                getToSalary(xssClear(element.getElementsByAttributeValueStarting("class", "santa-flex-shrink-0").next().text().trim())),
                                workBefore,
                                xssClear(element.getElementsByAttributeValue("target", "_self").attr("href").trim()),
                                link, localDate);
                        list.add(rTo);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<ResumeTo> getResumesWork(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(work, xssClear(element.getElementsByAttributeValueEnding("class", "pull-right").text().trim()));
                if (localDate.isAfter(reasonDateToLoad)) {
                    String workBefore, skills, age, title = getCorrectTitle(xssClear(element.getElementsByTag("a").first().text()));
                    workBefore = getToWorkBefore(xssClear(element.getElementsByTag("ul").text().trim()));
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("add-bottom").tagName("div").text().trim()));
                    age = xssClear(element.getElementsByAttributeValueContaining("data-toggle", "popover").next().next().text().trim());
                    age = getToAgeWork(isEmpty(age) ? xssClear(element.getElementsByTag("b").addClass("text-muted").next().next().text()) : age);
                    if (isToValid(freshen, join(title, workBefore, skills)) && isAgeAfter(age) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo(
                                title,
                                xssClear(element.getElementsByTag("b").text()),
                                getToAddressFormat(getToAddressWork(xssClear(element.getElementsByAttributeValueContaining("class", "add-bottom").prev().text().trim()))),
                                age,
                                getToSalary(xssClear(element.getElementsByAttributeValueStarting("class", "nowrap").tagName("span").text().trim())),
                                workBefore,
                                getToUrl(work, xssClear(element.getElementsByTag("a").attr("href"))),
                                skills, localDate);
                        list.add(rTo);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }
}
/*
                        System.out.println(i++ + "--------------------------------------------------------------------------------------");
                        System.out.println("dateString="+ element.getElementsByAttributeValueEnding("class", "pull-right").text().trim());
                        System.out.println("date="+rTo.getReleaseDate());
                        System.out.println("titleString="+ xssClear(element.getElementsByTag("a").first().text()));
                        System.out.println("title="+rTo.getTitle());
                        System.out.println("nameString="+ xssClear(element.getElementsByTag("b").text()));
                        System.out.println("name="+rTo.getName());
                        System.out.println("ageString="+ xssClear(element.getElementsByAttributeValueContaining("data-toggle", "popover").next().next().text().trim()));
                        System.out.println("age="+rTo.getAge());
                        System.out.println("addressString="+ xssClear(element.getElementsByAttributeValueContaining("class", "add-bottom").prev().text().trim()));
                        System.out.println("address="+rTo.getAddress());
                        System.out.println("salaryString="+ xssClear(element.getElementsByAttributeValueStarting("class", "nowrap").tagName("span").text().trim()));
                        System.out.println("salary="+rTo.getSalary());

                        System.out.println("workBeforeString="+ xssClear(element.getElementsByTag("ul").text().trim()));
                        System.out.println("workBefore="+rTo.getWorkBefore());
                        System.out.println("urlString="+ xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url="+rTo.getUrl());
                        System.out.println("skillsString="+ xssClear(element.getElementsByClass("add-bottom").tagName("div").text().trim()));
                        System.out.println("skills="+rTo.getSkills());
*/
