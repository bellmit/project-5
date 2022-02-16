package com.kindminds.drs.impl;


import com.kindminds.drs.api.v1.model.SupplementalLink;
import com.kindminds.drs.util.TestUtil;

public class SupplementalLinkImpl implements SupplementalLink {
	
	private Integer id;
	private String supplierKcode;
	private String name;
	private String url;
	private String description;
	
	public SupplementalLinkImpl(
			Integer id,
			String supplierKcode,
			String name,
			String url,
			String description){
		this.id = id;
		this.supplierKcode = supplierKcode;
		this.name = name;
		this.url = url;
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj){
		if( obj instanceof SupplementalLink ){
			SupplementalLink link = (SupplementalLink)obj;
			return TestUtil.nullableEquals(this.getSupplierKcode(),link.getSupplierKcode())
				&& TestUtil.nullableEquals(this.getName(), link.getName())
				&& TestUtil.nullableEquals(this.getUrl(), link.getUrl())
				&& TestUtil.nullableEquals(this.getDescription(), link.getDescription());
		}
		return false;
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
