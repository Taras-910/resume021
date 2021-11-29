package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.dispatcher.Dispatcher;
import ua.training.top.aggregator.strategy.*;

import static ua.training.top.aggregator.installation.Installation.autoRefreshProviders;
import static ua.training.top.aggregator.installation.Installation.testProvider;
import static ua.training.top.util.AutoRefreshUtil.getKey;
import static ua.training.top.util.AutoRefreshUtil.mapStrategies;

public class ProviderUtil {
    public static final Logger log = LoggerFactory.getLogger(ProviderUtil.class);

    public static Dispatcher getAllProviders() {
        if (testProvider) {
            return new Dispatcher(new Provider(new TestStrategy()));
        } else if (autoRefreshProviders) {
            log.info("autoRefreshProviders");
            return new Dispatcher(
                    mapStrategies.get(getKey(2)),
                    mapStrategies.get(getKey(2) + 2),
                    mapStrategies.get(4));
        } else {
            log.info("allProviders");
            return new Dispatcher(
                    new Provider(new DjinniStrategy()),      /*ua, foreign, remote, all  50pages */
                    new Provider(new GrcStrategy()),         /*меняет salary, date */
                    new Provider(new HabrStrategy()),        /*нет за_рубежем*/
                    new Provider(new RabotaStrategy()),      /*оч мало - до 10*/
                    new Provider(new WorkStrategy())         /*нет за_рубежем*/
            );
        }
    }
}

//https://career.softserveinc.com/en-us/vacancies
//https://jobs.ua/vacancy/kiev/rabota-java-developer
//https://kiev.careerist.ru/jobs-java-developer/
//https://kiev.jobcareer.ru/jobs/java/?feed=
//https://app.headz.io/candidates/new
//https://distillery.com/careers/senior-backend-developer-java-tg/
//https://edc.sale
//https://www.olx.ua
//https://www.ria.com
//https://trud.ua
//http://trudbox.com.ua/kiev/jobs-programmist
