package ua.training.top.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.ActiveDbProfileResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@Transactional
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class AbstractServiceTest {
    private final static Logger log = LoggerFactory.getLogger("");
    public static final List<String> listResult = new ArrayList<>();

    @Rule
    public Stopwatch stopwatch = new Stopwatch(){
        @Override
        protected void finished(long nanos, Description description) {
            String message = String.format("%s%s%d millis", description.getMethodName(), " finished: spent = ", TimeUnit.NANOSECONDS.toMillis(nanos));
            listResult.add(message);
            log.info(message);
        }
    };

    @AfterClass
    public static void after() {
        log.info(listResult.toString());
    }
}
