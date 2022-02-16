package com.kindminds.drs.core.migration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Country;

import com.kindminds.drs.api.data.transfer.productV2.LegacyProduct;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.api.message.command.manageProduct.MigrateLegacyDraftProductToProductV2;

import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.api.data.access.rdb.product.LegacyProductDao;
import com.kindminds.drs.api.data.access.usecase.product.MaintainProductOnboardingDao;
import com.kindminds.drs.api.usecase.product.LegacyProductUco;
import com.kindminds.drs.core.DrsCmdBus;

import com.kindminds.drs.core.DrsEventBus;
import com.kindminds.drs.core.actors.handlers.event.product.*;
import com.kindminds.drs.core.actors.handlers.command.product.ApplyOnboardingHandler;

import com.kindminds.drs.core.actors.handlers.command.product.MigrateLegacyDraftProductHandler;
import com.kindminds.drs.core.actors.handlers.command.product.ProductHandler;

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao;
import com.kindminds.drs.api.data.cqrs.store.read.queries.MigrateDraftProductQueries;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


import java.util.List;

import static akka.pattern.Patterns.ask;

public class MigrateProductDataService {

    private ActorRef drsCmdBus;
    private AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();

    private MaintainProductOnboardingDao dao;
    private ProductDao productDao;
    private LegacyProductUco legacyProductUco;
    private LegacyProductDao legacyProductDao;


    private MigrateDraftProductQueries migrateDraftProductQueries;

    public MigrateProductDataService(ActorRef drsCmdBus) {
        this.drsCmdBus = drsCmdBus;

        dao = springCtx.getBean(MaintainProductOnboardingDao.class);
        productDao = springCtx.getBean(ProductDao.class);
        legacyProductUco = springCtx.getBean(LegacyProductUco.class);
        legacyProductDao = springCtx.getBean(LegacyProductDao.class);
        migrateDraftProductQueries = springCtx.getBean(MigrateDraftProductQueries.class);

    }

    public static void main(String[] args) {

        ExtendedActorSystem system = (ExtendedActorSystem) ActorSystem.create("drs");

        LoggingAdapter log = Logging.getLogger(system, MigrateProductDataService.class);


        ActorRef drsCmdBus = system.actorOf(Props.create(DrsCmdBus.class),
                "DrsCmdBus");

        DrsEventBus drsEventBus = new DrsEventBus();

        //Event
        drsEventBus.subscribe(system.actorOf(OnboardingApplicationSubscriber.props() ,
                "OnboardingApplicationSubscriber" ) , com.kindminds.drs.api.message.event.Events.OnboardingApplication());

        drsEventBus.subscribe(system.actorOf(OnboardingApplicationLineitemSubscriber.props() ,
                "OnboardingApplicationLineitemSubscriber" ) ,
                com.kindminds.drs.api.message.event.Events.OnboardingApplicationLineitem());


        drsEventBus.subscribe(system.actorOf(ProductSubscriber.props() ,
                "ProductSubscriber" ) , com.kindminds.drs.api.message.event.Events.Product());

        drsEventBus.subscribe(system.actorOf(EditingMarketingMaterialRequestSubscriber.props() ,
                "EditingMarketingMaterialRequestSubscriber" ) ,
                com.kindminds.drs.api.message.event.Events.MarketingMaterialEditingRequest());

        drsEventBus.subscribe(system.actorOf(ProductMarketingMaterialSubscriber.props() ,
                "ProductMarketingMaterialSubscriber" ) ,
                com.kindminds.drs.api.message.event.Events.ProductMarketingMaterial());

        //Command

        system.actorOf(ProductHandler.props(drsCmdBus ,drsEventBus),
                "ProductHandler");

        system.actorOf(ApplyOnboardingHandler.props(drsCmdBus ,drsEventBus),
                "ApplyOnboardingHandler");

        system.actorOf(MigrateLegacyDraftProductHandler.props(drsCmdBus ,drsEventBus),
                "MigrateLegacyDraftProductHandler");



        MigrateProductDataService mpds = new MigrateProductDataService(drsCmdBus);
        try {
            mpds.migrateData();
            mpds.doMpProdProd();
            // System.exit(0);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /*
    public void test(){

        List<Product> products = getBaseProductsOnboarding();
        ObjectMapper mapper = new ObjectMapper();
        for(Product product : products) {
            LegacyProduct bpod = getProductInfo(product.getProductBaseCode());

            try {
                //System.out.//println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                //System.out.//println( mapper.writeValueAsString(bpod.getProductInfoMarketSide()));
                //System.out.//println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }



    }*/

    public void migrateData() throws JsonProcessingException, InterruptedException {

        List<ProductDto> products = getBaseProductsOnboarding();


        Timeout timeout = new Timeout(Duration.create(10, "seconds"));

        for(ProductDto product : products) {

            //BP-K530-GBA-21012NRX62E-HFX BP-K611-W09
            LegacyProduct bpod = getProductInfo(product.getProductBaseCode());
            //LegacyProduct bpod = getProductInfo("BP-K611-W09");
            // OnboardingApplication bpod = getProductInfo("BP-K530-GBA-21012NRX62E-HFX");

            ////System.out.//println(bpod.getProductInfoSource().getData());


            ObjectMapper mapper = new ObjectMapper();



            final Future<Object> futureResult =
                    ask(drsCmdBus,
                            new MigrateLegacyDraftProductToProductV2(
                                    true,product.getSupplierKcode(),bpod
                                    // true ,"K611",bpod
                                    //bpod.getProductInfoSource().getJsonData(),
                                    //mapper.writeValueAsString(bpod.getProductInfoMarketSide()),
                                    //mapper.writeValueAsString(bpod.getProductMarketingMaterialSource()),
                                    //mapper.writeValueAsString(bpod.getProductMarketingMaterialMarketSide()),
                                    ),
                            timeout);


            try {
                String str  = (String)  Await.result(futureResult, timeout.duration());
                //System.out.//println(str);


                productDao.getId(bpod.getProductBaseCode(), Country.CORE).ifPresent(
                        mpId -> {
                            saveLegacyDraftRef(mpId, 0, bpod.getProductBaseCode());
                        } );


            } catch (Exception e) {
                e.printStackTrace();
            }



        }

    }




    private void doMpProdProd(){

        List<ProductDto> products = getBaseProductsOnboarding();


        for(ProductDto product : products) {


            LegacyProduct bpod = getProductInfo(product.getProductBaseCode());


            productDao.getId(bpod.getProductBaseCode(), Country.CORE).ifPresent(
                    mpId -> {
                        legacyProductDao.getSettlementProductId(bpod.getProductBaseCode()).ifPresent(
                                pId ->{
                                    legacyProductUco.createProductRef(mpId, 0 ,pId);
                                }
                        );
                    } );


        }


    }

    private List<ProductDto> getBaseProductsOnboarding() {
        int count = dao.queryBaseProductOnboardingCount();
        return dao.queryBaseProductOnboardingList(1, count);
    }

    private LegacyProduct getProductInfo(String productBaseCode) {
        return migrateDraftProductQueries.getBaseProductOnboardingApplication(productBaseCode);
    }

    private void saveLegacyDraftRef(String mpProductId, int mpMarketSide, String productBaseCode) {
        legacyProductUco.createDraftProductRef(mpProductId, mpMarketSide, productBaseCode);
    }
}