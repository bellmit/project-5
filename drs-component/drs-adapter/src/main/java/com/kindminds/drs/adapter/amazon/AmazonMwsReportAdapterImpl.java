package com.kindminds.drs.adapter.amazon;


import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.*;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.adapter.AmazonMwsReportAdapter;
import com.kindminds.drs.MwsReportType;
import com.kindminds.drs.adapter.amazon.config.AmazonMwsReportConfig;

import com.kindminds.drs.adapter.amazon.config.MwsApi;
import com.kindminds.drs.adapter.amazon.config.MwsConfigFactory;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class AmazonMwsReportAdapterImpl implements AmazonMwsReportAdapter {


	private AmazonMwsReportConfig config = null;
	private RequestReportRequest request = null;


	private void init(MwsReportType reportType , Marketplace marketplace){


		MwsConfigFactory configFactory = new MwsConfigFactory();
		 config =
				(AmazonMwsReportConfig)configFactory.getConfig(MwsApi.Report ,
						marketplace);

		 request = new RequestReportRequest()
				.withMerchant(config.getSellerId())
				.withMarketplaceIdList(config.getMarketplaceIdList())
				.withReportType(reportType.getValue())
				.withReportOptions("ShowSalesChannel=true");
	}


	public String requestReport(MwsReportType reportType , Marketplace marketplace) {


		this.init(reportType, marketplace);


		return this.invokeRequestReport(config.getClient(), request);



	}

	@Override
	public String requestReport(MwsReportType reportType, Marketplace marketplace, ZonedDateTime startDate,
								ZonedDateTime endDate) {

		this.init(reportType,marketplace);
		// demonstrates how to set the date range
		DatatypeFactory df = null;
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		System.out.println(startDate);

		XMLGregorianCalendar sd = df.newXMLGregorianCalendar(GregorianCalendar.from(startDate));
		request.setStartDate(sd);

		System.out.println(endDate);
		XMLGregorianCalendar ed = df
				.newXMLGregorianCalendar(GregorianCalendar.from(endDate));
		request.setEndDate(ed);

		return this.invokeRequestReport(config.getClient(), request);

	}

	@Override
	public OutputStream getReport(String reportId ,Marketplace marketplace) {

		//todo config
		MwsConfigFactory configFactory = new MwsConfigFactory();
		AmazonMwsReportConfig config =
				(AmazonMwsReportConfig)configFactory.getConfig(MwsApi.Report ,  marketplace);

		String sellerId = config.getSellerId();

		GetReportRequest request = new GetReportRequest();
		request.setMerchant( sellerId );

		request.setReportId(reportId);

		OutputStream report = null;
		try {
			report = new ByteArrayOutputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setReportOutputStream( report );

		return invokeGetReport(config.getClient(), request);

	}

	@Override
	public String getReportId(String requestId , MwsReportType reprotType ,Marketplace marketplace) {


		MwsConfigFactory configFactory = new MwsConfigFactory();
		AmazonMwsReportConfig config =
				(AmazonMwsReportConfig)configFactory.getConfig(MwsApi.Report ,  marketplace);

		String sellerId = config.getSellerId();

		GetReportListRequest request = new GetReportListRequest();
		request.setMerchant( sellerId );
		TypeList tl  = new TypeList().withType(reprotType.getValue());
		request.setReportTypeList(tl);
		//request.setReportRequestIdList();

		return invokeGetReportList(config.getClient(), request , requestId);

	}

	@Override
	public List<String> getReportIds( MwsReportType reportType, Marketplace marketplace) {

		MwsConfigFactory configFactory = new MwsConfigFactory();
		AmazonMwsReportConfig config =
				(AmazonMwsReportConfig)configFactory.getConfig(MwsApi.Report ,  marketplace);

		String sellerId = config.getSellerId();

		GetReportListRequest request = new GetReportListRequest();
		request.setMerchant( sellerId );
		TypeList tl  = new TypeList().withType(reportType.getValue());
		request.setReportTypeList(tl);
		//request.setReportRequestIdList();

		List<String> rptIds = new ArrayList<String>();
		java.util.List<ReportInfo> reportInfoListList = invokeGetReportList(config.getClient(), request );

		//todo arthur
			rptIds.add(reportInfoListList.get(0).getReportId());
			rptIds.add(reportInfoListList.get(1).getReportId());
		rptIds.add(reportInfoListList.get(2).getReportId());
		rptIds.add(reportInfoListList.get(3).getReportId());
		rptIds.add(reportInfoListList.get(4).getReportId());

				for (ReportInfo reportInfoList : reportInfoListList) {
					System.out.print("            ReportInfoList");
					System.out.println();
					if (reportInfoList.isSetReportId()) {
						System.out.print("                ReportId");
						System.out.println();
						System.out.print("                    " + reportInfoList.getReportId());
						System.out.println();
					}
					if (reportInfoList.isSetReportType()) {
						System.out.print("                ReportType");
						System.out.println();
						System.out.print("                    " + reportInfoList.getReportType());
						System.out.println();
					}
					if (reportInfoList.isSetReportRequestId()) {
						System.out.print("                ReportRequestId");
						System.out.println();
						System.out.print("                    " + reportInfoList.getReportRequestId());
						System.out.println();

					}
					if (reportInfoList.isSetAvailableDate()) {
						System.out.print("                AvailableDate");
						System.out.println();
						System.out.print("                    " + reportInfoList.getAvailableDate());
						System.out.println();
					}
					if (reportInfoList.isSetAcknowledged()) {
						System.out.print("                Acknowledged");
						System.out.println();
						System.out.print("                    " + reportInfoList.isAcknowledged());
						System.out.println();
					}
					if (reportInfoList.isSetAcknowledgedDate()) {
						System.out.print("                AcknowledgedDate");
						System.out.println();
						System.out.print("                    " + reportInfoList.getAcknowledgedDate());
						System.out.println();
					}

				}



		 return rptIds;
	}

	@Override
	public List<String> getScheduledReportIds(MwsReportType reportType, Marketplace marketplace) {

		return null;
	}

	@Override
	public String getReportListByNextToken(String token , String requestId ,Marketplace marketplace) {


		MwsConfigFactory configFactory = new MwsConfigFactory();
		AmazonMwsReportConfig config =
				(AmazonMwsReportConfig)configFactory.getConfig(MwsApi.Report ,  marketplace);

		String sellerId = config.getSellerId();



		GetReportListByNextTokenRequest request = new GetReportListByNextTokenRequest();
		request.setMerchant( sellerId );
		request.setNextToken(token);

		return invokeGetReportListByNextToken(config.getClient(), request , requestId);

	}




	/**
	 * Request Report  request sample
	 * requests the generation of a report
	 *
	 * @param service instance of MarketplaceWebService service
	 * @param request Action to invoke
	 */
	public static String invokeRequestReport(MarketplaceWebService service, RequestReportRequest request) {

		String requestId = "";

		try {


			RequestReportResponse response = service.requestReport(request);

			System.out.println ("RequestReport Action Response");
			System.out.println ("=============================================================================");
			System.out.println ();

			System.out.print("    RequestReportResponse");
			System.out.println();
			if (response.isSetRequestReportResult()) {
				System.out.print("        RequestReportResult");
				System.out.println();
				RequestReportResult  requestReportResult = response.getRequestReportResult();
				if (requestReportResult.isSetReportRequestInfo()) {
					System.out.print("            ReportRequestInfo");
					System.out.println();
					ReportRequestInfo  reportRequestInfo = requestReportResult.getReportRequestInfo();

					if (reportRequestInfo.isSetReportRequestId()) {
						System.out.print("                ReportRequestId");
						System.out.println();
						System.out.print("                    " + reportRequestInfo.getReportRequestId());
						System.out.println();
						requestId =  reportRequestInfo.getReportRequestId();
					}
					if (reportRequestInfo.isSetReportType()) {
						System.out.print("                ReportType");
						System.out.println();
						System.out.print("                    " + reportRequestInfo.getReportType());
						System.out.println();
					}
					if (reportRequestInfo.isSetStartDate()) {
						System.out.print("                StartDate");
						System.out.println();
						System.out.print("                    " + reportRequestInfo.getStartDate());
						System.out.println();
					}
					if (reportRequestInfo.isSetEndDate()) {
						System.out.print("                EndDate");
						System.out.println();
						System.out.print("                    " + reportRequestInfo.getEndDate());
						System.out.println();
					}
					if (reportRequestInfo.isSetSubmittedDate()) {
						System.out.print("                SubmittedDate");
						System.out.println();
						System.out.print("                    " + reportRequestInfo.getSubmittedDate());
						System.out.println();
					}
					if (reportRequestInfo.isSetReportProcessingStatus()) {
						System.out.print("                ReportProcessingStatus");
						System.out.println();
						System.out.print("                    " + reportRequestInfo.getReportProcessingStatus());
						System.out.println();
					}
					//System.out.println(	reportRequestInfo.getGeneratedReportId());
				}

			}
			if (response.isSetResponseMetadata()) {
				System.out.print("        ResponseMetadata");
				System.out.println();
				ResponseMetadata  responseMetadata = response.getResponseMetadata();
				if (responseMetadata.isSetRequestId()) {
					System.out.print("            RequestId");
					System.out.println();
					System.out.print("                " + responseMetadata.getRequestId());
					System.out.println();
				}
			}

			System.out.println();
			System.out.println(response.getResponseHeaderMetadata());
			System.out.println();


		} catch (MarketplaceWebServiceException ex) {

			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
			System.out.print("XML: " + ex.getXML());
			System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
		}


		return requestId;
	}


	/**
	 * Get Report  request sample
	 * The GetReport operation returns the contents of a report. Reports can potentially be
	 * very large (>100MB) which is why we only return one report at a time, and in a
	 * streaming fashion.
	 *
	 * @param service instance of MarketplaceWebService service
	 * @param request Action to invoke
	 */
	public static OutputStream invokeGetReport(MarketplaceWebService service, GetReportRequest request) {

		OutputStream os = null;

		try {

			GetReportResponse response = service.getReport(request);


			System.out.println ("GetReport Action Response");
			System.out.println ("=============================================================================");
			System.out.println ();

			System.out.print("    GetReportResponse");
			System.out.println();
			System.out.print("    GetReportResult");
			System.out.println();
			System.out.print("            MD5Checksum");
			System.out.println();
			System.out.print("                " + response.getGetReportResult().getMD5Checksum());
			System.out.println();
			if (response.isSetResponseMetadata()) {
				System.out.print("        ResponseMetadata");
				System.out.println();
				ResponseMetadata  responseMetadata = response.getResponseMetadata();
				if (responseMetadata.isSetRequestId()) {
					System.out.print("            RequestId");
					System.out.println();
					System.out.print("                " + responseMetadata.getRequestId());
					System.out.println();
				}
			}
			System.out.println();

			System.out.println("Report");
			System.out.println ("=============================================================================");
			System.out.println();


			os  = request.getReportOutputStream();

			/*
			OutputStreamWriter writer = null;
			try {
				writer = new OutputStreamWriter(
						request.getReportOutputStream() , "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}*/

			//System.out.println( request.getReportOutputStream().toString() );
			//System.out.println();

			System.out.println(response.getResponseHeaderMetadata());
			System.out.println();


		} catch (MarketplaceWebServiceException ex) {

			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
			System.out.print("XML: " + ex.getXML());
			System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
		}


		return os;



	}


	/**
	 * Get Report List  request sample
	 * returns a list of reports; by default the most recent ten reports,
	 * regardless of their acknowledgement status
	 *
	 * @param service instance of MarketplaceWebService service
	 * @param request Action to invoke
	 */
	public static String invokeGetReportList(MarketplaceWebService service,
											 GetReportListRequest request , String requestId) {

	String reportId = "";
		try {

			GetReportListResponse response = service.getReportList(request);

			System.out.println ("GetReportList Action Response");
			System.out.println ("=============================================================================");
			System.out.println ();

			System.out.print("    GetReportListResponse");
			System.out.println();
			if (response.isSetGetReportListResult()) {
				System.out.print("        GetReportListResult");
				System.out.println();
				GetReportListResult  getReportListResult = response.getGetReportListResult();
				if (getReportListResult.isSetNextToken()) {
					System.out.print("            NextToken");
					System.out.println();
					System.out.print("                " + getReportListResult.getNextToken());
					System.out.println();
				}
				if (getReportListResult.isSetHasNext()) {
					System.out.print("            HasNext");
					System.out.println();
					System.out.print("                " + getReportListResult.isHasNext());
					System.out.println();
				}

				java.util.List<ReportInfo> reportInfoListList = getReportListResult.getReportInfoList();
				for (ReportInfo reportInfoList : reportInfoListList) {
					System.out.print("            ReportInfoList");
					System.out.println();
					if (reportInfoList.isSetReportId()) {
						System.out.print("                ReportId");
						System.out.println();
						System.out.print("                    " + reportInfoList.getReportId());
						System.out.println();
					}
					if (reportInfoList.isSetReportType()) {
						System.out.print("                ReportType");
						System.out.println();
						System.out.print("                    " + reportInfoList.getReportType());
						System.out.println();
					}
					if (reportInfoList.isSetReportRequestId()) {
						System.out.print("                ReportRequestId");
						System.out.println();
						System.out.print("                    " + reportInfoList.getReportRequestId());
						System.out.println();

					}
					if (reportInfoList.isSetAvailableDate()) {
						System.out.print("                AvailableDate");
						System.out.println();
						System.out.print("                    " + reportInfoList.getAvailableDate());
						System.out.println();
					}
					if (reportInfoList.isSetAcknowledged()) {
						System.out.print("                Acknowledged");
						System.out.println();
						System.out.print("                    " + reportInfoList.isAcknowledged());
						System.out.println();
					}
					if (reportInfoList.isSetAcknowledgedDate()) {
						System.out.print("                AcknowledgedDate");
						System.out.println();
						System.out.print("                    " + reportInfoList.getAcknowledgedDate());
						System.out.println();
					}

					if (reportInfoList.isSetReportRequestId()) {

						System.out.println(reportInfoList.getReportRequestId());
						System.out.println(requestId);
						if(reportInfoList.getReportRequestId().trim().equals(requestId.trim())){
							if (reportInfoList.isSetReportId()) {
								System.out.println(reportInfoList.getReportId());
								reportId = reportInfoList.getReportId();
							}
						}
					}

				}
			}

			if (response.isSetResponseMetadata()) {
				System.out.print("        ResponseMetadata");
				System.out.println();
				ResponseMetadata  responseMetadata = response.getResponseMetadata();
				if (responseMetadata.isSetRequestId()) {
					System.out.print("            RequestId");
					System.out.println();
					System.out.print("                " + responseMetadata.getRequestId());
					System.out.println();
				}
			}
			System.out.println();
			System.out.println(response.getResponseHeaderMetadata());
			System.out.println();


		} catch (MarketplaceWebServiceException ex) {

			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
			System.out.print("XML: " + ex.getXML());
			System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
		}

		return reportId;
	}


	public static java.util.List<ReportInfo> invokeGetReportList(MarketplaceWebService service,
											 GetReportListRequest request) {


		try {

			GetReportListResponse response = service.getReportList(request);
			GetReportListResult  getReportListResult = response.getGetReportListResult();
			java.util.List<ReportInfo> reportInfoListList = getReportListResult.getReportInfoList();

			return reportInfoListList;


		} catch (MarketplaceWebServiceException ex) {

			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
			System.out.print("XML: " + ex.getXML());
			System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
		}

		return null;
	}

	public static String invokeGetReportListByNextToken(MarketplaceWebService service,
														GetReportListByNextTokenRequest request , String requestId) {

		String reportId = "";
		try {

			GetReportListByNextTokenResponse response = service.getReportListByNextToken(request);

			System.out.println ("GetReportList Action Response");
			System.out.println ("=============================================================================");
			System.out.println ();

			System.out.print("    GetReportListResponse");
			System.out.println();
			if (response.isSetGetReportListByNextTokenResult()) {
				System.out.print("        GetReportListResult");
				System.out.println();
				GetReportListByNextTokenResult  getReportListResult = response.getGetReportListByNextTokenResult();
				if (getReportListResult.isSetNextToken()) {
					System.out.print("            NextToken");
					System.out.println();
					System.out.print("                " + getReportListResult.getNextToken());
					System.out.println();
				}
				if (getReportListResult.isSetHasNext()) {
					System.out.print("            HasNext");
					System.out.println();
					System.out.print("                " + getReportListResult.isHasNext());
					System.out.println();
				}

				java.util.List<ReportInfo> reportInfoListList = getReportListResult.getReportInfoList();
				for (ReportInfo reportInfoList : reportInfoListList) {
					System.out.print("            ReportInfoList");
					System.out.println();
					if (reportInfoList.isSetReportId()) {
						System.out.print("                ReportId");
						System.out.println();
						System.out.print("                    " + reportInfoList.getReportId());
						System.out.println();
					}
					if (reportInfoList.isSetReportType()) {
						System.out.print("                ReportType");
						System.out.println();
						System.out.print("                    " + reportInfoList.getReportType());
						System.out.println();
					}
					if (reportInfoList.isSetReportRequestId()) {
						System.out.print("                ReportRequestId");
						System.out.println();
						System.out.print("                    " + reportInfoList.getReportRequestId());
						System.out.println();

					}
					if (reportInfoList.isSetAvailableDate()) {
						System.out.print("                AvailableDate");
						System.out.println();
						System.out.print("                    " + reportInfoList.getAvailableDate());
						System.out.println();
					}
					if (reportInfoList.isSetAcknowledged()) {
						System.out.print("                Acknowledged");
						System.out.println();
						System.out.print("                    " + reportInfoList.isAcknowledged());
						System.out.println();
					}
					if (reportInfoList.isSetAcknowledgedDate()) {
						System.out.print("                AcknowledgedDate");
						System.out.println();
						System.out.print("                    " + reportInfoList.getAcknowledgedDate());
						System.out.println();
					}

					if (reportInfoList.isSetReportRequestId()) {

						System.out.println(reportInfoList.getReportRequestId());
						System.out.println(requestId);
						if(reportInfoList.getReportRequestId().trim().equals(requestId.trim())){
							if (reportInfoList.isSetReportId()) {
								System.out.println(reportInfoList.getReportId());
								reportId = reportInfoList.getReportId();
							}
						}
					}

				}
			}

			if (response.isSetResponseMetadata()) {
				System.out.print("        ResponseMetadata");
				System.out.println();
				ResponseMetadata  responseMetadata = response.getResponseMetadata();
				if (responseMetadata.isSetRequestId()) {
					System.out.print("            RequestId");
					System.out.println();
					System.out.print("                " + responseMetadata.getRequestId());
					System.out.println();
				}
			}
			System.out.println();
			System.out.println(response.getResponseHeaderMetadata());
			System.out.println();


		} catch (MarketplaceWebServiceException ex) {

			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
			System.out.print("XML: " + ex.getXML());
			System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
		}

		return reportId;
	}



}
