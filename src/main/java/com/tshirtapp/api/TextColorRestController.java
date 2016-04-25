package com.tshirtapp.api;

import com.tshirtapp.api.util.HeaderUtil;

import com.tshirtapp.config.security.AuthoritiesConstants;
import com.tshirtapp.domain.TextColor;


import com.tshirtapp.repository.TextColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


/**
 * Created by aminedev on 3/4/16.
 */

@RestController
@RequestMapping("/api")
public class TextColorRestController {

    @Autowired
    TextColorRepository textColorRepository;


    @RequestMapping(value = "/textColor",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextColor> createTextColor(@RequestBody TextColor textColor) throws URISyntaxException {
        if (textColor.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new text color cannot already have an ID").body(null);
        if (textColorRepository.findByName(textColor.getName()).isPresent())
            return ResponseEntity.badRequest().header("Failure", "A name  already taken").body(null);
        TextColor result = textColorRepository.save(textColor);
        return ResponseEntity.created(new URI("/api/texColors/" + textColor.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("textColor", textColor.getId().toString()))
                .body(result);

    }


    @RequestMapping(value = "/textColor",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextColor> updateUser(@Validated @RequestBody TextColor textColor) throws URISyntaxException {
        if (textColor.getId() == null) {
            return createTextColor(textColor);
        }
        TextColor result = textColorRepository.save(textColor);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("textColor", textColor.getId().toString()))
                .body(result);

    }


    @RequestMapping(value = "/textColor/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextColor> getUser(@PathVariable String id) throws URISyntaxException {
        return Optional.ofNullable(textColorRepository.findOne(id))
                .map(user -> new ResponseEntity<>(
                        user,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


    @RequestMapping(value = "/textColor",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TextColor> getAllUsers() {
        return textColorRepository.findAll();
    }

    @RequestMapping(value = "/textColor/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) throws URISyntaxException {
        textColorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("textColor", id.toString())).build();

    }

}
