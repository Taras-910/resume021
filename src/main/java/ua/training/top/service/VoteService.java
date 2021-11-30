package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.model.Vote;
import ua.training.top.repository.VoteRepository;

import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.aggregator.installation.Installation.limitVoteKeeping;
import static ua.training.top.aggregator.installation.Installation.reasonPeriodKeeping;
import static ua.training.top.util.DateTimeUtil.thisDay;
import static ua.training.top.util.ValidationUtil.checkNotFoundData;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final VoteRepository repository;

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public Vote get(int id) {
        int userId = authUserId();
        log.info("get by id {} for user {}", id, userId);
        Vote vote = repository.get(id, userId);
        return checkNotFoundWithId(vote, id);
    }

    public List<Vote> getAll() {
        log.info("getAll votes");
        return repository.getAll();
    }

    public List<Vote> getAllForAuth() {
        log.info("get all for User {}", authUserId());
        return repository.getAllForAuth(authUserId());
    }

    @Transactional
    public Vote create(int resumeId) {
        log.info("create vote for resumeId {}", resumeId);
        Vote vote = new Vote(null, thisDay, resumeId, authUserId());
        return repository.save(vote, authUserId());
    }

    @Transactional
    public void update(int voteId, int resumeId) {
        log.info("update vote {} for resumeId {} of user {}", voteId, resumeId, authUserId());
        Vote vote = new Vote(voteId, thisDay, resumeId, authUserId());
        Assert.notNull(vote, "vote must not be null");
        checkNotFoundWithId(repository.save(vote, authUserId()), voteId);
    }

    @Transactional
    public void delete(int id) {
        log.info("delete vote {} for userId {}", id, authUserId());
        checkNotFoundWithId(repository.delete(id, authUserId()), id);
    }

    @Transactional
    public void deleteListByResumeId(int resumeId) {
        checkNotFoundData(repository.deleteListByResumeId(resumeId), resumeId);
    }

    @Transactional
    public void setVote(int resumeId, boolean toVote) {
        log.info(toVote ? "enable {}" : "disable {}", resumeId);
        if (!toVote) {
            log.info("deleteByResumeId {}", resumeId);
            checkNotFoundWithId(repository.deleteByResumeId(resumeId, authUserId()), resumeId);
        } else {
            log.info("create vote for resumeId {} userId {}", resumeId, authUserId());
            create(resumeId);
        }
    }

    @Transactional
    public void deleteOutDated() {
        log.info("deleteOutDated reasonPeriodToKeep {}", reasonPeriodKeeping);
        repository.deleteOutDated(reasonPeriodKeeping);
    }

    @Transactional
    public void deleteExceed() {
        log.info("deleteExceedLimit limitVote {}", limitVoteKeeping);
        repository.deleteExceedLimit(limitVoteKeeping);
    }
}
