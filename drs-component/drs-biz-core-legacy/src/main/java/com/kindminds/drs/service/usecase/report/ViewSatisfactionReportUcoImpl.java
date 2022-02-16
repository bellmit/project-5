package com.kindminds.drs.service.usecase.report;

import com.kindminds.drs.Context;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.ViewSatisfactionReportUco;
import com.kindminds.drs.v1.model.impl.report.DashboardStatisticsImpl;
import com.kindminds.drs.api.v1.model.report.CustomerSatisfactionReport;
import com.kindminds.drs.api.v1.model.report.Satisfaction;
import com.kindminds.drs.api.v1.model.report.SatisfactionReportLineItem;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.report.ViewSatisfactionReportDao;
import com.kindminds.drs.enums.ProductNameDisplayOption;
import com.kindminds.drs.v1.model.impl.report.CustomerSatisfactionReportImpl;
import com.kindminds.drs.v1.model.impl.report.SatisfactionReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewSatisfactionReportUcoImpl implements ViewSatisfactionReportUco {
	
	@Autowired ViewSatisfactionReportDao dao;
	@Autowired CompanyDao companyRepo;
	
	enum AmazonReturnReason {
		APPAREL_STYLE(true),
		APPAREL_TOO_LARGE(true),
		APPAREL_TOO_SMALL(true),
		DAMAGED_BY_CARRIER(false),
		DAMAGED_BY_FC(false),
		DEFECTIVE(true),
		EXCESSIVE_INSTALLATION(true),
		EXTRA_ITEM(false),
		FOUND_BETTER_PRICE(true),
		MISORDERED(false),
		MISSED_ESTIMATED_DELIVERY(false),
		MISSING_PARTS(true),
		NEVER_ARRIVED(false),
		NO_REASON_GIVEN(true),
		NOT_AS_DESCRIBED(true),
		NOT_COMPATIBLE(true),
		ORDERED_WRONG_ITEM(true),
		PART_NOT_COMPATIBLE(true),
		QUALITY_UNACCEPTABLE(true),
		SWITCHEROO(false),
		UNAUTHORIZED_PURCHASE(false),
		UNDELIVERABLE_CARRIER_MISS_SORTED(false),
		UNDELIVERABLE_FAILED_DELIVERY_ATTEMPTS(false),
		UNDELIVERABLE_INSUFFICIENT_ADDRESS(false),
		UNDELIVERABLE_MISSING_LABEL(false),
		UNDELIVERABLE_REFUSED(false),
		UNDELIVERABLE_UNCLAIMED(false),
		UNDELIVERABLE_UNKNOWN(false),
		UNKNOWN_OTHER_REASON(true),
		UNWANTED_ITEM(true);
		private boolean counted;
		AmazonReturnReason(boolean counted){ this.counted=counted; }
		public static List<String> getCountedReasons(){
			List<String> countedReturnReasons = new ArrayList<>();
			for(AmazonReturnReason reason: AmazonReturnReason.values()){
				if(reason.counted) countedReturnReasons.add(reason.name());
			}
			return countedReturnReasons;
		}
	}
	
	@Override
	public CustomerSatisfactionReport getReport(String marketplaceId, String supplierKCode) {
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		CustomerSatisfactionReportImpl report = new CustomerSatisfactionReportImpl();
		report.setEnd(this.dao.queryEnd());
		report.setLast1PeriodStart(this.dao.queryStart(0));
		report.setLast2PeriodStart(this.dao.queryStart(1));
		report.setLast6PeriodStart(this.dao.queryStart(5));
		boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		ProductNameDisplayOption option = this.getProductNameDisplayOption(isDrsUser);
		List<Satisfaction> reportList = this.dao.query(marketplace.getKey(),supplierKCode, AmazonReturnReason.getCountedReasons(), option);


		report.setLineItems(this.buildReport(this.buildResult(reportList)));
		return report;
	}

    private List<SatisfactionReportLineItem> buildResult(List<Satisfaction> dtoList) {

        List<SatisfactionReportLineItem>  reportList = new ArrayList<SatisfactionReportLineItem>();
        for (Satisfaction dto : dtoList) {
            DashboardStatisticsImpl lastOne = new DashboardStatisticsImpl();
            lastOne.setOrderQuantity(dto.getQuantityByOne());
            lastOne.setReturnQuantity(dto.getReturnQuantityByOne());
            DashboardStatisticsImpl lastTwo = new DashboardStatisticsImpl();
            lastTwo.setOrderQuantity(dto.getQuantityByTwo());
            lastTwo.setReturnQuantity(dto.getReturnQuantityByTwo());
            DashboardStatisticsImpl lastSix = new DashboardStatisticsImpl();
            lastSix.setOrderQuantity(dto.getQuantityBySix());
            lastSix.setReturnQuantity(dto.getReturnQuantityBySix());

            SatisfactionReportImpl report = new SatisfactionReportImpl();
            report.setProductSku(dto.getSkuCodeByDrs());
            report.setProductName(dto.getProductName());
            report.setLastOnePeriodData(lastOne);
            report.setLastTwoPeriodData(lastTwo);
            report.setLastSixPeriodData(lastSix);
            reportList.add(report);
        }
        return reportList;
    }
	
	private List<SatisfactionReportLineItem> buildReport(List<SatisfactionReportLineItem> proxy) {
		ArrayList<SatisfactionReportLineItem> reportList = new ArrayList<SatisfactionReportLineItem>();
		for (SatisfactionReportLineItem report : proxy) {
			reportList.add(new SatisfactionReportImpl(report));
		}
		return reportList;
	}

	private ProductNameDisplayOption getProductNameDisplayOption(boolean isDrsUser){
		if(isDrsUser) return ProductNameDisplayOption.DRS;
		else return ProductNameDisplayOption.SUPPLIER;
	}

}
