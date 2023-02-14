package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.util.ResumeUtil;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.Installation.limitResumesKeeping;
import static ua.training.top.aggregator.ProviderUtil.getAllProviders;
import static ua.training.top.util.InformUtil.finish_message;
import static ua.training.top.util.ResumeUtil.*;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.aggregateUtil.data.CommonUtil.isMatch;

@Service
@EnableScheduling
public class AggregatorService {
    private final static Logger log = LoggerFactory.getLogger(AggregatorService.class);
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private FreshenService freshenService;
    @Autowired
    private RateService rateService;
    @Autowired
    private VoteService voteService;

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB by freshen {}", freshen);
        boolean isRateOld = rateService.CurrencyRatesMapInit();
        Instant start = Instant.now();
        List<Resume> resumesNet = fromTos(getAllProviders().selectBy(freshen));
        if (!resumesNet.isEmpty()) {
            Freshen newFreshen = freshenService.create(freshen);
            List<Resume>
                    resumesDb = resumeService.getAll(),
                    resumesForCreate = new ArrayList<>(),
                    resumesForUpdate = new ArrayList<>();
            Map<String, Resume> mapDb = resumesDb.stream()
                    .collect(Collectors.toMap(ResumeUtil::getAnchor, r -> r));
            resumesNet.forEach(r -> {
                r.setFreshen(newFreshen);
                if (mapDb.containsKey(getAnchor(r))) {
                    resumesForUpdate.add(getForUpdate(r, mapDb.get(getAnchor(r))));
                } else {
                    if (!isMatch(resumesForCreate.stream().map(ResumeUtil::getAnchor).collect(Collectors.toList()), getAnchor(r))) {
                        resumesForCreate.add(r);
                    }
                }
            });
            executeRefreshDb(resumesDb, resumesForCreate, resumesForUpdate);
            long timeElapsed = Duration.between(start, Instant.now()).toMillis();
            log.info(finish_message, timeElapsed, resumesForCreate.size(), resumesForUpdate.size(), freshen);
            if (isRateOld) {
                updateRateDB();
            }
        }
    }

    @Transactional
    protected void executeRefreshDb(List<Resume> resumesDb, List<Resume> resumesForCreate, List<Resume> resumesForUpdate) {
        resumeService.deleteExceedLimitDb(resumesDb.size() + resumesForCreate.size() - limitResumesKeeping);
        Set<Resume> resumes = new HashSet<>(resumesForUpdate);
        resumes.addAll(resumesForCreate);
        if (!resumes.isEmpty()) {
            resumeService.createUpdateList(new ArrayList<>(resumes));
        }
    }

    @Transactional
    public void deleteOutDated() {
        resumeService.deleteOutDated();
        freshenService.deleteOutDated();
        voteService.deleteOutDated();
    }

    public void updateRateDB() {
        rateService.updateRateDB();
    }

    public static void main(String[] args) throws IOException {
        setTestAuthorizedUser(asAdmin());

/*
        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("all", "all", "all", UPGRADE));
        AtomicInteger i = new AtomicInteger(1);
        resumeTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info(common_number, resumeTos.size());
*/

    }
}


