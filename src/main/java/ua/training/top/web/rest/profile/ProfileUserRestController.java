package ua.training.top.web.rest.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.training.top.model.User;
import ua.training.top.service.UserService;

import java.net.URI;

import static ua.training.top.SecurityUtil.authUserId;

@RestController
@RequestMapping(ProfileUserRestController.REST_URL)
public class ProfileUserRestController {
    static final String REST_URL = "/rest/profile/users";

    @Autowired
    UserService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get() {
        User user = service.get(authUserId());
        return user;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        service.delete(authUserId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@RequestBody User user) {
        User created = service.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        service.update(user, authUserId());
    }
}
