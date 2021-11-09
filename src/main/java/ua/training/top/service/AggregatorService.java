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
import static ua.training.top.util.parser.data.CommonDataUtil.*;

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
            List<Resume>
                    resumesDb = resumeService.deleteOutDatedAndGetAll(),
                    resumesForCreate = new ArrayList<>(),
                    resumesForUpdate = new ArrayList<>();
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

//        List<ResumeTo> resumeTos = getAllProviders().selectBy(asNewFreshen("java", "all", "Киев", UPGRADE));
//        AtomicInteger i = new AtomicInteger(1);
//        resumeTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
//        log.info(common_number, resumeTos.size());

//        String text = "От 2 000 $ · Рассмотрю предложения реверс-инженер, Бэкенд разработчик, Разработчик мобильных приложений Владивосток · Готов к удалённой работе · Системное программирование · Обратная разработка · Delphi · Objective-С · C · ARM architecture · X86 asm · Win32 API · Lua · Java";
//        String text = "Рассмотрю предложения Java/Kotlin-разработчик, Бэкенд разработчик, Фулстек разработчик, Senior Москва · Готов к удалённой работе · Java · Kotlin · SQL · Java Spring Framework. · Git · TDD/BDD · NoSQL · RabbitMQ · MongoDB · Высоконагруженные системы Возраст и стаж 29 лет · 6 лет и 11 месяцев 29 лет · 6 лет и 11 месяцев Последнее место работы Сбер · Java/Kotlin-разработчик · 3 года и 5 месяцев Сбер · Java/Kotlin-разработчик · 3 года и 5 месяцев Высшее образование МГТУ им. Н.Э. Баумана · Информатики и систем управления; ИУ · 1 год и 10 месяцев МГТУ им. Н.Э. Баумана · Информатики и систем управления; ИУ · 1 год и 10 месяцев Дополнительное образование stringconcat.com · OTUS stringconcat.com · OTUS";
        String text = "От 2 000 $ ·  Ищу работу Backend, Бэкенд разработчик, Фулстек разработчик Ульяновск · Готов к удалён";
        System.out.println(getToWorkBefore(text));
    }
    public static String extract_work_before_habr = "(Рассмотрю).*|(Не ищу).*|(Ищу).*",
    extract_age = "(?:[1-7]\\d)\\s([годалетрківи])+",
    extract_address = "(?:[а-яА-ЯіїєA-Za-z,\\s·]+)\\b";

    public static String getToWorkBefore(String text) {
        if(isEmpty(text)) {
            return "see the card";
        }
        List<String> list = getMatcherGroups(text, extract_work_before_habr);
        return list.size() > 0 ? list.get(0).trim() : "see the card";
    }
}
//От 200 000 ₽ · Рассмотрю предложения Ведущий
//Не ищу работу Backend, Бэкенд разработчик, Фулстек разработчик Ульяновск · Готов к удалён



