package com.tshirtapp.repository;


import com.tshirtapp.domain.Authority;
import com.tshirtapp.domain.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
public interface ProductTypeRepository extends MongoRepository<ProductType, String> {

    ProductType findOneByName(String name);

    List<ProductType> findAllByStatus(String status);

}
