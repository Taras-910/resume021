package ua.training.top.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.Vote;
import ua.training.top.testData.VoteTestData;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.ResumeTestData.resume2_id;
import static ua.training.top.testData.UserTestData.*;
import static ua.training.top.testData.VoteTestData.*;
import static ua.training.top.util.DateTimeUtil.thisDay;

class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    VoteService service;

    @BeforeEach
    void auth(){
        setTestAuthorizedUser(admin);
    }

    @Test
    void get() {
        Vote vote = service.get(vote1_id);
        vote_matcher.assertMatch(vote1, vote);
    }

    @Test
    void getNotFound() {
        validateRootCause(NotFoundException.class, () -> service.get(not_found));
    }

    @Test
    void getAll() {
        List<Vote> all = service.getAllForAuth();
        vote_matcher.assertMatch(allVotes(), all);
    }

    @Test
    void getAllForAuthUser() {
        List<Vote> all = service.getAllForAuth();
        vote_matcher.assertMatch(List.of(vote1), all);
    }

    @Test
    void delete() {
        service.delete(vote1_id);
        validateRootCause(NotFoundException.class, () -> service.get(vote1_id));
    }

    @Test
    void deletedNotFound() {
        validateRootCause(NotFoundException.class, () -> service.delete(not_found));
    }

    @Test
    void deleteNotOwn() {
        validateRootCause(NotFoundException.class, () -> service.delete(vote2_id));
    }

    @Test
    void update() {
        service.update(vote1_id, resume2_id);
        Vote expected = service.get(vote1_id);
        vote_matcher.assertMatch(expected, VoteTestData.getUpdated());
    }

    @Test
    void updateIllegalArgument() {
        validateRootCause(NotFoundException.class, () -> service.update(not_found, resume2_id));
    }

    @Test
    void create() {
        Vote created = service.create(resume2_id);
        int newId = created.id();
        Vote newVote = new Vote(null, thisDay, resume2_id, admin_id);
        newVote.setId(newId);
        vote_matcher.assertMatch(newVote, created);
        vote_matcher.assertMatch(newVote, service.get(newId));
    }
}
