package ua.training.top.web.rest.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.training.top.model.Freshen;
import ua.training.top.service.ResumeService;
import ua.training.top.to.ResumeTo;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.FreshenUtil.asNewFreshen;

@RestController
@RequestMapping(value = ProfileResumeRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileResumeRestController {
    static final String REST_URL = "/rest/profile/resumes";
    public static final Logger log = LoggerFactory.getLogger(ProfileResumeRestController.class);

    private final ResumeService resumeService;

    public ProfileResumeRestController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping("/{id}")
    public ResumeTo get(@PathVariable int id) {
        return resumeService.getTo(id);
    }

    @GetMapping
    public List<ResumeTo> getAll() {
        return resumeService.getAllTos();
    }

    @Transactional
    @GetMapping(value = "/filter")
    public List<ResumeTo> getByFilter(@Valid Freshen freshen) {
        log.info("getByFilter freshen={}", freshen);
        return resumeService.getTosByFilter(asNewFreshen(freshen));
    }
}
