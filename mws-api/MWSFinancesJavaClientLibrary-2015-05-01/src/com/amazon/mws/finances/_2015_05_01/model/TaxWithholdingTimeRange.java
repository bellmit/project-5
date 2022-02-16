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
 * Tax Withholding Time Range
 * API Version: 2015-05-01
 * Library Version: 2020-08-26
 * Generated: Fri Sep 18 08:52:02 PDT 2020
 */
package com.amazon.mws.finances._2015_05_01.model;

import com.amazonservices.mws.client.*;

/**
 * TaxWithholdingTimeRange complex type.
 *
 * XML schema:
 *
 * <pre>
 * &lt;complexType name="TaxWithholdingTimeRange"&gt;
 *    &lt;complexContent&gt;
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *          &lt;sequence&gt;
 *             &lt;element name="StartDateMillis" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *             &lt;element name="EndDateMillis" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *          &lt;/sequence&gt;
 *       &lt;/restriction&gt;
 *    &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
public class TaxWithholdingTimeRange extends AbstractMwsObject {

    private Long startDateMillis;

    private Long endDateMillis;

    /**
     * Get the value of StartDateMillis.
     *
     * @return The value of StartDateMillis.
     */
    public Long getStartDateMillis() {
        return startDateMillis;
    }

    /**
     * Set the value of StartDateMillis.
     *
     * @param startDateMillis
     *            The new value to set.
     */
    public void setStartDateMillis(Long startDateMillis) {
        this.startDateMillis = startDateMillis;
    }

    /**
     * Check to see if StartDateMillis is set.
     *
     * @return true if StartDateMillis is set.
     */
    public boolean isSetStartDateMillis() {
        return startDateMillis != null;
    }

    /**
     * Set the value of StartDateMillis, return this.
     *
     * @param startDateMillis
     *             The new value to set.
     *
     * @return This instance.
     */
    public TaxWithholdingTimeRange withStartDateMillis(Long startDateMillis) {
        this.startDateMillis = startDateMillis;
        return this;
    }

    /**
     * Get the value of EndDateMillis.
     *
     * @return The value of EndDateMillis.
     */
    public Long getEndDateMillis() {
        return endDateMillis;
    }

    /**
     * Set the value of EndDateMillis.
     *
     * @param endDateMillis
     *            The new value to set.
     */
    public void setEndDateMillis(Long endDateMillis) {
        this.endDateMillis = endDateMillis;
    }

    /**
     * Check to see if EndDateMillis is set.
     *
     * @return true if EndDateMillis is set.
     */
    public boolean isSetEndDateMillis() {
        return endDateMillis != null;
    }

    /**
     * Set the value of EndDateMillis, return this.
     *
     * @param endDateMillis
     *             The new value to set.
     *
     * @return This instance.
     */
    public TaxWithholdingTimeRange withEndDateMillis(Long endDateMillis) {
        this.endDateMillis = endDateMillis;
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
        startDateMillis = r.read("StartDateMillis", Long.class);
        endDateMillis = r.read("EndDateMillis", Long.class);
    }

    /**
     * Write members to a MwsWriter.
     *
     * @param w
     *      The writer to write to.
     */
    @Override
    public void writeFragmentTo(MwsWriter w) {
        w.write("StartDateMillis", startDateMillis);
        w.write("EndDateMillis", endDateMillis);
    }

    /**
     * Write tag, xmlns and members to a MwsWriter.
     *
     * @param w
     *         The Writer to write to.
     */
    @Override
    public void writeTo(MwsWriter w) {
        w.write("http://mws.amazonservices.com/Finances/2015-05-01", "TaxWithholdingTimeRange",this);
    }


    /** Default constructor. */
    public TaxWithholdingTimeRange() {
        super();
    }

}
