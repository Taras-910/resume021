package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.CommonUtil.getLimitation;

public class CorrectWorkBefore {

    public static String getToWorkBefore(String workBefore) {
        workBefore = workBefore.replaceFirst("Последнее место", " Последнее место");
        return getLimitation(workBefore.substring(workBefore.indexOf(" ") + 1).trim());
    }

}
