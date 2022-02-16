package com.kindminds.drs.persist.v1.model.mapping;


import com.kindminds.drs.api.v1.model.SupplementalLink;

public class SupplementalLinkImpl implements SupplementalLink {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="supplier_kcode")
	private String supplierKcode;
	//@Column(name="name")
	private String name;
	//@Column(name="url")
	private String url;
	//@Column(name="description")
	private String description;

	public SupplementalLinkImpl() {
	}

	public SupplementalLinkImpl(int id, String supplierKcode, String name, String url, String description) {
		this.id = id;
		this.supplierKcode = supplierKcode;
		this.name = name;
		this.url = url;
		this.description = description;
	}

	@Override
	public String toString() {
		return "SupplementalLinkImpl [getId()=" + getId()
				+ ", getSupplierKcode()=" + getSupplierKcode() + ", getName()="
				+ getName() + ", getUrl()=" + getUrl() + ", getDescription()="
				+ getDescription() + "]";
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getUrl() {
		return this.url;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
