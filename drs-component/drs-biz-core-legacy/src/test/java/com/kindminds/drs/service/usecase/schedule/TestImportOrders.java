package com.kindminds.drs.service.usecase.schedule;

import com.kindminds.drs.Country;

import com.kindminds.drs.api.schedule.job.ImportAmazonOrderUco;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestImportOrders {

    @Autowired private ImportAmazonOrderUco uco;




    @Test
    @Transactional
    public void test(){

        System.out.println(Country.NA.name());

        /*
        ((ImportAmazonOrderUcoImpl)(this.uco)).setCountry(Country.NA);
        this.uco.importOrders();

        */

    }


}