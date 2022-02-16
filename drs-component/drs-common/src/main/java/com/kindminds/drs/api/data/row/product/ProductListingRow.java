package com.kindminds.drs.api.data.row.product;

import org.bson.types.ObjectId;

public interface ProductListingRow {

    ObjectId get_id();

    String getSku();

    String getMarketplace();

    String getBrandNameCH();

    String getBrandNameEN();

    String getProductNameCH();

    String getProductNameEN();

    String getVariationTheme();

    Boolean getG3Invoice();

    Boolean getMsds();

    Boolean getDangerousGoods();

    Boolean getUn383();

    Boolean getPpq505();

    Boolean getUsFDA();

    Boolean getUsMID();

    Boolean getFatContentSpecification();

    Boolean getExportTariffRequirement();

    Boolean getAdditionalRemarks();


}