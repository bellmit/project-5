package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.message.emailReminderUco.*;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.EmailReminderUco;
import com.kindminds.drs.core.RegisterCommandHandler;
import com.kindminds.drs.api.v1.model.accounting.SupplierLongTermStorageFee;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

public class EmailReminderHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsCmdBus ) {

        return Props.create(EmailReminderHandler.class ,
                () -> new EmailReminderHandler(drsCmdBus ) );
    }

    private final AnnotationConfigApplicationContext springCtx =  BizCoreCtx.get() ;
    private final ActorRef drsCmdBus ;

    private final String name = self().path().name();


    public EmailReminderHandler(ActorRef drsCmdBus ) {
        this.drsCmdBus = drsCmdBus;


        drsCmdBus.tell(new RegisterCommandHandler(name , GetIncludedSuppliers.class.getName()
                        , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , GetExcludedSuppliers.class.getName()
                        , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , UpdateLongTermStorageReminder.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , GetSuppliersOverFeeLimit.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , GetFeeToSendReminder.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , UpdateFeeToSendReminder.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , SendLTSFReminderToCurrentUser.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , EmailReminderUco.class.getName()
                , self()) , ActorRef.noSender());

    }

    @Override
    public Receive createReceive() {

        EmailReminderUco uco =
                (EmailReminderUco)springCtx.getBean(EmailReminderUco.class);

        return receiveBuilder()
                .match(GetIncludedSuppliers.class, gis -> {
                    Map<String, String> includedSuppliers = uco.getIncludedSuppliers();
                    getSender().tell(includedSuppliers, getSelf());

                }).match(GetExcludedSuppliers.class, ges ->{
                    Map<String, String> excludedSuppliers = uco.getExcludedSuppliers();
                    getSender().tell(excludedSuppliers, getSelf());

                }).match(UpdateLongTermStorageReminder.class, updateLTSR ->{
                    int updated = uco.updateLongTermStorageReminder(updateLTSR.kCodes());
                    getSender().tell(updated, getSelf());

                }).match(GetSuppliersOverFeeLimit.class, suppliers ->{
                    List<SupplierLongTermStorageFee> suppliersOverFeeLimit
                            = uco.getSuppliersOverFeeLimit(suppliers.kCodes());
                    getSender().tell(suppliersOverFeeLimit, getSelf());

                }).match(GetFeeToSendReminder.class, fee ->{
                    Double feeToSendReminder = uco.getFeeToSendReminder();
                    getSender().tell(feeToSendReminder, getSelf());

                }).match(UpdateFeeToSendReminder.class, updateFee ->{
                    int updated = uco.updateFeeToSendReminder(updateFee.limit());
                    getSender().tell(updated, getSelf());

                }).match(SendLTSFReminderToCurrentUser.class, sendLTSFR -> {
                    uco.sendLTSFReminderToCurrentUser(sendLTSFR.currentUserID());

                }).match(AutomateSendLTSFReminder.class, automateSendLTSFReminder -> {
                    uco.automateSendLTSFReminder();
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
