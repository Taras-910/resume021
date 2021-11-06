package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.to.ResumeTo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ua.training.top.util.ResumeUtil.fromTos;
import static ua.training.top.util.ResumeUtil.getPartWorkBefore;

public class AggregatorUtil {

    public static ResumeTo createTo(ResumeTo resumeTo, Freshen freshen) {
        resumeTo.setWorkplace(freshen.getWorkplace());
        resumeTo.setLevel(freshen.getLevel());
        resumeTo.setLanguage(freshen.getLanguage());
        resumeTo.setToVote(false);
        return resumeTo;
    }

    public static List<Resume> getForCreate(List<ResumeTo> resumeTosForCreate, Freshen freshenDb) {
        List<Resume> resumesForCreate = fromTos(resumeTosForCreate);
        return resumesForCreate.stream()
                .map(resume -> {
                    resume.setFreshen(freshenDb);
                    if (resume.getName().equalsIgnoreCase("Krat Maksym")) {
                        System.out.println(resume);
                    }
                    return resume;
                })
                .collect(Collectors.toList());
    }

    public static List<Resume> getForUpdate(List<ResumeTo> resumeTosForUpdate, Map<String, Resume> mapDb, Freshen freshenDb) {
        List<Resume> resumesForUpdate = fromTos(resumeTosForUpdate);
        return resumesForUpdate.stream()
                .map(rUpdate -> {
                    Resume r = mapDb.get(getPartWorkBefore(rUpdate.getWorkBefore()).toLowerCase());
                    rUpdate.setId(r.getId());
                    rUpdate.setTitle(r.getTitle().equalsIgnoreCase(rUpdate.getTitle()) ? rUpdate.getTitle() : r.getTitle());
                    rUpdate.setWorkBefore(r.getWorkBefore().equalsIgnoreCase(rUpdate.getWorkBefore()) ?
                            rUpdate.getWorkBefore() : r.getWorkBefore());
                    rUpdate.setFreshen(freshenDb);
                    return rUpdate;
                })
                .collect(Collectors.toList());
    }
}
