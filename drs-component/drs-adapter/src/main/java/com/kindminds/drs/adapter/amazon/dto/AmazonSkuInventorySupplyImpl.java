package com.kindminds.drs.adapter.amazon.dto;

import com.kindminds.drs.api.v1.model.amazon.AmazonSkuInventorySupply;

public class AmazonSkuInventorySupplyImpl implements AmazonSkuInventorySupply {
	
	private String marketplaceSku;
	private Integer totalSupplyQuantity;
	private Integer inStockSupplyQuantity;
	private Integer detailQuantityInStock;
	private Integer detailQuantityInbound;
	private Integer detailQuantityTransfer;
	
	public AmazonSkuInventorySupplyImpl(
			String marketplaceSku,
			Integer totalSupplyQuantity,
			Integer inStockSupplyQuantity,
			Integer detailQuantityInStock,
			Integer detailQuantityInbound,
			Integer detailQuantityTransfer) {
		super();
		this.marketplaceSku = marketplaceSku;
		this.totalSupplyQuantity = totalSupplyQuantity;
		this.inStockSupplyQuantity = inStockSupplyQuantity;
		this.detailQuantityInStock = detailQuantityInStock;
		this.detailQuantityInbound = detailQuantityInbound;
		this.detailQuantityTransfer = detailQuantityTransfer;
	}

	@Override
	public String toString() {
		return "AmazonSkuInventorySupplyImpl [getMarketplaceSku()=" + getMarketplaceSku()
				+ ", getTotalSupplyQuantity()=" + getTotalSupplyQuantity() + ", getInStockSupplyQuantity()="
				+ getInStockSupplyQuantity() + ", getDetailQuantityInStock()=" + getDetailQuantityInStock()
				+ ", getDetailQuantityInbound()=" + getDetailQuantityInbound() + ", getDetailQuantityTransfer()="
				+ getDetailQuantityTransfer() + "]";
	}

	@Override
	public String getMarketplaceSku() {
		return this.marketplaceSku;
	}

	@Override
	public Integer getTotalSupplyQuantity() {
		return this.totalSupplyQuantity;
	}

	@Override
	public Integer getInStockSupplyQuantity() {
		return this.inStockSupplyQuantity;
	}

	@Override
	public Integer getDetailQuantityInStock() {
		return this.detailQuantityInStock;
	}

	@Override
	public Integer getDetailQuantityInbound() {
		return this.detailQuantityInbound;
	}

	@Override
	public Integer getDetailQuantityTransfer() {
		return this.detailQuantityTransfer;
	}

}
