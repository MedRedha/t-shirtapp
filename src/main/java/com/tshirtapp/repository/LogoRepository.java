package com.tshirtapp.repository;


import com.tshirtapp.domain.Category;
import com.tshirtapp.domain.Logo;
import com.tshirtapp.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface LogoRepository extends MongoRepository<Logo, String> {

    Logo findByTag(String tag);

    List<Logo> findByCategory(Category category);

}
