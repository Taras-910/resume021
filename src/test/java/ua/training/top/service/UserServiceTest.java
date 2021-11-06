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
        user_matcher.assertMatch(created, newUser);
        user_matcher.assertMatch(service.get(newId), newUser);
    }

    @Test
    public void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() throws Exception {
        service.delete(user_id);
        assertThrows(NotFoundException.class, () -> service.get(user_id));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(not_found));
    }

    @Test
    public void get() throws Exception {
        User user = service.get(user_id);
        user_matcher.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(not_found));
    }

    @Test
    public void getByEmail() throws Exception {
        User user = service.getByEmail("admin@gmail.com");
        user_matcher.assertMatch(user, admin);
    }

    @Test
    public void update() throws Exception {
        User updated = getUpdated();
        service.update(updated, admin_id);
        user_matcher.assertMatch(service.get(admin_id), getUpdated());
    }

    @Test
    public void getAll() throws Exception {
        List<User> all = service.getAll();
        user_matcher.assertMatch(all, admin, user);
    }
}
