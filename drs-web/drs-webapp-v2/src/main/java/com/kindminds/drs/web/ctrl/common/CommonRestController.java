package com.kindminds.drs.web.ctrl.common;

import com.kindminds.drs.Context;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.api.message.command.notification.DeleteNotification;
import com.kindminds.drs.api.message.command.notification.MarkNotificationAsRead;
import com.kindminds.drs.api.message.query.notification.GetNotifications;
import com.kindminds.drs.security.SecurityUser;
import org.apache.commons.collections4.multiset.SynchronizedMultiSet;
import org.springframework.security.access.prepost.PreAuthorize;
import akka.util.Timeout;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import com.kindminds.drs.api.message.query.marketing.GetMarketingActivityList;
import akka.pattern.Patterns;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.command.CreateMarketingActivity;
import com.kindminds.drs.api.message.query.marketing.GetMarketplacesAndIds;
import com.kindminds.drs.api.message.query.supplier.GetSupplierCodeList;
import com.kindminds.drs.api.message.query.marketing.GetMarketingActivityByFilters;
import java.text.SimpleDateFormat;
import com.kindminds.drs.api.message.command.DeleteMarketingActivity;
import com.kindminds.drs.api.message.query.marketing.GetMarketingActivityById;
import com.kindminds.drs.api.message.command.UpdateMarketingActivity;
import com.kindminds.drs.api.message.query.product.GetProductBaseCode;
import com.kindminds.drs.api.message.query.product.GetProductSkuCode;
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel;

import com.kindminds.drs.api.usecase.sales.ListCustomerOrderUco;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;


@RestController
@RequestMapping("/c")
public class CommonRestController {

    ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();
    ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();

	 //get supplier list
     @CrossOrigin // todo dev only need remove after finishing developing fe
    //@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value ="/sup/g", method = RequestMethod.POST)
    public String supplierListGet()  {

         Timeout timeout = new Timeout(Duration.create(30, "seconds"));

         final Future<Object> supplierResult =
                 Patterns.ask(drsQueryBus, new GetSupplierCodeList(), timeout);

        String jsonData = "";

         try {
             jsonData = (String)Await.result(supplierResult, timeout.duration()) ;
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (TimeoutException e) {
             e.printStackTrace();
         }


         return jsonData;

    }

    //get supplier list
    @CrossOrigin // todo dev only need remove after finishing developing fe
   // @PreAuthorize("hasAnyReole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/bp/g", method = RequestMethod.POST)
    public String bpListGet(@RequestBody Map<String, Object> payload )  {

        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object>  marketplaceResult =
                Patterns.ask(drsQueryBus, new GetProductBaseCode(payload.get("kcode").toString()),
                        timeout);

        String jsonData = "";

        try {
            jsonData = (String)Await.result(marketplaceResult, timeout.duration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        return jsonData;

    }

    //get supplier list
    @CrossOrigin // todo dev only need remove after finishing developing fe
    //@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/sk/g", method = RequestMethod.POST)
    public String skuListGet(@RequestBody  Map<String, Object> payload )  {

        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> marketplaceResult = Patterns.ask(drsQueryBus,
                new GetProductSkuCode(payload.get("bpCode").toString()), timeout);

        String jsonData = "";

        try {
            jsonData = (String)Await.result(marketplaceResult, timeout.duration()) ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        return jsonData;

    }

    //get supplier list
    @CrossOrigin // todo dev only need remove after finishing developing fe
   // @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/mkpl/g", method = RequestMethod.POST)
    public Map  marketplaceGet() {

        //List<Pair<Int, String>

       Map salesChannels = new HashMap<Integer,String>();
       salesChannels.put(SalesChannel.AMAZON_COM.getKey(),SalesChannel.AMAZON_COM.getDisplayName());
       salesChannels.put(SalesChannel.AMAZON_CO_UK.getKey(),SalesChannel.AMAZON_CO_UK.getDisplayName());
       salesChannels.put(SalesChannel.AMAZON_CA.getKey(),SalesChannel.AMAZON_CA.getDisplayName());
       salesChannels.put(SalesChannel.AMAZON_DE.getKey(),SalesChannel.AMAZON_DE.getDisplayName());
       salesChannels.put(SalesChannel.AMAZON_FR.getKey(),SalesChannel.AMAZON_FR.getDisplayName());
       salesChannels.put(SalesChannel.AMAZON_IT.getKey(),SalesChannel.AMAZON_IT.getDisplayName());
       salesChannels.put(SalesChannel.AMAZON_ES.getKey(),SalesChannel.AMAZON_ES.getDisplayName());


        return salesChannels;

    }


    @CrossOrigin
    //@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/u", method = RequestMethod.POST)
    public String getUserInfo(@RequestBody Map<String, Object> payload  , HttpServletRequest request )  {


       // UserInfo u =  Context.getCurrentUser();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityUser su =   ((SecurityUser)userDetails);
        UserInfo u = su.getDRSUser();

        boolean enableSS = false;
        for (GrantedAuthority a : su.getAuthorities()) {
            if(a.getAuthority().equals("ROLE_DCP_SS_CLIENT")){
                enableSS = true;
                break;
            }
        }

        //todo arthur do we provide uid to fe
        String jsonData = "{\"uid\": "+  u.getUserId()  +", \"cid\":\""+ u.getCompanyKcode() +"\" ,\"name\":\"" +
                u.getUserDisplayName() +"\" , \"isSp\":" +  u.isSupplier() + " , \"enableSS\" : " + enableSS +" " +
                ", \"locale\":\""+ u.getLocale() +"\" }";

        return jsonData;
    }



    @CrossOrigin
    //@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value = "/ctx", method = RequestMethod.POST)
    public String payloadData(HttpServletRequest request )  {

        UserInfo u =  Context.getCurrentUser();
        String key = u.getUserId() + "_p2mApp";

        String p2mAppId ="";
        if(request.getSession().getAttribute(key)!=null){
            p2mAppId= request.getSession().getAttribute(key).toString();
        }

        String jsonData = "{";

        if(!StringUtils.isEmpty(p2mAppId)){
            jsonData += "\"p2mAppId\": \"" + p2mAppId + "\" }";
        }else{
            jsonData += "\"p2mAppId\": \"\" }";

        }

        return jsonData;
    }





}



