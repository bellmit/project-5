package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.message.AddProductSkuAsinUco.*;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.usecase.AddProductSkuAsinUco;
import com.kindminds.drs.core.RegisterCommandHandler;
import com.kindminds.drs.api.v1.model.product.SkuFnskuAsin;
import com.kindminds.drs.api.v1.model.product.SkuWithoutAsin;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

public class AddProductSkuAsinHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsCmdBus ) {

        return Props.create(AddProductSkuAsinHandler.class ,
                () -> new AddProductSkuAsinHandler(drsCmdBus) );
    }

    private final ActorRef drsCmdBus ;
    private final AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();
    private final String name = self().path().name();


    public AddProductSkuAsinHandler(ActorRef drsCmdBus ) {
        this.drsCmdBus = drsCmdBus;

        drsCmdBus.tell(new RegisterCommandHandler(name , GetMarketplaces.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , GetKcodeToSupplierName.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , GetSkuToAsin.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , GetMarketplaceToSku.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , AddFbaData.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , AddInventoryData.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , ToggleStorageFeeFlag.class.getName()
                , self()) , ActorRef.noSender());

    }

    @Override
    public Receive createReceive() {

        AddProductSkuAsinUco uco =
                springCtx.getBean(AddProductSkuAsinUco.class);

        return receiveBuilder()
                .match(GetMarketplaces.class, getM -> {
                    List<Marketplace> marketplaces = uco.getMarketplaces();
                    getSender().tell(marketplaces, getSelf());

                }).match(GetKcodeToSupplierName.class, getK ->{
                    Map<String,String> kCodeToSupplier = uco.getKcodeToSupplierName();
                    getSender().tell(kCodeToSupplier, getSelf());

                }).match(GetSkuToAsin.class, getS ->{
                    List<SkuFnskuAsin> skuToAsin = uco.getSkuToAsin(getS.marketplaceIdText(), getS.splrKcode());
                    getSender().tell(skuToAsin, getSelf());

                }).match(GetMarketplaceToSku.class, getMToS ->{
                    List<SkuWithoutAsin> skuWithoutAsins = uco.getMarketplaceToSku();
                    getSender().tell(skuWithoutAsins, getSelf());

                }).match(AddFbaData.class, addFba ->{
                    String result = uco.addFbaData(addFba.fileData(), addFba.marketplaceId());
                    getSender().tell(result, getSelf());

                }).match(AddInventoryData.class, addInv ->{
                    String result = uco.addInventoryData(addInv.fileData(), addInv.marketplaceId());
                    getSender().tell(result, getSelf());

                }).match(ToggleStorageFeeFlag.class, toggle ->{
                    uco.toggleStorageFeeFlag(toggle.marketplaceId(), toggle.marketplaceSku());

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
