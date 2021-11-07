package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.to.ResumeTo;

import static ua.training.top.util.ResumeUtil.getPartWorkBefore;

public class AggregatorUtil {
    public static final String link = "see the card";
    public static ResumeTo createTo(ResumeTo resumeTo, Freshen freshen) {
        resumeTo.setWorkplace(freshen.getWorkplace());
        resumeTo.setLevel(freshen.getLevel());
        resumeTo.setLanguage(freshen.getLanguage());
        resumeTo.setToVote(false);
        return resumeTo;
    }
    public static String getAnchor(Resume r) {
        return getPartWorkBefore(r.getWorkBefore()).toLowerCase();
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
}
