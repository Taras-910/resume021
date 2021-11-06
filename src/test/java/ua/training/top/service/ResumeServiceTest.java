package ua.training.top.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.top.model.Resume;
import ua.training.top.testData.ResumeToTestData;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.ResumeUtil;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.ResumeTestData.*;
import static ua.training.top.testData.ResumeToTestData.resume_to_matcher;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.testData.UserTestData.not_found;
import static ua.training.top.util.DateTimeUtil.testDate;
import static ua.training.top.util.ResumeUtil.fromTo;

public class ResumeServiceTest extends AbstractServiceTest {

    @Autowired
    private ResumeService resumeService;
    @Autowired
    private VoteService voteService;

    @Test
    public void get() throws Exception {
        Resume vacancy = resumeService.get(resume1_id);
        resume_matcher.assertMatch(vacancy, resume1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> resumeService.get(not_found));
    }

    @Test
    public void getAll() throws Exception {
        List<Resume> all = resumeService.getAll();
        resume_matcher.assertMatch(all, RESUMES_GET_ALL);
    }

    @Test
    public void delete() throws Exception {
        setTestAuthorizedUser(admin);
        resumeService.delete(resume1_id);
        assertThrows(NotFoundException.class, () -> resumeService.delete(resume1_id));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> resumeService.delete(not_found));
    }

    @Test
    public void createErrorData() throws Exception {
        setTestAuthorizedUser(admin);
        assertThrows(Exception.class, () -> resumeService.create(new ResumeTo(null, null, "Виктор", "20", "London", 100, "IBM", "https://www.ukr.net/1/11", "Microsoft", testDate, "java", "middle", "киев", false)));
        assertThrows(Exception.class, () -> resumeService.create(new ResumeTo(null, "Developer", null, "21", "London", 100, "IBM", "https://www.ukr.net/1/11", "Microsoft", testDate, "java", "middle", "киев", false)));
        assertThrows(Exception.class, () -> resumeService.create(new ResumeTo(null, "Developer", "Виктор", "22", null, 100, "IBM", "https://www.ukr.net/1/11", "Microsoft", testDate, "java", "middle", "киев", false)));
        assertThrows(Exception.class, () -> resumeService.create(new ResumeTo(null, "Developer", "Виктор", "23", "London", 100, "IBM", "https://www.ukr.net/1/11", null, testDate, "java", "middle", "киев", false)));
    }

    @Test
    public void createListOfResumes() throws Exception {
        setTestAuthorizedUser(admin);
        List<Resume> actual = List.of(resume3, resume4);
        List<Resume> created = resumeService.createUpdateList(actual);
        for (int i = 0; i < created.size(); i++) {
            actual.get(i).setId(created.get(i).getId());
        }
        resume_matcher.assertMatch(created, actual);
    }

    @Test
    public void createListErrorData() throws Exception {
        assertThrows(NullPointerException.class, () -> resumeService.createUpdateList(List.of(null, new Resume(resume3))));
        assertThrows(NullPointerException.class, () -> resumeService.createUpdateList(null));
    }

    @Test
    public void update() throws Exception {
        setTestAuthorizedUser(admin);
        ResumeTo vTo = ResumeToTestData.getUpdate();
        Resume updated = resumeService.updateTo(vTo);
        resume_matcher.assertMatch(fromTo(vTo), updated);
    }

    @Test
    public void updateErrorData() throws Exception {
        setTestAuthorizedUser(admin);
        assertThrows(NullPointerException.class, () -> resumeService.updateTo(new ResumeTo()));
    }

    @Test
    public void create() throws Exception {
        setTestAuthorizedUser(admin);
        ResumeTo newResumeTo = new ResumeTo(ResumeToTestData.getNew());
        Resume createdResume = resumeService.create(newResumeTo);
        int newIdResume = createdResume.id();
        newResumeTo.setId(newIdResume);
        resume_matcher.assertMatch(createdResume, fromTo(newResumeTo));
        resume_to_matcher.assertMatch(ResumeUtil.getTo(resumeService.get(newIdResume),
                voteService.getAllForAuth()), newResumeTo);
    }
}
