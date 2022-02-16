package com.kindminds.drs.web.ctrl.productCategory;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.kindminds.drs.api.message.command.productCategory.SaveProductCategory;
import com.kindminds.drs.api.message.query.productCategory.GetListByParent;
import com.kindminds.drs.api.message.query.productCategory.GetProductCategory;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Map;
import java.util.concurrent.TimeoutException;

@Controller
@RestController
@RequestMapping(value = "/pc")
public class ProductCategoryController {

    private ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();

    private ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();

    @CrossOrigin
    @RequestMapping(value = "/spc", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String saveProductCategory(@RequestBody Map<String, Object> payload){

        String productCategory = payload.get("productCategory").toString();

        System.out.println(productCategory);

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new SaveProductCategory(productCategory), timeout);

        String jsonData = "";

        try {
            jsonData = (String) Await.result(futureResult, timeout.duration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return "done server";

    }

    @CrossOrigin
    @RequestMapping(value = "/g", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getProductCategory(@RequestBody Map<String, Object> payload){

        String parent =  payload.get("parent").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus,
                        new GetProductCategory(parent), timeout);

        String jsonData = "";

        try {
            jsonData = (String) Await.result(futureResult, timeout.duration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println(jsonData);

        return jsonData;


    }

    @CrossOrigin
    @RequestMapping(value = "/gl", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getParentList(@RequestBody Map<String, Object> payload){

        String parent =  payload.get("parent").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus,
                        new GetListByParent(parent), timeout);

        String jsonData = "";

        try {
            jsonData = (String) Await.result(futureResult, timeout.duration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println(jsonData);

        return jsonData;


    }


}
