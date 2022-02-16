package com.kindminds.drs.service.usecase;

import com.kindminds.drs.core.service.command.ShipmentSkuIdentificationCmdSrv;
import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContextForShipment.xml" })
public class TestShipmentSkuIdentificationUco {

    @Autowired private ShipmentSkuIdentificationCmdSrv cmdSrv;
    @Autowired private ShipmentSkuIdentificationDao dao;

    @Test
    public void doShipmentSkuIdentification(){
        //String uns ="URRRR";
        String uns ="UNS-K2-522";
        //this.dao.doShipmentSkuIdentification(uns);
        this.dao.queryByUns(uns);
        this.cmdSrv.createShipmentSkuIdentification(uns);



    }


}
