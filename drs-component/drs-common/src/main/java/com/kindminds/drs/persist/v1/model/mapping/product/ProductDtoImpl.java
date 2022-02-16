package com.kindminds.drs.persist.v1.model.mapping.product;








import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;

import java.util.List;


public class ProductDtoImpl extends ProductDto {

        //@Id
        ////@Column(name="id")
        String productBaseCode;
        //@Column(name="supplier_kcode")
        String  supplierKcode;
        //@Column(name="json_data")
        String jsonData;


        ProductDetailImpl productInfoSource;

        ProductDetailImpl productMarketingMaterialSource;

        List<ProductDetail> productInfoMarketSide;

        List<ProductDetail> productMarketingMaterialMarketSide;

        public ProductDtoImpl(){}

        public ProductDtoImpl(
                String productBaseCode,
                String supplierKcode,
                String jsonData,
                ProductDetailImpl  productInfoSource,
                ProductDetailImpl productMarketingMaterialSource,
                List<ProductDetail> productInfoMarketSide,
                List<ProductDetail> productMarketingMaterialMarketSide) {


                this.productBaseCode = productBaseCode;
                this.supplierKcode = supplierKcode;
                this.jsonData = jsonData;
                this.productInfoSource = productInfoSource;
                this.productMarketingMaterialSource = productMarketingMaterialSource;
                this.productInfoMarketSide = productInfoMarketSide;
                this.productMarketingMaterialMarketSide = productMarketingMaterialMarketSide;

        }


        @Override
        public String getProductBaseCode() {
                return this.productBaseCode;
        }

        @Override
        public String getSupplierKcode() {
                return this.supplierKcode;
        }

        @Override
        public String getJsonData() {
                return this.jsonData;
        }

        @Override
        public ProductDetail getProductInfoSource() {
                return this.productInfoSource;
        }

        @Override
        public ProductDetail getProductMarketingMaterialSource() {
                return this.productMarketingMaterialSource;
        }

        @Override
        public List getProductInfoMarketSide() {
                return this.productInfoMarketSide;
        }

        @Override
        public List getProductMarketingMaterialMarketSide() {
                return this.productMarketingMaterialMarketSide;
        }
}
