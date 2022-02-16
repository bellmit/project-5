package com.kindminds.drs.persist;

import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;
import com.kindminds.drs.api.v1.model.product.BaseProduct;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;
import com.kindminds.drs.api.v1.model.product.SKU;

import com.kindminds.drs.persist.v1.model.mapping.product.ProductBaseImpl;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductMarketplaceInfoImpl;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductSkuImpl;
import org.json.JSONObject;

import java.math.BigDecimal;

public class TransformToProductV1 {

    public static BaseProduct transform(Product newProduct) {
        ProductBaseImpl baseProduct = new ProductBaseImpl();

        baseProduct.setSupplierCompanyKcode(newProduct.getSupplierKcode());
        baseProduct.setCodeBySupplier(newProduct.getProductBaseCode());



        try{
            JSONObject jsonObject = new JSONObject(newProduct.getData());
            baseProduct.setNameBySupplier(jsonObject.getString("productNameEnglish"));
            baseProduct.setInternalNotes(jsonObject.getString("note"));

        }catch (org.json.JSONException jsonex){
            jsonex.printStackTrace();

        }


        return baseProduct;
    }

    public static SKU transform(ProductVariation productVariation, String productBaseCode, String supplierKcode) {

        ProductSkuImpl sku = new ProductSkuImpl();
        sku.setCodeByDrs(productBaseCode);
        sku.setProductBaseCode(productBaseCode);
        sku.setCodeByDrs(supplierKcode +"-"+ productVariation.getVariationCode());
        sku.setCodeBySupplier(productVariation.getVariationCode());
        sku.setStatus(SKU.Status.SKU_DRAFT.toString());
        sku.setSupplierKcode(supplierKcode);

        try{

            JSONObject jsonObject = new JSONObject(productVariation.getData());
            if(jsonObject.getBoolean("providedByDRS"))
                sku.setEanProvider("DRS");
            else
                sku.setEanProvider("SUPPLIER");
            if(jsonObject.getString("GTINValue") != null)
                sku.setEan(jsonObject.getString("GTINValue"));

            if(jsonObject.getString("manufacturerLeadTime") != null)
                sku.setManufacturingLeadTimeDays(jsonObject.getString("manufacturerLeadTime"));

        }catch (org.json.JSONException jsonex){
            jsonex.printStackTrace();

            sku.setEanProvider("DRS");
            sku.setManufacturingLeadTimeDays("1");
        }



        return sku;
    }

    public static ProductMarketplaceInfo transform(SKU sku , Country country) {

        ProductMarketplaceInfoImpl pmi = new ProductMarketplaceInfoImpl();

        if(country == Country.US){
            pmi.setMarketplaceId(Marketplace.AMAZON_COM.getKey());
        }else if(country == Country.CA){
            pmi.setMarketplaceId(Marketplace.AMAZON_CA.getKey());
        }else if(country == Country.UK){
            pmi.setMarketplaceId(Marketplace.AMAZON_CO_UK.getKey());
        }else if(country == Country.DE){
            pmi.setMarketplaceId(Marketplace.AMAZON_DE.getKey());
        }else if(country == Country.FR){
            pmi.setMarketplaceId(Marketplace.AMAZON_FR.getKey());
        }else if(country == Country.IT) {
            pmi.setMarketplaceId(Marketplace.AMAZON_IT.getKey());
        }else if(country == Country.ES){
            pmi.setMarketplaceId(Marketplace.AMAZON_ES.getKey());
        }else if(country == Country.MX){
            pmi.setMarketplaceId(Marketplace.AMAZON_COM_MX.getKey());
        }

        pmi.setProductCodeByDrs(sku.getCodeByDrs());
        pmi.setMarketplaceSku(sku.getCodeBySupplier());

        pmi.setSupplierSuggestedActualRetailPrice(BigDecimal.valueOf(0));
        pmi.setApproxSupplierNetRevenue(BigDecimal.valueOf(0));
        pmi.setCurrentActualRetailPrice(BigDecimal.valueOf(0));
        pmi.setEstimatedDrsRetainment(BigDecimal.valueOf(0));
        pmi.setEstimatedFreightCharge(BigDecimal.valueOf(0));
        pmi.setEstimatedFulfillmentFees(BigDecimal.valueOf(0));
        pmi.setEstimatedImportDuty(BigDecimal.valueOf(0));
        pmi.setEstimatedMarketplaceFees(BigDecimal.valueOf(0));
        pmi.setMsrp(BigDecimal.valueOf(0));
        pmi.setReferralRate(BigDecimal.valueOf(0));


        //pmi.setCurrency(country.getCurrency().name());
        pmi.setStatus("Live");


        return pmi;
    }
}
