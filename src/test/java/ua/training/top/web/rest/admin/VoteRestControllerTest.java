package ua.training.top.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.top.model.Vote;
import ua.training.top.service.VoteService;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.AbstractControllerTest;
import ua.training.top.web.json.JsonUtil;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.ResumeTestData.resume2_id;
import static ua.training.top.testData.TestUtil.readFromJson;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.testData.UserTestData.admin_id;
import static ua.training.top.testData.VoteTestData.*;

class VoteRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteRestController.REST_URL + '/';
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private VoteService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + vote1_id)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(vote_matcher.contentJson(vote1));
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
    void getAllForAuthUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "auth")
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
                .with(userHttpBasic(admin)))
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        newVote.setId(created.getId());
        vote_matcher.assertMatch(created, newVote);
        setTestAuthorizedUser(admin);
        vote_matcher.assertMatch(service.get(created.getId()), newVote);
    }

    @Test
    void update() throws Exception {
        Vote updated = new Vote(vote1);
        updated.setResumeId(resume2_id);
        perform(MockMvcRequestBuilders.put(REST_URL + vote1_id)
                .param("resumeId", String.valueOf(resume2_id))
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        vote_matcher.assertMatch(service.get(vote1_id), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + vote1_id)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.get(vote1_id));
    }

    @Test
    void setVote() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + resume2_id)
                .param("toVote", String.valueOf(true))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk());
    }
}
