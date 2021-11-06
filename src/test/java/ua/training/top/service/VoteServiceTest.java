package ua.training.top.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.top.model.Vote;
import ua.training.top.testData.VoteTestData;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.ResumeTestData.resume2_id;
import static ua.training.top.testData.UserTestData.*;
import static ua.training.top.testData.VoteTestData.*;
import static ua.training.top.util.DateTimeUtil.thisDay;

public class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    VoteService service;

    @Test
    public void get() throws Exception {
        setTestAuthorizedUser(admin);
        Vote vote = service.get(vote1_id);
        vote_matcher.assertMatch(vote1, vote);
    }

    @Test
    public void getNotFound() throws Exception {
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.get(not_found));
    }

    @Test
    public void getAll() throws Exception {
        setTestAuthorizedUser(admin);
        List<Vote> all = service.getAllForAuth();
        vote_matcher.assertMatch(allVotes(), all);
    }

    @Test
    public void getAllForAuthUser() throws Exception {
        setTestAuthorizedUser(admin);
        List<Vote> all = service.getAllForAuth();
        vote_matcher.assertMatch(List.of(vote1), all);
    }

    @Test
    public void delete() throws Exception {
        setTestAuthorizedUser(admin);
        service.delete(vote1_id);
        assertThrows(NotFoundException.class, () -> service.get(vote1_id));
    }

    @Test
    public void deletedNotFound() throws Exception {
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.delete(not_found));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.delete(vote2_id));
    }

    @Test
    public void update() throws Exception {
        service.update(vote1_id, resume2_id);
        setTestAuthorizedUser(admin);
        Vote expected = service.get(vote1_id);
        vote_matcher.assertMatch(expected, VoteTestData.getUpdated());
    }

    @Test
    public void updateIllegalArgument() throws Exception {
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.update(not_found, resume2_id));
    }

    @Test
    public void create() throws Exception {
        setTestAuthorizedUser(admin);
        Vote created = service.create(resume2_id);
        int newId = created.id();
        Vote newVote = new Vote(null, thisDay, resume2_id, admin_id);
        newVote.setId(newId);
        vote_matcher.assertMatch(newVote, created);
        vote_matcher.assertMatch(newVote, service.get(newId));
    }
}
