package com.kindminds.drs.persist.v1.model.mapping.customercare;


import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;

public class CustomerCareCaseIssueCommentImpl implements CustomerCareCaseIssue.CustomerCareCaseIssueComment {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="line_seq")
	private int lineSeq;
	//@Column(name="creator")
	private String creator;
	//@Column(name="date_create")
	private String dateCreate;
	//@Column(name="content")
	private String content;

	public CustomerCareCaseIssueCommentImpl() {
	}

	public CustomerCareCaseIssueCommentImpl(int id, int lineSeq, String creator, String dateCreate, String content) {
		this.id = id;
		this.lineSeq = lineSeq;
		this.creator = creator;
		this.dateCreate = dateCreate;
		this.content = content;
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
