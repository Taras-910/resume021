package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.repository.ResumeRepository;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.ResumeUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.aggregator.installation.InstallationUtil.limitResumesKeeping;
import static ua.training.top.aggregator.installation.InstallationUtil.reasonPeriodKeeping;
import static ua.training.top.model.Goal.FILTER;
import static ua.training.top.util.FreshenUtil.getFreshenFromTo;
import static ua.training.top.util.ResumeCheckUtil.*;
import static ua.training.top.util.ResumeUtil.*;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;
import static ua.training.top.util.ValidationUtil.checkValidUrl;

@Service
public class ResumeService {
    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);
    private final ResumeRepository repository;
    private final VoteService voteService;
    private final FreshenService freshenService;

    public ResumeService(ResumeRepository repository, VoteService voteService, FreshenService freshenService) {
        this.repository = repository;
        this.voteService = voteService;
        this.freshenService = freshenService;
    }

    public Resume get(int id) {
        log.info("get id {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public ResumeTo getTo(int id) {
        log.info("getTo resume {}", id);
        return ResumeUtil.getTo(get(id), voteService.getAllForAuth());
    }

    public List<Resume> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public List<Resume> getByUserId(int userId) {
        log.info("getAllByUserId");
        return repository.getByUserId(userId);
    }

    public List<ResumeTo> getAllTos() {
        log.info("getAllTos for user {}", authUserId());
        return ResumeUtil.getTos(getAll(), voteService.getAllForAuth());
    }

    @Transactional
    public List<ResumeTo> getTosByFilter(Freshen f) {
        log.info("getTosByFilter language={} level={} workplace={}", f.getLanguage(), f.getLevel(), f.getWorkplace());
        f.setGoals(Collections.singleton(FILTER));
        freshenService.create(f);
        return getTos(getFilterLanguage(repository.getByFilter(f), f), voteService.getAllForAuth());
    }

    @Transactional
    public Resume updateTo(ResumeTo resumeTo) {
        log.info("update resumeTo {}", resumeTo);
        checkValidUrl(resumeTo.getUrl());
        Resume resumeDb = get(resumeTo.id());
        Resume resume = fromToForUpdate(resumeTo, resumeDb);
        checkNotOwnUpdate(resumeDb.getFreshen().getUserId());
        if (isNotSimilar(resumeDb, resumeTo)) {
            voteService.deleteListByResumeId(resumeTo.id());
        }
        return repository.save(resume);
    }

    @Transactional
    public Resume create(ResumeTo resumeTo) {
        log.info("create resumeTo={}", resumeTo);
        isNullPointerException(resumeTo);
        checkExistResumeForUser(repository.getByUserId(authUserId()));
        checkValidUrl(resumeTo.getUrl());
        Freshen newFreshen = freshenService.create(getFreshenFromTo(resumeTo));
        Resume resume = new Resume(fromTo(resumeTo));
        resume.setFreshen(newFreshen);
        return repository.save(resume);
    }

    @Transactional
    public List<Resume> createUpdateList(List<Resume> resumes) {
        log.info("createUpdateList size={}", resumes.size());
        return repository.saveAll(resumes);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        int userId = checkNotFoundWithId(repository.get(id), id).getFreshen().getUserId();
        checkNotOwnDelete(userId);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Transactional
    public List<Resume> deleteOutDatedAndGetAll() {
        log.info("deleteOutDateAndGetAll reasonPeriodKeeping {}", reasonPeriodKeeping);
        freshenService.deleteOutDated(LocalDateTime.of(reasonPeriodKeeping, LocalTime.MIN));
        List<Resume> resumes = repository.deleteOutDated(reasonPeriodKeeping);
        voteService.deleteOutDated(reasonPeriodKeeping);
        return resumes;
    }

    @Transactional
    public void deleteExceedLimitHeroku(int totalNumber) {
        log.info("deleteExceedLimitHeroku totalNumber {}", totalNumber);
        int exceedNumber = totalNumber - limitResumesKeeping;
        if (exceedNumber > 0) {
            log.info("start delete exceedNumber {}", exceedNumber);
            repository.deleteExceedLimit(exceedNumber);
            freshenService.deleteExceedLimit(limitResumesKeeping / 2 + 1);
            voteService.deleteExceedLimit(limitResumesKeeping / 5);
        }
    }

}
