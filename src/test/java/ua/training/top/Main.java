package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.model.Freshen;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.service.*;
import ua.training.top.to.ResumeTo;
import ua.training.top.web.rest.admin.ResumeRestController;

import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.strategy.provider.ProviderUtil.getAllProviders;
import static ua.training.top.model.Goal.*;
import static ua.training.top.testData.UserTestData.*;
import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.UserUtil.asAdmin;

public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext appCtx =
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"}, false);
        appCtx.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile());
        appCtx.refresh();

//        User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
//        User admin = new User(100000, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
        setTestAuthorizedUser(admin);
//        setTestAuthorizedUser(user);

/*
        System.out.println("Bean definition names: ");
        System.out.println("========================================");
        for(String s : appCtx.getBeanDefinitionNames()) {
            System.out.println(s);
        }
        System.out.println("========================================");
*/
        Freshen freshen = asNewFreshen("java", "middle", "киев", UPGRADE);


//        ResumeRestController resumeRestController = appCtx.getBean(ResumeRestController.class);
//        FreshenService freshenService = appCtx.getBean(FreshenService.class);
//        AggregatorService aggregatorService = appCtx.getBean(AggregatorService.class);
        UserService userService = appCtx.getBean(UserService.class);
        ResumeService resumeService = appCtx.getBean(ResumeService.class);
        VoteService voteService = appCtx.getBean(VoteService.class);

//        List<ResumeTo> resumeTos = resumeService.getTosByFilter(freshen);
//        System.out.println("resumeTos="+resumeTos);


        System.out.println("========================================");

//        System.out.println("resume="+resumeService.get(100013));
//        System.out.println("freshen="+resumeService.get(100013).getFreshen());
        System.out.println("userId="+resumeService.get(100218).getFreshen().getUserId());
//        System.out.println("admin="+userService.asAdmin());
//        System.out.println("authUserId()="+authUserId());
//        System.out.println("asAdmin().id()="+asAdmin().id());
//        System.out.println("SecurityUtil.get()="+SecurityUtil.get());
//        int userId = resumeService.get(100013).getFreshen().getUserId();


        System.out.println(".............................................................................");
        appCtx.close();
    }
}
/*   void getByFilter() throws Exception {
        setTestAuthorizedUser(admin);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("language", "java")
                .param("workplace", "киев")
                .param("level", "middle")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(RESUME_TO_MATCHER.contentJson(getTos(List.of(resume2), voteService.getAllForAuth())));
*/
