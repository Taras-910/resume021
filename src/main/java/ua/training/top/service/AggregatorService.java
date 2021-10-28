package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.model.Vote;
import ua.training.top.to.ResumeTo;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.installation.InstallationUtil.limitResumesToKeep;
import static ua.training.top.aggregator.strategy.provider.ProviderUtil.getAllProviders;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.AggregatorUtil.*;
import static ua.training.top.util.AutoRefreshUtil.getKey;
import static ua.training.top.util.AutoRefreshUtil.mapStrategies;
import static ua.training.top.util.FreshenUtil.*;
import static ua.training.top.util.ResumeUtil.*;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.VoteUtil.getVotesOutLimitHeroku;
import static ua.training.top.util.parser.data.CorrectAge.getToAgeRabota;

@Service
public class AggregatorService {
    private final static Logger log = LoggerFactory.getLogger(AggregatorService.class);
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private FreshenService freshenService;

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB by freshen {}", freshen);
        List<ResumeTo> resumeTos = getAllProviders().selectBy(freshen);

        if (!resumeTos.isEmpty()) {
            List<ResumeTo> resumeTosForCreate = new ArrayList<>(resumeTos);
            List<ResumeTo> resumeTosForUpdate = new ArrayList<>(resumeTos);

            List<Resume> resumesDb = resumeService.getAll();
            List<Vote> votes = voteService.getAll();
            List<ResumeTo> resumeTosDb = getTos(resumesDb, votes);

            /*https://stackoverflow.com/questions/9933403/subtracting-one-arraylist-from-another-arraylist*/
            resumeTosDb.forEach(resumeTosForCreate::remove);
            resumeTosForCreate.forEach(resumeTosForUpdate::remove);
            List<Resume> resumesForUpdate = new ArrayList<>();
            if (!resumeTosForUpdate.isEmpty()) {
                List<ResumeTo> ListTosForUpdate = new ArrayList<>(resumeTosForUpdate);
            }
            refresh(resumeTosForCreate, resumesForUpdate, freshen, votes);
        }
    }

    @Transactional
    protected void refresh(List<ResumeTo> resumeTosForCreate, List<Resume> resumesForUpdate,
                           Freshen freshen, List<Vote> votes) {
        Freshen freshenCreated = freshenService.create(freshen);
        List<Resume> resumesForCreate = getForCreate(resumeTosForCreate, freshenCreated);
        Set<Resume> resumes = new HashSet<>(resumesForUpdate);
        resumes.addAll(resumesForCreate);
        if (!resumes.isEmpty()) {
            resumeService.createUpdateList(new ArrayList<>(resumes));
        }
        List<Resume> resumesDb = resumeService.getAll();
        List<Freshen> freshensDb = freshenService.getAll();
        deleteOutDate(resumesDb, freshensDb);
        deleteLimitHeroku(resumesDb, freshensDb, votes);

        log.info("upgrade ok for Freshen: {}\n ....................................................\n", freshenCreated);
    }

    public void deleteOutDate(List<Resume> resumesDb, List<Freshen> freshenDb) {
        resumeService.deleteList(getResumesOutPeriodToKeep(resumesDb));
        freshenService.deleteList(getFreshensOutPeriodToKeep(freshenDb));
    }

    public void deleteLimitHeroku(List<Resume> resumesDb, List<Freshen> freshenDb, List<Vote> votesDb) {
        resumeService.deleteList(getResumesOutLimitHeroku(resumesDb));
        if (freshenDb.size() > limitResumesToKeep / 2 + 1) {
            freshenService.deleteList(getFreshensOutLimitHeroku(freshenDb));
        }
        if (votesDb.size() > limitResumesToKeep / 5) {
            voteService.deleteList(getVotesOutLimitHeroku(votesDb));
        }
    }

    public static void main(String[] args) throws IOException {
        setTestAuthorizedUser(asAdmin());
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "минск", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "санкт-петербург", UPGRADE));

//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "intern", "foreign", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "junior", "foreign", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "foreign", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "senior", "foreign", UPGRADE));

//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "intern", "remote", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "junior", "remote", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "remote", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "senior", "remote", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "expert", "remote", UPGRADE));

//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "intern", "киев", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "junior", "киев", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "киев", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "украина", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "all", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "санкт-петербург", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "варшава", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "польша", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "all", UPGRADE));
        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "all", "all", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "foreign", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "senior", "киев", UPGRADE));
//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "expert", "киев", UPGRADE));

        AtomicInteger i = new AtomicInteger(1);
        resumeTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", resumeTos.size());

        System.out.println(mapStrategies.get(getKey(2)));
    }
}
