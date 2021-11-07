package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
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
import static ua.training.top.util.ResumeCheckUtil.isSample;
import static ua.training.top.util.parser.data.CommonDataUtil.*;

public class ResumeUtil {
    private static Logger log = LoggerFactory.getLogger(ResumeUtil.class);

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
        Resume resume = new Resume(
                r == null ? null : r.getId(),
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

    public static List<Resume> getResumesOutPeriodToKeep(List<Resume> resumesDb) {
        return resumesDb.stream()
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

    public static String getPartWorkBefore(String text) {
        text = text.replaceAll("месяц", " ").trim();
        text = text.contains(" ") ? text.substring(0, text.indexOf(" ")).trim() : text;
        return getLimitation(text.contains(" ") ? text.substring(0, text.lastIndexOf(" ")).trim() : text, limit);
    }


    public static List<Resume> getFilterLanguage(List<Resume> resumes, Freshen f) {
        return f.getLanguage().equals("all") ? resumes : resumes.stream()
                .filter(r -> isSample(f.getLanguage(), List.of(r.getSkills(),  r.getTitle(), r.getWorkBefore())))
                .collect(Collectors.toList());
    }
}
