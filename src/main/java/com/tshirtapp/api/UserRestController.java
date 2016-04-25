package com.tshirtapp.api;

import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.config.security.AuthoritiesConstants;
import com.tshirtapp.domain.Authority;
import com.tshirtapp.domain.User;
import com.tshirtapp.domain.util.Const;
import com.tshirtapp.repository.AuthorityRepository;
import com.tshirtapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by Amine on 12/22/15.
 */

@RestController
@RequestMapping("/api")
public class UserRestController {


    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/user",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User user) throws URISyntaxException {
        if (user.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new name cannot already have an ID").body(null);
        if (userRepository.findOneByUsername(user.getUsername()).isPresent())
            return ResponseEntity.badRequest().header("Failure", "A username  already taken").body(null);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findOne(AuthoritiesConstants.USER));
        user.setStatus(Const.USER_ACTIVATED);
        user.setAuthorities(authorities);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User result = userRepository.save(user);
        return ResponseEntity.created(new URI("/api/names/" + user.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("user", user.getId().toString()))
                .body(result);

    }

    @RequestMapping(value = "/user",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@Validated @RequestBody User user) throws URISyntaxException {
        if (user.getId() == null) {
            return createUser(user);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User result = userRepository.save(user);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("user", user.getId().toString()))
                .body(result);

    }

    @RequestMapping(value = "/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable String id) throws URISyntaxException {
        return Optional.ofNullable(userRepository.findOne(id))
                .map(user -> new ResponseEntity<>(
                        user,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @RequestMapping(value = "/userfb/{fbID}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByFbID(@PathVariable String fbID) throws URISyntaxException {
        return Optional.ofNullable(userRepository.findOneByFbID(fbID))
                .map(user -> new ResponseEntity<>(
                        user,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


    @RequestMapping(value = "/user",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userRepository.findAllByAuthoritiesName(AuthoritiesConstants.USER);
    }


    @RequestMapping(value = "/user/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) throws URISyntaxException {
        userRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("name", id.toString())).build();

    }

}
