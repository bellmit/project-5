package com.kindminds.drs.product;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.data.access.nosql.mongo.p2m.P2MProductInfoDao;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;
import com.kindminds.drs.core.biz.repo.product.ProductRepoImpl;

import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductViewQueries;
import com.kindminds.drs.api.data.cqrs.store.read.queries.QuoteRequestViewQueries;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.ProductRepo;
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MApplicationDao;
import org.apache.commons.collections4.multiset.SynchronizedMultiSet;
import org.bson.Document;
import org.codehaus.plexus.util.ExceptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestProduct {

	private ProductDao productDao = new ProductDao();

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private QuoteRequestViewQueries quoteRequestViewQueries;

	@Autowired
	private ProductViewQueries productViewQueries;

	@Autowired
	com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao pd;


	private P2MApplicationDao p2MApplicationDao = new P2MApplicationDao();


	private P2MProductInfoDao p2MProductInfoDao;


	@Test
	public void testProductRepo(){

		ProductRepo repo = new ProductRepoImpl();
		List<Product> allMarketSide = repo.findAllMarketSide("ba226621-68d2-4e1c-a266-2168d27e1cf4");

		System.out.println(allMarketSide.size());

		for(int k = 1; k < allMarketSide.size(); k++){
			System.out.println(allMarketSide.get(k).getProductVariations().size());
			for(int d = 0; d < allMarketSide.get(k).getProductVariations().size(); d++){
				ProductVariation productSku = allMarketSide.get(k).getProductVariations().get(d);
				System.out.println("ssss="+productSku.getMarketside()+"ssss="+productSku.getVariationCode());
			}
		}




	}


	@Test
	public void testsupplierId() {

		List<String> allCompanyKcodeList = companyDao.queryAllCompanyKcodeList();

		System.out.println(allCompanyKcodeList);

		ProductRepo repo = new ProductRepoImpl();

		for(int a = 0; a <allCompanyKcodeList.size(); a++) {

			String allCompanyKcode = allCompanyKcodeList.get(a);

			List<ProductDto> getallbaseproducts = productViewQueries.getBaseProductOnboardingList(1,1000,allCompanyKcode);

			System.out.println(allCompanyKcode);

			System.out.println(getallbaseproducts.size());

			for(int b = 0; b <getallbaseproducts.size(); b++) {

				ProductDto productDto = getallbaseproducts.get(b);

				System.out.println(productDto.getProductBaseCode());

				Optional<String> result = pd.getId(productDto.getProductBaseCode(), Country.CORE);

				String product_Id = result.get();

				Product p = repo.findById(product_Id).get();

				System.out.println(p.getData());

				String coreProductInformation = p.getData();

				String kcode = p.getSupplierKcode();

				coreProductInformation = coreProductInformation.substring(1,coreProductInformation.length()-1);

				String[] separateData = coreProductInformation.split("\"products\":\"");

				String[] productdata = separateData[0].split(",");

				String[][] productList = new String[productdata.length][];

				for (int i=0; i < productdata.length; i++) {
//					System.out.println(productdata[i]);
					productList[i] = productdata[i].split(":");
				}
			}

			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
		}


	}

	@Test
	public void testProductIntoMongodb(){

		List<ProductDto> getallbaseproducts = productViewQueries.getBaseProductOnboardingList(1,1000,"K612");

		System.out.println("---------Total number of baseproducts = " + getallbaseproducts.size() + "---------");

		ProductRepo repo = new ProductRepoImpl();

		String saveBPjson = "";


		for (int a = 0; a < getallbaseproducts.size(); a++) {

			ProductDto productDto = getallbaseproducts.get(a);

			Optional<String> result = pd.getId(productDto.getProductBaseCode(), Country.CORE);

			String product_Id = result.get();

			Product p = repo.findById(product_Id).get();

			String kcode = p.getSupplierKcode();

			String[][] skuOfmarketSide = new String[10000][10000];

			int index = 0;

			List<Product> allMarketSide = repo.findAllMarketSide(product_Id);

			for(int k = 1; k < allMarketSide.size(); k++){
				System.out.println(allMarketSide.get(k).getProductVariations().size());
				for(int d = 0; d < allMarketSide.get(k).getProductVariations().size(); d++){
					String marketside = "";
					ProductVariation productSku = allMarketSide.get(k).getProductVariations().get(d);
					System.out.println("ssss=" + productSku.getMarketside() + "ssss="+productSku.getVariationCode());

					if(productSku.getMarketside().toString()=="US"){
						marketside = "Amazon.com";
					}else if(productSku.getMarketside().toString()=="UK"){
						marketside = "Amazon.co.uk";
					}else if(productSku.getMarketside().toString()=="CA"){
						marketside = "Amazon.ca";
					}else if(productSku.getMarketside().toString()=="DE"){
						marketside = "Amazon.de";
					}else if(productSku.getMarketside().toString()=="FR"){
						marketside = "Amazon.fr";
					}else if(productSku.getMarketside().toString()=="IT"){
						marketside = "Amazon.it";
					}else if(productSku.getMarketside().toString()=="ES"){
						marketside = "Amazon.es";
					}

					skuOfmarketSide[index][0] = productSku.getVariationCode();
					skuOfmarketSide[index][1] = marketside;
					index += 1;
				}
			}




			String[] productOrSku = getProductDataAndSkuData(p.getData());

			String productData = productOrSku[0];

			String[] productdata = productData.split(",");

			String[][] productList = new String[productdata.length][];

			Integer supplierId = companyDao.queryIdFromKcode(kcode);
			String category = "";
			String brandNameCH = "";
			String brandNameEN = "";
			String productNameCH = "";
			String productNameEN = "";
			String variationType1 = "";
			String variationType2 = "";
			String variationTheme = "";
			String multiTheme = "";


			try {

				for (int i = 0; i < productdata.length; i++) {

					productList[i] = productdata[i].split(":");
					if (productList[i][0].equals("proposalProductCategory"))
						category = productList[i][1];
					else if (productList[i][0].equals("brand"))
						brandNameCH = productList[i][1];
					else if (productList[i][0].equals("brandEng"))
						brandNameEN = productList[i][1];
					else if (productList[i][0].equals("productNameLocal"))
						productNameCH = productList[i][1];
					else if (productList[i][0].equals("productNameEnglish"))
						productNameEN = productList[i][1];
					else if (productList[i][0].equals("variationType1"))
						if (productList[i].length == 1) {
							variationType1 = "";
						} else {
							variationType1 = productList[i][1];
						}
					else if (productList[i][0].equals("variationType2")) {
						if (productList[i].length == 1) {
							variationType2 = "";
						} else {
							variationType2 = productList[i][1];
						}
					}
				}


				if (!"".equals(variationType1) && variationType2.equals("")) {
					variationTheme = variationType1;
				} else if (!"".equals(variationType1) && !"".equals(variationType2)) {
					variationTheme = variationType1 + "&" + variationType2;
				} else if (variationType1.equals("")) {
					variationTheme = "";
				}

				String skuData = productOrSku[1];

				String[] skudata = skuData.split("},\\{");
				String[][] skuList = new String[skudata.length][];
				String[][] SKU = new String[skuList.length][];
				String[][] gtinValue = new String[skuList.length][];
				String[][] gtinType = new String[skuList.length][];
				String[][] type1value = new String[skuList.length][];
				String[][] type2value = new String[skuList.length][];
				String[][] fcaPrice = new String[skuList.length][];

				for (int i = 0; i < skudata.length; i++)
					skuList[i] = skudata[i].split(",");

				System.out.println("Total number of SKUs = " + skuList.length);

				System.out.println("********************************************");

				String sellerSku = "";
				String productId = "";
				String productIdType = "";
				String retailPrice = "";
				String variablevalue1 = "";
				String variablevalue2 = "";
				String variable = "";

				String allvariablevalue1 = "";
				String allvariablevalue2 = "";

				String sku = "";

				String allsku = "";



				for (int i = 0; i < skuList.length; i++) {

					SKU[i] = skuList[i][8].split(":");
					gtinValue[i] = skuList[i][6].split(":");
					gtinType[i] = skuList[i][4].split(":");
					type1value[i] = skuList[i][1].split(":");
					type2value[i] = skuList[i][3].split(":");
					fcaPrice[i] = skuList[i][skuList[i].length - 8].split(":");
					String selling = "";

					String allselling = "";

					if (SKU[i].length == 1) {
						sellerSku = "";
					} else {
						sellerSku = SKU[i][1];
					}

					if (gtinValue[i].length == 1) {
						productId = "";
					} else {
						productId = gtinValue[i][1];
					}

					if (gtinType[i].length == 1) {
						productIdType = "";
					} else {
						productIdType = gtinType[i][1];
					}

					if (fcaPrice[i].length == 1) {
						retailPrice = "";
					} else {
						retailPrice = fcaPrice[i][1];
					}


					if (type1value[i].length == 1) {
						variablevalue1 = "";
					} else {
						variablevalue1 = type1value[i][1];
						allvariablevalue1 = allvariablevalue1 + variablevalue1 + ",";
					}

					if (type2value[i].length == 1) {
						variablevalue2 = "";
					} else {
						variablevalue2 = type2value[i][1];
						allvariablevalue2 = allvariablevalue2 + variablevalue2 + ",";
					}

					if (!"".equals(variablevalue1) && variablevalue2.equals("")) {
						variable = variablevalue1;
					} else if (!"".equals(variablevalue1) && !"".equals(variablevalue2)) {
						variable = variablevalue1 + "&" + variablevalue2;
					} else if (variablevalue1.equals("")) {
						variable = "";
					}

					for(int w = 0; w<index; w++){
						System.out.println(sellerSku);
						System.out.println(skuOfmarketSide[w][0]+skuOfmarketSide[w][1]);
						if(sellerSku.equals(skuOfmarketSide[w][0])){

							selling = "\"" + skuOfmarketSide[w][1] + "\",";

							allselling = allselling + selling;
						}
					}
					allselling = allselling.substring(0,allselling.length()-1);
					System.out.println(allselling);

					sku = "{ \"actions\": [\"申請銷售\",\"修改\"], \"sellerSku\": \"" + sellerSku + "\", " +
							"\"productId\": {\"name\": \"Product ID\", \"value\": \"" + productId + "\"}, " +
							"\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"" + productIdType + "\"}, " +
							"\"variable\": {\"name\": \"Variable\", \"value\": \"" + variable + "\"}, " +
							"\"variationTheme\": {\"name\": \"variationTheme\", \"value\": \"" + variationTheme + "\"}, " +
							"\"pageIndex\": 1,\"retailPrice\": \"" + retailPrice + "\", \"fbaQuantity\": 0, " +
							"\"settlementsPeriodOrder\":0, \"lastSevenDaysOrder\":0, \"applying\": [], \"selling\": [" + allselling + "], " +
							"\"editable\": false, " +
							"\"status\": \"applied\" },";

					allsku = allsku + sku;


				}

				allsku = allsku.substring(0, allsku.length() - 1);

				String index1 = "";

				String index2 = "";

				String variables1 = "";

				String variables2 = "";

				String allvariables1 = "";

				String allvariables2 = "";

				if (!"".equals(allvariablevalue1)) {
					allvariablevalue1 = allvariablevalue1.substring(0, allvariablevalue1.length() - 1);

		//				System.out.println(allvariablevalue1);

					String[] allvariablevalue1list = allvariablevalue1.split(",");
		//				String[] allvariablevalue1list = {"Coal Black","Dark Slate Gray","Paper White","Paper White","Dark Slate Gray"};

					List<String> list = new ArrayList<String>();

					for (int i = 0; i < allvariablevalue1list.length; i++) {
						if (!list.contains(allvariablevalue1list[i])) {
							list.add(allvariablevalue1list[i]);
						}
					}
					String[] newStr = list.toArray(new String[1]);

					for (int i = 0; i < newStr.length; i++) {
		//					System.out.println(newStr[i]);
						for (int q = 0; q < allvariablevalue1list.length; q++) {
							if (newStr[i].equals(allvariablevalue1list[q])) {
								index1 = index1 + q + ",";
							}
						}
						index1 = index1.substring(0, index1.length() - 1);
		//					System.out.println(index1);

						variables1 = "{ \"value\": \"" + newStr[i] + "\",\n" +
								"\"index\": [" + index1 + "]\n" +
								"},";

						allvariables1 = allvariables1 + variables1;

						index1 = "";
					}
					allvariables1 = allvariables1.substring(0, allvariables1.length() - 1);

				}

				if (!"".equals(allvariablevalue2)) {
					allvariablevalue2 = allvariablevalue2.substring(0, allvariablevalue2.length() - 1);

					String[] allvariablevalue2list = allvariablevalue2.split(",");

		//				String[] allvariablevalue2list = {"11111","22222","44444","33333","22222"};

					List<String> list = new ArrayList<String>();
					for (int i = 0; i < allvariablevalue2list.length; i++) {
						if (!list.contains(allvariablevalue2list[i])) {
							list.add(allvariablevalue2list[i]);
						}
					}
					String[] newStr = list.toArray(new String[1]);

					for (int i = 0; i < newStr.length; i++) {
		//					System.out.println(newStr[i]);
						for (int q = 0; q < allvariablevalue2list.length; q++) {
							if (newStr[i].equals(allvariablevalue2list[q])) {
								index2 = index2 + q + ",";
							}
						}
						index2 = index2.substring(0, index2.length() - 1);
		//					System.out.println(index2);

						variables2 = "{ \"value\": \"" + newStr[i] + "\",\n" +
								"\"index\": [" + index2 + "]\n" +
								"},";

						allvariables2 = allvariables2 + variables2;

						index2 = "";
					}
					allvariables2 = allvariables2.substring(0, allvariables2.length() - 1);

				}

		//			System.out.println(allvariables1);
		//
		//			System.out.println(allvariables2);

				if (!"".equals(variationType1) && variationType2.equals("")) {
		//				multiTheme = "{ \"theme\": \""+variationType1+"\", " +
		//						"\"variables\": [\n" + allvariables1 +
		//						"  ]"+
		//						" }";
				} else if (!"".equals(variationType1) && !"".equals(variationType2)) {
					multiTheme = "{ \"theme\": \"" + variationType1 + "\", " +
							"\"variables\": [\n" + allvariables1 +
							"  ]" +
							" }," +
							"{ \"theme\": \"" + variationType2 + "\", " +
							"\"variables\": [\n" + allvariables2 +
							"  ]" +
							" }";
				} else if (variationType1.equals("")) {
				}
				saveBPjson = "{\"supplierId\": \"" + kcode + "\",\n" +
						"  \"category\": \"" + category + "\",\n" +
						"  \"brandNameCH\": \"" + brandNameCH + "\",\n" +
						"  \"brandNameEN\": \"" + brandNameEN + "\",\n" +
						"  \"productNameCH\": \"" + productNameCH + "\",\n" +
						"  \"productNameEN\": \"" + productNameEN + "\",\n" +
						"  \"variationTheme\": \"" + variationTheme + "\",\n" +
						"  \"multiTheme\": [" + multiTheme + "],\n" +
						"  \"totalSkus\": " + skuList.length + ",\n" +
						"  \"pageSize\": \"5\",\n" +
						"  \"bpStatus\": \"applied\",\n" +
						"  \"categoryVersion\": 1.0,\n" +
						"\"skus\": [" + allsku + "]" +
						"}";

				productDao.productintoMongodb(saveBPjson);

			}catch(Exception e) {
				e.printStackTrace();

				saveBPjson = "{\"supplierId\": \"" + kcode + "\",\n" +
						"  \"product_Id\": \"" + product_Id + "\",\n" +
						"  \"exception_message\": \"" + e.getMessage() + "\",\n" +
						"  \"exception_stacktrace\": \"" + ExceptionUtils.getStackTrace(e) + "\"\n" +
						"}";

//				productDao.productintoMongodb(saveBPjson);

				productDao.productExceptionintoMongodb(saveBPjson);

			}
		}

	}

	private String[] getProductDataAndSkuData(String coreProductInformation){

		String[] productOrSku = new String[100];

		String originalPlace = "\",\"originalPlace\":";

		String[] otherData = {};

		coreProductInformation = coreProductInformation.substring(1,coreProductInformation.length() - 1);

		String[] separateData = coreProductInformation.split("\"products\":\"");

		String productData = separateData[0].replace("\\\"", "");

		productOrSku[0] = productData.replace("\"", "");

		if (separateData[1].indexOf(originalPlace) > 0) {
			otherData = separateData[1].split("\",\"originalPlace\":");
		} else {
			otherData = separateData[1].split("\",\"medical\":");
		}

		String skuData = otherData[0].substring(2, otherData[0].length() - 2);

		productOrSku[1] = skuData.replace("\\\"", "");


		return productOrSku;
	}


}
