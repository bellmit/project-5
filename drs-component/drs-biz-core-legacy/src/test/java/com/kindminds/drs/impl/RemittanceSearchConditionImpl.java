package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.accounting.RemittanceSearchCondition;

public class RemittanceSearchConditionImpl implements RemittanceSearchCondition {
	
	private String sndrKcode;
	private String rcvrKcode;	

	public RemittanceSearchConditionImpl(String sndrKcode, String rcvrKcode) {
		this.sndrKcode = sndrKcode;
		this.rcvrKcode = rcvrKcode;
	}

	@Override
	public String getSndrKcode() {
		return this.sndrKcode;
	}

	@Override
	public String getRcvrKcode() {
		return this.rcvrKcode;
	}

}
