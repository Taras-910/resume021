package ua.training.top.web.rest.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.training.top.model.Vote;
import ua.training.top.service.VoteService;

import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = ProfileVoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteRestController {
    static final String REST_URL = "/rest/profile/votes";
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final VoteService service;

    public ProfileVoteRestController(VoteService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        log.info("get by id {} for user {}", id, authUserId());
        return service.get(id);
    }

    @GetMapping
    public List<Vote> getAllForAuth() {
        log.info("get all for authUserId");
        return service.getAllForAuth();
    }

    @PostMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setVote(@PathVariable(name = "id") int resumeId, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", resumeId);
        service.setVote(resumeId, enabled);
    }
}
