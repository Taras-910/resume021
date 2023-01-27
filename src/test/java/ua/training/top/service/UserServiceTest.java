package ua.training.top.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.User;
import ua.training.top.testData.UserTestData;
import ua.training.top.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static ua.training.top.model.Role.USER;
import static ua.training.top.testData.UserTestData.*;

class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService service;

    @Test
    void get() {
        User user = service.get(user_id);
        user_matcher.assertMatch(user, UserTestData.user);
    }

    @Test
    void getNotFound() {
        validateRootCause(NotFoundException.class, () -> service.get(not_found));
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        user_matcher.assertMatch(user, admin);
    }

    @Test
    void getAll() {
        List<User> all = service.getAll();
        user_matcher.assertMatch(all, admin, user);
    }

    @Test
    void create() {
        User created = service.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        user_matcher.assertMatch(created, newUser);
        user_matcher.assertMatch(service.get(newId), newUser);
    }

    @Test
    void createDuplicate() {
        Assertions.assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", USER)));
    }

    @Test
    void createInvalid(){
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, null, "mail@yandex.ru", "password", USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "  ", "mail@yandex.ru", "password", USER)));
        validateRootCause(NullPointerException.class, () -> service.create(new User(null, "User", null, "password", USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "  ", "password", USER)));
        validateRootCause(IllegalArgumentException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", null, USER)));
    }


    @Test
    void update() {
        User updated = getUpdated();
        service.update(updated, admin_id);
        user_matcher.assertMatch(service.get(admin_id), getUpdated());
    }

    @Test
    void updateInvalid(){
        validateRootCause(ConstraintViolationException.class, () -> service.update(new User(null, null, "mail@yandex.ru", "password", USER), user_id));
        validateRootCause(ConstraintViolationException.class, () -> service.update(new User(null, "  ", "mail@yandex.ru", "password", USER), user_id));
        validateRootCause(NullPointerException.class, () -> service.update(new User(null, "user", null, "password", USER), user_id));
        validateRootCause(IllegalArgumentException.class, () -> service.update(new User(null, "user", "  ", "password", USER), user_id));
        validateRootCause(IllegalArgumentException.class, () -> service.update(new User(null, "user", "mail@yandex.ru", null, USER), user_id));
    }

    @Test
    void delete() {
        service.delete(user_id);
        validateRootCause(NotFoundException.class, () -> service.get(user_id));
    }

    @Test
    void deletedNotFound() {
        validateRootCause(NotFoundException.class, () -> service.delete(not_found));
    }

}
