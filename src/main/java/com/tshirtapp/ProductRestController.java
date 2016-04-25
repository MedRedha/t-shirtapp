package com.tshirtapp;

import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.api.util.PaginationUtil;
import com.tshirtapp.domain.Product;
import com.tshirtapp.domain.ProductType;
import com.tshirtapp.repository.ProductRepository;
import com.tshirtapp.repository.ProductTypeRepository;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Amine on 12/22/15.
 */

@RestController
@RequestMapping("/api")
public class ProductRestController {


    @Autowired
    ProductRepository productRepository;



    @RequestMapping(value = "/product",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(@Validated @RequestBody Product product) throws URISyntaxException {

        if (product.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new product cannot already have an ID").body(null);

        if (productRepository.findOneByRef(product.getRef()) == null) {
            Product result = productRepository.save(product);
            return ResponseEntity.created(new URI("/api/product/" +product.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("product", product.getId().toString()))
                    .body(result);
        } else
            return
                    ResponseEntity.badRequest().header("Failure", "product already exist").body(null);

    }

    @RequestMapping(value ="/product",
    method = RequestMethod.PUT,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProduct(@Validated @RequestBody Product product) throws URISyntaxException {
        if (product.getId() == null) {
            return createProduct(product);
        }
        Product result = productRepository.save(product);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("user", product.getId().toString()))
                .body(result);

    }

    @RequestMapping(value ="/product/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProduct(@PathVariable String id) throws URISyntaxException {


        return Optional.ofNullable(productRepository.findOne(id))
                .map(product -> new ResponseEntity<>(
                        product,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }








    /**
     * GET  /users -> get all products.
     */
    @RequestMapping(value = "/product",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<List<Product>> getAllProducts(Pageable pageable)
            throws URISyntaxException {
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),
                new Sort(
                        new Sort.Order(Sort.Direction.ASC, "size"),
                        new Sort.Order(Sort.Direction.ASC, "productType")
                ));
        Page<Product> page = productRepository.findAll(pageRequest);
        List<Product> managedUserDTOs = page.getContent().stream()
                .map(user -> user)
                .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product");
        return new ResponseEntity<>(managedUserDTOs, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/product{status}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public List<Product> getAllProductsEnabled(@RequestParam String status) {

        return productRepository.findAllByStatus(status);
    }







}
