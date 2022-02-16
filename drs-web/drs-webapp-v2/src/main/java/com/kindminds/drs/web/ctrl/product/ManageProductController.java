package com.kindminds.drs.web.ctrl.product;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.command.manageProduct.CreateBaseProduct;
import com.kindminds.drs.api.message.command.manageProduct.DeleteBaseProduct;
import com.kindminds.drs.api.message.command.manageProduct.UpdateBaseProduct;
import com.kindminds.drs.api.message.command.product.VerifyEANCode;

import com.kindminds.drs.api.message.command.product.VerifySku;
import com.kindminds.drs.api.message.query.manageProduct.GetBaseProductById;
import com.kindminds.drs.api.message.query.manageProduct.GetBaseProducts;
import com.kindminds.drs.api.message.query.manageProduct.GetNextPage;
import com.kindminds.drs.api.message.query.manageProduct.GetSimpleProductList;



import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Controller
@RestController
@RequestMapping(value = "/product/mdbp")
public class ManageProductController {

	private ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();
	private ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();


	private ObjectMapper om = new ObjectMapper();

	@CrossOrigin
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
	@RequestMapping(value = "/bpl", method = RequestMethod.POST)
	public String listProducts(@RequestBody Map<String, Object> payload) {

		String supplierId = payload.get("si").toString();
		Integer pageIndex = (Integer) payload.get("pi");

		final java.time.Duration t = java.time.Duration.ofSeconds(120);
		CompletionStage<String> stage
				= akka.pattern.Patterns.ask(drsQueryBus,  new GetBaseProducts(supplierId, pageIndex), t).thenApply(
				obj -> (String) obj);

		String jsonData = "";
		try {
			jsonData = stage.toCompletableFuture().get(120, SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}



		return jsonData;
	}

	@CrossOrigin
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
	@RequestMapping(value = "/sbpl", method = RequestMethod.POST)
	public String getSimpleProductList(@RequestBody Map<String, Object> payload) {

		String supplierId = payload.get("si").toString();


		final java.time.Duration t = java.time.Duration.ofSeconds(120);
		CompletionStage<String> stage
				= akka.pattern.Patterns.ask(drsQueryBus,  new GetSimpleProductList(supplierId), t).thenApply(
				obj -> (String) obj);

		String jsonData = "";
		try {
			jsonData = stage.toCompletableFuture().get(120, SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}



		return jsonData;
	}

	@CrossOrigin
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
	@RequestMapping(value = "/np", method = RequestMethod.POST)
	public String listNextPage(@RequestBody Map<String, Object> payload) {

		String supplierId = payload.get("si").toString();
		Integer pageIndex = (Integer) payload.get("ci");

		final java.time.Duration t = java.time.Duration.ofSeconds(120);
		CompletionStage<String> stage
				= akka.pattern.Patterns.ask(drsQueryBus,  new GetNextPage(supplierId, pageIndex), t).thenApply(
				obj -> (String) obj);

		String jsonData = "";
		try {
			jsonData = stage.toCompletableFuture().get(120, SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}



		return jsonData;
	}

	@CrossOrigin
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
	@RequestMapping(value = "/gbp", method = RequestMethod.POST)
	public String getBaseProduct(@RequestBody Map<String, Object> payload) {

		String supplierId = payload.get("si").toString();
		String productId = payload.get("bi").toString();


		final java.time.Duration t = java.time.Duration.ofSeconds(10);
		CompletionStage<String> stage
				= akka.pattern.Patterns.ask(drsQueryBus,  new GetBaseProductById(supplierId, productId), t).thenApply(
				obj -> (String) obj);

		String jsonData = "";
		try {
			jsonData = stage.toCompletableFuture().get(10, SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}



		return jsonData;
	}

	@CrossOrigin
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
	@RequestMapping(value = "/c", method = RequestMethod.POST)
	public String createProduct(@RequestBody Map<String, Object> payload ) {

		//why return data
		String supplierId = payload.get("si").toString();
		String productJson = payload.get("bp").toString();

		Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

		final Future<Object> futureResult =
				Patterns.ask(drsCmdBus, new CreateBaseProduct(supplierId, productJson), timeout);

		String jsonData = "";

		try {
			jsonData = (String)Await.result(futureResult, timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}



		return jsonData;
	}

	@CrossOrigin
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
	@RequestMapping(value = "/u", method = RequestMethod.POST)
	public String updateProduct(@RequestBody Map<String, Object> payload) {

		String supplierId = payload.get("si").toString();
		String bpId = payload.get("bi").toString();
		String productJson = payload.get("bp").toString();

		Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

		final Future<Object> futureResult =
				Patterns.ask(drsCmdBus, new UpdateBaseProduct(supplierId, bpId, productJson), timeout);

		String jsonData = "";

		try {
			jsonData = (String)Await.result(futureResult, timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}



		return jsonData;
	}

	@CrossOrigin
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
	@RequestMapping(value = "/d", method = RequestMethod.POST)
	public String deleteProduct(@RequestBody Map<String, Object> payload) {

		String supplierId =  payload.get("si").toString();
		String bpId = payload.get("bi").toString();

		Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

		final Future<Object> futureResult =
				Patterns.ask(drsCmdBus, new DeleteBaseProduct(supplierId, bpId), timeout);

		String jsonData = "";

		try {
			jsonData = (String)Await.result(futureResult, timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}



		return jsonData;
	}

	@CrossOrigin
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
	@RequestMapping(value = "/vec", method = RequestMethod.POST)
	public String verifyEANCode(@RequestBody Map<String, Object> payload) {

		String productId = payload.get("pi").toString();

		final java.time.Duration t = java.time.Duration.ofSeconds(10);
		CompletionStage<String> stage
				= akka.pattern.Patterns.ask(drsCmdBus,  new VerifyEANCode(productId), t).thenApply(
				obj -> obj.toString());

		String jsonData = "";
		try {
			jsonData = stage.toCompletableFuture().get(10, SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;
	}

	@CrossOrigin
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
	@RequestMapping(value = "/vsc", method = RequestMethod.POST)
	public String verifySkuCode(@RequestBody Map<String, Object> payload) {

		String sku = payload.get("sku").toString();

		Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

		final Future<Object> futureResult =
				Patterns.ask(drsCmdBus, new VerifySku(sku), timeout);

		String jsonData = "";

		try {
			jsonData = Await.result(futureResult, timeout.duration()).toString();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}



		return jsonData;
	}

			
}