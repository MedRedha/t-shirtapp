package com.tshirtapp.api;

import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.api.util.PaginationUtil;
import com.tshirtapp.domain.Logo;
import com.tshirtapp.domain.Product;
import com.tshirtapp.repository.LogoRepository;
import com.tshirtapp.util.AppUtil;
import com.tshirtapp.util.Unzip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by aminedev on 1/23/16.
 */

@RestController
@RequestMapping("/api")
public class LogoRestController {
    @Autowired
    LogoRepository logoRepository;

    @RequestMapping("/logo")
    public ResponseEntity<Logo> create(@Validated @RequestBody Logo logo) throws URISyntaxException {
        if (logo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new logo cannot  have an ID")
                    .body(null);
        }
        if (logoRepository.findByTag(logo.getTag()) != null)
            return ResponseEntity.badRequest().header("Failure", "tag name alredy taken")
                    .body(null);
        Logo result = logoRepository.save(logo);
        return ResponseEntity.created(new URI("/api/names/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("user", result.getId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/logo",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Logo> updateLogo(@Validated @RequestBody Logo logo) throws URISyntaxException {
        if (logo.getId() == null) {
            return create(logo);
        }
        Logo result = logoRepository.save(logo);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("user", logo.getId().toString()))
                .body(result);

    }

    @RequestMapping(value = "/logo/upload", method = RequestMethod.POST)
    public ResponseEntity<Void> handleFileUpload(@RequestParam("file") MultipartFile file
    ) throws IOException {
        AppUtil.createAppDir();
        String pathDir = AppUtil.createLogoDir();
        try {
            String uuid = UUID.randomUUID().toString();
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(pathDir,
                            uuid + ext)));
            stream.write(bytes);
            stream.close();
            return ResponseEntity.created(new URI("/api/names/" + uuid))
                    .headers(HeaderUtil.createEntityCreationAlert("uuid", uuid + ext)).build()
                    ;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().header("Failure", "tag name alredy taken")
                    .body(null);
        }
    }


    /**
     * GET  /users -> get all logos.
     */
    @RequestMapping(value = "/logo",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Logo>> getAllLogos(Pageable pageable)
            throws URISyntaxException {
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(
                        new Sort.Order(Sort.Direction.ASC, "tag")
                ));
        Page<Logo> page = logoRepository.findAll(pageRequest);
        List<Logo> logsDTOs = page.getContent().stream()
                .map(user -> user)
                .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/logo");
        return new ResponseEntity<>(logsDTOs, headers, HttpStatus.OK);
    }


    @RequestMapping("logo/image")
    public
    @ResponseBody
    FileSystemResource getImage(@RequestParam(name = "name") String uuid) throws IOException {
        String pathDir = AppUtil.createLogoDir();
        Path path = Paths.get(pathDir + File.separator + uuid);
        if (Files.exists(path) && !Files.isDirectory(path))
            return new FileSystemResource(pathDir + File.separator + uuid);
        else return null;

    }


    @RequestMapping(value = "/logo/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) throws URISyntaxException, IOException {
        String pathDir = AppUtil.createLogoDir();
        Logo logo = logoRepository.findOne(id);
        if (logo == null)
            return ResponseEntity.badRequest().build();
        Path path = Paths.get(pathDir + File.separator + logo.getUuid());
        Files.delete(path);
        logoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("name", id.toString())).build();

    }

}
