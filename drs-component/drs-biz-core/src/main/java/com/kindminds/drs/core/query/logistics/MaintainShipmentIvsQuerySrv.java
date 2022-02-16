package com.kindminds.drs.core.query.logistics;

import com.kindminds.drs.Context;
import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.*;
import com.kindminds.drs.v1.model.impl.ShipmentIvsSearchConditionImplSvc;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderInfo;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderSkuInfo;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShipmentIvsDao;
import com.kindminds.drs.persist.v1.model.mapping.logistics.ShipmentIvsImpl;

import com.kindminds.drs.util.DateHelper;
import org.apache.commons.collections4.BidiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Service
public class MaintainShipmentIvsQuerySrv {

    @Autowired
    private MaintainShipmentIvsDao dao;
    @Autowired private CompanyDao companyDao;
    @Autowired private CurrencyDao currencyRepo;
    @Autowired private UserDao userRepo;

    @Autowired private MessageSource messageSource;
    //@Autowired @Qualifier("envProperties") private Properties env;

    private final String embeddedShipmentNameForDraft="DRAFT";
    private final String shipmentNamePrefix="IVS";
    private final String fcaDeliveryDateFormat = "yyyy-MM-dd";

    private static final String LOGISTICS = "Logistics <logistics@drs.network>";
    private static final String ACCOUNT_MANAGERS = "account.managers@tw.drs.network";
    private static final String SOFTWARE_ENGINEERS = "software.engineering@drs.network";
    private static final String ADDRESS_NO_REPLY = "DRS.network <drs-noreply@tw.drs.network>";

    private static final int BUFFER_SIZE = 4096;


    @Autowired
    private IvsQueries ivsQ ;


    public DtoList<ShipmentIvs> getList(IvsSearchCondition condition, int pageIndex) {

        UserInfo user = Context.getCurrentUser();
        IvsSearchCondition actualCondition = this.getActualCondition(condition,user);
        DtoList<ShipmentIvs> list = new DtoList<ShipmentIvs>();

        list.setTotalRecords(this.ivsQ.queryCounts(user.getCompanyKcode(), actualCondition));
        list.setPager(new Pager(pageIndex,list.getTotalRecords(),50));

        list.setItems(this.ivsQ.queryList(user.getCompanyKcode(),list.getPager().getStartRowNum(),list.getPager().getPageSize(),actualCondition));

        this.appendPaidTotalToListItem(list);
        this.appendUnsName(list);

        return list;

    }

    public List<String> getDestinationCountries() {
        List<String> dstCountryList = new ArrayList<String>();
        dstCountryList.add(Country.US.name());
        dstCountryList.add(Country.CA.name());
        dstCountryList.add(Country.EU.name());
        dstCountryList.add(Country.UK.name());
        return dstCountryList;
    }

    public List<ShippingMethod> getShippingMethods(String countryStr) {
        Country country = Country.valueOf(countryStr);
        List<ShippingMethod> shippingMethodList = new ArrayList<ShippingMethod>();
        shippingMethodList.add(ShippingMethod.EXPRESS);
        shippingMethodList.add(ShippingMethod.AIR_CARGO);
        shippingMethodList.add(ShippingMethod.SEA_FREIGHT);
        return shippingMethodList;
    }


    public Map<String, String> getSellerKcodeToNameMap() {
        return this.companyDao.querySupplierKcodeToShortEnUsNameMap();
    }


    public List<ShippingMethod> getShippingMethods() {
        List<ShippingMethod> fullShippingMethodList = new ArrayList<ShippingMethod>();
        fullShippingMethodList.add(ShippingMethod.EXPRESS);
        fullShippingMethodList.add(ShippingMethod.AIR_CARGO);
        fullShippingMethodList.add(ShippingMethod.SEA_FREIGHT);
        return fullShippingMethodList;
    }


    public List<ShipmentStatus> getShipmentStatusList() {
        List<ShipmentStatus> shipmentStatusList = new ArrayList<ShipmentStatus>();
        shipmentStatusList.add(ShipmentStatus.SHPT_DRAFT);
        shipmentStatusList.add(ShipmentStatus.SHPT_AWAIT_PLAN);
        shipmentStatusList.add(ShipmentStatus.SHPT_INITIAL_VERIFIED);
        shipmentStatusList.add(ShipmentStatus.SHPT_FC_BOOKING_CONFIRM);
        shipmentStatusList.add(ShipmentStatus.SHPT_PLANNING);
        shipmentStatusList.add(ShipmentStatus.SHPT_CONFIRMED);
        shipmentStatusList.add(ShipmentStatus.SHPT_AWAIT_PICK_UP);
        shipmentStatusList.add(ShipmentStatus.SHPT_FROZEN);
        shipmentStatusList.add(ShipmentStatus.SHPT_IN_TRANSIT);
        shipmentStatusList.add(ShipmentStatus.SHPT_RECEIVING);
        shipmentStatusList.add(ShipmentStatus.SHPT_RECEIVED);
        shipmentStatusList.add(ShipmentStatus.SHPT_EXCEPTION);
        return shipmentStatusList;
    }

    public Map<String, String> getActiveAndOnboardingSkuCodeToSupplierNameMap() {
        String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
        return this.ivsQ.queryActiveAndOnboardingSkuCodeToSupplierNameMap(userCompanyKcode);
    }


    public Map<String, String> getActiveAndOnboardingSkuCodeToSupplierNameMap(String supplierKcode) {
        return this.ivsQ.queryActiveAndOnboardingSkuCodeToSupplierNameMap(supplierKcode);
    }

    public List<String> findApprovedCountryList(String kcode){
        return this.ivsQ.getApprovedCountryList(kcode);
    }

    public List<String> findApprovedSkuByCountryList(String kcode, int marketplaceId){
        return this.ivsQ.getApprovedSkuByCountryList(kcode,marketplaceId);
    }

    public List<String> findApprovedSkuByEUList(String kcode){
        return this.ivsQ.getApprovedSkuByEUList(kcode);
    }

    public String getDefaultSalesTaxPercentage() {
        BigDecimal salesTaxRate = null;
        String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
        if(this.companyDao.isSupplier(userCompanyKcode)){
            String handlerKcode = this.companyDao.queryHandlerKcode(userCompanyKcode);
            Country handlerCountry = Country.fromKey(this.companyDao.queryCompanyCountryId(handlerKcode));
            salesTaxRate = handlerCountry.getSalesTaxRate();
        }
        return salesTaxRate==null?null:salesTaxRate.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
    }


    public Boolean getIsGuiInvoiceRequired(String skuCode) {

        String r  = ivsQ.queryCountryOfOrigin(skuCode);
        if( r != null){
            return !"taiwan".equalsIgnoreCase(r);
        }

        return  false;
    }

    public ProductSkuStock getProductSkuStock(String shipmentName, String skuCodeByDrs) {

        return this.ivsQ.queryProductSkuStock(shipmentName, skuCodeByDrs);
    }

    public BidiMap<Integer, String> getFcaDeliveryLocationIdToLocationMap() {
        return this.ivsQ.queryAvailableFcaLocationIdToNameMap();
    }


    public String getFcaDeliveryDate(String destinationCountry,String shippingMethod, int FCADeliveryLocationId,String expectedExportDateStr) {
        Country dstCountry = Country.valueOf(destinationCountry);
        ShippingMethod shpMethod = ShippingMethod.valueOf(shippingMethod);
        int daysBeforeExportForFcaDelivery = this.ivsQ.queryDaysBeforeExportForFcaDelivery(dstCountry,shpMethod,FCADeliveryLocationId);
        Date expectedExportDate = DateHelper.toDate(expectedExportDateStr, this.fcaDeliveryDateFormat);
        Calendar cal = Calendar.getInstance();
        cal.setTime(expectedExportDate);
        Calendar fcaDeliveryDate = (Calendar)cal.clone();
        for(int i=0;i<daysBeforeExportForFcaDelivery;i++){
            fcaDeliveryDate.add(Calendar.DAY_OF_MONTH, -1);
            if (fcaDeliveryDate.get(Calendar.DAY_OF_WEEK) == 1 || fcaDeliveryDate.get(Calendar.DAY_OF_WEEK) == 7) daysBeforeExportForFcaDelivery++;
        }
        return DateHelper.toString(fcaDeliveryDate.getTime(),this.fcaDeliveryDateFormat);
    }

    public int getDaysToPrepareAddWeekend(int daysToPrepare){
        int daysToPrepareAddWeekend =daysToPrepare;
        Date today= new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        Calendar expectedExportDate=(Calendar) cal.clone();

        for(int i=0;i<daysToPrepareAddWeekend;i++){
            expectedExportDate.add(Calendar.DAY_OF_MONTH, 1);
            if (expectedExportDate.get(Calendar.DAY_OF_WEEK) == 1 || expectedExportDate.get(Calendar.DAY_OF_WEEK) == 7) daysToPrepareAddWeekend++;
        }

        return daysToPrepareAddWeekend;
    }

    public ShipmentIvs get(String name) {
        Assert.isTrue(this.checkAccessable(name),"Access Denied");
        return this.ivsQ.query(name);
    }

    private boolean checkAccessable(String shipmentName){

        String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
        if(Context.getCurrentUser().isDrsUser()) return true;

        String shipmentSellerKcode = this.ivsQ.querySellerKcode(shipmentName);
        if(shipmentSellerKcode==null) return false;

        if(userCompanyKcode.equals(shipmentSellerKcode)) return true;

        return false;
    }


    //=========

    private IvsSearchCondition getActualCondition(IvsSearchCondition originCondition, UserInfo user){
        if(user.isDrsUser())
            return originCondition;
        else
            return new ShipmentIvsSearchConditionImplSvc(originCondition,user.getCompanyKcode());

    }

    private void appendPaidTotalToListItem(DtoList<ShipmentIvs> list){
        List<String> shipmentNameList = this.createNameListFromShipmentList(list);
        if(shipmentNameList.size()==0) return;
        Map<String, BigDecimal> shipmentNameToPaymentTotalMap = this.dao.queryShipmentNameToPaymentTotalMap(shipmentNameList);
        for(ShipmentIvs shipment:list.getItems()){
            ShipmentIvsImpl origShp = (ShipmentIvsImpl)shipment;
            origShp.setPaymentTotal(shipmentNameToPaymentTotalMap.get(shipment.getName()));
        }
    }

    private void appendUnsName(DtoList<ShipmentIvs> list){
        List<String> shipmentNameList = this.createNameListFromShipmentList(list);
        if(shipmentNameList.size()==0) return;
        Map<String,String> shipmentNameToUnsNameMap = this.dao.queryShipmentNameToUnsNameMap(shipmentNameList);
        for(ShipmentIvs shipment:list.getItems()){
            ((ShipmentIvsImpl)shipment).setUnsName(shipmentNameToUnsNameMap.get(shipment.getName()));
        }
    }

    private List<String> createNameListFromShipmentList(DtoList<ShipmentIvs> list){
        List<String> shipmentNameList = new ArrayList<String>();
        for(ShipmentIvs shipment:list.getItems())
            shipmentNameList.add(shipment.getName());
        return shipmentNameList;
    }


    public Currency getSupplierCurrency() {
        String companyKcode = Context.getCurrentUser().getCompanyKcode();
        Assert.isTrue(this.companyDao.isSupplier(companyKcode));
        return this.currencyRepo.queryCompanyCurrency(companyKcode);
    }



    private String generateDraftShipmentName(String kcode,Integer nextDraftSerialId){
        return this.shipmentNamePrefix + "-" + kcode + "-"
                + this.embeddedShipmentNameForDraft+nextDraftSerialId.toString();
    }

    private void processShipmentLineItems(List<IvsLineItem> lineItems) {
        rearrangeLineItems(lineItems);
        numberLineItems(lineItems);
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

            lineItem.assignNumber(boxNumber,start,end);

            start = end;
        }
    }


    private BigDecimal calculateSalesTaxRate(String salesTaxPercentageStr){
        BigDecimal salesTaxPercentage = new BigDecimal(salesTaxPercentageStr);
        return salesTaxPercentage.divide(new BigDecimal("100"));
    }

    private BigDecimal calculateSubtotal(Ivs shipment, int scale) {
        BigDecimal amountUntaxed = BigDecimal.ZERO;
        for(IvsLineItem item : shipment.getLineItems()){
            amountUntaxed = amountUntaxed.add(new BigDecimal(item.getAmountUntaxed()));
        }
        return amountUntaxed.setScale(scale, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateSalesTax(BigDecimal salesTaxRate,BigDecimal amountUntaxed,int scale) {
        return amountUntaxed.multiply(salesTaxRate).setScale(scale,RoundingMode.HALF_UP);
    }


    private String generateShipmentName(String kcode,Integer nextSerialId){
        return this.shipmentNamePrefix+"-"+kcode+"-"+nextSerialId.toString();
    }

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





    private String getRequestPickupNotificationSubject(String supplierKcode, String shipmentName){
        return "Suppler "+supplierKcode+" has requested pick-up for shipment: "+shipmentName;
    }

    private String getRequestPickupNotificationMessageBody(String supplierKcode, String shipmentName){
        return this.getRequestPickupNotificationSubject(supplierKcode, shipmentName)+"\n"
                + "<br>"
                + "<br>"
                + "<a href='https://access.drs.network/InventoryShipments/"+shipmentName+"'+> Link to "+shipmentName+".</a> ";
    }



    public int getDaysToPrepare(String shippingMethod) {
        ShippingMethod method = ShippingMethod.valueOf(shippingMethod);
        return method.getDaysToPrepare();
    }

    private String getDraftShipmentName() {
        String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
        Integer serialId = this.dao.queryMaxSerialIdOfDraftShipments(userCompanyKcode);
        if (serialId == 0) serialId = 1;
        return this.generateDraftShipmentName(userCompanyKcode, serialId);
    }



    private String getRootFileDir(){
        return System.getProperty("catalina.home");
    }




    public PurchaseOrderInfo getPurchaseOrderInfo(String shipmentName) {
        return ivsQ.queryPurchaseOrderInfo(shipmentName);
    }


    public List<PurchaseOrderSkuInfo> getPurchaseOrderInfoList(String shipmentName) {
        return ivsQ.queryPurchaseOrderInfoList(shipmentName);
    }

    public ShipmentIvs.ShipmentIvsLineItem getShipmentLineItem(String ivsName , Integer boxNum ,Integer mixedBoxLineSeq) {
        return ivsQ.queryShipmentLineItem(ivsName,boxNum,mixedBoxLineSeq);
    }

}