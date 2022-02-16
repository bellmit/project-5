package com.kindminds.drs.api.data.transfer.logistics;

import java.math.BigDecimal;

public interface IvsProductVerifyInfo {
    
    String getSku();

    void setSku( String var1);

    
    String getProductName();

    
    String getBaseProductCode();

    
    String getProductNameLocal();

    
    String getBrandEng();

    
    String getCountryOfOrigin();

    
    String getMarketHsCode();

    
    String getHsCode();

    
    String getExportTariffReq();

    void setExportTariffReq( String var1);

    
    String getImportTariffReq();

    void setImportTariffReq( String var1);

    
    BigDecimal getFcaPrice();

    Integer getQuantity();

    void setQuantity( Integer var1);

    BigDecimal getFcaTotal();

    void setFcaTotal( BigDecimal var1);

    
    BigDecimal getInventoryPlacementFee();

    void setInventoryPlacementFee( BigDecimal var1);

    
    BigDecimal getNetWeight();

    
    String getWeightUnit();

    
    BigDecimal getLength();

    
    BigDecimal getWidth();

    
    BigDecimal getHeight();

    
    String getDimensionUnit();

    
    BigDecimal getBoxCbm();

    void setBoxCbm( BigDecimal var1);

    boolean isOversized();

    void setOversized(boolean var1);

    Boolean isDangerousGoods();

    void setDangerousGoods(Boolean var1);
}