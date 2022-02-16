package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueComment;

public class CustomerCareCaseIssueCommentImpl implements CustomerCareCaseIssueComment {
	
	private int lineSeq;
	private String dateCreate;
	private String creator;
	private String content;
	
	public CustomerCareCaseIssueCommentImpl(
			int lineSeq,
			String dateCreate,
			String creator,
			String content) {
		this.lineSeq = lineSeq;
		this.dateCreate = dateCreate;
		this.creator = creator;
		this.content = content;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof CustomerCareCaseIssueComment==false)
			return false;
		CustomerCareCaseIssueComment c = (CustomerCareCaseIssueComment)o;
		return this.getLineSeq().equals(c.getLineSeq())
			&& this.getCreateBy().equals(c.getCreateBy())
			// && this.getDateCreate().equals(c.getDateCreate())
			&& this.getContents().equals(c.getContents());
	}

	@Override
	public String toString() {
		return "CustomerCareCaseIssueCommentImpl [getLineSeq()=" + getLineSeq()
				+ ", getDateCreate()=" + getDateCreate() + ", getCreateBy()="
				+ getCreateBy() + ", getContents()=" + getContents() + "]";
	}

	@Override
	public Integer getLineSeq() {
		return this.lineSeq;
	}

	@Override
	public String getDateCreate() {
		return this.dateCreate;
	}

	@Override
	public String getCreateBy() {
		return this.creator;
	}

	@Override
	public String getContents() {
		return this.content;
	}

}
