package com.kindminds.drs.v1.model.impl.replenishment;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.SkuReplenishmentPredition;
import com.kindminds.drs.api.v1.model.replenishment.SkuInventoryInfo;
import com.kindminds.drs.util.DateHelper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class SkuReplenishmentPreditionImpl implements
		SkuReplenishmentPredition, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -658160187501261939L;
	
	private SkuInventoryInfo proxy;
	private BigDecimal averageWeeklySalesQuantity;
	private Date expectedStockDepletionDate;
	private DeliveryDate expectedProductionDate;
	private FcaDeliveryDate expectedShippingDate;
	private BigDecimal expectedEightWeeksReplenishmentQuantity;
	private final String dateFormat = "yyyy-MM-dd";
	
	public SkuReplenishmentPreditionImpl(SkuInventoryInfo proxy, BigDecimal averageWeeklySalesQuantity,
			Date expectedStockDepletionDate, DeliveryDate expectedProductionDate,
			FcaDeliveryDate expectedShippingDate, BigDecimal expectedEightWeeksReplenishmentQuantity) {
		this.proxy = proxy;
		this.averageWeeklySalesQuantity = averageWeeklySalesQuantity;
		this.expectedStockDepletionDate = expectedStockDepletionDate;
		this.expectedProductionDate = expectedProductionDate;
		this.expectedShippingDate = expectedShippingDate;
		this.expectedEightWeeksReplenishmentQuantity = expectedEightWeeksReplenishmentQuantity;
	}

	@Override
	public String getSkuNumber() {
		return proxy.getSkuCode();
	}
	
	@Override
	public String getSkuName() {
		return proxy.getSkuName();
	}

	@Override
	public Integer getProductionDays() {
		return proxy.getLeadTime();
	}

	@Override
	public Integer getSellableInventory() {
		return proxy.getSellableInventory();
	}

	@Override
	public Integer getPlanninQuantity() {
		return proxy.getPlanningQuantity();
	}
	
	@Override
	public Integer getInboundQuantity() {
		return proxy.getInboundQuantity();
	}

	@Override
	public String getExpectedArrivalDate() {
		if (proxy.getExpectedArrivableWarehouseDate() == null)
			return null;
		return DateHelper.toString(proxy.getExpectedArrivableWarehouseDate(), this.dateFormat);
	}

	@Override
	public String getAverageWeeklySalesQuantity() {
		if  (this.averageWeeklySalesQuantity == null)
			return null;
		return this.averageWeeklySalesQuantity.setScale(2, RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String getExpectedStockDepletionDate() {
		if (expectedStockDepletionDate == null)
			return null;
		return DateHelper.toString(expectedStockDepletionDate, this.dateFormat);
	}

	@Override
	public DeliveryDate getExpectedProductionDate() {
		return this.expectedProductionDate;
	}

	@Override
	public FcaDeliveryDate getExpectedShippingDate() {
		return this.expectedShippingDate;
	}

	@Override
	public String getExpectedEightWeeksReplenishmentQuantity() {
		if (this.expectedEightWeeksReplenishmentQuantity == null)
			return null;
		return this.expectedEightWeeksReplenishmentQuantity.setScale(0, RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((averageWeeklySalesQuantity == null) ? 0
						: averageWeeklySalesQuantity.hashCode());
		result = prime * result
				+ dateFormat.hashCode();
		result = prime
				* result
				+ ((expectedEightWeeksReplenishmentQuantity == null) ? 0
						: expectedEightWeeksReplenishmentQuantity.hashCode());
		result = prime
				* result
				+ ((expectedProductionDate == null) ? 0
						: expectedProductionDate.hashCode());
		result = prime
				* result
				+ ((expectedShippingDate == null) ? 0 : expectedShippingDate
						.hashCode());
		result = prime
				* result
				+ ((expectedStockDepletionDate == null) ? 0
						: expectedStockDepletionDate.hashCode());
		result = prime * result + ((proxy == null) ? 0 : proxy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkuReplenishmentPreditionImpl other = (SkuReplenishmentPreditionImpl) obj;
		if (averageWeeklySalesQuantity == null) {
			if (other.averageWeeklySalesQuantity != null)
				return false;
		} else if (!averageWeeklySalesQuantity
				.equals(other.averageWeeklySalesQuantity)) {
			return false;
		}
		if (!dateFormat.equals(other.dateFormat))
			return false;
		if (expectedEightWeeksReplenishmentQuantity == null) {
			if (other.expectedEightWeeksReplenishmentQuantity != null)
				return false;
		} else if (!expectedEightWeeksReplenishmentQuantity
				.equals(other.expectedEightWeeksReplenishmentQuantity))
			return false;
		if (expectedProductionDate == null) {
			if (other.expectedProductionDate != null)
				return false;
		} else if (!expectedProductionDate.equals(other.expectedProductionDate))
			return false;
		if (expectedShippingDate == null) {
			if (other.expectedShippingDate != null)
				return false;
		} else if (!expectedShippingDate.equals(other.expectedShippingDate))
			return false;
		if (expectedStockDepletionDate == null) {
			if (other.expectedStockDepletionDate != null)
				return false;
		} else if (!expectedStockDepletionDate
				.equals(other.expectedStockDepletionDate))
			return false;
		if (proxy == null) {
			if (other.proxy != null)
				return false;
		} else if (!proxy.equals(other.proxy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SkuReplenishmentPreditionImpl [getSkuNumber()=" + getSkuNumber() + ", getSkuName()=" + getSkuName()
				+ ", getProductionDays()=" + getProductionDays() + ", getSellableInventory()=" + getSellableInventory()
				+ ", getPlanninQuantity()=" + getPlanninQuantity() + ", getInboundQuantity()=" + getInboundQuantity()
				+ ", getExpectedArrivalDate()=" + getExpectedArrivalDate() + ", getAverageWeeklySalesQuantity()="
				+ getAverageWeeklySalesQuantity() + ", getExpectedStockDepletionDate()="
				+ getExpectedStockDepletionDate() + ", getExpectedProductionDate()=" + getExpectedProductionDate()
				+ ", getExpectedShippingDate()=" + getExpectedShippingDate()
				+ ", getExpectedEightWeeksReplenishmentQuantity()=" + getExpectedEightWeeksReplenishmentQuantity()
				+ ", hashCode()=" + hashCode() + "]";
	}

}
