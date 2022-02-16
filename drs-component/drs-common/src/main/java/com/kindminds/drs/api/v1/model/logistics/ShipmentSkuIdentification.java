package com.kindminds.drs.api.v1.model.logistics;

public class ShipmentSkuIdentification {

    private int id;
    private int unsShipmentId;
    private int ivsShipmentId;
    private int ivsShipmentLineItemId;
    private int seq;
    private int skuId;
    private String destinationCountry;
    private String ivsSkuSerialNo;
    private int drsTransactionId;
    private String status;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnsShipmentId() {
        return unsShipmentId;
    }

    public void setUnsShipmentId(int unsShipmentId) {
        this.unsShipmentId = unsShipmentId;
    }

    public int getIvsShipmentId() {
        return ivsShipmentId;
    }

    public void setIvsShipmentId(int ivsShipmentId) {
        this.ivsShipmentId = ivsShipmentId;
    }

    public int getIvsShipmentLineItemId() {
        return ivsShipmentLineItemId;
    }

    public void setIvsShipmentLineItemId(int ivsShipmentLineItemId) {
        this.ivsShipmentLineItemId = ivsShipmentLineItemId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getIvsSkuSerialNo() {
        return ivsSkuSerialNo;
    }

    public void setIvsSkuSerialNo(String ivsSkuSerialNo) {
        this.ivsSkuSerialNo = ivsSkuSerialNo;
    }

    public int getDrsTransactionId() {
        return drsTransactionId;
    }

    public void setDrsTransactionId(int drsTransactionId) {
        this.drsTransactionId = drsTransactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ShipmentSkuIdentification(int unsShipmentId, int ivsShipmentId, int ivsShipmentLineItemId, int seq, int skuId, String destinationCountry,
                                     String ivsName, String codeByDrs ,int boxNum ,int mixBoxLineSeq ,int sn) {
        this.unsShipmentId = unsShipmentId;
        this.ivsShipmentId = ivsShipmentId;
        this.ivsShipmentLineItemId = ivsShipmentLineItemId;
        this.seq = seq;
        this.skuId = skuId;
        this.destinationCountry = destinationCountry;
        this.ivsSkuSerialNo = GenerateIvsSkuSerialNo(ivsName,codeByDrs,boxNum,mixBoxLineSeq,sn);

    }

    private String GenerateIvsSkuSerialNo(String ivsName, String codeByDrs ,int boxNum ,int mixBoxLineSeq ,int seq){
       String ivsSkuSerialNo=ivsName+"_"+codeByDrs+"_"+boxNum+"-"+mixBoxLineSeq+"_"+seq;
       return ivsSkuSerialNo;
    }

    @Override
    public String toString() {
        return "ShipmentSkuIdentification{" +
                "id=" + id +
                ", unsShipmentId=" + unsShipmentId +
                ", ivsShipmentId=" + ivsShipmentId +
                ", ivsShipmentLineItemId=" + ivsShipmentLineItemId +
                ", seq=" + seq +
                ", skuId=" + skuId +
                ", destinationCountry='" + destinationCountry + '\'' +
                ", ivsSkuSerialNo='" + ivsSkuSerialNo + '\'' +
                ", drsTransactionId=" + drsTransactionId +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
