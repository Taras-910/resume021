package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Resume;
import ua.training.top.model.Vote;
import ua.training.top.to.ResumeTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.List.of;
import static ua.training.top.aggregator.installation.InstallationUtil.limitResumesToKeep;
import static ua.training.top.aggregator.installation.InstallationUtil.reasonPeriodToKeep;

public class ResumeUtil {
    public static Logger log = LoggerFactory.getLogger(ResumeUtil.class) ;

    public static List<ResumeTo> getTos(List<Resume> resumes, List<Vote> votes) {
        return resumes.isEmpty() ? getEmpty() : resumes.stream()
                        .map(r -> getTo(r, votes))
                        .sorted(ResumeTo::compareTo)
                        .collect(Collectors.toList());
    }

    public static ResumeTo getTo(Resume r, List<Vote> votes) {
        boolean toVote = votes.stream().filter(vote -> r.getId().equals(vote.getResumeId())).count() != 0;
        return new ResumeTo(r.getId(), r.getTitle(), r.getName(), r.getAge(), r.getAddress(),
                r.getSalary(), r.getWorkBefore(), r.getUrl(), r.getSkills(), r.getReleaseDate(),
                r.getFreshen().getLanguage(), r.getFreshen().getLevel(), r.getFreshen().getWorkplace(), toVote);
    }

    public static List<Resume> fromTos (List<ResumeTo> vTos) {
        return vTos.stream().map(ResumeUtil::fromTo).collect(Collectors.toList());
    }

    public static Resume fromTo(ResumeTo rTo) {
        return new Resume(rTo.getId(), rTo.getTitle(), rTo.getName(), rTo.getAge(), rTo.getAddress(),
                rTo.getSalary(), rTo.getWorkBefore(), rTo.getUrl(), rTo.getSkills(),
                rTo.getReleaseDate() != null? rTo.getReleaseDate() : LocalDate.now().minusDays(7));
    }

    private static List<ResumeTo> getEmpty() {
        return of(new ResumeTo(0, "", "", "", "", -1,
                "No filtering records found, refresh DB",
                "", "", LocalDate.now(), "", "", "",false));
    }

    public static Resume getForUpdate(ResumeTo rTo, Resume r) {
        Resume resume = new Resume(r == null ? null : r.getId(), rTo.getTitle(), rTo.getName(), rTo.getAge(), rTo.getAddress(),
                rTo.getSalary(), rTo.getWorkBefore(), rTo.getUrl(), rTo.getSkills(), r != null ?
                r.getReleaseDate() : rTo.getReleaseDate() != null ? rTo.getReleaseDate() : LocalDate.now().minusDays(7));
        assert r != null;
        resume.setFreshen(r.getFreshen());
        return resume;
    }

    public static List<Resume> getResumesOutPeriodToKeep(List<Resume> resumesDb) {
        return resumesDb.parallelStream()
                .filter(resumeTo -> reasonPeriodToKeep.isAfter(resumeTo.getReleaseDate()))
                .collect(Collectors.toList());
    }

    public static List<Resume> getResumesOutLimitHeroku(List<Resume> resumesDb) {
        return Optional.of(resumesDb.parallelStream()
                .sorted((r1, r2) -> r1.getReleaseDate().isAfter(r2.getReleaseDate()) ? 1 : 0)
                .sequential()
                .skip(limitResumesToKeep)
                .collect(Collectors.toList())).orElse(new ArrayList<>());
    }
}
