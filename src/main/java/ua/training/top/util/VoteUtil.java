package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Vote;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.training.top.aggregator.installation.InstallationUtil.limitResumesToKeep;
import static ua.training.top.util.MessagesUtil.not_found;

public class VoteUtil {
    public static Logger log = LoggerFactory.getLogger(VoteUtil.class);

    public static void checkNotFoundData(boolean found, Object id) {
        if (!found) {
            log.error(not_found, id);
        }
    }

    public static List<Vote> getVotesOutLimitHeroku(List<Vote> votesDb) {
        return Optional.of(votesDb.parallelStream()
                .sorted((v1, v2) -> v1.getLocalDate().isAfter(v2.getLocalDate()) ? 1 : 0)
                .sequential()
                .skip(limitResumesToKeep / 5)
                .collect(Collectors.toList())).orElse(new ArrayList<>());
    }
}
