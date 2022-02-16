package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueComment;

public class IssueCommentImpl implements CustomerCareCaseIssueComment{

	private Integer lineSeq;
	private String dateCreate;
	private String createBy;
	private String contents;
	private Boolean pendingSupplierAction;
	
    public IssueCommentImpl(){};	
	
    public IssueCommentImpl(Integer lineSeq,String dateCreate,String createBy,String contents,Boolean pendingSupplierAction){
    	this.lineSeq = lineSeq;
    	this.dateCreate = dateCreate;
    	this.createBy = createBy;
    	this.contents = contents;
    	this.pendingSupplierAction = pendingSupplierAction;
    }
    
	@Override
	public Integer getLineSeq() {
		return this.lineSeq;
	}

	public void setLineSeq(Integer lineSeq) {
		this.lineSeq = lineSeq; 		
	}
	
	@Override
	public String getDateCreate() {
		return this.dateCreate;
	}

	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;		
	}
	
	@Override
	public String getCreateBy() {		
		return this.createBy;
	}
	
	public void setCreateBy(String createBy) {
		this.createBy = createBy;		
	}
	
	@Override
	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;		
	}
	
	public Boolean getPendingSupplierAction(){
		return this.pendingSupplierAction;		
	}
	
	public void setPendingSupplierAction(Boolean pendingSupplierAction){
		this.pendingSupplierAction = pendingSupplierAction;		
	}
		
}