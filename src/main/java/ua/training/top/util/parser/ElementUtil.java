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
import static ua.training.top.aggregator.installation.Installation.reasonDateLoading;
import static ua.training.top.aggregator.strategy.HabrStrategy.getToSkillsHabr;
import static ua.training.top.aggregator.strategy.HabrStrategy.getToWorkBeforeHabr;
import static ua.training.top.aggregator.strategy.WorkStrategy.getToAddressWork;
import static ua.training.top.util.AggregatorUtil.getMatch;
import static ua.training.top.util.AggregatorUtil.isToValid;
import static ua.training.top.util.parser.data.DataUtil.*;
import static ua.training.top.util.parser.data.ReleaseDateUtil.getToLocalDate;
import static ua.training.top.util.parser.data.SalaryUtil.getToSalary;
import static ua.training.top.util.parser.data.UrlUtil.getToUrl;
import static ua.training.top.util.xss.XssUtil.xssClear;

public class ElementUtil {
    public static final Logger log = LoggerFactory.getLogger(ElementUtil.class);

    public static List<ResumeTo> getResumesDjinni(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        elements.forEach(element -> {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByTag("small").text().trim()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, workBefore, title = getUpperStart(xssClear(element.getElementsByClass("profile").tagName("a").text().trim()));
                    workBefore = getLimitation(xssClear(element.nextElementSibling().ownText()));
                    skills = getLimitation(xssClear(element.nextElementSibling().nextElementSibling().ownText()));
                    if (isToValid(freshen, join(title, workBefore, skills)) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo(title, link, link,
                                getMatch(address_field, xssClear(element.getElementsByAttributeValueStarting("class", "tiny-profile-details").text())),
                                getToSalary(xssClear(element.getElementsByAttributeValueStarting("class", "profile-details-salary").text().trim())),
                                workBefore,
                                getToUrl(djinni, xssClear(element.getElementsByTag("a").attr("href").trim())),
                                skills, localDate);
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
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("resume-search-item__date").text().trim()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String workBefore, age, title = getUpperStart(xssClear(element.getElementsByClass("resume-search-item__name").text().trim()));
                    workBefore = getLimitation(xssClear(element.getElementsByAttributeValueStarting("data-qa", "resume-serp_resume-item-content").text()));
                    age = getLinkIfEmpty(xssClear(element.getElementsByAttributeValueStarting("data-qa", "resume-serp__resume-age").text()));
                    if (isToValid(freshen, join(title, workBefore)) && isAgeAfter(age) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo(title, link, age, link,
                                getToSalary(xssClear(element.getElementsByClass("bloko-text bloko-text_large bloko-text_strong").text().trim())),
                                workBefore,
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
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("basic-date").attr("datetime")));
                if (localDate.isAfter(reasonDateLoading)) {
                    String age = link, workBefore, name, skills, title = getUpperStart(xssClear(element.getElementsByClass("resume-card__specialization").text()));
                    skills = getToSkillsHabr(xssClear(element.getElementsByClass("link-comp link-comp--appearance-dark").text().trim()));
                    name = xssClear(element.getElementsByClass("resume-card__title-link").text());
                    workBefore = getToWorkBeforeHabr(xssClear(element.getElementsByClass("inline-list").text()));
                    try {
                        age = getMatch(age_field, xssClear(element.getElementsByClass("content-section content-section--appearance-card-section").addClass("inline-list").first().text()));
                    } catch (Exception e) {
                        log.error(error_parse, age_field, e.getMessage());
                    }
                    if (isToValid(freshen, join(title, workBefore, skills)) && isAgeAfter(age) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo(title, name, age, link,
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
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("santa-typo-additional santa-text-black-500 santa-mr-20").text().trim()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String age = link, workBefore, name, title = getUpperStart(xssClear(element.getElementsByClass("santa-m-0 santa-typo-h3 santa-pb-10").text().trim()));
                    workBefore = getLimitation(xssClear(element.getElementsByAttributeValueStarting("class", "santa-mt-0").tagName("p").text().trim()));
                    name = xssClear(element.getElementsByAttributeValueStarting("class", "santa-pr-20 ").text());
                    try {
                        age = getMatch(age_field, xssClear(element.getElementsByAttributeValueContaining("class", "santa-space-x-10").next().first().text().trim()));
                    } catch (Exception e) {
                        log.error(error_parse, age_field, e.getMessage());
                    }
                    if (isToValid(freshen, join(title, workBefore)) && isAgeAfter(age) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo(title, name, age,
                                getMatch(address_field, xssClear(element.getElementsByAttributeValueStarting("class", "santa-flex-shrink-0").next().text())),
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
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByAttributeValueEnding("class", "pull-right").text().trim()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String workBefore, skills, name, age, age1, title = getUpperStart(xssClear(element.getElementsByTag("a").first().text()));
                    workBefore = getLimitation(xssClear(element.getElementsByTag("ul").text().trim()));
                    skills = getLimitation(xssClear(element.getElementsByClass("add-bottom").tagName("div").text().trim()));
                    name = xssClear(element.getElementsByTag("b").text());
                    age = xssClear(element.getElementsByAttributeValueContaining("data-toggle", "popover").next().next().text().trim());
                    age1 = xssClear(element.getElementsByTag("b").addClass("text-muted").next().next().text());
                    age = !isEmpty(age) && age.matches(is_age) ? getMatch(age_field, age) : getMatch(age_field, age1);
                    if (isToValid(freshen, join(title, workBefore, skills)) && isAgeAfter(age) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo(title, name, age,
                                getToAddressWork(xssClear(element.getElementsByAttributeValueContaining("class", "add-bottom").prev().text().trim())),
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
                        System.out.println(i++ + "-".repeat(70));

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
