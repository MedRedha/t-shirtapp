/**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 *
 * Copyright (C) 2010 - 2015 Ricardo Mariaca
 * http://www.dynamicreports.org
 *
 * This file is part of DynamicReports.
 *
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */

package com.tshirtapp.util;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;

import com.tshirtapp.domain.Order;
import com.tshirtapp.domain.User;
import com.tshirtapp.repository.OrderRepository;
import com.tshirtapp.repository.UserRepository;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
@Service
public class InvoiceDesign {
	@Inject
	private InvoiceData invoiceData;
	@Autowired
	OrderRepository orderRepository;

	private AggregationSubtotalBuilder<BigDecimal> totalSum;
	Invoice data;

	public JasperReportBuilder build(String id ) throws DRException {
		data = invoiceData.createInvoice(id);
		Order order = orderRepository.findOne(id);
		JasperReportBuilder report = report();
		//init styles
		StyleBuilder columnStyle = stl.style(Templates.columnStyle)
				.setBorder(stl.pen1Point());
		StyleBuilder subtotalStyle = stl.style(columnStyle)
				.bold();
		StyleBuilder shippingStyle = stl.style(Templates.boldStyle)
				.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		//init columns
		TextColumnBuilder<Integer> rowNumberColumn = col.reportRowNumberColumn()
				.setFixedColumns(2)
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
		TextColumnBuilder<String> descriptionColumn = col.column("Article", "description", type.stringType())
				.setFixedWidth(250);
		TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType())
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
		TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Prix", "unitprice", Templates.currencyType);
		TextColumnBuilder<String> taxColumn = col.column("Tax", exp.text("0%"))
				.setFixedColumns(3);
		//price = unitPrice * quantity
		TextColumnBuilder<BigDecimal> priceColumn = unitPriceColumn.multiply(1)
				.setTitle("Prix")
				.setDataType(Templates.currencyType);
		//vat = price * tax

		//total = price + vat
		TextColumnBuilder<BigDecimal> totalColumn = priceColumn.
				setTitle("Prix Total")
				.setDataType(Templates.currencyType)
				.setRows(0)
				.setStyle(subtotalStyle);
		//init subtotals
		totalSum = sbt.sum(totalColumn)
				.setLabel("Total:")
				.setLabelStyle(Templates.boldStyle);
		//configure report
		report
				.setTemplate(Templates.reportTemplate)
				.setColumnStyle(columnStyle)
				.setSubtotalStyle(subtotalStyle)
				//columns
				.columns(
						rowNumberColumn, descriptionColumn, quantityColumn, unitPriceColumn, totalColumn, priceColumn, taxColumn)
				.columnGrid(
						rowNumberColumn, descriptionColumn, quantityColumn, unitPriceColumn)
				//subtotals
				.subtotalsAtSummary(
						totalSum, sbt.sum(priceColumn))
				//band components
				.title(
						Templates.createTitleComponent("Commande No.: " + data.getId()),
						cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
								cmp.hListCell(createLeftComponent("", data.getLeftReport())).heightFixedOnTop(),
								cmp.hListCell(createRightComponent("", data.getRightReport())).heightFixedOnTop()
								),
						cmp.verticalGap(10))
				.pageFooter(
						Templates.footerComponent)
				.summary(
						cmp.text(data.getShipping()).setValueFormatter(Templates.createCurrencyValueFormatter("Shipping:")).setStyle(shippingStyle),
						cmp.horizontalList(
								cmp.text("COMMANDE A PARTIRE DE :" + order.getCreatedFrom()).setStyle(Templates.bold12CenteredStyle),
								cmp.text(new TotalPaymentExpression()).setStyle(Templates.bold12CenteredStyle)),
						cmp.verticalGap(30),
						cmp.text("Thank you for your business").setStyle(Templates.bold12CenteredStyle))
				.setDataSource(new JRBeanCollectionDataSource(data.getItems()));
		return report;
	}



	private void addCustomerAttribute(HorizontalListBuilder list, String label, String value,Color color) {
		if (value != null) {
			StyleBuilder columnTitleStyle  = stl.style(Templates.boldStyle)

					.setBackgroundColor(color);

			list.add(cmp.text(label + ":").setFixedColumns(8).setStyle(Templates.boldStyle), cmp.text(value).setStyle(columnTitleStyle)).newRow();
		}
	}

	private class TotalPaymentExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;

		@Override
		public String evaluate(ReportParameters reportParameters) {
			BigDecimal total = reportParameters.getValue(totalSum);
			BigDecimal shipping = total.add(data.getShipping());
			return "Total payment: " + Templates.currencyType.valueToString(shipping, reportParameters.getLocale());
		}
	}


	private ComponentBuilder<?, ?> createLeftComponent(String label, LeftReport leftReport) {
		HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
		addCustomerAttribute(list, "Pris par", leftReport.takenBy,Color.WHITE);
		addCustomerAttribute(list, "Prix", leftReport.total.toString(),Color.WHITE);
		addCustomerAttribute(list, "Avance", leftReport.getAdvance().toString(),Color.WHITE);
		addCustomerAttribute(list, "Rest", leftReport.getRest().toString(),Color.WHITE);
		return cmp.verticalList(
				cmp.text(label).setStyle(Templates.boldStyle),
				list);
	}

	private ComponentBuilder<?, ?> createRightComponent(String label, RightReport leftReport) {
		HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
		addCustomerAttribute(list, "Article", leftReport.getProduct(),Color.WHITE);
		addCustomerAttribute(list, "Taille", leftReport.getSize(),Color.WHITE);
		addCustomerAttribute(list, "Couleur", leftReport.getColor(),Color.decode(leftReport.getColor()));
		addCustomerAttribute(list, "Num du client", leftReport.getClient(),Color.WHITE);
		addCustomerAttribute(list, "Code Logo", leftReport.getCodeLogo(),Color.WHITE);
		return cmp.verticalList(
				cmp.text(label).setStyle(Templates.boldStyle),
				list);
	}



}