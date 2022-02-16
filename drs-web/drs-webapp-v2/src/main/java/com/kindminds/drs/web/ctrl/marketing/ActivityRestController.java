package com.kindminds.drs.web.ctrl.marketing;

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
import com.kindminds.drs.api.message.query.marketing.GetMarketingActivityList;
import akka.pattern.Patterns;
import com.kindminds.drs.api.message.command.CreateMarketingActivity;
import org.springframework.web.bind.annotation.RequestParam;
import com.kindminds.drs.api.message.query.marketing.GetMarketplacesAndIds;
import com.kindminds.drs.api.message.query.supplier.GetSupplierCodeList;
import com.kindminds.drs.api.message.query.marketing.GetMarketingActivityByFilters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.kindminds.drs.api.message.command.DeleteMarketingActivity;
import com.kindminds.drs.api.message.query.marketing.GetMarketingActivityById;
import org.springframework.web.bind.annotation.PathVariable;
import com.kindminds.drs.api.message.command.UpdateMarketingActivity;

import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


@RestController
@RequestMapping("/ms")
public class ActivityRestController {

    ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();

    ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();


	 //get marketplace list
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/mkp/g", method = RequestMethod.POST)
    public String marketplaceListGet()  {

		Timeout timeout = new Timeout(Duration.create(30, "seconds"));

		final Future<Object> marketplaceResult = Patterns.ask(drsQueryBus, new GetMarketplacesAndIds(), timeout);

        String jsonData = "";

		try {
			jsonData = (String) Await.result(marketplaceResult, timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return jsonData;

    }
	
	
	
    //get marketing activity list
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/mal/g", method = RequestMethod.POST)
    public String getMarketingActivityList(@RequestParam int page )  {

		Timeout timeout = new Timeout(Duration.create(30, "seconds"));

		final Future<Object>  futureResult = Patterns.ask(drsQueryBus, new GetMarketingActivityList("", page), timeout);

        String jsonData = "";

		try {
			jsonData = (String) Await.result(futureResult, timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}


        return jsonData;

    }
	
	
	//get marketing activity by ID
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/ma/g", method = RequestMethod.POST)
    public String getMarketingActivityById(@RequestParam String id )  {


		Timeout timeout = new Timeout(Duration.create(30, "seconds"));

		final Future<Object>  futureResult = Patterns.ask(drsQueryBus, new GetMarketingActivityById(id), timeout);

		String jsonData = "";

		try {
			jsonData = (String) Await.result(futureResult, timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}



		return jsonData;

    }
	
	
	//get filtered marketing activity list
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/mal/s", method = RequestMethod.POST)
    public String getFilteredMarketingActivityList(@RequestParam String supplier , @RequestParam String marketplace ,
    		@RequestParam String startDateString , @RequestParam String endDateString ,
			@RequestParam int pageIndex  ) throws ParseException {

		Timeout timeout = new Timeout(Duration.create(30, "seconds"));
		 
		Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
		Date endDate = new  SimpleDateFormat("yyyy-MM-dd").parse(endDateString);


		final Future<Object> futureResult = Patterns.ask(drsQueryBus, new GetMarketingActivityByFilters(marketplace, supplier, startDate,
			endDate, pageIndex), timeout);

		String jsonData = "";

		try {
			jsonData = (String) Await.result(futureResult, timeout.duration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}



		return jsonData;

    }
	
	
	//create marketing activity
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/create/submit", method = RequestMethod.POST)
    public String createMarketingActivity(@RequestBody String marketingActivity )  {

		Timeout timeout = new Timeout(Duration.create(30, "seconds"));

		final Future<Object> futureResult = Patterns.ask(drsCmdBus, new CreateMarketingActivity(marketingActivity), timeout);

        return "success or fail";

    }
	
	
	//edit marketing activity 
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/edit/{id}/submit", method = RequestMethod.POST)
    public String editMarketingActivity(@PathVariable String id , @RequestBody String marketingActivity )  {

		Timeout timeout = new Timeout(Duration.create(30, "seconds"));

		final Future<Object> futureResult = Patterns.ask(drsCmdBus, new UpdateMarketingActivity(marketingActivity), timeout);

        return "success or fail";

    }
	
	
	//delete marketing activity 
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteMarketingActivity(@RequestParam String id )  {

		Timeout timeout = new Timeout(Duration.create(30, "seconds"));

		final Future<Object> futureResult = Patterns.ask(drsCmdBus, new DeleteMarketingActivity(id), timeout);

        return "success or fail";

    }

}



