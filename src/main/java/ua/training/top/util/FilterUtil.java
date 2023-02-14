package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;
import static ua.training.top.util.aggregateUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregateUtil.data.ConstantsUtil.*;

public class FilterUtil {
    public static Logger log = LoggerFactory.getLogger(FilterUtil.class);

    public static List<Resume> getFilter(List<Resume> resumes, Freshen f) {
        return resumes.stream().
                filter(r -> isSuit(getJoin(r.getSkills(), " ", r.getTitle(), " ", r.getFreshen().getLanguage()).toLowerCase(), f.getLanguage())
                        && isSuit(getJoin(r.getTitle(), " ", r.getWorkBefore(), " ", r.getFreshen().getLevel()).toLowerCase(), f.getLevel())
                        && isSuit(getJoin(r.getAddress(), " ", r.getWorkBefore()).toLowerCase(), f.getWorkplace()))
                .collect(Collectors.toList());
    }

    public static boolean isSuit(String checkedText, String field) {
        return switch (field.toLowerCase()) {
            case "all" -> true;
            case "java", "react", "ruby" -> isCalibrated(checkedText, field);
            case "trainee", "стажировка", "стажер", "internship", "интерн", "intern" -> getAria(field).stream()
                    .anyMatch(a -> isCalibrated(checkedText, a));
            default -> getAria(field).size() == 1 ? isContains(checkedText, field) : getAria(field).stream()
                    .anyMatch(a -> !isMatch(getForeign(), field) ? isContains(checkedText, a) : isContains(checkedText, a)
                            && uaAria.stream().noneMatch(cityUA -> isContains(checkedText.toLowerCase(), cityUA)));
        };
    }

    private static List<String> getAria(String text) {
        return switch (text) {
            case "intern", "trainee", "интерн", "стажировка", "стажер" -> traineeAria;
            case "junior" -> juniorAria;
            case "middle" -> middleAria;
            case "senior" -> seniorAria;
            case "expert", "lead", "тимлид", "team lead" -> expertAria;
            case "ukraine", "україна", "украина", "ua" -> citiesUA;
            case "київ", "киев", "kiev", "kyiv" -> kievAria;
            case "foreign", "за_рубежем", "за рубежем", "за кордоном", "другие страны" -> getForeign();
            case "remote", "relocate", "релокейт", "удаленно", "віддалено" -> remoteAria;
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
            case "minsk", "минск", "мінськ" -> minskAria;
            default -> of(text);
        };
    }

    public static List<String> getForeign() {
        List<String> foreign = new ArrayList<>(otherAria);
        foreign.addAll(citiesPl);
        foreign.addAll(foreignAria);
        return foreign;
    }

    public static final List<String>
            juniorAria = of("junior", "младший", "без опыта", "обучение"),
            middleAria = of("middle", "средний"),
            seniorAria = of("senior", "старший"),
            expertAria = of("expert", "lead", "team lead", "ведущий", "тимлид"),
            kievAria = of("kyiv", "kiev", "київ", "киев"),
            dniproAria = of("дніпро", "днепр", "dnipro"),
            kharkivAria = of("харків", "харьков", "kharkiv"),
            lvivAria = of("львів", "львов", "lviv"),
            odesaAria = of("одесса", "odesa", "одеса"),
            mykolaivAria = of("mykolaiv", "миколаїв", "николаев"),
            vinnitsiaAria = of("винница", "vinnitsia", "вінниця"),
            zaporizhzhyaAria = of("запоріжжя", "запорожье", "zaporizhzhya"),
            chernivtsiAria = of("chernivtsi", "чернівці", "черновцы"),
            chernigivAria = of("чернігів", "чернигов", "chernigiv"),
            ivano_frankivskAria = of("івано-франківськ", "ивано-франковск", "ivano-frankivsk"),
            uzhgorodAria = of("ужгород", "uzhgorod"),
            polandAria = of("польша", "poland", "polski"),
            krakowAria = of("krakow", "краков"),
            warszawaAria = of("варшава", "warszawa"),
            wroclawAria = of("wroclaw", "вроцлав"),
            gdanskAria = of("гданськ", "гданск"),
            poznanAria = of("poznan", "познань"),
            minskAria = of("minsk", "минск", "мінськ");

}
