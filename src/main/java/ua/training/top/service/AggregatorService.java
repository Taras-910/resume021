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
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.ProviderUtil.getAllProviders;
import static ua.training.top.aggregator.installation.InstallationUtil.limitResumesToKeep;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.AggregatorUtil.getForCreate;
import static ua.training.top.util.AggregatorUtil.getForUpdate;
import static ua.training.top.util.FreshenUtil.*;
import static ua.training.top.util.ResumeUtil.*;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.VoteUtil.getVotesOutLimitHeroku;
import static ua.training.top.util.parser.data.CommonDataUtil.finish;

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
            Map<String, Resume> mapDb = resumesDb.stream()
                        .collect(Collectors.toMap(r -> getPartWorkBefore(r.getWorkBefore()).toLowerCase(), r -> r));
            List<ResumeTo> temp = new ArrayList<>();
            resumeTosForCreate.forEach(rForCreate -> {
                if(mapDb.containsKey(getPartWorkBefore(rForCreate.getWorkBefore()).toLowerCase())) {
                    resumeTosForUpdate.add(rForCreate);
                    temp.add(rForCreate);
                }
            });
            temp.forEach(resumeTosForCreate::remove);
            refresh(mapDb, resumeTosForCreate, resumeTosForUpdate, freshen, votes);
        }
    }

    @Transactional
    protected void refresh(Map<String, Resume> mapDb, List<ResumeTo> resumeTosForCreate,
                           List<ResumeTo> resumeTosForUpdate, Freshen freshen, List<Vote> votes) {
        Freshen freshenCreated = freshenService.create(freshen);
        List<Resume> resumesForCreate = getForCreate(resumeTosForCreate, freshenCreated);
        List<Resume> resumesForUpdate = getForUpdate(resumeTosForUpdate, mapDb, freshenCreated);
        Set<Resume> resumes = new HashSet<>(resumesForUpdate);
        resumes.addAll(resumesForCreate);
        if (!resumes.isEmpty()) {
            resumeService.createUpdateList(new ArrayList<>(resumes));
        }
        List<Resume> resumesDb = resumeService.getAll();
        List<Freshen> freshensDb = freshenService.getAll();
        deleteDuplicates(resumesDb);
        deleteOutDate(resumesDb, freshensDb);
        deleteLimitHeroku(resumesDb, freshensDb, votes);
        log.info(finish, resumesForCreate.size(), resumesForUpdate.size(), freshenCreated);
    }

    public void deleteDuplicates(List<Resume> resumesDb) {
        List<Resume> duplicatesForDelete = new ArrayList<>();
        Map<String, List<Resume>> map = resumesDb.stream()
                .collect(Collectors.groupingBy(r -> getPartWorkBefore(r.getWorkBefore()).toLowerCase(), Collectors.toList()));
        map.values().forEach(list -> {
            if (list.size() > 1) {
                duplicatesForDelete.addAll(list.stream()
                        .sorted((r1, r2) -> -r1.getId().compareTo(r2.getId()))
                        .skip(1)
                        .collect(Collectors.toList()));
            }
        });
        duplicatesForDelete.forEach(r -> log.info("{}", r));
        resumeService.deleteList(duplicatesForDelete);
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

        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "all", "Львов", UPGRADE));
        AtomicInteger i = new AtomicInteger(1);
        resumeTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", resumeTos.size());

    }
}

//	      djinni   grc*10   habr  rabota   work  linkedin  total
//all	    49	  49(0)	     1	     6	    16	   (100)	121
//Украина	32	   4(0)	     -	     6	    30	     -	     72
//foreign	49	   2(0)	     1	     1	     -	     -	     53
//Киев	    15	   1(0)	     1	     3	    15	     -	     35
//remote 	 -	  17(0)	     1	     3	    12	    (5)	     33
//Минск	     1	  10(0)	     1	     6	     -	     -	     18
//Харьков	 5	    -	     -	     2	     5	     -	     12
//Одесса	 5	    -	     -	     2	     4	     -	     11
//Санкт-П	 5	   5(0)	     1	     -	     -	     -	     11
//Москва	 -	   8(0)	     1	     -	     -	     -	      9
//Львов	     6	    -	     -	     8	     2	     -	      -
