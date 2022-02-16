package com.kindminds.drs.core.biz.logistics;

import com.kindminds.drs.api.data.row.logistics.IvsLineitemRow;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineitemProductVerificationStatus;
import com.kindminds.drs.api.v2.biz.domain.model.request.SaveIvsLineitemRequest;
import org.apache.commons.lang.StringUtils;

public class IvsLineItemImpl implements IvsLineItem {

    String ivsName = "";
    int id =0;
    int lineSeq =0;
    int boxNum =0;
    int mixedBoxLineSeq =0;
    String skuCode = "";
    
    // numbersOfCtn
    String productBaseCode = "";
    boolean requireRepackaging  = false;
    
     int cartonNumberStart = 0;
     int cartonNumberEnd =0;
    String nameBySupplier = "";
    String perCartonGrossWeightKg = "";
    String perCartonUnits = "" ;
    String cartonCounts = "";
    String quantity = "";
    String unitAmount = "";
    String amountUntaxed  = "";

    String cartonDimensionCm1  = "";
    String cartonDimensionCm2  = "";
    String cartonDimensionCm3  = "";
    String guiInvoiceNumber  = "";
    String  guiFileName  = null;
    String guiuuid= "";

    IvsLineitemProductVerificationStatus productVerificationStatus
     = IvsLineitemProductVerificationStatus.DEFAULT;
    String docRequirement    = "";
    String  retailPONumber  = "";

    @Override
    public String getIvsName() {
        return ivsName;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getLineSeq() {
        return lineSeq;
    }

    @Override
    public int getBoxNum() {
        return boxNum;
    }

    @Override
    public void setBoxNum(int var1) {
        this.boxNum = var1;
    }

    @Override
    public int getMixedBoxLineSeq() {
        return mixedBoxLineSeq;
    }

    @Override
    public String getSkuCode() {
        return skuCode;
    }

    @Override
    public String getProductBaseCode() {
        return productBaseCode;
    }

    @Override
    public boolean getRequireRepackaging() {
        return requireRepackaging;
    }

    public boolean isRequireRepackaging() {
        return requireRepackaging;
    }

    @Override
    public int getCartonNumberStart() {
        return cartonNumberStart;
    }

    @Override
    public void setCartonNumberStart(int var1) {
        this.cartonNumberStart = var1;
    }

    @Override
    public int getCartonNumberEnd() {
        return cartonNumberEnd;
    }

    @Override
    public void setCartonNumberEnd(int var1) {
        this.cartonNumberEnd = var1;
    }

    @Override
    public String getNameBySupplier() {
        return nameBySupplier;
    }

    @Override
    public String getPerCartonGrossWeightKg() {
        return perCartonGrossWeightKg;
    }

    @Override
    public String getPerCartonUnits() {
        return perCartonUnits;
    }

    @Override
    public String getCartonCounts() {
        return cartonCounts;
    }

    @Override
    public String getQuantity() {
        return quantity;
    }

    @Override
    public String getUnitAmount() {
        return unitAmount;
    }

    @Override
    public String getAmountUntaxed() {
        return amountUntaxed;
    }

    @Override
    public String getCartonDimensionCm1() {
        return cartonDimensionCm1;
    }

    @Override
    public String getCartonDimensionCm2() {
        return cartonDimensionCm2;
    }

    @Override
    public String getCartonDimensionCm3() {
        return cartonDimensionCm3;
    }

    @Override
    public String getGuiInvoiceNumber() {
        return guiInvoiceNumber;
    }

    @Override
    public String getGuiFileName() {
        return guiFileName;
    }

    public String getGuiuuid() { return guiuuid; }

    @Override
    public IvsLineitemProductVerificationStatus getProductVerificationStatus() {
        return productVerificationStatus;
    }

    @Override
    public String getDocRequirement() {
        return docRequirement;
    }

    @Override
    public String getRetailPONumber() {
        return retailPONumber;
    }


    public IvsLineItemImpl(String ivsName , SaveIvsLineitemRequest request ){

        this.id = request.getId();
        this.ivsName = ivsName;
        this.lineSeq = request.getLineSeq();
        this.boxNum = request.getBoxNum();
        this.mixedBoxLineSeq = request.getMixedBoxLineSeq();
        this.skuCode = request.getSkuCode();
        this.requireRepackaging = request.getRequireRepackaging();
        this.cartonNumberStart = request.getCartonNumberStart();
        this.cartonNumberEnd = request.getCartonNumberEnd();
        this.nameBySupplier = request.getNameBySupplier();

        this.perCartonGrossWeightKg = request.getPerCartonGrossWeightKg();
        this.perCartonUnits = request.getPerCartonUnits();

        this.cartonCounts = request.getCartonCounts();
        this.quantity = request.getQuantity();

        this.unitAmount = request.getUnitAmount();
        this.amountUntaxed = request.getAmountUntaxed();
        this.cartonDimensionCm1 = request.getCartonDimensionCm1();
        this.cartonDimensionCm2 = request.getCartonDimensionCm2();
        this.cartonDimensionCm3 = request.getCartonDimensionCm3();
        this.guiInvoiceNumber = request.getGUIInvoiceNumber();
        this.guiFileName = request.getGUIFileName();
        this.guiuuid = request.getGUIuuid();
        this.retailPONumber = request.getRetailPONumber();

        this.productVerificationStatus = IvsLineitemProductVerificationStatus.DEFAULT;



    }

    public IvsLineItemImpl(String ivsName  , IvsLineitemRow row){

        this.ivsName = ivsName;
        this.id = row.getId();
        this.lineSeq = row.getLineSeq();

        this.boxNum = row.getBoxNum();

        this.mixedBoxLineSeq = row.getMixedBoxLineSeq();

        //this.sourceShipmentName = row.sourceShipmentName
        this.skuCode = row.getCodeByDrs();
        this.productBaseCode = row.getProductBaseCode();

        this.nameBySupplier = row.getNameBySupplier();
        this.quantity = Integer.toString(row.getQuantity());

        this.unitAmount = row.getUnitAmount();
        this.cartonDimensionCm1 = row.getCtnDim1Cm();
        this.cartonDimensionCm2 = row.getCtnDim2Cm();
        this.cartonDimensionCm3 = row.getCtnDim3Cm();

        this.perCartonGrossWeightKg = row.getGrossWeightPerCtnKg();
        this.unitAmount = row.getUnitAmount();

        this.perCartonUnits = row.getUnitsPerCtn();

        this.cartonCounts = row.getNumbersOfCtn();

        this.requireRepackaging = row.getRequirePackaging();

        this.guiInvoiceNumber = (row.getGuiInvoiceNumber() == null) ? "" :
                row.getGuiInvoiceNumber();

        this.guiFileName = row.getGuiFileName();

        this.guiuuid = row.getGuiuuid();

        this.cartonNumberStart = row.getCartonNumberStart();
            this.cartonNumberEnd = row.getCartonNumberEnd();

        if(StringUtils.isEmpty(row.getLineStatus())){
            this.productVerificationStatus = IvsLineitemProductVerificationStatus.DEFAULT;
        }else{
            this.productVerificationStatus =
                    IvsLineitemProductVerificationStatus.fromValue(row.getLineStatus());
        }

    }




    @Override
    public void verifyProduct() {

        this.productVerificationStatus =
                IvsLineitemProductVerificationStatus.TO_BE_CONFIRMED;

    }

    @Override
    public void  rejectProduct() {
        this.productVerificationStatus = IvsLineitemProductVerificationStatus.INVALID;
    }

    @Override
    public void confirmProduct() {
        this.productVerificationStatus = IvsLineitemProductVerificationStatus.CONFIRMED;
    }

    @Override
    public void assignNumber(int boxNum,int cartonNumberStart, int cartonNumberEnd) {
        this.boxNum = boxNum;
        this.cartonNumberStart = cartonNumberStart;
        this.cartonNumberEnd = cartonNumberEnd;
    }

    @Override
    public void updateDocRequirement(String docRequirement) {
        this.docRequirement = docRequirement;
    }

    @Override
    public void updateProductVerificationStatus(IvsLineitemProductVerificationStatus status) {
        this.productVerificationStatus = status;
    }
}
