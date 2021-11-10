package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.model.Freshen;
import ua.training.top.service.AggregatorService;
import ua.training.top.service.FreshenService;
import ua.training.top.service.ResumeService;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.util.FreshenUtil.asNewFreshen;

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
        System.out.println("=".repeat(40));
        for(String s : appCtx.getBeanDefinitionNames()) {
            System.out.println(s);
        }
        System.out.println("=".repeat(40));
*/
        Freshen freshen = asNewFreshen("java", "middle", "киев", UPGRADE);


//        ResumeRestController resumeRestController = appCtx.getBean(ResumeRestController.class);
        FreshenService freshenService = appCtx.getBean(FreshenService.class);
//        AggregatorService aggregatorService = appCtx.getBean(AggregatorService.class);
//        UserService userService = appCtx.getBean(UserService.class);
        ResumeService resumeService = appCtx.getBean(ResumeService.class);
        AggregatorService aggregatorService = appCtx.getBean(AggregatorService.class);
//        FreshenService freshenService = appCtx.getBean(FreshenService.class);
//        ResumeRepository resumeRepository = appCtx.getBean(ResumeRepository.class);
//        VoteService voteService = appCtx.getBean(VoteService.class);

        System.out.println("=".repeat(40));
        int countBefore = resumeService.getAll().size();
        resumeService.deleteExceedLimitHeroku(3);
        int countAfter = resumeService.getAll().size();
        System.out.println("countBefore ="+ countBefore);
        System.out.println("countAfter  ="+ countAfter);

        System.out.println(".".repeat(40));
        appCtx.close();
    }

}
