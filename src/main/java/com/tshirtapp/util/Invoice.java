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

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class Invoice {
	private Long id;
	private BigDecimal shipping;
	private Double tax;
	private RightReport rightReport;
	private LeftReport leftReport;
	private List<Item> items;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getShipping() {
		return shipping;
	}

	public void setShipping(BigDecimal shipping) {
		this.shipping = shipping;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}


	public RightReport getRightReport() {
		return rightReport;
	}

	public void setRightReport(RightReport rightReport) {
		this.rightReport = rightReport;
	}

	public LeftReport getLeftReport() {
		return leftReport;
	}

	public void setLeftReport(LeftReport leftReport) {
		this.leftReport = leftReport;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
