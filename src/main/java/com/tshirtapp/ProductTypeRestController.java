package com.tshirtapp;

import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.config.security.AuthoritiesConstants;
import com.tshirtapp.domain.Authority;
import com.tshirtapp.domain.ProductType;
import com.tshirtapp.domain.User;
import com.tshirtapp.domain.util.Const;
import com.tshirtapp.repository.ProductTypeRepository;
import com.tshirtapp.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

/**
 * Created by Amine on 12/22/15.
 */

@RestController
@RequestMapping("/api")
public class ProductTypeRestController {


    @Autowired
    ProductTypeRepository productTypeRepository;


    @RequestMapping(value = "/productType",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductType> createProductType(@Validated @RequestBody ProductType productType) throws URISyntaxException {
        if (productType.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new productType cannot already have an ID").body(null);
        if (productTypeRepository.findOneByName(productType.getName()) == null) {
            ProductType result = productTypeRepository.save(productType);
            return ResponseEntity.created(new URI("/api/productType/" + productType.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("productType", productType.getId().toString()))
                    .body(result);
        } else
            return
                    ResponseEntity.badRequest().header("Failure", "productType already exist").body(null);

    }

    @RequestMapping(value = "/productType",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductType> updateProductType(@Validated @RequestBody ProductType productType) throws URISyntaxException {
        if (productType.getId() == null) {
            return createProductType(productType);
        }
        ProductType result = productTypeRepository.save(productType);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("user", productType.getId().toString()))
                .body(result);

    }

    @RequestMapping(value = "/productType/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductType> getClient(@PathVariable String id) throws URISyntaxException {
        return Optional.ofNullable(productTypeRepository.findOne(id))
                .map(productType -> new ResponseEntity<>(
                        productType,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


    @RequestMapping(value = "/productType",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductType> getAllClients() {
        return productTypeRepository.findAll();
    }


    @RequestMapping(value = "/productType/status{status}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductType> getAllProductsEnabled(@RequestParam(value = "status") String status) {
        return productTypeRepository.findAllByStatus(status);
    }

}
