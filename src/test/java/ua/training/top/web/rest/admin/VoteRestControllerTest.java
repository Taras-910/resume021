package ua.training.top.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.top.AbstractControllerTest;
import ua.training.top.model.Vote;
import ua.training.top.service.VoteService;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.json.JsonUtil;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.ResumeTestData.resume2_id;
import static ua.training.top.testData.TestUtil.readFromJson;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.*;
import static ua.training.top.testData.VoteTestData.*;
import static ua.training.top.web.rest.admin.VoteRestController.REST_URL;

class VoteRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';
    @Autowired
    private VoteService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + vote1_id)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(vote_matcher.contentJson(vote1));
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
    void getAll() throws Exception {
        Iterable<Vote> iterable = List.of(vote1, vote2);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(vote_matcher.contentJson(iterable));
    }

    @Test
    void getAllAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "auth")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(vote_matcher.contentJson(List.of(vote1)));
    }

    @Test
    void create() throws Exception {
        Vote newVote = new Vote(null, LocalDate.now(), resume2_id, admin_id);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("resumeId", String.valueOf(resume2_id))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        newVote.setId(created.getId());
        vote_matcher.assertMatch(created, newVote);
        setTestAuthorizedUser(admin);
        vote_matcher.assertMatch(service.get(created.getId()), newVote);
    }

    @Test
    void createForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        Vote updated = new Vote(vote1);
        updated.setResumeId(resume2_id);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + vote1_id)
                .param("resumeId", String.valueOf(resume2_id))
                .with(userHttpBasic(admin)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        vote_matcher.assertMatch(service.get(vote1_id), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + vote1_id)
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.get(vote1_id));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + not_found)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void setVote() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + resume2_id)
                .param("toVote", String.valueOf(true))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isOk());
    }
}
