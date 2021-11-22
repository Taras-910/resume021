package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.util.AggregatorUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.ProviderUtil.getAllProviders;
import static ua.training.top.aggregator.installation.Installation.limitResumesKeeping;
import static ua.training.top.util.AggregatorUtil.getAnchor;
import static ua.training.top.util.AggregatorUtil.getForUpdate;
import static ua.training.top.util.ResumeUtil.fromTos;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.parser.data.DataUtil.finish;
import static ua.training.top.util.parser.data.DataUtil.getLimitation;

@Service
public class AggregatorService {
    private final static Logger log = LoggerFactory.getLogger(AggregatorService.class);
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private FreshenService freshenService;

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB by freshen {}", freshen);
        List<Resume> resumesNet = fromTos(getAllProviders().selectBy(freshen));
        if (!resumesNet.isEmpty()) {
            Freshen newFreshen = freshenService.create(freshen);
            List<Resume>
                    resumesDb = resumeService.deleteOutDatedAndGetAll(),
                    resumesForCreate = new ArrayList<>(),
                    resumesForUpdate = new ArrayList<>();
            Map<String, Resume> mapDb = resumesDb.stream()
                    .collect(Collectors.toMap(AggregatorUtil::getAnchor, r -> r));
            resumesNet.forEach(r -> {
                r.setFreshen(newFreshen);
                if (mapDb.containsKey(getAnchor(r))) {
                    resumesForUpdate.add(getForUpdate(r, mapDb.get(getAnchor(r))));
                } else if(!resumesForCreate.contains(r)) {
                        resumesForCreate.add(r);
                    }
            });
            executeRefreshDb(resumesDb, resumesForCreate, resumesForUpdate);
            log.info(finish, resumesForCreate.size(), resumesForUpdate.size(), freshen);
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

    public static void main(String[] args) throws IOException {
        setTestAuthorizedUser(asAdmin());

/*
        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "all", "all", UPGRADE));
        AtomicInteger i = new AtomicInteger(1);
        resumeTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info(common_number, resumeTos.size());
*/

        String salary = " ";
//        String salary = "2 050 EUR";
//        String salary = "800 бел. руб.";
//        String salary = "3 500 USD";
//        String salary = "100 000 руб.";
        System.out.println(getLimitation(salary));

    }
//                wasteSalary = of(" ", " ", "&nbsp;", "[.]{2,}", "(\\p{Sc}|ƒ)", "\\s+"),
}
//	      djinni   grc*10   habr  rabota   work  linkedin  total
//all	    49	  49(0)	     1	     6	    16	   (100)	121
//Украина	32	   4(0)	     -	     6	    30	     -	     72
//foreign	49	   2(0)	     1	     1	     -	     -	     53
//Киев	    15	   1(0)	     1	     3	    15	     -	     35
//remote 	 -	  17(0)	     1	     3	    12	    (5)	     33
//Минск	     1	  10(0)	     1	     6	     -	     -	     18
//Львов	     6	    -	     -	     8	     2	     -	     16
//Харьков	 5	    -	     -	     2	     5	     -	     12
//Одесса	 5	    -	     -	     2	     4	     -	     11
//Санкт-П	 5	   5(0)	     1	     -	     -	     -	     11
//Москва	 -	   8(0)	     1	     -	     -	     -	      9



