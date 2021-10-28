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
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.aggregator.installation.InstallationUtil.reasonDateToLoad;
import static ua.training.top.util.ResumeCheckUtil.getMatchesLanguage;
import static ua.training.top.util.parser.data.CorrectAddress.*;
import static ua.training.top.util.parser.data.CorrectAge.*;
import static ua.training.top.util.parser.data.CommonUtil.*;
import static ua.training.top.util.parser.data.CorrectWorkBefore.*;
import static ua.training.top.util.parser.data.CorrectSkills.*;
import static ua.training.top.util.parser.data.CorrectTitle.*;
import static ua.training.top.util.parser.data.CorrectUrl.*;
import static ua.training.top.util.parser.date.DateUtil.*;
import static ua.training.top.util.parser.date.ToCorrectDate.getCorrectDate;
import static ua.training.top.util.parser.salary.MinMax.*;
import static ua.training.top.util.parser.salary.SalaryUtil.getCorrectSalary;
import static ua.training.top.util.xss.XssUtil.xssClear;

public class ElementUtil {
    public static final Logger log = LoggerFactory.getLogger(ElementUtil.class);

    public static List<ResumeTo> getResumesDjinni(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList();
        elements.forEach(element -> {
            try {
                LocalDate localDate = parseCustom(supportDate(xssClear(element.getElementsByTag("small").text().trim())), element);
                if (localDate.isAfter(reasonDateToLoad)) {
                    String skills, workBefore, title = getCorrectTitle(xssClear(element.getElementsByClass("profile").tagName("a").text().trim()));
                    workBefore = getLimitation(xssClear(element.nextElementSibling().ownText()));
                    skills = getSkillsDjinni(title, xssClear(element.getElementsByAttributeValueStarting("class", "tiny-profile-details").text()), element);
                    if (getMatchesLanguage(freshen, title, workBefore.concat(" ").concat(skills)) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo();
                        rTo.setTitle(title);
                        rTo.setName(link);
                        rTo.setAge(link);
                        rTo.setAddress(getToAddressFormat(xssClear(element.getElementsByAttributeValueStarting("class", "tiny-profile-details").text()).split("· \\$")[0]));
                        rTo.setSalary(getSalary(getCorrectSalary(xssClear(element.getElementsByAttributeValueStarting("class", "profile-details-salary").text().trim())), null));
                        rTo.setWorkBefore(hasText(workBefore) ? workBefore : link);
                        rTo.setUrl(getCorrectUrl(djinni, xssClear(element.getElementsByTag("a").attr("href").trim())));
                        rTo.setSkills(hasText(skills) ? skills : workBefore);
                        rTo.setReleaseDate(localDate);
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
                LocalDate localDate = parseCustom(supportDate(prepareGrc(xssClear(element.getElementsByAttributeValueStarting("class", "bloko-text bloko-text_tertiary").text().trim()))), element);
                if (localDate.isAfter(reasonDateToLoad)) {
                    String workBefore, title = getCorrectTitle(xssClear(element.getElementsByClass("resume-search-item__name").text().trim()));
                    workBefore = getLimitation(xssClear(element.getElementsByAttributeValueStarting("data-qa", "resume-serp_resume-item-content").text()));
                    if (getMatchesLanguage(freshen, title, workBefore) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo();
                        rTo.setTitle(title);
                        rTo.setName(link);
                        rTo.setAge(getMessageIfEmpty(xssClear(element.getElementsByAttributeValueStarting("data-qa", "resume-serp__resume-age").text())));
                        rTo.setAddress(link);
                        rTo.setSalary(getSalary(getCorrectSalary(xssClear(element.getElementsByAttributeValueStarting("class", "profile-details-salary").text().trim())), null));
                        rTo.setWorkBefore(hasText(workBefore) ? workBefore : link);
//                        rTo.setExperience(xssClear(element.getElementsByAttributeValueStarting("data-qa", "resume-serp__resume-excpirience-sum").text().trim()));
                        rTo.setUrl(getCorrectUrl(grc, xssClear(element.getElementsByClass("resume-search-item__name").attr("href").trim())));
                        rTo.setSkills(workBefore);
                        rTo.setReleaseDate(localDate);
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
                LocalDate localDate = parseCustom(xssClear(element.getElementsByClass("basic-date").attr("datetime")), element);
                if (localDate.isAfter(reasonDateToLoad)) {
                    String workBefore, skills, title = getCorrectTitle(xssClear(element.getElementsByClass("resume-card__specialization").text()));
                    skills = getSkillsHabr(xssClear(element.getElementsByClass("link-comp link-comp--appearance-dark").text().trim()));
                    workBefore = getToWorkBefore(xssClear(element.getElementsByClass("inline-list").text()));
                    if (getMatchesLanguage(freshen, title, workBefore.concat(" ").concat(skills)) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo();
                        rTo.setTitle(title);
                        rTo.setName(xssClear(element.getElementsByClass("resume-card__title-link").text()));
                        rTo.setAge(getMessageIfEmpty(xssClear(element.getElementsByClass("content-section content-section--appearance-card-section").addClass("inline-list").first().text())));
                        rTo.setAddress(link);
                        rTo.setSalary(getSalary(getCorrectSalary(xssClear(element.getElementsByClass("resume-card__offer").addClass("preserve-line").tagName("span").text().trim())), element));
                        rTo.setWorkBefore(workBefore);
//                        rTo.setExperience(xssClear(element.getElementsByClass("preserve-line").text()));
                        rTo.setUrl(getCorrectUrl(habr, xssClear(element.getElementsByTag("a").attr("href").trim())));
                        rTo.setSkills(skills);
                        rTo.setReleaseDate(localDate);
                        list.add(rTo);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<ResumeTo> getResumesLinkedin(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList();
        AtomicInteger i = new AtomicInteger();
        elements.forEach(element -> {
            System.out.println(i.getAndIncrement() + "  -------------------------------------------------------------------------------------");
//            System.out.println("element = " + element + "\n");
            try {
                LocalDate localDate = parseCustom(xssClear(element.getElementsByTag("time").tagName("time").attr("datetime")), element);
                if (localDate.isAfter(reasonDateToLoad)) {
                    String title = getCorrectTitle(xssClear(element.getElementsByClass("base-search-card__title").text().trim()));
                    if (/*title.toLowerCase().matches(".*\\b" + freshen.getLanguage() + "\\b.*")*/ true) {
                        ResumeTo rTo = new ResumeTo();
                        rTo.setTitle(title);
                        rTo.setName(link);
                        rTo.setAge(link);
                        rTo.setAddress(getToAddressFormat(getToAddress(getToAddressLinkedin(xssClear(element.getElementsByClass("job-search-card__location").text().trim())))));
                        rTo.setSalary(1);
                        rTo.setWorkBefore(link);
                        rTo.setUrl(xssClear(element.getElementsByTag("a").first().attr("href")));
                        rTo.setSkills(link);
                        rTo.setReleaseDate(localDate);
                        list.add(rTo);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        });
        return list;
    }

    public static List<ResumeTo> getResumesRabota(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getCorrectDate(xssClear(element.getElementsByClass("santa-typo-additional santa-text-black-500 santa-mr-20").text().trim()));
                if (localDate.isAfter(reasonDateToLoad)) {
                    String title = getCorrectTitle(xssClear(element.getElementsByClass("santa-m-0 santa-typo-h3 santa-pb-10").text().trim()));
                    String workBefore = getToWorkBefore(xssClear(element.getElementsByAttributeValueStarting("class", "santa-mt-0").tagName("p").text().trim()));
                    if (getMatchesLanguage(freshen, title, workBefore) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo();
                        rTo.setTitle(title);
                        rTo.setName(xssClear(element.getElementsByAttributeValueStarting("class","santa-pr-20 ").text()));
//                        rTo.setAge(getToAgeRabota(xssClear(element.getElementsByAttributeValueStarting("class", "santa-flex-shrink-0").next().text().trim())));
                        rTo.setAge(getToAgeRabota(xssClear(element.getElementsByAttributeValueContaining("class", "santa-space-x-10").next().first().text().trim())));
                        rTo.setAddress(getToAddressFormat(getToAddressRabota(xssClear(element.getElementsByAttributeValueStarting("class", "santa-flex-shrink-0").next().text().trim()))));
                        rTo.setSalary(getSalary(getCorrectSalary(getSalaryRabota(xssClear(element.getElementsByAttributeValueStarting("class", "santa-flex-shrink-0").next().text().trim()))), element));
                        rTo.setWorkBefore(workBefore);
                        rTo.setUrl(xssClear(element.getElementsByAttributeValue("target","_self").attr("href").trim()));
                        rTo.setSkills(workBefore);
                        rTo.setReleaseDate(localDate);
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
                LocalDate localDate = getCorrectDate(xssClear(element.getElementsByAttributeValueEnding("class", "pull-right").text().trim()));
                if (localDate.isAfter(reasonDateToLoad)) {
                    String workBefore, skills, title = getCorrectTitle(xssClear(element.getElementsByTag("a").first().text()));
                    workBefore = xssClear(element.getElementsByTag("ul").text().trim());
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("add-bottom").tagName("div").text().trim()));
                    if (getMatchesLanguage(freshen, title, workBefore.concat(" ").concat(skills)) && workBefore.length() > 2) {
                        ResumeTo rTo = new ResumeTo();
                        rTo.setTitle(title);
                        rTo.setName(xssClear(element.getElementsByTag("b").text()));
                        rTo.setAge(getMessageIfEmpty(xssClear(element.getElementsByAttributeValueContaining("data-toggle", "popover").next().next().text().trim())));
                        rTo.setAddress(getToAddressFormat(getToAddressWork(xssClear(element.getElementsByAttributeValueContaining("class", "add-bottom").prev().text().trim()))));
                        rTo.setSalary(getSalary(getCorrectSalary(xssClear(element.getElementsByAttributeValueStarting("class", "nowrap").tagName("span").text().trim())), element));
                        rTo.setWorkBefore(workBefore);
                        rTo.setUrl(getCorrectUrl(work, xssClear(element.getElementsByTag("a").attr("href"))));
                        rTo.setSkills(skills);
                        rTo.setReleaseDate(localDate);
                        list.add(rTo);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<ResumeTo> getResumesYandex(Elements elements, Freshen freshen) {
        List<ResumeTo> list = new ArrayList<>();
        int i = 1;
        for (Element element : elements) {
            System.out.println(i++ + "  -------------------------------------------------------------------------------------");
            System.out.println("element = " + element + "\n");
            try {
                LocalDate localDate = getCorrectDate(xssClear(element.getElementsByClass("serp-vacancy__date").text().trim()));
                System.out.println("stringDate=" + xssClear(element.getElementsByAttributeValueEnding("class", "pull-right").text().trim()));
                System.out.println("localDate=" + localDate);
                if (localDate.isAfter(reasonDateToLoad)) {
                    String address, skills, title = getCorrectTitle(xssClear(element.getElementsByClass("heading heading_level_3").text().trim()));
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("serp-vacancy__requirements").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        ResumeTo rTo = new ResumeTo();
                        rTo.setTitle(title);
                        rTo.setName(link);
                        rTo.setAge(link);
                        address = xssClear(element.select("div.address").text().trim());
                        rTo.setAddress(getToAddressFormat(getToAddress(getToAddressYandex(address, freshen))));
                        rTo.setSalary(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("serp-vacancy__salary").text().trim())), element));
                        rTo.setWorkBefore(link);
                        rTo.setUrl(getCorrectUrlYandex(xssClear(element.getElementsByTag("a").attr("href"))));
                        rTo.setSkills(skills);
                        rTo.setReleaseDate(localDate);
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
                    System.out.println("------------------------------------------------------------------------------------------");
                    String line = element.getElementsByTag("a").last().text().trim();
                    System.out.println("element:\n" + element);
                    System.out.println("getCorrectEmployerName=" + getCorrectEmployerName(xssClear(line)));


                        String salary = element.getElementsByAttributeValueStarting("class", "text-truncate badgy salary").text().trim();
                        System.out.println("salary="+salary);
                        String correctSalary = getCorrectSalary(xssClear(salary));
                        System.out.println("correctSalary="+correctSalary);
                        int salaryMin = salaryMin(correctSalary, null);
                        System.out.println("salaryMin="+salaryMin);
*/


/*
                        System.out.println("............................................");
                        Element sibling1 = element.nextElementSibling();
                        System.out.println("lastWork="+sibling1.ownText());
                        Element sibling2 = element.nextElementSibling().nextElementSibling();
                        System.out.println("\nskills="+sibling2.ownText());
*/
/*                String line = xssClear(element.getElementsByAttributeValueStarting("class", "bloko-text bloko-text_tertiary").text().trim());
                System.out.println("line ="+line);
                String prepareGrc = (prepareGrc(line));
                System.out.println("prepareGrc="+prepareGrc);
                String supportDate = supportDate(prepareGrc);
                System.out.println("supportDate="+supportDate);
*/
