package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.to.ResumeTo;
import java.time.LocalDate;
import static ua.training.top.testData.ResumeTestData.RESUME1_ID;

public class ResumeToTestData {
    public static TestMatcher<ResumeTo> RESUME_TO_MATCHER = TestMatcher.usingFieldsComparator(ResumeTo.class, "freshen");

    public static final ResumeTo resumeTo1 = new ResumeTo(RESUME1_ID, "Middle Game Developer", "Петя", "21",
        "Киев, Лесной", 200, "SCM, 3 года",
            "https://grc.ua/vacancy/40006938?query=java","Java, spring Security",
            LocalDate.of(2021, 10, 20), "java", "middle", "киев", true);

    public static ResumeTo getNew() { return new ResumeTo(null, "NewJavaMiddle", "NewПетя", "22",
            "Киев", 500, "ЮЖД, 4 года, 4 месяца",
            "https://djinni.co/developers/?title=Java&exp_years=2y&employment=remote&region=other", "Java, Spring, Git, DataJPA",
            LocalDate.now(), "java", "middle", "киев", false);
    }

    public static ResumeTo getUpdate() {
        ResumeTo vTo = new ResumeTo(resumeTo1);
        vTo.setUrl("https://djinni.co/developers/?title=Java&exp_years=2y&employment=remote&region=other");
        vTo.setSalary(10000);
        vTo.setSkills("newDescription");
        return vTo;
    }

    public static ResumeTo getToUpdated() {
        return new ResumeTo(RESUME1_ID, "Updated Game Developer", "Юра", "23","Киев, Центр",
                777, "НИИ Сокол, 2 года", "https://www.updated.com?language=spring",
                "Java, Spring MVC", LocalDate.of(2021, 10, 20), "java",
                "middle", "киев", false);
    }

}
