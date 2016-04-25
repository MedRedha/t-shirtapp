/**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 * <p>
 * Copyright (C) 2010 - 2015 Ricardo Mariaca
 * http://www.dynamicreports.org
 * <p>
 * This file is part of DynamicReports.
 * <p>
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */

package com.tshirtapp.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.tshirtapp.domain.ImageOrder;
import com.tshirtapp.domain.Order;
import com.tshirtapp.domain.Product;
import com.tshirtapp.domain.User;
import com.tshirtapp.repository.OrderRepository;
import com.tshirtapp.repository.ProductRepository;
import com.tshirtapp.repository.UserRepository;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
@Service
public class InvoiceData {
    private Invoice invoice;


    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

    public Invoice createInvoice(String id) {
        Order order = orderRepository.findOne(id);
        User user = userRepository.findOne(order.getUserID());
        Product product = productRepository.findOne(order.getProductID());
        Invoice invoice = new Invoice();
        invoice.setId(order.getRef());
        invoice.setShipping(BigDecimal.valueOf(0));
        invoice.setTax(10.0);
        invoice.setRightReport(new RightReport(product.getProductType().getName(), product.getSize(),
                product.getColor(), user.getClientRef().toString(), getLogos(order.getImagerOrderList())));
        invoice.setLeftReport(new LeftReport(order.getValidatedBy().getFirstName() + " " + order.getValidatedBy().getLastName()
                , order.getTotal(), order.getAdvance(), order.getTotal() - order.getAdvance()));
        List<Item> items = new ArrayList<Item>();
        items.add(createItem(product.getProductType().getName(), order.getQuantity(), BigDecimal.valueOf(order.getTotal())));
        invoice.setItems(items);
        return invoice;
    }


    private Item createItem(String description, Integer quantity, BigDecimal unitprice) {
        Item item = new Item();
        item.setDescription(description);
        item.setQuantity(quantity);
        item.setUnitprice(unitprice);
        return item;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public JRDataSource createDataSource(String id) {
        createInvoice(id);
        return new JRBeanCollectionDataSource(invoice.getItems());
    }

    private String getLogos(List<ImageOrder> imagerOrderList) {
        String temp = "";
        for(ImageOrder imageOrder : imagerOrderList){
            if(imageOrder.getLogo()==true){
                if(imageOrder.getView()==0)
                    return imageOrder.getTag();
            }
        }
        return  temp;
    }
}
