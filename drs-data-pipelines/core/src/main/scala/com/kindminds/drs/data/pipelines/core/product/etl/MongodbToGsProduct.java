package com.kindminds.drs.data.pipelines.core.product.etl;

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.GsProductDao;
import com.kindminds.drs.data.pipelines.core.BizCoreCtx;
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MApplicationDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MProductDaoImpl;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.ShippingDaoImpl;
import com.kindminds.drs.persist.v1.model.mapping.product.GsProductDataImpl;
import org.bson.Document;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class MongodbToGsProduct {


    public static void main(String[] args) {
        final AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();
        //ean, code_by_drs, marketplace_sku, marketplace_id, name_by_drs, contain_lithium_ion_battery
        P2MApplicationDao p2mDao = new P2MApplicationDao();

        ProductDao productDao = new ProductDao();

        P2MProductDaoImpl p2MProductDao = new P2MProductDaoImpl();

        ShippingDaoImpl shippingDao = new ShippingDaoImpl();

        List<Document> p2m = p2mDao.findP2mApplicationList();

        GsProductDao gpd = springCtx.getBean(GsProductDao.class);



        for(Document doc : p2m) {
            String id = doc.get("_id").toString();

            String supplierCompany = doc.getString("supplierId");

            String bpId = doc.getString("bpId");

            String selectedPlatform = doc.getString("selectedPlatform");

            String status = doc.get("status").toString();

            String Country  = "";

            if (selectedPlatform.equals("Amazon.com")) {
                Country  = "US";
            } else if (selectedPlatform.equals("TrueToSource")) {
                Country  = "US";
            } else if (selectedPlatform.equals("Amazon.co.uk")) {
                Country  = "UK";
            } else if (selectedPlatform.equals("Amazon.ca")) {
                Country  = "CA";
            } else if (selectedPlatform.equals("Amazon.de")) {
                Country  = "DE";
            } else if (selectedPlatform.equals("Amazon.fr")) {
                Country  = "FR";
            } else if (selectedPlatform.equals("Amazon.it")) {
                Country  = "IT";
            } else if (selectedPlatform.equals("Amazon.es")) {
                Country  = "ES";
            }

            List<String> appliedSkuCode = (List<String>) doc.get("appliedSkuCode");

            try {
            if(supplierCompany.equals("K650") || supplierCompany.equals("K652") ||supplierCompany.equals("K653") ){
                if(status.equals("Approved")){

                Document product = productDao.findById(bpId);

            Document pai = Document.parse(p2MProductDao.findByP2MApplicationId(id));

            List<Document> p2mProductAdvancedInfoSku = (List<Document>) pai.get("appliedSku");

            Document shi = Document.parse(shippingDao.findByP2MApplicationId(id));

            List<Document> p2mShippingSku = (List<Document>) shi.get("appliedSku");

            String baseProductCode = product.getString("baseCode");

            String gtin = "";

            String sku = "";

            String productName = doc.getString("productNameEN");

            String productCategory = product.getString("category");

            String brandEng  = doc.getString("brandNameEN");

            String countryOfOrigin  = "";

            String hsCode  = "";

            String manufacturerLeadTime  = "";

            String fcaPrice  = "";

            String productDimensionL  = "";

            String productDimensionW  = "";

            String productDimensionH  = "";

            String productDimensionUnit  = "cm";

            String productMaterialPercent  = "";

            String productWeight  = "";

            String productWeightUnit  = "g";

            String salesPrice  = "";

            String note  = "";

            String packageDimensionL  = "";

            String packageDimensionW  = "";

            String packageDimensionH  = "";

            String packageDimensionUnit  = "cm";

            String packageWeight  = "";

            String packageWeightUnit  = "g";

            String quantityPerCarton  = "";

            String cartonDimensionL  = "";

            String cartonDimensionW  = "";

            String cartonDimensionH  = "";

            String cartonDimensionUnit  = "";

            String cartonWeight  = "";

            String cartonWeightUnit  = "";

            String importDutyRate  = "";

            String salesSideHsCode  = "";

            String unfulFillableInventoryRemovalSettings  = "";

            String ddpPrice  = "";



            for(String x : appliedSkuCode){
                for(Document paiSku : p2mProductAdvancedInfoSku){
                    if (x.equals(paiSku.getString("skuCode"))){
                        Document productId = (Document) paiSku.get("productId");
                        sku = x;
                        gtin = productId.getString("value");
                        countryOfOrigin = pai.getString("manufacturePlace");
                        manufacturerLeadTime = pai.getString("manufactureDays");
                        hsCode = paiSku.getString("exportSideHsCode");
                        salesSideHsCode = paiSku.getString("salesSideHsCode");
                        ddpPrice = paiSku.getString("ddpUnitPrice");

                        for(Document shiSku : p2mShippingSku){
                            if (x.equals(shiSku.getString("skuCode"))){
                                productDimensionL = shiSku.getString("netLength");
                                productDimensionW = shiSku.getString("netWidth");
                                productDimensionH = shiSku.getString("netHeight");
                                productWeight = shiSku.getString("netWeight");
                                packageDimensionL = shiSku.getString("includePackageLength");
                                packageDimensionW = shiSku.getString("includePackageWidth");
                                packageDimensionH = shiSku.getString("includePackageHeight");
                                packageWeight = shiSku.getString("includePackageWeight");
                                fcaPrice = shiSku.getString("fcaPrice");

                                GsProductDataImpl gsProductData = new GsProductDataImpl(baseProductCode,sku,gtin,productName,productCategory,supplierCompany,brandEng,countryOfOrigin,hsCode,manufacturerLeadTime,fcaPrice,productDimensionL,productDimensionW,productDimensionH,productDimensionUnit,productMaterialPercent,productWeight,productWeightUnit,Country,salesPrice,note,packageDimensionL,packageDimensionW,packageDimensionH,packageDimensionUnit,packageWeight,packageWeightUnit,quantityPerCarton,cartonDimensionL,cartonDimensionW,cartonDimensionH,cartonDimensionUnit,cartonWeight,cartonWeightUnit,importDutyRate,salesSideHsCode,unfulFillableInventoryRemovalSettings,ddpPrice);
                                if (selectedPlatform.equals("Amazon.com")) {
                                    gpd.insertUs(gsProductData);
                                } else if (selectedPlatform.equals("TrueToSource")) {
                                    gpd.insertUs(gsProductData);
                                } else if (selectedPlatform.equals("Amazon.co.uk")) {
                                    gpd.insertUk(gsProductData);
                                } else if (selectedPlatform.equals("Amazon.ca")) {
                                    gpd.insertCa(gsProductData);
                                } else if (selectedPlatform.equals("Amazon.de")) {
                                    gpd.insertDe(gsProductData);
                                } else if (selectedPlatform.equals("Amazon.fr")) {
                                    gpd.insertFr(gsProductData);
                                } else if (selectedPlatform.equals("Amazon.it")) {
                                    gpd.insertIt(gsProductData);
                                } else if (selectedPlatform.equals("Amazon.es")) {
                                    gpd.insertEs(gsProductData);
                                }

                                System.out.println("DONE");
                            }
                        }



                    }
                }
            }}}
            }catch(Exception ex) {
                ex.printStackTrace();
                //Because this bpId from postgresql not from mongodb
            }
        }
    }
}