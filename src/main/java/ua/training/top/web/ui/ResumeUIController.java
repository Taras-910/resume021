package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.model.Freshen;
import ua.training.top.service.ResumeService;
import ua.training.top.service.VoteService;
import ua.training.top.to.ResumeTo;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.FreshenUtil.asNewFreshen;

@ApiIgnore
@RestController
@RequestMapping(value = "profile/resumes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResumeUIController {
    public static final Logger log = LoggerFactory.getLogger(ResumeUIController.class);
    private final ResumeService resumeService;
    private final VoteService voteService;

    public ResumeUIController(ResumeService resumeService, VoteService voteService) {
        this.resumeService = resumeService;
        this.voteService = voteService;
    }

    @GetMapping("/{id}")
    public ResumeTo get(@PathVariable int id) {
        return resumeService.getTo(id);
    }

    @GetMapping
    public List<ResumeTo> getAll() {
        return resumeService.getAllTos();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        resumeService.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createOrUpdate(@Valid ResumeTo resumeTo) {
        log.info("createOrUpdate resumeTo={}", resumeTo);
        if (resumeTo.isNew()) {
            resumeService.create(resumeTo);
        } else {
            resumeService.updateTo(resumeTo);
        }
    }

    @Transactional
    @GetMapping(value = "/filter")
    public List<ResumeTo> getByFilter(@Valid Freshen freshen) {
        log.info("getByFilter language={} level={} workplace={}", freshen.getLanguage(), freshen.getLevel(), freshen.getWorkplace());
        return resumeService.getTosByFilter(asNewFreshen(freshen));
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setVote(@PathVariable(name = "id") int vacancyId, @RequestParam boolean enabled) {
        voteService.setVote(vacancyId, enabled);
    }
}
