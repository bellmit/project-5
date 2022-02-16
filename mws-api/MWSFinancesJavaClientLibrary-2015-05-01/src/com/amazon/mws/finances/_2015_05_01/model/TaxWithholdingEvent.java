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
 * Tax Withholding Event
 * API Version: 2015-05-01
 * Library Version: 2020-08-26
 * Generated: Fri Sep 18 08:52:02 PDT 2020
 */
package com.amazon.mws.finances._2015_05_01.model;

import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.client.*;

/**
 * TaxWithholdingEvent complex type.
 *
 * XML schema:
 *
 * <pre>
 * &lt;complexType name="TaxWithholdingEvent"&gt;
 *    &lt;complexContent&gt;
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *          &lt;sequence&gt;
 *             &lt;element name="PostedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *             &lt;element name="TaxWithholdingPeriod" type="{http://mws.amazonservices.com/Finances/2015-05-01}TaxWithholdingTimeRange" minOccurs="0"/&gt;
 *             &lt;element name="BaseAmount" type="{http://mws.amazonservices.com/Finances/2015-05-01}Currency" minOccurs="0"/&gt;
 *             &lt;element name="WithheldAmount" type="{http://mws.amazonservices.com/Finances/2015-05-01}Currency" minOccurs="0"/&gt;
 *          &lt;/sequence&gt;
 *       &lt;/restriction&gt;
 *    &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
public class TaxWithholdingEvent extends AbstractMwsObject {

    private XMLGregorianCalendar postedDate;

    private TaxWithholdingTimeRange taxWithholdingPeriod;

    private Currency baseAmount;

    private Currency withheldAmount;

    /**
     * Get the value of PostedDate.
     *
     * @return The value of PostedDate.
     */
    public XMLGregorianCalendar getPostedDate() {
        return postedDate;
    }

    /**
     * Set the value of PostedDate.
     *
     * @param postedDate
     *            The new value to set.
     */
    public void setPostedDate(XMLGregorianCalendar postedDate) {
        this.postedDate = postedDate;
    }

    /**
     * Check to see if PostedDate is set.
     *
     * @return true if PostedDate is set.
     */
    public boolean isSetPostedDate() {
        return postedDate != null;
    }

    /**
     * Set the value of PostedDate, return this.
     *
     * @param postedDate
     *             The new value to set.
     *
     * @return This instance.
     */
    public TaxWithholdingEvent withPostedDate(XMLGregorianCalendar postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    /**
     * Get the value of TaxWithholdingPeriod.
     *
     * @return The value of TaxWithholdingPeriod.
     */
    public TaxWithholdingTimeRange getTaxWithholdingPeriod() {
        return taxWithholdingPeriod;
    }

    /**
     * Set the value of TaxWithholdingPeriod.
     *
     * @param taxWithholdingPeriod
     *            The new value to set.
     */
    public void setTaxWithholdingPeriod(TaxWithholdingTimeRange taxWithholdingPeriod) {
        this.taxWithholdingPeriod = taxWithholdingPeriod;
    }

    /**
     * Check to see if TaxWithholdingPeriod is set.
     *
     * @return true if TaxWithholdingPeriod is set.
     */
    public boolean isSetTaxWithholdingPeriod() {
        return taxWithholdingPeriod != null;
    }

    /**
     * Set the value of TaxWithholdingPeriod, return this.
     *
     * @param taxWithholdingPeriod
     *             The new value to set.
     *
     * @return This instance.
     */
    public TaxWithholdingEvent withTaxWithholdingPeriod(TaxWithholdingTimeRange taxWithholdingPeriod) {
        this.taxWithholdingPeriod = taxWithholdingPeriod;
        return this;
    }

    /**
     * Get the value of BaseAmount.
     *
     * @return The value of BaseAmount.
     */
    public Currency getBaseAmount() {
        return baseAmount;
    }

    /**
     * Set the value of BaseAmount.
     *
     * @param baseAmount
     *            The new value to set.
     */
    public void setBaseAmount(Currency baseAmount) {
        this.baseAmount = baseAmount;
    }

    /**
     * Check to see if BaseAmount is set.
     *
     * @return true if BaseAmount is set.
     */
    public boolean isSetBaseAmount() {
        return baseAmount != null;
    }

    /**
     * Set the value of BaseAmount, return this.
     *
     * @param baseAmount
     *             The new value to set.
     *
     * @return This instance.
     */
    public TaxWithholdingEvent withBaseAmount(Currency baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    /**
     * Get the value of WithheldAmount.
     *
     * @return The value of WithheldAmount.
     */
    public Currency getWithheldAmount() {
        return withheldAmount;
    }

    /**
     * Set the value of WithheldAmount.
     *
     * @param withheldAmount
     *            The new value to set.
     */
    public void setWithheldAmount(Currency withheldAmount) {
        this.withheldAmount = withheldAmount;
    }

    /**
     * Check to see if WithheldAmount is set.
     *
     * @return true if WithheldAmount is set.
     */
    public boolean isSetWithheldAmount() {
        return withheldAmount != null;
    }

    /**
     * Set the value of WithheldAmount, return this.
     *
     * @param withheldAmount
     *             The new value to set.
     *
     * @return This instance.
     */
    public TaxWithholdingEvent withWithheldAmount(Currency withheldAmount) {
        this.withheldAmount = withheldAmount;
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
        postedDate = r.read("PostedDate", XMLGregorianCalendar.class);
        taxWithholdingPeriod = r.read("TaxWithholdingPeriod", TaxWithholdingTimeRange.class);
        baseAmount = r.read("BaseAmount", Currency.class);
        withheldAmount = r.read("WithheldAmount", Currency.class);
    }

    /**
     * Write members to a MwsWriter.
     *
     * @param w
     *      The writer to write to.
     */
    @Override
    public void writeFragmentTo(MwsWriter w) {
        w.write("PostedDate", postedDate);
        w.write("TaxWithholdingPeriod", taxWithholdingPeriod);
        w.write("BaseAmount", baseAmount);
        w.write("WithheldAmount", withheldAmount);
    }

    /**
     * Write tag, xmlns and members to a MwsWriter.
     *
     * @param w
     *         The Writer to write to.
     */
    @Override
    public void writeTo(MwsWriter w) {
        w.write("http://mws.amazonservices.com/Finances/2015-05-01", "TaxWithholdingEvent",this);
    }


    /** Default constructor. */
    public TaxWithholdingEvent() {
        super();
    }

}
