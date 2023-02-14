package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.model.Vote;
import ua.training.top.to.ResumeTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.List.of;
import static ua.training.top.util.aggregateUtil.data.CommonUtil.getJoin;
import static ua.training.top.util.aggregateUtil.data.CommonUtil.isMatch;
import static ua.training.top.util.aggregateUtil.data.ConstantsUtil.*;

public class ResumeUtil {

    private static List<ResumeTo> getEmpty() {
        return of(new ResumeTo(0, "", "", "", "", -1,
                "No filtering records found, refresh DB",
                "", "", LocalDate.now(), "", "", "", false));
    }

    public static List<ResumeTo> getTos(List<Resume> resumes, List<Vote> votes) {
        return resumes.isEmpty() ? getEmpty() : resumes.stream()
                .map(r -> getTo(r, votes))
                .sorted(ResumeTo::compareTo).toList();
    }

    public static ResumeTo getTo(Resume r, List<Vote> votes) {
        boolean toVote = votes.stream().anyMatch(vote -> r.getId().equals(vote.getResumeId()));
        return new ResumeTo(r.getId(), r.getTitle(), r.getName(), r.getAge(), r.getAddress(),
                r.getSalary(), r.getWorkBefore(), r.getUrl(), r.getSkills(), r.getReleaseDate(),
                r.getFreshen().getLanguage(), r.getFreshen().getLevel(), r.getFreshen().getWorkplace(), toVote);
    }

    public static List<Resume> fromTos(List<ResumeTo> vTos) {
        return vTos.stream().map(ResumeUtil::fromTo).toList();
    }

    public static Resume fromTo(ResumeTo rTo) {
        return new Resume(rTo.getId(), rTo.getTitle(), rTo.getName(), rTo.getAge(), rTo.getAddress(),
                rTo.getSalary(), rTo.getWorkBefore(), rTo.getUrl(), rTo.getSkills(),
                rTo.getReleaseDate() != null ? rTo.getReleaseDate() : LocalDate.now().minusDays(7));
    }

    public static Resume fromToForUpdate(ResumeTo rTo, Resume r) {
        assert r != null;
        Resume resume = new Resume(
                r.getId(),
                rTo.getTitle(),
                r.getName().equals(link) ? link : rTo.getName(),
                r.getAge().equals(link) ? link : rTo.getAge(),
                r.getAddress().equals(link) ? link : rTo.getAddress(),
                rTo.getSalary(),
                rTo.getWorkBefore(),
                rTo.getUrl(),
                r.getSkills().equals(link) ? link : rTo.getSkills(),
                r.getReleaseDate());
        resume.setFreshen(r.getFreshen());
        return resume;
    }

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
