package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.to.ResumeTo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.training.top.util.parser.data.CommonUtil.getJoin;
import static ua.training.top.util.parser.data.CommonUtil.isMatch;
import static ua.training.top.util.parser.data.ConstantsUtil.*;

public class AggregatorUtil {

    public static ResumeTo createTo(ResumeTo resumeTo, Freshen freshen) {
        resumeTo.setWorkplace(freshen.getWorkplace());
        resumeTo.setLevel(freshen.getLevel());
        resumeTo.setLanguage(freshen.getLanguage());
        resumeTo.setToVote(false);
        return resumeTo;
    }

    public static String getAnchor(Resume r) {
        String work = r.getWorkBefore();
        for (String period : getWorkPeriod(work)) {
            work = work.replaceAll(period, "");
        }
        return getJoin(r.getTitle(), " ", work).toLowerCase();
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
        return (allLanguages.stream().anyMatch(temp::contains) || temp.contains(f.getLanguage())
                || isMatch(workersIT, temp)) && wasteSkills.stream().noneMatch(temp::contains);
    }

    public static List<String> getWorkPeriod(String text) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile(date_period_extract, Pattern.CASE_INSENSITIVE).matcher(text);
        while (m.find()) {
            String s = m.group();
            if (s.matches(is_period_work)) {
                list.add(s);
            }
        }
        return list;
    }
}
