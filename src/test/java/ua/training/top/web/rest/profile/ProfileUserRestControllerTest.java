package ua.training.top.web.rest.profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.service.UserService;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.AbstractControllerTest;
import ua.training.top.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.TestUtil.readFromJson;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.USER_MATCHER;
import static ua.training.top.testData.UserTestData.user;

class ProfileUserRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileUserRestController.REST_URL;

    @Autowired
    private ProfileUserRestController controller;
    @Autowired
    UserService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(user);
        assertThrows(NotFoundException.class, () -> controller.delete());
    }

    @Test
    @Transactional
    void update() throws Exception {
        User updated = new User(user);
        updated.setName("NewName");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(user);
        USER_MATCHER.assertMatch(controller.get(), updated);
    }

    @Test
    void register() throws Exception {
        User newUser = new User(null, "newName", "newemail@ya.ru", "newPassword", Role.USER);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/register")
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
}
