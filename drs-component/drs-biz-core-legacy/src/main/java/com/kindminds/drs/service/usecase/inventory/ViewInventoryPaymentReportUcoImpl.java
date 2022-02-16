package com.kindminds.drs.service.usecase.inventory;

import com.kindminds.drs.api.usecase.inventory.ViewInventoryPaymentReportUco;
import com.kindminds.drs.api.v1.model.report.InventoryPaymentReport;
import com.kindminds.drs.api.v1.model.report.InventoryPaymentReport.InventoryPaymentReportQuantitySummaryLineItem;
import com.kindminds.drs.api.data.access.usecase.inventory.ViewInventoryPaymentReportDao;
import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShipmentIvsDao;
import com.kindminds.drs.persist.v1.model.mapping.accounting.PaymentRecordReportQuantitySummaryLineItemImpl;
import com.kindminds.drs.v1.model.impl.PaymentRecordReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ViewInventoryPaymentReportUcoImpl implements ViewInventoryPaymentReportUco {
	
	@Autowired private ViewInventoryPaymentReportDao dao;
	@Autowired private MaintainShipmentIvsDao ivsDao;

	@Override
	public InventoryPaymentReport getInventoryPaymentReport(String shipmentName) {
		PaymentRecordReportImpl report = new PaymentRecordReportImpl();
		report.setShipmentName(shipmentName);
		report.setCurrency(this.ivsDao.queryCurrency(shipmentName));
		report.setQuantitySummaryLineItems(
				this.dao.queryInventoryPaymentReportAmountSummaryLineItems(shipmentName));

		report.setAmountDetailLineItems(this.dao.queryInventoryPaymentReportAmountDetailLineItems(shipmentName));
//		this.appendQuantitySummaryLineItemOriginalInvoiceQuantity(report);
		this.appendAmountSummaryLineItemOriginalInvoiceQuantity(report);


		return tempSln4FixingWrongQty(report);
		//return report;
	}

	//todo need change it
	private PaymentRecordReportImpl tempSln4FixingWrongQty(PaymentRecordReportImpl report ){

		Map<String , List<InventoryPaymentReport.InventoryPaymentReportAmountDetailLineItem>> dtlMap =
				new HashMap<String , List<InventoryPaymentReport.InventoryPaymentReportAmountDetailLineItem>>();


		report.getAmountDetailLineItems().forEach(x->{
			if(dtlMap.containsKey(x.getSku())){
				dtlMap.get(x.getSku()).add(x);

			}else{
				List<InventoryPaymentReport.InventoryPaymentReportAmountDetailLineItem> dtlList =
						new ArrayList<InventoryPaymentReport.InventoryPaymentReportAmountDetailLineItem>();

				dtlList.add(x);
				dtlMap.put(x.getSku() , dtlList);
			}
		});

		for (InventoryPaymentReportQuantitySummaryLineItem item : report.getQuantitySummaryLineItems()) {

			if(StringUtils.hasText(item.getSku())){

//				if(dtlMap.containsKey(item.getSku())){
//					Integer paidQty =0;
//					Integer refundQty =0;
//					for (InventoryPaymentReport.InventoryPaymentReportAmountDetailLineItem dtlItem
//							: dtlMap.get(item.getSku()) ) {
//
//						if(dtlItem.getItemName().equals("SS2SP_ProductInventoryPayment")){
//							paidQty += Integer.parseInt(dtlItem.getQuantity());
//						}else if (dtlItem.getItemName().equals("SS2SP_ProductInventoryRefund")){
//							refundQty += Integer.parseInt(dtlItem.getQuantity());
//						}
//					}
//
//					//item.setQuantityPaid(paidQty);
//					item.setQuantityRefund(refundQty);
//
//				}

				if(dtlMap.containsKey(item.getSku())){
					Integer paidAmount =0;
					Integer refundAmount =0;
					for (InventoryPaymentReport.InventoryPaymentReportAmountDetailLineItem dtlItem
							: dtlMap.get(item.getSku()) ) {

						if(dtlItem.getItemName().equals("SS2SP_ProductInventoryPayment")){
							paidAmount += Integer.parseInt(dtlItem.getAmount());
						}else if (dtlItem.getItemName().equals("SS2SP_ProductInventoryRefund")){
							refundAmount += Integer.parseInt(dtlItem.getAmount());
						}
					}

					//item.setQuantityPaid(paidQty);
					item.setQuantityRefund(refundAmount);

				}


			}

		}

		return report;
	}
	
//	private void appendQuantitySummaryLineItemOriginalInvoiceQuantity(InventoryPaymentReport report){
//
//		Map<String,Integer> skuToOriginalInvoiceQuantityMap =
//				this.dao.queryShipmentSkuToOriginalInvoiceQuantityMap(report.getShipmentName());
//
//		for(InventoryPaymentReportQuantitySummaryLineItem item:report.getQuantitySummaryLineItems()){
//			PaymentRecordReportQuantitySummaryLineItemImpl origItem =
//					(PaymentRecordReportQuantitySummaryLineItemImpl)item;
//
//			origItem.setOriginalInvoiceQuantity(skuToOriginalInvoiceQuantityMap.get(origItem.getSku()));
//
//		}
//	}

	private void appendAmountSummaryLineItemOriginalInvoiceQuantity(InventoryPaymentReport report){

		Map<String,Integer> skuToOriginalInvoiceQuantityMap =
				this.dao.queryShipmentSkuToOriginalInvoiceAmountMap(report.getShipmentName());

		for(InventoryPaymentReportQuantitySummaryLineItem item:report.getQuantitySummaryLineItems()){
			PaymentRecordReportQuantitySummaryLineItemImpl origItem =
					(PaymentRecordReportQuantitySummaryLineItemImpl)item;

			origItem.setOriginalInvoiceAmount(skuToOriginalInvoiceQuantityMap.get(origItem.getSku()));

		}
	}
}
