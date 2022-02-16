package com.kindminds.drs.web.ctrl.logistics.ivs;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.kindminds.drs.Context;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.api.message.query.user.GetUserRoles;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/shipment")
public class IvsController {


    ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value = "/ivp" , method = RequestMethod.POST)
    public String ivsVerifyProduct(HttpServletRequest request ) {

        UserInfo u =  Context.getCurrentUser();
        String isLogistics   = "false";

        Timeout timeout =  new Timeout(Duration.create(5, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus, new GetUserRoles(u.getUsername()), timeout);

        String result = null ;
        try {
            result = (String) Await.result(futureResult, timeout.duration());
            if(result.contains("DRS_LOGISTICS")){
                isLogistics = "true";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        String ivsName = request.getParameter("ivs");
        String dc = request.getParameter("dc");
        String lineSeq = request.getParameter("lineSeq");
        String boxNum = request.getParameter("boxNum");
        String mixedBoxLineSeq = request.getParameter("mixedBoxLineSeq");
        String sku = request.getParameter("sku");
        String productVerificationStatus = request.getParameter("productVerificationStatus");
        String qty = request.getParameter("qty");

        List valList = Arrays.asList(ivsName,dc,lineSeq,boxNum,mixedBoxLineSeq,
                sku,isLogistics , productVerificationStatus,qty);

        String key = u.getUserId() + "_IvsVp";
        List ivsVpList = (List)request.getSession().getAttribute(key);
        if(ivsVpList == null ){
            request.getSession(true).setAttribute(key , valList);
        }else{
            request.getSession().setAttribute(key , valList);
        }



        return "th/reactIndex";

    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value = "/fulfillmentList" , method = RequestMethod.GET)
    public String fulfillmentList()  {

        return "th/reactIndex";

    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value = "/exportShipmentList" , method = {RequestMethod.GET})
    public String exportShipmentList()  {

        return "th/reactIndex";

    }


    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value = "/unifiedShipmentList" , method = {RequestMethod.GET})
    public String unifiedShipmentList()  {

        return "th/reactIndex";

    }



    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value ="/exportBasicInfo" , method = {RequestMethod.GET})
    public String exportBasicInfo()  {

        return "th/reactIndex";

    }


    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value = "/transportationInformation" , method = {RequestMethod.GET})
    public String transportationInformation()  {

        return "th/reactIndex";

    }


    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value ="/itemDetails" , method = {RequestMethod.GET})
    public String itemDetails()  {

        return "th/reactIndex";

    }


    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value = "/checkProducts" , method = {RequestMethod.GET})
    public String checkProducts()  {

        return "th/reactIndex";

    }


    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value = "/checkPackages" , method = {RequestMethod.GET})
    public String checkPackages()  {

        return "th/reactIndex";

    }


    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value = "/finalizeRemarkedItems" , method = {RequestMethod.GET})
    public String finalizeRemarkedItems()  {

        return "th/reactIndex";

    }



}


