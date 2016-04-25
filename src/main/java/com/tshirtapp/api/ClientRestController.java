package com.tshirtapp.api;

import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.config.security.AuthoritiesConstants;
import com.tshirtapp.domain.Authority;
import com.tshirtapp.domain.User;
import com.tshirtapp.domain.util.Const;
import com.tshirtapp.repository.AuthorityRepository;
import com.tshirtapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by Amine on 12/22/15.
 */

@RestController
@RequestMapping("/api")
public class ClientRestController {


    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AuthorityRepository authorityRepository;

    @RequestMapping(value = "/client",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createClient(@RequestBody User user) throws URISyntaxException {

        if (user.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new client cannot already have an ID").body(null);


            Set<Authority> authorities = new HashSet<>();
            Authority temp =authorityRepository.findOne(AuthoritiesConstants.CLIENT);
            authorities.add(temp);
            user.setAuthorities(authorities);
            user.setStatus(Const.USER_ACTIVATED);
            user.setClientRef(clientRepository.countAllByAuthoritiesName(AuthoritiesConstants.CLIENT)+1);
            User result = clientRepository.save(user);

            return ResponseEntity.created(new URI("/api/clients/" + user.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("user", user.getId().toString()))
                    .body(result);
           }

    @RequestMapping(value ="/client/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getClient(@PathVariable String id) throws URISyntaxException {

       User result= clientRepository.findOne(id);
        if(result !=null){
           return new ResponseEntity<>(
                    result,
                    HttpStatus.OK);
        }

        else {
           result= clientRepository.findOneByFbID(id);
           if(result !=null)
            return new ResponseEntity<>(
                    result,
                    HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value ="/client",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateClient(@Validated @RequestBody User user) throws URISyntaxException {

        User result = clientRepository.save(user);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("user", user.getId().toString()))
                .body(result);

    }

    @RequestMapping(value ="/clientfb/{fbID}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getClientByFbID(@PathVariable String fbID) throws URISyntaxException {


        return Optional.ofNullable(clientRepository.findOneByFbID(fbID))
                .map(user -> new ResponseEntity<>(
                        user,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }




    @RequestMapping(value = "/client",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public List<User> getAllClients() {

        return clientRepository.findAllByAuthoritiesNameOrderByFirstName(AuthoritiesConstants.CLIENT);
    }







}
