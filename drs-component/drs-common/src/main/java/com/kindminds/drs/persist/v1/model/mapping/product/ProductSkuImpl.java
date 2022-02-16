package com.kindminds.drs.persist.v1.model.mapping.product;

import java.util.List;






import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;
import com.kindminds.drs.api.v1.model.product.SKU;


public class ProductSkuImpl implements SKU {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="supplier_company_kcode")
	private String supplierCompanyName;
	//@Column(name="product_base_code")
	private String productBaseCode;
	//@Column(name="code_by_supplier")
	private String codeBySupplier;
	//@Column(name="code_by_drs")
	private String codeByDrs;
	//@Column(name="name_by_supplier")
	private String nameBySupplier;
	//@Column(name="name_by_drs")
	private String nameByDrs;
	//@Column(name="ean")
	private String ean;
	//@Column(name="ean_provider")
	private String eanProvider;
	//@Column(name="status")
	private String status;
	//@Column(name="manufacturing_lead_time_days")
	private String manufacturingLeadTimeDays;
	//@Column(name="contain_lithium_ion_battery")
	private boolean containLithiumIonBattery;
	//@Column(name="internal_notes")
	private String internalNotes;

	private List<ProductMarketplaceInfo> regionInfoList=null;

	public ProductSkuImpl() {
	}

	public ProductSkuImpl(
			String supplierCompanyName,
			String productBaseCode,
			String codeBySupplier,
			String codeByDrs,
			String nameBySupplier,
			String nameByDrs,
			String ean,
			String eanProvider,
			String status,
			String manufacturingLeadTimeDays,
			boolean containLithiumIonBattery,
			String internalNotes) {
		this.supplierCompanyName = supplierCompanyName;
		this.productBaseCode = productBaseCode;
		this.codeBySupplier = codeBySupplier;
		this.codeByDrs = codeByDrs;
		this.nameBySupplier = nameBySupplier;
		this.nameByDrs = nameByDrs;
		this.ean = ean;
		this.eanProvider = eanProvider;
		this.status = status;
		this.manufacturingLeadTimeDays = manufacturingLeadTimeDays;
		this.containLithiumIonBattery = containLithiumIonBattery;
		this.internalNotes = internalNotes;
	}

	public ProductSkuImpl(int id, String supplierCompanyName, String productBaseCode, String codeBySupplier, String codeByDrs, String nameBySupplier, String nameByDrs, String ean, String eanProvider, String status, String manufacturingLeadTimeDays, boolean containLithiumIonBattery, String internalNotes) {
		this.id = id;
		this.supplierCompanyName = supplierCompanyName;
		this.productBaseCode = productBaseCode;
		this.codeBySupplier = codeBySupplier;
		this.codeByDrs = codeByDrs;
		this.nameBySupplier = nameBySupplier;
		this.nameByDrs = nameByDrs;
		this.ean = ean;
		this.eanProvider = eanProvider;
		this.status = status;
		this.manufacturingLeadTimeDays = manufacturingLeadTimeDays;
		this.containLithiumIonBattery = containLithiumIonBattery;
		this.internalNotes = internalNotes;
	}

	public void setRegionInfoList(List<ProductMarketplaceInfo> regionInfoList) {
		this.regionInfoList = regionInfoList;
	}

	@Override
	public String toString() {
		return "ProductSkuImpl [getSupplierKcode()=" + getSupplierKcode() + ", getBaseProductCode()="
				+ getBaseProductCode() + ", getCodeBySupplier()=" + getCodeBySupplier() + ", getCodeByDrs()="
				+ getCodeByDrs() + ", getNameBySupplier()=" + getNameBySupplier() + ", getNameByDrs()=" + getNameByDrs()
				+ ", getProductEAN()=" + getProductEAN() + ", getEanProvider()=" + getEanProvider() + ", getStatus()="
				+ getStatus() + ", getManufacturingLeadTimeDays()=" + getManufacturingLeadTimeDays()
				+ ", getContainLithiumIonBattery()=" + getContainLithiumIonBattery() + ", getInternalNote()="
				+ getInternalNote() + ", getRegionInfoList()=" + getRegionInfoList() + "]";
	}
	
	@Override
	public String getSupplierKcode() {
		return this.supplierCompanyName;
	}
	
	@Override
	public String getBaseProductCode() {
		return this.productBaseCode;
	}
	
	@Override
	public String getCodeBySupplier() {
		return this.codeBySupplier;
	}
	
	@Override
	public String getCodeByDrs() {
		return this.codeByDrs;
	}
	
	@Override
	public String getNameBySupplier() {
		return this.nameBySupplier;
	}

	@Override
	public String getNameByDrs() {
		return this.nameByDrs;
	}
	
	@Override
	public String getProductEAN() {
		return this.ean;
	}
	
	@Override
	public String getEanProvider() {

		return this.eanProvider != null ? EAN_PROVIDER.valueOf(this.eanProvider).name() : "";
	}

	@Override
	public String getStatus() {
		if(this.status==null) return null;
		return Status.valueOf(this.status).name();
	}
	
	@Override
	public String getManufacturingLeadTimeDays() {
		return this.manufacturingLeadTimeDays;
	}

	@Override
	public boolean getContainLithiumIonBattery() {
		return this.containLithiumIonBattery;
	}

	@Override
	public String getInternalNote() {
		return this.internalNotes;
	}
	
	@Override
	public List<ProductMarketplaceInfo> getRegionInfoList() {
		return this.regionInfoList;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSupplierKcode(String supplierCompanyName) {
		this.supplierCompanyName = supplierCompanyName;
	}

	public void setProductBaseCode(String productBaseCode) {
		this.productBaseCode = productBaseCode;
	}

	public void setCodeBySupplier(String codeBySupplier) {
		this.codeBySupplier = codeBySupplier;
	}

	public void setCodeByDrs(String codeByDrs) {
		this.codeByDrs = codeByDrs;
	}

	public void setNameBySupplier(String nameBySupplier) {
		this.nameBySupplier = nameBySupplier;
	}

	public void setNameByDrs(String nameByDrs) {
		this.nameByDrs = nameByDrs;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public void setEanProvider(String eanProvider) {
		this.eanProvider = eanProvider;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setManufacturingLeadTimeDays(String manufacturingLeadTimeDays) {
		this.manufacturingLeadTimeDays = manufacturingLeadTimeDays;
	}

	public void setContainLithiumIonBattery(boolean containLithiumIonBattery) {
		this.containLithiumIonBattery = containLithiumIonBattery;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}
}
