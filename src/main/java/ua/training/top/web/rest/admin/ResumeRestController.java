package ua.training.top.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.training.top.model.Freshen;
import ua.training.top.model.Resume;
import ua.training.top.service.ResumeService;
import ua.training.top.to.ResumeTo;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.FreshenUtil.getFreshenFromTo;

@RestController
@RequestMapping(value = ResumeRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ResumeRestController {
    static final String REST_URL = "/rest/admin/resumes";
    public static final Logger log = LoggerFactory.getLogger(ResumeRestController.class);
    @Autowired
    private ResumeService resumeService;

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
        resumeService.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid ResumeTo resumeTo) {
        resumeService.updateTo(resumeTo);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resume> createResume(@RequestBody @Valid ResumeTo resumeTo) {
        Resume created = resumeService.create(resumeTo);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping(value = "/filter")
    public List<ResumeTo> getByFilter(@Valid Freshen freshen) {
        log.info("getByFilter freshen={}", freshen);
        return resumeService.getTosByFilter(asNewFreshen(freshen));
    }
}
