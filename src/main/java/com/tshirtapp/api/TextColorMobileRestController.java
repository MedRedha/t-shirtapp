package com.tshirtapp.api;

import com.tshirtapp.api.util.HeaderUtil;
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
@RequestMapping("/api/mobile")
public class TextColorMobileRestController {

    @Autowired
    TextColorRepository textColorRepository;


    @RequestMapping(value = "/textColor",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TextColor> getAllColors() {
        return textColorRepository.findAll();
    }

}
