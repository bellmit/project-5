package com.kindminds.drs.api.v1.model.replenishment;

import com.kindminds.drs.Warehouse;

import java.io.Serializable;

public interface ReplenishmentPredition extends Serializable {
	public enum ReplenishmentStatus {
		Normal(1,"Normal", ""),
		ExpiredIn7Days(2,"Expired", "Expeced production date in 7 days"),
		ExpiredIn14Days(3, "Start planning", "Expeced production date in 14 days"),
		ExpiredIn21Days(4, "Start planning", "Expeced production date in 21 days"),;
		
		private int key;
		private String statusText;
		private String statusDescription;
		
		ReplenishmentStatus(int key, String statusText, String statusDescription) {
			this.key = key;
			this.statusText = statusText;
			this.statusDescription = statusDescription;
		}
		
		public int getKey() {
			return this.key;
		}
		
		public String getStatusText() {
			return this.statusText;
		}
		
		public String getStatusDescription() {
			return this.statusDescription;
		}
	}
	public Warehouse getWarehouse();
	public int getReceivingPeriod();
	
	public interface Supplier {
		public String getSupplierKcode(); 
		public String getSupplierName();
	}
	
	public interface WarehouseDto {
		public int getKey();
		public String getName();
		public Warehouse getWarehouse();
	}
	
	public interface SkuReplenishmentPredition {
		public String getSkuNumber();
		public String getSkuName();
		public Integer getProductionDays();
		public Integer getSellableInventory();
		public Integer getPlanninQuantity();
		public Integer getInboundQuantity();
		public String getExpectedArrivalDate();
		public String getAverageWeeklySalesQuantity();
		public String getExpectedStockDepletionDate();
		public DeliveryDate getExpectedProductionDate();
		public FcaDeliveryDate getExpectedShippingDate();
		public String getExpectedEightWeeksReplenishmentQuantity();
		
		public interface FcaDeliveryDate {
			public String getCourierDate();
			public ReplenishmentStatus getCourierStatus();
			public Boolean getCourierSelloutRisk();
			public String getCourierMaxOutOfStockDays();
			public String getCourierMinOutOfStockDays();						
			public String getAirFreightDate();			
			public ReplenishmentStatus getAirFreightStatus();
			public Boolean getAirFreightSelloutRisk();
			public String getAirFreightMaxOutOfStockDays();
			public String getAirFreightMinOutOfStockDays();			
			public String getSurfaceFreightDate();
			public ReplenishmentStatus getSurfaceFreightStatus();
			public Boolean getSurfaceFreightSelloutRisk();
			public String getSurfaceFreightMaxOutOfStockDays();
			public String getSurfaceFreightMinOutOfStockDays();
		}
				
		public interface DeliveryDate {
			public String getCourierDate();
			public ReplenishmentStatus getCourierStatus();
			public String getAirFreightDate();
			public ReplenishmentStatus getAirFreightStatus();
			public String getSurfaceFreightDate();
			public ReplenishmentStatus getSurfaceFreightStatus();
		}
	}
}
