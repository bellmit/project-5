package com.kindminds.drs.persist.cqrs.store.event.dao.productV2;

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.GsProductDao;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.GsProductDetailDao;
import com.kindminds.drs.api.data.transfer.productV2.GsProductData;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Repository
public class GsProductDaoImpl  extends Dao implements GsProductDao {

    @Override @Transactional("transactionManager")
    public String insertUs(GsProductData gsProductData) {

        String sql = "insert into product.gs_product "
                + "( base_product_code, sku,  gtin,  product_name, product_category, supplier_company, brand_eng, country_of_origin, hs_code, manufacturer_lead_time, fca_price, product_dimension_l, product_dimension_w, product_dimension_h, product_dimension_unit, product_material_percent, product_weight, product_weight_unit, us_country, us_sales_price, us_note, us_package_dimension_l, us_package_dimension_w, us_package_dimension_h, us_package_dimension_unit, us_package_weight, us_package_weight_unit, us_quantity_per_carton, us_carton_dimension_l, us_carton_dimension_w, us_carton_dimension_h, us_carton_dimension_unit, us_carton_weight, us_carton_weight_unit, us_import_duty_rate, us_hs_code,       us_unfulfillable_inventory_removal_settings, us_ddp_price  ) values "
                + "( :baseProductCode,  :sku, :gtin, :productName, :productCategory, :supplierCompany, :brandEng, :countryOfOrigin,  :hsCode, :manufacturerLeadTime,  :fcaPrice, :productDimensionL,  :productDimensionW,  :productDimensionH,  :productDimensionUnit,  :productMaterialPercent,  :productWeight, :productWeightUnit,  :country,   :salesPrice,    :note,   :packageDimensionL,     :packageDimensionW,     :packageDimensionH,     :packageDimensionUnit,     :packageWeight,    :packageWeightUnit,     :quantityPerCarton,     :cartonDimensionL,     :cartonDimensionW,     :cartonDimensionH,     :cartonDimensionUnit,     :cartonWeight,    :cartonWeightUnit,     :importDutyRate,     :salesSideHsCode, :unfulFillableInventoryRemovalSettings,      :ddpPrice  )";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("baseProductCode", gsProductData.getBaseProductCode());
        q.addValue("sku", gsProductData.getSku());
        q.addValue("gtin", gsProductData.getGtin());
        q.addValue("productName", gsProductData.getProductName());
        q.addValue("productCategory", gsProductData.getProductCategory());
        q.addValue("supplierCompany", gsProductData.getSupplierCompany());
        q.addValue("brandEng", gsProductData.getBrandEng());
        q.addValue("countryOfOrigin", gsProductData.getCountryOfOrigin());
        q.addValue("hsCode", gsProductData.getHsCode());
        q.addValue("manufacturerLeadTime", gsProductData.getManufacturerLeadTime());
        q.addValue("fcaPrice", gsProductData.getFcaPrice());
        q.addValue("productDimensionL", gsProductData.getProductDimensionL());
        q.addValue("productDimensionW", gsProductData.getProductDimensionW());
        q.addValue("productDimensionH", gsProductData.getProductDimensionH());
        q.addValue("productDimensionUnit", gsProductData.getProductDimensionUnit());
        q.addValue("productMaterialPercent", gsProductData.getProductMaterialPercent());
        q.addValue("productWeight", gsProductData.getProductWeight());
        q.addValue("productWeightUnit", gsProductData.getProductWeightUnit());
        q.addValue("country", gsProductData.getCountry());
        q.addValue("salesPrice", gsProductData.getSalesPrice());
        q.addValue("note", gsProductData.getNote());
        q.addValue("packageDimensionL", gsProductData.getPackageDimensionL());
        q.addValue("packageDimensionW", gsProductData.getPackageDimensionW());
        q.addValue("packageDimensionH", gsProductData.getPackageDimensionH());
        q.addValue("packageDimensionUnit", gsProductData.getPackageDimensionUnit());
        q.addValue("packageWeight", gsProductData.getPackageWeight());
        q.addValue("packageWeightUnit", gsProductData.getPackageWeightUnit());
        q.addValue("quantityPerCarton", gsProductData.getQuantityPerCarton());
        q.addValue("cartonDimensionL", gsProductData.getCartonDimensionL());
        q.addValue("cartonDimensionW", gsProductData.getCartonDimensionW());
        q.addValue("cartonDimensionH", gsProductData.getCartonDimensionH());
        q.addValue("cartonDimensionUnit", gsProductData.getCartonDimensionUnit());
        q.addValue("cartonWeight", gsProductData.getCartonWeight());
        q.addValue("cartonWeightUnit", gsProductData.getCartonWeightUnit());
        q.addValue("importDutyRate", gsProductData.getImportDutyRate());
        q.addValue("salesSideHsCode", gsProductData.getSalesSideHsCode());
        q.addValue("unfulFillableInventoryRemovalSettings", gsProductData.getUnfulFillableInventoryRemovalSettings());
        q.addValue("ddpPrice", gsProductData.getDdpPrice());
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        return null;
    }

    @Override @Transactional("transactionManager")
    public String insertUk(GsProductData gsProductData) {

        String sql = "insert into product.gs_product "
                + "( base_product_code, sku,  gtin,  product_name, product_category, supplier_company, brand_eng, country_of_origin, hs_code, manufacturer_lead_time, fca_price, product_dimension_l, product_dimension_w, product_dimension_h, product_dimension_unit, product_material_percent, product_weight, product_weight_unit, uk_country, uk_sales_price, uk_note, uk_package_dimension_l, uk_package_dimension_w, uk_package_dimension_h, uk_package_dimension_unit, uk_package_weight, uk_package_weight_unit, uk_quantity_per_carton, uk_carton_dimension_l, uk_carton_dimension_w, uk_carton_dimension_h, uk_carton_dimension_unit, uk_carton_weight, uk_carton_weight_unit, uk_import_duty_rate, uk_hs_code,       uk_unfulfillable_inventory_removal_settings, uk_ddp_price  ) values "
                + "( :baseProductCode,  :sku, :gtin, :productName, :productCategory, :supplierCompany, :brandEng, :countryOfOrigin,  :hsCode, :manufacturerLeadTime,  :fcaPrice, :productDimensionL,  :productDimensionW,  :productDimensionH,  :productDimensionUnit,  :productMaterialPercent,  :productWeight, :productWeightUnit,  :country,   :salesPrice,    :note,   :packageDimensionL,     :packageDimensionW,     :packageDimensionH,     :packageDimensionUnit,     :packageWeight,    :packageWeightUnit,     :quantityPerCarton,     :cartonDimensionL,     :cartonDimensionW,     :cartonDimensionH,     :cartonDimensionUnit,     :cartonWeight,    :cartonWeightUnit,     :importDutyRate,     :salesSideHsCode, :unfulFillableInventoryRemovalSettings,      :ddpPrice  )";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("baseProductCode", gsProductData.getBaseProductCode());
        q.addValue("sku", gsProductData.getSku());
        q.addValue("gtin", gsProductData.getGtin());
        q.addValue("productName", gsProductData.getProductName());
        q.addValue("productCategory", gsProductData.getProductCategory());
        q.addValue("supplierCompany", gsProductData.getSupplierCompany());
        q.addValue("brandEng", gsProductData.getBrandEng());
        q.addValue("countryOfOrigin", gsProductData.getCountryOfOrigin());
        q.addValue("hsCode", gsProductData.getHsCode());
        q.addValue("manufacturerLeadTime", gsProductData.getManufacturerLeadTime());
        q.addValue("fcaPrice", gsProductData.getFcaPrice());
        q.addValue("productDimensionL", gsProductData.getProductDimensionL());
        q.addValue("productDimensionW", gsProductData.getProductDimensionW());
        q.addValue("productDimensionH", gsProductData.getProductDimensionH());
        q.addValue("productDimensionUnit", gsProductData.getProductDimensionUnit());
        q.addValue("productMaterialPercent", gsProductData.getProductMaterialPercent());
        q.addValue("productWeight", gsProductData.getProductWeight());
        q.addValue("productWeightUnit", gsProductData.getProductWeightUnit());
        q.addValue("country", gsProductData.getCountry());
        q.addValue("salesPrice", gsProductData.getSalesPrice());
        q.addValue("note", gsProductData.getNote());
        q.addValue("packageDimensionL", gsProductData.getPackageDimensionL());
        q.addValue("packageDimensionW", gsProductData.getPackageDimensionW());
        q.addValue("packageDimensionH", gsProductData.getPackageDimensionH());
        q.addValue("packageDimensionUnit", gsProductData.getPackageDimensionUnit());
        q.addValue("packageWeight", gsProductData.getPackageWeight());
        q.addValue("packageWeightUnit", gsProductData.getPackageWeightUnit());
        q.addValue("quantityPerCarton", gsProductData.getQuantityPerCarton());
        q.addValue("cartonDimensionL", gsProductData.getCartonDimensionL());
        q.addValue("cartonDimensionW", gsProductData.getCartonDimensionW());
        q.addValue("cartonDimensionH", gsProductData.getCartonDimensionH());
        q.addValue("cartonDimensionUnit", gsProductData.getCartonDimensionUnit());
        q.addValue("cartonWeight", gsProductData.getCartonWeight());
        q.addValue("cartonWeightUnit", gsProductData.getCartonWeightUnit());
        q.addValue("importDutyRate", gsProductData.getImportDutyRate());
        q.addValue("salesSideHsCode", gsProductData.getSalesSideHsCode());
        q.addValue("unfulFillableInventoryRemovalSettings", gsProductData.getUnfulFillableInventoryRemovalSettings());
        q.addValue("ddpPrice", gsProductData.getDdpPrice());
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        return null;
    }

    @Override @Transactional("transactionManager")
    public String insertCa(GsProductData gsProductData) {

        String sql = "insert into product.gs_product "
                + "( base_product_code, sku,  gtin,  product_name, product_category, supplier_company, brand_eng, country_of_origin, hs_code, manufacturer_lead_time, fca_price, product_dimension_l, product_dimension_w, product_dimension_h, product_dimension_unit, product_material_percent, product_weight, product_weight_unit, ca_country, ca_sales_price, ca_note, ca_package_dimension_l, ca_package_dimension_w, ca_package_dimension_h, ca_package_dimension_unit, ca_package_weight, ca_package_weight_unit, ca_quantity_per_carton, ca_carton_dimension_l, ca_carton_dimension_w, ca_carton_dimension_h, ca_carton_dimension_unit, ca_carton_weight, ca_carton_weight_unit, ca_import_duty_rate, ca_hs_code,       ca_unfulfillable_inventory_removal_settings, ca_ddp_price  ) values "
                + "( :baseProductCode,  :sku, :gtin, :productName, :productCategory, :supplierCompany, :brandEng, :countryOfOrigin,  :hsCode, :manufacturerLeadTime,  :fcaPrice, :productDimensionL,  :productDimensionW,  :productDimensionH,  :productDimensionUnit,  :productMaterialPercent,  :productWeight, :productWeightUnit,  :country,   :salesPrice,    :note,   :packageDimensionL,     :packageDimensionW,     :packageDimensionH,     :packageDimensionUnit,     :packageWeight,    :packageWeightUnit,     :quantityPerCarton,     :cartonDimensionL,     :cartonDimensionW,     :cartonDimensionH,     :cartonDimensionUnit,     :cartonWeight,    :cartonWeightUnit,     :importDutyRate,     :salesSideHsCode, :unfulFillableInventoryRemovalSettings,      :ddpPrice  )";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("baseProductCode", gsProductData.getBaseProductCode());
        q.addValue("sku", gsProductData.getSku());
        q.addValue("gtin", gsProductData.getGtin());
        q.addValue("productName", gsProductData.getProductName());
        q.addValue("productCategory", gsProductData.getProductCategory());
        q.addValue("supplierCompany", gsProductData.getSupplierCompany());
        q.addValue("brandEng", gsProductData.getBrandEng());
        q.addValue("countryOfOrigin", gsProductData.getCountryOfOrigin());
        q.addValue("hsCode", gsProductData.getHsCode());
        q.addValue("manufacturerLeadTime", gsProductData.getManufacturerLeadTime());
        q.addValue("fcaPrice", gsProductData.getFcaPrice());
        q.addValue("productDimensionL", gsProductData.getProductDimensionL());
        q.addValue("productDimensionW", gsProductData.getProductDimensionW());
        q.addValue("productDimensionH", gsProductData.getProductDimensionH());
        q.addValue("productDimensionUnit", gsProductData.getProductDimensionUnit());
        q.addValue("productMaterialPercent", gsProductData.getProductMaterialPercent());
        q.addValue("productWeight", gsProductData.getProductWeight());
        q.addValue("productWeightUnit", gsProductData.getProductWeightUnit());
        q.addValue("country", gsProductData.getCountry());
        q.addValue("salesPrice", gsProductData.getSalesPrice());
        q.addValue("note", gsProductData.getNote());
        q.addValue("packageDimensionL", gsProductData.getPackageDimensionL());
        q.addValue("packageDimensionW", gsProductData.getPackageDimensionW());
        q.addValue("packageDimensionH", gsProductData.getPackageDimensionH());
        q.addValue("packageDimensionUnit", gsProductData.getPackageDimensionUnit());
        q.addValue("packageWeight", gsProductData.getPackageWeight());
        q.addValue("packageWeightUnit", gsProductData.getPackageWeightUnit());
        q.addValue("quantityPerCarton", gsProductData.getQuantityPerCarton());
        q.addValue("cartonDimensionL", gsProductData.getCartonDimensionL());
        q.addValue("cartonDimensionW", gsProductData.getCartonDimensionW());
        q.addValue("cartonDimensionH", gsProductData.getCartonDimensionH());
        q.addValue("cartonDimensionUnit", gsProductData.getCartonDimensionUnit());
        q.addValue("cartonWeight", gsProductData.getCartonWeight());
        q.addValue("cartonWeightUnit", gsProductData.getCartonWeightUnit());
        q.addValue("importDutyRate", gsProductData.getImportDutyRate());
        q.addValue("salesSideHsCode", gsProductData.getSalesSideHsCode());
        q.addValue("unfulFillableInventoryRemovalSettings", gsProductData.getUnfulFillableInventoryRemovalSettings());
        q.addValue("ddpPrice", gsProductData.getDdpPrice());
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        return null;
    }

    @Override @Transactional("transactionManager")
    public String insertDe(GsProductData gsProductData) {

        String sql = "insert into product.gs_product "
                + "( base_product_code, sku,  gtin,  product_name, product_category, supplier_company, brand_eng, country_of_origin, hs_code, manufacturer_lead_time, fca_price, product_dimension_l, product_dimension_w, product_dimension_h, product_dimension_unit, product_material_percent, product_weight, product_weight_unit, de_country, de_salesprice, de_note, de_package_dimension_l, de_package_dimension_w, de_package_dimension_h, de_package_dimension_unit, de_package_weight, de_package_weight_unit, de_quantity_per_carton, de_carton_dimension_l, de_carton_dimension_w, de_carton_dimension_h, de_carton_dimension_unit, de_carton_weight, de_carton_weight_unit, de_hs_code,       de_ddp_price  ) values "
                + "( :baseProductCode,  :sku, :gtin, :productName, :productCategory, :supplierCompany, :brandEng, :countryOfOrigin,  :hsCode, :manufacturerLeadTime,  :fcaPrice, :productDimensionL,  :productDimensionW,  :productDimensionH,  :productDimensionUnit,  :productMaterialPercent,  :productWeight, :productWeightUnit,  :country,   :salesPrice,    :note,   :packageDimensionL,     :packageDimensionW,     :packageDimensionH,     :packageDimensionUnit,     :packageWeight,    :packageWeightUnit,     :quantityPerCarton,     :cartonDimensionL,     :cartonDimensionW,     :cartonDimensionH,     :cartonDimensionUnit,     :cartonWeight,    :cartonWeightUnit,     :salesSideHsCode, :ddpPrice  )";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("baseProductCode", gsProductData.getBaseProductCode());
        q.addValue("sku", gsProductData.getSku());
        q.addValue("gtin", gsProductData.getGtin());
        q.addValue("productName", gsProductData.getProductName());
        q.addValue("productCategory", gsProductData.getProductCategory());
        q.addValue("supplierCompany", gsProductData.getSupplierCompany());
        q.addValue("brandEng", gsProductData.getBrandEng());
        q.addValue("countryOfOrigin", gsProductData.getCountryOfOrigin());
        q.addValue("hsCode", gsProductData.getHsCode());
        q.addValue("manufacturerLeadTime", gsProductData.getManufacturerLeadTime());
        q.addValue("fcaPrice", gsProductData.getFcaPrice());
        q.addValue("productDimensionL", gsProductData.getProductDimensionL());
        q.addValue("productDimensionW", gsProductData.getProductDimensionW());
        q.addValue("productDimensionH", gsProductData.getProductDimensionH());
        q.addValue("productDimensionUnit", gsProductData.getProductDimensionUnit());
        q.addValue("productMaterialPercent", gsProductData.getProductMaterialPercent());
        q.addValue("productWeight", gsProductData.getProductWeight());
        q.addValue("productWeightUnit", gsProductData.getProductWeightUnit());
        q.addValue("country", gsProductData.getCountry());
        q.addValue("salesPrice", gsProductData.getSalesPrice());
        q.addValue("note", gsProductData.getNote());
        q.addValue("packageDimensionL", gsProductData.getPackageDimensionL());
        q.addValue("packageDimensionW", gsProductData.getPackageDimensionW());
        q.addValue("packageDimensionH", gsProductData.getPackageDimensionH());
        q.addValue("packageDimensionUnit", gsProductData.getPackageDimensionUnit());
        q.addValue("packageWeight", gsProductData.getPackageWeight());
        q.addValue("packageWeightUnit", gsProductData.getPackageWeightUnit());
        q.addValue("quantityPerCarton", gsProductData.getQuantityPerCarton());
        q.addValue("cartonDimensionL", gsProductData.getCartonDimensionL());
        q.addValue("cartonDimensionW", gsProductData.getCartonDimensionW());
        q.addValue("cartonDimensionH", gsProductData.getCartonDimensionH());
        q.addValue("cartonDimensionUnit", gsProductData.getCartonDimensionUnit());
        q.addValue("cartonWeight", gsProductData.getCartonWeight());
        q.addValue("cartonWeightUnit", gsProductData.getCartonWeightUnit());
        q.addValue("salesSideHsCode", gsProductData.getSalesSideHsCode());
        q.addValue("ddpPrice", gsProductData.getDdpPrice());
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        return null;
    }

    @Override @Transactional("transactionManager")
    public String insertFr(GsProductData gsProductData) {

        String sql = "insert into product.gs_product "
                + "( base_product_code, sku,  gtin,  product_name, product_category, supplier_company, brand_eng, country_of_origin, hs_code, manufacturer_lead_time, fca_price, product_dimension_l, product_dimension_w, product_dimension_h, product_dimension_unit, product_material_percent, product_weight, product_weight_unit, fr_country, fr_salesprice, fr_note, fr_package_dimension_l, fr_package_dimension_w, fr_package_dimension_h, fr_package_dimension_unit, fr_package_weight, fr_package_weight_unit, fr_quantity_per_carton, fr_carton_dimension_l, fr_carton_dimension_w, fr_carton_dimension_h, fr_carton_dimension_unit, fr_carton_weight, fr_carton_weight_unit, fr_hs_code,       fr_ddp_price  ) values "
                + "( :baseProductCode,  :sku, :gtin, :productName, :productCategory, :supplierCompany, :brandEng, :countryOfOrigin,  :hsCode, :manufacturerLeadTime,  :fcaPrice, :productDimensionL,  :productDimensionW,  :productDimensionH,  :productDimensionUnit,  :productMaterialPercent,  :productWeight, :productWeightUnit,  :country,   :salesPrice,    :note,   :packageDimensionL,     :packageDimensionW,     :packageDimensionH,     :packageDimensionUnit,     :packageWeight,    :packageWeightUnit,     :quantityPerCarton,     :cartonDimensionL,     :cartonDimensionW,     :cartonDimensionH,     :cartonDimensionUnit,     :cartonWeight,    :cartonWeightUnit,     :salesSideHsCode, :ddpPrice  )";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("baseProductCode", gsProductData.getBaseProductCode());
        q.addValue("sku", gsProductData.getSku());
        q.addValue("gtin", gsProductData.getGtin());
        q.addValue("productName", gsProductData.getProductName());
        q.addValue("productCategory", gsProductData.getProductCategory());
        q.addValue("supplierCompany", gsProductData.getSupplierCompany());
        q.addValue("brandEng", gsProductData.getBrandEng());
        q.addValue("countryOfOrigin", gsProductData.getCountryOfOrigin());
        q.addValue("hsCode", gsProductData.getHsCode());
        q.addValue("manufacturerLeadTime", gsProductData.getManufacturerLeadTime());
        q.addValue("fcaPrice", gsProductData.getFcaPrice());
        q.addValue("productDimensionL", gsProductData.getProductDimensionL());
        q.addValue("productDimensionW", gsProductData.getProductDimensionW());
        q.addValue("productDimensionH", gsProductData.getProductDimensionH());
        q.addValue("productDimensionUnit", gsProductData.getProductDimensionUnit());
        q.addValue("productMaterialPercent", gsProductData.getProductMaterialPercent());
        q.addValue("productWeight", gsProductData.getProductWeight());
        q.addValue("productWeightUnit", gsProductData.getProductWeightUnit());
        q.addValue("country", gsProductData.getCountry());
        q.addValue("salesPrice", gsProductData.getSalesPrice());
        q.addValue("note", gsProductData.getNote());
        q.addValue("packageDimensionL", gsProductData.getPackageDimensionL());
        q.addValue("packageDimensionW", gsProductData.getPackageDimensionW());
        q.addValue("packageDimensionH", gsProductData.getPackageDimensionH());
        q.addValue("packageDimensionUnit", gsProductData.getPackageDimensionUnit());
        q.addValue("packageWeight", gsProductData.getPackageWeight());
        q.addValue("packageWeightUnit", gsProductData.getPackageWeightUnit());
        q.addValue("quantityPerCarton", gsProductData.getQuantityPerCarton());
        q.addValue("cartonDimensionL", gsProductData.getCartonDimensionL());
        q.addValue("cartonDimensionW", gsProductData.getCartonDimensionW());
        q.addValue("cartonDimensionH", gsProductData.getCartonDimensionH());
        q.addValue("cartonDimensionUnit", gsProductData.getCartonDimensionUnit());
        q.addValue("cartonWeight", gsProductData.getCartonWeight());
        q.addValue("cartonWeightUnit", gsProductData.getCartonWeightUnit());
        q.addValue("salesSideHsCode", gsProductData.getSalesSideHsCode());
        q.addValue("ddpPrice", gsProductData.getDdpPrice());
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        return null;
    }

    @Override @Transactional("transactionManager")
    public String insertIt(GsProductData gsProductData) {

        String sql = "insert into product.gs_product "
                + "( base_product_code, sku,  gtin,  product_name, product_category, supplier_company, brand_eng, country_of_origin, hs_code, manufacturer_lead_time, fca_price, product_dimension_l, product_dimension_w, product_dimension_h, product_dimension_unit, product_material_percent, product_weight, product_weight_unit, it_country, it_salesprice, it_note, it_package_dimension_l, it_package_dimension_w, it_package_dimension_h, it_package_dimension_unit, it_package_weight, it_package_weight_unit, it_quantity_per_carton, it_carton_dimension_l, it_carton_dimension_w, it_carton_dimension_h, it_carton_dimension_unit, it_carton_weight, it_carton_weight_unit, it_hs_code,       it_ddp_price  ) values "
                + "( :baseProductCode,  :sku, :gtin, :productName, :productCategory, :supplierCompany, :brandEng, :countryOfOrigin,  :hsCode, :manufacturerLeadTime,  :fcaPrice, :productDimensionL,  :productDimensionW,  :productDimensionH,  :productDimensionUnit,  :productMaterialPercent,  :productWeight, :productWeightUnit,  :country,   :salesPrice,    :note,   :packageDimensionL,     :packageDimensionW,     :packageDimensionH,     :packageDimensionUnit,     :packageWeight,    :packageWeightUnit,     :quantityPerCarton,     :cartonDimensionL,     :cartonDimensionW,     :cartonDimensionH,     :cartonDimensionUnit,     :cartonWeight,    :cartonWeightUnit,     :salesSideHsCode, :ddpPrice  )";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("baseProductCode", gsProductData.getBaseProductCode());
        q.addValue("sku", gsProductData.getSku());
        q.addValue("gtin", gsProductData.getGtin());
        q.addValue("productName", gsProductData.getProductName());
        q.addValue("productCategory", gsProductData.getProductCategory());
        q.addValue("supplierCompany", gsProductData.getSupplierCompany());
        q.addValue("brandEng", gsProductData.getBrandEng());
        q.addValue("countryOfOrigin", gsProductData.getCountryOfOrigin());
        q.addValue("hsCode", gsProductData.getHsCode());
        q.addValue("manufacturerLeadTime", gsProductData.getManufacturerLeadTime());
        q.addValue("fcaPrice", gsProductData.getFcaPrice());
        q.addValue("productDimensionL", gsProductData.getProductDimensionL());
        q.addValue("productDimensionW", gsProductData.getProductDimensionW());
        q.addValue("productDimensionH", gsProductData.getProductDimensionH());
        q.addValue("productDimensionUnit", gsProductData.getProductDimensionUnit());
        q.addValue("productMaterialPercent", gsProductData.getProductMaterialPercent());
        q.addValue("productWeight", gsProductData.getProductWeight());
        q.addValue("productWeightUnit", gsProductData.getProductWeightUnit());
        q.addValue("country", gsProductData.getCountry());
        q.addValue("salesPrice", gsProductData.getSalesPrice());
        q.addValue("note", gsProductData.getNote());
        q.addValue("packageDimensionL", gsProductData.getPackageDimensionL());
        q.addValue("packageDimensionW", gsProductData.getPackageDimensionW());
        q.addValue("packageDimensionH", gsProductData.getPackageDimensionH());
        q.addValue("packageDimensionUnit", gsProductData.getPackageDimensionUnit());
        q.addValue("packageWeight", gsProductData.getPackageWeight());
        q.addValue("packageWeightUnit", gsProductData.getPackageWeightUnit());
        q.addValue("quantityPerCarton", gsProductData.getQuantityPerCarton());
        q.addValue("cartonDimensionL", gsProductData.getCartonDimensionL());
        q.addValue("cartonDimensionW", gsProductData.getCartonDimensionW());
        q.addValue("cartonDimensionH", gsProductData.getCartonDimensionH());
        q.addValue("cartonDimensionUnit", gsProductData.getCartonDimensionUnit());
        q.addValue("cartonWeight", gsProductData.getCartonWeight());
        q.addValue("cartonWeightUnit", gsProductData.getCartonWeightUnit());
        q.addValue("salesSideHsCode", gsProductData.getSalesSideHsCode());
        q.addValue("ddpPrice", gsProductData.getDdpPrice());
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        return null;
    }

    @Override @Transactional("transactionManager")
    public String insertEs(GsProductData gsProductData) {

        String sql = "insert into product.gs_product "
                + "( base_product_code, sku,  gtin,  product_name, product_category, supplier_company, brand_eng, country_of_origin, hs_code, manufacturer_lead_time, fca_price, product_dimension_l, product_dimension_w, product_dimension_h, product_dimension_unit, product_material_percent, product_weight, product_weight_unit, es_country, es_salesprice, es_note, es_package_dimension_l, es_package_dimension_w, es_package_dimension_h, es_package_dimension_unit, es_package_weight, es_package_weight_unit, es_quantity_per_carton, es_carton_dimension_l, es_carton_dimension_w, es_carton_dimension_h, es_carton_dimension_unit, es_carton_weight, es_carton_weight_unit, es_hs_code,       es_ddp_price  ) values "
                + "( :baseProductCode,  :sku, :gtin, :productName, :productCategory, :supplierCompany, :brandEng, :countryOfOrigin,  :hsCode, :manufacturerLeadTime,  :fcaPrice, :productDimensionL,  :productDimensionW,  :productDimensionH,  :productDimensionUnit,  :productMaterialPercent,  :productWeight, :productWeightUnit,  :country,   :salesPrice,    :note,   :packageDimensionL,     :packageDimensionW,     :packageDimensionH,     :packageDimensionUnit,     :packageWeight,    :packageWeightUnit,     :quantityPerCarton,     :cartonDimensionL,     :cartonDimensionW,     :cartonDimensionH,     :cartonDimensionUnit,     :cartonWeight,    :cartonWeightUnit,     :salesSideHsCode, :ddpPrice  )";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("baseProductCode", gsProductData.getBaseProductCode());
        q.addValue("sku", gsProductData.getSku());
        q.addValue("gtin", gsProductData.getGtin());
        q.addValue("productName", gsProductData.getProductName());
        q.addValue("productCategory", gsProductData.getProductCategory());
        q.addValue("supplierCompany", gsProductData.getSupplierCompany());
        q.addValue("brandEng", gsProductData.getBrandEng());
        q.addValue("countryOfOrigin", gsProductData.getCountryOfOrigin());
        q.addValue("hsCode", gsProductData.getHsCode());
        q.addValue("manufacturerLeadTime", gsProductData.getManufacturerLeadTime());
        q.addValue("fcaPrice", gsProductData.getFcaPrice());
        q.addValue("productDimensionL", gsProductData.getProductDimensionL());
        q.addValue("productDimensionW", gsProductData.getProductDimensionW());
        q.addValue("productDimensionH", gsProductData.getProductDimensionH());
        q.addValue("productDimensionUnit", gsProductData.getProductDimensionUnit());
        q.addValue("productMaterialPercent", gsProductData.getProductMaterialPercent());
        q.addValue("productWeight", gsProductData.getProductWeight());
        q.addValue("productWeightUnit", gsProductData.getProductWeightUnit());
        q.addValue("country", gsProductData.getCountry());
        q.addValue("salesPrice", gsProductData.getSalesPrice());
        q.addValue("note", gsProductData.getNote());
        q.addValue("packageDimensionL", gsProductData.getPackageDimensionL());
        q.addValue("packageDimensionW", gsProductData.getPackageDimensionW());
        q.addValue("packageDimensionH", gsProductData.getPackageDimensionH());
        q.addValue("packageDimensionUnit", gsProductData.getPackageDimensionUnit());
        q.addValue("packageWeight", gsProductData.getPackageWeight());
        q.addValue("packageWeightUnit", gsProductData.getPackageWeightUnit());
        q.addValue("quantityPerCarton", gsProductData.getQuantityPerCarton());
        q.addValue("cartonDimensionL", gsProductData.getCartonDimensionL());
        q.addValue("cartonDimensionW", gsProductData.getCartonDimensionW());
        q.addValue("cartonDimensionH", gsProductData.getCartonDimensionH());
        q.addValue("cartonDimensionUnit", gsProductData.getCartonDimensionUnit());
        q.addValue("cartonWeight", gsProductData.getCartonWeight());
        q.addValue("cartonWeightUnit", gsProductData.getCartonWeightUnit());
        q.addValue("salesSideHsCode", gsProductData.getSalesSideHsCode());
        q.addValue("ddpPrice", gsProductData.getDdpPrice());
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        return null;
    }
}
