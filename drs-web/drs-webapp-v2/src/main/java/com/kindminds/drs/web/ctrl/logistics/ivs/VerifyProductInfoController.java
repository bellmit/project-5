package com.kindminds.drs.web.ctrl.logistics.ivs;


import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Context;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.api.message.command.ivs.ConfirmIvsProductVerifyInfo;
import com.kindminds.drs.api.message.command.ivs.InitVerifyIvsProductInfo;
import com.kindminds.drs.api.message.command.ivs.RejectIvsProductVerifyInfo;
import com.kindminds.drs.api.message.query.ivs.*;
import com.kindminds.drs.api.message.query.marketing.GetMarketingActivityList;
import com.kindminds.drs.api.message.query.user.GetUserRoles;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;


@Controller
@RestController
@RequestMapping("/ivs/vp")
public class VerifyProductInfoController {

    ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();

    ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();

    //    check is AM or not
    @CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value = "/pld", method = RequestMethod.POST)
    public String payloadData(@RequestBody Map<String, Object> payload  , HttpServletRequest request )  {

        UserInfo u =  Context.getCurrentUser();
        String key = u.getUserId() + "_IvsVp";
        List s = (List) request.getSession().getAttribute(key);

        String jsonData = "{";

        if(s != null){

            List ivsVpList  = s ;

          jsonData+= "\"ivsName\": \""+ ivsVpList.get(0) + "\", \"dc\":\""+ ivsVpList.get(1) + "\" , " +
                  "\"lineSeq\": \""+ ivsVpList.get(2) + "\",";
            jsonData+= "\"boxNum\": \""+ ivsVpList.get(3) + "\", \"mixBoxLineSeq\": \""+ ivsVpList.get(4) + "\",";
            jsonData+= "\"sku\": \""+ ivsVpList.get(5) + "\", ";
            jsonData += "\"isLogistics\": "+ ivsVpList.get(6) +", \"vpStatus\": \"" + ivsVpList.get(7) + "\"" +
                    ", \"qty\": \"" + ivsVpList.get(8) + "\" }";

        }else{

            jsonData+= "\"ivsName\": \"\", \"dc\": \"\", \"lineSeq\": \"\",";
            jsonData+= "\"boxNum\": \"\", \"mixBoxLineSeq\": \"\",";
            jsonData+= "\"sku\": \"\", ";
            jsonData += "\"isLogistics\": false , \"vpStatus\": \"\", \"qty\": \"\" }";

        }



        /*var jsonData = "{"
        jsonData+= "\"ivsName\": \"IVS-K486-17\", \"lineSeq\": \"0\","
        jsonData+= "\"boxNum\": \"6\", \"mixBoxLineSeq\": \"0\","
        jsonData+= "\"sku\": \"K486-BAL\", "
        jsonData += "\"isLogistics\": true , \"vpStatus\": \"To be confirmed\" }"*/



        return jsonData;
    }

    // get all ivs list
    @CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value ="/ivs", method = RequestMethod.POST)
    public String getIvs(@RequestBody  Map<String, Object> payload )  {


        String kcode = payload.get("kcode").toString();

        Timeout timeout  = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus, new GetIvsNumbers(kcode), timeout);

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

    // get sku list
    @CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value ="/sku", method = RequestMethod.POST)
    public String getSku(@RequestBody Map<String, Object> payload  )  {

        String ivsNumber = payload.get("ivsNumber").toString();

        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus,new GetIvsLineitemSku(ivsNumber), timeout);

        String jsonData = "";

        try {
            jsonData = (String)Await.result(futureResult, timeout.duration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }



        //var jsonData = "[\"K598-ISE1-8-4T\", \"K598-ISE1-2-4T\"]";
        return jsonData;
    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value ="/sd",method = RequestMethod.POST)
    public String getShippingDate(@RequestBody Map<String, Object> payload )  {

        String ivsNumber = payload.get("ivsNumber").toString();

        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus, new GetExpectedShippingDate(ivsNumber), timeout);

        String jsonData = "";

        try {
            jsonData = (String)Await.result(futureResult, timeout.duration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        //var jsonData = "[\"K598-ISE1-8-4T\", \"K598-ISE1-2-4T\"]";
        return jsonData;
    }

    //    get shipment box list
    //@CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value ="/box" ,method = RequestMethod.POST)
    public String getBox(@RequestBody Map<String, Object> payload ){


        String sku = payload.get("sku").toString();
        String ivsNumber = payload.get("ivsNumber").toString();

        Timeout timeout  = new Timeout(Duration.create(120, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus, new GetBoxNumbers(ivsNumber, sku), timeout);

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

//    get shipment lineItem info
//@CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value ="/lineitem",method = RequestMethod.POST)
    public String getLineItem(@RequestBody Map<String, Object> payload ){

        String ivsNumber = payload.get("ivsNumber").toString();
        int boxNum = payload.get("boxNum") == null ? 0 : Integer.parseInt(payload.get("boxNum").toString());
        int mixBoxLineSeq = payload.get("mixLineSeq") == null ? 0 :Integer.parseInt(payload.get("mixLineSeq").toString());

        Timeout timeout = new Timeout(Duration.create(120, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus, new GetShipmentLineItem(ivsNumber, boxNum, mixBoxLineSeq), timeout);

        String jsonData = "";

        try {
            jsonData = (String)Await.result(futureResult, timeout.duration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }



//    var jsonData = "{";
//
//        jsonData += "\"outSideBoxSize\": \"47.5 x 33.5 x 20\" , " +
//                "\"singleBoxWeight\": \"7.000\", \"singleBoxQty\": \"10\" , \"boxQty\": \"3\", \"productQty\": \"30\" }"

        return jsonData;
    }

    //get sku data
    //@CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value = "/data",method = RequestMethod.POST ,
            produces = "application/json; charset=utf-8")
    public String getSkuData(@RequestBody Map<String, Object> payload ){

        String sku =  payload.get("sku").toString();
        String dc = payload.get("dc").toString();
        String ivs = payload.get("ivs").toString();

        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus, new GetIvsProductVerifyInfo(ivs,sku,dc), timeout);

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


    //@CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value ="/dr",method = RequestMethod.POST)
    public String getDocRequriement(@RequestBody Map<String, Object> payload ){

        String ivsName = payload.get("ivsName").toString();
        int boxNum = payload.get("boxNum") == null ? 0 : Integer.parseInt(payload.get("boxNum").toString());
        int mixLineSeq = payload.get("mixLineSeq") == null ? 0 :Integer.parseInt(payload.get("mixLineSeq").toString());

        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus, new GetIvsProdDocRequirement(ivsName , boxNum , mixLineSeq), timeout);

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

    //@CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value ="/vpst",method = RequestMethod.POST)
    public String getProductVerificationStatus(@RequestBody Map<String, Object> payload ){

        String ivsName = payload.get("ivsName").toString();
        int boxNum = payload.get("boxNum") == null ? 0 : Integer.parseInt(payload.get("boxNum").toString());
        int mixLineSeq = payload.get("mixLineSeq") == null ? 0 :Integer.parseInt(payload.get("mixLineSeq").toString());

        Timeout timeout = new Timeout(Duration.create(30, "seconds"));


        final Future<Object> futureResult =
                Patterns.ask(drsQueryBus, new GetIvsLineitemProdVerificationStatus(ivsName , boxNum , mixLineSeq), timeout);

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


    //  initially verify sku data
    //@CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value ="/init",method = RequestMethod.POST)
    public String initVerify(@RequestBody Map<String, Object> payload ){


        String ivsName = payload.get("ivsName").toString();
        int boxNum = payload.get("boxNum") == null ? 0 : Integer.parseInt(payload.get("boxNum").toString());
        int mixLineSeq = payload.get("mixLineSeq") == null ? 0 :Integer.parseInt(payload.get("mixLineSeq").toString());


        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> futureResult =
                Patterns.ask(drsCmdBus,
                        new InitVerifyIvsProductInfo(ivsName, boxNum, mixLineSeq), timeout);


        String jsonData = "You Verified Successfully!";

        return jsonData;
    }

    //    confirm sku data
    //@CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value ="/confirm",method = RequestMethod.POST)
    public String confirmSkuData(@RequestBody Map<String, Object> payload ){


        String ivsName = payload.get("ivsName").toString();
        int boxNum = payload.get("boxNum") == null ? 0 : Integer.parseInt(payload.get("boxNum").toString());
        int mixLineSeq = payload.get("mixLineSeq") == null ? 0 :Integer.parseInt(payload.get("mixLineSeq").toString());

        String docReqS = payload.get("docReqData").toString();

        ObjectMapper mapper = new ObjectMapper();
        try {
          List rList =  mapper.readValue(docReqS,List.class);
          Timeout timeout = new Timeout(Duration.create(30, "seconds"));

          String docReq = "{\"ivsName\": \""+ ivsName+"\", \"boxNum\": "+ boxNum +", \"mixedBoxLineSeq\": "+ mixLineSeq +"," +
                    " \"g3Invoice\": "+  ((Map<String , Boolean>)rList.get(0)).get("isChecked")  +  " , " +
                    "\"msds\":"+ ((Map<String , Boolean>)rList.get(1)).get("isChecked")  + ", " +
                    "\"dangerousGoods\":"+ ((Map<String , Boolean>)rList.get(2)).get("isChecked") +"," +
                    "\"un383\":"+ ((Map<String , Boolean>)rList.get(3)).get("isChecked") +"," +
                    "\"ppq505\":"+ ((Map<String , Boolean>)rList.get(4)).get("isChecked") +"," +
                    "\"usFDA\":"+ ((Map<String , Boolean>)rList.get(5)).get("isChecked") +"," +
                    "\"usMID\": "+ ((Map<String , Boolean>)rList.get(6)).get("isChecked") +" , " +
                    "\"fatContentSpecification\": "+ ((Map<String , Boolean>)rList.get(7)).get("isChecked") +"," +
                    " \"exportTariffRequirement\": " + ((Map<String , Boolean>)rList.get(8)).get("isChecked") + " ," +
                    " \"additionalRemarks\":false}";


            final Future<Object> futureResult =
                    Patterns.ask(drsCmdBus,
                           new  ConfirmIvsProductVerifyInfo(ivsName, boxNum, mixLineSeq , docReq), timeout);


        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonData = "You Confirmed Successfully!";
        return jsonData;
    }

    //  reject sku data
    //@CrossOrigin
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
    @RequestMapping(value ="/reject",method = RequestMethod.POST)
    public String rejectSkuData(@RequestBody Map<String, Object> payload ){


        String ivsName = payload.get("ivsName").toString();
        int boxNum = payload.get("boxNum") == null ? 0 : Integer.parseInt(payload.get("boxNum").toString());
        int mixLineSeq = payload.get("mixLineSeq") == null ? 0 :Integer.parseInt(payload.get("mixLineSeq").toString());

        String docReqS = payload.get("docReqData").toString();

        ObjectMapper mapper = new ObjectMapper();
        List rList = null;
        try {
            rList = mapper.readValue(docReqS, List.class);
            Timeout timeout = new Timeout(Duration.create(30, "seconds"));

            String docReq = "{\"ivsName\": \""+ ivsName +"\", \"boxNum\": " + boxNum+ ", \"mixedBoxLineSeq\": "+ mixLineSeq +"," +
                    " \"g3Invoice\": "+  ((Map<String , Boolean>)rList.get(0)).get("isChecked")  +  " , " +
                    "\"msds\":"+ ((Map<String , Boolean>)rList.get(1)).get("isChecked")  + ", " +
                    "\"dangerousGoods\":"+ ((Map<String , Boolean>)rList.get(2)).get("isChecked") +"," +
                    "\"un383\":"+ ((Map<String , Boolean>)rList.get(3)).get("isChecked") +"," +
                    "\"ppq505\":"+ ((Map<String , Boolean>)rList.get(4)).get("isChecked") +"," +
                    "\"usFDA\":"+ ((Map<String , Boolean>)rList.get(5)).get("isChecked") +"," +
                    "\"usMID\": "+ ((Map<String , Boolean>)rList.get(6)).get("isChecked") +" , " +
                    "\"fatContentSpecification\": "+ ((Map<String , Boolean>)rList.get(7)).get("isChecked") +"," +
                    " \"exportTariffRequirement\": " + ((Map<String , Boolean>)rList.get(8)).get("isChecked") + " ," +
                    " \"additionalRemarks\":false}";

            final Future<Object> futureResult =
                    Patterns.ask(drsCmdBus,
                           new  RejectIvsProductVerifyInfo(ivsName, boxNum, mixLineSeq , docReq), timeout);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonData = "You rejected Successfully!";
        return jsonData;
    }

    //create marketing activity
    //@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
//    @CrossOrigin
//    @RequestMapping("/confirm", method = arrayOf(RequestMethod.POST))
//    open fun confirmProductINfo(@RequestBody productInfo : String) : String {
//        println(productInfo)
//        var jsonData = "{\"isOk\":1 }";
//        return jsonData;
//    }
}
