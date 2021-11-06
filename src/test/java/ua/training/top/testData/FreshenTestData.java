package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;

import java.util.Collections;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.testData.UserTestData.admin_id;
import static ua.training.top.testData.UserTestData.user_id;

public class FreshenTestData {
    public static TestMatcher<Freshen> freshen_matcher = TestMatcher.usingFieldsComparator(Freshen.class, "recordedDate", "resumes");

    public static final int freshen1_id = START_SEQ + 2;
    public static final int freshen2_id = freshen1_id + 1;
    public static final Freshen freshen1 = new Freshen(freshen1_id, of(2020, 10, 25, 12, 0), "java", "middle", "киев", Collections.singleton(Goal.UPGRADE), admin_id);
    public static final Freshen freshen2 = new Freshen(freshen2_id, of(2020, 10, 25, 13, 0), "php", "middle", "днепр", Collections.singleton(Goal.FILTER), user_id);

    public static Freshen getNew() {
        return new Freshen(null, now(), "java", "middle", "newCity", Collections.singleton(Goal.UPGRADE), admin_id);
    }

    public static Freshen getUpdated() {
        Freshen updated = new Freshen(freshen1);
        updated.setLanguage("javascript");
        updated.setWorkplace("UpdatedCity");
        return updated;
    }
}
