/*******************************************************************************
 * Copyright 2009-2016 Amazon Services. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 *
 * You may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *******************************************************************************
 * FBA Outbound Service MWS
 * API Version: 2010-10-01
 * Library Version: 2016-10-19
 * Generated: Wed Oct 19 08:37:51 PDT 2016
 */
package com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01;

import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.ResponseHeaderMetadata;
import com.amazonservices.mws.client.MwsException;
 import com.amazonservices.mws.client.MwsResponseHeaderMetadata;

/**
 * Exceptions thrown by FBAOutboundServiceMWS operations.
 */
public class FBAOutboundServiceMWSException extends MwsException {

    /** For serialization. */
    private static final long serialVersionUID = 1L;

    @Override
    public ResponseHeaderMetadata getResponseHeaderMetadata() {
        MwsResponseHeaderMetadata baseRHMD = super.getResponseHeaderMetadata();
        if(baseRHMD != null) {
            return new ResponseHeaderMetadata(baseRHMD);
        } else {
            return null;
        }
    }

    public void setResponseHeaderMetadata(ResponseHeaderMetadata rhmd) {
        super.setResponseHeaderMetadata(rhmd);
    }

    public FBAOutboundServiceMWSException(String message) {
        super(0, message, null);
    }

    public FBAOutboundServiceMWSException(String message, int statusCode) {
        super(statusCode, message, null);
    }

    public FBAOutboundServiceMWSException(Throwable t) {
        super(0, null, t);
    }

    public FBAOutboundServiceMWSException(String message, Throwable t) {
        super(0, message, t);
    }

    public FBAOutboundServiceMWSException(String message, int statusCode, 
         String errorCode, String errorType, String requestId,
         String xml, ResponseHeaderMetadata rhmd) {
        super(statusCode, message, errorCode, errorType, xml, rhmd);
    }

    public FBAOutboundServiceMWSException(String message, int statusCode, 
         String errorCode, String errorType, String requestId,
         String xml) {
        super(statusCode, message, errorCode, errorType, xml, null);
    }

}

