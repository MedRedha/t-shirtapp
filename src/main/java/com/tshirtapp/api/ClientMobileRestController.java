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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Amine on 12/22/15.
 */

@RestController
@RequestMapping("/api/mobile")
public class ClientMobileRestController {


    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AuthorityRepository authorityRepository;



    @RequestMapping(value = "/client",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createClient(@Validated @RequestBody User user) throws URISyntaxException {

        if (user.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new name cannot already have an ID").body(null);

        if (clientRepository.findOneByFbID(user.getFbID()) == null) {
            Set<Authority> authorities = new HashSet<>();
            Authority temp =authorityRepository.findOne(AuthoritiesConstants.CLIENT);
            authorities.add(temp);
            user.setAuthorities(authorities);
            user.setStatus(Const.USER_ACTIVATED);
            user.setClientRef(clientRepository.countAllByAuthoritiesName(AuthoritiesConstants.CLIENT)+1);
            User result = clientRepository.save(user);
            return ResponseEntity.created(new URI("/api/names/" + user.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("user", user.getId().toString()))
                    .body(result);
        } else
            return
                    ResponseEntity.badRequest().header("Failure", "User already exist").body(null);

    }

    @RequestMapping(value ="/client",
    method = RequestMethod.PUT,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateClient(@Validated @RequestBody User user) throws URISyntaxException {
        if (user.getId() == null) {
            return createClient(user);
        }
        User result = clientRepository.save(user);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("user", user.getId().toString()))
                .body(result);

    }

    @RequestMapping(value ="/client/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getClient(@PathVariable String id) throws URISyntaxException {


        return Optional.ofNullable(clientRepository.findOneByFbID(id))
                .map(user -> new ResponseEntity<>(
                        user,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

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

        return clientRepository.findAllByAuthoritiesName(AuthoritiesConstants.CLIENT);
    }



    @RequestMapping(value ="/profile",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateProfile(@Validated @RequestBody User user) throws URISyntaxException {
        User temp = clientRepository.findOneByFbID(user.getFbID());
        temp.setWilaya(user.getWilaya());
        temp.setPhoneNumber(user.getPhoneNumber());
        User result = clientRepository.save(temp);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("user", user.getFbID().toString()))
                .body(result);

    }


    @RequestMapping(value ="/client/token",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTokent( @RequestParam("fbID") String fbID,@RequestParam("token") String token) throws URISyntaxException {
        User temp = clientRepository.findOneByFbID(fbID);
        temp.setToken(token);
        User result = clientRepository.save(temp);
        return ResponseEntity.ok().build();

    }



}
