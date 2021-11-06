package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;

public class AggregatorUtil {

    public static ResumeTo createTo(ResumeTo resumeTo, Freshen freshen) {
        resumeTo.setWorkplace(freshen.getWorkplace());
        resumeTo.setLevel(freshen.getLevel());
        resumeTo.setLanguage(freshen.getLanguage());
        resumeTo.setToVote(false);
        return resumeTo;
    }
}
