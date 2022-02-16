package com.kindminds.drs.api.data.row.logistics;

import org.bson.types.ObjectId;

public interface IvsProdDocRequirementRow {

    ObjectId get_id();

    String getIvsName();

    int getBoxNum();

    int getMixedBoxLineSeq();

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