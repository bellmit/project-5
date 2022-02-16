package com.kindminds.drs.api.v1.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto清單
 * @author HSDc Team
 *
 * @param <T>
 */
public  class DtoList<T> {
	/**
	 * 總筆數
	 */
	private int totalRecords;
	/**
	 * 分頁
	 */
	private Pager pager;
	/**
	 * 清單
	 */
	private List<T> items;
	/**
	 * 建構子
	 */
	public DtoList(){
		items = new ArrayList<T>();
	}
	/**
	 * 總筆數
	 * @return
	 */
	public int getTotalRecords() {
		return totalRecords;
	}
	/**
	 * 設定總筆數
	 * @param totalRecords
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
		
	/**
	 * 取得所有的項目列表
	 * @return
	 */
	public List<T> getItems() {
		return items;
	}
	/**
	 * 設定項目
	 * @param items
	 */
	public void setItems(List<T> items) {
		this.items = items;
		
	}
	/*
	 * 增加項目
	 */
	public void addItem(T item) {
		this.items.add(item);
	}
	/**
	 * 取得分頁
	 * @return
	 */
	public Pager getPager(){
		return this.pager;
	}
	
	/**
	 * 設定分頁
	 * @param pager
	 */
	public void setPager(Pager pager){
		this.pager = pager;
	}
		
	
}
