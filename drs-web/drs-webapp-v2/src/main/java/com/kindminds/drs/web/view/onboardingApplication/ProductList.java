package com.kindminds.drs.web.view.onboardingApplication;

import java.util.List;
import java.util.Map;

public class ProductList{

	String baseProduct ="";
	String productName="";
	String supplierKcode="";
	List<Map<String,Object>> variationProducts;



	public  ProductList(String baseProduct,String productName,String supplierKcode,
						List<Map<String,Object>> variationProducts){
	
        this.baseProduct =  baseProduct;
        this.productName = productName;
        this.supplierKcode = supplierKcode;
        this.variationProducts = variationProducts;
     

    }

}