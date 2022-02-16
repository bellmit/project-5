package com.kindminds.drs.persist.data.row.logistics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.kindminds.drs.api.data.row.logistics.IvsProdDocRequirementRow;
import org.bson.types.ObjectId;




//@Document(collection = "IvsProdDocReq")
public class IvsProdDocRequirementRowImpl implements IvsProdDocRequirementRow {


    @JsonSerialize(using = ToStringSerializer.class)
    //@Id
    ObjectId _id;

    
    String ivsName      = "";

    
    int boxNum      = 0;

    
    int mixedBoxLineSeq      = 0;

    
    Boolean g3Invoice      = false;

    
    Boolean msds      = false;

    
    Boolean dangerousGoods      = false;

    
    Boolean un383      = false;

    
    Boolean ppq505     = false;

    
    Boolean usFDA      = false;

    
    Boolean usMID     = false;

    
    Boolean fatContentSpecification     = false;

    
    Boolean exportTariffRequirement     = false;

    
    Boolean additionalRemarks     = false;

    @Override
    public ObjectId get_id() {
        return _id;
    }

    @Override
    public String getIvsName() {
        return ivsName;
    }

    @Override
    public int getBoxNum() {
        return boxNum;
    }

    @Override
    public int getMixedBoxLineSeq() {
        return mixedBoxLineSeq;
    }

    @Override
    public Boolean getG3Invoice() {
        return g3Invoice;
    }

    @Override
    public Boolean getMsds() {
        return msds;
    }

    @Override
    public Boolean getDangerousGoods() {
        return dangerousGoods;
    }

    @Override
    public Boolean getUn383() {
        return un383;
    }

    @Override
    public Boolean getPpq505() {
        return ppq505;
    }

    @Override
    public Boolean getUsFDA() {
        return usFDA;
    }

    @Override
    public Boolean getUsMID() {
        return usMID;
    }

    @Override
    public Boolean getFatContentSpecification() {
        return fatContentSpecification;
    }

    @Override
    public Boolean getExportTariffRequirement() {
        return exportTariffRequirement;
    }

    @Override
    public Boolean getAdditionalRemarks() {
        return additionalRemarks;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }
}