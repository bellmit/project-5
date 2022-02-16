package com.kindminds.drs.web.ctrl;


import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;

import akka.pattern.Patterns;
import akka.util.Timeout;
import com.kindminds.drs.Context;
import com.kindminds.drs.api.message.command.manageProduct.DeleteBaseProduct;
import com.kindminds.drs.api.message.command.notification.*;
import com.kindminds.drs.api.message.query.notification.GetNotifications;
import com.kindminds.drs.web.config.DrsActorSystem;
import com.sun.nio.sctp.SendFailedNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

@RestController
@Controller
@CrossOrigin("*")
public class NotificationController {


    private ExecutorService nonBlockingService = Executors
            .newCachedThreadPool();

    private ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();

    ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();

    @Autowired
    private EmitterService emitterService;

    @GetMapping("/subscription/{userid}")
    public SseEmitter subsribe(@PathVariable int userid) {
        //log.info("subscribing...");

        SseEmitter sseEmitter = new SseEmitter(24 * 60 * 60 * 1000l);
        emitterService.addEmitter(sseEmitter , userid);

        //ActorSelection jobManagerSel = DrsActorSystem.actorSystem.actorSelection(
          //      ActorPath.fromString("akka://drs@localhost:5002/user/NotificationHandler"));

       // jobManagerSel.tell(new SendUnreadNotificationCount("1") , ActorRef.noSender());

        //log.info("subscribed");

        return sseEmitter;
    }



    @PostMapping("/notification/{username}")
    public ResponseEntity<?> send(@PathVariable String usernam){ //@RequestBody NotificationRequest request) {

       // emitterService.pushNotification(username, request.getFrom(), request.getMessage());
        return ResponseEntity.ok().body("message pushed to user " + usernam);

    }


    //@CrossOrigin
    //@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/c/n", method = RequestMethod.POST)
    public String getNotifications(@RequestBody  Map<String, Object> payload )  {

        Integer pi = (Integer)payload.get("pi");

        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> notifications = Patterns.ask(drsQueryBus,
                new GetNotifications(Context.getCurrentUser().getUserId(),pi), timeout);

        String jsonData = "";

        try {
            jsonData = (String)Await.result(notifications, timeout.duration()) ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        return jsonData;

    }


    //@CrossOrigin
    //@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/c/mnr", method = RequestMethod.POST)
    public String markNotificationAsRead(@RequestBody  Map<String, Object> payload )  {

        String id = payload.get("id").toString();
        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> futureResult = Patterns.ask(drsCmdBus,
                new MarkNotificationAsRead(id), timeout);


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

    //@CrossOrigin
    //@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/c/mnur", method = RequestMethod.POST)
    public String markNotificationAsUnRead(@RequestBody  Map<String, Object> payload )  {

        String id = payload.get("id").toString();
        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> futureResult = Patterns.ask(drsCmdBus,
                new MarkNotificationAsUnRead(id), timeout);


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


    //@CrossOrigin
    //@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/c/manr", method = RequestMethod.POST)
    public String markAllNotificationAsRead(@RequestBody  Map<String, Object> payload )  {


        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> futureResult = Patterns.ask(drsCmdBus,
                new MarkAllNotificationsAsRead(Context.getCurrentUser().getUserId()), timeout);


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


    //@CrossOrigin
    //@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")
    @RequestMapping(value = "/c/dn", method = RequestMethod.POST)
    public String deleteNotification(@RequestBody  Map<String, Object> payload )  {

        String id = payload.get("id").toString();
        Timeout timeout = new Timeout(Duration.create(30, "seconds"));

        final Future<Object> futureResult = Patterns.ask(drsCmdBus,
                new DeleteNotification(id), timeout);

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





}
