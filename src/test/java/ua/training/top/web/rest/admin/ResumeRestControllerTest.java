package ua.training.top.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Resume;
import ua.training.top.service.ResumeService;
import ua.training.top.service.VoteService;
import ua.training.top.testData.ResumeToTestData;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.ResumeUtil;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.AbstractControllerTest;
import ua.training.top.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.ResumeTestData.*;
import static ua.training.top.testData.ResumeToTestData.resume_to_matcher;
import static ua.training.top.testData.TestUtil.readFromJson;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.testData.UserTestData.not_found;
import static ua.training.top.util.ResumeUtil.fromTo;
import static ua.training.top.util.ResumeUtil.getTos;

@Transactional
class ResumeRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ResumeRestController.REST_URL + '/';
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private VoteService voteService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + resume1_id)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(resume_to_matcher.contentJson(ResumeUtil.getTo(resume1, voteService.getAllForAuth())));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + not_found)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(resume_to_matcher.contentJson(getTos(List.of(resume1, resume2), voteService.getAllForAuth())));
    }

    @Test
    @Transactional
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + resume1_id)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> resumeService.delete(resume1_id));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + not_found)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void update() throws Exception {
        ResumeTo updated = new ResumeTo(ResumeToTestData.getToUpdated());
        perform(MockMvcRequestBuilders
                .put(REST_URL + resume1_id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        resume_matcher.assertMatch(fromTo(updated), resumeService.get(resume1_id));
    }

    @Test
    @Transactional
    void updateInvalid() throws Exception {
        ResumeTo invalid = new ResumeTo(ResumeToTestData.getToUpdated());
        invalid.setTitle(null);
        perform(MockMvcRequestBuilders.put(REST_URL + resume2_id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    void create() throws Exception {
        ResumeTo newResumeTo = new ResumeTo(ResumeToTestData.getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newResumeTo)))
                .andDo(print())
                .andExpect(status().isCreated());
        Resume createdResume = readFromJson(action, Resume.class);
        int newIdResume = createdResume.id();
        newResumeTo.setId(newIdResume);
        resume_matcher.assertMatch(createdResume, fromTo(newResumeTo));
        setTestAuthorizedUser(admin);
        resume_to_matcher.assertMatch(ResumeUtil.getTo(resumeService.get(newIdResume),
                voteService.getAllForAuth()), newResumeTo);
    }


    @Test
    @Transactional
    void createInvalid() throws Exception {
        ResumeTo invalid = new ResumeTo(ResumeToTestData.getNew());
        invalid.setTitle(null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getByFilter() throws Exception {
        setTestAuthorizedUser(admin);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("language", "php")
                .param("workplace", "киев")
                .param("level", "middle")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(resume_to_matcher.contentJson(getTos(List.of(resume1), voteService.getAllForAuth())));
    }

    @Test
    void getByFilterInvalid() throws Exception {
        setTestAuthorizedUser(admin);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("language", "")
                .param("workplace", "")
                .param("level", "")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + resume1_id))
                .andExpect(status().isUnauthorized());
    }
}
