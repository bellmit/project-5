/*******************************************************************************
 * Copyright 2009-2021 Amazon Services. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 *
 * You may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *******************************************************************************
 * Fulfillment Execution
 * API Version: 2013-09-01
 * Library Version: 2021-01-06
 * Generated: Wed Jan 06 18:02:45 UTC 2021
 */
package com.amazonservices.mws.orders._2013_09_01.model;

import com.amazonservices.mws.client.*;

/**
 * FulfillmentExecution complex type.
 *
 * XML schema:
 *
 * <pre>
 * &lt;complexType name="FulfillmentExecution"&gt;
 *    &lt;complexContent&gt;
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *          &lt;sequence&gt;
 *             &lt;element name="FulfillmentSupplySourceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *          &lt;/sequence&gt;
 *       &lt;/restriction&gt;
 *    &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
public class FulfillmentExecution extends AbstractMwsObject {

    private String fulfillmentSupplySourceId;

    /**
     * Get the value of FulfillmentSupplySourceId.
     *
     * @return The value of FulfillmentSupplySourceId.
     */
    public String getFulfillmentSupplySourceId() {
        return fulfillmentSupplySourceId;
    }

    /**
     * Set the value of FulfillmentSupplySourceId.
     *
     * @param fulfillmentSupplySourceId
     *            The new value to set.
     */
    public void setFulfillmentSupplySourceId(String fulfillmentSupplySourceId) {
        this.fulfillmentSupplySourceId = fulfillmentSupplySourceId;
    }

    /**
     * Check to see if FulfillmentSupplySourceId is set.
     *
     * @return true if FulfillmentSupplySourceId is set.
     */
    public boolean isSetFulfillmentSupplySourceId() {
        return fulfillmentSupplySourceId != null;
    }

    /**
     * Set the value of FulfillmentSupplySourceId, return this.
     *
     * @param fulfillmentSupplySourceId
     *             The new value to set.
     *
     * @return This instance.
     */
    public FulfillmentExecution withFulfillmentSupplySourceId(String fulfillmentSupplySourceId) {
        this.fulfillmentSupplySourceId = fulfillmentSupplySourceId;
        return this;
    }

    /**
     * Read members from a MwsReader.
     *
     * @param r
     *      The reader to read from.
     */
    @Override
    public void readFragmentFrom(MwsReader r) {
        fulfillmentSupplySourceId = r.read("FulfillmentSupplySourceId", String.class);
    }

    /**
     * Write members to a MwsWriter.
     *
     * @param w
     *      The writer to write to.
     */
    @Override
    public void writeFragmentTo(MwsWriter w) {
        w.write("FulfillmentSupplySourceId", fulfillmentSupplySourceId);
    }

    /**
     * Write tag, xmlns and members to a MwsWriter.
     *
     * @param w
     *         The Writer to write to.
     */
    @Override
    public void writeTo(MwsWriter w) {
        w.write("https://mws.amazonservices.com/Orders/2013-09-01", "FulfillmentExecution",this);
    }


    /** Default constructor. */
    public FulfillmentExecution() {
        super();
    }

}
