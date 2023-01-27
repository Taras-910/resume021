package ua.training.top.web.rest.admin;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.AbstractControllerTest;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.service.UserService;
import ua.training.top.testData.UserTestData;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.json.JsonUtil;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.testData.TestUtil.readFromJson;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.*;
import static ua.training.top.util.exception.ErrorType.DATA_ERROR;
import static ua.training.top.util.exception.ErrorType.VALIDATION_ERROR;
import static ua.training.top.web.rest.admin.UserRestController.REST_URL;

class UserRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService service;
    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setup() {
        Objects.requireNonNull(cacheManager.getCache("users")).clear();
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + admin_id)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(user_matcher.contentJson(admin));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + not_found)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + admin_id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by?email=" + admin.getEmail())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(user_matcher.contentJson(admin));
    }

    @Test
    void getAll() throws Exception {
        Iterable<User> iterable = List.of(admin, user);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(user_matcher.contentJson(iterable));
    }

    @Test
    void create() throws Exception {
        User newUser = new User(getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTestData.jsonWithPassword(newUser, "newPass"))
                .content(JsonUtil.writeValue(newUser)))
                .andDo(print())
                .andExpect(status().isCreated());
        User created = readFromJson(action, User.class);
        newUser.setId(created.getId());
        user_matcher.assertMatch(created, newUser);
        user_matcher.assertMatch(service.get(created.getId()), newUser);
    }

    @Test
    void createInvalid() throws Exception {
        User invalid = new User(null, null, "", "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(jsonWithPassword(invalid, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        User expected = new User(null, "New", "user@yandex.ru", "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(jsonWithPassword(expected, "newPass")))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(DATA_ERROR));
    }
    @Test
    void register() throws Exception {
        User newUser = new User(null, "newName", "newemail@ya.ru", "newPassword", Role.USER);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "register")
                .with(userHttpBasic(admin)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUser)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int newId = created.id();
        newUser.setId(newId);
        user_matcher.assertMatch(created, newUser);
        user_matcher.assertMatch(service.get(newId), newUser);
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + user_id)
                .param("enabled", "false")
                .with(userHttpBasic(admin)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(service.get(user_id).isEnabled());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + user_id)
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(user_id));
    }

    @Test
    @Transactional
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + admin_id)
                .with(userHttpBasic(admin)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        user_matcher.assertMatch(service.get(admin_id), updated);
    }

    @Test
    void updateInvalid() throws Exception {
        User invalid = new User(user);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + user_id)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(jsonWithPassword(invalid, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        User updated = new User(user);
        updated.setEmail("admin@gmail.com");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + user_id)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(jsonWithPassword(updated, "password")))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(DATA_ERROR));
    }

    @Test
    void updateForbidden() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + admin_id)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        User invalid = new User(user);
        invalid.setName(user.getName()+"<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + user_id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
        User userDB = service.get(user_id);
        assertEquals(userDB.getName(), user.getName());
    }

}
