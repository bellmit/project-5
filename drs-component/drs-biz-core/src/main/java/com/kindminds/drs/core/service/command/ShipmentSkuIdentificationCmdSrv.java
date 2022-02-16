package com.kindminds.drs.core.service.command;

import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShipmentUnsDao;
import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import com.kindminds.drs.api.v1.model.logistics.ShipmentSkuIdentification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentSkuIdentificationCmdSrv {


    //unsDao
    @Autowired
    private MaintainShipmentUnsDao unsDao;

    //shipmentSkuIdentificationDao
    @Autowired
    private ShipmentSkuIdentificationDao dao;

    //shipmentSkuIdentification


    public void createShipmentSkuIdentification(String uns){

        if(checkShipmentSkuIdentificationAvailable(uns) == true) {
            List<ShipmentSkuIdentification> ssi = this.dao.doShipmentSkuIdentification(uns);
            this.dao.add(ssi);
        }

    }

    private boolean checkShipmentSkuIdentificationAvailable(String uns){
        List<Object[]> result=this.dao.queryByUns(uns);
        if (result.size()==0){
            return true;
        }else{
            return false;
        }

    }

}
