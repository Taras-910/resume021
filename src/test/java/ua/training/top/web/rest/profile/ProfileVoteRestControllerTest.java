package ua.training.top.web.rest.profile;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.top.AbstractControllerTest;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.testData.ResumeTestData.resume1_id;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.*;
import static ua.training.top.testData.VoteTestData.*;
import static ua.training.top.web.rest.profile.ProfileVoteRestController.REST_URL;

class ProfileVoteRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + vote1_id)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
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
    void getAllAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(vote_matcher.contentJson(List.of(vote1)));
    }

    @Test
    void setVote() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + resume1_id)
                .param("enabled", String.valueOf(true))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isNoContent());
    }
}
