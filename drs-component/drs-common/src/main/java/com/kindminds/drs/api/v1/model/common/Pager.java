package com.kindminds.drs.api.v1.model.common;

/**
 * 分頁 Dto
 * @author HSDc Team
 *
 */
public class Pager {
	/**
	 * 每頁筆數
	 */
	private int pageSize = 10;
	/**
	 * 頁次大小
	 */
	private int eachPageSize = 10;
	/**
	 * 總頁數
	 */
	private int totalPages;
	/**
	 * 目前頁數
	 */
	private int currentPageIndex;
	/**
	 * 起始頁
	 */
	private int startPage;
	/**
	 * 結束頁
	 */
	private int endPage;

	private int startRowNum =0;

	private int endRowNum =0;

	public Pager(){

	}
	
	public Pager(int currentPageIndex ,int totalRecords){
		this(currentPageIndex, totalRecords, 20);

		this.startRowNum = ((currentPageIndex -1) * pageSize ) +1;
		this.endRowNum =(((currentPageIndex -1) * pageSize ) + pageSize) ;
	}
	
	public Pager(int currentPageIndex ,int totalRecords, int pageSize){
		this.currentPageIndex = currentPageIndex;
		this.pageSize = pageSize;
		this.totalPages = (int) Math.ceil(((double)totalRecords )/ ((double)this.pageSize));
		if(this.totalPages != 0){
			this.startPage = ((int)Math.floor(((double)(this.currentPageIndex-1) /this.eachPageSize)))*this.eachPageSize +1;
			this.endPage = this.startPage + this.eachPageSize -1;
			if(this.endPage > this.totalPages) this.endPage = this.totalPages;
		}

		this.startRowNum = ((currentPageIndex -1) * pageSize ) +1;
		this.endRowNum =(((currentPageIndex -1) * pageSize ) + pageSize) ;

	}
	
	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	
	public int getStartRowNum() {

		return startRowNum;
	}

	public int getEndRowNum() {

		return endRowNum;
	}


}
