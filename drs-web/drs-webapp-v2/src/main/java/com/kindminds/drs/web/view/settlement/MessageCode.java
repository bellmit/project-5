package com.kindminds.drs.web.view.settlement;

import java.io.Serializable;

public class MessageCode implements Serializable{
	
	private static final long serialVersionUID = 6868800194052846845L;
	private String code;
	private String description;
	
	public MessageCode(){
		
	}
		
	public String getCode(){
		return this.code;		
	}
	
	public String getDescription(){
		return this.description;		
	}
	
}