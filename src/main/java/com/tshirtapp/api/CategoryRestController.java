package com.tshirtapp.api;

import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.api.util.PaginationUtil;
import com.tshirtapp.domain.Category;
import com.tshirtapp.repository.CategoryRepository;
import com.tshirtapp.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by aminedev on 1/23/16.
 */

@RestController
@RequestMapping("/api")
public class CategoryRestController {
    @Autowired
    CategoryRepository categoryRepository;

    @RequestMapping(value = "/category",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> create(@Validated @RequestBody Category category) throws URISyntaxException {
        if (category.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new category cannot  have an ID")
                    .body(null);
        }
        if (categoryRepository.findByName(category.getName()) != null)
            return ResponseEntity.badRequest().header("Failure", "The name alredy taken")
                    .body(null);
        Category result = categoryRepository.save(category);
        return ResponseEntity.created(new URI("/api/names/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("user", result.getId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/category",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> updateCategory(@Validated @RequestBody Category category) throws URISyntaxException {
        if (category.getId() == null) {
            return create(category);
        }
        Category result = categoryRepository.save(category);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("user", category.getId().toString()))
                .body(result);

    }


    /**
     * GET  /users -> get all categorys.
     */
    @RequestMapping(value = "/category",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> getAllCategorys(Pageable pageable)
            throws URISyntaxException {
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(
                        new Sort.Order(Sort.Direction.ASC, "name")
                ));
        Page<Category> page = categoryRepository.findAll(pageRequest);
        List<Category> logsDTOs = page.getContent().stream()
                .map(user -> user)
                .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/category");
        return new ResponseEntity<>(logsDTOs, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/category/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) throws URISyntaxException, IOException {
        Category category = categoryRepository.findOne(id);
        if (category == null)
            return ResponseEntity.badRequest().build();
        categoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("name", id.toString())).build();

    }

}
