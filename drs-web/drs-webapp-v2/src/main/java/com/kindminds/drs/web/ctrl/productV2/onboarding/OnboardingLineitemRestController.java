package com.kindminds.drs.web.ctrl.productV2.onboarding;

import akka.pattern.Patterns;
import com.fasterxml.jackson.databind.JsonNode;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import akka.actor.ActorRef;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.kindminds.drs.util.Encryptor;
import com.kindminds.drs.api.message.query.onboardingApplication.*;
import com.kindminds.drs.api.message.command.onboardingApplication.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;


@RestController
@RequestMapping("/oai")
public class OnboardingLineitemRestController {


	ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();
	ActorRef drsQueryBus  = DrsActorSystem.drsQueryBus();

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value ="/tlp/g", method = RequestMethod.POST)
	public String trailListProductGet(@RequestBody String id ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		//get draft call
		Future<Object> getTrailList = Patterns.ask(
			drsQueryBus,  new GetTrialList(Encryptor.decrypt(id, false)),
			timeout
		);
		
		String jsonData = "";
		
		try {
			
			jsonData =  (String) Await.result(getTrailList , timeout.duration());

			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return jsonData;
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/tlp/s", method = RequestMethod.POST )
	public String trailListProductSave(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		ObjectMapper  mapper  = new ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing = Patterns.ask(drsCmdBus,
					new DoTrialList(Encryptor.decrypt(id, false), form.toString(), false), timeout);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
//		try {
//			String transactionsJSON = 
//				 (String) Await.result(doTrailListing, timeout.duration());
//			
//		} catch (Exception e) {		
//			e.printStackTrace();
//		}		
			
		
		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/tlp/sb", method = RequestMethod.POST)
	public String trailListProductSubmit(@RequestBody String jsonString ) {

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);
			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing = Patterns.ask(drsCmdBus,
					new DoTrialList(Encryptor.decrypt(id, false), form.toString(), true), timeout);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
//		try {
//			String transactionsJSON = 
//				 (String) Await.result(doTrailListing, timeout.duration());
//			
//		} catch (Exception e) {		
//			e.printStackTrace();
//		}		
			
		
		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/es/g",method = RequestMethod.POST)
	public String evalSampleGet(@RequestBody String id  ){


		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		//get draft call
		Future<Object> getTrailList = Patterns.ask(
			drsQueryBus,  new GetEvalSample(Encryptor.decrypt(id, false)),
			timeout
		);
		
		String jsonData = "";
		
		try {
			
			jsonData =  (String)Await.result(getTrailList , timeout.duration());
			

		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return jsonData;
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/es/s", method = RequestMethod.POST)
	public String evalSampleSave(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		ObjectMapper  mapper = new ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);
			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing = Patterns.ask(drsCmdBus,
					new EvaluateSample(Encryptor.decrypt(id, false), form.toString(), false), timeout);


		} catch (IOException e) {
			e.printStackTrace();
		}

		
//		try {
//			String transactionsJSON = 
//				 (String) Await.result(doTrailListing, timeout.duration());
//			
//		} catch (Exception e) {		
//			e.printStackTrace();
//		}		
			
		
		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/es/sb", method = RequestMethod.POST )
	public String evalSampleSubmit(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);
			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object>  doTrailListing = Patterns.ask(drsCmdBus,
					new EvaluateSample(Encryptor.decrypt(id, false), form.toString(), true), timeout);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
//		try {
//			String transactionsJSON = 
//				 (String) Await.result(doTrailListing, timeout.duration());
//			
//		} catch (Exception e) {		
//			e.printStackTrace();
//		}		
			
		
		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value  ="/p/g", method = RequestMethod.POST)
	public String presentGet(@RequestBody String id  ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		//get draft call
		Future<Object> getTrailList =  Patterns.ask(
			drsQueryBus,  new GetPresentSample(Encryptor.decrypt(id, false)),
			timeout
		);
		
		String jsonData = "";
		
		try {
			jsonData =  (String) Await.result(getTrailList , timeout.duration());

			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return jsonData;
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/p/s", method = RequestMethod.POST )
	public String presentSave(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("save draft");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new PresentSample(Encryptor.decrypt(id, false), form.toString(), false), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}

//		try {
//			String transactionsJSON = 
//				 (String) Await.result(doTrailListing, timeout.duration());
//			
//		} catch (Exception e) {		
//			e.printStackTrace();
//		}		
			
		
		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/p/sb", method = RequestMethod.POST )
	public String presentSubmit(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("submit");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new PresentSample(Encryptor.decrypt(id, false), form.toString(), true), timeout);


		} catch (IOException e) {
			e.printStackTrace();
		}

			
		
		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/pc/g",method = RequestMethod.POST)
	public String provideCommentGet(@RequestBody String id ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		//get draft call
		Future<Object> getTrailList =  Patterns.ask(
			drsQueryBus, new  GetProvideComment(Encryptor.decrypt(id, false)),
			timeout
		);

		String jsonData = "";
		
		try {

			jsonData =  (String) Await.result(getTrailList , timeout.duration());

		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return jsonData;
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/pc/s", method = RequestMethod.POST )
	public String provideCommentSave(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("save draft");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new ProvideComment(Encryptor.decrypt(id, false), form.toString(), false), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}


		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/pc/sb", method = RequestMethod.POST )
	public String provideCommentSubmit(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("submit");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new ProvideComment(Encryptor.decrypt(id, false), form.toString(), true), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return "success or fail";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/gf/g",method = RequestMethod.POST)
	public String  giveFeedbackGet(@RequestBody String id){


		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		//get draft call
		Future<Object> getTrailList =  Patterns.ask(
			drsQueryBus,  new GetGiveFeedback(Encryptor.decrypt(id, false)),
			timeout
		);

		String jsonData = "";
		
		try {

			jsonData =  (String) Await.result(getTrailList , timeout.duration()) ;

		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return jsonData;
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/gf/s", method = RequestMethod.POST )
	public String getGiveFeedbackSave(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("save draft");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new GiveFeedback(Encryptor.decrypt(id, false), form.toString(), false), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}


		
		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/gf/sb", method = RequestMethod.POST )
	public String giveFeedbackSubmit(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new GiveFeedback(Encryptor.decrypt(id, false), form.toString(), true), timeout);


		} catch (IOException e) {
			e.printStackTrace();
		}


		
		return "success or fail";
	}
	
	
	@RequestMapping(value = "/aps/g",method = RequestMethod.POST)
	public String approveSampleGet(@RequestBody String id ){


		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		//get draft call
		Future<Object> getTrailList =  Patterns.ask(
			drsQueryBus,  new GetApproveSample(Encryptor.decrypt(id, false)),
			timeout
		);

		String jsonData = "";
		
		try {

			jsonData =  (String) Await.result(getTrailList , timeout.duration());

		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return jsonData;
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/aps/s", method = RequestMethod.POST )
	public String approveSampleSave(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("save draft");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new ApproveSample(Encryptor.decrypt(id, false), form.toString(), false), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/aps/sb", method = RequestMethod.POST )
	public String approveSampleSubmit(@RequestBody String jsonString  ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new ApproveSample(Encryptor.decrypt(id, false), form.toString(), true), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return "success or fail";
	}
	
		
	@RequestMapping(value = "/ckc/g",method = RequestMethod.POST)
	public String checkComplianceGet(@RequestBody String id ){


		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		//get draft call
		Future<Object> getTrailList =  Patterns.ask(
			drsQueryBus, new GetCheckComplianceAndCertAvailability(Encryptor.decrypt(id, false)),
			timeout
		);

		String jsonData = "";
		
		try {

			jsonData =  (String) Await.result(getTrailList , timeout.duration()) ;

		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return jsonData;
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/ckc/s", method = RequestMethod.POST )
	public String checkComplianceSave(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("save draft");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new CheckComplianceAndCertificationAvailability(Encryptor.decrypt(id, false), form.toString(), false), timeout);


		} catch (IOException e) {
			e.printStackTrace();
		}


		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/ckc/sb", method = RequestMethod.POST )
	public String checkComplianceSubmit(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("submit");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");


			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
				new	CheckComplianceAndCertificationAvailability(Encryptor.decrypt(id, false), form.toString(), true), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}


		
		return "success or fail";
	}
	
	
	@RequestMapping(value = "/cki/g",method = RequestMethod.POST)
	public String checkInsuranceGet(@RequestBody String id ){


		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		//get draft call
		Future<Object> getTrailList =  Patterns.ask(
			drsQueryBus, new  GetCheckInsurance(Encryptor.decrypt(id, false)),
			timeout
		);

		String jsonData = "";
		
		try {

			jsonData =  (String)  Await.result(getTrailList , timeout.duration()) ;

		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return jsonData;
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/cki/s", method = RequestMethod.POST )
	public String checkInsuranceSave(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("save draft");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new CheckInsurance(Encryptor.decrypt(id, false), form.toString(), false), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value ="/cki/sb", method = RequestMethod.POST )
	public String checkInsuranceSubmit(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("submit");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new CheckInsurance(Encryptor.decrypt(id, false), form.toString(), true), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return "success or fail";
	}
	
	
	@RequestMapping(value = "/ckp/g",method = RequestMethod.POST)
	public String checkProfitabilityGet(@RequestBody String id ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		//get draft call
		Future<Object> getTrailList =  Patterns.ask(
			drsQueryBus,  new GetCheckProfitability(Encryptor.decrypt(id, false)),
			timeout
		);

		String jsonData = "";
		
		try {

			jsonData =  (String)  Await.result(getTrailList , timeout.duration()) ;

		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return jsonData;
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/ckp/s", method = RequestMethod.POST )
	public String checkProfitabilitySave(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("save draft");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");


			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new CheckProfitability(Encryptor.decrypt(id, false), form.toString(), false), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}


		
		return "success or fail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value = "/ckp/sb", method = RequestMethod.POST )
	public String checkProfitabilitySubmit(@RequestBody String jsonString ){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		System.out.println("submit");

		ObjectMapper  mapper = new  ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(jsonString);

			String id = rootNode.path("id").asText();
			JsonNode form = rootNode.path("form");

			//save draft database call here
			Future<Object> doTrailListing =  Patterns.ask(drsCmdBus,
					new CheckProfitability(Encryptor.decrypt(id, false), form.toString(), true), timeout);

		} catch (IOException e) {
			e.printStackTrace();
		}



		return "success or fail";
	}
	
	
	

	
	
}