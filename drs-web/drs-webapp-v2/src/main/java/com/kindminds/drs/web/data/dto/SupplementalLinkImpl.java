package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v1.model.SupplementalLink;

public class SupplementalLinkImpl implements SupplementalLink{

	private Integer id;
	private String supplierKcode;	
	private String name;
	private String url;
	private String description;
		
	@Override
	public Integer getId() {		
		return this.id;
	}
	
	public void setId(Integer id){
		this.id = id;		
	}
	
	@Override
	public String getSupplierKcode() {		
		return this.supplierKcode;
	}
	
	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode;		
	}
	
	@Override
	public String getName() {		
		return this.name;
	}

	public void setName(String name){
		this.name = name;		
	}
		
	@Override
	public String getUrl() {		
		return this.url;
	}

	public void setUrl(String url){
		this.url = url;		
	}
	
	@Override
	public String getDescription() {		
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
		
}