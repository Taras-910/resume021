package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.CommonDataUtil.getLimitation;
import static ua.training.top.util.parser.data.CommonDataUtil.limitText;

public class WorkBeforeUtil {

    public static String getToWorkBeforeHabr(String workBefore) {
        workBefore = workBefore.startsWith("От") ? workBefore.substring(workBefore.indexOf("·") + 1) : workBefore;
        return getLimitWorkBefore(workBefore);
    }
    public static String getLimitWorkBefore(String workBefore) {
        return getLimitation(workBefore, limitText);
    }


}
