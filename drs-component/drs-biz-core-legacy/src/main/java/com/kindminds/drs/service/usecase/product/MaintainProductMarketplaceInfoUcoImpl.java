package com.kindminds.drs.service.usecase.product;

import com.kindminds.drs.Context;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.product.MaintainProductMarketplaceInfoUco;
import com.kindminds.drs.api.v1.model.product.AmazonAsin;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo.ProductMarketStatus;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.product.MaintainProductMarketplaceInfoDao;
import com.kindminds.drs.v1.model.impl.ProductMarketplaceInfoImplSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MaintainProductMarketplaceInfoUcoImpl implements MaintainProductMarketplaceInfoUco {

	@Autowired private MaintainProductMarketplaceInfoDao dao;
	@Autowired private CompanyDao companyRepo;

	private static final BigDecimal drsRetainmentRate = new BigDecimal("0.15");

	private boolean checkProductSkuAccessable(String skuCodeByDrs){
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(Context.getCurrentUser().isDrsUser()) return true;
		String supplierKcodeOfBase = this.dao.querySupplierKcodeOfProductSku(skuCodeByDrs);
		if(userCompanyKcode.equals(supplierKcodeOfBase)) return true;
		return false;
	}

	@Override
	public ProductMarketplaceInfo update(ProductMarketplaceInfo info) {
		Assert.isTrue(this.checkProductSkuAccessable(info.getProductCodeByDrs()));
		ProductMarketplaceInfoImplSvc dto = new ProductMarketplaceInfoImplSvc(info);
		this.setAutoCalculatedFields(dto);
		String supplierKcodeOfBase = this.dao.querySupplierKcodeOfProductSku(info.getProductCodeByDrs());
		if(supplierKcodeOfBase.equals("K101")){
			return this.dao.updateForK101(dto);
		}
		return this.dao.update(dto);
	}

	@Override
	public boolean delete(String skuCodeByDrs,Integer marketplaceId) {
		Assert.isTrue(this.checkProductSkuAccessable(skuCodeByDrs));
		String supplierKcodeOfBase = this.dao.querySupplierKcodeOfProductSku(skuCodeByDrs);
		if(supplierKcodeOfBase.equals("K101")){
			return this.dao.deleteForK101(skuCodeByDrs, marketplaceId);
		}
		return this.dao.delete(skuCodeByDrs, marketplaceId);
	}

	@Override
	public List<String> getProductSkuMarketplaceStatusList() {
		ArrayList<String> list = new ArrayList<String>();
		for(ProductMarketStatus rs:ProductMarketStatus.values()){
			list.add(rs.name());
		}
		return list;
	}

	@Override
	public Map<String, String> getSupplierKcodeToNameMap() {
		return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
	}

	@Override
	public ProductMarketplaceInfo insert(ProductMarketplaceInfo info) {
		String supplierKcodeOfBase = this.dao.querySupplierKcodeOfProductSku(info.getProductCodeByDrs());
		if(supplierKcodeOfBase.equals("K101")){
			this.assertNonDuplicateMarketplaceForK101(info.getProductCodeByDrs(), info.getMarketplace().getKey());
			ProductMarketplaceInfoImplSvc dto = new ProductMarketplaceInfoImplSvc(info);
			this.setAutoCalculatedFields(dto);
			return this.dao.insertForK101(dto);
		}
		this.assertNonDuplicateMarketplace(info.getProductCodeByDrs(), info.getMarketplace().getKey());
		ProductMarketplaceInfoImplSvc dto = new ProductMarketplaceInfoImplSvc(info);
		this.setAutoCalculatedFields(dto);
		return this.dao.insert(dto);
	}

	private void assertNonDuplicateMarketplace(String productSkuCode,Integer marketplaceId){
		Assert.isTrue(!this.dao.queryMarketplaceIdListOfExistingMarketplaceInfo(productSkuCode).contains(marketplaceId));
	}

	private void assertNonDuplicateMarketplaceForK101(String productSkuCode,Integer marketplaceId){
		Assert.isTrue(!this.dao.queryMarketplaceIdListOfExistingMarketplaceInfoForK101(productSkuCode).contains(marketplaceId));
	}

	private void setAutoCalculatedFields(ProductMarketplaceInfoImplSvc dto){
		BigDecimal currentActualRetailPrice = new BigDecimal(dto.getCurrentBaseRetailPrice());
		BigDecimal referralRate = new BigDecimal(dto.getReferralRate());
		BigDecimal estimatedFulfillmentFees = new BigDecimal(dto.getEstimatedFulfillmentFees());
		BigDecimal estimatedImportDuty = new BigDecimal(dto.getEstimatedImportDuty());
		BigDecimal estimatedFreightCharge = new BigDecimal(dto.getEstimatedFreightCharge());
		BigDecimal priceToCalculateRevenue = currentActualRetailPrice;
		if(dto.getMarketplace()==Marketplace.AMAZON_CO_UK) priceToCalculateRevenue = currentActualRetailPrice.divide(new BigDecimal("1.2"),2,RoundingMode.HALF_UP);
		BigDecimal estimatedDrsRetainment = priceToCalculateRevenue.multiply(drsRetainmentRate);
		BigDecimal estimatedMarketplaceFees = currentActualRetailPrice.multiply(referralRate);
		BigDecimal approxSupplierNetRevenue = priceToCalculateRevenue
				.subtract(estimatedDrsRetainment)
				.subtract(estimatedMarketplaceFees)
				.subtract(estimatedFulfillmentFees)
				.subtract(estimatedImportDuty)
				.subtract(estimatedFreightCharge);
		dto.setEstimatedDrsRetainment(estimatedDrsRetainment.toPlainString());
		dto.setEstimatedMarketplaceFees(estimatedMarketplaceFees.toPlainString());
		dto.setApproxNetOperatingIncome(approxSupplierNetRevenue.toPlainString());
	}

	@Override
	public ProductMarketplaceInfo get(String skuCodeByDrs,Integer marketplaceId) {
		String supplierKcodeOfBase = this.dao.querySupplierKcodeOfProductSku(skuCodeByDrs);
		if(supplierKcodeOfBase.equals("K101")){
			return this.dao.queryForK101(skuCodeByDrs, marketplaceId);
		}
		return this.dao.query(skuCodeByDrs, marketplaceId);
	}

	@Override
	public ProductMarketplaceInfo getProductMarketplace(String skuCodeByDrs,Integer marketplaceId) {
		return this.dao.query(skuCodeByDrs, marketplaceId);
	}

	@Override
	public List<Marketplace> getMarketplaceList() {
		List<Marketplace> marketplaceList = Marketplace.getAmazonMarketplaces();
		marketplaceList.add(Marketplace.TRUETOSOURCE);
		return marketplaceList;
	}

	@Override
	public String getMarketplaceCurrency(String marketplaceName) {
		return Marketplace.valueOf(marketplaceName).getCurrency().name();
	}

	@Override
	public List<AmazonAsin> getLiveAmazonAsins() {
		return dao.queryLiveAmazonAsins();
	}

    @Override
    public List<AmazonAsin> getLiveK101AmazonAsins() {
        return dao.queryLiveK101AmazonAsins();
    }

}
