package ua.training.top.repository;

import ua.training.top.model.Vote;

import java.util.List;

public interface VoteRepository {

    Vote get(int id, int userId);

    List<Vote> getAll();

    List<Vote> getAllForAuth(int userId);

    Vote save(Vote vote, int userId);

    boolean delete(int id, int userId);

    boolean deleteByResumeId(int resumeId, int authUserId);

    boolean deleteListByResumeId(int resumeId);

    void deleteList(List<Vote> listToDelete);
}
