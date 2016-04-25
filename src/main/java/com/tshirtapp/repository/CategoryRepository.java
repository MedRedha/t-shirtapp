package com.tshirtapp.repository;


import com.tshirtapp.domain.Category;
import com.tshirtapp.domain.Logo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Category.
 */
public interface CategoryRepository extends MongoRepository<Category, String> {

    Category findByName(String name);


}
