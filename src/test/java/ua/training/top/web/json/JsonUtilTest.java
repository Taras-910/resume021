package ua.training.top.web.json;

import org.junit.jupiter.api.Test;
import ua.training.top.model.Resume;
import ua.training.top.model.User;
import ua.training.top.testData.ResumeTestData;
import ua.training.top.testData.UserTestData;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.training.top.testData.ResumeTestData.*;

class JsonUtilTest {

    @Test
    void readWriteValue() {
        String json = JsonUtil.writeValue(resume1);
        System.out.println(json);
        Resume vacancy = JsonUtil.readValue(json, Resume.class);
        resume_matcher.assertMatch(vacancy, resume1);
    }

    @Test
    void readWriteValues() {
        String json = JsonUtil.writeValue(getListResumes());
        System.out.println(json);
        List<Resume> resumes = JsonUtil.readValues(json, Resume.class);
        resume_matcher.assertMatch(resumes, ResumeTestData.getListResumes());
    }

    @Test
    void writeOnlyAccess() {
        String json = JsonUtil.writeValue(UserTestData.user);
        System.out.println(json);
        assertThat(json, containsString("password"));
        String jsonWithPass = UserTestData.jsonWithPassword(UserTestData.user, "newPass");
        System.out.println(jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        assertEquals(user.getPassword(), "newPass");
    }
}
