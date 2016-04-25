package com.tshirtapp.repository;


import com.tshirtapp.domain.Product;
import com.tshirtapp.domain.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
public interface ProductRepository extends MongoRepository<Product, String> {

    Product findOneByRef(String ref);

    List<Product> findAllByStatus(String status);

    List<Product> findAllByProductTypeAndStatusAndQuantityGreaterThanOrderBySize(ProductType productType, String status, int quantity);

    List<Product> findAllByProductTypeAndStatusAndQuantityGreaterThanAndSize(ProductType productType, String status, int quantity, String size);

    List<Product> findAllByProductType(ProductType productType);

}
