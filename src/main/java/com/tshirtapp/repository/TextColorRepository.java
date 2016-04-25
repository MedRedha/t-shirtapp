package com.tshirtapp.repository;


import com.tshirtapp.domain.Category;
import com.tshirtapp.domain.Logo;
import com.tshirtapp.domain.TextColor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
public interface TextColorRepository extends MongoRepository<TextColor, String> {

    Optional<TextColor> findByName(String name);

    Optional<TextColor> findByValue(String value);

}
