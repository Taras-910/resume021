package ua.training.top;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.top.util.ValidationUtil.getRootCause;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app-test.xml",
        "classpath:spring/spring-db.xml"
})
@ExtendWith(TimingExtension.class)
@Sql(scripts = "classpath:db/hsqldb_populate.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class AbstractServiceTest {

    protected <T extends Throwable> void validateRootCause(Class<T> rootExceptionClass, Runnable runnable) {
        assertThrows(rootExceptionClass, () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }

}
