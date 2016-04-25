package com.tshirtapp.api;

import com.tshirtapp.api.dto.OrderDTO;
import com.tshirtapp.api.util.HeaderUtil;
import com.tshirtapp.api.util.PaginationUtil;
import com.tshirtapp.config.security.SecurityUtils;
import com.tshirtapp.domain.Order;
import com.tshirtapp.domain.Product;
import com.tshirtapp.domain.User;
import com.tshirtapp.domain.util.Const;
import com.tshirtapp.repository.ClientRepository;
import com.tshirtapp.repository.OrderRepository;
import com.tshirtapp.repository.ProductRepository;
import com.tshirtapp.repository.UserRepository;
import com.tshirtapp.util.AppUtil;
import com.tshirtapp.util.InvoiceDesign;
import com.tshirtapp.util.Unzip;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
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
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Amine on 12/22/15.
 */

@RestController
@RequestMapping("/api")
public class OrderRestController {


    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvoiceDesign shippingLabelDesign;


    @RequestMapping(value = "/order",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createOrder(@RequestBody Order order) throws URISyntaxException {
        User user = userRepository.findOneByUsername(SecurityUtils.getCurrentUser().getUsername()).get();
        if (order.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new order cannot already have an ID").body(null);
        Product product = productRepository.findOne(order.getProductID());
        if (product.getQuantity() >= order.getQuantity()) {
            product.setQuantity(product.getQuantity() - order.getQuantity());
            productRepository.save(product);
            order.setStatus(Const.ORDER_PROGRESS);
            order.setCreationDate(ZonedDateTime.now());
            order.setCreatedFrom(Const.ORDER_FROM_STORE);
            order.setRef(orderRepository.count() + 1);
            order.setUuid(UUID.randomUUID().toString());
            if (order.getTotal() == null)
                order.setTotal(0D);
            if (order.getAdvance() == null)
                order.setAdvance(0D);
            order.setValidatedBy(user);
            Order result = orderRepository.save(order);
            return ResponseEntity.created(new URI("/api/order/" + order.getUuid()))
                    .headers(HeaderUtil.createEntityCreationAlert("order", order.getId().toString()))
                    .body(result);
        } else
            return
                    ResponseEntity.badRequest().header("Failure", "Quantity non disponible").body(null);

    }


    @RequestMapping(value = "/order/{id}/{status}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String id, @PathVariable String status, @RequestParam("canceledFor") String canceledFor) throws URISyntaxException {
        Order result;
        Order order = orderRepository.findOne(id);
        order.setStatus(status);
        User user = userRepository.findOneByUsername(SecurityUtils.getCurrentUser().getUsername()).get();
        if (status.equals(Const.ORDER_CANCELED)) {
            order.setCanceledBy(user);
            order.setCancelDate(ZonedDateTime.now());
            order.setCanceledFor(canceledFor);
            result = orderRepository.save(order);
            Product product = productRepository.findOne(order.getProductID());
            product.setQuantity(product.getQuantity() + order.getQuantity());
            productRepository.save(product);
        } else if (status.equals(Const.ORDER_PROGRESS)) {
            order.setValidationDate(ZonedDateTime.now());
            order.setValidatedBy(user);
            result = orderRepository.save(order);

        } else {
            order.setDeliverDate(ZonedDateTime.now());
            // order.setRealizedBy(user);
            result = orderRepository.save(order);

        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus(result.getStatus());
        orderDTO.setId(result.getId());
        orderDTO.setImagerOrderList(result.getImagerOrderList());
        orderDTO.setProduct(productRepository.findOne(result.getProductID()));
        orderDTO.setClient(clientRepository.findOneByFbID(result.getFbID()));
        orderDTO.setQuantity(result.getQuantity());
        orderDTO.setTextOrders(result.getTextOrders());
        orderDTO.setUuid(result.getUuid());
        orderDTO.setUuid(result.getUuid());
        orderDTO.setCreatedFrom(result.getCreatedFrom());
        orderDTO.setCreationDate(result.getCreationDate());
        orderDTO.setTotal(result.getTotal());
        orderDTO.setRef(result.getRef());
        orderDTO.setDescription(result.getDescription());
        orderDTO.setCanceledFor(result.getCanceledFor());
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("user", order.getId().toString()))
                .body(orderDTO);

    }

    @RequestMapping(value = "/order",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> updateOrder(@RequestBody OrderDTO orderDTO) throws URISyntaxException {
        Order order = orderRepository.findOne(orderDTO.getId());
        order.setTotal(orderDTO.getTotal());
        order.setAdvance(orderDTO.getAdvance());
        order.setRealizedBy(orderDTO.getRealizedBy());
        Order result = orderRepository.save(order);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("order", order.getId().toString()))
                .body(result);

    }

    @RequestMapping(value = "/order/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String id) throws URISyntaxException {
        return Optional.ofNullable(orderRepository.findOne(id))
                .map(order ->
                        {
                            OrderDTO orderDTO = new OrderDTO();
                            orderDTO.setStatus(order.getStatus());
                            orderDTO.setId(order.getId());
                            orderDTO.setImagerOrderList(order.getImagerOrderList());
                            orderDTO.setProduct(productRepository.findOne(order.getProductID()));
                            orderDTO.setClient(clientRepository.findOne(order.getUserID()));
                            orderDTO.setQuantity(order.getQuantity());
                            orderDTO.setTextOrders(order.getTextOrders());
                            orderDTO.setUuid(order.getUuid());
                            orderDTO.setUuid(order.getUuid());
                            orderDTO.setCreatedFrom(order.getCreatedFrom());
                            orderDTO.setCreationDate(order.getCreationDate());
                            orderDTO.setRealizedBy(order.getRealizedBy());
                            orderDTO.setCanceledBy(order.getCanceledBy());
                            orderDTO.setValidatedBy(order.getValidatedBy());
                            orderDTO.setTotal(order.getTotal());
                            orderDTO.setRef(order.getRef());
                            orderDTO.setPayment(order.getPayment());
                            if (order.getAdvance() != null)
                                orderDTO.setAdvance(order.getAdvance());
                            orderDTO.setCanceledFor(order.getCanceledFor());
                            orderDTO.setDescription(order.getDescription());
                            return new ResponseEntity<>(
                                    orderDTO,
                                    HttpStatus.OK);

                        }
                )
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


    /**
     * GET  /users -> get all orders.
     */
    @RequestMapping(value = "/order",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> getAllOrders(Pageable pageable, @RequestParam("status") JSONObject object)
            throws URISyntaxException, JSONException {
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize()
                , new Sort(
                new Sort.Order(Sort.Direction.DESC, "creationDate")));
        Page<Order> page = orderRepository.findAllByStatusIn(pageRequest, getStatus(object));
        List<OrderDTO> managedOrdersDTOs = page.getContent().stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setStatus(order.getStatus());
                    orderDTO.setId(order.getId());
                    orderDTO.setImagerOrderList(order.getImagerOrderList());
                    orderDTO.setProduct(productRepository.findOne(order.getProductID()));
                    orderDTO.setClient(clientRepository.findOne(order.getUserID()));
                    orderDTO.setQuantity(order.getQuantity());
                    orderDTO.setTextOrders(order.getTextOrders());
                    orderDTO.setUuid(order.getUuid());
                    orderDTO.setCreatedFrom(order.getCreatedFrom());
                    orderDTO.setCreationDate(order.getCreationDate());
                    orderDTO.setTotal(order.getTotal());
                    orderDTO.setRef(order.getRef());
                    orderDTO.setPayment(order.getPayment());
                    return orderDTO;
                })
                .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order");
        return new ResponseEntity<>(managedOrdersDTOs, headers, HttpStatus.OK);
    }

    /*@RequestMapping(value = "/order{status}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public List<Order> getAllOrdersEnabled(@RequestParam String status) {

        return orderRepository.findAllByStatus(status);
    }*/


    @RequestMapping("order/{id}/image")
    public
    @ResponseBody
    FileSystemResource getImage(@PathVariable String id, @RequestParam(name = "name") String name) throws IOException {
        String dirApp = AppUtil.createAppDir();
        Path path = Paths.get(dirApp + File.separator + id);
        String zipName = orderRepository.findOne(id).getUuid();
        if (!Files.exists(path)) {
            Unzip.unZipIt(dirApp + File.separator + zipName + ".zip", dirApp + File.separator + id);
            Path zipPath = Paths.get(dirApp + File.separator + zipName + ".zip");
            Files.deleteIfExists(zipPath);
        }
        // headers.setContentType(MediaType.IMAGE_PNG);
        Path imagePath = Paths.get(dirApp + File.separator + id + "/" + name);
        if (Files.exists(imagePath))
            return new FileSystemResource(dirApp + File.separator + id + "/" + name);
        else return null;

    }


    @RequestMapping("order/{id}/screens")
    public List<String> getScreenShots(@PathVariable String id) throws IOException {
        String dirApp = AppUtil.createAppDir();
        Path path = Paths.get(dirApp + File.separator + id);
        String zipName = orderRepository.findOne(id).getUuid();
        if (!Files.exists(path)) {
            Unzip.unZipIt(dirApp + File.separator + zipName + ".zip", dirApp + File.separator + id);
            Path zipPath = Paths.get(dirApp + File.separator + zipName + ".zip");
            Files.deleteIfExists(zipPath);
        }
        File[] files = null;
        File imageFolder = null;
        List<String> listFiles = new ArrayList<>();
        imageFolder = new File(dirApp + File.separator + id);
        if (imageFolder.isDirectory())
            files = imageFolder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.getName().endsWith("-screenshot.jpg"))
                    listFiles.add(f.getName());
            }

        }
        return listFiles;

    }

    @RequestMapping("order/{id}/pdf")
    private FileSystemResource exportPdf(@PathVariable String id) throws DRException, IOException, JRException {
        JasperReportBuilder report = shippingLabelDesign.build(id);
        JasperExportManager.exportReportToPdfFile(report.toJasperPrint(), "temp.pdf");
        Path path = Paths.get("temp.pdf");
        if (Files.exists(path) && !Files.isDirectory(path))
            return new FileSystemResource("temp.pdf");
        else return null;

    }

    @RequestMapping("order/payment")
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

    private List<String> getStatus(JSONObject jsonObject) throws JSONException {
        List<String> temp = new ArrayList<>();
        if (jsonObject.getBoolean(Const.ORDER_CANCELED) == true)
            temp.add(Const.ORDER_CANCELED);
        if (jsonObject.getBoolean(Const.ORDER_NEW) == true)
            temp.add(Const.ORDER_NEW);
        if (jsonObject.getBoolean(Const.ORDER_PROGRESS) == true)
            temp.add(Const.ORDER_PROGRESS);
        if (jsonObject.getBoolean(Const.ORDER_DELIVERED) == true)
            temp.add(Const.ORDER_DELIVERED);
        if (jsonObject.getBoolean(Const.ORDER_OUT_WILAYA) == true)
            temp.add(Const.ORDER_OUT_WILAYA);
        if (jsonObject.getBoolean(Const.ORDER_WAIT_PAYMENT) == true)
            temp.add(Const.ORDER_WAIT_PAYMENT);
        return temp;
    }

}
