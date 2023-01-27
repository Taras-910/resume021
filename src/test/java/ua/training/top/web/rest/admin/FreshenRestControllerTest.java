package ua.training.top.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.AbstractControllerTest;
import ua.training.top.model.Freshen;
import ua.training.top.service.FreshenService;
import ua.training.top.testData.FreshenTestData;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.testData.FreshenTestData.*;
import static ua.training.top.testData.TestUtil.readFromJson;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.web.rest.admin.FreshenRestController.REST_URL;

class FreshenRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';
    @Autowired
    private FreshenService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + freshen1_id)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(freshen_matcher.contentJson(freshen1));
    }

    @Test
    void getAll() throws Exception {
        Iterable<Freshen> iterable = List.of(freshen1, freshen2);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(freshen_matcher.contentJson(iterable));
    }

    @Test
    @Transactional
    void create() throws Exception {
        Freshen newFreshen = new Freshen(FreshenTestData.getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(JsonUtil.writeValue(newFreshen)))
                .andExpect(status().isCreated());
        Freshen created = readFromJson(action, Freshen.class);
        newFreshen.setId(created.getId());
        freshen_matcher.assertMatch(created, newFreshen);
        freshen_matcher.assertMatch(service.get(created.getId()), newFreshen);
    }

    @Test
    @Transactional
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + freshen1_id)
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(freshen1_id));
    }

    @Test
    @Transactional
    void update() throws Exception {
        Freshen updated = new Freshen(FreshenTestData.getUpdated());
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + freshen1_id)
                .with(userHttpBasic(admin)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        freshen_matcher.assertMatch(service.get(freshen1_id), updated);
    }
}
