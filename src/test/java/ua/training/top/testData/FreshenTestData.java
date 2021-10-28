package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;

import java.util.Collections;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.testData.UserTestData.ADMIN_ID;
import static ua.training.top.testData.UserTestData.USER_ID;

public class FreshenTestData {
    public static TestMatcher<Freshen> FRESHEN_MATCHER = TestMatcher.usingFieldsComparator(Freshen.class,"recordedDate", "resumes");

    public static final int FRESHEN1_ID = START_SEQ + 2;
    public static final int FRESHEN2_ID = FRESHEN1_ID + 1;
    public static final Freshen freshen1 = new Freshen(FRESHEN1_ID, of(2020, 10, 25, 12,0), "java", "middle", "киев", Collections.singleton(Goal.UPGRADE), ADMIN_ID);
    public static final Freshen freshen2 = new Freshen(FRESHEN2_ID, of(2020, 10, 25, 13,0), "php", "middle", "днепр", Collections.singleton(Goal.FILTER), USER_ID);

    public static Freshen getNew() {
        return new Freshen(null, now(), "java", "middle", "newCity", Collections.singleton(Goal.UPGRADE), ADMIN_ID);
    }

    public static Freshen getUpdated() {
        Freshen updated = new Freshen(freshen1);
        updated.setLanguage("javascript");
        updated.setWorkplace("UpdatedCity");
        return updated;
    }
}
