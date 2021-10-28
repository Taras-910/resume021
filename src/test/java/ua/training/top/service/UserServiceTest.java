package ua.training.top.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.testData.UserTestData;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ua.training.top.testData.UserTestData.*;

public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void create() throws Exception {
        User created = service.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    public void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() throws Exception {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void get() throws Exception {
        User user = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getByEmail() throws Exception {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    public void update() throws Exception {
        User updated = getUpdated();
        service.update(updated, ADMIN_ID);
        USER_MATCHER.assertMatch(service.get(ADMIN_ID), getUpdated());
    }

    @Test
    public void getAll() throws Exception {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, user);
    }
}
