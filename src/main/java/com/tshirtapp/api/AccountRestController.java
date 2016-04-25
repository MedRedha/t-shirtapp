package com.tshirtapp.api;

import com.tshirtapp.api.dto.UserDTO;
import com.tshirtapp.config.security.SecurityUtils;
import com.tshirtapp.domain.User;
import com.tshirtapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by aminedev on 1/2/16.
 */
@RestController
@RequestMapping("/api")
public class AccountRestController {


    @Autowired
    UserRepository userRepository;

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getAccount() {
        User result = userRepository.findOneByUsername(SecurityUtils.getCurrentUser().getUsername()).get();
        result.getAuthorities().size();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
