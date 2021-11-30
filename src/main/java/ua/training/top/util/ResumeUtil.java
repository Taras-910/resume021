package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.model.Vote;
import ua.training.top.to.ResumeTo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;
import static ua.training.top.util.parser.data.DataUtil.*;

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

    public static List<Resume> getFilter(List<Resume> resumes, Freshen f) {
        return f.getLanguage().equals("all") && f.getLevel().equals("all") && f.getWorkplace().equals("all") ? resumes : resumes.stream()
                .filter(r -> f.getLanguage().equals("all") ||
                        isFindExactly(List.of(r.getSkills(), r.getTitle(), r.getWorkBefore(), r.getFreshen().getLanguage()), f.getLanguage()))
                .filter(r -> f.getLevel().equals("all") ||
                        isContains(List.of(r.getTitle(), r.getWorkBefore(), r.getFreshen().getLevel()), f.getLevel()))
                .filter(r -> f.getWorkplace().equals("all") ||
                        isMatch(getAria(f.getWorkplace()), getBuild(r.getAddress()).append(r.getWorkBefore())))
                .collect(Collectors.toList());
    }

    public static boolean isFindExactly(List<String> list, String word) {
        return list.stream().anyMatch(s -> s.toLowerCase().matches(".*\\b" + word + "\\b.*"));
    }

    public static boolean isContains(List<String> area, String word) {
        return area.stream().anyMatch(a -> a.toLowerCase().indexOf(word) > -1);
    }

    private static List<String> getAria(String workplace) {
        return switch (workplace) {
            case "ukraine", "україна", "украина" -> ukraineAria;
            case "київ", "киев", "kiev" -> kievAria;
            case "foreign", "за_рубежем" -> citiesWorld;
            case "харків", "харьков", "kharkiv" -> kharkivAria;
            case "дніпро", "днепр", "dnipro" -> dniproAria;
            case "одеса", "одесса", "odesa" -> odesaAria;
            case "львів", "львов", "lviv" -> lvivAria;
            case "запоріжжя", "запорожье", "zaporizhzhya" -> zaporizhzhyaAria;
            case "миколаїв", "николаев", "mykolaiv" -> mykolaivAria;
            case "чорновці", "черновцы", "chernivtsi" -> chernivtsiAria;
            case "чернігів", "чернигов", "chernigiv" -> chernigivAria;
            case "вінниця", "винница", "vinnitsia" -> vinnitsiaAria;
            case "ужгород", "uzhgorod" -> uzhgorodAria;
            case "івано-франківськ", "ивано-франковск", "ivano-frankivsk" -> ivano_frankivskAria;
            case "польша", "poland", "polski" -> polandAria;
            case "варшава", "warszawa" -> warszawaAria;
            case "krakow", "краков" -> krakowAria;
            case "wroclaw", "вроцлав" -> wroclawAria;
            case "gdansk", "гданськ", "гданск" -> gdanskAria;
            case "poznan", "познань" -> poznanAria;
            default -> of(workplace);
        };
    }
}
