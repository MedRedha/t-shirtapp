package com.tshirtapp.api;

import com.mongodb.BasicDBObject;
import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.api.util.PaginationUtil;
import com.tshirtapp.domain.Product;
import com.tshirtapp.repository.ProductRepository;
import com.tshirtapp.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Amine on 12/22/15.
 */

@RestController
@RequestMapping("/api/mobile")
public class ProductMobileRestController {


    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductTypeRepository productTypeRepository;
    @Autowired
    MongoTemplate mongoTemplate;


    @RequestMapping(value = "/product",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(@Validated @RequestBody Product product) throws URISyntaxException {
        if (product.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new product cannot already have an ID").body(null);
        if (productRepository.findOneByRef(product.getRef()) == null) {
            Product result = productRepository.save(product);
            return ResponseEntity.created(new URI("/api/product/" + product.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("product", product.getId().toString()))
                    .body(result);
        } else
            return
                    ResponseEntity.badRequest().header("Failure", "product already exist").body(null);

    }

    @RequestMapping(value = "/product",
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

    @RequestMapping(value = "/product/{id}",
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
    @RequestMapping(value = "/product{type}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(value = "type", required = true) String type, @RequestParam(value = "size", required = false) String size)
            throws URISyntaxException {
        List<Product> products;
        if (size == null) {
            products = productRepository.findAllByProductTypeAndStatusAndQuantityGreaterThanOrderBySize(productTypeRepository.findOne(type), "ENABLED", 0);
            HashMap<String, Product> stringProductHashMap = new HashMap<>();
            for (Product p : products) {
                if (stringProductHashMap.get(p.getSize()) == null)
                    stringProductHashMap.put(p.getSize(), p);

            }
            products.clear();
            stringProductHashMap.forEach((s, product) -> products.add(product));
        } else {
            products = productRepository.findAllByProductTypeAndStatusAndQuantityGreaterThanAndSize(productTypeRepository.findOne(type), "ENABLED", 0, size);

        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
