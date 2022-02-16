package com.kindminds.drs.web.ctrl.productV2;

import akka.pattern.Patterns;
import com.kindminds.drs.api.message.query.prdocut.dashboard.GetDailyOrder;
import com.kindminds.drs.api.message.query.prdocut.dashboard.GetDetailPageSalesTraffic;
import com.kindminds.drs.api.message.viewKeyProductStatsUco.GetKeyProductBaseRetailPrice;
import com.kindminds.drs.api.message.viewKeyProductStatsUco.GetKeyProductInventoryStats;
import com.kindminds.drs.api.message.viewKeyProductStatsUco.GetKeyProductLastSevenDaysOrder;
import com.kindminds.drs.api.message.viewKeyProductStatsUco.GetKeyProductSinceLastSettlementOrder;
import org.springframework.security.access.prepost.PreAuthorize;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import akka.actor.ActorRef;
import com.kindminds.drs.util.Encryptor;
import com.kindminds.drs.api.message.query.onboardingApplication.*;
import com.kindminds.drs.api.message.command.onboardingApplication.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.query.customer.GetReturns;
import com.kindminds.drs.api.message.query.prdocut.dashboard.GetMTDCampaignSpendAndAcos;
import com.kindminds.drs.api.message.query.prdocut.dashboard.GetDailySalesQtyAndRev;
import com.kindminds.drs.api.message.query.product.GetAmazonProductReview;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import scala.Option;

import java.util.Map;
import java.util.concurrent.TimeoutException;


@RestController
@RequestMapping("/pd")
public class ProductDashboardRestController {


	 ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();


	//@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	//@CrossOrigin // todo dev only need remove after finishing developing fe
	@RequestMapping(value = "/gdsqr", method = RequestMethod.POST )
	public String getDailySalesQtyAndRev(@RequestBody Map<String, Object> payload ){

		Timeout timeout  =  new Timeout(Duration.create(60, "seconds"));

		final Future<Object> getDailySalesQtyAndRev = Patterns.ask(
				drsQueryBus, new  GetDailySalesQtyAndRev(
				payload.get("kCode") != null ?
					scala.Option.apply(payload.get("kCode").toString()) : scala.Option.empty(),
						payload.get("mp") != null ? scala.Option.apply(payload.get("mp").toString()) : scala.Option.empty(),
				payload.get("bpCode") != null ? scala.Option.apply(payload.get("bpCode").toString()) : scala.Option.empty(),
				payload.get("skuCode") != null ? scala.Option.apply(payload.get("skuCode").toString()) : scala.Option.empty()	),
				timeout
		);

		String jsonData = "";

		try {
			jsonData =  (String)Await.result(getDailySalesQtyAndRev , timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;
	}

	//@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	//@CrossOrigin // todo dev only need remove after finishing developing fe
	@RequestMapping(value = "/gdo", method = RequestMethod.POST )
	public String getDailyOrder(@RequestBody Map<String, Object> payload ){

		Timeout timeout  =  new Timeout(Duration.create(60, "seconds"));

		final Future<Object> getDailyOrder = Patterns.ask(
				drsQueryBus, new GetDailyOrder(
						payload.get("kCode") != null ?
								scala.Option.apply(payload.get("kCode").toString()) : scala.Option.empty(),
						payload.get("mp") != null ? scala.Option.apply(payload.get("mp").toString()) : scala.Option.empty(),
						payload.get("bpCode") != null ? scala.Option.apply(payload.get("bpCode").toString()) : scala.Option.empty(),
						payload.get("skuCode") != null ? scala.Option.apply(payload.get("skuCode").toString()) : scala.Option.empty()	),
				timeout
		);

		String jsonData = "";

		try {
			jsonData =  (String)Await.result(getDailyOrder , timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;
	}

	@RequestMapping(value = "/ginvst", method = RequestMethod.POST )
	public String getKeyProductInventoryStats(@RequestBody Map<String, Object> payload ){

		Timeout timeout  =  new Timeout(Duration.create(60, "seconds"));

		final Future<Object> result = Patterns.ask(
				drsQueryBus, new GetKeyProductInventoryStats(
						payload.get("kCode") != null ?
								scala.Option.apply(payload.get("kCode").toString()) : scala.Option.empty(),
						payload.get("mp") != null ? scala.Option.apply(payload.get("mp").toString()) : scala.Option.empty(),
						payload.get("bpCode") != null ? scala.Option.apply(payload.get("bpCode").toString()) : scala.Option.empty(),
						payload.get("skuCode") != null ? scala.Option.apply(payload.get("skuCode").toString()) : scala.Option.empty()	),
				timeout
		);

		String jsonData = "";

		try {
			jsonData =  (String)Await.result(result , timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;
	}

	@RequestMapping(value = "/gkplseor", method = RequestMethod.POST )
	public String getKeyProductSinceLastSettlementOrder(@RequestBody Map<String, Object> payload ){

		Timeout timeout  =  new Timeout(Duration.create(60, "seconds"));

		final Future<Object> result = Patterns.ask(
				drsQueryBus, new GetKeyProductSinceLastSettlementOrder(
						payload.get("kCode") != null ?
								scala.Option.apply(payload.get("kCode").toString()) : scala.Option.empty(),
						payload.get("mp") != null ? scala.Option.apply(payload.get("mp").toString()) : scala.Option.empty(),
						payload.get("bpCode") != null ? scala.Option.apply(payload.get("bpCode").toString()) : scala.Option.empty(),
						payload.get("skuCode") != null ? scala.Option.apply(payload.get("skuCode").toString()) : scala.Option.empty()	),
				timeout
		);

		String jsonData = "";

		try {
			jsonData =  (String)Await.result(result , timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;
	}


	@RequestMapping(value = "/gkplsor", method = RequestMethod.POST )
	public String getKeyProductLastSevenDaysOrder(@RequestBody Map<String, Object> payload ){

		Timeout timeout  =  new Timeout(Duration.create(60, "seconds"));

		final Future<Object> result = Patterns.ask(
				drsQueryBus, new GetKeyProductLastSevenDaysOrder(
						payload.get("kCode") != null ?
								scala.Option.apply(payload.get("kCode").toString()) : scala.Option.empty(),
						payload.get("mp") != null ? scala.Option.apply(payload.get("mp").toString()) : scala.Option.empty(),
						payload.get("bpCode") != null ? scala.Option.apply(payload.get("bpCode").toString()) : scala.Option.empty(),
						payload.get("skuCode") != null ? scala.Option.apply(payload.get("skuCode").toString()) : scala.Option.empty()	),
				timeout
		);

		String jsonData = "";

		try {
			jsonData =  (String)Await.result(result , timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;
	}

	@RequestMapping(value = "/gkprp", method = RequestMethod.POST )
	public String GetKeyProductBaseRetailPrice(@RequestBody Map<String, Object> payload ){

		Timeout timeout  =  new Timeout(Duration.create(60, "seconds"));

		final Future<Object> result = Patterns.ask(
				drsQueryBus, new GetKeyProductBaseRetailPrice(
						payload.get("kCode") != null ?
								scala.Option.apply(payload.get("kCode").toString()) : scala.Option.empty(),
						payload.get("mp") != null ? scala.Option.apply(payload.get("mp").toString()) : scala.Option.empty(),
						payload.get("bpCode") != null ? scala.Option.apply(payload.get("bpCode").toString()) : scala.Option.empty(),
						payload.get("skuCode") != null ? scala.Option.apply(payload.get("skuCode").toString()) : scala.Option.empty()	),
				timeout
		);

		String jsonData = "";

		try {
			jsonData =  (String)Await.result(result , timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;
	}

	@RequestMapping(value = "/gpst", method = RequestMethod.POST )
	public String getDetailPageSalesTraffic(@RequestBody Map<String, Object> payload ){

		Timeout timeout  =  new Timeout(Duration.create(60, "seconds"));

		final Future<Object> result = Patterns.ask(
				drsQueryBus, new GetDetailPageSalesTraffic(
						payload.get("kCode") != null ?
								scala.Option.apply(payload.get("kCode").toString()) : scala.Option.empty(),
						payload.get("mp") != null ? scala.Option.apply(payload.get("mp").toString()) : scala.Option.empty(),
						payload.get("bpCode") != null ? scala.Option.apply(payload.get("bpCode").toString()) : scala.Option.empty(),
						payload.get("skuCode") != null ? scala.Option.apply(payload.get("skuCode").toString()) : scala.Option.empty()	),
				timeout
		);

		String jsonData = "";

		try {
			jsonData =  (String)Await.result(result , timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	//@CrossOrigin // todo dev only need remove after finishing developing fe
	@RequestMapping(value = "/gapr", method = RequestMethod.POST )
	public String getAmazonReviews(@RequestBody Map<String, Object> payload  ){


		Timeout timeout  =  new Timeout(Duration.create(60, "seconds"));


		final Future<Object> getDailySalesQtyAndRev = Patterns.ask(
				drsQueryBus, new  GetAmazonProductReview(
						payload.get("kCode") != null ?
								scala.Option.apply(payload.get("kCode").toString()) : scala.Option.empty(),
						payload.get("mp") != null ? scala.Option.apply(payload.get("mp").toString()) : scala.Option.empty(),
						payload.get("bpCode") != null ? scala.Option.apply(payload.get("bpCode").toString()) : scala.Option.empty(),
						payload.get("skuCode") != null ? scala.Option.apply(payload.get("skuCode").toString()) : scala.Option.empty()),
				timeout
		);

		String jsonData = "";

		try {
			jsonData =  (String)Await.result(getDailySalesQtyAndRev , timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;
	}



	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	//@CrossOrigin // todo dev only need remove after finishing developing fe
	@RequestMapping(value = "/gre", method = RequestMethod.POST )
	public String getReturns(@RequestBody Map<String, Object> payload ){

		Timeout timeout  =  new Timeout(Duration.create(60, "seconds"));

		final Future<Object> getReturns =  Patterns.ask(
				drsQueryBus, new GetReturns(
						payload.get("kCode") != null ?
								scala.Option.apply(payload.get("kCode").toString()) : scala.Option.empty(),
						payload.get("mp") != null ? scala.Option.apply(payload.get("mp").toString()) : scala.Option.empty(),
						payload.get("bpCode") != null ? scala.Option.apply(payload.get("bpCode").toString()) : scala.Option.empty(),
						payload.get("skuCode") != null ? scala.Option.apply(payload.get("skuCode").toString()) : scala.Option.empty()),
				timeout
		);

		String jsonData = "";

		try {
			jsonData =  (String)Await.result(getReturns , timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	//@CrossOrigin // todo dev only need remove after finishing developing fe
	@RequestMapping(value = "/gca", method = RequestMethod.POST )
	public String getMTDCampaignSpendAndAcos(@RequestBody Map<String, Object> payload ){

		Timeout timeout  =  new Timeout(Duration.create(60, "seconds"));

		final Future<Object> getCampaignTSandAcos =  Patterns.ask(
				drsQueryBus,  new GetMTDCampaignSpendAndAcos(
						payload.get("kCode") != null ?
								scala.Option.apply(payload.get("kCode").toString()) : scala.Option.empty(),
						payload.get("mp") != null ? scala.Option.apply(payload.get("mp").toString()) : scala.Option.empty(),
						payload.get("bpCode") != null ? scala.Option.apply(payload.get("bpCode").toString()) : scala.Option.empty(),
						payload.get("skuCode") != null ? scala.Option.apply(payload.get("skuCode").toString()) : scala.Option.empty()),
				timeout
		);

		String jsonData = "";

		try {
			jsonData =  (String)Await.result(getCampaignTSandAcos , timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}


		return jsonData;
	}



}