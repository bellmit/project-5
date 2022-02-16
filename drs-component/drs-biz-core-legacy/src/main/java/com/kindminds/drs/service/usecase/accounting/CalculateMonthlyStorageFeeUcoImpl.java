package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.accounting.MaintainInternationalTransactionDao;
import com.kindminds.drs.api.data.access.usecase.report.ViewMonthlyStorageFeeReportDao;
import com.kindminds.drs.api.usecase.CalculateMonthlyStorageFeeUco;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;
import com.kindminds.drs.v1.model.impl.InternationalTransactionImpl;
import com.kindminds.drs.v1.model.impl.InternationalTransactionLineItemImpl;
import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.error.Mark;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CalculateMonthlyStorageFeeUcoImpl implements CalculateMonthlyStorageFeeUco {

	@Autowired private ViewMonthlyStorageFeeReportDao dao;
	@Autowired private CompanyDao companyRepo;
	@Autowired private MaintainInternationalTransactionDao internationalTransactionDao;

	private final Map<String,Integer> countryToMarketplaceId = Marketplace.getCountryToMarketplaceIdMap();

	private int yearStart = 2017;

	@Override
	public List<String> getYears() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int length = currentYear - yearStart;
		List<String> years = new ArrayList<>();
		for(int i=0;i<length;i++) years.add(String.valueOf(currentYear-i));
		return years;
	}

	@Override
	public List<String> getMonths() {
		return Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12");
	}

	@Override
	public String calculate(String year, String month) {

		int internationalTransactionCreateCount = 0;
		int monthlyStorageFeeCount = this.internationalTransactionDao.queryTotalCountsMonthlyStorageFee(year,month);
		if(monthlyStorageFeeCount != 0) return "No need to calculate";
		List<String> supplierKcodeList = this.dao.querySupplierKcodeList();

		Map<String,String> msdcKcodeCountryMap = this.dao.queryMsdcKcodeCountryMap();
		for(String kcode:supplierKcodeList){
			for (Map.Entry<String, String> entry : msdcKcodeCountryMap.entrySet()){
				System.out.println(entry.getKey());
				BigDecimal sum = this.calculateSumOfTotalEstimatedMonthlyStorageFee(kcode, entry.getValue(), year, month);
				System.out.println(sum);
				if(sum != null){
					this.generateInternationalTransaction(sum, kcode, entry.getKey(), year, month);
					internationalTransactionCreateCount = internationalTransactionCreateCount+1;
				}
			}
		}
		return internationalTransactionCreateCount +" created successfully";
	}

	@Override
	public BigDecimal calculateSumOfTotalEstimatedMonthlyStorageFee(String supplierKcode, String country,String year, String month){
		return this.dao.querySumOfTotalEstimatedMonthlyStorageFee(supplierKcode, country, this.countryToMarketplaceId.get(country), year, month);
	}

	private void generateInternationalTransaction(BigDecimal sum,String supplierKcode,String msdcKcode,String year, String month){
		List<InternationalTransaction.InternationalTransactionLineItem> lineItems = new ArrayList<InternationalTransaction.InternationalTransactionLineItem>();
		lineItems.add(new InternationalTransactionLineItemImpl(1,95,"FBA_MONTHLY_STORAGE",year+"-"+month+" FBA_MONTHLY_STORAGE",sum.toString()));
		Currency currency = this.companyRepo.queryCompanyCurrency(msdcKcode);
		String utcDate = new SimpleDateFormat("yyyy-MM-dd z").format(new Date());
		InternationalTransactionImpl transaction = new InternationalTransactionImpl(null,utcDate,0,msdcKcode,null,"K2",null,supplierKcode,null,currency.toString(),sum.toString(),year+"-"+month+" FBA_MONTHLY_STORAGE",lineItems);
		this.createInternationalTransaction(transaction);
	}

	private Integer createInternationalTransaction(InternationalTransaction transaction){
		Date transactionDate = DateHelper.toDate(transaction.getUtcDate()+" UTC","yyyy-MM-dd z");
		BigDecimal total = this.calculateTotal(transaction.getLineItems());
		if(transactionDate.before(this.dao.queryLastSettlementEnd())) return null;
		return this.internationalTransactionDao.insert(transaction,transactionDate,total);
	}

	private BigDecimal calculateTotal(List<InternationalTransaction.InternationalTransactionLineItem> lineItems){
		Assert.isTrue(!lineItems.isEmpty());
		BigDecimal total = BigDecimal.ZERO;
		for(InternationalTransaction.InternationalTransactionLineItem item:lineItems){
			total = total.add(new BigDecimal(item.getSubtotal()));
		}
		return total;
	}

}