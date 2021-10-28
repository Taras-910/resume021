package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.repository.ResumeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public class DataJpaResumeRepository implements ResumeRepository {
    private static final Sort SORT_DATE_NAME = Sort.by(Sort.Direction.DESC, "releaseDate", "title");
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CrudResumeRepository resumeRepository;

    public DataJpaResumeRepository(CrudResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    @Transactional
    @Override
    public Resume save(Resume resume) {
        Resume resumeDouble = getByParams(resume.getTitle(), resume.getSkills());
        if (resumeDouble != null && (resume.isNew() || resumeDouble.getId() != resume.getId())) {
            delete(resumeDouble.getId());
            log.error("same resume " + resumeDouble + " already existed in the database but was replaced by " + resume);
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
            for(Resume r : resumes) {
                log.error("error " + r + " redirect on method save");
                resumesDb.add(save(r));
            }
        }
        return resumesDb;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        log.info("delete id {}", id);
        boolean b = Optional.of(resumeRepository.delete(id)).orElse(0) != 0;
        return b;
    }

    @Transactional
    @Override
    public void deleteList(List<Resume> listToDelete) {
        resumeRepository.deleteAll(listToDelete);
    }

    public Resume getByParams(String title, String skills) {
        try {
            return resumeRepository.getByParams(title, skills);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Resume> getByFilter(Freshen freshen) {
        String language = freshen.getLanguage(), workplace = freshen.getWorkplace(), level = freshen.getLevel();
        return resumeRepository.getByFilter(language.equals("all") ? "" : language, level.equals("all") ? "" : level,
                workplace.equals("all") ? "" : workplace);
    }

    @Override
    public int getCountToday() {
        return resumeRepository.getCountToday(LocalDate.now()).size();
    }

    @Override
    public int getByFreshenId(Integer id) {
        return resumeRepository.getByFreshenId(id).size();
    }

    @Override
    public Resume get(int id) {
        return resumeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Resume> getAll() {
        return resumeRepository.getAll();
    }

    @Override
    public List<Resume> getByUserId(int userId) {
        return Optional.of(resumeRepository.getByUserId(userId)).orElse(new ArrayList<>());
    }

}

