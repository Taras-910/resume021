package ua.training.top.repository;

import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;

import java.util.List;

public interface ResumeRepository {

    Resume save(Resume resume);

    List<Resume> saveAll(List<Resume> resumes);

    Resume get(int id);

    List<Resume> getAll();

    Resume getByParams(String title, String name, String workBefore);

    boolean delete(int id);

    void deleteList(List<Resume> listToDelete);

    List<Resume> getByFilter(Freshen freshen);

    List<Resume> getByUserId(int userId);
}

