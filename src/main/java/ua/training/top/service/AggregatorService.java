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
import static ua.training.top.util.AggregatorUtil.getAnchor;
import static ua.training.top.util.AggregatorUtil.getForUpdate;
import static ua.training.top.util.ResumeUtil.fromTos;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.parser.data.CommonDataUtil.finish;
import static ua.training.top.util.parser.data.CommonDataUtil.grc;
import static ua.training.top.util.parser.data.LocalDateUtil.getToLocalDate;

@Service
public class AggregatorService {
    private final static Logger log = LoggerFactory.getLogger(AggregatorService.class);
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private FreshenService freshenService;

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB by freshen {}", freshen);
        List<Resume> resumesStrategy = fromTos(getAllProviders().selectBy(freshen));
        if (!resumesStrategy.isEmpty()) {
            Freshen newFreshen = freshenService.create(freshen);
            List<Resume> resumesDb = resumeService.deleteOutDatedAndGetAll();
            List<Resume> resumesForCreate = new ArrayList<>();
            List<Resume> resumesForUpdate = new ArrayList<>();
            Map<String, Resume> mapDb = resumesDb.stream()
                    .collect(Collectors.toMap(AggregatorUtil::getAnchor, r -> r));
            resumesStrategy.forEach(r -> {
                r.setFreshen(newFreshen);
                if (mapDb.containsKey(getAnchor(r))) {
                    resumesForUpdate.add(getForUpdate(r, mapDb.get(getAnchor(r))));
                } else {
                    resumesForCreate.add(r);
                }
            });
            executeRefreshDb(resumesDb, resumesForCreate, resumesForUpdate);
            log.info(finish, resumesForCreate.size(), resumesForUpdate.size(), freshen);
        }
    }

    @Transactional
    protected void executeRefreshDb(List<Resume> resumesDb, List<Resume> resumesForCreate, List<Resume> resumesForUpdate) {
        resumeService.deleteExceedLimitHeroku(resumesDb.size() + resumesForCreate.size());
        Set<Resume> resumes = new HashSet<>(resumesForUpdate);
        resumes.addAll(resumesForCreate);
        if (!resumes.isEmpty()) {
            resumeService.createUpdateList(new ArrayList<>(resumes));
        }
    }

    public static void main(String[] args) throws IOException {
        setTestAuthorizedUser(asAdmin());

/*
        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "all", "україна", UPGRADE));
        AtomicInteger i = new AtomicInteger(1);
        resumeTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", resumeTos.size());
*/


//        String text = "3 часа назад", siteName = work;
//        String text = "вчера", siteName = work;
//        String text = "2 дня назад", siteName = work;
//        String text = "6 дней назад", siteName = work;
//        String text = "1 неделя назад", siteName = work;
//        String text = "4 недели назад", siteName = work;
//        String text = "1 месяц назад", siteName = work;
//        String text = "Обновлено 22 минуты назад", siteName = work;
//        String text = "Обновлено 2 часа назад", siteName = work;
//        String text = "Обновлено 23 часа назад", siteName = work;
//        String text = "Обновлено 1 день назад", siteName = work;
//        String text = "Обновлено 2 дня назад", siteName = work;
//        String text = "Обновлено 1 неделю назад", siteName = work;
//        String text = "Обновлено 4 недели назад", siteName = work;
//        String text = "Обновлено 1 месяц назад", siteName = work;

//        String text = "сьогодні", siteName = grc;
//        String text = "вчора", siteName = grc;
//        String text = "6 листопада", siteName = grc;
//        String text = "31 жовтня", siteName = grc;
//        String text = "30 вересня", siteName = grc;
//        String text = "Обновлено 8 ноября 06:22", siteName = grc;
//        String text = "Обновлено 5 ноября 12:29", siteName = grc;
//        String text = "Обновлено 28 октября 10:47", siteName = grc;
//        String text = "Обновлено 18 октября 11:29", siteName = grc;
//        String text = "Обновлено 21 сентября 19:27", siteName = grc;
//        String text = "Обновлено 4 ноября 21:03", siteName = grc;
        String text = "Обновлено 21 сентября 19:27", siteName = grc;

//        String text = "2021-11-05T18:33:02+03:00", siteName = habr;

        System.out.println(getToLocalDate(siteName, text));

    }
}

// getBruteCleaning -> number? string

// djinni                сьогодні
// djinni                вчора
// djinni              6 листопада
// djinni             31 жовтня
// djinni             30 вересня

// grc      Обновлено 8 ноября 06:22
// grc      Обновлено 5 ноября 12:29
// grc      Обновлено 28 октября 10:47
// grc      Обновлено 18 октября 11:29
// grc      Обновлено 21 сентября 19:27
// grc      Обновлено 4 ноября 21:03
// grc      Обновлено 21 сентября 19:27

// work               3 часа назад
//                    вчера
//                    2 дня назад
//                    6 дней назад
//                    1 неделя назад
//                    4 недели назад
//                    1 месяц назад

// rabota   Обновлено 22 минуты назад
//          Обновлено 2 часа назад
//          Обновлено 23 часа назад
//          Обновлено 1 день назад
//          Обновлено 2 дня назад
//          Обновлено 1 неделю назад
//          Обновлено 4 недели назад
//          Обновлено 1 месяц назад
//          Обновлено 2 месяца назад

// habr     2021-11-08T18:33:02+03:00

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




