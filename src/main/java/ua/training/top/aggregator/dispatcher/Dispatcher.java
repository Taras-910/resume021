package ua.training.top.aggregator.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.training.top.aggregator.Provider;
import ua.training.top.model.Freshen;
import ua.training.top.repository.AggregatorInterface;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.ResumeCheckUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ua.training.top.aggregator.Installation.reasonDateLoading;
import static ua.training.top.util.AggregatorUtil.createTo;
import static ua.training.top.util.parser.data.CommonUtil.isMatch;
import static ua.training.top.util.parser.data.ConstantsUtil.citiesRU;
import static ua.training.top.util.parser.data.DataUtil.common_number;
import static ua.training.top.util.parser.data.DataUtil.error_select;

@Repository
public class Dispatcher implements AggregatorInterface {
    private final Logger log = LoggerFactory.getLogger(Dispatcher.class);

    private final Provider[] providers;
    public static ArrayDeque<Provider> allProviders;

    public Dispatcher(Provider... providers) throws IllegalArgumentException {
        this.providers = providers;
    }

    @Override
    public List<ResumeTo> selectBy(Freshen freshen) {
        if(isMatch(citiesRU, freshen.getWorkplace())){
            return new ArrayList<>();
        }
        allProviders = new ArrayDeque<>(Arrays.asList(providers));
        Set<ResumeTo> set = new HashSet<>();
        while (allProviders.peek() != null) {
            try {
                set.addAll(allProviders.pollFirst().getResumes(freshen));
            } catch (IOException e) {
                log.error(error_select, e.getMessage());
            }
        }

        List<ResumeTo> resumeTos = set.parallelStream()
                .filter(rTo -> reasonDateLoading.isBefore(rTo.getReleaseDate()))
                .filter(ResumeCheckUtil::checkNullDataResumeTo)
                .map(rTo -> createTo(rTo, freshen)).distinct()
                .collect(Collectors.toList());
        log.info(common_number, resumeTos.size());
        return resumeTos;
    }
}
