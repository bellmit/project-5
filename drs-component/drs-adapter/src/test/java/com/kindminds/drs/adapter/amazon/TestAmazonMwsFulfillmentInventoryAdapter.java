package com.kindminds.drs.adapter.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.amazon.AmazonSkuInventorySupply;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestAmazonMwsFulfillmentInventoryAdapter {

    @Test
    public void testrequestListInventorySupplies(){
        AmazonMwsFulfillmentInventoryAdapterImpl adapterImpl = new AmazonMwsFulfillmentInventoryAdapterImpl();

        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        ZonedDateTime startDate = ZonedDateTime.of(2021,9,01,0,0,0,0, ZoneOffset.UTC);
        ZonedDateTime endDate = ZonedDateTime.of(2021,9,16,0,0,0,0,ZoneOffset.UTC);
        XMLGregorianCalendar sd = df.newXMLGregorianCalendar(GregorianCalendar.from(startDate));

        List<String> newAmazonSkuInventorySupply = new ArrayList<>();
        List<AmazonSkuInventorySupply> amazonSkuInventorySupplies = adapterImpl.requestListInventorySupplies(Marketplace.fromName("Amazon.fr"), sd);
        for(AmazonSkuInventorySupply supply:amazonSkuInventorySupplies){
            if (supply.getMarketplaceSku().contains("641")){
                newAmazonSkuInventorySupply.add(supply.getMarketplaceSku() + " - " + supply.getDetailQuantityInStock());
            }
        }
        for(String supply:newAmazonSkuInventorySupply){
            System.out.println(supply);
        }




    }
}
