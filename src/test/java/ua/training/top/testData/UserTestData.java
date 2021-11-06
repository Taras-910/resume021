package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static TestMatcher<User> user_matcher = TestMatcher.usingFieldsComparator(User.class,"registered", "password");

    public static final int admin_id = START_SEQ;
    public static final int user_id = START_SEQ + 1;
    public static final int not_found = 10;

    public static final User admin = new User(admin_id, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
    public static final User user = new User(user_id, "User", "user@yandex.ru", "password", Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass",false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(admin);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
