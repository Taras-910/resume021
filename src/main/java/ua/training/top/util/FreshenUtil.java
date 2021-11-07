package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;
import ua.training.top.to.ResumeTo;

import static java.time.LocalDateTime.now;
import static java.util.Collections.singleton;
import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.model.Goal.UPGRADE;

public class FreshenUtil {
    public static Logger log = LoggerFactory.getLogger(FreshenUtil.class);

    public static Freshen getFreshenFromTo(ResumeTo rTo) {
        return new Freshen(null, now(), rTo.getLanguage(), rTo.getLevel(),
                hasText(rTo.getWorkplace()) ? rTo.getWorkplace() : rTo.getAddress(), singleton(UPGRADE), authUserId());
    }

    public static Freshen asNewFreshen(Freshen f) {
        return new Freshen(f.getId(), now(), f.getLanguage(), f.getLevel(), f.getWorkplace(),
                f.getGoals() == null ? singleton(UPGRADE) : f.getGoals(),
                f.getUserId() == null ? authUserId() : f.getUserId());
    }

    public static Freshen asNewFreshen(String language, String level, String workplace, Goal goal) {
        return new Freshen(null, now(), language, level, workplace, singleton(goal == null ? UPGRADE : goal), authUserId());
    }
}
