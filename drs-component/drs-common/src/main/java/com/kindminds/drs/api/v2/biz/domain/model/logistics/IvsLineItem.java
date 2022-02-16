package com.kindminds.drs.api.v2.biz.domain.model.logistics;


public interface IvsLineItem {

  String getIvsName();

  int getId();

  int getLineSeq();

  int getBoxNum();

  void setBoxNum(int var1);

  int getMixedBoxLineSeq();


  String getSkuCode();


  String getProductBaseCode();

  boolean getRequireRepackaging();

  int getCartonNumberStart();

  void setCartonNumberStart(int var1);

  int getCartonNumberEnd();

  void setCartonNumberEnd(int var1);


  String getNameBySupplier();


  String getPerCartonGrossWeightKg();


  String getPerCartonUnits();


  String getCartonCounts();


  String getQuantity();


  String getUnitAmount();


  String getAmountUntaxed();


  String getCartonDimensionCm1();


  String getCartonDimensionCm2();


  String getCartonDimensionCm3();


  String getGuiInvoiceNumber();


  String getGuiFileName();

  String getGuiuuid();


  String getRetailPONumber();


  IvsLineitemProductVerificationStatus getProductVerificationStatus();


  String getDocRequirement();

  void verifyProduct();

  void rejectProduct();

  void confirmProduct();

  void assignNumber(int var1, int var2, int var3);

  void updateDocRequirement( String var1);

  void updateProductVerificationStatus( IvsLineitemProductVerificationStatus var1);
}