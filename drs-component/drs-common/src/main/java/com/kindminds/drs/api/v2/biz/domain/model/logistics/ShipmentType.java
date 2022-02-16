package com.kindminds.drs.api.v2.biz.domain.model.logistics;

public  enum  ShipmentType  {

    INVENTORY("Supplier Inventory"),
    UNIFIED("Unified");

    ShipmentType(String value){this.value = value;}
    private String value;

    public String getValue(){ return this.value; }
    public static ShipmentType fromValue(String value){
        ShipmentType type = null;
        for(ShipmentType st: ShipmentType.values()){
            if(st.value.equals(value)) type = st;
        }
        return type;
    }
}
