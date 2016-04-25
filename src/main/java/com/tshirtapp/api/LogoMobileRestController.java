package com.tshirtapp.api;

import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.api.util.PaginationUtil;
import com.tshirtapp.domain.Logo;
import com.tshirtapp.repository.CategoryRepository;
import com.tshirtapp.repository.LogoRepository;
import com.tshirtapp.util.AppUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by aminedev on 1/23/16.
 */

@RestController
@RequestMapping("/api/mobile")
public class LogoMobileRestController {
    @Autowired
    LogoRepository logoRepository;

    @Autowired
    CategoryRepository categoryRepository;


    /**
     * GET  /users -> get all logos.
     */
    @RequestMapping(value = "/logo",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Logo>> getAllLogos(@RequestParam(value = "category") String category)
            throws URISyntaxException {
        List<Logo> logsDTOs = new ArrayList<>();
        if (category == null || category.equals("null"))
            logsDTOs = logoRepository.findAll();
        else
            logsDTOs = logoRepository.findByCategory(categoryRepository.findOne(category));
        return new ResponseEntity<>(logsDTOs, HttpStatus.OK);
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

}
