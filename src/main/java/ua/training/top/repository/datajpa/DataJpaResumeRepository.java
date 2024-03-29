package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Resume;
import ua.training.top.repository.ResumeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ua.training.top.util.InformUtil.exist_end_replace;
import static ua.training.top.util.InformUtil.update_error_and_redirect;

@Transactional(readOnly = true)
@Repository
public class DataJpaResumeRepository implements ResumeRepository {
    private static final Sort SORT_DATE_NAME = Sort.by(Sort.Direction.DESC, "releaseDate", "id");
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CrudResumeRepository resumeRepository;

    public DataJpaResumeRepository(CrudResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    @Transactional
    @Override
    public Resume save(Resume resume) {
        Resume resumeDouble = getByParams(resume.getTitle(), resume.getName(), resume.getWorkBefore());
        if (resumeDouble != null && (resume.isNew() || resumeDouble.getId() != resume.getId())) {
            delete(resumeDouble.getId());
            log.error(exist_end_replace, resumeDouble, resume);
        }
        return resumeRepository.save(resume);
    }

    @Transactional
    @Override
    public List<Resume> saveAll(List<Resume> resumes) {
        List<Resume> resumesDb = new ArrayList<>();
        try {
            resumesDb = resumeRepository.saveAll(resumes);
        } catch (Exception e) {
            for (Resume r : resumes) {
                log.error(update_error_and_redirect, r);
                resumesDb.add(save(r));
            }
        }
        return resumesDb;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return Optional.of(resumeRepository.delete(id)).orElse(0) != 0;
    }

    @Transactional
    @Override
    public void deleteList(List<Resume> listToDelete) {
        resumeRepository.deleteAll(listToDelete);
    }

    @Transactional
    @Override
    public Resume getByParams(String title, String name, String workBefore) {
        try {
            return resumeRepository.getByParams(title, name, workBefore);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Resume get(int id) {
        return resumeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Resume> getAll() {
        return resumeRepository.findAll(SORT_DATE_NAME);
    }

    @Override
    public List<Resume> getByUserId(int userId) {
        return Optional.of(resumeRepository.getByUserId(userId)).orElse(new ArrayList<>());
    }

    @Transactional
    @Override
    public List<Resume> deleteOutDated(LocalDate reasonPeriodToKeep) {
        deleteList(resumeRepository.getOutDated(reasonPeriodToKeep));
        return getAll();
    }

    @Transactional
    @Override
    public List<Resume>  getList(int limit) { return resumeRepository.getList(limit); }
}

