package com.tshirtapp.repository;

import com.tshirtapp.domain.Authority;
import com.tshirtapp.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Amine on 12/22/15.
 */
public interface UserRepository extends MongoRepository<User, String> {


    User findOneByFbID(String fbID);

    Optional<User> findOneByUsername(String id);

    List<User> findAllByAuthoritiesName(String authority);

}
