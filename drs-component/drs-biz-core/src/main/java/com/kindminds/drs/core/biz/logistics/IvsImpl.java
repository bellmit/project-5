package com.kindminds.drs.core.biz.logistics;

import com.kindminds.drs.Context;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.row.logistics.FreightFeeRateRow;
import com.kindminds.drs.api.data.row.logistics.IvsLineitemRow;
import com.kindminds.drs.api.data.row.logistics.IvsRow;
import com.kindminds.drs.api.data.row.logistics.IvsShippingCostsRow;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.rdb.logistics.IvsDao;
import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShipmentIvsDao;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.*;
import com.kindminds.drs.api.v2.biz.domain.model.request.SaveIvsLineitemRequest;
import com.kindminds.drs.api.v2.biz.domain.model.request.SaveIvsRequest;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.v1.model.impl.logistics.ShippingCostDataImpl;
import com.kindminds.drs.api.v1.model.logistics.ShippingCostData;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class IvsImpl implements Ivs {


    private String shipmentNamePrefix = "IVS";
    private String embeddedShipmentNameForDraft = "DRAFT";

    private CompanyDao companyDao = SpringAppCtx.get().getBean(CompanyDao.class);

    private CurrencyDao currencyDao = SpringAppCtx.get().getBean(CurrencyDao.class);


    //todo need refactoring ShipmentIvs to here
    private MaintainShipmentIvsDao ivsDao = SpringAppCtx.get().getBean(MaintainShipmentIvsDao.class);

    //todo arthur need refactor
    private IvsDao dao = SpringAppCtx.get().getBean(IvsDao.class);

    private Integer id;
   
    private String name;
   
    private BigDecimal salesTaxRate;
   
    private BigDecimal subtotal;
   
    private BigDecimal salesTax;
   
    private BigDecimal total;
   
    private Currency handlerCurrency;
   
    private Integer serialId;
   
    private ShipmentType type;
   
    private String expectedExportDate;
   
    private String fcaDeliveryLocationId;
   
    private String fcaDeliveryDate;
   
    private String specialRequest;
   
    private String boxesNeedRepackaging;
   
    private String repackagingFee;
   
    private Boolean requiredPO;
   
    private String poNumber;
   
    private String invoiceNumber;
   
    private String sellerCompanyKcode;
   
    private String buyerCompanyKcode;
   
    private Currency currency;
   
    private String salesTaxPercentage;
   
    private ShipmentStatus status;
   
    private String destinationCountry;
   
    private ShippingMethod shippingMethod;
   
    private OffsetDateTime dateCreated;
   
    private BigDecimal paidTotal;
   
    private String unsName;
   
    private String datePurchased;
   
    private String internalNote;
   
    private BigDecimal freightFeeOriginal = BigDecimal.ZERO;
   
    private BigDecimal freightFeeNonMerged = BigDecimal.ZERO;

    private int freightFeeRateIdOriginal;
    private int freightFeeRateIdNonMerged;
   
    private BigDecimal truckCost = BigDecimal.ZERO;
   
    private BigDecimal inventoryPlacementFee = BigDecimal.ZERO;
   
    private BigDecimal hsCodeFee = BigDecimal.ZERO;
   
    private BigDecimal otherExpense = BigDecimal.ZERO;
   
    private BigDecimal shippingCostOriginal = BigDecimal.ZERO;
   
    private BigDecimal shippingCostNonMerged = BigDecimal.ZERO;
   
    private List<IvsLineItem> lineItems;


    private static final String TO_BE_CONFIRMED = "TO_BE_CONFIRMED";
    private static final String INVALID = "INVALID";
    private static final String CONFIRMED = "CONFIRMED";

   // public static final IvsImpl.Companion Companion = new IvsImpl.Companion((DefaultConstructorMarker)null);

   
    public String getName() {
        return this.name;
    }

   
    public BigDecimal getSalesTaxRate() {
        return this.salesTaxRate;
    }

   
    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

   
    public BigDecimal getSalesTax() {
        return this.salesTax;
    }

   
    public BigDecimal getTotal() {
        return this.total;
    }

   
    public Currency getHandlerCurrency() {
        return this.handlerCurrency;
    }

   
    public Integer getSerialId() {
        return this.serialId;
    }

   
    public ShipmentType getType() {
        return this.type;
    }

   
    public String getExpectedExportDate() {
        return this.expectedExportDate;
    }

   
    public String getFcaDeliveryLocationId() {
        return this.fcaDeliveryLocationId;
    }

   
    public String getFcaDeliveryDate() {
        return this.fcaDeliveryDate;
    }

   
    public String getSpecialRequest() {
        return this.specialRequest;
    }

   
    public String getBoxesNeedRepackaging() {
        return this.boxesNeedRepackaging;
    }

   
    public String getRepackagingFee() {
        return this.repackagingFee;
    }

   
    public Boolean getRequiredPO() {
        return this.requiredPO;
    }

   
    public String getPoNumber() {
        return this.poNumber;
    }

   
    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

   
    public String getSellerCompanyKcode() {
        return this.sellerCompanyKcode;
    }

   
    public String getBuyerCompanyKcode() {
        return this.buyerCompanyKcode;
    }

   
    public Currency getCurrency() {
        return this.currency;
    }

   
    public String getSalesTaxPercentage() {
        return this.salesTaxPercentage;
    }

   
    public ShipmentStatus getStatus() {
        return this.status;
    }

   
    public String getDestinationCountry() {
        return this.destinationCountry;
    }

   
    public ShippingMethod getShippingMethod() {
        return this.shippingMethod;
    }

   
    public OffsetDateTime getDateCreated() {
        return this.dateCreated;
    }

   
    public BigDecimal getPaidTotal() {
        return this.paidTotal;
    }

   
    public String getUnsName() {
        return this.unsName;
    }

   
    public String getDatePurchased() {
        return this.datePurchased;
    }

   
    public String getInternalNote() {
        return this.internalNote;
    }

   
    public BigDecimal getFreightFeeOriginal() {
        return this.freightFeeOriginal;
    }

   
    public BigDecimal getFreightFeeNonMerged() {
        return this.freightFeeNonMerged;
    }

    public int getFreightFeeRateIdOriginal() {
        return this.freightFeeRateIdOriginal;
    }

    public int getFreightFeeRateIdNonMerged() {
        return this.freightFeeRateIdNonMerged;
    }

   
    public BigDecimal getTruckCost() {
        return this.truckCost;
    }

    @Override
    public BigDecimal getInventoryPlacementFee() {
        return this.inventoryPlacementFee;
    }


    public BigDecimal getHsCodeFee() {
        return this.hsCodeFee;
    }

   
    public BigDecimal getOtherExpense() {
        return this.otherExpense;
    }

   
    public BigDecimal getShippingCostOriginal() {
        return this.shippingCostOriginal;
    }

   
    public BigDecimal getShippingCostNonMerged() {
        return this.shippingCostNonMerged;
    }

   
    public List<IvsLineItem> getLineItems() {
        return this.lineItems;
    }

    public void setLineItems( List var1) {
        this.lineItems = var1;
    }



    public IvsImpl(){}



    public IvsImpl (IvsRow ivsRow ,List<IvsLineitemRow> lineItemRows ) {

        this.id = ivsRow.getId();
        this.name = ivsRow.getName();

       this.type = ShipmentType.fromValue(ivsRow.getType());

        this.expectedExportDate  = ivsRow.getExpectedExportDate();

        this.fcaDeliveryLocationId = ivsRow.getShipmentFcaLocationId();
        this.fcaDeliveryDate = ivsRow.getFcaDeliveryDate();

        this.invoiceNumber = ivsRow.getInvoiceNumber() == null ? "" : ivsRow.getInvoiceNumber();
        this.sellerCompanyKcode = ivsRow.getSellerCompanyKcode() == null ? "" : ivsRow.getSellerCompanyKcode();
        this.buyerCompanyKcode = ivsRow.getBuyerCompanyKcode() == null ? "" : ivsRow.getBuyerCompanyKcode();

        this.currency = Currency.valueOf(ivsRow.getCurrencyName());

        this.salesTaxRate = ivsRow.getSalesTaxRate();

        this.subtotal = ivsRow.getSubtotal();

        this.salesTax = ivsRow.getAmountTax();

        this.total = ivsRow.getAmountTotal();

        this.destinationCountry = ivsRow.getDestinationCountryCode();

        this.shippingMethod = ShippingMethod.valueOf(ivsRow.getShippingMethod());

        this.dateCreated = ivsRow.getDateCreated();

        this.datePurchased = ivsRow.getDatePurchased();

        this.internalNote = ivsRow.getInternalNote() == null ? "" : ivsRow.getInternalNote();
        this.specialRequest = ivsRow.getSpecialRequest();

        this.boxesNeedRepackaging = ivsRow.getNumOfRepackaging();

        this.repackagingFee = ivsRow.getRepackagingFee();

        this.requiredPO = ivsRow.getRequiredPo();

        this.poNumber = ivsRow.getPoNumber();

         this.status = ShipmentStatus.valueOf(ivsRow.getStatus());

         if(lineItemRows != null){
             this.lineItems = new ArrayList<IvsLineItem>();
             if(lineItemRows.size() > 0){
                 lineItemRows.forEach(x->{
                     this.lineItems.add(new IvsLineItemImpl(this.name,x));
                 });
             }

         }

        this.paidTotal =  dao.queryShipmentPaymentTotal(name);

    }

    /*
    fun changeNote(){
        this.internalNote = "123456789"
    }*/

    @Override
    public void processDraft(SaveIvsRequest saveIvsDraftRequest) {

        String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();

        //todo arthur refactor to repo
        String handlerKcode = this.companyDao.queryHandlerKcode(userCompanyKcode);

        processDraftForKcode(saveIvsDraftRequest, userCompanyKcode, handlerKcode);


    }

    @Override
    public void processDraftForKcode(SaveIvsRequest saveIvsDraftRequest,
                                     String userCompanyKcode, String handlerKcode) {

        load(saveIvsDraftRequest);

        sellerCompanyKcode = userCompanyKcode;
        buyerCompanyKcode = handlerKcode;

        int scale = this.getCurrencyScale(handlerKcode);

        salesTaxRate = this.calculateSalesTaxRate(saveIvsDraftRequest.getSalesTaxPercentage());
        subtotal = this.calculateSubtotal(saveIvsDraftRequest, scale);
        salesTax =this.calculateSalesTax(salesTaxRate,subtotal,scale);
        total = subtotal.add(salesTax);
        status = ShipmentStatus.SHPT_DRAFT;

        processShipmentLineItems();

        this.serialId = this.generateDraftSerialId(userCompanyKcode );
        this.name = this.generateDraftShipmentName( userCompanyKcode ,
                this.serialId);
    }

    @Override
    public void update(SaveIvsRequest saveIvsRequest) {

        this.load(saveIvsRequest);

        if (status == ShipmentStatus.SHPT_DRAFT
                || status == ShipmentStatus.SHPT_AWAIT_PLAN
                || status == ShipmentStatus.SHPT_PLANNING
                || status == ShipmentStatus.SHPT_INITIAL_VERIFIED) {

            String handlerKcode = this.dao.queryBuyerKcode(saveIvsRequest.getName());
            int scale = this.getCurrencyScale(handlerKcode);

             salesTaxRate = this.calculateSalesTaxRate(saveIvsRequest.getSalesTaxPercentage());
             subtotal = this.calculateSubtotal(saveIvsRequest, scale);
             salesTax = this.calculateSalesTax(salesTaxRate, subtotal, scale);
             total = subtotal.add(salesTax);

            if(saveIvsRequest.getRequiredPO() !=null) poNumber = saveIvsRequest.getPONumber();

            processShipmentLineItems();

            if (status != ShipmentStatus.SHPT_DRAFT) {
                this.validateLineitemStatus();
            }


        } else {
            this.status = saveIvsRequest.getStatus();
            Assert.isTrue(this.status != ShipmentStatus.SHPT_DRAFT
                    && status != ShipmentStatus.SHPT_AWAIT_PLAN);

        }

    }


    private void load(SaveIvsRequest r){

        this.type = r.getType();
        this.expectedExportDate = r.getExpectedExportDate();
        this.fcaDeliveryLocationId = r.getFcaDeliveryLocationId();
        this.fcaDeliveryDate = r.getFcaDeliveryDate();
        this.specialRequest = r.getSpecialRequest();
        this.boxesNeedRepackaging = r.getBoxesNeedRepackaging();
        this.repackagingFee = r.getRepackagingFee();
        this.requiredPO = r.getRequiredPO();
        this.poNumber = r.getPONumber();
        this.invoiceNumber = r.getInvoiceNumber();
        this.sellerCompanyKcode = r.getSellerCompanyKcode();
        this.buyerCompanyKcode = r.getBuyerCompanyKcode();
        this.currency = r.getCurrency();
        this.salesTaxPercentage = r.getSalesTaxPercentage();

        this.destinationCountry = r.getDestinationCountry();
        this.shippingMethod = r.getShippingMethod();
        this.paidTotal = StringUtils.isNotEmpty(r.getPaidTotal())  ? new BigDecimal(r.getPaidTotal()) : BigDecimal.ZERO;
        this.unsName = r.getUnsName();
        this.datePurchased  = r.getDatePurchased();
        this.internalNote = r.getInternalNote();
        this.dateCreated = OffsetDateTime.now();



        this.processLineitemStatus(r.getLineItem());

    }

    private int getCurrencyScale(String kcode) {

        this.handlerCurrency = this.currencyDao.queryCompanyCurrency(kcode);

        int scale = handlerCurrency.getScale();

        return scale;

    }

    private int generateDraftSerialId(String userCompanyKcode )  {
        int serialId = 1 + this.dao.queryMaxSerialIdOfDraftShipments(userCompanyKcode);
        return serialId;
    }

    @Override
    public void changeStatus(ShipmentStatus status) {
        this.status = status;
    }

    @Override
     public void submit(String name) {

        //todo arthur need refactor
        String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
        int nextSerialId = this.dao.queryMaxSerialIdOfNonDraftShipments(userCompanyKcode) + 1;
        String ivsName = this.generateShipmentName(userCompanyKcode, nextSerialId);

        this.dao.updateStatus(name, ShipmentStatus.SHPT_AWAIT_PLAN);

        this.dao.updateNameAndSerialId(name, ivsName, nextSerialId);

        this.dao.setPickupRequsterForShipment(ivsName, Context.getCurrentUser().getUserId());

        this.name = ivsName;

    }

    @Override
    public String accept(String name) {

        this.dao.updateStatus(name, ShipmentStatus.SHPT_FC_BOOKING_CONFIRM);

        return name;
    }

    @Override
     public String confirm(String name) {

        calculateShippingCosts();

        this.dao.deleteShippingCost(name);
        this.dao.insertShippingCost(name, freightFeeOriginal, freightFeeNonMerged,
                freightFeeRateIdOriginal, freightFeeRateIdNonMerged, truckCost, inventoryPlacementFee,
                hsCodeFee, otherExpense, shippingCostOriginal, shippingCostNonMerged);

        this.dao.updateStatus(name, ShipmentStatus.SHPT_CONFIRMED);

        Date now = new Date();
        this.dao.updatePurchasedDate(name, now);
        this.dao.updateDateConfirmed(name, now);
        this.dao.setConfirmor(name, Context.getCurrentUser().getUserId());

        return name;

    }

    @Override
    public boolean isAllInitiallyVerified() {

        for (IvsLineItem it: this.lineItems) {
            if(it.getProductVerificationStatus() != IvsLineitemProductVerificationStatus.TO_BE_CONFIRMED){
                if(it.getProductVerificationStatus() != IvsLineitemProductVerificationStatus.CONFIRMED){
                    return false ;
                }
            }
        }

        return true;
    }

    @Override
    public boolean isAllConfirmed() {

        for (IvsLineItem it: this.lineItems) {
            if(it.getProductVerificationStatus() != IvsLineitemProductVerificationStatus.CONFIRMED){
                return false;
            }
        }


        return true;
    }




    //=====
    //encapsulation method from uco

    private String generateDraftShipmentName(String kcode,int nextDraftSerialId) {
        return (this.shipmentNamePrefix + "-" + kcode + "-"
                + this.embeddedShipmentNameForDraft + nextDraftSerialId);
    }



    private String generateShipmentName(String kcode, int nextSerialId) {
        return this.shipmentNamePrefix+"-"+kcode+"-"+nextSerialId;
    }



    private BigDecimal calculateSalesTaxRate(String salesTaxPercentageStr)  {

      if(salesTaxPercentageStr != null){
          BigDecimal salesTaxPercentage = new BigDecimal(salesTaxPercentageStr);
          return salesTaxPercentage.divide(new BigDecimal("100"));
      }else{
          return new BigDecimal(0);
      }
    }

    private BigDecimal calculateSubtotal(SaveIvsRequest request, int scale)  {

        BigDecimal amountUntaxed = BigDecimal.ZERO;
        for (SaveIvsLineitemRequest item : request.getLineItem()) {
            if(StringUtils.isNotEmpty(item.getAmountUntaxed())){
                amountUntaxed = amountUntaxed.add(new BigDecimal(item.getAmountUntaxed()));
            }
        }

        return amountUntaxed.setScale(scale, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateSalesTax(BigDecimal salesTaxRate,BigDecimal amountUntaxed,int scale) {
        return amountUntaxed.multiply(salesTaxRate).setScale(scale, RoundingMode.HALF_UP);
    }


    private void processShipmentLineItems() {

        List<IvsLineItem> r =   rearrangeLineItems();
        List<IvsLineItem> n  =   numberLineItems(r);

      //  this.processLineitemStatus(newlineItems)

        this.lineItems = n;

    }

    void processLineitemStatus( List<SaveIvsLineitemRequest> saveIvsLineitemRequestList ){

            List<IvsLineItem> newlineItems  = new ArrayList<IvsLineItem>();
            saveIvsLineitemRequestList.forEach(it-> {
                IvsLineItemImpl ivsLineItem = new IvsLineItemImpl(this.name,it);
                newlineItems.add(ivsLineItem);
            });

            newlineItems.forEach(it -> {
                IvsLineItem x = it;

                if(x.getId() > 0){
                    List<IvsLineItem> item = this.lineItems.stream().filter(it2 ->
                        it2.getId() == x.getId()
                    ).collect(Collectors.toList());

                    if(item.size() > 0){
                        x.updateProductVerificationStatus(item.get(0).getProductVerificationStatus());
                    }
                }
            });

            this.lineItems = newlineItems;

    }

    //todo arthur need refactoring
    private List<IvsLineItem> rearrangeLineItems()  {


        List<IvsLineItem> mixedBoxItems = new ArrayList<IvsLineItem>();
        Iterator<IvsLineItem> iterator = this.lineItems.iterator();
        while (iterator.hasNext()) {
            IvsLineItem nextItem = iterator.next();
            if (nextItem.getMixedBoxLineSeq() != 0) {
                if(StringUtils.isNotEmpty(nextItem.getSkuCode())){
                    mixedBoxItems.add(nextItem);
                    iterator.remove();
                }
            }
        }

        Collections.sort(mixedBoxItems,
                Comparator.comparing(IvsLineItem::getBoxNum)
                        .thenComparing(IvsLineItem::getMixedBoxLineSeq));
        Collections.sort(lineItems,
                (IvsLineItem o1, IvsLineItem o2)->o1.getSkuCode().compareTo(o2.getSkuCode()));

        List<IvsLineItem> newItems = lineItems;
        newItems.addAll(mixedBoxItems);

        return newItems;

    }





    /*
    private fun rearrangeLineItems(lineItems: MutableList<ShipmentIvsLineItem>) {
        val mixedBoxItems = ArrayList<ShipmentIvsLineItem>()
        val iterator = lineItems.iterator()
        while (iterator.hasNext()) {
            val nextItem = iterator.next()
            if (nextItem.mixedBoxLineSeq != 0) {
                mixedBoxItems.add(nextItem)
                iterator.remove()
            }
        }


        Collections.sort(lineItems
        ) { o1: ShipmentIvsLineItem, o2: ShipmentIvsLineItem -> o1.skuCode.compareTo(o2.skuCode) }
        lineItems.addAll(mixedBoxItems)
    }
    */

    private List<IvsLineItem> numberLineItems(List<IvsLineItem> lineItems )   {
        int boxNumber = 0;
        int start = 0;
        int end = 0;

        for (IvsLineItem lineItem : lineItems) {

            if(StringUtils.isNotEmpty(lineItem.getSkuCode())){
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

        return lineItems;
    }

    private void validateLineitemStatus(){

        long verified = this.lineItems.stream()
                .filter(it -> (it.getProductVerificationStatus() == IvsLineitemProductVerificationStatus.TO_BE_CONFIRMED)
                        ).count();


        long confirmed = this.lineItems.stream()
                .filter(it -> (it.getProductVerificationStatus() == IvsLineitemProductVerificationStatus.CONFIRMED))
                .count();

        long actual = this.lineItems.stream()
                .filter(it -> (it.getSkuCode().trim() != ""))
                .count();


        if (confirmed ==  actual) {

            this.status = ShipmentStatus.SHPT_FC_BOOKING_CONFIRM;

        } else if (verified + confirmed == actual) {

            this.status = ShipmentStatus.SHPT_INITIAL_VERIFIED;

        } else {
            this.status = ShipmentStatus.SHPT_AWAIT_PLAN;
        }

    }

    /*
    Below: Related to Calculate Shipping Cost
 */
    @Override
    public String getShippingCosts(String shipmentName) {

        IvsShippingCostsRow shippingCosts = (IvsShippingCostsRow) dao.queryShippingCosts(shipmentName);

        this.freightFeeOriginal = shippingCosts.getFreightFeeOriginal();
        this.freightFeeNonMerged = shippingCosts.getFreightFeeNonMerged();
        this.freightFeeRateIdOriginal = shippingCosts.getFreightFeeRateIdOriginal();
        this.freightFeeRateIdNonMerged = shippingCosts.getFreightFeeRateIdNonMerged();
        this.truckCost = shippingCosts.getTruckCost();
        this.inventoryPlacementFee = shippingCosts.getInventoryPlacementFee();
        this.hsCodeFee = shippingCosts.getHsCodeFee();
        this.otherExpense = shippingCosts.getOtherExpense();
        this.shippingCostOriginal = shippingCosts.getShippingCostOriginal();
        this.shippingCostNonMerged = shippingCosts.getShippingCostNonMerged();
        BigDecimal fcaRelatedCost = shippingCostOriginal
                .subtract(freightFeeOriginal)
                .subtract(truckCost)
                .subtract(inventoryPlacementFee)
                .subtract(hsCodeFee)
                .subtract(otherExpense);

        return getShippingCostsAsString(fcaRelatedCost);
    }

    private String getShippingCostsAsString(BigDecimal fcaRelatedCost) {
        return ("freightFeeOriginal: " + freightFeeOriginal.setScale(2).toPlainString() + "<br>"
                + "freightFeeNonMerged: " + freightFeeNonMerged.setScale(2).toPlainString() + "<br>"
                + "freightFeeRateIdOriginal: " + freightFeeRateIdOriginal + "<br>"
                + "freightFeeRateIdNonMerged: " + freightFeeRateIdNonMerged + "<br>"
                + "truckCost: " + truckCost.setScale(2).toPlainString() + "<br>"
                + "fcaRelatedCost: " + fcaRelatedCost.setScale(2).toPlainString() + "<br>"
                + "inventoryPlacementFee: " + inventoryPlacementFee.setScale(2).toPlainString() + "<br>"
                + "hsCodeFee: " + hsCodeFee.setScale(2).toPlainString() + "<br>"
                + "otherExpense: " + otherExpense.setScale(2).toPlainString() + "<br>"
                + "shippingCostOriginal: " + shippingCostOriginal.setScale(2).toPlainString() + "<br>"
                + "shippingCostNonMerged: " + shippingCostNonMerged.setScale(2).toPlainString() + "<br>");
    }

    private void calculateShippingCosts() {

        String countryCode = checkForEUdestination(this.destinationCountry);
        List<ShippingCostData>  shippingData = getShippingCostData(countryCode);

        this.hsCodeFee = calculateHSCodeFee(countryCode, shippingData);
        BigDecimal fcaRelatedCost = getFCARelatedCost();
        this.inventoryPlacementFee = getInventoryPlacementFee(countryCode,
                this.lineItems, shippingData);

        BigDecimal chargeableWeight = getChargeableWeight(this.shippingMethod,this.lineItems);

        boolean isMergedShipment = isMergedShipment(shippingData);

        calculateShippingCostOriginal(countryCode, fcaRelatedCost, chargeableWeight, isMergedShipment);

        calculateShippingCostNonMerged(countryCode, fcaRelatedCost, chargeableWeight);
    }

    public String checkForEUdestination(String destination) {
        if (destination.toUpperCase().equals("EU")) {
           return "UK";
        } else return destination;
    }

    public List<ShippingCostData> getShippingCostData(String countryCode) {

        List<ShippingCostData> shippingData = new ArrayList<ShippingCostData>();

        for (IvsLineItem item : this.lineItems) {
            ShippingCostData shippingCostData =
                    getShippingCostDataPerLine(countryCode, item);
            if(shippingCostData != null)
                shippingData.add(shippingCostData);
        }

        return shippingData;
    }

   public ShippingCostData getShippingCostDataPerLine(String countryCode,IvsLineItem item) {

        Object[] skuData = ivsDao.queryShippingCostData(item.getProductBaseCode(), countryCode);
        if (skuData == null) {
            //logger.info("ERROR: IvsProductVerifierImpl")
            //logger.info("No dpim json data found for " + item.skuCode + ", country: " + countryCode)
            return null;
        }


        String hsCode = skuData[0].toString();

        BigDecimal weight = doBigDecimal(skuData[1]);

        String weightUnit = skuData[2].toString();
        BigDecimal dim1 = doBigDecimal(skuData[3]);
        BigDecimal dim2 = doBigDecimal(skuData[4]);
        BigDecimal dim3 = doBigDecimal(skuData[5]);
        String dimUnit = skuData[6].toString();

        return new ShippingCostDataImpl(item.getSkuCode(),
                countryCode, hsCode, weight, weightUnit, dim1, dim2, dim3, dimUnit , item.getProductBaseCode());
    }

    private BigDecimal doBigDecimal(Object data )  {
        if(StringUtils.isEmpty(data.toString())) {
            return BigDecimal.ZERO;
        }
        return  new BigDecimal((data.toString()).trim());
    }

    private void calculateShippingCostOriginal(String countryCode, BigDecimal fcaRelatedCost,
                                               BigDecimal chargeableWeight, Boolean isMergedShipment) {

        FreightFeeRateRow freightFeeRateOriginal = getFreightFeeRate(
                this.shippingMethod, countryCode,
                isMergedShipment, chargeableWeight);

        this.freightFeeRateIdOriginal = freightFeeRateOriginal.getId();

        this.freightFeeOriginal = getFreightFee(chargeableWeight, freightFeeRateOriginal.getFreightFeeRate());


        this.shippingCostOriginal = truckCost
                .add(hsCodeFee)
                .add(fcaRelatedCost)
                .add(freightFeeOriginal)
                .add(inventoryPlacementFee).setScale(0, RoundingMode.UP)
                .add(otherExpense);

    }

    private void calculateShippingCostNonMerged(String countryCode, BigDecimal fcaRelatedCost,
                                                BigDecimal  chargeableWeight) {

        FreightFeeRateRow freightFeeRateNonMerged = getFreightFeeRate(
                this.shippingMethod, countryCode,
                false, chargeableWeight);
        this.freightFeeRateIdNonMerged = freightFeeRateNonMerged.getId();

        this.freightFeeNonMerged = getFreightFee(chargeableWeight, freightFeeRateNonMerged.getFreightFeeRate());

        this.shippingCostNonMerged = truckCost
                .add(hsCodeFee)
                .add(fcaRelatedCost)
                .add(freightFeeNonMerged)
                .add(inventoryPlacementFee).setScale(0, RoundingMode.UP)
                .add(otherExpense);

    }


    private BigDecimal calculateHSCodeFee(String countryCode, List<ShippingCostData> shippingData) {
        if (countryCode.toUpperCase().equals("CA") ||
                countryCode.toUpperCase().equals("UK")) {
            return BigDecimal.ZERO;
        }
        if (this.fcaDeliveryLocationId.equals("4")) {
            return BigDecimal.ZERO;
        }
        if (this.shippingMethod == ShippingMethod.EXPRESS) {
            return BigDecimal.ZERO;
        }

        HashSet pricesFCA = new HashSet<String>();
        HashSet codesHS = new HashSet<String>();
        HashSet skus = new HashSet<String>();
        List<IvsLineItem> shipmentItems = this.lineItems;

        for (int i = 0; i < this.lineItems.size() ; i++) {
            skus.add(shipmentItems.get(i).getSkuCode());
            codesHS.add(shippingData.get(i).getHsCode());
            pricesFCA.add(shipmentItems.get(i).getUnitAmount());
        }

        int count = 1;
        if (codesHS.size() > 1 && pricesFCA.size() > 1) {
            count = skus.size();
        } else if (codesHS.size() > 1) {
            count = codesHS.size();
        } else if (pricesFCA.size() > 1) {
            count = pricesFCA.size();
        }
        return new BigDecimal(264 * count);
    }

    private BigDecimal getFCARelatedCost()  {

        if (this.fcaDeliveryLocationId == "4") {
          return   BigDecimal.ZERO ;
        } else return getInsuranceFee(this.shippingMethod, this.lineItems)
                .setScale(0, RoundingMode.HALF_UP);
    }

    private BigDecimal getInsuranceFee(ShippingMethod shippingMethod,
                                       List<IvsLineItem> lineItems) {

        BigDecimal insuranceFee = new BigDecimal(400);
        BigDecimal sumAmount = BigDecimal.ZERO;

        if(lineItems != null){
            if (shippingMethod == ShippingMethod.EXPRESS) {
                for (IvsLineItem item : lineItems) {
                    sumAmount = sumAmount.add(
                            new BigDecimal(item.getUnitAmount())
                                    .multiply(new BigDecimal(item.getQuantity()))
                                    .multiply(new BigDecimal(0.0017))
                                    .setScale(6, RoundingMode.HALF_UP));
                }
                if (insuranceFee.compareTo(sumAmount) > 0) {
                    return insuranceFee;
                }
            } else if (shippingMethod == ShippingMethod.AIR_CARGO || shippingMethod == ShippingMethod.SEA_FREIGHT) {

                for (IvsLineItem item : lineItems) {
                    sumAmount = sumAmount.add(
                           new BigDecimal(item.getUnitAmount())
                                    .multiply(new BigDecimal(item.getQuantity()))
                                    .multiply(new BigDecimal(0.0007))
                                    .setScale(6, RoundingMode.HALF_UP));
                }
            }

        }

        return sumAmount;
    }

    private BigDecimal getFreightFee(BigDecimal chargeableWeight, BigDecimal freightFeeRate) {

        if (freightFeeRate == null) {
            return BigDecimal.ZERO;
        }

        if (this.fcaDeliveryLocationId.equals("4")) {
            return BigDecimal.ZERO;
        }

        return chargeableWeight.multiply(freightFeeRate).setScale(0, RoundingMode.HALF_UP);
    }

    private FreightFeeRateRow getFreightFeeRate(ShippingMethod shippingMethod, String destination,
                                                Boolean  msFlag, BigDecimal chargeableWeight) {
        if (destination.equals("UK")) {
            destination = "EU";
        }

        return dao.queryFreightFeeRate(destination, shippingMethod, msFlag, chargeableWeight);
    }

    private BigDecimal getChargeableWeight(ShippingMethod shippingMethod,List<IvsLineItem> lineItems) {

        BigDecimal ivsGrossWeight = calculateIVSGrossWeight(shippingMethod, lineItems);
        BigDecimal ivsVolumeWeight = calculateIVSVolumeWeight(shippingMethod, lineItems);

        if (ivsGrossWeight.compareTo(ivsVolumeWeight) > 0)
            return  ivsGrossWeight ;
        else
            return ivsVolumeWeight;
    }

    private BigDecimal calculateIVSGrossWeight(ShippingMethod shippingMethod,
                                        List<IvsLineItem> lineItems) {
        BigDecimal ivsGrossWeight = BigDecimal.ZERO;

        if(lineItems != null){
            for (IvsLineItem item : lineItems) {
                BigDecimal lineGW = calculateBoxGrossWeight(
                        shippingMethod,new BigDecimal(item.getPerCartonGrossWeightKg()))
                        .multiply(new BigDecimal(item.getCartonCounts()));
                ivsGrossWeight = ivsGrossWeight.add(lineGW);
            }
        }

        return ivsGrossWeight;
    }

    private BigDecimal calculateBoxGrossWeight(ShippingMethod shippingMethod, BigDecimal cartonGrossWeight) {
        if (shippingMethod == ShippingMethod.EXPRESS) {
            return cartonGrossWeight.divide(new BigDecimal(0.5), 0, RoundingMode.UP)
                    .multiply(new BigDecimal(0.5));
        } else if (shippingMethod == ShippingMethod.AIR_CARGO || shippingMethod == ShippingMethod.SEA_FREIGHT) {
            return cartonGrossWeight;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateIVSVolumeWeight(ShippingMethod shippingMethod,
                                                List<IvsLineItem> lineItems) {
        BigDecimal ivsVolumeWeight = BigDecimal.ZERO;
        BigDecimal volumeWeightCoefficient = getVolumeWeightCoefficient(shippingMethod);

      if(lineItems != null){

          if (shippingMethod == ShippingMethod.EXPRESS) {
              for (IvsLineItem item : lineItems) {
                  BigDecimal boxVolumeWeight = calculateBoxCBM(item)
                          .multiply(new BigDecimal(1000000))
                          .divide(volumeWeightCoefficient, 8, RoundingMode.HALF_UP)
                          .divide(new BigDecimal(0.5), 0, RoundingMode.UP)
                          .multiply(new BigDecimal(0.5));
                  BigDecimal lineVolumeWeight = boxVolumeWeight
                          .multiply(new BigDecimal(item.getCartonCounts()));
                  ivsVolumeWeight = ivsVolumeWeight.add(lineVolumeWeight);
              }
          } else if (shippingMethod == ShippingMethod.AIR_CARGO || shippingMethod == ShippingMethod.SEA_FREIGHT) {
              for (IvsLineItem item : lineItems) {
                  BigDecimal lineCBM = calculateBoxCBM(item)
                          .multiply(new BigDecimal(item.getCartonCounts()));
                  ivsVolumeWeight = ivsVolumeWeight.add(lineCBM);
              }
              ivsVolumeWeight = ivsVolumeWeight.multiply(new BigDecimal(1000000))
                      .divide(volumeWeightCoefficient, 1, RoundingMode.UP);
          }
      }

        return ivsVolumeWeight;
    }

    public BigDecimal calculateBoxCBM(IvsLineItem lineItem) {
        BigDecimal dim1 = new BigDecimal(lineItem.getCartonDimensionCm1());
        BigDecimal dim2 = new BigDecimal(lineItem.getCartonDimensionCm2());
        BigDecimal dim3 = new BigDecimal(lineItem.getCartonDimensionCm3());
        return dim1.multiply(dim2).multiply(dim3)
                .divide(new BigDecimal(1000000), 3, RoundingMode.HALF_UP);
    }

    private BigDecimal getVolumeWeightCoefficient(ShippingMethod shippingMethod) {
        if (shippingMethod == ShippingMethod.EXPRESS) {
            return new BigDecimal(5000);
        } else if (shippingMethod == ShippingMethod.AIR_CARGO || shippingMethod == ShippingMethod.SEA_FREIGHT) {
            return new BigDecimal(6000);
        }
        return BigDecimal.ZERO;
    }

    private Boolean isMergedShipment(List<ShippingCostData> shippingData ) {
        //If containsOversize is TRUE or containsDGR is True, MS_flag is False. Otherwise is True.
        return !(containsOversized(shippingData) || containsDangerousGoods(shippingData));
    }

    private Boolean containsOversized( List<ShippingCostData> shippingData) {
        for (ShippingCostData item : shippingData) {
            if(item != null){
                if (isOversize(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isOversize(ShippingCostData item) {

        if(item != null){
            String country = item.getCountryCode();
            BigDecimal packageDimLong = item.getPackageDimLong();
            BigDecimal packageDimMedium = item.getPackageDimMedium();
            BigDecimal packageDimShort = item.getPackageDimShort();

            if ("US".equals(country.toUpperCase())) {
                if ("cm".equals(item.getPackageDimUnit().toUpperCase())) {
                    packageDimLong = packageDimLong.multiply(new BigDecimal(0.3937));
                    packageDimMedium = packageDimMedium.multiply(new BigDecimal(0.3937));
                    packageDimShort = packageDimShort.multiply(new BigDecimal(0.3937));
                }

                return (packageDimLong.compareTo(new BigDecimal(18)) > 0
                        || packageDimMedium.compareTo(new BigDecimal(14)) > 0
                        || packageDimShort.compareTo(new BigDecimal(8)) > 0);

            } else if ("UK".equals(country.toUpperCase()) || "CA".equals(country.toUpperCase())) {
                if ("in".equals(item.getPackageDimUnit().toLowerCase())) {
                    packageDimLong = packageDimLong.divide(new BigDecimal(0.3937), 6, RoundingMode.HALF_UP);
                    packageDimMedium = packageDimMedium.divide(new BigDecimal(0.3937), 6, RoundingMode.HALF_UP);
                    packageDimShort = packageDimShort.divide(new BigDecimal(0.3937), 6, RoundingMode.HALF_UP);
                }

                if ("UK".equals(country.toUpperCase())) {
                    return  (packageDimLong.compareTo(new BigDecimal(45)) > 0
                            || packageDimMedium.compareTo(new BigDecimal(34)) > 0
                            || packageDimShort.compareTo(new BigDecimal(26)) > 0);

                } else {

                    return (packageDimLong.compareTo(new BigDecimal(45)) > 0
                            || packageDimMedium.compareTo(new BigDecimal(35)) > 0
                            || packageDimShort.compareTo(new BigDecimal(20)) > 0);
                }
            }
        }

        return false;
    }

    private Boolean containsDangerousGoods( List<ShippingCostData> shippingData)  {
        //    	If battery type is Lithium ion or Lithium metal, IsDGR is TRUE, others is FALSE
        for (ShippingCostData item : shippingData) {
            if (isDangerousGoods(item.getProductBaseCode())) {
                return true;
            }
        }
        return false;
    }


    public boolean isDangerousGoods(String prodcutBaseCode) {

        String batteryType = ivsDao.queryBatteryType(prodcutBaseCode);
        return "LithiumIon".toUpperCase().equals(batteryType.toUpperCase()) ||
                "LithiumMetal".toUpperCase().equals(batteryType.toUpperCase());
    }


    BigDecimal getInventoryPlacementFee(String destination,
                                 List<IvsLineItem> lineItems, List<ShippingCostData> shippingData) {

        BigDecimal inventoryPlacementFee = BigDecimal.ZERO;

        if(lineItems != null){

            for (int i = 0; i < lineItems.size(); i++) {
                if(shippingData.size() - 1 >= i){
                    inventoryPlacementFee = inventoryPlacementFee.add(
                            getUnitPlacmentFee(destination, lineItems.get(i), shippingData.get(i)));
                }
            }


        }


        return inventoryPlacementFee;
    }

    public BigDecimal getUnitPlacmentFee(String destination,
                                  IvsLineItem lineItem ,ShippingCostData costData) {


        BigDecimal unitPlacementFee = BigDecimal.ZERO;

        if (destination.toUpperCase().equals("US")) {

            BigDecimal packageWeightlbs = costData != null ? getPackageWeightPounds(
                    costData.getPackageWeight(), costData.getPackageWeightUnit()) : BigDecimal.ZERO;
            if (isOversize(costData)) {
                unitPlacementFee = new BigDecimal(39);
                if (packageWeightlbs.compareTo(new BigDecimal(5)) > 0) {
                    BigDecimal extraWeight = packageWeightlbs
                            .subtract(new BigDecimal(5)).setScale(0, RoundingMode.UP);
                    unitPlacementFee = unitPlacementFee
                            .add(extraWeight.multiply(new BigDecimal(6)));
                }
            } else {
                if (packageWeightlbs.compareTo(BigDecimal.ONE) <= 0) {
                    unitPlacementFee = new BigDecimal(9);
                } else if (packageWeightlbs.compareTo(new BigDecimal(2)) <= 0) {
                    unitPlacementFee = new BigDecimal(12);
                } else {
                    unitPlacementFee = new BigDecimal(12);
                     BigDecimal extraWeight = packageWeightlbs
                            .subtract(new BigDecimal(2)).setScale(0, RoundingMode.UP);
                    unitPlacementFee = unitPlacementFee
                            .add(extraWeight.multiply(new BigDecimal(3)));
                }
            }

            unitPlacementFee = unitPlacementFee.multiply(new BigDecimal(lineItem.getQuantity()));
        }

        return unitPlacementFee;
    }

    private BigDecimal getPackageWeightPounds(BigDecimal weight, String weightUnit) {
        if ("g".equals(weightUnit.toLowerCase())) {
            return weight.multiply(new BigDecimal(0.002204622));
        }
        if ("kg".equals(weightUnit.toLowerCase())) {
            return weight.multiply(new BigDecimal(2.204622));
        }
        if ("oz".equals(weightUnit.toLowerCase())) {
            return weight.multiply(new BigDecimal(0.062499993));
        } else
            return BigDecimal.ZERO;
    }




}