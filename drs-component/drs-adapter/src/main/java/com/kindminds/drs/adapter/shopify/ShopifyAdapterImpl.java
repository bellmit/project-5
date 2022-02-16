package com.kindminds.drs.adapter.shopify;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.adapter.ShopifyAdapter;
import com.kindminds.drs.adapter.shopify.dto.RawShopifyOrder;
import com.kindminds.drs.adapter.shopify.dto.ShopifyOrderImpl;
import com.kindminds.drs.adapter.shopify.factory.ShopifyObjectFactory;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrder;

@Repository
public class ShopifyAdapterImpl implements ShopifyAdapter {
	
	public ShopifyOrder toOrder(String orderJsonStr) throws Exception{
		ObjectMapper mapper = this.createObjectMapper();
		JsonNode node = mapper.readTree(orderJsonStr);
		JsonNode eventsArrayNode = node.get("order");
		RawShopifyOrder rso = mapper.readValue(eventsArrayNode.toString(), RawShopifyOrder.class);
		return ShopifyObjectFactory.generateObject(rso,ShopifyOrderImpl.class);
	}

	@Override
	public ShopifyOrder getOrder() {
		String url = "https://true-to-source.myshopify.com/admin/orders/3411315009.json";
		String plainCreds = "c56b8344d4bf28f6fb82bf6610c218fa:ed5d7ab0906c2ee9bf5aa23ae56d0d8a";
		String authStringEnc = Base64.getEncoder().encodeToString(plainCreds.getBytes());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization","Basic "+authStringEnc);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		String eventsStr = response.getBody();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		try {
			JsonNode node = mapper.readTree(eventsStr);
			JsonNode eventsArrayNode = node.get("order");
			RawShopifyOrder se = mapper.readValue(eventsArrayNode.toString(), RawShopifyOrder.class);
			return new ShopifyOrderImpl(se);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ShopifyOrder getTestOrder() {
		String orderJsonStr = "{\"order\":{\"id\":3411315009,\"email\":\"untung7@me.com\",\"closed_at\":\"2016-06-29T03:25:25-07:00\",\"created_at\":\"2016-06-28T15:25:35-07:00\",\"updated_at\":\"2016-07-01T13:12:12-07:00\",\"number\":8,\"note\":null,\"token\":\"a643944e8b02455fd2221e7b1ea60697\",\"gateway\":\"shopify_payments\",\"test\":false,\"total_price\":\"67.51\",\"subtotal_price\":\"56.85\",\"total_weight\":26,\"total_tax\":\"4.71\",\"taxes_included\":false,\"currency\":\"USD\",\"financial_status\":\"paid\",\"confirmed\":true,\"total_discounts\":\"0.00\",\"total_line_items_price\":\"56.85\",\"cart_token\":\"ee74a21fc7e578297f31eb6439fa8f70\",\"buyer_accepts_marketing\":false,\"name\":\"USTTS1008\",\"referring_site\":\"https://www.google.com/\",\"landing_site\":\"/\",\"cancelled_at\":null,\"cancel_reason\":null,\"total_price_usd\":\"67.51\",\"checkout_token\":\"d1b70b96deb8d62ab2864f9e5f340b41\",\"reference\":null,\"user_id\":null,\"location_id\":null,\"source_identifier\":null,\"source_url\":null,\"processed_at\":\"2016-06-28T15:25:35-07:00\",\"device_id\":null,\"browser_ip\":\"104.32.213.19\",\"landing_site_ref\":null,\"order_number\":1008,\"discount_applications\":[],\"discount_codes\":[],\"note_attributes\":[],\"payment_gateway_names\":[\"shopify_payments\"],\"processing_method\":\"direct\",\"checkout_id\":10876705793,\"source_name\":\"web\",\"fulfillment_status\":\"fulfilled\",\"tax_lines\":[{\"title\":\"CA State Tax\",\"price\":\"4.71\",\"rate\":0.075}],\"tags\":\"\",\"contact_email\":\"untung7@me.com\",\"order_status_url\":\"https://checkout.shopify.com/11047414/checkouts/d1b70b96deb8d62ab2864f9e5f340b41/thank_you_token?key=ddc1ab63dba9b12de5bed8ace21ad27a\",\"total_tip_received\":\"0.0\",\"admin_graphql_api_id\":\"gid://shopify/Order/490234708066\",\"line_items\":[{\"id\":6005850369,\"variant_id\":16768365569,\"title\":\"EnergyFlux Lite 2600mAh Portable External Back Up Battery / Rechargeable Hand Warmer\",\"quantity\":2,\"price\":\"18.95\",\"grams\":9,\"sku\":\"K408-11A\",\"variant_title\":\"Green/Black\",\"vendor\":\"Human Creations\",\"fulfillment_service\":\"amazon_marketplace_web\",\"product_id\":5466156353,\"requires_shipping\":true,\"taxable\":true,\"gift_card\":false,\"name\":\"EnergyFlux Lite 2600mAh Portable External Back Up Battery / Rechargeable Hand Warmer - Green/Black\",\"variant_inventory_management\":\"amazon_marketplace_web\",\"properties\":[],\"product_exists\":true,\"fulfillable_quantity\":0,\"total_discount\":\"0.00\",\"fulfillment_status\":\"fulfilled\",\"discount_allocations\":[],\"admin_graphql_api_id\":\"gid://shopify/LineItem/1244590506082\",\"tax_lines\":[{\"title\":\"CA State Tax\",\"price\":\"2.84\",\"rate\":0.075}],\"origin_location\":{\"id\":1416643393,\"country_code\":\"US\",\"province_code\":\"DE\",\"name\":\"TrueToSource\",\"address1\":\"113 Barksdale Professional Center\",\"address2\":\"\",\"city\":\"Newark\",\"zip\":\"19711-3258\"},\"destination_location\":{\"id\":1474509569,\"country_code\":\"US\",\"province_code\":\"CA\",\"name\":\"Swee Peng Santoso\",\"address1\":\"2708 Doubletree Lane\",\"address2\":\"\",\"city\":\"Rowland Heights\",\"zip\":\"91748\"}},{\"id\":6005850433,\"variant_id\":16768336961,\"title\":\"EnergyFlux Lite 2600mAh Portable External Back Up Battery / Rechargeable Hand Warmer\",\"quantity\":1,\"price\":\"18.95\",\"grams\":9,\"sku\":\"K408-11C\",\"variant_title\":\"Blue/Black\",\"vendor\":\"Human Creations\",\"fulfillment_service\":\"amazon_marketplace_web\",\"product_id\":5466156353,\"requires_shipping\":true,\"taxable\":true,\"gift_card\":false,\"name\":\"EnergyFlux Lite 2600mAh Portable External Back Up Battery / Rechargeable Hand Warmer - Blue/Black\",\"variant_inventory_management\":\"amazon_marketplace_web\",\"properties\":[],\"product_exists\":true,\"fulfillable_quantity\":0,\"total_discount\":\"0.00\",\"fulfillment_status\":\"fulfilled\",\"tax_lines\":[{\"title\":\"CA State Tax\",\"price\":\"1.42\",\"rate\":0.075}],\"origin_location\":{\"id\":1416643393,\"country_code\":\"US\",\"province_code\":\"DE\",\"name\":\"TrueToSource\",\"address1\":\"113 Barksdale Professional Center\",\"address2\":\"\",\"city\":\"Newark\",\"zip\":\"19711-3258\"},\"destination_location\":{\"id\":1474509569,\"country_code\":\"US\",\"province_code\":\"CA\",\"name\":\"Swee Peng Santoso\",\"address1\":\"2708 Doubletree Lane\",\"address2\":\"\",\"city\":\"Rowland Heights\",\"zip\":\"91748\"}}],\"shipping_lines\":[{\"id\":2882415809,\"title\":\"Standard Shipping\",\"price\":\"5.95\",\"code\":\"Standard Shipping\",\"source\":\"shopify\",\"phone\":null,\"requested_fulfillment_service_id\":null,\"delivery_category\":null,\"carrier_identifier\":null,\"price_set\": {\"shop_money\":{\"amount\":\"2.93\",\"currency_code\":\"USD\"},\"presentment_money\":{\"amount\":\"2.93\",\"currency_code\":\"USD\"}},\"discounted_price_set\": {\"shop_money\":{\"amount\":\"2.93\",\"currency_code\":\"USD\"},\"presentment_money\":{\"amount\":\"2.93\",\"currency_code\":\"USD\"}},\"discount_allocations\":[],\"tax_lines\":[{\"title\":\"CA State Tax\",\"price\":\"0.45\",\"rate\":0.075}]}],\"billing_address\":{\"first_name\":\"Swee Peng\",\"address1\":\"2708 Doubletree Lane\",\"phone\":\"6269350266\",\"city\":\"Rowland Heights\",\"zip\":\"91748\",\"province\":\"California\",\"country\":\"United States\",\"last_name\":\"Santoso\",\"address2\":\"\",\"company\":\"\",\"latitude\":33.970488,\"longitude\":-117.89943,\"name\":\"Swee Peng Santoso\",\"country_code\":\"US\",\"province_code\":\"CA\"},\"shipping_address\":{\"first_name\":\"Swee Peng\",\"address1\":\"2708 Doubletree Lane\",\"phone\":\"6269350266\",\"city\":\"Rowland Heights\",\"zip\":\"91748\",\"province\":\"California\",\"country\":\"United States\",\"last_name\":\"Santoso\",\"address2\":\"\",\"company\":\"\",\"latitude\":33.970488,\"longitude\":-117.89943,\"name\":\"Swee Peng Santoso\",\"country_code\":\"US\",\"province_code\":\"CA\"},\"fulfillments\":[{\"id\":2662146689,\"order_id\":3411315009,\"status\":\"success\",\"created_at\":\"2016-06-28T15:25:37-07:00\",\"service\":\"amazon_marketplace_web\",\"updated_at\":\"2016-07-01T13:12:12-07:00\",\"tracking_company\":\"USPS\",\"shipment_status\":\"delivered\",\"tracking_number\":\"9361289692090111356627\",\"tracking_numbers\":[\"9361289692090111356627\"],\"tracking_url\":\"https://tools.usps.com/go/TrackConfirmAction_input?qtc_tLabels1=9361289692090111356627\",\"tracking_urls\":[\"https://tools.usps.com/go/TrackConfirmAction_input?qtc_tLabels1=9361289692090111356627\"],\"receipt\":{\"response_status\":\"Accepted\",\"response_comment\":\"Successfully submitted the order\"},\"line_items\":[{\"id\":6005850369,\"variant_id\":16768365569,\"title\":\"EnergyFlux Lite 2600mAh Portable External Back Up Battery / Rechargeable Hand Warmer\",\"quantity\":2,\"price\":\"18.95\",\"grams\":9,\"sku\":\"K408-11A\",\"variant_title\":\"Green/Black\",\"vendor\":\"Human Creations\",\"fulfillment_service\":\"amazon_marketplace_web\",\"product_id\":5466156353,\"requires_shipping\":true,\"taxable\":true,\"gift_card\":false,\"name\":\"EnergyFlux Lite 2600mAh Portable External Back Up Battery / Rechargeable Hand Warmer - Green/Black\",\"variant_inventory_management\":\"amazon_marketplace_web\",\"properties\":[],\"product_exists\":true,\"fulfillable_quantity\":0,\"total_discount\":\"0.00\",\"fulfillment_status\":\"fulfilled\",\"tax_lines\":[{\"title\":\"CA State Tax\",\"price\":\"2.84\",\"rate\":0.075}],\"origin_location\":{\"id\":1416643393,\"country_code\":\"US\",\"province_code\":\"DE\",\"name\":\"TrueToSource\",\"address1\":\"113 Barksdale Professional Center\",\"address2\":\"\",\"city\":\"Newark\",\"zip\":\"19711-3258\"},\"destination_location\":{\"id\":1474509569,\"country_code\":\"US\",\"province_code\":\"CA\",\"name\":\"Swee Peng Santoso\",\"address1\":\"2708 Doubletree Lane\",\"address2\":\"\",\"city\":\"Rowland Heights\",\"zip\":\"91748\"}},{\"id\":6005850433,\"variant_id\":16768336961,\"title\":\"EnergyFlux Lite 2600mAh Portable External Back Up Battery / Rechargeable Hand Warmer\",\"quantity\":1,\"price\":\"18.95\",\"grams\":9,\"sku\":\"K408-11C\",\"variant_title\":\"Blue/Black\",\"vendor\":\"Human Creations\",\"fulfillment_service\":\"amazon_marketplace_web\",\"product_id\":5466156353,\"requires_shipping\":true,\"taxable\":true,\"gift_card\":false,\"name\":\"EnergyFlux Lite 2600mAh Portable External Back Up Battery / Rechargeable Hand Warmer - Blue/Black\",\"variant_inventory_management\":\"amazon_marketplace_web\",\"properties\":[],\"product_exists\":true,\"fulfillable_quantity\":0,\"total_discount\":\"0.00\",\"fulfillment_status\":\"fulfilled\",\"tax_lines\":[{\"title\":\"CA State Tax\",\"price\":\"1.42\",\"rate\":0.075}],\"origin_location\":{\"id\":1416643393,\"country_code\":\"US\",\"province_code\":\"DE\",\"name\":\"TrueToSource\",\"address1\":\"113 Barksdale Professional Center\",\"address2\":\"\",\"city\":\"Newark\",\"zip\":\"19711-3258\"},\"destination_location\":{\"id\":1474509569,\"country_code\":\"US\",\"province_code\":\"CA\",\"name\":\"Swee Peng Santoso\",\"address1\":\"2708 Doubletree Lane\",\"address2\":\"\",\"city\":\"Rowland Heights\",\"zip\":\"91748\"}}]}],\"client_details\":{\"browser_ip\":\"104.32.213.19\",\"accept_language\":\"en-US,en;q=0.8\",\"user_agent\":\"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36\",\"session_hash\":\"ab18f5213430097b7c88c931323c9e41\",\"browser_width\":1903,\"browser_height\":979},\"refunds\":[],\"payment_details\":{\"credit_card_bin\":\"410039\",\"avs_result_code\":\"Y\",\"cvv_result_code\":\"M\",\"credit_card_number\":\"•••• •••• •••• 5044\",\"credit_card_company\":\"Visa\"},\"customer\":{\"id\":3356111361,\"email\":\"untung7@me.com\",\"accepts_marketing\":false,\"created_at\":\"2016-06-28T15:22:13-07:00\",\"updated_at\":\"2016-06-28T15:25:36-07:00\",\"first_name\":\"Swee Peng\",\"last_name\":\"Santoso\",\"orders_count\":1,\"state\":\"disabled\",\"total_spent\":\"67.51\",\"last_order_id\":3411315009,\"note\":null,\"verified_email\":true,\"multipass_identifier\":null,\"tax_exempt\":false,\"tags\":\"\",\"last_order_name\":\"USTTS1008\",\"default_address\":{\"id\":3528979713,\"first_name\":\"Swee Peng\",\"last_name\":\"Santoso\",\"company\":\"\",\"address1\":\"2708 Doubletree Lane\",\"address2\":\"\",\"city\":\"Rowland Heights\",\"province\":\"California\",\"country\":\"United States\",\"zip\":\"91748\",\"phone\":\"6269350266\",\"name\":\"Swee Peng Santoso\",\"province_code\":\"CA\",\"country_code\":\"US\",\"country_name\":\"United States\",\"default\":true}}}}";
		ObjectMapper mapper = this.createObjectMapper();
		try {
			JsonNode node = mapper.readTree(orderJsonStr);
			JsonNode eventsArrayNode = node.get("order");
			RawShopifyOrder rso = mapper.readValue(eventsArrayNode.toString(), RawShopifyOrder.class);
			return ShopifyObjectFactory.generateObject(rso,ShopifyOrderImpl.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ShopifyOrder> requestOrders(String updatedAtMin,String updatedAtMax,ShopifyOrderStatus status) throws Exception{
		URI uri = this.constructUri(updatedAtMin, updatedAtMax, status.getValue());
		HttpHeaders httpHeaders = this.constructHeaders();
		HttpEntity<String> request = new HttpEntity<String>(httpHeaders);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		String ordersJsonStr = response.getBody();
		ObjectMapper mapper = this.createObjectMapper();
		JsonNode ordersJsonNode = mapper.readTree(ordersJsonStr);
		JsonNode ordersArrayJsonNode = ordersJsonNode.get("orders");
		List<ShopifyOrder> shopifyOrders = new ArrayList<ShopifyOrder>();
		for(JsonNode orderJsonNode:ordersArrayJsonNode){
			RawShopifyOrder rawShopifyOrder = mapper.readValue(orderJsonNode.toString(), RawShopifyOrder.class);
			shopifyOrders.add(ShopifyObjectFactory.generateObject(rawShopifyOrder,ShopifyOrderImpl.class));
		}
		return shopifyOrders;
	}
	
	private HttpHeaders constructHeaders(){
		String plainCreds = "c56b8344d4bf28f6fb82bf6610c218fa:ed5d7ab0906c2ee9bf5aa23ae56d0d8a";
		String authStringEnc = Base64.getEncoder().encodeToString(plainCreds.getBytes());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization","Basic "+authStringEnc);
		return headers;
	}
	
	private URI constructUri(String updatedAtMin,String updatedAtMax,String status){
		String url = "https://true-to-source.myshopify.com/admin/orders.json";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("updated_at_min", updatedAtMin)
				.queryParam("updated_at_max", updatedAtMax)
				.queryParam("status", status);
		return uriComponentsBuilder.build().toUri();
	}
	
	private ObjectMapper createObjectMapper(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		return mapper;
	}

}
