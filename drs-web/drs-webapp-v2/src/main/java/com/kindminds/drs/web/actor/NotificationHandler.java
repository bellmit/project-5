package com.kindminds.drs.web.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.message.command.notification.SendNotificationToWebFE;
import com.kindminds.drs.api.message.command.notification.SendUnreadNotificationCount;
import com.kindminds.drs.api.message.util.MailMessage;
import com.kindminds.drs.api.message.util.MailMessageWithBCC;

import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.web.ctrl.EmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

public class NotificationHandler extends AbstractActor {


    private EmitterService emitterService = SpringAppCtx.get().getBean(EmitterService.class);

    //todo arhtur how to init this actor where ?

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final String name = self().path().name();

    public static Props props( ) {

        return Props.create(NotificationHandler.class ,
                () -> new NotificationHandler() );
    }

    public NotificationHandler() {
    }

    @Override
    public Receive createReceive() {
        
        return receiveBuilder()
                .match( SendNotificationToWebFE.class, snwf -> {

                    Thread.sleep(15000);

                    // String message = "{\"update\":\"70 new employees are shifted\",\"timestamp\":1616566371815}";
                    //Thread.sleep(30000);
                    System.out.println(snwf.content());
                    System.out.println("receivedreceivedreceivedreceivedreceivedreceived");
                    // System.out.println(ur.sn());

                    //todo arthur event name
                    emitterService.pushNotification(snwf.userId(),"test" , snwf.content());
                })
                .match( SendUnreadNotificationCount.class, ur -> {

                    Thread.sleep(15000);

                   // String message = "{\"update\":\"70 new employees are shifted\",\"timestamp\":1616566371815}";
//                    Thread.sleep(30000);
                    System.out.println();
                    //String message = "{\"update\":\"70 new employees are shifted\",\"timestamp\":1616566371815}";
                    System.out.println("receivedreceivedreceivedreceivedreceivedreceived");
                   // System.out.println(ur.sn());

                    //emitterService.pushNotification(1,"test" , message);

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }


}
