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
 * List Orders Order Item
 * API Version: 2013-09-01
 * Library Version: 2021-01-06
 * Generated: Wed Jan 06 18:02:45 UTC 2021
 */
package com.amazonservices.mws.orders._2013_09_01.model;

import com.amazonservices.mws.client.*;

/**
 * ListOrdersOrderItem complex type.
 *
 * XML schema:
 *
 * <pre>
 * &lt;complexType name="ListOrdersOrderItem"&gt;
 *    &lt;complexContent&gt;
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *          &lt;sequence&gt;
 *             &lt;element name="StoreChainStoreId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *          &lt;/sequence&gt;
 *       &lt;/restriction&gt;
 *    &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
public class ListOrdersOrderItem extends AbstractMwsObject {

    private String storeChainStoreId;

    /**
     * Get the value of StoreChainStoreId.
     *
     * @return The value of StoreChainStoreId.
     */
    public String getStoreChainStoreId() {
        return storeChainStoreId;
    }

    /**
     * Set the value of StoreChainStoreId.
     *
     * @param storeChainStoreId
     *            The new value to set.
     */
    public void setStoreChainStoreId(String storeChainStoreId) {
        this.storeChainStoreId = storeChainStoreId;
    }

    /**
     * Check to see if StoreChainStoreId is set.
     *
     * @return true if StoreChainStoreId is set.
     */
    public boolean isSetStoreChainStoreId() {
        return storeChainStoreId != null;
    }

    /**
     * Set the value of StoreChainStoreId, return this.
     *
     * @param storeChainStoreId
     *             The new value to set.
     *
     * @return This instance.
     */
    public ListOrdersOrderItem withStoreChainStoreId(String storeChainStoreId) {
        this.storeChainStoreId = storeChainStoreId;
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
        storeChainStoreId = r.read("StoreChainStoreId", String.class);
    }

    /**
     * Write members to a MwsWriter.
     *
     * @param w
     *      The writer to write to.
     */
    @Override
    public void writeFragmentTo(MwsWriter w) {
        w.write("StoreChainStoreId", storeChainStoreId);
    }

    /**
     * Write tag, xmlns and members to a MwsWriter.
     *
     * @param w
     *         The Writer to write to.
     */
    @Override
    public void writeTo(MwsWriter w) {
        w.write("https://mws.amazonservices.com/Orders/2013-09-01", "ListOrdersOrderItem",this);
    }


    /** Default constructor. */
    public ListOrdersOrderItem() {
        super();
    }

}
