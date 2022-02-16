package com.kindminds.drs.api.data.row.logistics;

public  class IvsLineitemRow{

     private int id ;
     private int lineSeq ;
     private int boxNum;
     private int mixedBoxLineSeq;
     private String sourceShipmentName;
     private String codeByDrs ;
     private String nameBySupplier;
     private int quantity;
     private String unitAmount;
     private String ctnDim1Cm ;
     private String ctnDim2Cm;
     private String ctnDim3Cm;
     private String grossWeightPerCtnKg;
     private String unitsPerCtn;
     private String numbersOfCtn;
     private Boolean requirePackaging;
     private String guiInvoiceNumber;
     private String guiFileName;
     private String guiuuid;
     private int cartonNumberStart;
     private int cartonNumberEnd;
     private String lineStatus ;
     private String productBaseCode ;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLineSeq() {
        return lineSeq;
    }

    public void setLineSeq(int lineSeq) {
        this.lineSeq = lineSeq;
    }

    public int getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(int boxNum) {
        this.boxNum = boxNum;
    }

    public int getMixedBoxLineSeq() {
        return mixedBoxLineSeq;
    }

    public void setMixedBoxLineSeq(int mixedBoxLineSeq) {
        this.mixedBoxLineSeq = mixedBoxLineSeq;
    }

    public String getSourceShipmentName() {
        return sourceShipmentName;
    }

    public void setSourceShipmentName(String sourceShipmentName) {
        this.sourceShipmentName = sourceShipmentName;
    }

    public String getCodeByDrs() {
        return codeByDrs;
    }

    public void setCodeByDrs(String codeByDrs) {
        this.codeByDrs = codeByDrs;
    }

    public String getNameBySupplier() {
        return nameBySupplier;
    }

    public void setNameBySupplier(String nameBySupplier) {
        this.nameBySupplier = nameBySupplier;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(String unitAmount) {
        this.unitAmount = unitAmount;
    }

    public String getCtnDim1Cm() {
        return ctnDim1Cm;
    }

    public void setCtnDim1Cm(String ctnDim1Cm) {
        this.ctnDim1Cm = ctnDim1Cm;
    }

    public String getCtnDim2Cm() {
        return ctnDim2Cm;
    }

    public void setCtnDim2Cm(String ctnDim2Cm) {
        this.ctnDim2Cm = ctnDim2Cm;
    }

    public String getCtnDim3Cm() {
        return ctnDim3Cm;
    }

    public void setCtnDim3Cm(String ctnDim3Cm) {
        this.ctnDim3Cm = ctnDim3Cm;
    }

    public String getGrossWeightPerCtnKg() {
        return grossWeightPerCtnKg;
    }

    public void setGrossWeightPerCtnKg(String grossWeightPerCtnKg) {
        this.grossWeightPerCtnKg = grossWeightPerCtnKg;
    }

    public String getUnitsPerCtn() {
        return unitsPerCtn;
    }

    public void setUnitsPerCtn(String unitsPerCtn) {
        this.unitsPerCtn = unitsPerCtn;
    }

    public String getNumbersOfCtn() {
        return numbersOfCtn;
    }

    public void setNumbersOfCtn(String numbersOfCtn) {
        this.numbersOfCtn = numbersOfCtn;
    }

    public Boolean getRequirePackaging() {
        return requirePackaging;
    }

    public void setRequirePackaging(Boolean requirePackaging) {
        this.requirePackaging = requirePackaging;
    }

    public String getGuiInvoiceNumber() {
        return guiInvoiceNumber;
    }

    public void setGuiInvoiceNumber(String guiInvoiceNumber) {
        this.guiInvoiceNumber = guiInvoiceNumber;
    }

    public String getGuiFileName() {
        return guiFileName;
    }

    public void setGuiFileName(String guiFileName) {
        this.guiFileName = guiFileName;
    }

    public String getGuiuuid() {
        return guiuuid;
    }

    public void setGuiuuid(String guiuuid) {
        this.guiuuid = guiuuid;
    }

    public int getCartonNumberStart() {
        return cartonNumberStart;
    }

    public void setCartonNumberStart(int cartonNumberStart) {
        this.cartonNumberStart = cartonNumberStart;
    }

    public int getCartonNumberEnd() {
        return cartonNumberEnd;
    }

    public void setCartonNumberEnd(int cartonNumberEnd) {
        this.cartonNumberEnd = cartonNumberEnd;
    }

    public String getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(String lineStatus) {
        this.lineStatus = lineStatus;
    }

    public String getProductBaseCode() {
        return productBaseCode;
    }

    public void setProductBaseCode(String productBaseCode) {
        this.productBaseCode = productBaseCode;
    }


    public IvsLineitemRow(){}

    public IvsLineitemRow(int id, int lineSeq, int boxNum, int mixedBoxLineSeq, String sourceShipmentName, String codeByDrs, String nameBySupplier, int quantity, String unitAmount, String ctnDim1Cm, String ctnDim2Cm, String ctnDim3Cm, String grossWeightPerCtnKg, String unitsPerCtn, String numbersOfCtn, Boolean requirePackaging, String guiInvoiceNumber, String guiFileName,String guiuuid , int cartonNumberStart, int cartonNumberEnd, String lineStatus, String productBaseCode) {
        this.id = id;
        this.lineSeq = lineSeq;
        this.boxNum = boxNum;
        this.mixedBoxLineSeq = mixedBoxLineSeq;
        this.sourceShipmentName = sourceShipmentName;
        this.codeByDrs = codeByDrs;
        this.nameBySupplier = nameBySupplier;
        this.quantity = quantity;
        this.unitAmount = unitAmount;
        this.ctnDim1Cm = ctnDim1Cm;
        this.ctnDim2Cm = ctnDim2Cm;
        this.ctnDim3Cm = ctnDim3Cm;
        this.grossWeightPerCtnKg = grossWeightPerCtnKg;
        this.unitsPerCtn = unitsPerCtn;
        this.numbersOfCtn = numbersOfCtn;
        this.requirePackaging = requirePackaging;
        this.guiInvoiceNumber = guiInvoiceNumber;
        this.guiFileName = guiFileName;
        this.guiuuid = guiuuid;
        this.cartonNumberStart = cartonNumberStart;
        this.cartonNumberEnd = cartonNumberEnd;
        this.lineStatus = lineStatus;
        this.productBaseCode = productBaseCode;
    }


}
