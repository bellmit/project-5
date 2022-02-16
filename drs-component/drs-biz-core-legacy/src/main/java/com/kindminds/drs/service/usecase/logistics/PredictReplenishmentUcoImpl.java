package com.kindminds.drs.service.usecase.logistics;

import com.kindminds.drs.Context;
import com.kindminds.drs.Warehouse;
import com.kindminds.drs.v1.model.impl.replenishment.*;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;
import com.kindminds.drs.api.usecase.logistics.PredictReplenishmentUco;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.SkuReplenishmentPredition;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.SkuReplenishmentPredition.DeliveryDate;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.SkuReplenishmentPredition.FcaDeliveryDate;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.Supplier;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.WarehouseDto;
import com.kindminds.drs.api.v1.model.replenishment.SkuInventoryInfo;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.logistics.PredictReplenishmentDao;
import com.kindminds.drs.core.biz.logistics.AirFreight;
import com.kindminds.drs.core.biz.logistics.Courier;
import com.kindminds.drs.core.biz.logistics.SurfaceFreight;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PredictReplenishmentUcoImpl implements PredictReplenishmentUco {
	
	@Autowired private PredictReplenishmentDao dao;
	@Autowired private CompanyDao cRepo;
	
	private int calculatePeriod = 28;
	
	@Override
	public List<WarehouseDto> getMarketplaceList() {
		List<WarehouseDto> dtos = new LinkedList<WarehouseDto>();
//		for(Warehouse w : Warehouse.values()) {
//			dtos.add(new WarehouseDtoImpl(w));
//		}
		dtos.add(new WarehouseDtoImpl(Warehouse.FBA_US));
		dtos.add(new WarehouseDtoImpl(Warehouse.FBA_CA));
		dtos.add(new WarehouseDtoImpl(Warehouse.FBA_EU));
		return dtos;
	}

	@Override
	public String initial() {
		return dao.getNotes();
	}

	@Override
	public Map<Supplier, List<SkuReplenishmentPredition>> retrieveSupplierSku(
			Warehouse warehouse) {
		return getSupplierSku(warehouse, null, null, null);
	}
	
	private Map<Supplier, List<SkuReplenishmentPredition>> getSupplierSku(Warehouse warehouse, 
			String sku, BigDecimal quantity, Integer leadTime) {
		String supplierId = null;
		if (cRepo.isSupplier(Context.getCurrentUser().getCompanyKcode()))
			supplierId = Context.getCurrentUser().getCompanyKcode();
		List<SkuInventoryInfo> skuInventories = dao.getSkuInventoryList(warehouse.getKey(), supplierId, calculatePeriod, sku);		
		ReplenishmentTimeSpent timeSpent = dao.getReplenishmentTimeSpent(warehouse.getKey());
		return buildReplenishmentPredition(skuInventories, timeSpent,  quantity, leadTime, warehouse);
	}
	
	private Map<Supplier, List<SkuReplenishmentPredition>> buildReplenishmentPredition(List<SkuInventoryInfo> skuInventories, ReplenishmentTimeSpent timeSpent, BigDecimal quantity, Integer leadTime, Warehouse warehouse) {
		Map<Supplier, List<SkuReplenishmentPredition>> map = new LinkedHashMap<Supplier, List<SkuReplenishmentPredition>>();
		Supplier supplier;
		for (SkuInventoryInfo info : skuInventories) {
			supplier = new SupplierImpl(info.getSupplierCode(), info.getSupplierCode() + " " + info.getSupplierShortName());
			List<SkuReplenishmentPredition> preditionList;
			if (!map.containsKey(supplier)) {
				preditionList = new ArrayList<SkuReplenishmentPredition>();
				map.put(supplier, preditionList);
			}
			preditionList = map.get(supplier);
			preditionList.add(buildReplenishmentPredition(info, timeSpent, quantity, leadTime, warehouse));
			map.put(supplier, preditionList);
		}
		return map;
	}
	
	private DeliveryDate calculateProductionDate(FcaDeliveryDate shippingDate, Integer leadTime, Integer manualLeadTime) {
		int MLT = leadTime;
		if (shippingDate == null)
			return null;
		if(manualLeadTime != null){
			MLT = manualLeadTime;			
		}
		String dateFormat = "yyyy-MM-dd";
		Date courierDate = null;
		Date airFreightDate = null;
		Date SurfaceFreightDate = null;		
		if(shippingDate.getCourierDate() != null){
			Calendar cc = Calendar.getInstance();
			cc.setTime(DateHelper.toDate(shippingDate.getCourierDate(), dateFormat));
			cc.add(Calendar.DATE, MLT*-1);
			courierDate = cc.getTime();			
		}
		if(shippingDate.getAirFreightDate() != null){
			Calendar afc = Calendar.getInstance();
			afc.setTime(DateHelper.toDate(shippingDate.getAirFreightDate(), dateFormat));
			afc.add(Calendar.DATE, MLT*-1);
			airFreightDate = afc.getTime();
		}
		if(shippingDate.getSurfaceFreightDate() != null){
			Calendar sfc = Calendar.getInstance();
			sfc.setTime(DateHelper.toDate(shippingDate.getSurfaceFreightDate(), dateFormat));
			sfc.add(Calendar.DATE, MLT*-1);
			SurfaceFreightDate = sfc.getTime();
		}		
		return new DeliveryDateImpl(courierDate, airFreightDate, SurfaceFreightDate);
	}
		
	private FcaDeliveryDate calculateRecommendedFcaDeliveryDate(Date expectedStockDepletionDate, ReplenishmentTimeSpent timeSpent, Warehouse warehouse){						
		if (expectedStockDepletionDate == null)
			return null;
		Courier courier = SpringAppCtx.get().getBean(Courier.class);
		AirFreight airFreight = SpringAppCtx.get().getBean(AirFreight.class);
		SurfaceFreight surfaceFreight = SpringAppCtx.get().getBean(SurfaceFreight.class);
		return new FcaDeliveryDateImpl(
				courier.calculateRecommendedFcaDeliveryDate(expectedStockDepletionDate, timeSpent, warehouse),
				airFreight.calculateRecommendedFcaDeliveryDate(expectedStockDepletionDate, timeSpent, warehouse),
				surfaceFreight.calculateRecommendedFcaDeliveryDate(expectedStockDepletionDate, timeSpent, warehouse),
				courier.calculateEarlySelloutDate(expectedStockDepletionDate, courier.calculateBufferPeriod(expectedStockDepletionDate)),
				courier.calculateLateSelloutDate(expectedStockDepletionDate, courier.calculateBufferPeriod(expectedStockDepletionDate)),
				courier.calculateEstimatedReceivingCompletionDate(expectedStockDepletionDate, timeSpent, warehouse),
				airFreight.calculateEstimatedReceivingCompletionDate(expectedStockDepletionDate, timeSpent, warehouse),
				surfaceFreight.calculateEstimatedReceivingCompletionDate(expectedStockDepletionDate, timeSpent, warehouse)			
		);		
	}
							
	private SkuReplenishmentPredition buildReplenishmentPredition(SkuInventoryInfo info, ReplenishmentTimeSpent timeSpent, BigDecimal quantity, Integer leadTime, Warehouse warehouse) {		
		BigDecimal averageWeeklySalesQuantity = this.calculateAverageWeeklySalesQuantity(info, quantity);
		Date expectedStockDepletionDate = this.calculateExpectedStockDepletionDate(info, averageWeeklySalesQuantity);
		FcaDeliveryDate recommendedFcaDeliveryDate = this.calculateRecommendedFcaDeliveryDate(expectedStockDepletionDate, timeSpent, warehouse);
		DeliveryDate expectedProductionDate = this.calculateProductionDate(recommendedFcaDeliveryDate, info.getLeadTime(), leadTime);
		BigDecimal expectedEightWeeksReplenishmentQuantity = null;
		if (averageWeeklySalesQuantity != null)
			expectedEightWeeksReplenishmentQuantity = averageWeeklySalesQuantity.multiply(new BigDecimal(8));
		SkuReplenishmentPreditionImpl rp = new SkuReplenishmentPreditionImpl(info, averageWeeklySalesQuantity, expectedStockDepletionDate,
				expectedProductionDate, recommendedFcaDeliveryDate, expectedEightWeeksReplenishmentQuantity);
		return rp;
	}
	
	/**
	 * 顯示:四捨五入到小數第二位(但是計算時保留完整的小數)

資料來源: 銷售量及庫存歷史檔

計算邏輯
Ａ. 有庫存的天數區間: 依照今天往前推算庫存>0的日子，直到庫存天數達到SPW Calculation Period，則以第一個庫存＞0的日期以及最後一個庫存>0 的日期作為有庫存的天數區間.
Ｂ. 庫存天數銷售量=合計有庫存的天數區間內的銷售量
Ｃ. 平均周銷售量= 7 * B / SPW Calculation Period
	 * @param info
	 * @return
	 */
	private BigDecimal calculateAverageWeeklySalesQuantity(SkuInventoryInfo info, BigDecimal quantity) {
		if (quantity != null && !quantity.equals(BigDecimal.ZERO))
			return quantity;
		if (info.getSalesDays() == null)
			return null;
		if(info.getSalesQuantitySum() == null || info.getSalesQuantitySum().equals(0))
			return BigDecimal.ZERO;
		
		BigDecimal awsq = new BigDecimal(info.getSalesQuantitySum()).divide(new BigDecimal(info.getSalesDays()), 10000, RoundingMode.HALF_EVEN).multiply(new BigDecimal(7));
		return awsq;
	}
	
	/**
	 * 計算順序:
1-1) 如果有預定到貨日且預定到貨日>=今天
A.計算每日銷售量＝平均周銷售量/7, 如果供應商輸入調整後的周銷售量, 則以調整後的周銷售量/7, 保留小數點不進位也不四捨五入
B. 計算到貨前天數＝預定到貨日-今天
C. 計算預定到貨日之前用掉的庫存量= A*B, 當 A*B > 可銷售量, 則令用掉的庫存量=可銷售量
D. 計算預定到貨日庫存量= 計劃數量+待入庫量+ 可銷售量 - C
E. 計算庫存售罄日數= D/ A, 無條件進位到整數
F. 計算庫存售罄日期= 預定到貨日+E -1

1-2) 如果有預定到貨日且預定到貨日<今天
A.計算每日銷售量＝平均周銷售量/7, 如果供應商輸入調整後的周銷售量, 則以調整後的周銷售量/7, 保留小數點不進位也不四捨五入
B. 計算到貨前天數:  因為預定到貨日已經過期，假設出表日會到貨，則到貨前天數＝0
C. 計算預定到貨日之前用掉的庫存量= A*B=0
D. 計算預定到貨日庫存量= 計劃數量+待入庫量+ 可銷售量 - C
E. 計算庫存售罄日數= D/ A, 無條件進位到整數
F. 計算庫存售罄日期= 出表日+E -1

2) 如果沒有預定到貨日
A. 計算每日銷售量＝平均周銷售量/7, 如果供應商輸入調整後的周銷售量, 則以調整後的周銷售量/7, 保留小數點不進位也不四捨五入
B.計算庫存售罄日數=可銷售量/ A, 無條件進位到整數
C. 計算庫存售罄日期= 今天 + B -1
	 * @param info
	 * @return
	 */
	private Date calculateExpectedStockDepletionDate(SkuInventoryInfo info, BigDecimal awsq) {
		if (awsq == null || awsq.equals(BigDecimal.ZERO))
			return null;
		if (info.getSellableInventory() == null)
			return null;
		BigDecimal daySales = awsq.divide(BigDecimal.valueOf(7), 10000, RoundingMode.HALF_EVEN);			
		int stockOutDays = (new BigDecimal(info.getSellableInventory())).divide(daySales, 100, RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_UP).intValue();
		Calendar baseday = getTodayCalendar();
		if (info.getExpectedArrivableWarehouseDate() != null){
			int dbsi = 0;
			BigDecimal dbsq = BigDecimal.ZERO;			
			BigDecimal inboundQuantity = info.getInboundQuantity() == null? BigDecimal.ZERO : new BigDecimal(info.getInboundQuantity());			
			BigDecimal planningQuantity = info.getPlanningQuantity() == null? BigDecimal.ZERO : new BigDecimal(info.getPlanningQuantity());			
			BigDecimal expectedStocking = planningQuantity.add(inboundQuantity).add(new BigDecimal(info.getSellableInventory())).subtract(dbsq);
			stockOutDays = expectedStocking.divide(daySales, 100, RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_UP).intValue();												
			if(info.getExpectedArrivableWarehouseDate().after(getToday()) || info.getExpectedArrivableWarehouseDate().equals(getToday())){						
				dbsi = calculateDaysBeforeStockingIn(info.getExpectedArrivableWarehouseDate());
				dbsq = daySales.multiply(new BigDecimal(dbsi));
				if (dbsq.compareTo(new BigDecimal(info.getSellableInventory()))>0)
					dbsq = new BigDecimal(info.getSellableInventory());
				expectedStocking = planningQuantity.add(inboundQuantity).add(new BigDecimal(info.getSellableInventory())).subtract(dbsq);
				stockOutDays = expectedStocking.divide(daySales, 100, RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_UP).intValue();
				baseday.setTime(info.getExpectedArrivableWarehouseDate());						
			}						
		}
		baseday.add(Calendar.DATE, stockOutDays - 1);
		return baseday.getTime();
	}
	
	private Date getToday(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();		
	}	
				
	private Calendar getTodayCalendar() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		return today;
	}
	
	private int calculateDaysBeforeStockingIn(Date startDt) {
		Calendar end = getTodayCalendar();
		Date endDt = end.getTime();
		long diff = startDt.getTime() - endDt.getTime();
		return (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	@Override
	public Map<Supplier, List<SkuReplenishmentPredition>> calculate(
			Warehouse warehouse, int calculatePeriod) {
		this.calculatePeriod = calculatePeriod;
		return this.retrieveSupplierSku(warehouse);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SkuReplenishmentPredition calculateManually(String sku, Warehouse warehouse, int calculatePeriod,
			BigDecimal quantity, int leadTime) {
		this.calculatePeriod = calculatePeriod;
		Map<Supplier, List<SkuReplenishmentPredition>> map = getSupplierSku(warehouse, sku, quantity, leadTime);
		SkuReplenishmentPredition result = null;
		for (Object s : map.values().toArray()) {
			result = ((List<SkuReplenishmentPredition>) s).get(0);
			break;
		}
		return result;
	}

	@Override
	public ReplenishmentTimeSpent retrieveWarehouseDescription(
			Warehouse warehouse) {
		return dao.getReplenishmentTimeSpent(warehouse.getKey());
	}

}
