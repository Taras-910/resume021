package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.model.Vote;
import ua.training.top.to.ResumeTo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;
import static ua.training.top.util.ResumeCheckUtil.isAccuracy;
import static ua.training.top.util.parser.data.CommonDataUtil.link;

public class ResumeUtil {

    private static List<ResumeTo> getEmpty() {
        return of(new ResumeTo(0, "", "", "", "", -1,
                "No filtering records found, refresh DB",
                "", "", LocalDate.now(), "", "", "", false));
    }

    public static List<ResumeTo> getTos(List<Resume> resumes, List<Vote> votes) {
        return resumes.isEmpty() ? getEmpty() : resumes.stream()
                .map(r -> getTo(r, votes))
                .sorted(ResumeTo::compareTo)
                .collect(Collectors.toList());
    }

    public static ResumeTo getTo(Resume r, List<Vote> votes) {
        boolean toVote = votes.stream().anyMatch(vote -> r.getId().equals(vote.getResumeId()));
        return new ResumeTo(r.getId(), r.getTitle(), r.getName(), r.getAge(), r.getAddress(),
                r.getSalary(), r.getWorkBefore(), r.getUrl(), r.getSkills(), r.getReleaseDate(),
                r.getFreshen().getLanguage(), r.getFreshen().getLevel(), r.getFreshen().getWorkplace(), toVote);
    }

    public static List<Resume> fromTos(List<ResumeTo> vTos) {
        return vTos.stream().map(ResumeUtil::fromTo).collect(Collectors.toList());
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

    public static List<Resume> getFilterLanguage(List<Resume> resumes, Freshen f) {
        return f.getLanguage().equals("all") ? resumes : resumes.stream()
                .filter(r -> isAccuracy(f.getLanguage(), List.of(r.getSkills(),  r.getTitle(), r.getWorkBefore())))
                .collect(Collectors.toList());
    }
}
