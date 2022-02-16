package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.api.usecase.logistics.ViewUnsRecognizeRevenueReportUco;
import com.kindminds.drs.api.data.access.usecase.logistics.ViewUnsRecognizeRevenueReportDao;
import com.kindminds.drs.api.data.access.usecase.logistics.ViewUnsRecognizeRevenueReportDao.PaymentDetail;
import com.kindminds.drs.api.data.access.usecase.logistics.ViewUnsRecognizeRevenueReportDao.PriceInfo;
import com.kindminds.drs.enums.Incoterm;
import com.kindminds.drs.v1.model.impl.UnsGroupDetailImpl;
import com.kindminds.drs.v1.model.impl.UnsGroupImpl;
import com.kindminds.drs.v1.model.impl.accounting.PaymentDetailImpl;
import com.kindminds.drs.v1.model.impl.product.PriceInfoImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ViewUnsRecognizeRevenueReportUcoImpl implements ViewUnsRecognizeRevenueReportUco {
	
	@Autowired private ViewUnsRecognizeRevenueReportDao dao;

	@Override
	public String getTsvReport(String ms2ssStatementName) {
		Incoterm incoterm = Incoterm.DDP;
		List<Object []> columnsList = this.dao.queryUnsSkuPaymentDetails(ms2ssStatementName,incoterm.name());

		Map<String, Map<String,PaymentDetail>> unsSkuPaymentDetails = new HashMap<>();
		for(Object[] columns:columnsList){
			String unsName = (String)columns[0];
			String sku = (String)columns[1];
			Integer quantityPayment = BigInteger.valueOf(Long.parseLong(columns[2].toString())).intValue();
			Integer quantityRefund  =BigInteger.valueOf(Long.parseLong(columns[3].toString())).intValue();
			BigDecimal ddpSubtotal = (BigDecimal)columns[4];
			if(!unsSkuPaymentDetails.containsKey(unsName)) unsSkuPaymentDetails.put(unsName, new HashMap<>());
			Assert.isTrue(!unsSkuPaymentDetails.get(unsName).containsKey(sku));
			unsSkuPaymentDetails.get(unsName).put(sku, new PaymentDetailImpl(quantityPayment,quantityRefund,ddpSubtotal));
		}


		List<Object []> unsSkuPriceInfoList = this.dao.queryUnsSkuPriceInfo(ms2ssStatementName,incoterm.name());

		Map<String, Map<String,PriceInfo>> unsSkuPriceInfo = new HashMap<>();
		for(Object[] columns: unsSkuPriceInfoList){
			String unsName = (String)columns[0];
			String sku = (String)columns[1];
			BigDecimal ddpPrice = (BigDecimal)columns[2];
			BigDecimal cifPrice = (BigDecimal)columns[3];
			if(!unsSkuPriceInfo.containsKey(unsName)) unsSkuPriceInfo.put(unsName, new HashMap<>());
			Assert.isTrue(!unsSkuPriceInfo.get(unsName).containsKey(sku));
			unsSkuPriceInfo.get(unsName).put(sku,new PriceInfoImpl(ddpPrice,cifPrice));
		}


		List<UnsGroup> unsGroups = this.createUnsGroups(unsSkuPaymentDetails, unsSkuPriceInfo);
		Map<String,String> unsInvoiceNumber = this.dao.queryUnsInvoiceNumber(unsSkuPaymentDetails.keySet());
		this.setInvoiceNumber(unsGroups, unsInvoiceNumber);
		try {
			return this.toTsv(unsGroups);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<UnsGroup> createUnsGroups(Map<String,Map<String,PaymentDetail>> unsSkuPaymentDetails, Map<String,Map<String,PriceInfo>>  unsSkuPriceInfo){
		List<UnsGroup> unsGroups = new ArrayList<>();
		for(String unsName:unsSkuPaymentDetails.keySet()){
			List<UnsGroupDetail> unsGroupDetails = new ArrayList<>();
			for(String sku:unsSkuPaymentDetails.get(unsName).keySet()){
				PaymentDetail paymentDetail = unsSkuPaymentDetails.get(unsName).get(sku);
				PriceInfo unsPriceInfo = unsSkuPriceInfo.get(unsName).get(sku);
				//Assert.isTrue(this.ddpPriceValid(paymentDetail, unsPriceInfo));
				Integer quantityPayment = paymentDetail.getQuantityPayment();
				Integer quantityRefund  = paymentDetail.getQuantityRefund();
				BigDecimal ddpPrice = unsPriceInfo.getDdpPrice();
				BigDecimal cifPrice = unsPriceInfo.getCifPrice();
				unsGroupDetails.add(new UnsGroupDetailImpl(sku, ddpPrice, cifPrice, quantityPayment, quantityRefund));
			}
			unsGroups.add(new UnsGroupImpl(unsName,unsGroupDetails));
		}
		return unsGroups;
	}
	//TODO change rule
	private boolean ddpPriceValid(PaymentDetail paymentDetail,PriceInfo unsPriceInfo){
		BigDecimal quantityNet = new BigDecimal(paymentDetail.getQuantityPayment()-paymentDetail.getQuantityRefund());
		BigDecimal ddpUnitPrice = paymentDetail.getDdpSubtotal().divide(quantityNet);
		return unsPriceInfo.getDdpPrice().compareTo(ddpUnitPrice.abs())==0;
	}
	
	private void setInvoiceNumber(List<UnsGroup> unsGroups,Map<String,String> unsInvoiceNumber){
		for(UnsGroup unsGroup:unsGroups){
			String invoiceNumber = unsInvoiceNumber.get(unsGroup.getUnsName());
			unsGroup.setInvoiceNumber(invoiceNumber);
		}
	}
	
	@SuppressWarnings("resource")
	private String toTsv(List<UnsGroup> unsGroups) throws IOException{
		StringWriter writer = new StringWriter();
		CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.TDF.withHeader("Shipment name","Invoice","Total","SKU","DDP price","CIF price","QTY Order","QTY Refund","QTY Net","Subtotal"));
		for(UnsGroup unsGroup:unsGroups){
			int index = 0;
			for(UnsGroupDetail unsGroupDetail:unsGroup.getUnsGroupDetails()){
				if(index==0){
					csvPrinter.printRecord(unsGroup.getUnsName(),unsGroup.getInvoiceNumber(),unsGroup.getTotal(),
							unsGroupDetail.getSku(), unsGroupDetail.getDdpPrice(), unsGroupDetail.getCifPrice(), unsGroupDetail.getQuantityPayment(), unsGroupDetail.getQuantityRefund(), unsGroupDetail.getQuantityEffective(), unsGroupDetail.getSubtotalText());
				} else {
					csvPrinter.printRecord("","","",
							unsGroupDetail.getSku(), unsGroupDetail.getDdpPrice(), unsGroupDetail.getCifPrice(), unsGroupDetail.getQuantityPayment(), unsGroupDetail.getQuantityRefund(), unsGroupDetail.getQuantityEffective(), unsGroupDetail.getSubtotalText());	
				}
				index++;
			}
		}
		writer.flush();
		return writer.toString();
	}

}
