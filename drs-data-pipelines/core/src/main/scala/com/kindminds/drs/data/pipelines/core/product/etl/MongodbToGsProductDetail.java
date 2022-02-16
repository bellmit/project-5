package com.kindminds.drs.data.pipelines.core.product.etl;

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.GsProductDetailDao;
import com.kindminds.drs.data.pipelines.core.BizCoreCtx;
import com.kindminds.drs.persist.cqrs.store.event.dao.productV2.GsProductDetailDaoImpl;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.*;
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao;
import com.sun.jersey.server.impl.cdi.Utils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class MongodbToGsProductDetail {


   public static void main(String[] args) {
      final AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();
      //ean, code_by_drs, marketplace_sku, marketplace_id, name_by_drs, contain_lithium_ion_battery
      P2MApplicationDao p2mDao = new P2MApplicationDao();

      ProductDao productDao = new ProductDao();

      P2MProductDaoImpl p2MProductDao = new P2MProductDaoImpl();

      List<Document> p2m = p2mDao.findP2mApplicationList();

      GsProductDetailDao gpd = springCtx.getBean(GsProductDetailDao.class);

      for(Document doc : p2m) {
         String id = doc.get("_id").toString();

         String supplierId = doc.getString("supplierId");

         String codeByDrs = "";

         String nameByDrs = "";

         String bpId = doc.getString("bpId");

         System.out.println(bpId);

         String selectedPlatform = doc.getString("selectedPlatform");

         String ean = "";

         int marketplaceId = 0;

         String marketplaceSku = "";

         Document pai = Document.parse(p2MProductDao.findByP2MApplicationId(id));

         String isBattery = pai.getString("isBattery");

         boolean containLithiumIonBattery = false;
         if(isBattery!= null){
         if (isBattery.equals("no")){
            containLithiumIonBattery = false;
         } else {
            containLithiumIonBattery = true;
         }}

         if (selectedPlatform.equals("Amazon.com")) {
            marketplaceId = 1;
         } else if (selectedPlatform.equals("TrueToSource")) {
            marketplaceId = 2;
         } else if (selectedPlatform.equals("Amazon.co.uk")) {
            marketplaceId = 4;
         } else if (selectedPlatform.equals("Amazon.ca")) {
            marketplaceId = 5;
         } else if (selectedPlatform.equals("Amazon.de")) {
            marketplaceId = 6;
         } else if (selectedPlatform.equals("Amazon.fr")) {
            marketplaceId = 7;
         } else if (selectedPlatform.equals("Amazon.it")) {
            marketplaceId = 8;
         } else if (selectedPlatform.equals("Amazon.es")) {
            marketplaceId = 9;
         }

         List<String> appliedSkuCode = (List<String>) doc.get("appliedSkuCode");
         try {
         Document product = productDao.findById(bpId);

         List<Document> productSku = (List<Document>) product.get("skus");

         for(String x : appliedSkuCode){
            for(Document sku : productSku){
               if (x.equals(sku.getString("sellerSku"))){
                  Document productId = (Document) sku.get("productId");
                  ean = productId.getString("value");
                  marketplaceSku = supplierId + sku.getString("sellerSku");
                  codeByDrs = supplierId + sku.getString("sellerSku");
                  nameByDrs = sku.getString("sellerSku");

                  gpd.save(ean,codeByDrs,marketplaceSku,marketplaceId,nameByDrs,containLithiumIonBattery);
                  System.out.println("DONE");
               }
            }
         }
         }catch(Exception ex) {
            ex.printStackTrace();
            //Because this bpId from postgresql not from mongodb
         }
      }
   }
}
