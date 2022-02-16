package com.kindminds.drs.web.view.onboardingApplication;

public class Product {

    public String productBaseCode ="";

    public String productInfoSource="";
    public  String productInfoMarketSide="";
    public  String productMarketingMaterialSource="";
    public String productMarketingMaterialMarketSide="";
    public String serialNumber="";


    public Product( String productBaseCode, String productInfoSource, String productInfoMarketSide,
                    String productMarketingMaterialSource, String productMarketingMaterialMarketSide, String serialNumber) {

        this.productBaseCode =  productBaseCode;
        this.productInfoSource = productInfoSource;
        this.productInfoMarketSide = productInfoMarketSide;
        this.productMarketingMaterialSource = productMarketingMaterialSource;
        this.productMarketingMaterialMarketSide = productMarketingMaterialMarketSide;
        this.serialNumber = serialNumber;
	

    }


	
	

}

