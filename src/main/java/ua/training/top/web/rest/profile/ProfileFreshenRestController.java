package ua.training.top.web.rest.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;
import ua.training.top.service.FreshenService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.UserUtil.asAdmin;

@RestController
@RequestMapping(value = ProfileFreshenRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileFreshenRestController {
    static final String REST_URL = "/rest/profile/freshen";
    public static final Logger log = LoggerFactory.getLogger(ProfileFreshenRestController.class);

    private final FreshenService service;

    public ProfileFreshenRestController(FreshenService service) {
        this.service = service;
    }

    @GetMapping
    public List<Freshen> getAllOwn() {
        return service.getAll().stream().filter(f -> authUserId() == f.getUserId()).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Freshen get(@PathVariable int id) {
        return getAllOwn().stream().filter(f -> f.getId() == id).findFirst().orElse(null);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refreshDB(@Valid @RequestBody Freshen freshen) {
        log.info("refreshDB freshen {}", freshen);
        freshen.setUserId(asAdmin().id());
        freshen.setGoals(Collections.singleton(Goal.UPGRADE));
        service.refreshDB(asNewFreshen(freshen));
    }

}
