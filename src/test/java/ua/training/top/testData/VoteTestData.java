package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Vote;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.testData.ResumeTestData.resume1_id;
import static ua.training.top.testData.ResumeTestData.resume2_id;
import static ua.training.top.testData.UserTestData.admin_id;
import static ua.training.top.testData.UserTestData.user_id;
import static ua.training.top.util.DateTimeUtil.thisDay;

public class VoteTestData {
    public static TestMatcher<Vote> vote_matcher = TestMatcher.usingFieldsComparator(Vote.class,"localDate");
    public static final int vote1_id = START_SEQ + 6;
    public static final int vote2_id = vote1_id + 1;
    public static final Vote vote1 = new Vote(vote1_id,  LocalDate.of(2020, 10, 25), resume1_id, admin_id);
    public static final Vote vote2 = new Vote(vote2_id,  LocalDate.of(2020, 10, 25), resume2_id, user_id);

    public static Vote getNew() {
        return new Vote(null, thisDay, resume2_id, admin_id);
    }

    public static Vote getUpdated() {
        return new Vote(vote1_id, thisDay, resume2_id, admin_id);
    }

    public static List<Vote> allVotes(){
        return Arrays.asList(vote1);
    }
}
