package com.kindminds.drs.data.pipelines.core.product.etl;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductViewQueries;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.ProductRepo;
import com.kindminds.drs.core.biz.repo.product.ProductRepoImpl;
import com.kindminds.drs.data.pipelines.core.BizCoreCtx;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.*;
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao;
import org.bson.Document;
import org.codehaus.plexus.util.ExceptionUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class NewManageProductAndP2mApplicationToMongoEtl {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();

        P2MApplicationDao p2mDao = new P2MApplicationDao();

        String kcode = "K637";

        ProductDao productDao = new ProductDao();

        P2MProductDaoImpl p2MProductInfoDao = new P2MProductDaoImpl();

        RegionalDao regionalDao = new RegionalDao();

        ShippingDaoImpl shippingDao = new ShippingDaoImpl();

        MarketplaceInfoDaoImpl marketplaceInfoDao = new MarketplaceInfoDaoImpl();

        InsuranceDaoImpl insuranceDao = new InsuranceDaoImpl();

        CompanyDao companyDao = springCtx.getBean(CompanyDao.class);

        ProductViewQueries productViewQueries = springCtx.getBean(ProductViewQueries.class);

        com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao pd = springCtx.getBean(com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao.class);

        List<String> allCompanyKcodeList = companyDao.queryAllCompanyKcodeList();

//        for(int a = 0; a <allCompanyKcodeList.size(); a++) {
//
//            String kcode = allCompanyKcodeList.get(a);

        List<ProductDto> getAllBaseProducts = productViewQueries.getBaseProductOnboardingList(1, 1000, kcode);

        System.out.println("---------Total number of BaseProducts = " + getAllBaseProducts.size() + "---------");

        ProductRepo repo = new ProductRepoImpl();

        String saveProductJson = "";

        OffsetDateTime[] perTimeOfCountry = new OffsetDateTime[100];

        String[] perDataOfCountry = new String[100];
        String[] perProductSKU = new String[100];
        String[] perProductNetLength = new String[100];
        String[] perProductNetWidth = new String[100];
        String[] perProductNetHeight = new String[100];
        String[] perProductNetWeight = new String[100];
        String[] perMarketplaceInfoSKU = new String[100];
        String[] perInsuranceSKU = new String[100];
        String[] perProductAdvancedInfoSku = new String[100];
        String[] perShippingSKU = new String[100];
        String[] perCountryShippingSKU = new String[100];
        String[] perCountryProductAdvancedInfoSku = new String[100];
        String[] perSKUCountry = new String[100];
        String[] perCountryNetLength = new String[100];
        String[] perCountryNetWidth = new String[100];
        String[] perCountryNetHeight = new String[100];
        String[] perCountryNetWeight = new String[100];
        String[] perCountryIncludePackageLength = new String[100];
        String[] perCountryIncludePackageWidth = new String[100];
        String[] perCountryIncludePackageHeight = new String[100];
        String[] perCountryIncludePackageWeight = new String[100];

        for (int a = 0; a < getAllBaseProducts.size(); a++) {

            ProductDto productDto = getAllBaseProducts.get(a);
            String baseCode = productDto.getProductBaseCode();

            try {

                Optional<String> result = pd.getId(baseCode, Country.CORE);

                String product_Id = result.get();

                Product p = repo.findById(product_Id).get();
                long createdDateTime = p.getCreateTime().toInstant().toEpochMilli();
                String[][] skuOfMarketSide = new String[10000][10000];
                int skuOfMarketSideIndex = 0;
                List<Product> allMarketSide = repo.findAllMarketSide(product_Id);

                for (int k = 1; k < allMarketSide.size(); k++) {
                    for (int d = 0; d < allMarketSide.get(k).getProductVariations().size(); d++) {
                        String marketside = "";
                        ProductVariation productSku = allMarketSide.get(k).getProductVariations().get(d);

                        if (productSku.getMarketside().toString() == "US") {
                            marketside = "Amazon.com";
                        } else if (productSku.getMarketside().toString() == "UK") {
                            marketside = "Amazon.co.uk";
                        } else if (productSku.getMarketside().toString() == "CA") {
                            marketside = "Amazon.ca";
                        } else if (productSku.getMarketside().toString() == "DE") {
                            marketside = "Amazon.de";
                        } else if (productSku.getMarketside().toString() == "FR") {
                            marketside = "Amazon.fr";
                        } else if (productSku.getMarketside().toString() == "IT") {
                            marketside = "Amazon.it";
                        } else if (productSku.getMarketside().toString() == "ES") {
                            marketside = "Amazon.es";
                        }
                        skuOfMarketSide[skuOfMarketSideIndex][0] = productSku.getVariationCode();
                        skuOfMarketSide[skuOfMarketSideIndex][1] = marketside;
                        skuOfMarketSideIndex += 1;
                    }
                }

                /*  To deal with the strange format in the JSON of some products
                    Example: "{\"productNameEnglish\":\"IAUE Carbide Square End Mill for Aluminum application, 1/8\" 3/16\" 1/4\" 5/16\" 3/8\" 1/2\" 5/8\"\"}";
                */
                Document docP = Document.parse(p.getData());

                System.out.println("AA");
                System.out.println(p.getData());
                System.out.println("BB");

                String data = p.getData().replace("\\","");
                data = data.replace(":\"[{",":[{");
                data = data.replace("}]\"","}]");
                data = data.replace(":\"[",":[");
                data = data.replace("]\"","]");
                data = data.replace("\"Fabric:","Fabric:");
                data = data.replace("(PP)\"","(PP)");

                System.out.println(data);

                String[] splitData = new String[100];
                splitData = data.split(",\"products\"");
                String productData = "{" + "\"products\"" + splitData[1];
                Document doc = Document.parse(productData);

                String category = docP.getString("proposalProductCategory");
                String brandNameCH = docP.getString("brand");
                String brandNameEN = docP.getString("brandEng");
                String productNameCH = docP.getString("productNameLocal");
                String productNameEN = docP.getString("productNameEnglish").replace("\"","");
                String variationType1 = docP.getString("variationType1");
                String variationType2 = docP.getString("variationType2");
                String variationTheme = "";
                String multiTheme = "";

                if (!"".equals(variationType1) && variationType2.equals("")) {
                    variationTheme = variationType1;
                } else if (!"".equals(variationType1) && !"".equals(variationType2)) {
                    variationTheme = variationType1 + "&" + variationType2;
                } else if (variationType1.equals("")) {
                    variationTheme = "";
                }

                List<Object> p2mAppliedSkuData = (List<Object>)doc.get("products");

                System.out.println("ProductBaseCode : " + productDto.getProductBaseCode());
                System.out.println("ProductId : " + product_Id);
                System.out.println("Total number of SKUs = " + p2mAppliedSkuData.size());
                System.out.println("********************************************");

                String sellerSku = "";
                String productId = "";
                String productIdType = "";
                String variableValue1 = "";
                String variableValue2 = "";
                String fcaPrice = "";
                String variable = "";
                String allVariableValue1 = "";
                String allVariableValue2 = "";
                String manageProductSku = "";
                String allSku = "";
                System.out.println("Total number of SKUs = " + p2mAppliedSkuData.size());

                String netLength = "0";
                String netWidth = "0";
                String netHeight = "0";
                String netWeight = "0";

                String bpSku = "";
                String marketplaceInfoSKU = "";
                String insuranceSKU = "";
                String productAdvancedInfoSku = "";
                String shippingSku = "";

                int perProductSKUIndex = 0;


                for(Object x :p2mAppliedSkuData){
                    Document sku = (Document) x;
                    String selling = "";
                    String allSelling = "";
                    sellerSku = sku.getString("SKU");
                    productId = sku.getString("GTINValue");
                    productIdType = sku.getString("GTINType");
                    variableValue1 = sku.getString("type1Value");
                    variableValue2 = sku.getString("type2Value");
                    fcaPrice = sku.getString("FCAPrice");

                    netLength = sku.getString("packageDimension1");
                    netWidth = sku.getString("packageDimension2");
                    netHeight = sku.getString("packageDimension3");
                    netWeight = sku.getString("packageWeight");

                    if (!"".equals(variableValue1)) {
                        allVariableValue1 = allVariableValue1 + variableValue1 + ",";
                    }
                    if (!"".equals(variableValue2)) {
                        allVariableValue2 = allVariableValue2 + variableValue2 + ",";
                    }

                    if (!"".equals(variableValue1) && variableValue2.equals("")) {
                        variable = variableValue1;
                    } else if (!"".equals(variableValue1) && !"".equals(variableValue2)) {
                        variable = variableValue1 + "&" + variableValue2;
                    } else if (variableValue1.equals("")) {
                        variable = "";
                    }

                    for (int w = 0; w < skuOfMarketSideIndex; w++) {
                        if (sellerSku.equals(skuOfMarketSide[w][0])) {

                            selling = "\"" + skuOfMarketSide[w][1] + "\",";

                            allSelling = allSelling + selling;
                        }
                    }

                    if (allSelling.length() > 0) {
                        allSelling = allSelling.substring(0, allSelling.length() - 1);
                    }

                    manageProductSku = "{ \"actions\": [\"申請銷售\",\"修改\"], \"sellerSku\": \"" + sellerSku + "\", " +
                            "\"productId\": {\"name\": \"Product ID\", \"value\": \"\"}, " +
                            "\"noIdProvide\": true, " +
                            "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"" + productIdType + "\"}, " +
                            "\"variable\": {\"name\": \"Variable\", \"value\": \"" + variable + "\"}, " +
                            "\"variationTheme\": {\"name\": \"variationTheme\", \"value\": \"" + variationTheme + "\"}, " +
                            "\"pageIndex\": 1,\"retailPrice\": \"\", \"fbaQuantity\": 0, " +
                            "\"salesVolume\":0, \"openPosition\":0, \"applying\": [], \"selling\": [" + allSelling + "], " +
                            "\"editable\": false, " +
                            "\"status\": \"applied\"," +
                            "\"productIdByDrs\": \"" + productId + "\"" +
                            " },";

                    allSku = allSku + manageProductSku;

                    bpSku = "{ \"sellerSku\": \"" + sellerSku + "\", " +
                            "\"productId\": {\"name\": \"Product ID\", \"value\": \"" + productId + "\"}, " +
                            "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"" + productIdType + "\"}, " +
                            "\"variable\": {\"name\": \"Variable\", \"value\": \"" + variable + "\"}, " +
                            "\"variationTheme\": {\"name\": \"variationTheme\", \"value\": \"" + variationTheme + "\"}, " +
                            "\"pageIndex\": 1,\"retailPrice\": \"\"," +
                            "\"salesVolume\": 0,\"openPosition\": 0,\"fbaQuantity\": 0, " +
                            "\"status\": \"applied\",\"actions\": [\"申請銷售\",\"修改\"]," +
                            "\"editable\": true, ";

                    marketplaceInfoSKU = bpSku + "\"skuCode\": \"" + sellerSku + "\", " +
                            "\"variationNameForMarketplace\" : \"\", " +
                            "\"img\" : [],\"title\" : \"\",\"description\" : \"\", " +
                            "\"feature\" : [ \n" +
                            "       \"\", \n" +
                            "       \"\", \n" +
                            "       \"\", \n" +
                            "       \"\", \n" +
                            "       \"\"\n" +
                            "       ],\n" +
                            "\"keyword\" : [ \n" +
                            "       \"\", \n" +
                            "       \"\", \n" +
                            "       \"\", \n" +
                            "       \"\", \n" +
                            "       \"\"\n" +
                            "       ],\n" +
                            "\"comment\" : {\n" +
                            "       \"variationNameForMarketplace\" : \"\",\n" +
                            "       \"img\" : \"\",\n" +
                            "       \"title\" : \"\",\n" +
                            "       \"description\" : \"\",\n" +
                            "       \"feature\" : \"\",\n" +
                            "       \"keyword\" : \"\"\n" +
                            "       }\n" +
                            "},";

                    insuranceSKU = "{\"skuCode\" : \"" + sellerSku + "\", " +
                            "\"supplierInsuranceFile\" : [{\n" +
                            "       \"name\" : \"\",\n" +
                            "       \"insuredProductName\" : \"\",\n" +
                            "       \"insuredRegions\" : [],\n" +
                            "       \"insuredValidDate\" : \"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()) + "\",\n" +
                            "       \"agreement\" : \"\"\n" +
                            "       }]\n" +
                            "},";

                    productAdvancedInfoSku = bpSku + "\"skuCode\": \"" + sellerSku + "\",\n" +
                            "\"url\": \"\",\n" +
                            "\"startDate\": \"\",\n" +
                            "\"manufactureDays\": \"\",\n" +
                            "\"manufacturePlace\": \"\",\n" +
                            "\"modelNumber\": \"\",\n" +
                            "\"packageFile\": [],\n" +
                            "\"packageImg\": [],\n" +
                            "\"manualFile\": [],\n" +
                            "\"manualImg\": [],\n" +
                            "\"exportSideHsCode\": \"\",\n" +
                            "\"ingredient\": \"\",\n" +
                            "\"woodenFile\": [],\n" +
                            "\"wirelessFile\": [\"Choose a file\"],\n" +
                            "\"batteryFile\": [\"Choose a file\"],\n";

                    shippingSku = "{\"skuCode\": \"" + sellerSku + "\",\n" +
                            " \"fcaPrice\" : \"" + fcaPrice + "\",\n" +
                            " \"netLength\" :\"" + netLength + "\",\n" +
                            " \"netWidth\" : \""+ netWidth + "\",\n" +
                            " \"netHeight\" :\"" + netHeight + "\",\n" +
                            " \"netWeight\" :\"" + netWeight + "\",\n";

                    perProductSKU[perProductSKUIndex] = sellerSku;
                    perProductNetLength[perProductSKUIndex] = netLength;
                    perProductNetWidth[perProductSKUIndex] = netWidth;
                    perProductNetHeight[perProductSKUIndex] = netHeight;
                    perProductNetWeight[perProductSKUIndex] = netWeight;

                    perMarketplaceInfoSKU[perProductSKUIndex] = marketplaceInfoSKU;
                    perInsuranceSKU[perProductSKUIndex] = insuranceSKU;
                    perProductAdvancedInfoSku[perProductSKUIndex] = productAdvancedInfoSku;
                    perShippingSKU[perProductSKUIndex] = shippingSku;
                    perProductSKUIndex += 1;

                }

                // Process ManageProduct

                System.out.println("********************************************");

                allSku = allSku.substring(0, allSku.length() - 1);
                String index1 = "";
                String index2 = "";
                String variables1 = "";
                String variables2 = "";
                String allVariables1 = "";
                String allVariables2 = "";

                if (!"".equals(allVariableValue1)) {
                    allVariableValue1 = allVariableValue1.substring(0, allVariableValue1.length() - 1);

                    String[] allVariableValue1List = allVariableValue1.split(",");

                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < allVariableValue1List.length; i++) {
                        if (!list.contains(allVariableValue1List[i])) {
                            list.add(allVariableValue1List[i]);
                        }
                    }
                    String[] newStr = list.toArray(new String[1]);

                    for (int i = 0; i < newStr.length; i++) {
                        for (int q = 0; q < allVariableValue1List.length; q++) {
                            if (newStr[i].equals(allVariableValue1List[q])) {
                                index1 = index1 + q + ",";
                            }
                        }
                        index1 = index1.substring(0, index1.length() - 1);

                        variables1 = "{ \"value\": \"" + newStr[i] + "\",\n" +
                                "\"index\": [" + index1 + "]\n" +
                                "},";

                        allVariables1 = allVariables1 + variables1;

                        index1 = "";
                    }
                    allVariables1 = allVariables1.substring(0, allVariables1.length() - 1);

                }
                if (!"".equals(allVariableValue2)) {
                    allVariableValue2 = allVariableValue2.substring(0, allVariableValue2.length() - 1);

                    String[] allVariableValue2List = allVariableValue2.split(",");

                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < allVariableValue2List.length; i++) {
                        if (!list.contains(allVariableValue2List[i])) {
                            list.add(allVariableValue2List[i]);
                        }
                    }
                    String[] newStr = list.toArray(new String[1]);

                    for (int i = 0; i < newStr.length; i++) {
                        for (int q = 0; q < allVariableValue2List.length; q++) {
                            if (newStr[i].equals(allVariableValue2List[q])) {
                                index2 = index2 + q + ",";
                            }
                        }
                        index2 = index2.substring(0, index2.length() - 1);

                        variables2 = "{ \"value\": \"" + newStr[i] + "\",\n" +
                                "\"index\": [" + index2 + "]\n" +
                                "},";

                        allVariables2 = allVariables2 + variables2;

                        index2 = "";
                    }
                    allVariables2 = allVariables2.substring(0, allVariables2.length() - 1);

                }

                if (!"".equals(variationType1) && !"".equals(variationType2)) {
                    multiTheme = "{ \"theme\": \"" + variationType1 + "\", " +
                            "\"variables\": [\n" + allVariables1 +
                            "  ]" +
                            " }," +
                            "{ \"theme\": \"" + variationType2 + "\", " +
                            "\"variables\": [\n" + allVariables2 +
                            "  ]" +
                            " }";
                }

                saveProductJson = "{\"supplierId\": \"" + kcode + "\",\n" +
                        "  \"baseCode\": \"" + baseCode + "\",\n" +
                        "  \"category\": \"" + category + "\",\n" +
                        "  \"brandNameCH\": \"" + brandNameCH + "\",\n" +
                        "  \"brandNameEN\": \"" + brandNameEN + "\",\n" +
                        "  \"productNameCH\": \"" + productNameCH + "\",\n" +
                        "  \"productNameEN\": \"" + productNameEN + "\",\n" +
                        "  \"manufacturerCH\": \"" + brandNameEN + "\",\n" +
                        "  \"manufacturerEN\": \"" + brandNameEN + "\",\n" +
                        "  \"variationTheme\": \"" + variationTheme + "\",\n" +
                        "  \"multiTheme\": [" + multiTheme + "],\n" +
                        "  \"totalSkus\": " + p2mAppliedSkuData.size() + ",\n" +
                        "  \"bpStatus\": \"applied\",\n" +
                        "  \"categoryVersion\": 1.0,\n" +
                        "  \"skus\": [" + allSku + "],\n" +
                        "  \"createdDateTime\": " + createdDateTime +
                        "}";

                List<Object> referenceFiles = (List<Object>)doc.get("referenceFiles");
                String perSkuFile = "";
                String otherFile = "";
                String patentFile = "";
                String certificateFile = "";
                String allFileAppliedSku = "";

                // Process Regional File Data
                if (referenceFiles.size() > 0) {
                    for (int m = 0; m < perProductSKUIndex; m++) {
                        String appliedSkuOtherFile = "";
                        String appliedSkuPatentFile = "";
                        String appliedSkuCertificateFile = "";

                        for (Object x : referenceFiles) {
                            Document file = (Document) x;
                            String name = "";
                            String type = "";
                            String description = "";
                            String skuFileDetail = "";
                            String fileDetail = "";
                            String perFileappliedSku = "";
                            String fileAppliedSku = "";

                            if (file.toJson().indexOf(perProductSKU[m]) > 0) {
                                name = file.getString("file");
                                try{
                                    List<String>  fileType = (List<String>)file.get("type");
                                    if (fileType==null) {
                                        type="";
                                    }else {
                                        type=fileType.toString();
                                    }
                                }catch (ClassCastException e){
                                    type="";
                                }

                                description = file.getString("description");

                                if (type.equals("fcc")) {
                                    skuFileDetail = "{\n" +
                                            "\"name\" : \"" + name + "\",\n" +
                                            "\"modelNumber\" : \"\",\n" +
                                            "\"documentType\" : \"\",\n" +
                                            "\"documentValidDate\" : \"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()) + "\",\n" +
                                            "\"certificationBody\" : \"\",\n" +
                                            "\"complianceType\" : \"\"\n" +
                                            "},";
                                    appliedSkuCertificateFile = appliedSkuCertificateFile + skuFileDetail;
                                } else {
                                    skuFileDetail = "{\n" +
                                            "\"name\" : \"" + name + "\",\n" +
                                            "\"description\" : \"" + description + "\"\n" +
                                            "},";
                                    appliedSkuOtherFile = appliedSkuOtherFile + skuFileDetail;
                                }
                            }
                            List<String> appliedSku = (List<String>) file.get("appliedVariationProduct");

                            for (int i = 0; i < appliedSku.size(); i++) {
                                perFileappliedSku = "	{\"value\" : \"" + appliedSku.get(i) + "\",\n" +
                                        "	 \"label\" : \"" + appliedSku.get(i) + "\",\n" +
                                        "	},";
                                fileAppliedSku = fileAppliedSku + perFileappliedSku;
                            }

                            fileAppliedSku = fileAppliedSku.substring(0, fileAppliedSku.length() - 1);

                            if (type.equals("fcc")) {
                                fileDetail = "{\n" +
                                        "\"name\" : \"" + name + "\",\n" +
                                        "\"appliedSku\" : [\n" + fileAppliedSku + "\n],\n" +
                                        "\"modelNumber\" : \"\",\n" +
                                        "\"documentType\" : \"\",\n" +
                                        "\"documentValidDate\" : \"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()) + "\",\n" +
                                        "\"certificationBody\" : \"\",\n" +
                                        "\"complianceType\" : \"\"\n" +
                                        "},";
                                certificateFile = certificateFile + fileDetail;
                            } else {
                                fileDetail = "{\n" +
                                        "\"name\" : \"" + name + "\",\n" +
                                        "\"appliedSku\" : [\n" + fileAppliedSku + "\n],\n" +
                                        "\"description\" : \"" + description + "\"\n" +
                                        "},";
                                otherFile = otherFile + fileDetail;
                            }

                        }
                        if (appliedSkuCertificateFile.length() > 0)
                            appliedSkuCertificateFile = appliedSkuCertificateFile.substring(0, appliedSkuCertificateFile.length() - 1);

                        if (appliedSkuOtherFile.length() > 0)
                            appliedSkuOtherFile = appliedSkuOtherFile.substring(0, appliedSkuOtherFile.length() - 1);

                        if (certificateFile.length() > 0)
                            certificateFile = certificateFile.substring(0, certificateFile.length() - 1);

                        if (otherFile.length() > 0)
                            otherFile = otherFile.substring(0, otherFile.length() - 1);

                        perSkuFile = "{\n" +
                                "            \"skuCode\" : \"" + perProductSKU[m] + "\",\n" +
                                "            \"productImg\" : [],\n" +
                                "   		 \"certificateFile\" : [" + appliedSkuCertificateFile + "],\n" +
                                "   		 \"patentFile\" : [],\n" +
                                "   		 \"otherFile\" : [" + appliedSkuOtherFile + "]\n" +
                                "        },";
                        allFileAppliedSku = allFileAppliedSku + perSkuFile;
                    }
                    if (allFileAppliedSku.length() > 0)
                        allFileAppliedSku = allFileAppliedSku.substring(0, allFileAppliedSku.length() - 1);
                }else{
                    for (int m = 0; m < perProductSKUIndex; m++) {
                        perSkuFile = "{\n" +
                                "            \"skuCode\" : \"" + perProductSKU[m] + "\",\n" +
                                "            \"productImg\" : [],\n" +
                                "   		 \"certificateFile\" : [],\n" +
                                "   		 \"patentFile\" : [],\n" +
                                "   		 \"otherFile\" : []\n" +
                                "        },";
                        allFileAppliedSku = allFileAppliedSku + perSkuFile;
                    }
                    if (allFileAppliedSku.length() > 0)
                        allFileAppliedSku = allFileAppliedSku.substring(0, allFileAppliedSku.length() - 1);
                }

                // Process ProductAdvancedInfo Data
                String isBattery = "no";
                String perAppliedSku = "";
                String batteryAppliedSku = "";
                String batteryCategory = "";
                String wirelessFunction = "";
                String isWireless = "no";

                List<Object> battery = (List<Object>)doc.get("batteries");

                if (battery.size()>0) {
                    isBattery = "yes";

                    for (Object x : battery) {
                        Document batteryFile = (Document) x;
                        List<String> appliedSku = (List<String>) batteryFile.get("appliedVariationProduct");

                        for (int i = 0; i < appliedSku.size(); i++) {
                            perAppliedSku = "{\"value\" : \"" + appliedSku.get(i) + "\",\n" +
                                    "\"label\" : \"" + appliedSku.get(i) + "\",\n" +
                                    "},";
                            batteryAppliedSku = batteryAppliedSku + perAppliedSku;
                        }
                        if (batteryAppliedSku.length()>0)
                            batteryAppliedSku = batteryAppliedSku.substring(0, batteryAppliedSku.length() - 1);

                        batteryCategory = batteryFile.getString("batteryType");
                    }
                }

                List<String>  wirelessTech = (List<String>)doc.get("wirelessTech");

                if (wirelessTech==null) {
                    isWireless = "no";
                }else {
                    if (wirelessTech.toString().equals("[None]")) {
                        isWireless = "no";
                    } else if (wirelessTech.toString().equals("[bluetooth]")){
                        isWireless = "yes";
                        wirelessFunction = "\"bluetooth\"";
                    } else if (wirelessTech.toString().equals("[bluetooth, Wifi]")){
                        isWireless = "yes";
                        wirelessFunction = "\"bluetooth\",\"Wifi\"";
                    }
                }

                // Process P2M Data
                String saveP2mApplicationJson = "";
                String saveProductAdvancedInfoJson = "";
                String saveRegionalJson = "";
                String saveMarketplaceInfoJson = "";
                String saveInsuranceJson = "";
                String saveShippingJson = "";
                int index = 0;

                for (int i = 1; i < 12; i++) {
                    try {
                        Product pc = repo.find(product_Id, Country.fromKey(i)).get();
                        perDataOfCountry[index] = pc.getData();
                        perTimeOfCountry[index] = pc.getCreateTime();
                        index += 1;
                    } catch (NoSuchElementException ex) {
                    }
                }
                String bpId = "";


                try{
                    bpId = productDao.intoMongodb(saveProductJson);
                    System.out.println("Done");
                }catch (Exception e) {
                    e.printStackTrace();
                    saveProductJson = "{\"supplierId\": \"" + kcode + "\",\n" +
                            "  \"product_Id\": \"" + product_Id + "\",\n" +
                            "  \"baseCode\": \"" + baseCode + "\",\n" +
                            "  \"exception_message\": \"" + e.getMessage() + "\",\n" +
                            "  \"exception_stacktrace\": \"" + ExceptionUtils.getStackTrace(e) + "\"\n" +
                            "}";

                    productDao.productExceptionintoMongodb(saveProductJson);
                    System.out.println("Error");
                }

                for (int l = 0; l < index; l++) {

                    long appliedDate = perTimeOfCountry[l].toInstant().toEpochMilli();
                    String countryData = perDataOfCountry[l].replace("\\","");
                    countryData = countryData.replace(":\"[{",":[{");
                    countryData = countryData.replace("}]\"","}]");
                    countryData = countryData.replace(":\"[",":[");
                    countryData = countryData.replace("]\"","]");
                    Document countryDoc = Document.parse(countryData);
                    String selectedCountry = countryDoc.getString("country");
                    String salesSideHsCode = countryDoc.getString("hsCode");
                    String selectedPlatform = "";

                    if (selectedCountry.equals("US")) {
                        selectedPlatform = "Amazon.com";
                    } else if (selectedCountry.equals("UK")) {
                        selectedPlatform = "Amazon.co.uk";
                    } else if (selectedCountry.equals("CA")) {
                        selectedPlatform = "Amazon.ca";
                    } else if (selectedCountry.equals("DE")) {
                        selectedPlatform = "Amazon.de";
                    } else if (selectedCountry.equals("FR")) {
                        selectedPlatform = "Amazon.fr";
                    } else if (selectedCountry.equals("IT")) {
                        selectedPlatform = "Amazon.it";
                    } else if (selectedCountry.equals("ES")) {
                        selectedPlatform = "Amazon.es";
                    }

                    List<Object> perCountrySkuData = (List<Object>)countryDoc.get("products");
                    System.out.println(selectedCountry);
                    System.out.println("Total number of CountrySKUs = " + perCountrySkuData.size());
                    // Process Shipping Data
                    String psAllSkuApplied = "no";
                    String pnwAllSkuApplied = "no";
                    String allNetLength = "0";
                    String allNetWidth = "0";
                    String allNetHeight = "0";
                    String allNetWeight = "0";

                    String includePackageLength = "0";
                    String includePackageWidth = "0";
                    String includePackageHeight = "0";
                    String includePackageWeight = "0";
                    String ppsAllSkuApplied="no";
                    String allIncludePackageLength= "0";
                    String allIncludePackageWidth= "0";
                    String allIncludePackageHeight= "0";
                    String ppwAllSkuApplied="no";
                    String allIncludePackageWeight= "0";

                    String suggestedPricingNoTax = "";
                    String suggestedPricingTax = "";
                    String suggestedRetailPriceNoTax = "";
                    String suggestedRetailPriceTax = "";
                    String ddpUnitPrice = "";

                    String countryShippingSku = "";
                    String countryProductAdvancedInfoSku = "";
                    //---------------------------------------------------------------------------------------------------------
                    String skuOfCountry = "";
                    String appliedSkuCode = "";
                    String appliedMarketplaceInfoSKU = "";
                    String appliedInsuranceSKU = "";
                    String appliedProductAdvancedInfoSKU = "";
                    String appliedShippingSKU = "";
                    int perSKUCountryIndex = 0;
                    int perCountryShippingIndex= 0;

                    for(Object x :perCountrySkuData) {
                        Document countrySku = (Document) x;
                        skuOfCountry = countrySku.getString("SKU");
                        includePackageLength = countrySku.getString("packageDimension1");
                        includePackageWidth = countrySku.getString("packageDimension2");
                        includePackageHeight = countrySku.getString("packageDimension3");
                        includePackageWeight = countrySku.getString("packageWeight");

                        suggestedPricingNoTax = countrySku.getString("SSBP");
                        suggestedPricingTax = countrySku.getString("SSBPtax");
                        suggestedRetailPriceNoTax = countrySku.getString("MSRP");
                        suggestedRetailPriceTax = countrySku.getString("MSRPtax");
                        ddpUnitPrice = countrySku.getString("DDPprice");

                        countryShippingSku = " \"includePackageLength\" :\"" + includePackageLength + "\",\n" +
                                " \"includePackageWidth\" :\"" + includePackageWidth + "\",\n" +
                                " \"includePackageHeight\" :\"" + includePackageHeight + "\",\n" +
                                " \"includePackageWeight\" :\"" + includePackageWeight + "\",\n" +
                                "},";

                        countryProductAdvancedInfoSku = "\"suggestedPricingNoTax\" : \"" + suggestedPricingNoTax + "\",\n" +
                                "\"suggestedPricingTax\" : \"" + suggestedPricingTax + "\",\n" +
                                "\"suggestedRetailPriceNoTax\" : \"" + suggestedRetailPriceNoTax + "\",\n" +
                                "\"suggestedRetailPriceTax\" : \"" + suggestedRetailPriceTax + "\",\n" +
                                "\"salesSideHsCode\": \"" + salesSideHsCode + "\",\n" +
                                "\"ddpUnitPrice\" : \"" + ddpUnitPrice + "\"\n" +
                                "},";

                        perSKUCountry[perSKUCountryIndex] = skuOfCountry;

                        perCountryIncludePackageLength[perSKUCountryIndex] = includePackageLength;
                        perCountryIncludePackageWidth[perSKUCountryIndex] = includePackageWidth;
                        perCountryIncludePackageHeight[perSKUCountryIndex] = includePackageHeight;
                        perCountryIncludePackageWeight[perSKUCountryIndex] = includePackageWeight;

                        perCountryShippingSKU[perSKUCountryIndex] = countryShippingSku;

                        perCountryProductAdvancedInfoSku[perSKUCountryIndex] = countryProductAdvancedInfoSku;

                        perSKUCountryIndex += 1;
                    }

                    for (int m = 0; m < perProductSKUIndex; m++) {
                        for (int n = 0; n < perSKUCountryIndex; n++) {
                            System.out.println(perSKUCountry[n]);
                            if (perProductSKU[m].equals(perSKUCountry[n])) {
                                appliedSkuCode = appliedSkuCode + "\"" + perProductSKU[m] + "\",";
                                appliedMarketplaceInfoSKU = appliedMarketplaceInfoSKU + perMarketplaceInfoSKU[m];
                                appliedInsuranceSKU = appliedInsuranceSKU + perInsuranceSKU[m];
                                appliedProductAdvancedInfoSKU = appliedProductAdvancedInfoSKU + perProductAdvancedInfoSku[m] + perCountryProductAdvancedInfoSku[n];
                                appliedShippingSKU = appliedShippingSKU + perShippingSKU[m] + perCountryShippingSKU[n];
                                perCountryNetLength[perCountryShippingIndex] = perProductNetLength[m];
                                perCountryNetWidth[perCountryShippingIndex] = perProductNetWidth[m];
                                perCountryNetHeight[perCountryShippingIndex] = perProductNetHeight[m];
                                perCountryNetWeight[perCountryShippingIndex] = perProductNetWeight[m];

                                perCountryShippingIndex += 1;
                            }
                        }
                    }

                    for (int j = 0; j < perCountrySkuData.size(); j++) {
                        if(perCountryNetLength[j] == null) {
                            perCountryNetLength[j] = perProductNetLength[0];
                        }
                        if(perCountryNetWidth[j] == null) {
                            perCountryNetWidth[j] = perProductNetWidth[0];
                        }
                        if(perCountryNetHeight[j] == null) {
                            perCountryNetHeight[j] = perProductNetHeight[0];
                        }
                        if(perCountryNetWeight[j] == null) {
                            perCountryNetWeight[j] = perProductNetWeight[0];
                        }
                    }

                    if (perCountrySkuData.size()>1 && p2mAppliedSkuData.size()>=perCountrySkuData.size()){
                        if (allSkuApplied(perCountryNetLength,perCountryNetWidth,perCountryNetHeight) == true){
                            psAllSkuApplied="yes";
                            allNetLength=perCountryNetLength[0];
                            allNetWidth=perCountryNetWidth[0];
                            allNetHeight=perCountryNetHeight[0];
                        }
                        if(allSkuWeightApplied(perCountryNetWeight) == true){
                            pnwAllSkuApplied="yes";
                            allNetWeight=perCountryNetWeight[0];
                        }
                        if (allSkuApplied(perCountryIncludePackageLength,perCountryIncludePackageWidth,perCountryIncludePackageHeight)== true){
                            ppsAllSkuApplied="yes";
                            allIncludePackageLength=perCountryIncludePackageLength[0];
                            allIncludePackageWidth=perCountryIncludePackageWidth[0];
                            allIncludePackageHeight=perCountryIncludePackageHeight[0];
                        }
                        if(allSkuWeightApplied(perCountryIncludePackageWeight) == true){
                            ppwAllSkuApplied="yes";
                            allIncludePackageWeight=perCountryIncludePackageWeight[0];
                        }
                    }else{
                        psAllSkuApplied="yes";
                        pnwAllSkuApplied="yes";
                        ppsAllSkuApplied="yes";
                        ppwAllSkuApplied="yes";
                        allNetLength=perCountryNetLength[0];
                        allNetWidth=perCountryNetWidth[0];
                        allNetHeight=perCountryNetHeight[0];
                        allNetWeight=perCountryNetWeight[0];
                        allIncludePackageLength=perCountryIncludePackageLength[0];
                        allIncludePackageWidth=perCountryIncludePackageWidth[0];
                        allIncludePackageHeight=perCountryIncludePackageHeight[0];
                        allIncludePackageWeight=perCountryIncludePackageWeight[0];
                    }

                    try {
                        appliedSkuCode = appliedSkuCode.substring(0, appliedSkuCode.length() - 1);
                        appliedMarketplaceInfoSKU = appliedMarketplaceInfoSKU.substring(0, appliedMarketplaceInfoSKU.length() - 1);
                        appliedInsuranceSKU = appliedInsuranceSKU.substring(0, appliedInsuranceSKU.length() - 1);
                        appliedShippingSKU = appliedShippingSKU.substring(0,appliedShippingSKU.length() - 1);
                        appliedProductAdvancedInfoSKU = appliedProductAdvancedInfoSKU.substring(0, appliedProductAdvancedInfoSKU.length() - 1);

                        saveMarketplaceInfoJson = "     \"type\":\"MarketPlace Information\",\n" +
                                "     \"main\" : {\n" +
                                "           \"imgUrl\" : \"\",\n" +
                                "           \"secondaryImg\" : []," +
                                "           \"title\" : \"\",\n" +
                                "           \"description\" : \"\",\n" +
                                "           \"feature\" : [ \n" +
                                "                   \"\", \n" +
                                "                   \"\", \n" +
                                "                   \"\", \n" +
                                "                   \"\", \n" +
                                "                   \"\"\n" +
                                "                   ],\n" +
                                "           \"keyword\" : [ \n" +
                                "                   \"\", \n" +
                                "                   \"\", \n" +
                                "                   \"\", \n" +
                                "                   \"\", \n" +
                                "                   \"\"\n" +
                                "                   ],\n" +
                                "           \"comment\" : {\n" +
                                "                   \"img\" : \"\",\n" +
                                "                   \"title\" : \"\",\n" +
                                "                   \"description\" : \"\",\n" +
                                "                   \"feature\" : \"\",\n" +
                                "                   \"keyword\" : \"\"\n" +
                                "                   }\n" +
                                "           }," +
                                "     \"appliedSku\": [" + appliedMarketplaceInfoSKU + "],\n" +
                                "     \"advanced\" : {\n" +
                                "         \"forbiddenWords\" : \"\",\n" +
                                "         \"expectedWeeklySales\" : \"\",\n" +
                                "         \"consumerWarranty\" : \"\",\n" +
                                "         \"useSoftware\" : \"\",\n" +
                                "         \"tradeMarkFile\" : \"\",\n" +
                                "         \"comment\" : {\n" +
                                "             \"forbiddenWords\" : \"\",\n" +
                                "             \"expectedWeeklySales\" : \"\",\n" +
                                "             \"consumerWarranty\" : \"\",\n" +
                                "             \"useSoftware\" : \"\",\n" +
                                "             \"tradeMarkFile\" : \"\"\n" +
                                "         }\n"+
                                "     },\n" +
                                "	  \"competitorInfo\" : \"\",\n" +
                                "	  \"comment\" : {\n" +
                                "     \"competitorInfo\" : \"\"\n" +
                                "     },\n" +
                                "     \"status\" : \"Pending\"";


                        saveInsuranceJson = "   \"type\" : \"Insurance\",\n" +
                                "    \"appliedSku\" : [" + appliedInsuranceSKU + "],\n" +
                                "    \"hasInsured\" : \"no\",\n" +
                                "    \"inquiryForm\" : [{\"name\" : \"Choose a file\"}],\n" +
                                "    \"quotationFromDrs\" : [{\"name\" : \"Choose a file\"}],\n" +
                                "    \"signedQuotation\" : [{\"name\" : \"Choose a file\"}],\n"+
                                "    \"insuranceFile\" : [{\"name\" : \"Choose a file\"," +
                                "                          \"appliedSku\":[]," +
                                "                          \"insuredProductName\":\"\"," +
                                "                          \"insuredRegions\":[]," +
                                "                          \"insuredValidDate\" : \"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()) + "\"," +
                                "                          \"agreeOfAdditionalInsured\" : false," +
                                "                          \"agreeOfAdditionalFee\" : false}]," +
                                "    \"updatedInsuranceFile\" : [{\"name\" : \"Choose a file\"," +
                                "                          \"appliedSku\":[]," +
                                "                          \"insuredProductName\":\"\"," +
                                "                          \"insuredRegions\":[]," +
                                "                          \"insuredValidDate\" : \"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()) + "\"," +
                                "                          \"agreeOfAdditionalInsured\" : false," +
                                "                          \"agreeOfAdditionalFee\" : false}]," +
                                "    \"reviewOfInsurance\" : \"\"," +
                                "    \"caseTypeOfInsure\" : \"\"," +
                                "    \"process\" : \"ph-1\"," +
                                "    \"steps\" : [{\"name\" : \"insurance.step1\",\"state\" : \"active\"}," +
                                "                 {\"name\" : \"insurance.step2\",\"state\" : \"\"}," +
                                "                 {\"name\" : \"insurance.step3\",\"state\" : \"\"}," +
                                "                 {\"name\" : \"insurance.step4\",\"state\" : \"\"}," +
                                "                 {\"name\" : \"insurance.step5\",\"state\" : \"\"}," +
                                "                 {\"name\" : \"insurance.step6\",\"state\" : \"\"}," +
                                "                 {\"name\" : \"insurance.step7\",\"state\" : \"\"}]";

                        saveRegionalJson = "    \"type\" : \"Regional\",\n" +
                                "    \"appliedSku\" : [ \n" + allFileAppliedSku + "],\n" +
                                "    \"otherFile\" : [" + otherFile + "],\n" +
                                "    \"patentFile\" : [],\n" +
                                "    \"certificateFile\" : [" + certificateFile + "],\n" +
                                "    \"comment\" : {\n" +
                                "           \"otherFile\" : \"\",\n" +
                                "           \"patentFile\" : \"\",\n" +
                                "           \"certificateFile\" : \"\",\n" +
                                "           \"productImg\" : \"\"\n" +
                                "    }," +
                                "     \"status\" : \"Pending\"";

                        saveProductAdvancedInfoJson = "		\"type\": \"Product Advanced Information\",\n" +
                                "	\"appliedSku\": [" + appliedProductAdvancedInfoSKU + "],\n" +
                                "	\"ingredient\": \"\",\n" +
                                "	\"urlAllSkuApplied\": \"yes\",\n" +
                                "	\"startDateAllSkuApplied\": \"yes\",\n" +
                                "	\"manufactureDaysAllSkuApplied\": \"yes\",\n" +
                                "	\"manufacturePlaceAllSkuApplied\": \"yes\",\n" +
                                "	\"url\": \"\",\n" +
                                "	\"startDate\": \"2021-01-01T07:39:20.000Z\",\n" +
                                "	\"manufactureDays\": \"\",\n" +
                                "	\"manufacturePlace\": \"\",\n" +
                                "   \"modelNumber\": \"\",\n" +
                                "	\"packageFile\": [{\n" +
                                "		\"name\":\"Choose a file\",\n" +
                                "		\"appliedSku\": []\n" +
                                "	}],\n" +
                                "	\"manualFile\": [{\n" +
                                "		\"name\":\"Choose a file\",\n" +
                                "		\"appliedSku\": []\n" +
                                "	}],\n" +
                                "	\"woodenFile\": [{\n" +
                                "		\"name\":\"Choose a file\",\n" +
                                "		\"appliedSku\": []\n" +
                                "	}],\n" +
                                "	\"wirelessFile\": [{\n" +
                                "		\"name\":\"Choose a file\",\n" +
                                "		\"appliedSku\": []\n" +
                                "	}],\n" +
                                "	\"batteryFile\": [{\n" +
                                "		\"name\":\"Choose a file\",\n" +
                                "		\"appliedSku\": [" + batteryAppliedSku + "],\n" +
                                "		\"batteryCategory\": \"" + batteryCategory + "\"\n" +
                                "	}],\n" +
                                "	\"isWooden\": \"no\",\n" +
                                "	\"isBattery\": \"" + isBattery + "\",\n" +
                                "	\"isWireless\": \"" + isWireless + "\",\n" +
                                "	\"wirelessFunction\": [" + wirelessFunction + "]\n" +
                                "	\"comment\": {\n" +
                                "		\"url\": \"\",\n" +
                                "		\"startDate\": \"\",\n" +
                                "		\"manufactureDays\": \"\",\n" +
                                "		\"manufacturePlace\": \"\",\n" +
                                "       \"modelNumber\": \"\",\n" +
                                "		\"productId\": \"\",\n" +
                                "		\"packageImg\": \"\",\n" +
                                "		\"packageFile\": \"\",\n" +
                                "		\"manualImg\": \"\",\n" +
                                "		\"manualFile\": \"\",\n" +
                                "		\"woodenFile\": \"\",\n" +
                                "		\"wirelessFile\": \"\",\n" +
                                "		\"batteryFile\": \"\",\n" +
                                "		\"hsCode\": \"\",\n" +
                                "		\"ingredient\": \"\"\n" +
                                "	}," +
                                "     \"status\" : \"Pending\"";

                        saveShippingJson = "\"type\" : \"Shipping\" ,\n" +
                                " \"appliedSku\" : [" + appliedShippingSKU + "]\n" +
                                " \"checkReturn\" : true ,\n" +
                                " \"haslocalWarehouse\" : \"yes\",\n" +
                                " \"psAllSkuApplied\" : \"" + psAllSkuApplied +"\",\n" +
                                " \"pnwAllSkuApplied\" : \"" + pnwAllSkuApplied +"\",\n" +
                                " \"ppsAllSkuApplied\" : \"" + ppsAllSkuApplied +"\",\n" +
                                " \"ppwAllSkuApplied\" : \"" + ppwAllSkuApplied +"\",\n" +
                                " \"allIncludePackageLength\" : \"" + allIncludePackageLength +"\",\n" +
                                " \"allIncludePackageWidth\" : \"" + allIncludePackageWidth +"\",\n" +
                                " \"allIncludePackageHeight\" : \"" + allIncludePackageHeight +"\",\n" +
                                " \"allIncludePackageWeight\" : \"" + allIncludePackageWeight +"\",\n" +
                                " \"allNetLength\" : \"" + allNetLength +"\",\n" +
                                " \"allNetWidth\" : \"" + allNetWidth +"\",\n" +
                                " \"allNetHeight\" : \"" + allNetHeight +"\",\n" +
                                " \"allNetWeight\" : \"" + allNetWeight +"\",\n" +
                                " \"returnAddr\" : {\n" +
                                "                \"country\" : \"\",\n" +
                                "                \"city\" : \"\",\n" +
                                "                \"stateorprovince\" : \"\",\n" +
                                "                \"ziporpostalcode\" : \"\",\n" +
                                "                \"address1\" : \"\",\n" +
                                "                \"address2\" : \"\",\n" +
                                "                \"fullname\" : \"\",\n" +
                                "                \"phonenumber\" : \"\",\n" +
                                "                \"emailaddress\" : \"\"\n" +
                                "            },\n" +
                                "\"comment\" : {\n" +
                                "                \"shippingMethod\" : \"\",\n" +
                                "                \"shippingInfo\" : \"\"\n" +
                                "            }," +
                                "\"status\" : \"Pending\"";

                        int serialNum = p2mDao.findMaxSerialNumber(kcode) + 1;
                        String p2mName = getP2MName(kcode, serialNum);

                        saveP2mApplicationJson = "{\"name\": \"" + p2mName + "\",\n" +
                                "  \"type\": \"new\",\n" +
                                "  \"supplierId\": \"" + kcode + "\",\n" +
                                "  \"serial_num\": " + serialNum + ",\n" +
                                "  \"createdDateTime\": " + appliedDate + ", \n" +
                                "  \"appliedDate\": " + appliedDate + ", \n" +
                                "  \"approvedDate\" : " + appliedDate + ", \n" +
                                "  \"rejectedDate\" : \"\", \n" +
                                "  \"selectedProduct\": \"" + productNameEN + "\",\n" +
                                "  \"selectedCountry\": \"" + selectedCountry + "\",\n" +
                                "  \"selectedPlatform\": \"" + selectedPlatform + "\",\n" +
                                "  \"brandNameEN\": \"" + brandNameEN + "\",\n" +
                                "  \"productNameEN\": \"" + productNameEN + "\",\n" +
                                "  \"bpId\": \"" + bpId + "\",\n" +
                                "  \"deprecatedId\": \"" + product_Id + "\",\n" +
                                "  \"appliedSkuCode\": [" + appliedSkuCode + "],\n" +
                                "  \"subApplication\": {\n" +
                                "      \"marketPlaceInfo\":{\n" +
                                "           \"name\": \"" + productNameEN + "-MarketPlace Information\",\n" +
                                saveMarketplaceInfoJson + "\n" +
                                "			},\n" +
                                "      \"insurance\":{\n" +
                                "           \"name\": \"" + productNameEN + "-Insurance\",\n" +
                                saveInsuranceJson + "\n" +
                                "			},\n" +
                                "      \"regional\":{\n" +
                                "           \"name\": \"" + productNameEN + "-Regional\",\n" +
                                saveRegionalJson + "\n" +
                                "			},\n" +
                                "      \"shipping\":{\n" +
                                "			\"name\": \"" + productNameEN + "-Shipping\",\n" +
                                saveShippingJson + "\n" +
                                "			},\n" +
                                "      \"productInfo\":{\n" +
                                "           \"name\": \"" + productNameEN + "-Product Advanced Information\",\n" +
                                saveProductAdvancedInfoJson + "\n" +
                                "			}\n" +
                                "  	   },\n" +
                                "  \"status\": \"Approved\",\n" +
                                "  \"version\": \"1\" \n" +
                                "}";

                        String p2mApplicationId = "";

                        p2mApplicationId = p2mDao.intoMongodb(saveP2mApplicationJson);

                        saveMarketplaceInfoJson = "{\n" +
                                saveMarketplaceInfoJson + ",\n" +
                                "    \"name\": \"" + productNameEN + "-MarketPlace Information\",\n" +
                                "	 \"p2mApplicationId\":{ \"$oid\" : \"" + p2mApplicationId + "\"},\n" +
                                "    \"version\" : 1\n" +
                                "}";

                        saveInsuranceJson = "{\n" +
                                saveInsuranceJson + ",\n" +
                                "    \"name\": \"" + productNameEN + "-Insurance\",\n" +
                                "	 \"p2mApplicationId\":{ \"$oid\" : \"" + p2mApplicationId + "\"},\n" +
                                "    \"version\" : 1\n" +
                                "}";

                        saveShippingJson = "{\n" +
                                saveShippingJson + ",\n" +
                                "    \"name\": \"" + productNameEN + "-Shipping\",\n" +
                                "    \"p2mApplicationId\":{ \"$oid\" : \"" + p2mApplicationId + "\"},\n" +
                                "    \"version\": \"1\"\n" +
                                "}";

                        saveRegionalJson = "{\n" +
                                saveRegionalJson + ",\n" +
                                "    \"name\": \"" + productNameEN + "-Regional\",\n" +
                                "	 \"p2mApplicationId\":{ \"$oid\" : \"" + p2mApplicationId + "\"},\n" +
                                "    \"version\" : 1\n" +
                                "}";

                        saveProductAdvancedInfoJson = "{\n" +
                                saveProductAdvancedInfoJson + ",\n" +
                                "	 \"name\": \"" + productNameEN + "-Product Advanced Information\",\n" +
                                "	 \"p2mApplicationId\":{ \"$oid\" : \"" + p2mApplicationId + "\"},\n" +
                                "    \"version\" : 1\n" +
                                "}";

                        marketplaceInfoDao.intoMongodb(saveMarketplaceInfoJson);

                        insuranceDao.intoMongodb(saveInsuranceJson);

                        shippingDao.intoMongodb(saveShippingJson);

                        p2MProductInfoDao.intoMongodb(saveProductAdvancedInfoJson);

                        regionalDao.intoMongodb(saveRegionalJson);

                        System.out.println(selectedCountry);
                        System.out.println("Done");

                    } catch (Exception e) {
                        e.printStackTrace();

                        saveP2mApplicationJson = "{\"name\": \"\",\n" +
                                "  \"supplierId\": \"" + kcode + "\",\n" +
                                "  \"selectedProduct\": \"" + baseCode + "\",\n" +
                                "  \"selectedCountry\": \"" + selectedCountry + "\",\n" +
                                "  \"selectedPlatform\": \"" + selectedPlatform + "\",\n" +
                                "  \"exception_message\": \"" + e.getMessage() + "\",\n" +
                                "  \"exception_stacktrace\": \"" + ExceptionUtils.getStackTrace(e) + "\"\n" +
                                "}";

                        System.out.println(selectedCountry);
                        System.out.println("Error");

                        p2mDao.intoMongodbException(saveP2mApplicationJson);

                    }

                }
                System.out.println("**************************************");

            }catch(Exception ex) {

                ex.printStackTrace();


                String exJson = "{\"name\": \"\",\n" +
                        "  \"supplierId\": \"" + kcode + "\",\n" +
                        "  \"selectedProduct\": \"" + baseCode + "\",\n" +
                        "  \"exception_message\": \"" + ex.getMessage() + "\",\n" +
                        "  \"exception_stacktrace\": \"" + ExceptionUtils.getStackTrace(ex) + "\"\n" +
                        "}";


                System.out.println("Error");

                System.out.println(exJson);

                p2mDao.intoMongodbException(exJson);
            }

        }

        //      }

    }
    public static String getP2MName(String kcode , int serialNum){

        return "P2M-" + kcode + "-" + serialNum;
    }
    public static  Boolean allSkuApplied(String[] length, String[] width, String[] height){
        Boolean allSkuApplied=true;
        String tempLength = length[0];
        String tempWidth = width[0];
        String tempHeight = height[0];


        for (int i=1;i<width.length;i++){
            if(length[i] != null){
                if (!length[i].equals(tempLength)){
                    allSkuApplied = false;
                    break;
                }
            }

            if(width[i]!=null){
                if (!width[i].equals(tempWidth)){
                    allSkuApplied = false;
                    break;
                }
            }

            if(height[i]!=null){
                if (!height[i].equals(tempHeight)){
                    allSkuApplied = false;
                    break;
                }
            }

        }
        return allSkuApplied;
    }
    public static  Boolean allSkuWeightApplied(String[] weight){
        Boolean allSkuWeightApplied =true;
        String tempWeight = weight[0];
        for (String skuWeight : weight){
            if(skuWeight != null){
                if (!skuWeight.equals(tempWeight)){
                    allSkuWeightApplied = false;
                    break;
                }
            }

        }
        return allSkuWeightApplied;
    }
}