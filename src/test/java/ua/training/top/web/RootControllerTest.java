package ua.training.top.web;

import org.junit.jupiter.api.Test;
import ua.training.top.testData.UserTestData;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.training.top.testData.TestUtil.userAuth;
import static ua.training.top.testData.UserTestData.admin;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        perform(get("/users")
                .with(userAuth(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"));
    }

    @Test
    void unAuth() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void getVacancies() throws Exception {
        perform(get("/resumes")
                .with(userAuth(UserTestData.user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("resumes"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/resumes.jsp"));
    }
}
