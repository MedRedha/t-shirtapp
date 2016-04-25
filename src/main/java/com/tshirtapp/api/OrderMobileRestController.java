package com.tshirtapp.api;

import com.tshirtapp.api.dto.OrderClientDTO;
import com.tshirtapp.api.dto.OrderDTO;
import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.api.util.PaginationUtil;
import com.tshirtapp.domain.Order;
import com.tshirtapp.domain.Product;
import com.tshirtapp.domain.User;
import com.tshirtapp.domain.util.Const;
import com.tshirtapp.repository.ClientRepository;
import com.tshirtapp.repository.OrderRepository;
import com.tshirtapp.repository.ProductRepository;
import com.tshirtapp.util.AppUtil;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by aminedev on 1/19/16.
 */

@RestController
@RequestMapping("/api/mobile")
public class OrderMobileRestController {


    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ProductRepository productRepository;

    @RequestMapping(value = "/order",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createProduct(@Validated @RequestBody Order order) throws URISyntaxException {
        if (order.getId() != null || clientRepository.findOneByFbID(order.getFbID()) == null)
            return ResponseEntity.badRequest().header("Failure", "A new order cannot already have an ID").body(null);
        User client = clientRepository.findOneByFbID(order.getFbID());
        if (client.getStatus().equals(Const.USER_BLOCKED))
            return ResponseEntity.badRequest().header("Failure", "Yor account has banned").body(null);
        if (orderRepository.findOneByUuid(order.getUuid()) == null) {
            Product product = productRepository.findOne(order.getProductID());
            if (product.getQuantity() >= order.getQuantity()) {
                product.setQuantity(product.getQuantity() - order.getQuantity());
                productRepository.save(product);
                if (client.getWilaya().toLowerCase().equals("alger"))
                    order.setStatus(Const.ORDER_NEW);
                else
                    order.setStatus(Const.ORDER_OUT_WILAYA);
                order.setCreationDate(ZonedDateTime.now());
                order.setCreatedFrom(Const.ORDER_FROM_MOBILE);
                order.setRef(orderRepository.count() + 1);
                order.setTotal(0D);
                order.setAdvance(0D);
                order.setUserID(clientRepository.findOneByFbID(order.getFbID()).getId());
                Order result = orderRepository.save(order);
                return ResponseEntity.created(new URI("/api/order/" + order.getUuid()))
                        .headers(HeaderUtil.createEntityCreationAlert("order", order.getId().toString()))
                        .body(result);
            } else
                return
                        ResponseEntity.badRequest().header("Failure", "Quantity non disponible,Quantity Max est  " + product.getQuantity()).body(null);
        } else
            return
                    ResponseEntity.badRequest().header("Failure", "product already exist").body(null);

    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<Void> handleFileUpload(@RequestParam("orderID") String orderID, @RequestParam("fbID") String fbID
            , @RequestParam("file") MultipartFile file
    ) throws IOException {
        User client = clientRepository.findOneByFbID(fbID);
        if (client.getStatus().equals(Const.USER_BLOCKED))
            return ResponseEntity.badRequest().header("Failure", "Yor account has banned").body(null);
        String dirApp = AppUtil.createAppDir();
        Path path = Paths.get(dirApp + File.separator + orderID + ".zip");
        if (Files.exists(path))
            return ResponseEntity.ok().build();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(dirApp + File.separator + orderID + ".zip")));
                stream.write(bytes);
                stream.close();
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @RequestMapping(value = "/upload/payment", method = RequestMethod.POST)
    public ResponseEntity<Void> uplaodPayment(@RequestParam("orderID") String orderID
            , @RequestParam("file") MultipartFile file
    ) throws IOException {
        Order order = orderRepository.findOne(orderID);
        String pathDir = AppUtil.createPaymentDir();
        try {
            String uuid = UUID.randomUUID().toString();
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(pathDir,
                            uuid + ext)));
            stream.write(bytes);
            stream.close();
            order.setPayment(uuid + ext);
            orderRepository.save(order);
            return ResponseEntity.created(new URI("/api/names/" + uuid))
                    .headers(HeaderUtil.createEntityCreationAlert("uuid", uuid + ext)).build()
                    ;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().header("Failure", "tag name alredy taken")
                    .body(null);
        }
    }

    @RequestMapping("payment")
    public
    @ResponseBody
    FileSystemResource getPayment(@RequestParam(name = "orderID") String orderID) throws IOException {
        String pathDir = AppUtil.createPaymentDir();
        String paymentID = orderRepository.findOne(orderID).getPayment();
        Path path = Paths.get(pathDir + File.separator + paymentID);
        if (Files.exists(path) && !Files.isDirectory(path))
            return new FileSystemResource(pathDir + File.separator + paymentID);
        else return null;

    }

    @RequestMapping(value = "/order",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderClientDTO>> getAllOrders(Pageable pageable, @RequestParam("fbID") String fbID)
            throws URISyntaxException, JSONException {
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize()
                , new Sort(
                new Sort.Order(Sort.Direction.DESC, "creationDate")));
        List<Order> page = orderRepository.findAllByFbIDOrderByCreationDateDesc(fbID);
        List<OrderClientDTO> managedOrdersDTOs = page.stream()
                .map(order -> {
                    OrderClientDTO orderDTO = new OrderClientDTO();
                    orderDTO.setStatus(order.getStatus());
                    orderDTO.setId(order.getId());
                    orderDTO.setProduct(productRepository.findOne(order.getProductID()));
                    orderDTO.setQuantity(order.getQuantity());
                    orderDTO.setTotal(order.getTotal());
                    orderDTO.setRef(order.getRef());
                    orderDTO.setCreationDate(order.getCreationDate());
                    orderDTO.setCanceledFor(order.getCanceledFor());
                    orderDTO.setPayment(order.getPayment());
                    return orderDTO;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(managedOrdersDTOs, HttpStatus.OK);
    }


    @RequestMapping(value = "/order/getpath",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPaths()
            throws URISyntaxException, JSONException, IOException {
        String pathDir = AppUtil.createPaymentDir();
        return new ResponseEntity<>(pathDir, HttpStatus.OK);
    }
}
