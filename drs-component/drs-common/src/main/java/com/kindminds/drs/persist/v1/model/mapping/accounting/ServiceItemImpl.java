package com.kindminds.drs.persist.v1.model.mapping.accounting;

import java.io.Serializable;
import java.math.BigDecimal;









//@Table(name = "service_item")
public class ServiceItemImpl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1576521599412277769L;
	//@Id
	//@Column(name = "service_item_id")
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	//@Column(name = "item_name")
	private String name;
	//@Column(name = "amount")
	private BigDecimal amount;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getServiceItemName() {
		return name;
	}

	public void setServiceItemName(String name) {
		this.name = name;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
		
}
