package com.tshirtapp.api;

import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.api.util.PaginationUtil;
import com.tshirtapp.domain.Category;
import com.tshirtapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by aminedev on 1/23/16.
 */

@RestController
@RequestMapping("/api/mobile")
public class CategoryMobileRestController {
    @Autowired
    CategoryRepository categoryRepository;


    /**
     * GET  /users -> get all categorys.
     */
    @RequestMapping(value = "/category",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> getAllCategorys()
            throws URISyntaxException {
        List<Category> list = categoryRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
