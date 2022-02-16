package com.kindminds.drs.web.data.dto;

public class IssueType {
	
	private Integer categoryId;
	private Integer typeId;
	private String name;
	
	public Integer getCategoryId(){		
		return this.categoryId;
	}
	
	public void setCategoryId(Integer categoryId){
		this.categoryId = categoryId;		
	}
		
	public Integer getTypeId(){
		return this.typeId;		
	}
	
	public void setTypeId(Integer typeId){
		this.typeId = typeId; 		
	}
		
	public String getName(){
		return this.name;		
	}
	
	public void setName(String name){
		this.name = name;		
	}
	
}