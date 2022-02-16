package com.kindminds.drs.persist.v1.model.mapping.product;



import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;




public  class ProductDetailImpl extends ProductDetail {

         String id ;
        //@Id ////@Column(name="id")
        String productBaseCode ;
        //@Column(name="supplier_kcode")
        String supplierKcode ;
        //@Column(name="json_data")
        String data ;
        //@Column(name="status")
        String status ;

        String country  ;

        public ProductDetailImpl(){}

        public ProductDetailImpl(
                String id ,
                String productBaseCode  ,
                String supplierKcode  ,
                String data  ,
                String  status  ,
                String country  ) {


                this.id = id;
                this.productBaseCode = productBaseCode;
                this.supplierKcode = supplierKcode;
                this.data = data;
                this.status = status;
                this.country = country;


        }


        @Override
        public String getId() {
                return this.id;
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
        public String getData() {
                return this.data;
        }

        @Override
        public String getStatus() {
                return this.status;
        }

        @Override
        public String getCountry() {
                return this.country;
        }
}