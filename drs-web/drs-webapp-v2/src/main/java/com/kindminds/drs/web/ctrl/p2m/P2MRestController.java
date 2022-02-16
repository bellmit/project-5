package com.kindminds.drs.web.ctrl.p2m;

import akka.actor.ActorRef;

import akka.pattern.Patterns;
import akka.util.Timeout;
import com.kindminds.drs.Context;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.api.message.command.p2m.*;
import com.kindminds.drs.api.message.query.manageProduct.GetTotalProductNumber;
import com.kindminds.drs.api.message.query.manageProduct.GetTotalSkuNumber;
import com.kindminds.drs.api.message.query.p2m.*;

import com.kindminds.drs.util.Encryptor;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.helpers.SyslogWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

//todo arthur have to handle spring security and user info


@RestController
@RequestMapping(value = "/p2m")
public class P2MRestController {

    //todo arthur remove crossorigin

    private ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();

    private ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();

    @Autowired
    ServletContext servletContext;

//    UserInfo u =  Context.getCurrentUser();

    @CrossOrigin
    @RequestMapping(value = "/ch", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String change(@RequestBody Map<String, Object> payload){

        String kcode =  payload.get("si").toString();
        String p2mId = payload.get("pid").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new ChangeP2MApplication(p2mId , Context.getCurrentUser().getUserId()), timeout);

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
    @RequestMapping(value = "/up", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String update(@RequestBody Map<String, Object> payload){

        String kcode =  payload.get("si").toString();
        String p2mId = payload.get("pid").toString();
        String skuInfo = payload.get("skui").toString();
        String currentAp = payload.get("currentAp").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new UpdateP2MApplication(p2mId , currentAp , skuInfo , Context.getCurrentUser().getUserId()), timeout);

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
    @RequestMapping(value = "/c", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String create(@RequestBody Map<String, Object> payload){

        String currentAp = payload.get("currentAp").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new CreateP2MApplication(currentAp , Context.getCurrentUser().getUserId()), timeout);

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
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/s", method = RequestMethod.POST)
    public String submit(@RequestBody Map<String, Object> payload){

        UserInfo u =  Context.getCurrentUser();
        //Integer supplierId = (Integer) payload.get("si");
        String p2mAp = payload.get("p2mAp").toString();

        //Integer supplierId = (Integer) payload.get("si");

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new SubmitP2MApplication(p2mAp, u.getUserId()
                                , u.getCompanyKcode()), timeout);

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
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/atr", method = RequestMethod.POST)
    public String applytoremove(@RequestBody Map<String, Object> payload){

        UserInfo u =  Context.getCurrentUser();
        //Integer supplierId = (Integer) payload.get("si");
        String p2mId = payload.get("p2mId").toString();

        //Integer supplierId = (Integer) payload.get("si");

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new ApplyToRemoveP2MApplication(p2mId, u.getUserId()
                                , u.getCompanyKcode()), timeout);

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
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/rtr", method = RequestMethod.POST)
    public String rejecttoremove(@RequestBody Map<String, Object> payload){

        if(Context.getCurrentUser().isDrsUser()){

            UserInfo u =  Context.getCurrentUser();
            //Integer supplierId = (Integer) payload.get("si");
            String p2mAp = payload.get("p2mAp").toString();
            Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

            final Future<Object> futureResult =
                    Patterns.ask(drsCmdBus,
                            new RejectToRemoveP2MApplication(p2mAp,u.getUserId()), timeout);

            String jsonData = "";

            try {
                jsonData = (String) Await.result(futureResult, timeout.duration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            return "done server";
        }else{
            return "fail";

        }


    }

    @CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/aprtr", method = RequestMethod.POST)
    public String approvetoremove(@RequestBody Map<String, Object> payload){

        if(Context.getCurrentUser().isDrsUser()){

            UserInfo u =  Context.getCurrentUser();
            //Integer supplierId = (Integer) payload.get("si");
            String p2mAp = payload.get("p2mAp").toString();
            Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

            final Future<Object> futureResult =
                    Patterns.ask(drsCmdBus,
                            new ApproveToRemoveP2MApplication(p2mAp,u.getUserId()), timeout);

            String jsonData = "";

            try {
                jsonData = (String) Await.result(futureResult, timeout.duration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            return "done server";
        }else{
            return "fail";

        }


    }

    @CrossOrigin
    @RequestMapping(value = "/d", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String delete(@RequestBody Map<String, Object> payload){

        UserInfo u =  Context.getCurrentUser();

        String p2mId = payload.get("pid").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new DeleteP2MApplication(p2mId, u.getUserId()
                                , u.getCompanyKcode()), timeout);

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
    @RequestMapping(value = "/re", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String reApply(@RequestBody Map<String, Object> payload){

       // String kcode =  payload.get("si").toString();
        String p2mId = payload.get("pid").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new ReApplyP2MApplication(p2mId , Context.getCurrentUser().getUserId()), timeout);

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
    @RequestMapping(value = "/smi", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String saveMarketplaceInfo(@RequestBody Map<String, Object> payload){

        //Integer supplierId = (Integer) payload.get("si");
        String mpi = payload.get("marketplaceInfo").toString();

        //System.out.println(supplierId);
        System.out.println(mpi);

        //Integer supplierId = (Integer) payload.get("si");
        //todo arthur it can get from usercontext
        Integer supplierId = 1;

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new SaveMarketplaceInfo(mpi), timeout);

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
    @RequestMapping(value = "/si", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String saveInsurance(@RequestBody Map<String, Object> payload){

        //Integer supplierId = (Integer) payload.get("si");
        String insurance = payload.get("insurance").toString();

        //System.out.println(supplierId);
        System.out.println(insurance);

        //Integer supplierId = (Integer) payload.get("si");
        //todo arthur it can get from usercontext
        Integer supplierId = 1;

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new SaveInsurance(insurance), timeout);

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
    @RequestMapping(value = "/sr", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String saveRegional(@RequestBody Map<String, Object> payload){

        //Integer supplierId = (Integer) payload.get("si");
        String regional = payload.get("regional").toString();

        //System.out.println(supplierId);
        System.out.println(regional);

        //Integer supplierId = (Integer) payload.get("si");
        //todo arthur it can get from usercontext
        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new SaveRegional(regional), timeout);

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
    @RequestMapping(value = "/spi", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String saveP2MProductInfo(@RequestBody Map<String, Object> payload){

        //Integer supplierId = (Integer) payload.get("si");
        String productInfo = payload.get("productInfo").toString();

        //System.out.println(supplierId);
        System.out.println(productInfo);

        //Integer supplierId = (Integer) payload.get("si");
        //todo arthur it can get from usercontext
        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new SaveP2MProductInfo(productInfo), timeout);

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
    @RequestMapping(value = "/ssh", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String saveP2MShipping(@RequestBody Map<String, Object> payload){

        //Integer supplierId = (Integer) payload.get("si");
        String shipping = payload.get("shipping").toString();

        //System.out.println(supplierId);
        System.out.println(shipping);

        //Integer supplierId = (Integer) payload.get("si");
        //todo arthur it can get from usercontext
        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new SaveShipping(shipping), timeout);

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
    public String getP2MApplication(@RequestBody Map<String, Object> payload){

        //System.out.println(payload);

        String id =  payload.get("id").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus,
                        new GetP2MApplication(id), timeout);

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

    @CrossOrigin
    @RequestMapping(value = "/apl", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String listApplications(@RequestBody Map<String, Object> payload){

        String supplierId = payload.get("si").toString();
        Integer pageIndex = (Integer) payload.get("pi");
        String country = payload.get("c").toString();
        String status = payload.get("s").toString();
        String kcode = payload.get("k").toString();
        String product = payload.get("p").toString();

        Timeout timeout  = new Timeout(Duration .create(30, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus, new GetApplicationList( supplierId,pageIndex,country,status,kcode,product), timeout);

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
    @RequestMapping(value = "/aapl", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String listAllApplications(@RequestBody Map<String, Object> payload){

        String supplierId = payload.get("si").toString();
        String kcode = payload.get("k").toString();
        String product = payload.get("p").toString();

        Timeout timeout  = new Timeout(Duration .create(30, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus, new GetAllApplicationList(supplierId,kcode,product), timeout);

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
    @RequestMapping(value = "/gac", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getAllComment(@RequestBody Map<String, Object> payload){

        System.out.println(payload);

        String p2mId =  payload.get("p2mId").toString();

        System.out.println(p2mId);

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus,
                        new GetAllComment(p2mId), timeout);

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


    @CrossOrigin
    @RequestMapping(value = "/gm", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getP2MMarketplaceInfo(@RequestBody Map<String, Object> payload){

        System.out.println(payload);

        String p2mId =  payload.get("p2mId").toString();

        System.out.println(p2mId);

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus,
                        new GetP2MMarketplaceInfo(p2mId), timeout);

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
    @RequestMapping(value = "/gp", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getP2MProductInfo(@RequestBody Map<String, Object> payload){

        System.out.println(payload);

        String p2mId =  payload.get("p2mId").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus,
                        new GetP2MProductInfo(p2mId), timeout);

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

    @CrossOrigin
    @RequestMapping(value = "/gi", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getP2MInsurance(@RequestBody Map<String, Object> payload){

        System.out.println(payload);

        String p2mId =  payload.get("p2mId").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus,
                        new GetP2MInsurance(p2mId), timeout);

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

    @CrossOrigin
    @RequestMapping(value = "/gr", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getP2MRegional(@RequestBody Map<String, Object> payload){

        System.out.println(payload);

        String p2mId =  payload.get("p2mId").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus,
                        new GetP2MRegional(p2mId), timeout);

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

    @CrossOrigin
    @RequestMapping(value = "/gs", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getP2MShipping(@RequestBody Map<String, Object> payload){

        System.out.println(payload);

        String p2mId =  payload.get("p2mId").toString();

        Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus,
                        new GetP2MShipping(p2mId), timeout);

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

    @CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/a", method = RequestMethod.POST)
    public String approve(@RequestBody Map<String, Object> payload){

        if(Context.getCurrentUser().isDrsUser()){

            UserInfo u =  Context.getCurrentUser();
            //Integer supplierId = (Integer) payload.get("si");
            String p2mAp = payload.get("p2mAp").toString();
            Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

            final Future<Object> futureResult =
                    Patterns.ask(drsCmdBus,
                            new ApproveP2MApplication(p2mAp,u.getUserId()), timeout);

            String jsonData = "";

            try {
                jsonData = (String) Await.result(futureResult, timeout.duration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            return "done server";
        }else{
            return "fail";

        }


    }

    @CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/r", method = RequestMethod.POST)
    public String reject(@RequestBody Map<String, Object> payload){

        if(Context.getCurrentUser().isDrsUser()){

            UserInfo u =  Context.getCurrentUser();
            //Integer supplierId = (Integer) payload.get("si");
            String p2mAp = payload.get("p2mAp").toString();

            Timeout timeout  = new Timeout(Duration.create(10, "seconds"));

            final Future<Object> futureResult =
                    Patterns.ask(drsCmdBus,
                            new RejectP2MApplication(p2mAp, u.getUserId()), timeout);

            String jsonData = "";

            try {
                jsonData = (String) Await.result(futureResult, timeout.duration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            return "done server";
        }else{
            return "fail";

        }


    }

    private String getRootFileDir() {
        return System.getProperty("catalina.home");
    }

    @CrossOrigin
    @RequestMapping(value="/upi",method= RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public @ResponseBody void uploadImage(MultipartHttpServletRequest request,
                                HttpServletResponse response ){

        String p2mName = request.getParameter("p2mName");
        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];

        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = request.getFile(itr.next());
        try {
            String originalFileName = mpf.getOriginalFilename();
            byte[] fileBytes = mpf.getBytes();

            String path = this.getRootFileDir() + "/supplier/" + kcode + "/" + p2mName +"/images" ;

            File fullPath = new File(path);
            if (!fullPath.exists()) { fullPath.mkdirs(); }

            FileUtils.writeByteArrayToFile(new File(path +"/" + originalFileName), fileBytes);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @CrossOrigin
    @RequestMapping(value="/upf",method= RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public @ResponseBody void uploadFile(MultipartHttpServletRequest request,
                                     HttpServletResponse response ){

        String p2mName = request.getParameter("p2mName");
        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];


        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = request.getFile(itr.next());
        try {
            String originalFileName = mpf.getOriginalFilename();
            byte[] fileBytes = mpf.getBytes();

            String path = this.getRootFileDir() + "/supplier/" + kcode +  "/" + p2mName +"/files" ;

            File fullPath = new File(path);
            if (!fullPath.exists()) { fullPath.mkdirs(); }
            
            FileUtils.writeByteArrayToFile(new File(path +"/" + originalFileName), fileBytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @CrossOrigin
    @RequestMapping(value = "/gtpn", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getTotalProductNumber(@RequestBody Map<String, Object> payload){

        String kcode =  payload.get("si").toString();


        final java.time.Duration t = java.time.Duration.ofSeconds(120);
        CompletionStage<String> stage
                = akka.pattern.Patterns.ask(drsQueryBus,  new GetTotalProductNumber(kcode), t).thenApply(
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
    @RequestMapping(value = "/gtsn", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getTotalSkuNumber(@RequestBody Map<String, Object> payload){


        String kcode =  payload.get("si").toString();

        final java.time.Duration t = java.time.Duration.ofSeconds(120);
        CompletionStage<String> stage
                = akka.pattern.Patterns.ask(drsQueryBus,  new GetTotalSkuNumber(kcode), t).thenApply(
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
    @RequestMapping(value = "/gtasn", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getTotalAppliedSkuNumber(@RequestBody Map<String, Object> payload){


        String kcode =  payload.get("si").toString();

        final java.time.Duration t = java.time.Duration.ofSeconds(120);
        CompletionStage<String> stage
                = akka.pattern.Patterns.ask(drsQueryBus,  new GetTotalAppliedSkuNumber(kcode), t).thenApply(
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
    @RequestMapping(value = "/gtossn", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getTotalOnSaleSkuNumber(@RequestBody Map<String, Object> payload){


        String kcode =  payload.get("si").toString();
        
        final java.time.Duration t = java.time.Duration.ofSeconds(120);
        CompletionStage<String> stage
                = akka.pattern.Patterns.ask(drsQueryBus,  new GetTotalOnSaleSkuNumber(kcode), t).thenApply(
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



}
