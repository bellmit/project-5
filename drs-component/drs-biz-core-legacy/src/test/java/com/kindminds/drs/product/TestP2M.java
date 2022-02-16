package com.kindminds.drs.product;

import com.kindminds.drs.Country;
import com.kindminds.drs.core.biz.repo.product.ProductRepoImpl;

import com.kindminds.drs.api.data.access.nosql.mongo.p2m.P2MProductInfoDao;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductViewQueries;
import com.kindminds.drs.api.data.cqrs.store.read.queries.QuoteRequestViewQueries;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MApplicationDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MProductDaoImpl;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.RegionalDao;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.ProductRepo;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.ShippingDaoImpl;
import org.codehaus.plexus.util.ExceptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestP2M {


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

	private P2MProductDaoImpl p2MProductInfoDao = new P2MProductDaoImpl();


	private RegionalDao regionalDao = new RegionalDao();

	private ShippingDaoImpl shippingDao = new ShippingDaoImpl();

	@Test
	public void testp2m() {
		List<ProductDto> getallbaseproducts = productViewQueries.getBaseProductOnboardingList(1,1000,"K612");

		System.out.println("---------Total number of baseproducts = " + getallbaseproducts.size() + "---------");

		ProductRepo repo = new ProductRepoImpl();

		int currentIndex = 1;

		String[] perDataOfCountry = new String[100];

		String[][] perProductSKU = new String[100][100];

		String[] perSKUCountry = new String[100];





		for (int a = 0; a < getallbaseproducts.size(); a++) {

			ProductDto productDto = getallbaseproducts.get(a);

			String basecode = productDto.getProductBaseCode();

			Optional<String> result = pd.getId(basecode, Country.CORE);

			String product_Id = result.get();

			Product p = repo.findById(product_Id).get();

//			String createTime =  OffsetDateTime.ofInstant(p.getCreateTime().toZonedDateTime().toInstant(), ZoneId.of("Asia/Taipei")).toString();
//
//			createTime = createTime.substring(0,10).replace("-","");
//
//			String p2mName = basecode + "-" + createTime;
			String p2mName = getP2MName(p,basecode);

			System.out.println(p2mName);

			String data = p.getData().substring(1, p.getData().length() - 1);

			String kcode = p.getSupplierKcode();

			String[] productorsku = data.split("\"products\":\"");
//
//			String productDataHeading = productorsku[0].replace("\\\"", "");
//
//			productDataHeading = productDataHeading.replace("\"", "");
//
//			String[] bpData = productDataHeading.split(",");
			String[] bpData = getData(productorsku[0]);

			String[][] bpDataList = new String[bpData.length][];


			Integer supplierId = companyDao.queryIdFromKcode(kcode);

			String brandNameEN = "";
			String productNameEN = "";
			String selectedCountry = "";
			String selectedPlatform = "";
			String variationType1 = "";
			String variationType2 = "";
			String variationTheme = "";

			for (int i = 0; i < bpData.length; i++) {
				//				System.out.println(productdata[i]);
				bpDataList[i] = bpData[i].split(":");
				if (bpDataList[i][0].equals("brandEng"))
					brandNameEN = bpDataList[i][1];
				else if (bpDataList[i][0].equals("productNameEnglish"))
					productNameEN = bpDataList[i][1];
				else if (bpDataList[i][0].equals("variationType1"))
					if (bpDataList[i].length == 1) {
						variationType1 = "";
					} else {
						variationType1 = bpDataList[i][1];
					}
				else if (bpDataList[i][0].equals("variationType2")) {
					if (bpDataList[i].length == 1) {
						variationType2 = "";
					} else {
						variationType2 = bpDataList[i][1];
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

			//getSkuData
			String originalPlace = "\",\"originalPlace\":";

			String[] bpOtherData = {};

			if (productorsku[1].indexOf(originalPlace) > 0) {
				bpOtherData = productorsku[1].split("\",\"originalPlace\":");

				bpOtherData[1] = "\"originalPlace\":" + bpOtherData[1];
			} else {
				bpOtherData = productorsku[1].split("\",\"medical\":");

				bpOtherData[1] = "\"medical\":" + bpOtherData[1];
			}

			String p2mAppliedskudata = bpOtherData[0].substring(2, bpOtherData[0].length() - 2);

			p2mAppliedskudata = p2mAppliedskudata.replace("\\\"", "");

			String productAdvancedInfo = bpOtherData[1].replace("\\", "");

			productAdvancedInfo = productAdvancedInfo.replace("\"", "");

//			System.out.println(p2mAppliedskudata);

			String[] skudata = p2mAppliedskudata.split("},\\{");
			String[][] skuList = new String[skudata.length][];
			String[][] SKU = new String[skuList.length][];
			String[][] gtinValue = new String[skuList.length][];
			String[][] gtinType = new String[skuList.length][];
			String[][] type1value = new String[skuList.length][];
			String[][] type2value = new String[skuList.length][];
			String[][] fcaPrice = new String[skuList.length][];

			String batteries = "batteries:";
			String isBattery = "no";
			String[] batteryData = {};
			String[] batteriesFile = {};
			String[] appliedVariationProduct = {};
			String[] batteryappliedSku = {};
			String perappliedSku = "";
			String batteryallappliedSku = "";
			String[] batteriesData = {};
			String[][] batterieslist = {};
			String batteryCategory = "";
			String[] productAdvanceddata = productAdvancedInfo.split(",");
			String wirelessFunction = "";
			String[][] productAdvancedlist = new String[productAdvanceddata.length][];
			String isWireless = "no";

			for (int i = 0; i < skudata.length; i++)
				skuList[i] = skudata[i].split(",");

			System.out.println("ProductBaseCode : " + productDto.getProductBaseCode());

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

			String bpSku = "";


			int perProductSKUIndex = 0;


			for (int i = 0; i < skuList.length; i++) {

				SKU[i] = skuList[i][8].split(":");
				gtinValue[i] = skuList[i][6].split(":");
				gtinType[i] = skuList[i][4].split(":");
				type1value[i] = skuList[i][1].split(":");
				type2value[i] = skuList[i][3].split(":");
				fcaPrice[i] = skuList[i][skuList[i].length - 8].split(":");

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

				bpSku = "{ \"sellerSku\": \"" + sellerSku + "\", " +
						"\"productId\": {\"name\": \"Product ID\", \"value\": \"" + productId + "\"}, " +
						"\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"" + productIdType + "\"}, " +
						"\"variable\": {\"name\": \"Variable\", \"value\": \"" + variable + "\"}, " +
						"\"variationTheme\": {\"name\": \"variationTheme\", \"value\": \"" + variationTheme + "\"}, " +
						"\"pageIndex\": 1,\"retailPrice\": \"" + retailPrice + "\"," +
						"\"settlementsPeriodOrder\": 0,\"lastSevenDaysOrder\": 0,\"fbaQuantity\": 0, " +
						"\"status\": \"applied\",\"actions\": [\"申請銷售\",\"修改\"]," +
						"\"editable\": false" +
						"},";

				perProductSKU[perProductSKUIndex][0] = sellerSku;
				perProductSKU[perProductSKUIndex][1] = bpSku;
				perProductSKUIndex += 1;


			}



			String[] referenceFiles = {};

			String[] fileInfo = {};

			String[] fileData = {};

			String otherFile = "";

			String patentFile = "";

			String certificateFile = "";

			String allFileAppliedSku = "";

			if (productAdvancedInfo.indexOf("referenceFiles:") > 0){

				referenceFiles = productAdvancedInfo.split(",referenceFiles:");

				if(referenceFiles[1].indexOf(",containedMaterial:[none]")>0){
					fileInfo = referenceFiles[1].split(",containedMaterial:");
				}else {
					fileInfo = referenceFiles[1].split(",hazardousMaterials:");
				}

//				System.out.println(productAdvancedInfo);
//				System.out.println(fileInfo[0]);

				if (fileInfo[0].length() > 2) {
					fileInfo[0] = fileInfo[0].substring(2, fileInfo[0].length() - 2);


					fileData =fileInfo[0].split("},\\{");

					String[][] fileList = new String[fileData.length][];
					String[] fileVariationProduct1 = {};
					String[] fileVariationProduct2 = {};
					String[] fileDescription = {};

					String[][] fileName = new String[fileList.length][];
					String[][] fileType = new String[fileList.length][];
					String perSkuFile = "";


					for (int m = 0; m < perProductSKUIndex; m++) {
						String appliedSkuOtherFile = "";

						String appliedSkuPatentFile = "";

						String appliedSkuCertificateFile = "";
						for (int i = 0; i < fileData.length; i++) {
							String name = "";
							String type = "";
							String description = "";
							String fileDetail = "";


							if (fileData[i].indexOf(perProductSKU[m][0])>0) {
								fileList[i] = fileData[i].split(",");
								fileName[i] = fileList[i][0].split(":");
								fileType[i] = fileList[i][2].split(":");
								fileDescription = fileData[i].split("description:");
								if(fileName[i].length>1){
									name = fileName[i][1];
								}

								if(fileType[i].length>1) {
									type = fileType[i][1].substring(1,fileType[i][1].length()-1);
								}

								if(fileDescription.length>1) {
									description = fileDescription[1];
								}
								if(type.equals("fcc")){
									fileDetail = "{\n" +
											"\"name\" : \"" + name + "\",\n" +
											"\"modelNumber\" : \"\",\n" +
											"\"documentType\" : \"\",\n" +
											"\"documentValidDate\" : \"2021-05-05T05:14:28.924Z\",\n" +
											"\"certificationBody\" : \"\",\n" +
											"\"complianceType\" : \"\"\n" +
											"},";
									appliedSkuCertificateFile = appliedSkuCertificateFile + fileDetail;
								}else{
									fileDetail = "{\n" +
											"\"name\" : \"" + name + "\",\n" +
											"\"description\" : \"" + description + "\"\n" +
											"},";
									appliedSkuOtherFile = appliedSkuOtherFile + fileDetail;
								}
							}
						}
						if(appliedSkuCertificateFile.length()>0)
							appliedSkuCertificateFile = appliedSkuCertificateFile.substring(0,appliedSkuCertificateFile.length()-1);

						if(appliedSkuOtherFile.length()>0)
							appliedSkuOtherFile = appliedSkuOtherFile.substring(0,appliedSkuOtherFile.length()-1);

						perSkuFile = "{\n" +
								"            \"skuCode\" : \"" + perProductSKU[m][0] + "\",\n" +
								"            \"productImg\" : [],\n" +
								"   		 \"certificateFile\" : [" + appliedSkuCertificateFile + "],\n" +
								"   		 \"patentFile\" : [],\n" +
								"   		 \"otherFile\" : [" + appliedSkuOtherFile + "]\n" +
								"        },";
						allFileAppliedSku = allFileAppliedSku + perSkuFile;
					}

					allFileAppliedSku = allFileAppliedSku.substring(0,allFileAppliedSku.length()-1);


					for (int i = 0; i < fileData.length; i++) {
						String perfileappliedSku = "";
						String fileAppliedSku = "";
						String name = "";
						String type = "";
						String description = "";
						String fileDetail = "";
						fileList[i] = fileData[i].split(",");
						fileVariationProduct1 = fileData[i].split("appliedVariationProduct:\\[");
						fileVariationProduct2 = fileVariationProduct1[1].split("],applicableRegion");
						String[] appliedSku = fileVariationProduct2[0].split(",");
						fileDescription = fileData[i].split("description:");

						for (int j = 0; j < appliedSku.length; j++) {
							perfileappliedSku = "	{\"value\" : \"" + appliedSku[j] + "\",\n" +
												"	 \"label\" : \"" + appliedSku[j] + "\",\n" +
												"	},";
							fileAppliedSku = fileAppliedSku + perfileappliedSku;
						}
						fileAppliedSku = fileAppliedSku.substring(0, fileAppliedSku.length() - 1);

						fileName[i] = fileList[i][0].split(":");
						fileType[i] = fileList[i][2].split(":");

						if(fileName[i].length>1){
							name = fileName[i][1];
						}

						if(fileType[i].length>1) {
							type = fileType[i][1].substring(1,fileType[i][1].length()-1);
						}

						if(fileDescription.length>1) {
							description = fileDescription[1];
						}

//						System.out.println(name);
//
//						System.out.println(type);
//
//						System.out.println(fileAppliedSku);
//
//						System.out.println(description);

						if(type.equals("fcc")){
							fileDetail = "{\n" +
								"\"name\" : \"" + name + "\",\n" +
								"\"appliedSku\" : [\n" + fileAppliedSku + "\n],\n" +
								"\"modelNumber\" : \"\",\n" +
								"\"documentType\" : \"\",\n" +
								"\"documentValidDate\" : \"2021-05-05T05:14:28.924Z\",\n" +
								"\"certificationBody\" : \"\",\n" +
								"\"complianceType\" : \"\"\n" +
							    "},";
							certificateFile = certificateFile + fileDetail;
						}else{
							fileDetail = "{\n" +
								"\"name\" : \"" + name + "\",\n" +
								"\"appliedSku\" : [\n" + fileAppliedSku + "\n],\n" +
								"\"description\" : \"" + description + "\"\n" +
								"},";
							otherFile = otherFile + fileDetail;
						}

					}
					if(certificateFile.length()>0)
						certificateFile = certificateFile.substring(0,certificateFile.length()-1);

					if(otherFile.length()>0)
						otherFile = otherFile.substring(0,otherFile.length()-1);

				}

			}

//			System.out.println("certificateFile : " + certificateFile);
//
//			System.out.println("otherFile : " + otherFile);


			if (productAdvancedInfo.indexOf(batteries) > 0) {


				batteryData = productAdvancedInfo.split(",batteries:");

				batteriesFile = batteryData[1].split(",note");


				if (batteriesFile[0].length() > 2) {
					batteriesFile[0] = batteriesFile[0].substring(2, batteriesFile[0].length() - 2);
					isBattery = "yes";
				}

				batteriesFile[0] = batteriesFile[0].replace("[", "");

				batteriesFile[0] = batteriesFile[0].replace("]", "");

				appliedVariationProduct = batteriesFile[0].split(",appliedVariationProduct:");

				if (appliedVariationProduct.length > 1) {
					batteryappliedSku = appliedVariationProduct[1].split(",packingWay:");

					String[] appliedSku = batteryappliedSku[0].split(",");

					for (int i = 0; i < appliedSku.length; i++) {
						perappliedSku = "{\"value\" : \"" + appliedSku[i] + "\",\n" +
										"\"label\" : \"" + appliedSku[i] + "\",\n" +
										"},";
						batteryallappliedSku = batteryallappliedSku + perappliedSku;
					}
					batteryallappliedSku = batteryallappliedSku.substring(0, batteryallappliedSku.length() - 1);
				}

				batteriesData = batteriesFile[0].split(",");

				batterieslist = new String[batteriesData.length][];

				for (int i = 0; i < batteriesData.length; i++) {
					batterieslist[i] = batteriesData[i].split(":");
					if (batterieslist[i][0].equals("batteryType")) {
						batteryCategory = batterieslist[i][1];
					}
				}
			}

			if (productAdvancedInfo.indexOf("wirelessTech:[bluetooth]") > 0) {
				wirelessFunction = "\"bluetooth\"";
			} else if (productAdvancedInfo.indexOf("wirelessTech:[bluetooth,Wifi]") > 0) {
				wirelessFunction = "\"bluetooth\",\"Wifi\"";
			}

			for (int i = 0; i < productAdvanceddata.length; i++) {
				//				System.out.println(productdata[i]);
				productAdvancedlist[i] = productAdvanceddata[i].split(":");
				if (productAdvancedlist[i][0].equals("wirelessTech")) {
					isWireless = productAdvancedlist[i][1];
					if (isWireless.equals("[None]")) {
						isWireless = "no";
					} else {
						isWireless = "yes";
					}
				}
			}

			String savep2mApplicationjson = "";

			String saveproductAdvancedInfojson = "";

			String saveregionaljson = "";

			String country = "";

			int index = 0;

			for (int i = 1; i < 12; i++) {
				try {
					Product pc = repo.find(product_Id, Country.fromKey(i)).get();

					perDataOfCountry[index]=pc.getData().substring(1, pc.getData().length() - 1);

					index+=1;

				} catch (NoSuchElementException ex) {
				}
			}


			for (int l = 0; l < index; l++) {

//				System.out.println(perDataOfCountry[l]);

				String countrydata = perDataOfCountry[l];

				String[] splitdata = countrydata.split("\"products\":\"");

				String countryDataHeading = splitdata[0].replace("\\\"", "");

				countryDataHeading = countryDataHeading.replace("\"", "");

				String[] countryBaseData = countryDataHeading.split(",");

				String[][] countryDataList = new String[countryBaseData.length][];


				for (int j = 0; j < countryBaseData.length; j++) {

					countryDataList[j] = countryBaseData[j].split(":");

					if (countryDataList[j][0].equals("country"))
						country = countryDataList[j][1];

					if (country.equals("US")) {
						selectedPlatform = "Amazon.com";
					} else if (country.equals("UK")) {
						selectedPlatform = "Amazon.co.uk";
					} else if (country.equals("CA")) {
						selectedPlatform = "Amazon.ca";
					} else if (country.equals("DE")) {
						selectedPlatform = "Amazon.de";
					} else if (country.equals("FR")) {
						selectedPlatform = "Amazon.fr";
					} else if (country.equals("IT")) {
						selectedPlatform = "Amazon.it";
					} else if (country.equals("ES")) {
						selectedPlatform = "Amazon.es";
					}

				}

				String[] countrySkuData = splitdata[1].split("}]\"");

//				System.out.println(countrySkuData[0]);

				countrySkuData[0] = countrySkuData[0].substring(2);

				countrySkuData[0] = countrySkuData[0].replace("\\\"", "");

//				System.out.println(countrySkuData[0]);

				String[] perCountrySkuData = countrySkuData[0].split("},\\{");

				String[][] perCountrySkuList = new String[perCountrySkuData.length][];

				String[][] perSKU = new String[perCountrySkuList.length][];

				String SkuOfCountry = "";

				String appliedSkuCode = "";

				String appliedSkuData = "";

				int perSKUCountryIndex = 0;

				for (int k = 0; k < perCountrySkuData.length; k++)
					perCountrySkuList[k] = perCountrySkuData[k].split(",");

				for (int i = 0; i < perCountrySkuList.length; i++) {

					perSKU[i] = perCountrySkuList[i][0].split(":");


					if (perSKU[i].length == 1) {
						SkuOfCountry = "";
					} else {
						SkuOfCountry = perSKU[i][1];
					}
					perSKUCountry[perSKUCountryIndex] = SkuOfCountry;

					perSKUCountryIndex += 1;

				}
				String productAdvancedInfoSku = "";

				String productInfoAppliedSku = "";

				for (int m = 0; m < perProductSKUIndex; m++) {
					for (int n = 0; n < perSKUCountryIndex; n++) {
						if (perProductSKU[m][0].equals(perSKUCountry[n])) {
							appliedSkuCode = appliedSkuCode + "\"" + perProductSKU[m][0] + "\",";
							appliedSkuData = appliedSkuData + perProductSKU[m][1];
						}
					}
				}


				for (int n = 0; n < perSKUCountryIndex; n++) {
//					System.out.println(perSKUCountry[n]);
					productAdvancedInfoSku = "{\"skuCode\": \"" + perSKUCountry[n] + "\",\n" +
							"\"url\": \"\",\n" +
							"\"startDate\": \"\",\n" +
							"\"manufactureDays\": \"\",\n" +
							"\"manufacturePlace\": \"\",\n" +
							"\"packageFile\": [],\n" +
							"\"packageImg\": [],\n" +
							"\"manualFile\": [],\n" +
							"\"manualImg\": [],\n" +
							"\"exportSideHsCode\": \"\",\n" +
							"\"salesSideHsCode\": \"\",\n" +
							"\"ingredient\": \"\",\n" +
							"\"woodenFile\": [],\n" +
							"\"wirelessFile\": [\"Choose a file\"],\n" +
							"\"batteryFile\": [\"Choose a file\"],\n" +
							"},";

					productInfoAppliedSku = productInfoAppliedSku + productAdvancedInfoSku;
				}

				productInfoAppliedSku = productInfoAppliedSku.substring(0, productInfoAppliedSku.length() - 1);

//				StringIndexOutOfBoundsException
				try {
					appliedSkuCode = appliedSkuCode.substring(0, appliedSkuCode.length() - 1);
					appliedSkuData = appliedSkuData.substring(0, appliedSkuData.length() - 1);

//					System.out.println(appliedSkuCode);
//					System.out.println(appliedSkuData);

					//todo ralph test shipping
					String p2mShipping =testP2mShipping(basecode,skuList,perCountrySkuList);

					saveregionaljson ="    \"type\" : \"Regional\",\n" +
							"    \"ref_id\" : \"\",\n" +
							"    \"appliedSku\" : [ \n" + allFileAppliedSku + "],\n" +
							"    \"otherFile\" : [" + otherFile + "],\n" +
							"    \"patentFile\" : [],\n" +
							"    \"certificateFile\" : [" + certificateFile + "]";

					saveproductAdvancedInfojson = "		\"type\": \"Product Advanced Information\",\n" +
							"	\"ref_id\": \"\",\n" +
							"	\"appliedSku\": [" + productInfoAppliedSku + "],\n" +
							"	\"urlAllSkuApplied\": \"yes\",\n" +
							"	\"startDateAllSkuApplied\": \"yes\",\n" +
							"	\"manufactureDaysAllSkuApplied\": \"yes\",\n" +
							"	\"manufacturePlaceAllSkuApplied\": \"yes\",\n" +
							"	\"url\": \"\",\n" +
							"	\"startDate\": \"1\",\n" +
							"	\"manufactureDays\": \"\",\n" +
							"	\"manufacturePlace\": \"\",\n" +
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
							"		\"appliedSku\": [" + batteryallappliedSku + "],\n" +
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
							"	}";

					savep2mApplicationjson = "{\"name\": \"" + p2mName + "\",\n" +
							"  \"type\": \"new\",\n" +
							"  \"supplierId\": \"" + kcode + "\",\n" +
							"  \"selectedProduct\": \"" + basecode + "\",\n" +
							"  \"selectedCountry\": \"" + country + "\",\n" +
							"  \"selectedPlatform\": \"" + selectedPlatform + "\",\n" +
							"  \"brandNameEN\": \"" + brandNameEN + "\",\n" +
							"  \"productNameEN\": \"" + productNameEN + "\",\n" +
							"  \"bpId\": \"" + product_Id + "\",\n" +
							"  \"appliedSkuCode\": [" + appliedSkuCode + "],\n" +
							"  \"appliedSku\": [" + appliedSkuData + "],\n" +
							"  \"country\": \"" + country + "\",\n" +
							"  \"platform\": \"" + selectedPlatform + "\",\n" +
							"  \"subApplication\": {\n" +
							"      \"marketPlaceInfo\":{\n" +
							"			\"type\":\"MarketPlace Information\",\n" +
							"           \"name\": \"" + p2mName + "-MarketPlace Information\",\n" +
							"      		\"ref_id\" : \"\",\n" +
							"           \"main\" : {\n" +
							"                \"imgUrl\" : \"\",\n" +
							"                \"title\" : \"\",\n" +
							"                \"description\" : \"\",\n" +
							"                \"feature\" : [ \n" +
							"                    \"\", \n" +
							"                    \"\", \n" +
							"                    \"\", \n" +
							"                    \"\", \n" +
							"                    \"\"\n" +
							"                ],\n" +
							"                \"keyword\" : [ \n" +
							"                    \"\", \n" +
							"                    \"\", \n" +
							"                    \"\", \n" +
							"                    \"\", \n" +
							"                    \"\"\n" +
							"                ],\n" +
							"                \"comment\" : {\n" +
							"                    \"img\" : \"\",\n" +
							"                    \"title\" : \"\",\n" +
							"                    \"description\" : \"\",\n" +
							"                    \"feature\" : \"\",\n" +
							"                    \"keyword\" : \"\"\n" +
							"                }\n" +
							"      		},"+
							"			\"appliedSku\": [" + appliedSkuData + "],\n" +
							"			\"competitorInfo\" : \"\""+
							"			\"comment\" : {\n" +
							"                \"competitorInfo\" : \"\"\n" +
							"            }"+
							"			},\n" +
							"      \"insurance\":{\n" +
							"			\"type\":\"Insurance\",\n" +
							"           \"name\": \"" + p2mName + "-Insurance\",\n" +
							"      		\"ref_id\" : \"\",\n" +
							"			\"appliedSku\": [" + appliedSkuData + "],\n" +
							"			\"hasInsured\" : \"no\","+
							"			\"inquiryForm\" : [ \n" +
							"                			{\n" +
							"                    			\"name\" : \"Choose a file\"\n" +
							"                			}\n" +
							"            			],"+
							"			\"quotationFromDrs\" : [ \n" +
							"                			{\n" +
							"                    			\"name\" : \"Choose a file\"\n" +
							"                			}\n" +
							"            			],"+
							"			\"signedQuotation\" : [ \n" +
							"                			{\n" +
							"                    			\"name\" : \"Choose a file\"\n" +
							"                			}\n" +
							"            			],"+
							"			\"insuranceFile\" : [ \n" +
							"                			{\n" +
							"                    			\"name\" : \"Choose a file\",\n" +
							"                			}\n" +
							"            			],"+
							"			\"updatedInsuranceFile\" : [ \n" +
							"                			{\n" +
							"                    			\"name\" : \"Choose a file\",\n" +
							"                			}\n" +
							"            			],"+
							"			\"reviewOfInsurance\" : \"\"," +
							"			\"caseTypeOfInsure\" : \"\","+
							"			\"process\" : \"ph-1\","+
							"			\"steps\" : [ \n" +
							"                {\n" +
							"                    \"name\" : \"insurance.step1\",\n" +
							"                    \"state\" : \"active\"\n" +
							"                }, \n" +
							"                {\n" +
							"                    \"name\" : \"insurance.step2\",\n" +
							"                    \"state\" : \"\"\n" +
							"                }, \n" +
							"                {\n" +
							"                    \"name\" : \"insurance.step3\",\n" +
							"                    \"state\" : \"\"\n" +
							"                }, \n" +
							"                {\n" +
							"                    \"name\" : \"insurance.step4\",\n" +
							"                    \"state\" : \"\"\n" +
							"                }, \n" +
							"                {\n" +
							"                    \"name\" : \"insurance.step5\",\n" +
							"                    \"state\" : \"\"\n" +
							"                }, \n" +
							"                {\n" +
							"                    \"name\" : \"insurance.step6\",\n" +
							"                    \"state\" : \"\"\n" +
							"                }, \n" +
							"                {\n" +
							"                    \"name\" : \"insurance.step7\",\n" +
							"                    \"state\" : \"\"\n" +
							"                }\n" +
							"            ]"+
							"			},\n" +
							"      \"regional\":{\n" +
							"           \"name\": \"" + p2mName + "-Regional\",\n" +
										saveregionaljson + ",\n" +
							"            \"comment\" : {\n" +
							"                \"otherFile\" : \"\",\n" +
							"                \"patentFile\" : \"\",\n" +
							"                \"certificateFile\" : \"\",\n" +
							"                \"productImg\" : \"\"\n" +
							"            }" +
							"			},\n" +
							"      \"shipping\":{\n" +
							"			\"name\": \"" + p2mName + "-Shipping\",\n" +
										p2mShipping + "\n" +
							"			},\n"+
							"      \"productInfo\":{\n" +
							"           \"name\": \"" + p2mName + "-Product Advanced Information\",\n" +
										saveproductAdvancedInfojson + "\n" +
							"			}\n" +
							"  	   },\n" +
							"  \"status\": \"Approved\",\n" +
							"}";

					String p2mApplicationid = "";

					p2mApplicationid = p2MApplicationDao.intoMongodb(savep2mApplicationjson);

					p2mShipping="{\n" +
							p2mShipping + ",\n"+
							" \"name\": \"" + p2mName + "\",\n" +
							" \"p2mApplicationId\":{ \"$oid\" : \"" + p2mApplicationid + "\"},\n" +
							" \"version\": \"1\"\n" +
							"}";

					saveregionaljson = "{\n" +
							saveregionaljson + ",\n"+
							"	 \"name\": \"" + p2mName + "\",\n" +
							"	 \"p2mApplicationId\":{ \"$oid\" : \"" + p2mApplicationid + "\"},\n" +
							"    \"version\" : 1\n" +
							"}";

					saveproductAdvancedInfojson = "{\n" +
							saveproductAdvancedInfojson + ",\n"+
							"	 \"name\": \"" + p2mName + "\",\n" +
							"	 \"p2mApplicationId\":{ \"$oid\" : \"" + p2mApplicationid + "\"},\n" +
							"    \"version\" : 1\n" +
							"}";

					shippingDao.intoMongodb(p2mShipping);

//					System.out.println(p2mApplicationid);

//					System.out.println(country);

//					System.out.println(saveproductAdvancedInfojson);

					p2MProductInfoDao.intoMongodb(saveproductAdvancedInfojson);



//					System.out.println(saveregionaljson);

					regionalDao.intoMongodb(saveregionaljson);


					country = "";


				} catch (Exception e) {
					e.printStackTrace();

					savep2mApplicationjson = "{\"name\": \"" + p2mName + "\",\n" +
							"  \"supplierId\": \"" + kcode + "\",\n" +
							"  \"selectedProduct\": \"" + basecode + "\",\n" +
							"  \"selectedCountry\": \"" + country + "\",\n" +
							"  \"selectedPlatform\": \"" + selectedPlatform + "\",\n" +
							"  \"bpId\": \"" + product_Id + "\",\n" +
							"  \"exception_message\": \"" + e.getMessage() + "\",\n" +
							"  \"exception_stacktrace\": \"" + ExceptionUtils.getStackTrace(e) + "\"\n" +
							"}";

					System.out.println(country);

					System.out.println(savep2mApplicationjson);

					p2MApplicationDao.intoMongodbException(savep2mApplicationjson);

					country = "";

				}

			}

		}

	}

	private Boolean allSkuApplied(String[] length, String[] width, String[] height ){
		Boolean allSkuApplied=true;
		String tempLength = length[0];
		String tempWidth = width[0];
		String tempHeight = height[0];


		for (int i=1;i<width.length;i++){
			 if (!length[i].equals(tempLength)){
				allSkuApplied = false;
				break;
			}else if (!width[i].equals(tempWidth)){
				allSkuApplied = false;
				break;
			}else if (!height[i].equals(tempHeight)){
				allSkuApplied = false;
				break;
			}
		}

		return allSkuApplied;
	}

	private Boolean allSkuWeightApplied(String[] weight){
		Boolean allSkuWeightApplied =true;
		String tempWeight = weight[0];
		for (String skuWeight : weight){
			if (!skuWeight.equals(tempWeight)){
				allSkuWeightApplied = false;
				break;
			}
		}
		return allSkuWeightApplied;
	}

	private String testP2mShipping(String basecode,String[][] skuList,String[][] perCountrySkuList){

			int variableCount =skuList.length;
			String[] sku = new String[variableCount];
			String[] netLength= new String[variableCount];
			String[] netWidth= new String[variableCount];
			String[] netHeight= new String[variableCount];
			String psAllSkuApplied ="no";
			String allNetLength= "0";
			String allNetWidth= "0";
			String allNetHeight= "0";

			String[] perCountrySku= new String[variableCount];
			String[] includePackageLength= new String[variableCount];
			String[] includePackageWidth= new String[variableCount];
			String[] includePackageHeight= new String[variableCount];
			String[] includePackageWeight= new String[variableCount];
			String ppsAllSkuApplied="no";
			String allIncludePackageLength= "0";
			String allIncludePackageWidth= "0";
			String allIncludePackageHeight= "0";
			String ppwAllSkuApplied="no";
			String allIncludePackageWeight= "0";

			for (int i=0;i<skuList.length;i++){
				for (String column : skuList[i]){
					String[] columnData = column.split(":");
					if (columnData[0].equals("SKU")) sku[i]=columnData[1];

					if (columnData[0].equals("packageDimension1")){
						netLength[i]=((columnData.length==1)?"None" : columnData[1]);
					}else if (columnData[0].equals("packageDimension2")){
						netWidth[i]=((columnData.length==1)?"None" : columnData[1]);
					}else if (columnData[0].equals("packageDimension3")){
						netHeight[i]=((columnData.length==1)?"None" : columnData[1]);
					}

				}
			}

			for (int i=0;i<perCountrySkuList.length;i++){
				for (String column : perCountrySkuList[i]){
					String[] columnData = column.split(":");
					if (columnData[0].equals("SKU")) perCountrySku[i]=columnData[1];

					if (columnData[0].equals("packageDimension1")){
						includePackageLength[i]=((columnData.length==1)?"None" : columnData[1]);
					}else if (columnData[0].equals("packageDimension2")){
						includePackageWidth[i]=((columnData.length==1)?"None" : columnData[1]);
					}else if (columnData[0].equals("packageDimension3")){
						includePackageHeight[i]=((columnData.length==1)?"None" : columnData[1]);
					}else if (columnData[0].equals("packageWeight")){
						includePackageWeight[i]=((columnData.length==1)?"None" : columnData[1]);
					}

				}
			}

			if (variableCount>1){
				if (allSkuApplied(netLength,netWidth,netHeight) == true){
					psAllSkuApplied="yes";
					allNetLength=netLength[0];
					allNetWidth=netWidth[0];
					allNetHeight=netHeight[0];
				}
				if (allSkuApplied(includePackageLength,includePackageWidth,includePackageHeight)== true){
					ppsAllSkuApplied="yes";
					allIncludePackageLength=includePackageLength[0];
					allIncludePackageWidth=includePackageWeight[0];
					allIncludePackageHeight=includePackageHeight[0];
				}
				if(allSkuWeightApplied(includePackageWeight) == true){
					ppwAllSkuApplied="yes";
					allIncludePackageWeight=includePackageWeight[0];
				}
			}
			String shippingSkuData="";

			for (int i=0;i<variableCount;i++){
				System.out.print(sku[i]+" ");
				System.out.print(netLength[i]+" ");
				System.out.print(netWidth[i]+" ");
				System.out.print(netHeight[i]+" ");
				System.out.println("includePackage: ");
				System.out.println(includePackageLength[i]+" ");
				System.out.println(includePackageWidth[i]+" ");
				System.out.println(includePackageHeight[i]+" ");
				System.out.println(includePackageWeight[i]+" ");
				System.out.println();

			shippingSkuData+="{\"sku\": \"" + sku[i] + "\",\n" +
					           " \"netLength\" :\"" + netLength[i] + "\",\n" +
					           " \"netWidth\" : \""+ netWidth[i] + "\",\n" +
					           " \"netHeight\" :\"" + netHeight[i] + "\",\n" +
					           " \"includePackageLength\" :\"" + includePackageLength[i] + "\",\n" +
					           " \"includePackageWidth\" :\"" + includePackageWidth[i] + "\",\n" +
					           " \"includePackageHeight\" :\"" + includePackageHeight[i] + "\",\n" +
					           " \"includePackageWeight\" :\"" + includePackageWeight[i] + "\",\n" +
					           "}";

			}

			String shippingJson="\"type\" : \"Shipping\" ,\n" +
			" \"appliedSku\" : [" + shippingSkuData + "]\n" +
			" \"checkReturn\" : false ,\n" +
			" \"haslocalWarehouse\" : \"yes\",\n" +
			" \"psAllSkuApplied\" : \"" + psAllSkuApplied +"\",\n" +
			" \"ppsAllSkuApplied\" : \"" + ppsAllSkuApplied +"\",\n" +
			" \"ppwAllSkuApplied\" : \"" + ppwAllSkuApplied +"\",\n" +
			" \"allIncludePackageLength\" : \"" + allIncludePackageLength +"\",\n" +
			" \"allIncludePackageWidth\" : \"" + allIncludePackageWidth +"\",\n" +
			" \"allIncludePackageHeight\" : \"" + allIncludePackageHeight +"\",\n" +
			" \"allIncludePackageWeight\" : \"" + allIncludePackageWeight +"\",\n" +
			" \"allNetLength\" : \"" + allNetLength +"\",\n" +
			" \"allNetWidth\" : \"" + allNetWidth +"\",\n" +
			" \"allNetHeight\" : \"" + allNetHeight +"\",\n" +
			" \"comment\" : { \"shippingMethod\" : \"\",\n"+
			" \"shippingInfo\" : \"\"}";

			return shippingJson;

	}

	private String getP2MName(Product p, String basecode){
		String createTime =  OffsetDateTime.ofInstant(p.getCreateTime().toZonedDateTime().toInstant(), ZoneId.of("Asia/Taipei")).toString();

		createTime = createTime.substring(0,10).replace("-","");

		String p2mName = basecode + "-" + createTime;

		return p2mName;
	}

	private String[] getData(String productorsku){
		String productDataHeading = productorsku.replace("\\\"", "");

		productDataHeading = productDataHeading.replace("\"", "");

		String[] bpData = productDataHeading.split(",");
		return bpData;
	}

}
