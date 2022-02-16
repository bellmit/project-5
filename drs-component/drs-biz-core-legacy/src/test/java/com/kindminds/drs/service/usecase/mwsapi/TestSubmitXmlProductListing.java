package com.kindminds.drs.service.usecase.mwsapi;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.adapter.AmazonMwsFeedAdapter;
import com.kindminds.drs.MwsFeedType;

import com.kindminds.drs.mws.xml.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestSubmitXmlProductListing {

    //todo arthur
    private AmazonMwsFeedAdapter adapter = null;//new AmazonMwsFeedAdapterImpl();

    public Jaxb2Marshaller createJaxb2Marshaller(String contextPath, String resourcePath) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(contextPath);
        Resource schema = new ClassPathResource(resourcePath);
        marshaller.setSchema(schema);

        Map<String, Object> properties = new HashMap<>();
        properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.setMarshallerProperties(properties);

        return marshaller;
    }


    @Test
    public void testMarshallerProduct() {
        AmazonEnvelope envelope = new AmazonEnvelope();
        Header header = new Header();
        header.setDocumentVersion("1.01");
        header.setMerchantIdentifier("A3QY80NORGXT27");
        envelope.setHeader(header);
        envelope.setMessageType("Product");

        AmazonEnvelope.Message message = new AmazonEnvelope.Message();
        message.setMessageID(BigInteger.ONE);
        message.setOperationType("Update");

        Product product = new Product();
        product.setSKU("5T1-Parent2");
        StandardProductID standardProductID = new StandardProductID();
        standardProductID.setType("EAN");
        standardProductID.setValue("4712805507283");
        product.setStandardProductID(standardProductID);

        Product.DescriptionData descriptionData = new Product.DescriptionData();
        descriptionData.setBrand("MOCHI MOCHI");
        descriptionData.setTitle("Silicone Trivet Mat");
        descriptionData.setItemType("kitchentools");
        product.setDescriptionData(descriptionData);

        message.setProduct(product);
        envelope.getMessage().add(message);


        Jaxb2Marshaller marshaller = createJaxb2Marshaller("com.kindminds.drs.mws.xml", "xjb/xsd/amzn-envelope.xsd");

        StringWriter writer = new StringWriter();

        marshaller.marshal(envelope, new StreamResult(writer));

        String xml = writer.toString();

        System.out.println(xml);

        adapter.submitFeed(xml, Marketplace.AMAZON_COM, MwsFeedType.Product_Feed);

        //249518018584
        //249553018584
        //249556018584
    }

    @Test
    public void testRequestFeedSubmissionResult() {
        adapter.getFeedSubmissionResult(Marketplace.AMAZON_COM, "249553018584");
    }


    @Test
    public void testMarshallerAutoAccessory() {
        AutoAccessory autoAccessory = new AutoAccessory();
        AutoAccessory.ProductType productType = new AutoAccessory.ProductType();
        Ridingboots ridingboots2 = new Ridingboots();
        ridingboots2.setModelName("Riding boots 2 Test");
        productType.setRidingboots(ridingboots2);
        autoAccessory.setProductType(productType);

        Jaxb2Marshaller marshaller = createJaxb2Marshaller("com.kindminds.drs.mws.xml", "xjb/xsd/AutoAccessory.xsd");

        StringWriter writer = new StringWriter();

        marshaller.marshal(autoAccessory, new StreamResult(writer));

        String xml = writer.toString();

        System.out.println(xml);
    }




}
