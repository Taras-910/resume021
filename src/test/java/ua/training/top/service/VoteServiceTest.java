package ua.training.top.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.top.model.Vote;
import ua.training.top.testData.VoteTestData;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.ResumeTestData.RESUME2_ID;
import static ua.training.top.testData.UserTestData.*;
import static ua.training.top.testData.VoteTestData.*;
import static ua.training.top.util.DateTimeUtil.thisDay;

public class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    VoteService service;

    @Test
    public void get() throws Exception {
        setTestAuthorizedUser(admin);
        Vote vote = service.get(VOTE1_ID);
        VOTE_MATCHER.assertMatch(vote1, vote);
    }

    @Test
    public void getNotFound() throws Exception {
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getAll() throws Exception {
        setTestAuthorizedUser(admin);
        List<Vote> all = service.getAllForAuth();
        VOTE_MATCHER.assertMatch(allVotes(), all);
    }

    @Test
    public void getAllForAuthUser() throws Exception {
        setTestAuthorizedUser(admin);
        List<Vote> all = service.getAllForAuth();
        VOTE_MATCHER.assertMatch(List.of(vote1), all);
    }

    @Test
    public void delete() throws Exception {
        setTestAuthorizedUser(admin);
        service.delete(VOTE1_ID);
        assertThrows(NotFoundException.class, () -> service.get(VOTE1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.delete(VOTE2_ID));
    }

    @Test
    public void update() throws Exception {
        service.update(VOTE1_ID, RESUME2_ID);
        setTestAuthorizedUser(admin);
        Vote expected = service.get(VOTE1_ID);
        VOTE_MATCHER.assertMatch(expected, VoteTestData.getUpdated());
    }

    @Test
    public void updateIllegalArgument() throws Exception {
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.update(NOT_FOUND, RESUME2_ID));
    }

    @Test
    public void create() throws Exception {
        setTestAuthorizedUser(admin);
        Vote created = service.create(RESUME2_ID);
        int newId = created.id();
        Vote newVote = new Vote(null, thisDay, RESUME2_ID, ADMIN_ID);
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(newVote, created);
        VOTE_MATCHER.assertMatch(newVote, service.get(newId));
    }
}
