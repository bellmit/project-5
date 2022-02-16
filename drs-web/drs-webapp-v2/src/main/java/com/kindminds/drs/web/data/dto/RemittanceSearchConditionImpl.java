package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v1.model.accounting.RemittanceSearchCondition;

public class RemittanceSearchConditionImpl implements RemittanceSearchCondition {

	private String sndrKcode;
	private String rcvrKcode;
	
	public RemittanceSearchConditionImpl(){}
	
	public RemittanceSearchConditionImpl(
			String sndrKcode,
			String rcvrKcode){
		this.sndrKcode = sndrKcode;
		this.rcvrKcode = rcvrKcode;		
	}
	
	@Override
	public String getSndrKcode(){
		return this.sndrKcode;		
	} 
	
	public void setSndrKcode(String sndrKcode){
		this.sndrKcode = sndrKcode;
	}
	
	@Override
	public String getRcvrKcode(){
		return this.rcvrKcode;
	}
	
	public void setRcvrKcode(String rcvrKcode){
		this.rcvrKcode = rcvrKcode;
	}
		
}