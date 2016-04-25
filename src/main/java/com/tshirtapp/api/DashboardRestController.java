package com.tshirtapp.api;

import com.tshirtapp.api.dto.DashboardDTO;
import com.tshirtapp.config.security.AuthoritiesConstants;
import com.tshirtapp.domain.util.Const;
import com.tshirtapp.repository.ClientRepository;
import com.tshirtapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by aminedev on 1/29/16.
 */

@RestController
@RequestMapping("/api")
public class DashboardRestController {


    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(value = "/dashboard",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardDTO> getDashboard() {
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setNbOrders(orderRepository.count());
        dashboardDTO.setNbOrdersNew(orderRepository.countByStatus(Const.ORDER_NEW));
        dashboardDTO.setNbOrdersDelivered(orderRepository.countByStatus(Const.ORDER_DELIVERED));
        dashboardDTO.setNbClient(clientRepository.countByAuthoritiesName(AuthoritiesConstants.CLIENT));
        return ResponseEntity.ok().body(dashboardDTO);
    }

}
