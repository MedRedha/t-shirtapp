package com.tshirtapp.repository;


import com.tshirtapp.domain.Authority;
import com.tshirtapp.domain.Order;
import com.tshirtapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface OrderRepository extends MongoRepository<Order, String> {

    Order findOneByUuid(String uuid);

    Long countByStatus(String status);

    Long countByValidatedByAndStatus(User user, String status);

    Long countByCreatedFrom(String status);

    Page<Order> findAllByStatusIn(Pageable pageable, List<String> status);

    List<Order> findAllByFbIDOrderByCreationDateDesc(String fbID);

}
