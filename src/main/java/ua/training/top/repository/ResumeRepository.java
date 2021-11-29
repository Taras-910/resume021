package ua.training.top.repository;

import ua.training.top.model.Resume;

import java.time.LocalDate;
import java.util.List;

public interface ResumeRepository {

    Resume save(Resume resume);

    List<Resume> saveAll(List<Resume> resumes);

    Resume get(int id);

    List<Resume> getAll();

    Resume getByParams(String title, String name, String workBefore);

    List<Resume> getByUserId(int userId);

    List<Resume> getList(int limit);

    boolean delete(int id);

    void deleteList(List<Resume> listToDelete);

    List<Resume> deleteOutDated(LocalDate reasonPeriodToKeep);
}

