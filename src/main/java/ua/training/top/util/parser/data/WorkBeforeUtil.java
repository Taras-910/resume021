package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.CommonDataUtil.getLimitation;
import static ua.training.top.util.parser.data.CommonDataUtil.limitText;

public class WorkBeforeUtil {

    public static String getToWorkBeforeRabota(String workBefore) {
        workBefore = workBefore.replaceFirst("Последнее место", " Последнее место");
        workBefore = workBefore.substring(workBefore.indexOf(" ") + 1).trim();
        return getToWorkBefore(workBefore);
    }
    public static String getToWorkBeforeHabr(String workBefore) {
        workBefore = workBefore.startsWith("От") ? workBefore.substring(workBefore.indexOf("·") + 1) : workBefore;
        workBefore = workBefore.replaceAll(", "," · ").trim();
        return getToWorkBefore(workBefore);
    }
    public static String getToWorkBefore(String workBefore) {
        return getLimitation(workBefore, limitText);
    }


}
