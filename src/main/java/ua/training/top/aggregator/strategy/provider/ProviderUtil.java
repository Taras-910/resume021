package ua.training.top.aggregator.strategy.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.*;
import ua.training.top.repository.AggregatorRepository;

import static ua.training.top.aggregator.installation.InstallationUtil.autoRefreshProviders;
import static ua.training.top.aggregator.installation.InstallationUtil.testProvider;
import static ua.training.top.util.AutoRefreshUtil.getKey;
import static ua.training.top.util.AutoRefreshUtil.mapStrategies;

public class ProviderUtil {
    public static final Logger log = LoggerFactory.getLogger(ProviderUtil.class);

    public static AggregatorRepository getAllProviders(){
        if (testProvider) {
            return new AggregatorRepository(new Provider(new TestStrategy()));
        }
        else if (autoRefreshProviders) {
            log.info("autoRefreshProviders");
            return new AggregatorRepository(
                    mapStrategies.get(getKey(2)),
                    mapStrategies.get(getKey(2) + 2),
                    mapStrategies.get(4));
        }
        else {
            log.info("allProviders");
            return new AggregatorRepository(
                    new Provider(new DjinniStrategy()),      //ua, foreign, remote, all ? 50pages : 1/
                    new Provider(new GrcStrategy()),         /*нет за_рубежем, меняет salary, date */
                    new Provider(new HabrStrategy()),        /*нет за_рубежем*/
//                    new Provider(new LinkedinStrategy()),    /*нет удаленно*/
                    new Provider(new RabotaStrategy()),         /*оч мало - до 10*/
                    new Provider(new WorkStrategy())         /*нет за_рубежем*/
            );
        }
    }
}
//  	   djinni	grc	   work
//all	    33		 -       20		53
//Украина	23		 -       20		43
//foreign	33		 -	     -   	33
//Киев	    12		 -       10		22
//Одесса	2		 -       3		 5
//Харьков	4		 -       3		 7
//remote    -		17	     9		26
//Санкт-П	5	     5		 -	    10
//Минск	    3	     3		 -	     6
//Москва	-	     8		 -	     8


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
