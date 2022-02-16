package com.kindminds.drs.api.v2.biz.domain.model.logistics;


public  enum  IvsLineitemProductVerificationStatus {

    DEFAULT("New"),
    TO_BE_CONFIRMED("To be confirmed"),
    INVALID("Invalid"),
    CONFIRMED("Confirmed");

    IvsLineitemProductVerificationStatus(String value){this.value = value;}
    private String value;
    public String getValue(){ return this.value; }

    public static IvsLineitemProductVerificationStatus fromValue(String value){
        IvsLineitemProductVerificationStatus status = null;
        for(IvsLineitemProductVerificationStatus st: IvsLineitemProductVerificationStatus.values()){
            if(st.value.equals(value)) status = st;
        }
        return status;
    }


}