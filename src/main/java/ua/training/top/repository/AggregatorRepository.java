package ua.training.top.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.training.top.aggregator.Provider;
import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.ResumeCheckUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ua.training.top.aggregator.installation.InstallationUtil.reasonDateToLoad;
import static ua.training.top.util.AggregatorUtil.getFilled;

@Repository
public class AggregatorRepository implements AggregatorInterface{
    private final Logger log = LoggerFactory.getLogger(AggregatorRepository.class);

    private final Provider[] providers;
    public static ArrayDeque<Provider> allProviders;

    public AggregatorRepository(Provider... providers) throws IllegalArgumentException {
        this.providers = providers;
    }

    @Override
    public List<ResumeTo> selectBy(Freshen freshen){
        allProviders = new ArrayDeque<>(Arrays.asList(providers));
        Set<ResumeTo> set = new HashSet<>();
        while(allProviders.peek()!=null){
            try {
                set.addAll(allProviders.pollFirst().getResumes(freshen));
            } catch (IOException e) {
                log.error("selectBy e {}", e.getMessage());
            }
        }
        List<ResumeTo> resumeTos= set.parallelStream()
                .filter(ResumeCheckUtil::checkNullDataResumeTo)
                .filter(rTo -> reasonDateToLoad.isBefore(rTo.getReleaseDate()))
//                .filter(vTo -> checkNullDataResumesTo(vTo) && getMatchesLanguage(freshen, vTo.getTitle(), vTo.getSkills()))
                .map(rTo -> getFilled(rTo, freshen)).distinct()
                .collect(Collectors.toList());
        log.info("Common number resumes = {}", resumeTos.size());
        return resumeTos;
    }
}
