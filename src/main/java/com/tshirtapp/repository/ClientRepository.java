package com.tshirtapp.repository;

import com.tshirtapp.domain.Authority;
import com.tshirtapp.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by Amine on 12/22/15.
 */
public interface ClientRepository extends MongoRepository<User, String> {


    User findOneByFbID(String fbID);

    List<User> findAllByAuthoritiesName(String authority);

    List<User> findAllByAuthoritiesNameOrderByFirstName(String authority);

    Long countAllByAuthoritiesName(String authority);

    Long countByAuthoritiesName(String authority);

}
