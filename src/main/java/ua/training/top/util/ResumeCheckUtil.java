package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.to.ResumeTo;

import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.MessagesUtil.*;
import static ua.training.top.util.UserUtil.asAdmin;

public class ResumeCheckUtil {
    public static Logger log = LoggerFactory.getLogger(ResumeCheckUtil.class) ;
    public static boolean isNotSimilar(Resume r, ResumeTo rTo) {
        return !r.getTitle().equals(rTo.getTitle()) ||
                !r.getName().equals(rTo.getName()) ||
                !r.getSkills().equals(rTo.getSkills());
    }

    public static void isNullPointerException(ResumeTo resumeTo) {
        if(!checkNullDataResumeTo(resumeTo)) {
            throw new NullPointerException(must_not_null + resumeTo);
        }
    }

    public static boolean checkNullDataResumeTo(ResumeTo v) {
        String[] line = {v.getTitle(), v.getName(), v.getAddress(), v.getSkills(), v.getUrl()};
        for(String text : line) {
            if (text == null || text.equals("")) {
                log.error(check_error_data, v);
                return false;
            }
        }
        return true;
    }

    public static void checkExistResumeForUser(List<Resume> resumes) {
        if(resumes.size()  != 0 && authUserId() != asAdmin().getId()) {
            throw new IllegalArgumentException(resume_exist_already);
        }
    }

    public static void checkNotOwnUpdate(int userId) {
        if(userId != authUserId()) {
            throw new IllegalArgumentException(not_own_data);
        }
    }

    public static void checkNotOwnDelete(int userId) {
        if(userId != authUserId()|| userId != asAdmin().getId()) {
            throw new IllegalArgumentException(not_own_data);
        }
    }

    public static boolean getMatchesLanguage(Freshen f, String title, String workBefore){

        return  title != null && workBefore != null && (f.getLanguage().equals("all")
                || title.toLowerCase().contains("рекрутер")|| title.toLowerCase().contains("recruiter")
                || title.toLowerCase().contains("developer")|| title.toLowerCase().contains("engineer")
                || title.toLowerCase().matches(".*\\b"+f.getLanguage()+"\\b.*")
//                || title.toLowerCase().matches(".*\\b"+f.getLevel()+"\\b.*")
                || workBefore.toLowerCase().matches(".*\\b"+f.getLanguage()+"\\b.*")
//                || skills.toLowerCase().matches(".*\\b"+f.getLevel()+"\\b.*")
        );
    }

    public static boolean getMatchesFreshen(Freshen f, String title, String skills){
        return f.getLanguage().equals("all")
                || title.toLowerCase().matches(".*\\b"+f.getLanguage()+"\\b.*")
                || skills.toLowerCase().matches(".*\\b"+f.getLanguage()+"\\b.*");
    }

    public static List<Resume> getMatchesByFreshen(List<Resume> resumes, Freshen f) {
        log.info("resumes={}", resumes.size());
        log.info("--------------------------------------------------------------------------------");
        return f.getLanguage().equals("all") ? resumes : resumes.stream()
                .filter(r -> r.getTitle().toLowerCase().matches(".*\\b" + f.getLanguage() + "\\b.*")
                        || r.getSkills().toLowerCase().matches(".*\\b" + f.getLanguage() + "\\b.*")
                        || r.getWorkBefore().toLowerCase().matches(".*\\b" + f.getLanguage() + "\\b.*")
                ).collect(Collectors.toList());
    }

}
