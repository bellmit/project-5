package com.kindminds.drs.service.usecase;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestCustomerCareCaseElasticSearch {
	
    @Test
    public void testElasticSearchConnection() {
    	SearchRequest searchRequest = new SearchRequest("drs-all_orders");
    	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    	MatchAllQueryBuilder matchAllQuery = new MatchAllQueryBuilder();
    	searchSourceBuilder.query(matchAllQuery).size(0);
    	searchSourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));
		searchSourceBuilder.trackTotalHits(true);
    	searchRequest.source(searchSourceBuilder);

    	try (RestHighLevelClient client = new RestHighLevelClient(
    	        RestClient.builder(
    	                new HttpHost("ec2-18-179-112-14.ap-northeast-1.compute.amazonaws.com", 9200, "http")))) {
    		
	    	SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			long totalHits = hits.getTotalHits().value;
			RestStatus status = searchResponse.status();
			TimeValue took = searchResponse.getTook();
			float maxScore = hits.getMaxScore();
			System.out.println("status: " + status);
			System.out.println("took: " + took);
			System.out.println("hits: " + hits);
			System.out.println("totalHits: " + totalHits);
			System.out.println("maxScore: " + maxScore);
			Assert.assertEquals("OK", status.toString());
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
    @Test
    public void testElasticMultiMatchSearch() {
    	SearchRequest searchRequest = new SearchRequest("t-customer_care_cases_view");
    	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    	String searchString = "john ";
    	String[] searchWords = searchString.trim().split("\\s");
    	for (String word : searchWords) {
    		System.out.println(word);
    	}

    	BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    	MultiMatchQueryBuilder multiMatch = new MultiMatchQueryBuilder(searchWords[0]);
    	multiMatch.field("marketplace_order_id");
    	multiMatch.field("supplier_en_name");
    	multiMatch.field("base_product_name");
    	multiMatch.field("sku_product_name");
    	multiMatch.field("issue_type_name");
    	multiMatch.field("issue_type_category_name");
    	multiMatch.field("issue_name");
    	multiMatch.field("id", (float) 2.1);
    	multiMatch.field("customer_name", 2);
    	multiMatch.field("product_base_code", (float) 3.0);
    	multiMatch.field("product_sku_code", (float) 1.5);
    	multiMatch.type(MultiMatchQueryBuilder.Type.CROSS_FIELDS);
    	multiMatch.lenient(true);
    	MatchPhraseQueryBuilder phraseQuery = new MatchPhraseQueryBuilder("supplier_en_name", "Sound Land");
    	phraseQuery.slop(0);
    	boolQueryBuilder.must(phraseQuery);
    	boolQueryBuilder.must(multiMatch);
    	searchSourceBuilder.query(boolQueryBuilder).size(0);
    	searchSourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));
		searchSourceBuilder.trackTotalHits(true);
    	searchRequest.source(searchSourceBuilder);

    	try (RestHighLevelClient client = new RestHighLevelClient(
    	        RestClient.builder(
    	                new HttpHost("10.0.0.248", 9200, "http")))) {
    		
	    	SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			long totalHits = hits.getTotalHits().value;
	    	
	    	searchSourceBuilder.from(0); 
	    	searchSourceBuilder.size(20);
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			RestStatus status = searchResponse.status();
			TimeValue took = searchResponse.getTook();
			hits = searchResponse.getHits();
			totalHits = hits.getTotalHits().value;
			float maxScore = hits.getMaxScore();
			
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit : searchHits) {
				System.out.println("-----------------------");
				System.out.println("SCORE*****: " + hit.getScore());
				Map<String, Object> sourceAsMap = hit.getSourceAsMap();
				System.out.println("id: " + (Integer) sourceAsMap.get("id"));
				System.out.println("customer_name: " + (String)sourceAsMap.get("customer_name"));
				System.out.println("type: " + (String)sourceAsMap.get("type"));
				System.out.println("supplier_en_name: " + (String)sourceAsMap.get("supplier_en_name"));
				System.out.println("issue_type_name: " + (String)sourceAsMap.get("issue_type_name"));
				System.out.println("sku_product_name: " + (String)sourceAsMap.get("sku_product_name"));
				System.out.println("product_sku_code: " + (String)sourceAsMap.get("product_sku_code"));
				System.out.println("product_base_code: " + (String)sourceAsMap.get("product_base_code"));
				System.out.println("base_product_name: " + (String)sourceAsMap.get("base_product_name"));
				System.out.println("issue_id: " + (Integer) sourceAsMap.get("issue_id"));
				System.out.println("date_create: " + (Long) sourceAsMap.get("date_create"));
				System.out.println("seconds_from_last_activity: " + (Integer) sourceAsMap.get("seconds_from_last_activity"));
				System.out.println("marketplace_name: " + (String) sourceAsMap.get("marketplace_name"));
				System.out.println("supplier_local_name: " + (String)sourceAsMap.get("supplier_local_name"));
			}
			System.out.println("status: " + status);
			System.out.println("took: " + took);
			System.out.println("hits: " + hits);
			System.out.println("totalHits: " + totalHits);
			System.out.println("maxScore: " + maxScore);
			Assert.assertEquals("OK", status.toString());
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
}
