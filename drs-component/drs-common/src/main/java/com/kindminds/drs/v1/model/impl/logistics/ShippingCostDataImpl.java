package com.kindminds.drs.v1.model.impl.logistics;

import com.kindminds.drs.api.v1.model.logistics.ShippingCostData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShippingCostDataImpl implements ShippingCostData {
	
	private String skuCode;
	private String productBaseCode;
	private String countryCode;
	private String hsCode;
	private BigDecimal packageWeight;
	private String packageWeightUnit;
	private BigDecimal packageDimShort;
	private BigDecimal packageDimMedium;
	private BigDecimal packageDimLong;
	private String packageDimUnit;
	
	public ShippingCostDataImpl() {
		
	}
	
	public ShippingCostDataImpl(String skuCode, String countryCode, String hsCode, BigDecimal packageWeight,
                                String packageWeightUnit, BigDecimal packageDim1, BigDecimal packageDim2, BigDecimal packageDim3,
                                String packageDimUnit , String productBaseCode) {
		this.skuCode = skuCode;
		this.countryCode = countryCode;
		this.hsCode = hsCode;
		this.packageWeight = packageWeight;
		this.packageWeightUnit = packageWeightUnit;
		List<BigDecimal> packageDimensions = new ArrayList<>();
		packageDimensions.add(packageDim1);
		packageDimensions.add(packageDim2);
		packageDimensions.add(packageDim3);
		Collections.sort(packageDimensions);
		this.packageDimShort = packageDimensions.get(0);
		this.packageDimMedium = packageDimensions.get(1);
		this.packageDimLong = packageDimensions.get(2);
		this.packageDimUnit = packageDimUnit;
		this.productBaseCode = productBaseCode;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public String getProductBaseCode() {
		return productBaseCode;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public String getHsCode() {
		return hsCode;
	}
	public BigDecimal getPackageWeight() {
		return packageWeight;
	}
	public String getPackageWeightUnit() {
		return packageWeightUnit;
	}
	public BigDecimal getPackageDimLong() {
		return packageDimLong;
	}
	public BigDecimal getPackageDimMedium() {
		return packageDimMedium;
	}
	public BigDecimal getPackageDimShort() {
		return packageDimShort;
	}
	public String getPackageDimUnit() {
		return packageDimUnit;
	}

	@Override
	public String toString() {
		return "ShippingCostDataImpl{" +
				"skuCode='" + skuCode + '\'' +
				", productBaseCode='" + productBaseCode + '\'' +
				", countryCode='" + countryCode + '\'' +
				", hsCode='" + hsCode + '\'' +
				", packageWeight=" + packageWeight +
				", packageWeightUnit='" + packageWeightUnit + '\'' +
				", packageDimShort=" + packageDimShort +
				", packageDimMedium=" + packageDimMedium +
				", packageDimLong=" + packageDimLong +
				", packageDimUnit='" + packageDimUnit + '\'' +
				'}';
	}
}
