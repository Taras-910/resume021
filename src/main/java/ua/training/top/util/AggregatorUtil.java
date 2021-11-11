package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.to.ResumeTo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.join;
import static ua.training.top.aggregator.installation.Installation.maxLengthText;
import static ua.training.top.util.parser.data.DataUtil.*;

public class AggregatorUtil {
    public static ResumeTo createTo(ResumeTo resumeTo, Freshen freshen) {
        resumeTo.setWorkplace(freshen.getWorkplace());
        resumeTo.setLevel(freshen.getLevel());
        resumeTo.setLanguage(freshen.getLanguage());
        resumeTo.setToVote(false);
        return resumeTo;
    }

    public static String getAnchor(Resume r) {
        String find = getMatch(month,r.getWorkBefore());
        String work = find.equals(r.getWorkBefore()) ? r.getWorkBefore() : r.getWorkBefore().replaceAll(find, "");
        return getByLimit(join(" ", r.getTitle(), r.getName(), work), maxLengthText / 3).toLowerCase();
    }

    public static Resume getForUpdate(Resume r, Resume resumeDb) {
        r.setId(resumeDb.getId());
        r.setTitle(resumeDb.getTitle().equals(link) ? r.getTitle() : resumeDb.getTitle());
        r.setAge(resumeDb.getAge().equals(link) ? r.getAge() : resumeDb.getAge());
        r.setAddress(resumeDb.getAddress().equals(link) ? r.getAddress() : resumeDb.getAddress());
        r.setSalary(resumeDb.getSalary() == 1 ? r.getSalary() : resumeDb.getSalary());
        r.setWorkBefore(resumeDb.getWorkBefore().equals(link) ? r.getWorkBefore() : resumeDb.getWorkBefore());
        r.setSkills(resumeDb.getSkills().equals(link) ? r.getSkills() : resumeDb.getSkills());
        return r;
    }

    public static boolean isToValid(Freshen f, String text) {
        String temp = text.toLowerCase();
        return (temp.contains(f.getLanguage()) || isWorkerIT(temp)) && wasteWorkBefore.stream().noneMatch(temp::contains);
    }

    public static String getMatch(String fieldName, String text) {
        getLinkIfEmpty(text);
        //https://stackoverflow.com/questions/63964529/a-regex-to-get-any-price-string
        Matcher m = Pattern.compile(getMatcherByField(fieldName), Pattern.CASE_INSENSITIVE).matcher(text);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list.size() > 0 ? list.get(0) : !fieldName.contains("field") ? text : link;
    }

    public static String getMatcherByField(String fieldName) {
        return switch (fieldName) {
            case month -> extract_month;
            case local_date -> extract_date;
            case address_field -> extract_address;
            case age_field -> extract_age;
            default -> "";
        };
    }

}
