package com.kindminds.drs.service.usecase.logistics;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.fasterxml.uuid.Generators;
import com.kindminds.drs.Country;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.api.data.access.rdb.product.ProductDao;

import com.kindminds.drs.api.data.transfer.productV2.ProductPackageDimWeightDto;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderInfo;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderSkuInfo;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsSearchCondition;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.api.v2.biz.domain.model.request.SaveIvsLineitemRequest;
import com.kindminds.drs.api.v2.biz.domain.model.request.SaveIvsRequest;
import com.kindminds.drs.core.query.CommonQuerySrv;
import com.kindminds.drs.core.query.logistics.MaintainShipmentIvsQuerySrv;
import com.kindminds.drs.core.service.command.MaintainShipmentIvsCmdSrv;

import com.kindminds.drs.api.usecase.logistics.MaintainShipmentIvsUco;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MApplicationDao;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.kindminds.drs.Context;
import com.kindminds.drs.Currency;

import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;



import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.v1.model.impl.ShipmentIvsSearchConditionImplSvc;
import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.service.util.MailUtil.SignatureType;

@Service
public class MaintainShipmentIvsUcoImpl implements MaintainShipmentIvsUco {

    /*
    @Autowired private MaintainShipmentIvsDao dao;
    @Autowired private CompanyDao companyRepo;
    @Autowired private CurrencyDao currencyRepo;
    @Autowired private UserDao userRepo;
    @Autowired private MailUtil mailUtil;
    @Autowired private MessageSource messageSource;
    @Autowired @Qualifier("envProperties") private Properties env;
    */

    // private MaintainShipmentIvsDao dao;
     private CompanyDao companyRepo;
     private CurrencyDao currencyRepo;
     @Autowired
     private ProductDao productRepo;
     private UserDao userRepo;
     private MailUtil mailUtil;
     @Autowired
     private MessageSource messageSource;

    @Autowired @Qualifier("envProperties") private Properties env;

    @Autowired
    private MaintainShipmentIvsQuerySrv qSrv;

    @Autowired
    private CommonQuerySrv cqSrv;

    private MaintainShipmentIvsCmdSrv cmdSrv = new MaintainShipmentIvsCmdSrv();

    private P2MApplicationDao p2MApplicationDao = new P2MApplicationDao();

    private static final String LOGISTICS = "Logistics <logistics@drs.network>";
    private static final String ACCOUNT_MANAGERS = "account.managers@tw.drs.network";
    private static final String SOFTWARE_ENGINEERS = "software.engineering@drs.network";
    private static final String ADDRESS_NO_REPLY = "DRS.network <drs-noreply@tw.drs.network>";



    public DtoList<ShipmentIvs> retrieveList(IvsSearchCondition condition, int pageIndex) {

        return qSrv.getList(condition , pageIndex);
    }

    @Override
    public Map<String, String> getActiveAndOnboardingSkuCodeToSupplierNameMap() {
        return this.qSrv.getActiveAndOnboardingSkuCodeToSupplierNameMap();
    }

    @Override
    public Map<String, String> getActiveAndOnboardingSkuCodeToSupplierNameMap(String supplierKcode) {
        return this.qSrv.getActiveAndOnboardingSkuCodeToSupplierNameMap(supplierKcode);
    }


    @Override
    public String getDefaultSalesTaxPercentage() {
        return this.qSrv.getDefaultSalesTaxPercentage();
    }




    @Override
    public Boolean isGuiInvoiceRequired(String skuCode) {
        return this.qSrv.getIsGuiInvoiceRequired(skuCode);
    }




    @Override
    public Map<Integer, String> getFcaDeliveryLocationIdToLocationMap() {
        return this.qSrv.getFcaDeliveryLocationIdToLocationMap();
    }

    @Override
    public String getFcaDeliveryDate(String destinationCountry,String shippingMethod,
                                     int fcaDeliveryLocationId,String expectedExportDateStr) {

        return this.qSrv.getFcaDeliveryDate(destinationCountry,shippingMethod
        ,fcaDeliveryLocationId , expectedExportDateStr);
    }

    @Override
    public String createDraft(SaveIvsRequest request) {
        return cmdSrv.createDraft(request);
    }

    @Override
    public ShipmentIvs get(String name) {
        return this.qSrv.get(name);
    }

    @Override
    public String update(SaveIvsRequest request) {
        return this.cmdSrv.update(request);
    }


    @Override
    public String submit(String name) {
        return this.cmdSrv.submit(name);
    }


    @Override
    public String accept(String name) {

        return this.cmdSrv.accept(name);
    }

    @Override
    public String confirm(String shipmentName) {
        return this.cmdSrv.confirm(shipmentName);
    }


    @Override
    public String delete(String shipmentName) {
        return this.cmdSrv.delete(shipmentName);
    }

    @Override
    public Map<String, String> getAllCompanyKcodeToNameMap() {

        return this.cqSrv.getAllCompanyKcodeToNameMap();
    }



    @Override
    public String saveGuiInvoiceFile(String supplierKcode , String fileName, byte[] bytes) {

        if (!StringUtils.hasText(fileName)) return "file name missing";

        String uuid = Generators.randomBasedGenerator().generate().toString();
        String fullFileName = uuid+fileName;
        System.out.println("catalina.home: " + System.getProperty("catalina.home"));
        System.out.println("catalina.base: " + System.getProperty("catalina.base"));
        File fullPath = new File(this.getRootFileDir() + File.separator + supplierKcode + File.separator + "IVS");
        if (!fullPath.exists()) fullPath.mkdirs();
        File file = new File(fullPath.getAbsolutePath() + File.separator + fullFileName);
        System.out.println(file.getAbsolutePath());

        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
            stream.write(bytes);
        } catch (IOException e2) {
            //logger.info("IOException in MaintainShipmentIvsUcoImpl method uploadGuiInvoiceFile: ", e2);
        }
        return uuid;

    }

//    @Override
//    public File getGuiInvoiceFile(String shipmentName, String fileName) {
//
//        String fullPath = this.getRootFileDir()+File.separator+shipmentName+File.separator+fileName;
//        File downloadFile = new File(fullPath);
//
//        return downloadFile;
//
//    }
    @Override
    public File getGuiInvoiceFile(String supplierKcode, String fileName) {

        String fullPath = this.getRootFileDir()+File.separator+supplierKcode+File.separator+"IVS"+File.separator+fileName;
        File downloadFile = new File(fullPath);


        return downloadFile;

    }


    //=======================

    private IvsSearchCondition getActualCondition(IvsSearchCondition originCondition, UserInfo user){
        if(user.isDrsUser()) return originCondition;
        else return new ShipmentIvsSearchConditionImplSvc(originCondition,user.getCompanyKcode());
    }

    /*
    private void appendPaidTotalToListItem(DtoList<ShipmentIvs> list){
        List<String> shipmentNameList = this.createNameListFromShipmentList(list);
        if(shipmentNameList.size()==0) return;
        Map<String,BigDecimal> shipmentNameToPaymentTotalMap = this.dao.queryShipmentNameToPaymentTotalMap(shipmentNameList);
        for(ShipmentIvs shipment:list.getItems()){
          //  ShipmentIvsImpl origShp = (ShipmentIvsImpl)shipment;
           // origShp.setPaymentTotal(shipmentNameToPaymentTotalMap.get(shipment.getName()));
        }
    }*/

    /*
    private void appendUnsName(DtoList<ShipmentIvs> list){
        List<String> shipmentNameList = this.createNameListFromShipmentList(list);
        if(shipmentNameList.size()==0) return;
        Map<String,String> shipmentNameToUnsNameMap = this.dao.queryShipmentNameToUnsNameMap(shipmentNameList);
        for(ShipmentIvs shipment:list.getItems()){
           // ((ShipmentIvsImpl)shipment).setUnsName(shipmentNameToUnsNameMap.get(shipment.getName()));
        }
    }*/

    private List<String> createNameListFromShipmentList(DtoList<ShipmentIvs> list){
        List<String> shipmentNameList = new ArrayList<String>();
       // for(ShipmentIvs shipment:list.getItems())
          //  shipmentNameList.add(shipment.getName());
        return shipmentNameList;
    }



    @Override
    public Currency getSupplierCurrency() {
        String companyKcode = Context.getCurrentUser().getCompanyKcode();
        Assert.isTrue(this.companyRepo.isSupplier(companyKcode));
        return this.currencyRepo.queryCompanyCurrency(companyKcode);
    }




    /*



    private void processShipmentLineItems(List<IvsLineItem> lineItems) {
        //rearrangeLineItems(lineItems);
        //numberLineItems(lineItems);
    }

    private void rearrangeLineItems(List<IvsLineItem> lineItems) {
        List<IvsLineItem> mixedBoxItems = new ArrayList<>();
        Iterator<IvsLineItem> iterator = lineItems.iterator();
        while(iterator.hasNext()) {
            IvsLineItem nextItem = iterator.next();
            if (nextItem.getMixedBoxLineSeq() != 0) {
                mixedBoxItems.add(nextItem);
                iterator.remove();
            }
        }
        Collections.sort(mixedBoxItems,
                Comparator.comparing(IvsLineItem::getBoxNum)
                        .thenComparing(IvsLineItem::getMixedBoxLineSeq));
        Collections.sort(lineItems,
                (IvsLineItem o1, IvsLineItem o2)->o1.getSkuCode().compareTo(o2.getSkuCode()));
        lineItems.addAll(mixedBoxItems);
    }

    private void numberLineItems(List<IvsLineItem> lineItems) {
        int boxNumber = 0, start = 0, end = 0;

        for (IvsLineItem lineItem : lineItems) {
            int mixedBoxLineNum = lineItem.getMixedBoxLineSeq();
            if (mixedBoxLineNum <= 1) {
                start++;
                end += Integer.valueOf(lineItem.getCartonCounts());
                boxNumber++;
            }
            lineItem.setBoxNum(boxNumber);
            lineItem.setCartonNumberStart(start);
            lineItem.setCartonNumberEnd(end);
            start = end;
        }
    }

    */

    /*

    private BigDecimal calculateSalesTaxRate(String salesTaxPercentageStr){
        BigDecimal salesTaxPercentage = new BigDecimal(salesTaxPercentageStr);
        return salesTaxPercentage.divide(new BigDecimal("100"));
    }

    private BigDecimal calculateSubtotal(ShipmentIvs shipment,int scale) {
        BigDecimal amountUntaxed = BigDecimal.ZERO;
        //for(IvsLineItem item : shipment.getLineItems()){
          //  amountUntaxed = amountUntaxed.add(new BigDecimal(item.getAmountUntaxed()));
       // }
        //return amountUntaxed.setScale(scale,RoundingMode.HALF_UP);

        return null;
    }

    private BigDecimal calculateSalesTax(BigDecimal salesTaxRate,BigDecimal amountUntaxed,int scale) {
        return amountUntaxed.multiply(salesTaxRate).setScale(scale,RoundingMode.HALF_UP);
    }
    */

//	private void checkSkuNotDuplicateInLine(List<ShipmentIvsLineItem> items){
//		Set<String> skuSet = new HashSet<String>();
//		for(ShipmentIvsLineItem item:items){
//			skuSet.add(item.getSkuCode());
//		}
//		Assert.isTrue(skuSet.size()==items.size(),"Duplicated SKU Line");
//	}





    /*

    private String generateShipmentName(String kcode,Integer nextSerialId){
        return this.shipmentNamePrefix+"-"+kcode+"-"+nextSerialId.toString();
    }*/

    private void renameShipmentDirectory(String oldName, String newName) {
        String rootPath = this.getRootFileDir() + File.separator;
        Path oldPath = Paths.get(rootPath, oldName);
        Path newPath = Paths.get(rootPath, newName);
        try {
            Files.move(oldPath, newPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*
    private void processAcceptNotificationToSupplier(String shipmentName,String buyerKcode) {
        boolean sendNotify = Boolean.parseBoolean(((String)this.env.get("SEND_NOTIFY")));
        if(sendNotify){
            try{
                Assert.isTrue(buyerKcode.equals("K2"));
                Locale locale = Context.getCurrentUser().getLocale();
                Assert.isTrue(locale.equals(Locale.TAIWAN));
                String subject = this.messageSource.getMessage("mail.InventoryShipmentsDRS-Accept-Subject", new Object[] {shipmentName}, locale);
                String body = this.messageSource.getMessage("mail.InventoryShipmentsDRS-Accept-Body", new Object[] {shipmentName,shipmentName,shipmentName},Context.getCurrentUser().getLocale());
                String signature = this.mailUtil.getSignature(SignatureType.NO_REPLY);
                body = this.mailUtil.appendSignature(body,signature);
                String sellerEmail = this.dao.queryShipmentPickupRequesterEmail(shipmentName);
                if(sellerEmail==null) return;
                String [] mailTo = {sellerEmail};
                Assert.isTrue(this.userRepo.isDrsUser(Context.getCurrentUser().getUserId()));
                String currentUserEmail = this.userRepo.queryUserMail(Context.getCurrentUser().getUserId());
                String [] bcc = {currentUserEmail};
                this.mailUtil.SendMimeWithBcc(mailTo,bcc,currentUserEmail,subject,body);
            }
            catch(Exception e){
                this.mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "IVS Accept email ERROR", e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
            }
            finally{
            }
        }
    }
    */

    private void processRequestPickupNotification(String supplierKcode,String shipmentName) {
        boolean sendNotify = Boolean.parseBoolean(((String)this.env.get("SEND_NOTIFY")));
        if(sendNotify){
            try {
                String [] toMails = {LOGISTICS, ACCOUNT_MANAGERS};
                String body = this.getRequestPickupNotificationMessageBody(supplierKcode, shipmentName);
                String signature = this.mailUtil.getSignature(SignatureType.NO_REPLY);
                body = this.mailUtil.appendSignature(body,signature);
                this.mailUtil.SendMime(toMails, ADDRESS_NO_REPLY, this.getRequestPickupNotificationSubject(supplierKcode, shipmentName), body);
            }catch(Exception e){
                this.mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "IVS Confirmation email ERROR", e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
            }
            finally{
            }
        }
    }

    private String getRequestPickupNotificationSubject(String supplierKcode, String shipmentName){
        return "Suppler "+supplierKcode+" has requested pick-up for shipment: "+shipmentName;
    }

    private String getRequestPickupNotificationMessageBody(String supplierKcode, String shipmentName){
        return this.getRequestPickupNotificationSubject(supplierKcode, shipmentName)+"\n"
                + "<br>"
                + "<br>"
                + "<a href='https://access.drs.network/InventoryShipments/"+shipmentName+"'+> Link to "+shipmentName+".</a> ";
    }



    @Override
    public int getDaysToPrepare(String shippingMethod) {
        ShippingMethod method = ShippingMethod.valueOf(shippingMethod);
        return this.qSrv.getDaysToPrepareAddWeekend(method.getDaysToPrepare());
    }


    @Override
    public String methodForTestToGetPickupRequesterEmail(String shipmentName) {
        //return this.dao.queryShipmentPickupRequesterEmail(shipmentName);
        return "";
    }


    @Override
    public List<String> getDestinationCountries() {
        return this.qSrv.getDestinationCountries();
    }

    @Override
    public List<String> getApprovedDestinationCountries(String kcode) {
        List<String> result = this.qSrv.findApprovedCountryList(kcode);

        List<String> list = new ArrayList<String>();

        for(int i = 0; i <result.size(); i++) {

            String selectedCountry = result.get(i);
            if(selectedCountry.equals("DE") || selectedCountry.equals("FR") || selectedCountry.equals("IT") || selectedCountry.equals("ES")){
                selectedCountry="EU";
            }
            if (!list.contains(selectedCountry)) {
                list.add(selectedCountry);
            }
        }

        return list;

    }

    @Override
    public List<String> getActiveAndOnboardingSkuCode(String kcode,String selectedCountry) {

        int marketplaceId = 0;
        if(selectedCountry.equals("US")){
            marketplaceId = 1;
        }else if(selectedCountry.equals("UK")){
            marketplaceId = 4;
        }else if(selectedCountry.equals("CA")){
            marketplaceId = 5;
        }else if(selectedCountry.equals("EU")){
            return this.qSrv.findApprovedSkuByEUList(kcode);
        }

        return this.qSrv.findApprovedSkuByCountryList(kcode,marketplaceId);
//        List<String> result2= p2MApplicationDao.findApprovedSkuByCountryList(kcode,selectedCountry);
//
//
//        return result2;
    }

    @Override
    public List<ShippingMethod> getShippingMethods(String countryStr) {
        return this.qSrv.getShippingMethods();
    }

    @Override
    public Map<String, String> getSellerKcodeToNameMap() {
        return this.cqSrv.getSellerKcodeToNameMap();
    }

    @Override
    public List<ShippingMethod> getShippingMethods() {
        List<ShippingMethod> fullShippingMethodList = new ArrayList<ShippingMethod>();
        fullShippingMethodList.add(ShippingMethod.EXPRESS);
        fullShippingMethodList.add(ShippingMethod.AIR_CARGO);
        fullShippingMethodList.add(ShippingMethod.SEA_FREIGHT);
        return fullShippingMethodList;
    }

    @Override
    public List<ShipmentStatus> getShipmentStatusList() {
        return this.qSrv.getShipmentStatusList();
    }




    @Override
    public ProductSkuStock getProductSkuStock(String shipmentName, String skuCodeByDrs) {
        return this.qSrv.getProductSkuStock(shipmentName,skuCodeByDrs);
    }



    @Override
    public Boolean removeGuiInvoiceFile(String supplierKcode , String fileName) {
        return cmdSrv.ngremoveGuiInvoiceFile(supplierKcode ,fileName);
    }

    private String getRootFileDir(){
        return System.getProperty("catalina.home");
    }


    @Override
    public PurchaseOrderInfo getPurchaseOrderInfo(String shipmentName) {
        return qSrv.getPurchaseOrderInfo(shipmentName);
    }


    @Override
    public List<PurchaseOrderSkuInfo> getPurchaseOrderInfoList(String shipmentName) {
        return qSrv.getPurchaseOrderInfoList(shipmentName);
    }


    @Override
    public ShipmentIvs.ShipmentIvsLineItem getShipmentLineItem(String ivsName , Integer boxNum ,Integer mixedBoxLineSeq) {
        return qSrv.getShipmentLineItem(ivsName , boxNum, mixedBoxLineSeq);
    }



    /*
    Methods for import IVS
     */
    private Map<String, ShippingMethod> shippingMethodMap;
    private Map<String, String> allSkuToBaseProductMap;
    private List<String> regions = Country.getIvsDestinationCountries();
    private BidiMap<Integer, String> idToDeliveryLocationMap;

    private void initializeLookupMaps(String language, String companyCode) {
        shippingMethodMap = getShippingMethodMap(language);

        allSkuToBaseProductMap = productRepo.queryAllSkuToBaseProductMap(companyCode);

        idToDeliveryLocationMap = qSrv.getFcaDeliveryLocationIdToLocationMap();
    }

    private Map<String, ShippingMethod> getShippingMethodMap(String language) {
        Map<String, ShippingMethod> shippingMethodMap = new HashMap<>();
        if (language.equals("EN")) {
            shippingMethodMap.put("Express", ShippingMethod.EXPRESS);
            shippingMethodMap.put("Air cargo", ShippingMethod.AIR_CARGO);
            shippingMethodMap.put("Sea freight", ShippingMethod.SEA_FREIGHT);
        } else {
            shippingMethodMap.put("快遞", ShippingMethod.EXPRESS);
            shippingMethodMap.put("空運", ShippingMethod.AIR_CARGO);
            shippingMethodMap.put("海陸運", ShippingMethod.SEA_FREIGHT);
        }
        return shippingMethodMap;
    }

    @Override
    public String importRetailIVS(byte[] fileBytes) {

        int lineCount = 1;
        boolean afterMixedBox = false;

        initializeLookupMaps("EN", "K101");

        StringBuilder resultMsg = new StringBuilder();

        List<CSVRecord> retailIvsList = getRecordsFromFile(fileBytes);
        if (retailIvsList.isEmpty()) {
            return resultMsg.toString();
        } else if (retailIvsList.get(0).size() != 10) {
            return "Incorrect number of columns";
        }


        List<SaveIvsRequest> ivsList = new ArrayList<>();
        SaveIvsRequest ivs = null;
        SaveIvsLineitemRequest ivsLine = null;
        String marketRegion;
        ShippingMethod shippingMethod;
        String exportDate;
        String deliveryLocationId;
        String deliveryDate;
        String mixedBox;
        String sku;
        String unitsPerCarton;
        String numOfCarton;
        int boxNum = 0;
        int mixedBoxLineSeq = 0;
        int cartonNumStart = 0;
        int cartonNumEnd = 0;
        String fcaPrice;
        String retailPONumber;
        BigDecimal dimLength;
        BigDecimal dimWidth;
        BigDecimal dimHeight;
        BigDecimal weight;
        BigDecimal quantity;
        BigDecimal amountUntaxed;


        for(CSVRecord record : retailIvsList){
            lineCount++;

            if (isEmptyLine(record)) {
                continue;
            }

            marketRegion = record.get(0);
            shippingMethod = shippingMethodMap.get(record.get(1));
            exportDate = record.get(2);
            deliveryDate = record.get(3);
            mixedBox = record.get(4);
            sku = record.get(5);
            if (sku.startsWith("K101-")) {
                sku = sku.replaceAll("K101-", "");
            }

            unitsPerCarton = record.get(6);
            numOfCarton = record.get(7);
            fcaPrice = record.get(8);
            retailPONumber = record.get(9);

            //reset afterMixedBox flag
            if (afterMixedBox &&
                    isNewShipment(marketRegion, record.get(2), "集貨櫃場(請用卡車派送)", deliveryDate)) {
                afterMixedBox = false;
            }


            //validate line and add message if error
            validateIVS(resultMsg, "", "", "", marketRegion,
                    record.get(1), exportDate, "集貨櫃場(請用卡車派送)",
                    deliveryDate, mixedBox, afterMixedBox, sku, numOfCarton, lineCount);


            if (isNewShipment(marketRegion, record.get(2), "集貨櫃場(請用卡車派送)", deliveryDate)) {
                if (ivs != null) {
                    ivsList.add(ivs);
                    boxNum = 0;
                    cartonNumStart = 0;
                    cartonNumEnd = 0;
                }

                if (shippingMethod.equals(ShippingMethod.EXPRESS)) {
                    deliveryLocationId = "1";
                } else {
                    deliveryLocationId = "3";
                }

                ivs = new SaveIvsRequest(marketRegion, shippingMethod, exportDate,
                        deliveryLocationId, deliveryDate);

            }

            //number boxNum, mixedBoxLineSeq, carton start and end
            if (mixedBox.equals("No")) {
                boxNum++;
                mixedBoxLineSeq = 0;
                cartonNumStart++;
                cartonNumEnd += Integer.valueOf(numOfCarton);
            } else if (mixedBox.equals("Yes")) {
                afterMixedBox = true;

                if (numOfCarton.equals("1")) {
                    boxNum++;
                    mixedBoxLineSeq = 1;
                    cartonNumStart++;
                    cartonNumEnd++;
                } else {
                    mixedBoxLineSeq++;
                }
            }

            if (resultMsg.length() != 0) {
                continue;
            }

            ProductPackageDimWeightDto productDimWeight = productRepo
                    .queryPackageDimWeight(allSkuToBaseProductMap.get(sku)).get(0);

            dimLength = new BigDecimal(productDimWeight.getDimLength());
            dimWidth = new BigDecimal(productDimWeight.getDimWidth());
            dimHeight = new BigDecimal(productDimWeight.getDimHeight());

            if (productDimWeight.getDimUnit().equals("in")) {
                dimLength = dimLength.multiply(BigDecimal.valueOf(2.54));
                dimWidth = dimWidth.multiply(BigDecimal.valueOf(2.54));
                dimHeight = dimHeight.multiply(BigDecimal.valueOf(2.54));
            }

            weight = new BigDecimal(productDimWeight.getWeight());
            if (productDimWeight.getWeightUnit().equals("g")) {
                weight = weight.divide(BigDecimal.valueOf(1000),3, RoundingMode.HALF_UP);
            }

            quantity = calculateQuantity(unitsPerCarton, numOfCarton);

            amountUntaxed = quantity.multiply(new BigDecimal(fcaPrice));


            ivsLine = new SaveIvsLineitemRequest(boxNum, mixedBoxLineSeq, cartonNumStart, cartonNumEnd, sku,
                    unitsPerCarton, numOfCarton, fcaPrice, dimLength.toPlainString(),
                    dimWidth.toPlainString(), dimHeight.toPlainString(),
                    weight.toPlainString(), retailPONumber, quantity.toPlainString(),
                    amountUntaxed.toPlainString(), false);


            ivs.getLineItem().add(ivsLine);

            cartonNumStart = cartonNumEnd;
        }

        //add last IVS
        ivsList.add(ivs);


        if (resultMsg.length() != 0) {
            resultMsg.insert(0, "Import file failed: <br>");
            return resultMsg.toString();
        }

        for (SaveIvsRequest saveIvsRequest : ivsList) {
            resultMsg.append("IVS number: ")
                    .append(cmdSrv.createDraftFromImport(saveIvsRequest))
                    .append(", successfully imported ")
                    .append(saveIvsRequest.getLineItem().size())
                    .append(" lines. <br>");
        }


        return resultMsg.toString();
    }

    private Boolean isEmptyLine(CSVRecord record) {
        for (int i = 0; i < record.size(); i++) {
            if (StringUtils.hasText(record.get(i))) {
                return false;
            }
        }
        return true;
    }

    private Boolean isNewShipment(String marketRegion, String shippingMethod,
                                  String deliveryLocation, String deliveryDate) {

        return StringUtils.hasText(marketRegion) && StringUtils.hasText(shippingMethod) &&
                StringUtils.hasText(deliveryLocation) && StringUtils.hasText(deliveryDate);
    }


    private void validateIVS(StringBuilder errorMsg, String fileKCode, String kCode,
                             String requireRepackaging, String marketRegion, String shippingMethod,
                             String exportDate, String deliveryLocation, String deliveryDate,
                             String mixedBox, boolean afterMixedBox, String sku,
                             String numOfCarton, Integer lineCount) {

        Locale locale = Context.getCurrentUser().getLocale();

        if (StringUtils.hasText(fileKCode) && !fileKCode.equalsIgnoreCase(kCode)) {
            errorMsg.append(messageSource.getMessage("inventoryShipment.importCompanyCodeError",
                    new Object[] {lineCount, fileKCode}, locale));
        }

        if (StringUtils.hasText(requireRepackaging) &&
                (!requireRepackaging.equals("Yes") && !requireRepackaging.equals("No"))) {
            errorMsg.append(messageSource.getMessage("inventoryShipment.importRepackagingError",
                    new Object[] {lineCount}, locale));
        }

        if (StringUtils.hasText(marketRegion) && !regions.contains(marketRegion)) {
            errorMsg.append(messageSource.getMessage("inventoryShipment.importMarketError",
                    new Object[] {lineCount, marketRegion}, locale));
        }

        if (StringUtils.hasText(marketRegion) && !shippingMethodMap.containsKey(shippingMethod)) {
            errorMsg.append(messageSource.getMessage("inventoryShipment.importShippingMethodError",
                    new Object[] {lineCount, shippingMethod}, locale));
        }

        if (StringUtils.hasText(marketRegion) && (!StringUtils.hasText(exportDate)
                || !exportDate.matches("\\d{4}-\\d{2}-\\d{2}"))) {

            errorMsg.append(messageSource.getMessage("inventoryShipment.importExportDateError",
                    new Object[] {lineCount, exportDate}, locale));
        }

        if (StringUtils.hasText(marketRegion) && (!StringUtils.hasText(deliveryLocation)
                || !idToDeliveryLocationMap.containsValue(deliveryLocation))) {

            errorMsg.append(messageSource.getMessage("inventoryShipment.importDeliveryLocationError",
                    new Object[] {lineCount, deliveryLocation}, locale));
        }

        if (StringUtils.hasText(marketRegion) && (!StringUtils.hasText(deliveryDate)
                || !deliveryDate.matches("\\d{4}-\\d{2}-\\d{2}"))) {

            errorMsg.append(messageSource.getMessage("inventoryShipment.importDeliveryDateError",
                    new Object[] {lineCount, deliveryDate}, locale));
        }

        if (!mixedBox.equals("Yes") && !mixedBox.equals("No")) {
            errorMsg.append(messageSource.getMessage("inventoryShipment.importMixedBoxError",
                    new Object[] {lineCount}, locale));
        } else if (afterMixedBox && mixedBox.equals("No")) {
            errorMsg.append(messageSource.getMessage("inventoryShipment.importNonMixedBoxError",
                    new Object[] {lineCount}, locale));
        } else if (!afterMixedBox && mixedBox.equals("Yes") && !numOfCarton.equals("1")) {
            errorMsg.append(messageSource.getMessage("inventoryShipment.importFirstLineMixedError",
                    new Object[] {lineCount}, locale));
        }

        if (!allSkuToBaseProductMap.containsKey(sku)) {
            errorMsg.append(messageSource.getMessage("inventoryShipment.importSkuError",
                    new Object[] {lineCount, sku}, locale));
        }

        if (mixedBox.equals("Yes") && !numOfCarton.equals("0") && !numOfCarton.equals("1")) {
            errorMsg.append(messageSource.getMessage("inventoryShipment.importNumOfCartonsError",
                    new Object[] {lineCount, numOfCarton}, locale));
        }


    }


    @Override
    public String importDCPIVS(byte[] fileBytes, String kCode) {

        int lineCount = 1;
        boolean afterMixedBox = false;

        StringBuilder resultMsg = new StringBuilder();

        initializeLookupMaps("TW", kCode);

        List<CSVRecord> dcpIvsList = getRecordsFromFile(fileBytes);
        if (dcpIvsList.isEmpty()) {
            return resultMsg.toString();
        } else if (dcpIvsList.get(0).size() != 18) {
            return "Incorrect number of columns";
        }

        List<SaveIvsRequest> ivsList = new ArrayList<>();
        SaveIvsRequest ivs = null;
        SaveIvsLineitemRequest ivsLine = null;
        String companyCode;
        String marketRegion;
        ShippingMethod shippingMethod;
        String exportDate;
        String deliveryLocation;
        String deliveryDate;
        String specialRequest;
        Boolean requirePO;
        String mixedBox;
        String sku;
        Boolean requireRepackaging;
        String unitsPerCarton;
        String numOfCarton;
        int boxNum = 0;
        int mixedBoxLineSeq = 0;
        int cartonNumStart = 0;
        int cartonNumEnd = 0;
        String fcaPrice;
        Integer repackagingCount = 0;
        String dimLength;
        String dimWidth;
        String dimHeight;
        String weight;
        BigDecimal quantity;
        BigDecimal amountUntaxed;


        for(CSVRecord record : dcpIvsList){
            lineCount++;

            if (isEmptyLine(record)) {
                continue;
            }

            companyCode = record.get(0);
            marketRegion = record.get(1);
            shippingMethod = shippingMethodMap.get(record.get(2));
            exportDate = record.get(3);
            deliveryLocation = record.get(4);
            deliveryDate = record.get(5);
            specialRequest = record.get(6);
            requirePO = record.get(7).equals("Yes");
            mixedBox = record.get(8);
            sku = record.get(9);
            requireRepackaging = record.get(10).equals("Yes");
            dimLength = record.get(11);
            dimWidth = record.get(12);
            dimHeight = record.get(13);
            weight = record.get(14);
            unitsPerCarton = record.get(15);
            numOfCarton = record.get(16);
            fcaPrice = record.get(17);

            //reset afterMixedBox flag
            if (afterMixedBox &&
                    isNewShipment(marketRegion, record.get(2), deliveryLocation, deliveryDate)) {
                afterMixedBox = false;
            }

            //validate line and add message if error
            validateIVS(resultMsg, companyCode, kCode, record.get(10), marketRegion,
                    record.get(2), exportDate, deliveryLocation,
                    deliveryDate, mixedBox, afterMixedBox, sku, numOfCarton, lineCount);


            if (isNewShipment(marketRegion, record.get(2), deliveryLocation, deliveryDate)) {
                if (ivs != null) {
                    addIVS(ivsList, ivs, repackagingCount);
                    repackagingCount = 0;
                    boxNum = 0;
                    cartonNumStart = 0;
                    cartonNumEnd = 0;
                }

                ivs = new SaveIvsRequest(marketRegion, shippingMethod, exportDate,
                        idToDeliveryLocationMap.getKey(deliveryLocation).toString(), deliveryDate,
                        specialRequest, requirePO, companyCode);

            }

            //number boxNum, mixedBoxLineSeq, carton start and end
            if (mixedBox.equals("No")) {
                boxNum++;
                mixedBoxLineSeq = 0;
                cartonNumStart++;
                cartonNumEnd += Integer.valueOf(numOfCarton);
            } else if (mixedBox.equals("Yes")) {
                afterMixedBox = true;

                if (numOfCarton.equals("1")) {
                    boxNum++;
                    mixedBoxLineSeq = 1;
                    cartonNumStart++;
                    cartonNumEnd++;
                } else {
                    mixedBoxLineSeq++;
                }
            }

            if (resultMsg.length() != 0) {
                continue;
            }

            if (requireRepackaging) {

                repackagingCount++;
            }


            quantity = calculateQuantity(unitsPerCarton, numOfCarton);

            amountUntaxed = quantity.multiply(new BigDecimal(fcaPrice));

            ivsLine = new SaveIvsLineitemRequest(boxNum, mixedBoxLineSeq,
                    cartonNumStart, cartonNumEnd, sku,
                    unitsPerCarton, numOfCarton, fcaPrice, dimLength,
                    dimWidth, dimHeight, weight, "", quantity.toPlainString(),
                    amountUntaxed.toPlainString(), requireRepackaging);


            ivs.getLineItem().add(ivsLine);

            cartonNumStart = cartonNumEnd;

        }

        //add last IVS
        if (ivs != null) {
            addIVS(ivsList, ivs, repackagingCount);
        }


        if (resultMsg.length() != 0) {
            return resultMsg.toString();
        }


        for (SaveIvsRequest saveIvsRequest : ivsList) {
            resultMsg.append("IVS number: ")
                    .append(cmdSrv.createDraftFromImport(saveIvsRequest))
                    .append(", successfully imported ")
                    .append(saveIvsRequest.getLineItem().size())
                    .append(" lines. <br>");
        }


        return resultMsg.toString();


    }

    private BigDecimal calculateQuantity(String unitsPerCarton, String numOfCarton) {
        if (numOfCarton.equals("0")) {
            return new BigDecimal(unitsPerCarton);
        }

        return new BigDecimal(unitsPerCarton).multiply(new BigDecimal(numOfCarton));
    }

    private void addIVS(List<SaveIvsRequest> ivsList, SaveIvsRequest ivs, Integer repackagingCount) {
        if (repackagingCount > 0) {
            Integer repackagingFee = repackagingCount * 100;
            ivs.setBoxesNeedRepackaging(repackagingCount.toString());
            ivs.setRepackagingFee(repackagingFee.toString());
        }
        ivsList.add(ivs);
    }

    private List<CSVRecord> getRecordsFromFile(byte[] fileData) {
        try {
            CSVParser parser = createParser(fileData);
            List<CSVRecord> records = parser.getRecords();
            parser.close();
            return records;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private CSVParser createParser(byte[] fileData) throws IOException {
        Reader reader = new InputStreamReader(
                new BOMInputStream(new ByteArrayInputStream(fileData)));

        return CSVParser.parse(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
    }



}