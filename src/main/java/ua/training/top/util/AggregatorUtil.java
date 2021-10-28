package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.model.Vote;
import ua.training.top.to.ResumeTo;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ua.training.top.util.ResumeUtil.fromTo;
import static ua.training.top.util.ResumeUtil.getTo;

public class AggregatorUtil {

    public static ResumeTo getFilled(ResumeTo resumeTo, Freshen freshen) {
        resumeTo.setWorkplace(freshen.getWorkplace());
        resumeTo.setLevel(freshen.getLevel());
        resumeTo.setLanguage(freshen.getLanguage());
        resumeTo.setToVote(false);
        return resumeTo;
    }

    public static List<Resume> getForCreate(List<ResumeTo> resumeTosForCreate, Freshen freshenDb) {
        return resumeTosForCreate.stream()
                .map(rTo -> {
                    Resume resume = fromTo(rTo);
                    resume.setFreshen(freshenDb);
                    return resume;
                })
                .collect(Collectors.toList());
    }
}
