package ua.training.top.web.rest.profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;
import ua.training.top.model.Resume;
import ua.training.top.service.FreshenService;
import ua.training.top.service.ResumeService;
import ua.training.top.testData.FreshenTestData;
import ua.training.top.web.AbstractControllerTest;
import ua.training.top.web.json.JsonUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.installation.Installation.setTestProvider;
import static ua.training.top.aggregator.installation.Installation.setTestReasonPeriodToKeep;
import static ua.training.top.aggregator.strategy.TestStrategy.getTestList;
import static ua.training.top.testData.FreshenTestData.*;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.testData.UserTestData.user;
import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.ResumeUtil.fromTos;

class ProfileFreshenRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileFreshenRestController.REST_URL + '/';
    @Autowired
    private FreshenService freshenService;
    @Autowired
    private ResumeService resumeService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + freshen2_id)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(freshen_matcher.contentJson(freshen2));
    }

    @Test
    void getOwn() throws Exception {
        Iterable<Freshen> freshens = List.of(freshen2);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(freshen_matcher.contentJson(freshens));
    }

    @Test
    @Transactional
    void refreshDB() throws Exception {
        Freshen freshen = new Freshen(null, null, "Java", "middle", "Киев", Collections.singleton(Goal.UPGRADE), null);
        List<Resume> resumesDbBefore = resumeService.getAll();
        List<Freshen> freshensDbBefore = freshenService.getAll();
        setTestProvider();
        setTestAuthorizedUser(admin);
        setTestReasonPeriodToKeep();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(asNewFreshen(freshen))))
                .andDo(print())
                .andExpect(status().isNoContent());

        Freshen fr = freshenService.create(FreshenTestData.getNew());
        List<Freshen> allFreshens = freshenService.getAll();
        /*https://stackoverflow.com/questions/9933403/subtracting-one-arraylist-from-another-arraylist*/
        freshensDbBefore.forEach(allFreshens::remove);
        Freshen newFreshen = allFreshens.get(0);
        fr.setRecordedDate(newFreshen.getRecordedDate());
        fr.setUserId(newFreshen.getUserId());
        fr.setId(newFreshen.getId());
        freshen_matcher.assertMatch(newFreshen, fr);

        List<Resume> resumesTest = fromTos(getTestList());
        List<Resume> allResumes = resumeService.getAll();
        List<Resume> newVacancies = allResumes.stream()
                .filter(r -> !resumesDbBefore.contains(r))
                .collect(Collectors.toList());
        assertEquals((int) resumesTest.stream()
                .filter(r -> !newVacancies.contains(r))
                .count(), 0);
    }
}

