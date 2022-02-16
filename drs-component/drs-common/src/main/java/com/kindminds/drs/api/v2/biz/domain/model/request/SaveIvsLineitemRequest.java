package com.kindminds.drs.api.v2.biz.domain.model.request;


public class SaveIvsLineitemRequest {

    private int id = 0;
    private int lineSeq = 0;
    private int boxNum = 0;
    private int mixedBoxLineSeq  = 0;
    private String skuCode = "";
    private Boolean requireRepackaging  = false;
    private int cartonNumberStart = 0;
    private int cartonNumberEnd = 0;
    private String skuNameBySupplier   =  "";
    private String cartonDimensionCm1   = "";
    private String cartonDimensionCm2 = "";
    private String cartonDimensionCm3  = "";
    private String perCartonGrossWeightKg  = "";
    private String perCartonUnits  = "";
    private String cartonCounts  = "0";
    private String quantity  = "";
    private String unitAmount  = "";
    private String amountUntaxed = "";
    private String GUIInvoiceNumber  = "";
    private String GUIFileName   = "";
    private String GUIuuid = "";
    private Boolean isGUIInvoiceIsRequired  = false;
    private String retailPONumber  = "";


    public SaveIvsLineitemRequest() {}


    public SaveIvsLineitemRequest(int boxNum, int mixedBoxLineSeq,int cartonNumberStart,int cartonNumberEnd,
                                  String skuCode, String unitsPerCarton,
                                  String numOfCarton,String fcaPrice,String dimLength,String dimWidth,
                                  String dimHeight,String weight,String retailPONumber,
                                  String quantity, String amountUntaxed, Boolean requireRepackaging) {

        this.boxNum = boxNum;
        this.mixedBoxLineSeq = mixedBoxLineSeq;
        this.cartonNumberStart = cartonNumberStart;
        this.cartonNumberEnd = cartonNumberEnd;
        this.skuCode = skuCode;
        this.perCartonUnits = unitsPerCarton;
        this.cartonCounts = numOfCarton;
        this.unitAmount = fcaPrice;
        this.cartonDimensionCm1 = dimLength;
        this.cartonDimensionCm2 = dimWidth;
        this.cartonDimensionCm3 = dimHeight;
        this.perCartonGrossWeightKg = weight;
        this.retailPONumber = retailPONumber;
        this.quantity = quantity;
        this.amountUntaxed = amountUntaxed;
        this.requireRepackaging = requireRepackaging;

    }

    @Override
    public String toString()  {

        return "SaveIvsLineitemRequest [ getId()=" + getId() + ", getLineSeq()=" + getLineSeq() + ", getBoxNum()=" + getBoxNum()
                + ", getMixedBoxLineSeq()=" + getMixedBoxLineSeq() + ", getSkuCode()=" + getSkuCode()
                + ", getRequireRepackaging()=" + getRequireRepackaging() + ", getCartonNumberStart()="
                + getCartonNumberStart() + ", getCartonNumberEnd()=" + getCartonNumberEnd() + ", getNameBySupplier()="
                + getNameBySupplier() + ", getCartonDimensionCm1()=" + getCartonDimensionCm1()
                + ", getCartonDimensionCm2()=" + getCartonDimensionCm2() + ", getCartonDimensionCm3()="
                + getCartonDimensionCm3() + ", getPerCartonGrossWeightKg()=" + getPerCartonGrossWeightKg()
                + ", getPerCartonUnits()=" + getPerCartonUnits() + ", getCartonCounts()=" + getCartonCounts()
                + ", getQuantity()=" + getQuantity() + ", getUnitAmount()=" + getUnitAmount() + ", getAmountUntaxed()="
                + getAmountUntaxed()  + ", getGUIInvoiceNumber()=" + getGUIInvoiceNumber()+ ", getGUIFileName()="+ getGUIFileName()
                +", getGUIuuid()=" + getGUIuuid()+ ", getIsGUIInvoiceIsRequired()=" + getIsGUIInvoiceIsRequired()
                + ", getGUIInvoiceFileBytes()=" + null + "]";
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
         this.id = id;
    }

    public int getLineSeq() {
        return 0;
    }


    public int getBoxNum() {
        return this.boxNum;
    }


    public void setBoxNum(int boxNum) {
        this.boxNum = boxNum;
    }


    public int getMixedBoxLineSeq() {
        return this.mixedBoxLineSeq;
    }


    public void setMixedBoxLineSeq(int mixedBoxLineSeq) {
        this.mixedBoxLineSeq = mixedBoxLineSeq;
    }


    public  String getSkuCode() {
        return this.skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }


    public Boolean getRequireRepackaging()  {
        return this.requireRepackaging;
    }

    public void setRequireRepackaging(Boolean requireRepackaging) {
        this.requireRepackaging = requireRepackaging;
    }


    public int getCartonNumberStart() {
        return this.cartonNumberStart;
    }

    public void setCartonNumberStart(int cartonNumberStart) {
        this.cartonNumberStart = cartonNumberStart;
    }


    public int getCartonNumberEnd() {
        return this.cartonNumberEnd;
    }

    public  void setCartonNumberEnd(int cartonNumberEnd) {
        this.cartonNumberEnd = cartonNumberEnd;
    }


    public String getNameBySupplier()  {
        return this.skuNameBySupplier;
    }

    public void setSkuNameBySupplier(String skuNameBySupplier) {
        this.skuNameBySupplier = skuNameBySupplier;
    }


    public String getCartonDimensionCm1() {
        return this.cartonDimensionCm1;
    }

    public void setCartonDimensionCm1(String cartonDimensionCm1) {
        this.cartonDimensionCm1 = cartonDimensionCm1;
    }


    public String getCartonDimensionCm2()  {
        return this.cartonDimensionCm2;
    }

    public void setCartonDimensionCm2(String cartonDimensionCm2 ) {
        this.cartonDimensionCm2 = cartonDimensionCm2;
    }


    public String getCartonDimensionCm3()  {
        return this.cartonDimensionCm3;
    }

    public void setCartonDimensionCm3(String cartonDimensionCm3) {
        this.cartonDimensionCm3 = cartonDimensionCm3;
    }


    public String getPerCartonGrossWeightKg()  {
        return this.perCartonGrossWeightKg;
    }

    public void setPerCartonGrossWeightKg(String perCartonGrossWeightKg) {
        this.perCartonGrossWeightKg = perCartonGrossWeightKg;
    }


    public String getPerCartonUnits()  {
        return this.perCartonUnits;
    }

    public void setPerCartonUnits(String perCartonUnits) {
        this.perCartonUnits = perCartonUnits;
    }


    public String getCartonCounts(){
        return this.cartonCounts;
    }

    public void setCartonCounts(String cartonCounts) {
        this.cartonCounts = cartonCounts;
    }


    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity ) {
        this.quantity = quantity;
    }


    public String getUnitAmount() {
        return this.unitAmount;
    }

    public void setUnitAmount(String unitAmount) {
        this.unitAmount = unitAmount;
    }


    public String getAmountUntaxed()  {
        return this.amountUntaxed;
    }

    public void setAmountUntaxed(String amountUntaxed ) {
        this.amountUntaxed = amountUntaxed;
    }


    public String getGUIInvoiceNumber() {
        return this.GUIInvoiceNumber;
    }

    public void setGUIInvoiceNumber(String GUIInvoiceNumber) {
        if (GUIInvoiceNumber.isEmpty()) this.GUIInvoiceNumber = ""; else this.GUIInvoiceNumber = GUIInvoiceNumber;
    }


    public String getGUIFileName() {
        return this.GUIFileName;
    }

    public  void setGUIFileName(String GUIFileName) {
        if (GUIFileName.isEmpty()) this.GUIFileName = ""; else this.GUIFileName = GUIFileName;
    }

    public String getGUIuuid() {
        return this.GUIuuid;
    }

    public void setGUIuuid(String GUIuuid) {
        if (GUIuuid.isEmpty()) this.GUIuuid = ""; else this.GUIuuid = GUIuuid;
    }

    public Boolean getIsGUIInvoiceIsRequired() {
        return this.isGUIInvoiceIsRequired;
    }

    public void setRetailPONumber(String retailPONumber) {
        if (retailPONumber.isEmpty()) this.retailPONumber = ""; else this.retailPONumber = retailPONumber;
    }

    public String getRetailPONumber() {
        return this.retailPONumber;
    }



}
