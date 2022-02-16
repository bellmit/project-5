package com.kindminds.drs.adapter.amazon;


import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceException;

import com.amazonaws.mws.model.*;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.adapter.AmazonMwsFeedAdapter;
import com.kindminds.drs.MwsFeedType;
import com.kindminds.drs.adapter.amazon.config.AmazonMwsFeedConfig;
import com.kindminds.drs.adapter.amazon.config.MwsApi;
import com.kindminds.drs.adapter.amazon.config.MwsConfigFactory;


import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class AmazonMwsFeedAdapterImpl implements AmazonMwsFeedAdapter {


	private AmazonMwsFeedConfig config;


	public AmazonMwsFeedAdapterImpl() {
	}

	private void initializeConfig(Marketplace marketplace) {

		MwsConfigFactory configFactory = new MwsConfigFactory();
		config = (AmazonMwsFeedConfig) configFactory.getConfig(MwsApi.Feeds ,
				marketplace);

	}


    @Override
    public String submitFeedWithFile(String filePath, Marketplace marketplace, MwsFeedType feedType) {
		System.out.println("SUBMIT FEED");

		try {

			File initialFile = new File(filePath);
			InputStream feedContent = new FileInputStream(initialFile);

			initializeConfig(marketplace);
			for (String id : config.getMarketplaceIdList().getId()) {
				System.out.println("getMarketplaceIdList: " + id);
			}
			System.out.println("CONFIG SELLER ID: " + config.getSellerId());

			SubmitFeedRequest request = new SubmitFeedRequest()
					.withMerchant(config.getSellerId())
					.withMarketplaceIdList(config.getMarketplaceIdList())
					.withFeedType(feedType.getValue())
					.withFeedContent(feedContent)
					.withContentType(ContentType.TextXml);

			SubmitFeedResponse feedResponse = config.getClient().submitFeedFromFile(request);
			SubmitFeedResult feedResult = feedResponse.getSubmitFeedResult();
			String feedSubmissionId = feedResult.getFeedSubmissionInfo().getFeedSubmissionId();
			System.out.println("Feed Submission ID: " + feedSubmissionId);

			feedContent.close();
			return feedSubmissionId;

		} catch (MarketplaceWebServiceException ex) {
			ex.printStackTrace();

			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
			System.out.print("XML: " + ex.getXML());
			System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());

		} catch (Exception fe) {
			fe.printStackTrace();
		}

        return null;
    }

	@Override
	public String submitFeed(String xml, Marketplace marketplace, MwsFeedType feedType) {
		System.out.println("SUBMIT FEED");

		try {

			initializeConfig(marketplace);
			for (String id : config.getMarketplaceIdList().getId()) {
				System.out.println("getMarketplaceIdList: " + id);
			}
			System.out.println("CONFIG SELLER ID: " + config.getSellerId());


			InputStream feedContent = new ByteArrayInputStream(xml.getBytes());

			SubmitFeedRequest request = new SubmitFeedRequest()
					.withMerchant(config.getSellerId())
					.withMarketplaceIdList(config.getMarketplaceIdList())
					.withFeedType(feedType.getValue())
					.withFeedContent(feedContent)
					.withContentType(ContentType.TextXml);

			String contentMd5 = this.calculateContentMD5(xml);
			request.setContentMD5(contentMd5);

			SubmitFeedResponse feedResponse = config.getClient().submitFeed(request);
			SubmitFeedResult feedResult = feedResponse.getSubmitFeedResult();
			String feedSubmissionId = feedResult.getFeedSubmissionInfo().getFeedSubmissionId();
			System.out.println("Feed Submission ID: " + feedSubmissionId);

			feedContent.close();
			return feedSubmissionId;

		} catch (MarketplaceWebServiceException ex) {
			ex.printStackTrace();

			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
			System.out.print("XML: " + ex.getXML());
			System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());

		} catch (Exception fe) {
			fe.printStackTrace();
		}

		return null;
	}

	private String calculateContentMD5(String content) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(content.getBytes(StandardCharsets.UTF_8)); // <-- note encoding
			return new String(Base64.encodeBase64(hash));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}



    @Override
    public String getFeedSubmissionList(
    		Marketplace marketplace, IdList feedSubmissionIdList) {
		System.out.println("getFeedSubmissionList");

		initializeConfig(marketplace);

		GetFeedSubmissionListRequest submissionListRequest =
				new GetFeedSubmissionListRequest()
						.withFeedSubmissionIdList(feedSubmissionIdList)
						.withMerchant(config.getSellerId());


		return invokeGetFeedSubmissionList(config.getClient(), submissionListRequest);
    }

	@Override
	public String getFeedSubmissionList(
			Marketplace marketplace, Integer maxCount, TypeList feedTypeList) {
		System.out.println("getFeedSubmissionList maxCount");

		initializeConfig(marketplace);

		//todo arthur
		feedTypeList  = new TypeList().withType(MwsFeedType.Product_Feed.getValue());
		GetFeedSubmissionListRequest submissionListRequest =
				new GetFeedSubmissionListRequest()
						.withMerchant(config.getSellerId())
						.withMaxCount(maxCount);

		if (feedTypeList != null) {
			submissionListRequest.withFeedTypeList(feedTypeList);
		}


		return invokeGetFeedSubmissionList(config.getClient(), submissionListRequest);
	}

	private static String invokeGetFeedSubmissionList(
			MarketplaceWebService service, GetFeedSubmissionListRequest request) {

		try {
			GetFeedSubmissionListResponse response = service.getFeedSubmissionList(request);
			GetFeedSubmissionListResult listResult = response.getGetFeedSubmissionListResult();

			System.out.println(response.toXML());
			System.out.println("JSON: " + response.toJSON());
			System.out.println("response header metadata: " + response.getResponseHeaderMetadata().toString());

			for (FeedSubmissionInfo info : listResult.getFeedSubmissionInfoList()) {
				System.out.println("getFeedSubmissionId: " + info.getFeedSubmissionId());
				System.out.println("getFeedProcessingStatus: " + info.getFeedProcessingStatus());
				System.out.println("getStartedProcessingDate: " + info.getStartedProcessingDate());
				System.out.println("getCompletedProcessingDate: " + info.getCompletedProcessingDate());
				System.out.println("getFeedType: " + info.getFeedType());
			}
			return "";

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





    @Override
    public String getFeedSubmissionResult(
    		Marketplace marketplace, String feedSubmissionId) {

		System.out.println("getFeedSubmissionRESULT");

		initializeConfig(marketplace);

		System.out.println(feedSubmissionId);
		GetFeedSubmissionResultRequest request =
				new GetFeedSubmissionResultRequest()
						.withFeedSubmissionId(feedSubmissionId)
						.withMerchant(config.getSellerId());

        return invokeGetFeedSubmissionsResult(config.getClient(), request);
    }

	private static String invokeGetFeedSubmissionsResult(
			MarketplaceWebService service, GetFeedSubmissionResultRequest request) {

		try {


			GetFeedSubmissionResultResponse response = service.getFeedSubmissionResult(request);
			GetFeedSubmissionResultResult result = response.getGetFeedSubmissionResultResult();

			System.out.println("invokeGetFeedSubmissionsResult");
			System.out.println(response.toJSON());
			System.out.println("RESULT getMD5Checksum: " + result.getMD5Checksum());

			return "";
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





//    @Override
//    public GetFeedSubmissionCountResponse getFeedSubmissionCount(
//    		Marketplace marketplace, TypeList feedTypeList) {
//
//		initializeConfig(marketplace);
//
//		GetFeedSubmissionCountRequest request =
//				new GetFeedSubmissionCountRequest()
//						.withFeedTypeList(feedTypeList)
//						.withMerchant(config.getSellerId());
//
//
//        return invokeGetFeedSubmissionCount(config.getClient(), request);
//    }
//
//    private static GetFeedSubmissionCountResponse invokeGetFeedSubmissionCount(
//    		MarketplaceWebService service, GetFeedSubmissionCountRequest request) {
//
//		try {
//			GetFeedSubmissionCountResponse response = service.getFeedSubmissionCount(request);
//
//			System.out.println("invokeGetFeedSubmissionCount");
//			System.out.println(response.toXML());
//			System.out.println("JSON: " + response.toJSON());
//			System.out.println("response header metadata: " + response.getResponseHeaderMetadata().toString());
//
//
//			return response;
//		} catch (MarketplaceWebServiceException ex) {
//
//			System.out.println("Caught Exception: " + ex.getMessage());
//			System.out.println("Response Status Code: " + ex.getStatusCode());
//			System.out.println("Error Code: " + ex.getErrorCode());
//			System.out.println("Error Type: " + ex.getErrorType());
//			System.out.println("Request ID: " + ex.getRequestId());
//			System.out.print("XML: " + ex.getXML());
//			System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
//		}
//
//		return null;
//	}



//    @Override
//    public CancelFeedSubmissionsResponse cancelFeedSubmissions(
//    		Marketplace marketplace, IdList feedIdList) {
//
//		initializeConfig(marketplace);
//
//		CancelFeedSubmissionsRequest request =
//				new CancelFeedSubmissionsRequest()
//						.withFeedSubmissionIdList(feedIdList)
//						.withMerchant(config.getSellerId());
//
//
//		return invokeCancelFeedSubmissionsRequest(config.getClient(), request);
//    }
//
//	private static CancelFeedSubmissionsResponse invokeCancelFeedSubmissionsRequest(
//			MarketplaceWebService service, CancelFeedSubmissionsRequest request) {
//
//		try {
//			CancelFeedSubmissionsResponse response = service.cancelFeedSubmissions(request);
//
//			System.out.println("invokeCancelFeedSubmissionsRequest");
//			System.out.println(response.toXML());
//			System.out.println("JSON: " + response.toJSON());
//			System.out.println("response header metadata: " + response.getResponseHeaderMetadata().toString());
//
//
//			return response;
//		} catch (MarketplaceWebServiceException ex) {
//
//			System.out.println("Caught Exception: " + ex.getMessage());
//			System.out.println("Response Status Code: " + ex.getStatusCode());
//			System.out.println("Error Code: " + ex.getErrorCode());
//			System.out.println("Error Type: " + ex.getErrorType());
//			System.out.println("Request ID: " + ex.getRequestId());
//			System.out.print("XML: " + ex.getXML());
//			System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
//		}
//
//		return null;
//	}

}
