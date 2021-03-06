/*******************************************************************************
 * Copyright 2009-2020 Amazon Services. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 *
 * You may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *******************************************************************************
 * List Financial Events Result
 * API Version: 2015-05-01
 * Library Version: 2020-08-26
 * Generated: Fri Sep 18 08:52:02 PDT 2020
 */
package com.amazon.mws.finances._2015_05_01.model;

import com.amazonservices.mws.client.*;

/**
 * ListFinancialEventsResult complex type.
 *
 * XML schema:
 *
 * <pre>
 * &lt;complexType name="ListFinancialEventsResult"&gt;
 *    &lt;complexContent&gt;
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *          &lt;sequence&gt;
 *             &lt;element name="NextToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *             &lt;element name="FinancialEvents" type="{http://mws.amazonservices.com/Finances/2015-05-01}FinancialEvents" minOccurs="0"/&gt;
 *          &lt;/sequence&gt;
 *       &lt;/restriction&gt;
 *    &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
public class ListFinancialEventsResult extends AbstractMwsObject {

    private String nextToken;

    private FinancialEvents financialEvents;

    /**
     * Get the value of NextToken.
     *
     * @return The value of NextToken.
     */
    public String getNextToken() {
        return nextToken;
    }

    /**
     * Set the value of NextToken.
     *
     * @param nextToken
     *            The new value to set.
     */
    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    /**
     * Check to see if NextToken is set.
     *
     * @return true if NextToken is set.
     */
    public boolean isSetNextToken() {
        return nextToken != null;
    }

    /**
     * Set the value of NextToken, return this.
     *
     * @param nextToken
     *             The new value to set.
     *
     * @return This instance.
     */
    public ListFinancialEventsResult withNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    /**
     * Get the value of FinancialEvents.
     *
     * @return The value of FinancialEvents.
     */
    public FinancialEvents getFinancialEvents() {
        return financialEvents;
    }

    /**
     * Set the value of FinancialEvents.
     *
     * @param financialEvents
     *            The new value to set.
     */
    public void setFinancialEvents(FinancialEvents financialEvents) {
        this.financialEvents = financialEvents;
    }

    /**
     * Check to see if FinancialEvents is set.
     *
     * @return true if FinancialEvents is set.
     */
    public boolean isSetFinancialEvents() {
        return financialEvents != null;
    }

    /**
     * Set the value of FinancialEvents, return this.
     *
     * @param financialEvents
     *             The new value to set.
     *
     * @return This instance.
     */
    public ListFinancialEventsResult withFinancialEvents(FinancialEvents financialEvents) {
        this.financialEvents = financialEvents;
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
        nextToken = r.read("NextToken", String.class);
        financialEvents = r.read("FinancialEvents", FinancialEvents.class);
    }

    /**
     * Write members to a MwsWriter.
     *
     * @param w
     *      The writer to write to.
     */
    @Override
    public void writeFragmentTo(MwsWriter w) {
        w.write("NextToken", nextToken);
        w.write("FinancialEvents", financialEvents);
    }

    /**
     * Write tag, xmlns and members to a MwsWriter.
     *
     * @param w
     *         The Writer to write to.
     */
    @Override
    public void writeTo(MwsWriter w) {
        w.write("http://mws.amazonservices.com/Finances/2015-05-01", "ListFinancialEventsResult",this);
    }


    /** Default constructor. */
    public ListFinancialEventsResult() {
        super();
    }

}
