package ua.training.top.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.service.UserService;
import ua.training.top.testData.UserTestData;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.AbstractControllerTest;
import ua.training.top.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.testData.TestUtil.readFromJson;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.*;

class UserRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = UserRestController.REST_URL + '/';
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by?email=" + admin.getEmail())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    @Transactional
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + ADMIN_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(service.get(ADMIN_ID), updated);
    }

    @Test
    void create() throws Exception {
        User newUser = new User(getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTestData.jsonWithPassword(newUser, "newPass"))
                .content(JsonUtil.writeValue(newUser)))
                .andDo(print())
                .andExpect(status().isCreated());
        User created = readFromJson(action, User.class);
        newUser.setId(created.getId());
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(created.getId()), newUser);
    }

    @Test
    void register() throws Exception {
        User newUser = new User(null, "newName", "newemail@ya.ru", "newPassword", Role.USER);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUser)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    void getAll() throws Exception {
        Iterable<User> iterable = List.of(admin, user);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(iterable));
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + USER_ID)
                .param("enabled", "false")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(service.get(USER_ID).isEnabled());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }
}
