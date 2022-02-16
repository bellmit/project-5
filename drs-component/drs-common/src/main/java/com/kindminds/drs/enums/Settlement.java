package com.kindminds.drs.enums;

import org.springframework.util.Assert;

public interface Settlement {


    enum AmazonReturnReportLineStatus{
        RETURNED("Unit returned to inventory"),
        DATA_UNAVAILABLE("Data not available"),
        REIMBURSED("Reimbursed");
        private String value;
        AmazonReturnReportLineStatus(String value){this.value = value;}
        public String getValue(){ return this.value; }
        public static AmazonReturnReportLineStatus fromValue(String value){
            AmazonReturnReportLineStatus statusToReturn = null;
            for(AmazonReturnReportLineStatus status: AmazonReturnReportLineStatus.values()){
                if(status.value.equals(value)) statusToReturn = status;
            }
            Assert.notNull(statusToReturn,"statusToReturn null");
            return statusToReturn;
        }
    }
    enum AmazonReturnReportDetailedDisposition{
        CUSTOMER_DAMAGED("CUSTOMER_DAMAGED"),
        DEFECTIVE("DEFECTIVE"),
        SELLABLE("SELLABLE");
        private String value;
        AmazonReturnReportDetailedDisposition(String value){this.value = value;}
        public String getValue(){ return this.value; }
        public static AmazonReturnReportDetailedDisposition fromValue(String value){
            AmazonReturnReportDetailedDisposition dispToReturn = null;
            for(AmazonReturnReportDetailedDisposition disposition: AmazonReturnReportDetailedDisposition.values()){
                if(disposition.value.equals(value)) dispToReturn = disposition;
            }
            Assert.notNull(dispToReturn,"dispToReturn null");
            return dispToReturn;
        }
    }
}


