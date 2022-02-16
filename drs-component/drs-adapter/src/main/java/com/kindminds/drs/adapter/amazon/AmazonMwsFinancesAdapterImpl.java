package com.kindminds.drs.adapter.amazon;

import com.amazon.mws.finances._2015_05_01.model.*;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.adapter.AmazonMwsFinancesAdapter;
import com.kindminds.drs.adapter.amazon.config.*;
import com.kindminds.drs.api.v2.biz.domain.model.finance.*;
import com.kindminds.drs.util.DateHelper;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


//@Service
public class AmazonMwsFinancesAdapterImpl implements AmazonMwsFinancesAdapter {

    private AmazonMwsFinancesConfig config = null;
    private ListFinancialEventsRequest request = null;
    private ListFinancialEventGroupsRequest grpRequest = null;
    private ListFinancialEventsRequest feRequest = null;
    private final String longDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private final String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ssZ";

    private Date formatDate(String dateString) {
        if (dateString.length() == 28) {	// Ex. "2018-09-11T17:12:12.663+0000"
            return DateHelper.toDate(dateString,this.longDateTimeFormat);
        } else {	// Ex. "2018-09-11T17:12:12+0000"
            return DateHelper.toDate(dateString,this.dateTimeFormat);
        }
    }

    private String replaceZwithTimeZone(String originalDateStr){
        return originalDateStr.replaceAll("Z$", "+0000"); }



    private void init( Marketplace marketplace){


        MwsConfigFactory configFactory = new MwsConfigFactory();
        config =
                (AmazonMwsFinancesConfig)configFactory.getConfig(MwsApi.Finances ,
                        marketplace);


       // request = new ListFinancialEventsRequest()
         //       .withSellerId(config.getSellerId());
    }


    @Override
    public void request() {

        List<FinancialEvents> fel = new ArrayList<>();

        this.init(Marketplace.AMAZON_COM);
        grpRequest = new ListFinancialEventGroupsRequest()
                .withSellerId(config.getSellerId());

        feRequest = new ListFinancialEventsRequest().withSellerId(config.getSellerId());

        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        ZonedDateTime startDate = ZonedDateTime.of(2020,12,01,0,0,0,0, ZoneOffset.UTC);
        ZonedDateTime endDate = ZonedDateTime.of(2020,12,16,0,0,0,0,ZoneOffset.UTC);
        XMLGregorianCalendar sd = df.newXMLGregorianCalendar(GregorianCalendar.from(startDate));


        System.out.println(endDate);
        XMLGregorianCalendar ed = df
                .newXMLGregorianCalendar(GregorianCalendar.from(endDate));

        grpRequest.setFinancialEventGroupStartedAfter(sd);
        grpRequest.setFinancialEventGroupStartedBefore(ed);

        feRequest.setPostedBefore(ed);
        feRequest.setPostedAfter(sd);
        //feRequest.setFinancialEventGroupId("TYbrayPMXpvnoYN-9_9vdMxJqWsRm_dDZPCDN9_-iQo");
        ListFinancialEventsResponse response = config.getClient().listFinancialEvents(feRequest);



        System.out.println(response.getListFinancialEventsResult().toJSON());
        //com.kindminds.drs.api.v1.model.finance.test
        ListFinancialEventsResult fer=response.getListFinancialEventsResult();
        String nextToken=response.getListFinancialEventsResult().getNextToken();

        FinancialEvents firstFe = fer.getFinancialEvents();
        fel.add(firstFe);

        while(nextToken!=null){
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
            }
            ListFinancialEventsByNextTokenRequest nextTokenRequest =
                    new ListFinancialEventsByNextTokenRequest(nextToken).withSellerId(config.getSellerId());

            ListFinancialEventsByNextTokenResponse nextResponse =config.getClient().listFinancialEventsByNextToken(nextTokenRequest);
            ListFinancialEventsByNextTokenResult eventResult =nextResponse.getListFinancialEventsByNextTokenResult();
            FinancialEvents fent =eventResult.getFinancialEvents();
            nextToken =eventResult.getNextToken();
            fel.add(fent);

        }


        //ShipmentEvent se=fe.getShipmentEventList().get(0);
        //List<ShipmentEvent>refundevent =fe.getRefundEventList();
        //int size =fe.getShipmentEventList().size();

        //response.getListFinancialEventsResult().getFinancialEvents().

        //ListFinancialEventGroupsResponse listFinancialEventGroupsResponse =
          //      config.getClient().listFinancialEventGroups(grpRequest);
 /*
       List<FinancialEventGroup> list =
               listFinancialEventGroupsResponse.getListFinancialEventGroupsResult().getFinancialEventGroupList();

       list.forEach(x -> {
           System.out.println(x.getFinancialEventGroupId());
           System.out.println(x.getFinancialEventGroupStart());
           System.out.println(x.getFinancialEventGroupEnd());
       });
       */


       //this.config.getClient().getFi
        //request.get

    }



    public AmazonFinanceEvent getFinanceEvent() {

        List<FinancialEvents> fel = new ArrayList<>();

        this.init(Marketplace.AMAZON_COM);
        grpRequest = new ListFinancialEventGroupsRequest()
                .withSellerId(config.getSellerId());

        feRequest = new ListFinancialEventsRequest().withSellerId(config.getSellerId());

        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        ZonedDateTime startDate = ZonedDateTime.of(2020, 12, 01, 0, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime endDate = ZonedDateTime.of(2020, 12, 16, 0, 0, 0, 0, ZoneOffset.UTC);
        XMLGregorianCalendar sd = df.newXMLGregorianCalendar(GregorianCalendar.from(startDate));


        System.out.println(endDate);
        XMLGregorianCalendar ed = df
                .newXMLGregorianCalendar(GregorianCalendar.from(endDate));

        grpRequest.setFinancialEventGroupStartedAfter(sd);
        grpRequest.setFinancialEventGroupStartedBefore(ed);

        feRequest.setPostedBefore(ed);
        feRequest.setPostedAfter(sd);
        //feRequest.setFinancialEventGroupId("TYbrayPMXpvnoYN-9_9vdMxJqWsRm_dDZPCDN9_-iQo");
        ListFinancialEventsResponse response = config.getClient().listFinancialEvents(feRequest);

        System.out.println(response.getListFinancialEventsResult().toJSON());
        //com.kindminds.drs.api.v1.model.finance.test
        //int size =fe.getShipmentEventList().size();
        ListFinancialEventsResult fer=response.getListFinancialEventsResult();
        String nextToken=response.getListFinancialEventsResult().getNextToken();

        FinancialEvents firstFe = fer.getFinancialEvents();
        fel.add(firstFe);

        while(nextToken!=null){
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
            }
            ListFinancialEventsByNextTokenRequest nextTokenRequest =
                    new ListFinancialEventsByNextTokenRequest(nextToken).withSellerId(config.getSellerId());

            ListFinancialEventsByNextTokenResponse nextResponse =config.getClient().listFinancialEventsByNextToken(nextTokenRequest);
            ResponseHeaderMetadata rhmd=nextResponse.getResponseHeaderMetadata();
            ListFinancialEventsByNextTokenResult eventResult =nextResponse.getListFinancialEventsByNextTokenResult();
            FinancialEvents fent =eventResult.getFinancialEvents();
            nextToken =eventResult.getNextToken();
            fel.add(fent);

        }

        AmazonFinanceEvent youFe = new AmazonFinanceEvent();
        List<AmazonShipmentEvent> yourShipmentList = new ArrayList<AmazonShipmentEvent>();
        List<AmazonShipmentEvent> yourAmazonRefundEvent = new ArrayList<AmazonShipmentEvent>();
        List<AmazonShipmentEvent> yourAmazonGuaranteeClaimEvent = new ArrayList<AmazonShipmentEvent>();
        List<AmazonShipmentEvent> yourAmazonChargebackEvent = new ArrayList<AmazonShipmentEvent>();
        List<AmazonPayWithAmazonEvent> yourAmazonPayWithAmazonEvent = new ArrayList<>();
        List<AmazonSolutionProviderCreditEvent> yourAmazonSolutionProviderCreditEvent = new ArrayList<>();
        List<AmazonRetrochargeEvent> yourAmazonRetrochargeEvent = new ArrayList<>();
        List<AmazonRentalTransactionEvent> yourAmazonRentalTransactionEvent = new ArrayList<>();
        List<AmazonPerformanceBondRefundEvent> yourAmazonPerformanceBondRefundEvent =new ArrayList<>();
        List<AmazonProductAdsPaymentEvent> yourAmazonProductAdsPaymentEvent = new ArrayList<>();
        List<AmazonServiceFeeEvent> yourAmazonServiceFeeEvent = new ArrayList<>();
        List<AmazonSellerDealPaymentEvent> yourAmazonSellerDealPaymentEvent = new ArrayList<>();
        List<AmazonDebtRecoveryEvent> yourAmazonDebtRecoveryEvent = new ArrayList<>();
        List<AmazonLoanServicingEvent> yourAmazonLoanServicingEvent= new ArrayList<>();
        List<AmazonAdjustmentEvent> yourAmazonAdjustmentEvent = new ArrayList<>();
        List<AmazonSAFETReimbursementEvent> yourAmazonSAFETReimbursementEvent = new ArrayList<>();
        List<AmazonSellerReviewEnrollmentPaymentEvent> yourAmazonSellerReviewEnrollmentPaymentEvent = new ArrayList<>();
        List<AmazonFBALiquidationEvent> yourAmazonFBALiquidationEvent = new ArrayList<>();
        List<AmazonCouponPaymentEvent> yourAmazonCouponPaymentEvent = new ArrayList<>();
        List<AmazonImagingServicesFeeEvent> yourAmazonImagingServicesFeeEvent =new ArrayList<>();
        List<AmazonNetworkComminglingTransactionEvent> yourAmazonNetworkComminglingTransactionEvent = new ArrayList<>();
        List<AmazonAffordabilityExpenseEvent> yourAmazonAffordabilityExpenseEvent = new ArrayList<>();
        List<AmazonAffordabilityExpenseEvent> yourAmazonAffordabilityExpenseReversalEvent = new ArrayList<>();
        List<AmazonRemovalShipmentEvent> yourAmazonRemovalShipmentEvent = new ArrayList<>();
        List<AmazonTrialShipmentEvent> yourAmazonTrialShipmentEvent = new ArrayList<>();
        List<AmazonTDSReimbursementEvent> yourAmazonTDSReimbursementEvent = new ArrayList<>();
        List<AmazonTaxWithholdingEvent> yourAmazonTaxWithholdingEvent = new ArrayList<>();

        fel.forEach(fe -> {

        fe.getShipmentEventList().forEach(x -> {
            AmazonShipmentEvent ase = new AmazonShipmentEvent();

            ase.setAmazonOrderId(x.getAmazonOrderId());
            ase.setSellerOrderId(x.getSellerOrderId());
            ase.setMarketplaceName(x.getMarketplaceName());
            ase.setPostedDate(x.getPostedDate().toGregorianCalendar().getTime());

            yourShipmentList.add(ase);
        });


        fe.getRefundEventList().forEach(x -> {
            AmazonShipmentEvent are = new AmazonShipmentEvent();

            are.setAmazonOrderId(x.getAmazonOrderId());
            are.setSellerOrderId(x.getSellerOrderId());
            are.setMarketplaceName(x.getMarketplaceName());
            are.setPostedDate(x.getPostedDate().toGregorianCalendar().getTime());

            yourAmazonRefundEvent.add(are);
        });


        fe.getGuaranteeClaimEventList().forEach(x -> {
            AmazonShipmentEvent agce = new AmazonShipmentEvent();

            agce.setAmazonOrderId(x.getAmazonOrderId());
            agce.setSellerOrderId(x.getSellerOrderId());
            agce.setMarketplaceName(x.getMarketplaceName());
            agce.setPostedDate(x.getPostedDate().toGregorianCalendar().getTime());

            yourAmazonGuaranteeClaimEvent.add(agce);
        });


        fe.getChargebackEventList().forEach(x -> {
            AmazonShipmentEvent ace = new AmazonShipmentEvent();

            ace.setAmazonOrderId(x.getAmazonOrderId());
            ace.setSellerOrderId(x.getSellerOrderId());
            ace.setMarketplaceName(x.getMarketplaceName());
            ace.setPostedDate(x.getPostedDate().toGregorianCalendar().getTime());

            yourAmazonChargebackEvent.add(ace);
        });

        fe.getPayWithAmazonEventList().forEach(x-> {
            AmazonPayWithAmazonEvent apwae = new AmazonPayWithAmazonEvent();
            apwae.setSellerOrderId(x.getSellerOrderId());
            apwae.setTransactionPostedDate(formatDate(this.replaceZwithTimeZone(x.getTransactionPostedDate().toString())));
            apwae.setBusinessObjectType(x.getBusinessObjectType());
            apwae.setSalesChannel(x.getSalesChannel());
            apwae.setPaymentAmountType(x.getPaymentAmountType());
            apwae.setAmountDescription(x.getAmountDescription());
            apwae.setFulfillmentChannel(x.getFulfillmentChannel());
            apwae.setStoreName(x.getStoreName());

            yourAmazonPayWithAmazonEvent.add(apwae);
        });

        fe.getServiceProviderCreditEventList().forEach(x ->{
            AmazonSolutionProviderCreditEvent aspce = new AmazonSolutionProviderCreditEvent();
            aspce.setProviderTransactionType(x.getProviderTransactionType());
            aspce.setSellerOrderId(x.getSellerOrderId());
            aspce.setMarketplaceId(x.getMarketplaceId());
            aspce.setMarketplaceCountry(x.getMarketplaceCountryCode());
            aspce.setSellerId(x.getSellerId());
            aspce.setSellerStoreName(x.getSellerStoreName());
            aspce.setProviderId(x.getProviderId());
            aspce.setProviderStoreName(x.getProviderStoreName());
            aspce.setTransactionCurrency(x.getTransactionAmount().getCurrencyCode());
            aspce.setTransactionAmount(x.getTransactionAmount().getCurrencyAmount());
            aspce.setTransactionCreationDate(formatDate(this.replaceZwithTimeZone(x.getTransactionCreationDate().toString())));

            yourAmazonSolutionProviderCreditEvent.add(aspce);
        });

        fe.getRetrochargeEventList().forEach(x ->{
            AmazonRetrochargeEvent are =new AmazonRetrochargeEvent();
            are.setRetrochargeEventType(x.getRetrochargeEventType());
            are.setAmazonOrderId(x.getAmazonOrderId());
            are.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            are.setBaseTaxCurrency(x.getBaseTax().getCurrencyCode());
            are.setBaseTaxAmount(x.getBaseTax().getCurrencyAmount());
            are.setShippingTaxCurrency(x.getShippingTax().getCurrencyCode());
            are.setShippingTaxAmount(x.getShippingTax().getCurrencyAmount());
            are.setMarketplaceName(x.getMarketplaceName());

            yourAmazonRetrochargeEvent.add(are);
        });

        fe.getRentalTransactionEventList().forEach(x ->{
            AmazonRentalTransactionEvent arte = new AmazonRentalTransactionEvent();
            arte.setAmazonOrderId(x.getAmazonOrderId());
            arte.setRentalEventType(x.getRentalEventType());
            arte.setExtensionLength(x.getExtensionLength());
            arte.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            arte.setMarketplaceName(x.getMarketplaceName());
            arte.setRentalInitialCurrency(x.getRentalInitialValue().getCurrencyCode());
            arte.setRentalInitialValue(x.getRentalInitialValue().getCurrencyAmount());
            arte.setRentalReimbursementCurrency(x.getRentalReimbursement().getCurrencyCode());
            arte.setRentalReimbursementAmount(x.getRentalReimbursement().getCurrencyAmount());

            yourAmazonRentalTransactionEvent.add(arte);
        });

        fe.getPerformanceBondRefundEventList().forEach(x ->{
            AmazonPerformanceBondRefundEvent apbre = new AmazonPerformanceBondRefundEvent();
            apbre.setMarketplaceCountry(x.getMarketplaceCountryCode());
            apbre.setCurrency(x.getAmount().getCurrencyCode());
            apbre.setAmount(x.getAmount().getCurrencyAmount());

            StringBuffer sb= new StringBuffer();
            x.getProductGroupList().forEach(y ->{
                sb.append(y+" , ");
            });
            apbre.setProductGroupList(sb.toString());

            yourAmazonPerformanceBondRefundEvent.add(apbre);
        });

        fe.getProductAdsPaymentEventList().forEach(x ->{
            AmazonProductAdsPaymentEvent apape =new AmazonProductAdsPaymentEvent();
            apape.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            apape.setTransactionType(x.getTransactionType());
            apape.setInvoiceId(x.getInvoiceId());
            apape.setCurrency(x.getTransactionValue().getCurrencyCode());
            apape.setBaseValue(x.getBaseValue().getCurrencyAmount());
            apape.setTaxValue(x.getTaxValue().getCurrencyAmount());
            apape.setTransactionValue(x.getTransactionValue().getCurrencyAmount());

            yourAmazonProductAdsPaymentEvent.add(apape);
        });

        fe.getServiceFeeEventList().forEach(x ->{
            AmazonServiceFeeEvent asfe = new AmazonServiceFeeEvent();
            asfe.setAmazonOrderId(x.getAmazonOrderId());
            asfe.setFeeReason(x.getFeeReason());
            asfe.setSellerSKU(x.getSellerSKU());
            asfe.setFnSKU(x.getFnSKU());
            asfe.setFeeDescription(x.getFeeDescription());
            asfe.setAsin(x.getASIN());

            yourAmazonServiceFeeEvent.add(asfe);
        });

        fe.getSellerDealPaymentEventList().forEach(x ->{
            AmazonSellerDealPaymentEvent asdpe = new AmazonSellerDealPaymentEvent();
            asdpe.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            asdpe.setDealId(x.getDealId());
            asdpe.setDealDescription(x.getDealDescription());
            asdpe.setEventType(x.getEventType());
            asdpe.setFeeType(x.getFeeType());
            asdpe.setCurrency(x.getTotalAmount().getCurrencyCode());
            asdpe.setFeeAmount(x.getFeeAmount().getCurrencyAmount());
            asdpe.setTaxAmount(x.getTaxAmount().getCurrencyAmount());
            asdpe.setTotalAmount(x.getTotalAmount().getCurrencyAmount());

            yourAmazonSellerDealPaymentEvent.add(asdpe);
        });

        fe.getDebtRecoveryEventList().forEach(x ->{
            AmazonDebtRecoveryEvent adre = new AmazonDebtRecoveryEvent();
            adre.setDebtRecoveryType(x.getDebtRecoveryType());
            adre.setCurrency(x.getOverPaymentCredit().getCurrencyCode());
            adre.setRecoveryAmount(x.getRecoveryAmount().getCurrencyAmount());
            adre.setOverPaymentCredit(x.getOverPaymentCredit().getCurrencyAmount());

            yourAmazonDebtRecoveryEvent.add(adre);
        });

        fe.getLoanServicingEventList().forEach(x ->{
            AmazonLoanServicingEvent alse = new AmazonLoanServicingEvent();
            alse.setLoanCurrency(x.getLoanAmount().getCurrencyCode());
            alse.setLoanAmount(x.getLoanAmount().getCurrencyAmount());
            alse.setSourceBusinessEventType(x.getSourceBusinessEventType());

            yourAmazonLoanServicingEvent.add(alse);
        });

        fe.getAdjustmentEventList().forEach(x ->{
            AmazonAdjustmentEvent aae =new AmazonAdjustmentEvent();
            aae.setAdjustmentType(x.getAdjustmentType());
            aae.setAdjustmentCurrency(x.getAdjustmentAmount().getCurrencyCode());
            aae.setAdjustmentAmount(x.getAdjustmentAmount().getCurrencyAmount());
            aae.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));

            yourAmazonAdjustmentEvent.add(aae);
        });

        fe.getSAFETReimbursementEventList().forEach(x ->{
            AmazonSAFETReimbursementEvent asre = new AmazonSAFETReimbursementEvent();
            asre.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            asre.setSafetClaimId(x.getSAFETClaimId());
            asre.setReimbursedCurrency(x.getReimbursedAmount().getCurrencyCode());
            asre.setReimbursedAmount(x.getReimbursedAmount().getCurrencyAmount());
            asre.setReasonCode(x.getReasonCode());

            yourAmazonSAFETReimbursementEvent.add(asre);
        });

        fe.getSellerReviewEnrollmentPaymentEventList().forEach(x ->{
            AmazonSellerReviewEnrollmentPaymentEvent asrepe = new AmazonSellerReviewEnrollmentPaymentEvent();
            asrepe.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            asrepe.setEnrollmentId(x.getEnrollmentId());
            asrepe.setParentAsin(x.getParentASIN());
            asrepe.setCurrency(x.getTotalAmount().getCurrencyCode());
            asrepe.setTotalAmount(x.getTotalAmount().getCurrencyAmount());

            yourAmazonSellerReviewEnrollmentPaymentEvent.add(asrepe);
        });

        fe.getFBALiquidationEventList().forEach(x ->{
            AmazonFBALiquidationEvent afle =new AmazonFBALiquidationEvent();
            afle.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            afle.setOriginalRemovalOrderId(x.getOriginalRemovalOrderId());
            afle.setLiquidationProceedsCurrency(x.getLiquidationProceedsAmount().getCurrencyCode());
            afle.setLiquidationProceedsAmount(x.getLiquidationProceedsAmount().getCurrencyAmount());
            afle.setLiquidationFeeCurrency(x.getLiquidationFeeAmount().getCurrencyCode());
            afle.setLiquidationFeeAmount(x.getLiquidationFeeAmount().getCurrencyAmount());

            yourAmazonFBALiquidationEvent.add(afle);
        });

        fe.getCouponPaymentEventList().forEach(x ->{
            AmazonCouponPaymentEvent acpe = new AmazonCouponPaymentEvent();
            acpe.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            acpe.setCouponId(x.getCouponId());
            acpe.setSellerCouponDescription(x.getSellerCouponDescription());
            acpe.setClipOrRedemptionCount(x.getClipOrRedemptionCount());
            acpe.setPaymentEventId(x.getPaymentEventId());
            acpe.setCurrency(x.getTotalAmount().getCurrencyCode());
            acpe.setTotalAmount(x.getTotalAmount().getCurrencyAmount());

            yourAmazonCouponPaymentEvent.add(acpe);
        });

        fe.getImagingServicesFeeEventList().forEach(x ->{
            AmazonImagingServicesFeeEvent aisfe = new AmazonImagingServicesFeeEvent();
            aisfe.setImagingRequestBillingItemID(x.getImagingRequestBillingItemID());
            aisfe.setAsin(x.getASIN());
            aisfe.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));

            yourAmazonImagingServicesFeeEvent.add(aisfe);
        });

        fe.getNetworkComminglingTransactionEventList().forEach(x ->{
            AmazonNetworkComminglingTransactionEvent ancte = new AmazonNetworkComminglingTransactionEvent();
            ancte.setTransactionType(x.getTransactionType());
            ancte.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            ancte.setNetCoTransactionID(x.getNetCoTransactionID());
            ancte.setSwapReason(x.getSwapReason());
            ancte.setAsin(x.getASIN());
            ancte.setMarketplaceId(x.getMarketplaceId());
            ancte.setCurrency(x.getTaxAmount().getCurrencyCode());
            ancte.setTaxExclusiveAmount(x.getTaxExclusiveAmount().getCurrencyAmount());
            ancte.setTaxAmount(x.getTaxAmount().getCurrencyAmount());

            yourAmazonNetworkComminglingTransactionEvent.add(ancte);
        });

        fe.getAffordabilityExpenseEventList().forEach(x ->{
            AmazonAffordabilityExpenseEvent aaee =new AmazonAffordabilityExpenseEvent();
            aaee.setAmazonOrderId(x.getAmazonOrderId());
            aaee.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            aaee.setMarketplaceId(x.getMarketplaceId());
            aaee.setTransactionType(x.getTransactionType());
            aaee.setExpenseCurrency(x.getTotalExpense().getCurrencyCode());
            aaee.setBaseExpense(x.getBaseExpense().getCurrencyAmount());
            aaee.setTaxTypeIGST(x.getTaxTypeIGST().getCurrencyAmount());
            aaee.setTaxTypeCGST(x.getTaxTypeCGST().getCurrencyAmount());
            aaee.setTaxTypeSGST(x.getTaxTypeSGST().getCurrencyAmount());
            aaee.setTotalExpense(x.getTotalExpense().getCurrencyAmount());

            yourAmazonAffordabilityExpenseEvent.add(aaee);
        });

        fe.getAffordabilityExpenseReversalEventList().forEach(x ->{
            AmazonAffordabilityExpenseEvent aaere =new AmazonAffordabilityExpenseEvent();
            aaere.setAmazonOrderId(x.getAmazonOrderId());
            aaere.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            aaere.setMarketplaceId(x.getMarketplaceId());
            aaere.setTransactionType(x.getTransactionType());
            aaere.setExpenseCurrency(x.getTotalExpense().getCurrencyCode());
            aaere.setBaseExpense(x.getBaseExpense().getCurrencyAmount());
            aaere.setTaxTypeIGST(x.getTaxTypeIGST().getCurrencyAmount());
            aaere.setTaxTypeCGST(x.getTaxTypeCGST().getCurrencyAmount());
            aaere.setTaxTypeSGST(x.getTaxTypeSGST().getCurrencyAmount());
            aaere.setTotalExpense(x.getTotalExpense().getCurrencyAmount());

            yourAmazonAffordabilityExpenseReversalEvent.add(aaere);
        });

        fe.getRemovalShipmentEventList().forEach(x ->{
            AmazonRemovalShipmentEvent arse = new AmazonRemovalShipmentEvent();
            arse.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            arse.setOrderId(x.getOrderId());
            arse.setTransactionType(x.getTransactionType());

            yourAmazonRemovalShipmentEvent.add(arse);
        });

        fe.getTrialShipmentEventList().forEach(x ->{
            AmazonTrialShipmentEvent atse = new AmazonTrialShipmentEvent();
            atse.setAmazonOrderId(x.getAmazonOrderId());
            atse.setFinancialEventGroupId(x.getFinancialEventGroupId());
            atse.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            atse.setSku(x.getSKU());

            yourAmazonTrialShipmentEvent.add(atse);
        });

        fe.getTDSReimbursementEventList().forEach(x ->{
            AmazonTDSReimbursementEvent atre = new AmazonTDSReimbursementEvent();
            atre.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            atre.setTdsOrderId(x.getTdsOrderId());
            atre.setReimbursedCurrency(x.getReimbursedAmount().getCurrencyCode());
            atre.setReimbursedAmount(x.getReimbursedAmount().getCurrencyAmount());

            yourAmazonTDSReimbursementEvent.add(atre);
        });

        fe.getTaxWithholdingEventList().forEach(x ->{
            AmazonTaxWithholdingEvent atwe =new AmazonTaxWithholdingEvent();
            atwe.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            atwe.setPeriodStart(OffsetDateTime.ofInstant(Instant.ofEpochMilli(x.getTaxWithholdingPeriod().getStartDateMillis()), ZoneId.systemDefault()));
            atwe.setPeriodEnd(OffsetDateTime.ofInstant(Instant.ofEpochMilli(x.getTaxWithholdingPeriod().getEndDateMillis()), ZoneId.systemDefault()));
            atwe.setCurrency(x.getBaseAmount().getCurrencyCode());
            atwe.setBaseAmount(x.getBaseAmount().getCurrencyAmount());
            atwe.setWithheldAmount(x.getWithheldAmount().getCurrencyAmount());

            yourAmazonTaxWithholdingEvent.add(atwe);
        });

     });

        youFe.setShipmentEventList(yourShipmentList);
        youFe.setRefundEventList(yourAmazonRefundEvent);
        youFe.setGuaranteeClaimEventList(yourAmazonGuaranteeClaimEvent);
        youFe.setChargebackEventList(yourAmazonChargebackEvent);
        youFe.setPayWithAmazonEventList(yourAmazonPayWithAmazonEvent);
        youFe.setServiceProviderCreditEventList(yourAmazonSolutionProviderCreditEvent);
        youFe.setRetrochargeEventList(yourAmazonRetrochargeEvent);
        youFe.setRentalTransactionEventList(yourAmazonRentalTransactionEvent);
        youFe.setPerformanceBondRefundEventList(yourAmazonPerformanceBondRefundEvent);
        youFe.setProductAdsPaymentEventList(yourAmazonProductAdsPaymentEvent);
        youFe.setServiceFeeEventList(yourAmazonServiceFeeEvent);
        youFe.setSellerDealPaymentEventList(yourAmazonSellerDealPaymentEvent);
        youFe.setDebtRecoveryEventList(yourAmazonDebtRecoveryEvent);
        youFe.setLoanServicingEventList(yourAmazonLoanServicingEvent);
        youFe.setAdjustmentEventList(yourAmazonAdjustmentEvent);
        youFe.setSafetReimbursementEventList(yourAmazonSAFETReimbursementEvent);
        youFe.setSellerReviewEnrollmentPaymentEventList(yourAmazonSellerReviewEnrollmentPaymentEvent);
        youFe.setFbaLiquidationEventList(yourAmazonFBALiquidationEvent);
        youFe.setCouponPaymentEventList(yourAmazonCouponPaymentEvent);
        youFe.setImagingServicesFeeEventList(yourAmazonImagingServicesFeeEvent);
        youFe.setNetworkComminglingTransactionEventList(yourAmazonNetworkComminglingTransactionEvent);
        youFe.setAffordabilityExpenseEventList(yourAmazonAffordabilityExpenseEvent);
        youFe.setAffordabilityExpenseReversalEventList(yourAmazonAffordabilityExpenseReversalEvent);
        youFe.setRemovalShipmentEventList(yourAmazonRemovalShipmentEvent);
        youFe.setTrialShipmentEventList(yourAmazonTrialShipmentEvent);
        youFe.setTdsReimbursementEventList(yourAmazonTDSReimbursementEvent);
        youFe.setTaxWithholdingEventList(yourAmazonTaxWithholdingEvent);

        return youFe;
    }




    public AmazonFinanceEvent testget() {



        this.init(Marketplace.AMAZON_COM);
        grpRequest = new ListFinancialEventGroupsRequest()
                .withSellerId(config.getSellerId());

        feRequest = new ListFinancialEventsRequest().withSellerId(config.getSellerId());

        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        ZonedDateTime startDate = ZonedDateTime.of(2020, 12, 01, 0, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime endDate = ZonedDateTime.of(2020, 12, 16, 0, 0, 0, 0, ZoneOffset.UTC);
        XMLGregorianCalendar sd = df.newXMLGregorianCalendar(GregorianCalendar.from(startDate));


        System.out.println(endDate);
        XMLGregorianCalendar ed = df
                .newXMLGregorianCalendar(GregorianCalendar.from(endDate));

        grpRequest.setFinancialEventGroupStartedAfter(sd);
        grpRequest.setFinancialEventGroupStartedBefore(ed);

        feRequest.setPostedBefore(ed);
        feRequest.setPostedAfter(sd);
        //feRequest.setFinancialEventGroupId("TYbrayPMXpvnoYN-9_9vdMxJqWsRm_dDZPCDN9_-iQo");
        ListFinancialEventsResponse response = config.getClient().listFinancialEvents(feRequest);


        System.out.println(response.getListFinancialEventsResult().toJSON());
        //com.kindminds.drs.api.v1.model.finance.test
        //int size =fe.getShipmentEventList().size();
        ListFinancialEventsResult fer=response.getListFinancialEventsResult();
        String nextToken=response.getListFinancialEventsResult().getNextToken();

        FinancialEvents fe = fer.getFinancialEvents();

        AmazonFinanceEvent youFe = new AmazonFinanceEvent();
        List<AmazonShipmentEvent> yourShipmentEvent = new ArrayList<>();
        List<AmazonShipmentEvent> yourAmazonRefundEvent = new ArrayList<>();
        List<AmazonShipmentEvent> yourAmazonGuaranteeClaimEvent = new ArrayList<>();
        List<AmazonShipmentEvent> yourAmazonChargebackEvent = new ArrayList<>();
        List<AmazonPayWithAmazonEvent> yourAmazonPayWithAmazonEvent = new ArrayList<>();
        List<AmazonSolutionProviderCreditEvent> yourAmazonSolutionProviderCreditEvent = new ArrayList<>();
        List<AmazonRetrochargeEvent> yourAmazonRetrochargeEvent = new ArrayList<>();
        List<AmazonRentalTransactionEvent> yourAmazonRentalTransactionEvent = new ArrayList<>();
        List<AmazonPerformanceBondRefundEvent> yourAmazonPerformanceBondRefundEvent =new ArrayList<>();
        List<AmazonProductAdsPaymentEvent> yourAmazonProductAdsPaymentEvent = new ArrayList<>();
        List<AmazonServiceFeeEvent> yourAmazonServiceFeeEvent = new ArrayList<>();
        List<AmazonSellerDealPaymentEvent> yourAmazonSellerDealPaymentEvent = new ArrayList<>();
        List<AmazonDebtRecoveryEvent> yourAmazonDebtRecoveryEvent = new ArrayList<>();
        List<AmazonLoanServicingEvent> yourAmazonLoanServicingEvent= new ArrayList<>();
        List<AmazonAdjustmentEvent> yourAmazonAdjustmentEvent = new ArrayList<>();
        List<AmazonSAFETReimbursementEvent> yourAmazonSAFETReimbursementEvent = new ArrayList<>();
        List<AmazonSellerReviewEnrollmentPaymentEvent> yourAmazonSellerReviewEnrollmentPaymentEvent = new ArrayList<>();
        List<AmazonFBALiquidationEvent> yourAmazonFBALiquidationEvent = new ArrayList<>();
        List<AmazonCouponPaymentEvent> yourAmazonCouponPaymentEvent = new ArrayList<>();
        List<AmazonImagingServicesFeeEvent> yourAmazonImagingServicesFeeEvent =new ArrayList<>();
        List<AmazonNetworkComminglingTransactionEvent> yourAmazonNetworkComminglingTransactionEvent = new ArrayList<>();
        List<AmazonAffordabilityExpenseEvent> yourAmazonAffordabilityExpenseEvent = new ArrayList<>();
        List<AmazonAffordabilityExpenseEvent> yourAmazonAffordabilityExpenseReversalEvent = new ArrayList<>();
        List<AmazonRemovalShipmentEvent> yourAmazonRemovalShipmentEvent = new ArrayList<>();
        List<AmazonTrialShipmentEvent> yourAmazonTrialShipmentEvent = new ArrayList<>();
        List<AmazonTDSReimbursementEvent> yourAmazonTDSReimbursementEvent = new ArrayList<>();
        List<AmazonTaxWithholdingEvent> yourAmazonTaxWithholdingEvent = new ArrayList<>();




            fe.getShipmentEventList().forEach(x -> {
                AmazonShipmentEvent ase = new AmazonShipmentEvent();

                ase.setAmazonOrderId(x.getAmazonOrderId());
                ase.setSellerOrderId(x.getSellerOrderId());
                ase.setMarketplaceName(x.getMarketplaceName());
                ase.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));

                yourShipmentEvent.add(ase);
            });


            fe.getRefundEventList().forEach(x -> {
                AmazonShipmentEvent are = new AmazonShipmentEvent();

                are.setAmazonOrderId(x.getAmazonOrderId());
                are.setSellerOrderId(x.getSellerOrderId());
                are.setMarketplaceName(x.getMarketplaceName());
                are.setPostedDate(x.getPostedDate().toGregorianCalendar().getTime());

                yourAmazonRefundEvent.add(are);
            });


            fe.getGuaranteeClaimEventList().forEach(x -> {
                AmazonShipmentEvent agce = new AmazonShipmentEvent();

                agce.setAmazonOrderId(x.getAmazonOrderId());
                agce.setSellerOrderId(x.getSellerOrderId());
                agce.setMarketplaceName(x.getMarketplaceName());
                agce.setPostedDate(x.getPostedDate().toGregorianCalendar().getTime());

                yourAmazonGuaranteeClaimEvent.add(agce);
            });


            fe.getChargebackEventList().forEach(x -> {
                AmazonShipmentEvent ace = new AmazonShipmentEvent();

                ace.setAmazonOrderId(x.getAmazonOrderId());
                ace.setSellerOrderId(x.getSellerOrderId());
                ace.setMarketplaceName(x.getMarketplaceName());
                ace.setPostedDate(x.getPostedDate().toGregorianCalendar().getTime());

                yourAmazonChargebackEvent.add(ace);
            });

            fe.getPayWithAmazonEventList().forEach(x -> {
                AmazonPayWithAmazonEvent apwae = new AmazonPayWithAmazonEvent();
                apwae.setSellerOrderId(x.getSellerOrderId());
                apwae.setTransactionPostedDate(formatDate(this.replaceZwithTimeZone(x.getTransactionPostedDate().toString())));
                apwae.setBusinessObjectType(x.getBusinessObjectType());
                apwae.setSalesChannel(x.getSalesChannel());
                apwae.setPaymentAmountType(x.getPaymentAmountType());
                apwae.setAmountDescription(x.getAmountDescription());
                apwae.setFulfillmentChannel(x.getFulfillmentChannel());
                apwae.setStoreName(x.getStoreName());

                yourAmazonPayWithAmazonEvent.add(apwae);
            });

            fe.getServiceProviderCreditEventList().forEach(x ->{
                AmazonSolutionProviderCreditEvent aspce = new AmazonSolutionProviderCreditEvent();
                aspce.setProviderTransactionType(x.getProviderTransactionType());
                aspce.setSellerOrderId(x.getSellerOrderId());
                aspce.setMarketplaceId(x.getMarketplaceId());
                aspce.setMarketplaceCountry(x.getMarketplaceCountryCode());
                aspce.setSellerId(x.getSellerId());
                aspce.setSellerStoreName(x.getSellerStoreName());
                aspce.setProviderId(x.getProviderId());
                aspce.setProviderStoreName(x.getProviderStoreName());
                aspce.setTransactionCurrency(x.getTransactionAmount().getCurrencyCode());
                aspce.setTransactionAmount(x.getTransactionAmount().getCurrencyAmount());
                aspce.setTransactionCreationDate(formatDate(this.replaceZwithTimeZone(x.getTransactionCreationDate().toString())));

                yourAmazonSolutionProviderCreditEvent.add(aspce);

            });

            fe.getRetrochargeEventList().forEach(x ->{
                AmazonRetrochargeEvent are =new AmazonRetrochargeEvent();
                are.setRetrochargeEventType(x.getRetrochargeEventType());
                are.setAmazonOrderId(x.getAmazonOrderId());
                are.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
                are.setBaseTaxCurrency(x.getBaseTax().getCurrencyCode());
                are.setBaseTaxAmount(x.getBaseTax().getCurrencyAmount());
                are.setShippingTaxCurrency(x.getShippingTax().getCurrencyCode());
                are.setShippingTaxAmount(x.getShippingTax().getCurrencyAmount());
                are.setMarketplaceName(x.getMarketplaceName());

                yourAmazonRetrochargeEvent.add(are);
            });

            fe.getRentalTransactionEventList().forEach(x ->{
                AmazonRentalTransactionEvent arte = new AmazonRentalTransactionEvent();
                arte.setAmazonOrderId(x.getAmazonOrderId());
                arte.setRentalEventType(x.getRentalEventType());
                arte.setExtensionLength(x.getExtensionLength());
                arte.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
                arte.setMarketplaceName(x.getMarketplaceName());
                arte.setRentalInitialCurrency(x.getRentalInitialValue().getCurrencyCode());
                arte.setRentalInitialValue(x.getRentalInitialValue().getCurrencyAmount());
                arte.setRentalReimbursementCurrency(x.getRentalReimbursement().getCurrencyCode());
                arte.setRentalReimbursementAmount(x.getRentalReimbursement().getCurrencyAmount());

                yourAmazonRentalTransactionEvent.add(arte);
            });

            fe.getPerformanceBondRefundEventList().forEach(x ->{
                AmazonPerformanceBondRefundEvent apbre = new AmazonPerformanceBondRefundEvent();
                apbre.setMarketplaceCountry(x.getMarketplaceCountryCode());
                apbre.setCurrency(x.getAmount().getCurrencyCode());
                apbre.setAmount(x.getAmount().getCurrencyAmount());

                StringBuffer sb= new StringBuffer();
                x.getProductGroupList().forEach(y ->{
                    sb.append(y+" , ");
                });
                apbre.setProductGroupList(sb.toString());

                yourAmazonPerformanceBondRefundEvent.add(apbre);
            });

            fe.getProductAdsPaymentEventList().forEach(x ->{
                AmazonProductAdsPaymentEvent apape =new AmazonProductAdsPaymentEvent();
                apape.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
                apape.setTransactionType(x.getTransactionType());
                apape.setInvoiceId(x.getInvoiceId());
                apape.setCurrency(x.getTransactionValue().getCurrencyCode());
                apape.setBaseValue(x.getBaseValue().getCurrencyAmount());
                apape.setTaxValue(x.getTaxValue().getCurrencyAmount());
                apape.setTransactionValue(x.getTransactionValue().getCurrencyAmount());

                yourAmazonProductAdsPaymentEvent.add(apape);
            });

            fe.getServiceFeeEventList().forEach(x ->{
                AmazonServiceFeeEvent asfe = new AmazonServiceFeeEvent();
                asfe.setAmazonOrderId(x.getAmazonOrderId());
                asfe.setFeeReason(x.getFeeReason());
                asfe.setSellerSKU(x.getSellerSKU());
                asfe.setFnSKU(x.getFnSKU());
                asfe.setFeeDescription(x.getFeeDescription());
                asfe.setAsin(x.getASIN());

                yourAmazonServiceFeeEvent.add(asfe);
            });

            fe.getSellerDealPaymentEventList().forEach(x ->{
                AmazonSellerDealPaymentEvent asdpe = new AmazonSellerDealPaymentEvent();
                asdpe.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
                asdpe.setDealId(x.getDealId());
                asdpe.setDealDescription(x.getDealDescription());
                asdpe.setEventType(x.getEventType());
                asdpe.setFeeType(x.getFeeType());
                asdpe.setCurrency(x.getTotalAmount().getCurrencyCode());
                asdpe.setFeeAmount(x.getFeeAmount().getCurrencyAmount());
                asdpe.setTaxAmount(x.getTaxAmount().getCurrencyAmount());
                asdpe.setTotalAmount(x.getTotalAmount().getCurrencyAmount());

                yourAmazonSellerDealPaymentEvent.add(asdpe);
            });

            fe.getDebtRecoveryEventList().forEach(x ->{
                AmazonDebtRecoveryEvent adre = new AmazonDebtRecoveryEvent();
                adre.setDebtRecoveryType(x.getDebtRecoveryType());
                adre.setCurrency(x.getOverPaymentCredit().getCurrencyCode());
                adre.setRecoveryAmount(x.getRecoveryAmount().getCurrencyAmount());
                adre.setOverPaymentCredit(x.getOverPaymentCredit().getCurrencyAmount());

                yourAmazonDebtRecoveryEvent.add(adre);
            });

            fe.getLoanServicingEventList().forEach(x ->{
                AmazonLoanServicingEvent alse = new AmazonLoanServicingEvent();
                alse.setLoanCurrency(x.getLoanAmount().getCurrencyCode());
                alse.setLoanAmount(x.getLoanAmount().getCurrencyAmount());
                alse.setSourceBusinessEventType(x.getSourceBusinessEventType());

                yourAmazonLoanServicingEvent.add(alse);
            });

            fe.getAdjustmentEventList().forEach(x ->{
                AmazonAdjustmentEvent aae =new AmazonAdjustmentEvent();
                aae.setAdjustmentType(x.getAdjustmentType());
                aae.setAdjustmentCurrency(x.getAdjustmentAmount().getCurrencyCode());
                aae.setAdjustmentAmount(x.getAdjustmentAmount().getCurrencyAmount());
                aae.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));

                yourAmazonAdjustmentEvent.add(aae);

            });

            fe.getSAFETReimbursementEventList().forEach(x ->{
                AmazonSAFETReimbursementEvent asre = new AmazonSAFETReimbursementEvent();
                asre.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
                asre.setSafetClaimId(x.getSAFETClaimId());
                asre.setReimbursedCurrency(x.getReimbursedAmount().getCurrencyCode());
                asre.setReimbursedAmount(x.getReimbursedAmount().getCurrencyAmount());
                asre.setReasonCode(x.getReasonCode());

                yourAmazonSAFETReimbursementEvent.add(asre);
            });

            fe.getSellerReviewEnrollmentPaymentEventList().forEach(x ->{
                AmazonSellerReviewEnrollmentPaymentEvent asrepe = new AmazonSellerReviewEnrollmentPaymentEvent();
                asrepe.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
                asrepe.setEnrollmentId(x.getEnrollmentId());
                asrepe.setParentAsin(x.getParentASIN());
                asrepe.setCurrency(x.getTotalAmount().getCurrencyCode());
                asrepe.setTotalAmount(x.getTotalAmount().getCurrencyAmount());

                yourAmazonSellerReviewEnrollmentPaymentEvent.add(asrepe);
            });

            fe.getFBALiquidationEventList().forEach(x ->{
                AmazonFBALiquidationEvent afle =new AmazonFBALiquidationEvent();
                afle.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
                afle.setOriginalRemovalOrderId(x.getOriginalRemovalOrderId());
                afle.setLiquidationProceedsCurrency(x.getLiquidationProceedsAmount().getCurrencyCode());
                afle.setLiquidationProceedsAmount(x.getLiquidationProceedsAmount().getCurrencyAmount());
                afle.setLiquidationFeeCurrency(x.getLiquidationFeeAmount().getCurrencyCode());
                afle.setLiquidationFeeAmount(x.getLiquidationFeeAmount().getCurrencyAmount());

                yourAmazonFBALiquidationEvent.add(afle);
            });

            fe.getCouponPaymentEventList().forEach(x ->{
                AmazonCouponPaymentEvent acpe = new AmazonCouponPaymentEvent();
                acpe.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
                acpe.setCouponId(x.getCouponId());
                acpe.setSellerCouponDescription(x.getSellerCouponDescription());
                acpe.setClipOrRedemptionCount(x.getClipOrRedemptionCount());
                acpe.setPaymentEventId(x.getPaymentEventId());
                acpe.setCurrency(x.getTotalAmount().getCurrencyCode());
                acpe.setTotalAmount(x.getTotalAmount().getCurrencyAmount());

                yourAmazonCouponPaymentEvent.add(acpe);
            });

            fe.getImagingServicesFeeEventList().forEach(x ->{
                AmazonImagingServicesFeeEvent aisfe = new AmazonImagingServicesFeeEvent();
                aisfe.setImagingRequestBillingItemID(x.getImagingRequestBillingItemID());
                aisfe.setAsin(x.getASIN());
                aisfe.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));

                yourAmazonImagingServicesFeeEvent.add(aisfe);
            });

            fe.getNetworkComminglingTransactionEventList().forEach(x ->{
                AmazonNetworkComminglingTransactionEvent ancte = new AmazonNetworkComminglingTransactionEvent();
                ancte.setTransactionType(x.getTransactionType());
                ancte.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
                ancte.setNetCoTransactionID(x.getNetCoTransactionID());
                ancte.setSwapReason(x.getSwapReason());
                ancte.setAsin(x.getASIN());
                ancte.setMarketplaceId(x.getMarketplaceId());
                ancte.setCurrency(x.getTaxAmount().getCurrencyCode());
                ancte.setTaxExclusiveAmount(x.getTaxExclusiveAmount().getCurrencyAmount());
                ancte.setTaxAmount(x.getTaxAmount().getCurrencyAmount());

                yourAmazonNetworkComminglingTransactionEvent.add(ancte);
            });

            fe.getAffordabilityExpenseEventList().forEach(x ->{
                AmazonAffordabilityExpenseEvent aaee =new AmazonAffordabilityExpenseEvent();
                aaee.setAmazonOrderId(x.getAmazonOrderId());
                aaee.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
                aaee.setMarketplaceId(x.getMarketplaceId());
                aaee.setTransactionType(x.getTransactionType());
                aaee.setExpenseCurrency(x.getTotalExpense().getCurrencyCode());
                aaee.setBaseExpense(x.getBaseExpense().getCurrencyAmount());
                aaee.setTaxTypeIGST(x.getTaxTypeIGST().getCurrencyAmount());
                aaee.setTaxTypeCGST(x.getTaxTypeCGST().getCurrencyAmount());
                aaee.setTaxTypeSGST(x.getTaxTypeSGST().getCurrencyAmount());
                aaee.setTotalExpense(x.getTotalExpense().getCurrencyAmount());

                yourAmazonAffordabilityExpenseEvent.add(aaee);
            });

        fe.getAffordabilityExpenseReversalEventList().forEach(x ->{
            AmazonAffordabilityExpenseEvent aaere =new AmazonAffordabilityExpenseEvent();
            aaere.setAmazonOrderId(x.getAmazonOrderId());
            aaere.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            aaere.setMarketplaceId(x.getMarketplaceId());
            aaere.setTransactionType(x.getTransactionType());
            aaere.setExpenseCurrency(x.getTotalExpense().getCurrencyCode());
            aaere.setBaseExpense(x.getBaseExpense().getCurrencyAmount());
            aaere.setTaxTypeIGST(x.getTaxTypeIGST().getCurrencyAmount());
            aaere.setTaxTypeCGST(x.getTaxTypeCGST().getCurrencyAmount());
            aaere.setTaxTypeSGST(x.getTaxTypeSGST().getCurrencyAmount());
            aaere.setTotalExpense(x.getTotalExpense().getCurrencyAmount());

            yourAmazonAffordabilityExpenseReversalEvent.add(aaere);
        });

        fe.getRemovalShipmentEventList().forEach(x ->{
            AmazonRemovalShipmentEvent arse = new AmazonRemovalShipmentEvent();
            arse.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            arse.setOrderId(x.getOrderId());
            arse.setTransactionType(x.getTransactionType());

            yourAmazonRemovalShipmentEvent.add(arse);
        });

        fe.getTrialShipmentEventList().forEach(x ->{
            AmazonTrialShipmentEvent atse = new AmazonTrialShipmentEvent();
            atse.setAmazonOrderId(x.getAmazonOrderId());
            atse.setFinancialEventGroupId(x.getFinancialEventGroupId());
            atse.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            atse.setSku(x.getSKU());

            yourAmazonTrialShipmentEvent.add(atse);
        });

        fe.getTDSReimbursementEventList().forEach(x ->{
            AmazonTDSReimbursementEvent atre = new AmazonTDSReimbursementEvent();
            atre.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            atre.setTdsOrderId(x.getTdsOrderId());
            atre.setReimbursedCurrency(x.getReimbursedAmount().getCurrencyCode());
            atre.setReimbursedAmount(x.getReimbursedAmount().getCurrencyAmount());

            yourAmazonTDSReimbursementEvent.add(atre);
        });

        fe.getTaxWithholdingEventList().forEach(x ->{
            AmazonTaxWithholdingEvent atwe =new AmazonTaxWithholdingEvent();
            atwe.setPostedDate(formatDate(this.replaceZwithTimeZone(x.getPostedDate().toString())));
            atwe.setPeriodStart(OffsetDateTime.ofInstant(Instant.ofEpochMilli(x.getTaxWithholdingPeriod().getStartDateMillis()), ZoneId.systemDefault()));
            atwe.setPeriodEnd(OffsetDateTime.ofInstant(Instant.ofEpochMilli(x.getTaxWithholdingPeriod().getEndDateMillis()), ZoneId.systemDefault()));
            atwe.setCurrency(x.getBaseAmount().getCurrencyCode());
            atwe.setBaseAmount(x.getBaseAmount().getCurrencyAmount());
            atwe.setWithheldAmount(x.getWithheldAmount().getCurrencyAmount());

            yourAmazonTaxWithholdingEvent.add(atwe);
        });


        youFe.setShipmentEventList(yourShipmentEvent);
        youFe.setRefundEventList(yourAmazonRefundEvent);
        youFe.setGuaranteeClaimEventList(yourAmazonGuaranteeClaimEvent);
        youFe.setChargebackEventList(yourAmazonChargebackEvent);
        youFe.setPayWithAmazonEventList(yourAmazonPayWithAmazonEvent);
        youFe.setServiceProviderCreditEventList(yourAmazonSolutionProviderCreditEvent);
        youFe.setRetrochargeEventList(yourAmazonRetrochargeEvent);
        youFe.setRentalTransactionEventList(yourAmazonRentalTransactionEvent);
        youFe.setPerformanceBondRefundEventList(yourAmazonPerformanceBondRefundEvent);
        youFe.setProductAdsPaymentEventList(yourAmazonProductAdsPaymentEvent);
        youFe.setServiceFeeEventList(yourAmazonServiceFeeEvent);
        youFe.setSellerDealPaymentEventList(yourAmazonSellerDealPaymentEvent);
        youFe.setDebtRecoveryEventList(yourAmazonDebtRecoveryEvent);
        youFe.setLoanServicingEventList(yourAmazonLoanServicingEvent);
        youFe.setAdjustmentEventList(yourAmazonAdjustmentEvent);
        youFe.setSafetReimbursementEventList(yourAmazonSAFETReimbursementEvent);
        youFe.setSellerReviewEnrollmentPaymentEventList(yourAmazonSellerReviewEnrollmentPaymentEvent);
        youFe.setFbaLiquidationEventList(yourAmazonFBALiquidationEvent);
        youFe.setCouponPaymentEventList(yourAmazonCouponPaymentEvent);
        youFe.setImagingServicesFeeEventList(yourAmazonImagingServicesFeeEvent);
        youFe.setNetworkComminglingTransactionEventList(yourAmazonNetworkComminglingTransactionEvent);
        youFe.setAffordabilityExpenseEventList(yourAmazonAffordabilityExpenseEvent);
        youFe.setAffordabilityExpenseReversalEventList(yourAmazonAffordabilityExpenseReversalEvent);
        youFe.setRemovalShipmentEventList(yourAmazonRemovalShipmentEvent);
        youFe.setTrialShipmentEventList(yourAmazonTrialShipmentEvent);
        youFe.setTdsReimbursementEventList(yourAmazonTDSReimbursementEvent);
        youFe.setTaxWithholdingEventList(yourAmazonTaxWithholdingEvent);

        return youFe;
    }


}
