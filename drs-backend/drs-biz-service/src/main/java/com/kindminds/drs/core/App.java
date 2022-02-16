package com.kindminds.drs.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.actor.Props;

import akka.routing.RoundRobinPool;
import com.kindminds.drs.api.message.command.CalculateOriginalAccountsReceivable;
import com.kindminds.drs.api.message.command.GenerateAccountsReceivableAgingReport;
import com.kindminds.drs.api.message.command.SendBuyBoxReminder;
import com.kindminds.drs.api.message.emailReminderUco.AutomateSendLTSFReminder;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.core.actors.handlers.command.accounting.SettlementCmdHandler;

import com.kindminds.drs.core.actors.handlers.command.accounting.SettlementWorker;
import com.kindminds.drs.core.actors.handlers.command.customercare.CustomercareCmdHandler;
import com.kindminds.drs.core.actors.handlers.command.logistics.IvsHandler;
import com.kindminds.drs.core.actors.handlers.command.logistics.VerifyIvsProductInfoHandler;
import com.kindminds.drs.core.actors.handlers.command.marketing.MarketingActivityCmdHandler;
import com.kindminds.drs.core.actors.handlers.command.mws.FeedsHandler;
import com.kindminds.drs.core.actors.handlers.command.p2m.P2MCmdHandler;
import com.kindminds.drs.core.actors.handlers.command.process.UserTaskCmdHandler;
import com.kindminds.drs.core.actors.handlers.command.productCategory.ProductCategoryCmdHandler;
import com.kindminds.drs.core.actors.handlers.query.product.ManageProductQueryHandler;
import com.kindminds.drs.core.actors.handlers.command.sales.QuoteServiceFeeHandler;
import com.kindminds.drs.core.actors.handlers.event.accounting.SettlementSubscriber;
import com.kindminds.drs.core.actors.handlers.event.customercare.CaseSubscriber;
import com.kindminds.drs.core.actors.handlers.event.p2m.P2MApplicationSubscriber;
import com.kindminds.drs.core.actors.handlers.event.product.*;
import com.kindminds.drs.core.actors.handlers.command.CalculateMonthlyStorageFeeHandler;
import com.kindminds.drs.core.actors.handlers.command.ImportAmazonDateRangeReportHandler;
import com.kindminds.drs.core.actors.handlers.command.product.*;
import com.kindminds.drs.core.actors.handlers.event.sales.QuoteRequestSubscriber;

import com.kindminds.drs.core.actors.handlers.query.ViewAmazonSponsoredBrandsCampaignReportHandler;

import com.kindminds.drs.api.message.confirmMarketingMaterialUco.AutoRenotifySuppliers;
import com.kindminds.drs.core.actors.handlers.command.*;
import com.kindminds.drs.core.actors.handlers.query.*;
import com.kindminds.drs.core.actors.handlers.query.common.SettlementPeriodListHandler;

import com.kindminds.drs.core.actors.handlers.query.customer.CustomerQueryHandler;
import com.kindminds.drs.core.actors.handlers.query.logistics.IvsProductVerifyInfoQueryHandler;
import com.kindminds.drs.core.actors.handlers.query.marketing.MarketingActivityQueryHandler;
import com.kindminds.drs.core.actors.handlers.query.p2m.P2MQueryHandler;
import com.kindminds.drs.core.actors.handlers.query.productCategory.ProductCategoryQueryHandler;
import com.kindminds.drs.core.actors.handlers.query.product.*;
import com.kindminds.drs.core.actors.handlers.query.supplier.SupplierViewHandler;
import com.kindminds.drs.core.actors.handlers.query.user.UserQueryHandler;
import com.kindminds.drs.core.schedule.ProfitShareEmailReminderHandler;


import com.kindminds.drs.core.actors.handlers.query.ViewSs2spStatementHandler;

import com.kindminds.drs.persist.data.access.nosql.mongo.DrsMongoClient;
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension;


public class App 
{

    public static void main( String[] args ) throws Exception {

       BizCoreCtx.get();
       //todo arthur need refactor it.
       DrsMongoClient.getInstance();

        Thread.sleep(5000);


        ExtendedActorSystem system = (ExtendedActorSystem)ActorSystem.create("drs");


        ActorRef drsCmdBus = system.actorOf(Props.create(DrsCmdBus.class),
                "DrsCmdBus");


        ActorRef drsQueryBus = system.actorOf(Props.create(DrsQueryBus.class),
                "DrsQueryBus");

        DrsEventBus drsEventBus = new DrsEventBus();

        //Command Handler

        system.actorOf(AddProductSkuAsinHandler.props(drsCmdBus), "AddProductSkuAsinHandler");

        system.actorOf(ManageFbaInventoryImporterHandler.props(drsCmdBus), "ManageFbaInventoryImporterHandler");

        system.actorOf(CalculateMonthlyStorageFeeHandler.props(drsCmdBus), "CalculateMonthlyStorageFeeHandler");



        system.actorOf(CalculateShippingCostHandler.props(drsCmdBus),
                "CalculateShippingCostHandler");

        system.actorOf(ImportAmazonDateRangeReportHandler.props(drsCmdBus),
                "ImportAmazonDateRangeReportHandler");


        system.actorOf(com.kindminds.drs.core.actors.handlers.command.ProcessCouponRedemptionHandler.props(drsCmdBus),
                "ProcessCouponRedemptionHandlerCmd");


        ActorRef applyOnboardingLineitemHandler = system.actorOf(ApplyOnboardingLineitemHandler.props(drsCmdBus ,drsEventBus),
                "ApplyOnboardingLineitemHandler");

        ActorRef productHandler = system.actorOf(ProductHandler.props(drsCmdBus ,drsEventBus),
                "ProductHandler");

        system.actorOf(RequestEditingMarketingMaterialHandler.props(drsCmdBus ,drsEventBus),
                "RequestEditingMarketingMaterialHandler");

        system.actorOf(ProductMarketingMaterialHandler.props(drsCmdBus ,drsEventBus),
                "ProductMarketingMaterialHandler");

        system.actorOf(ProcessAdSpendTransactionHandler.props(drsCmdBus),
                "ProcessAdSpendTransactionHandler");

        system.actorOf(TransformProductHandler.props(drsCmdBus),
                "TransformProductHandler");

        system.actorOf(QuoteServiceFeeHandler.props(drsCmdBus, drsEventBus),
                "QuoteServiceFeeHandler");

        ActorRef emailReminderActor = system.actorOf(EmailReminderHandler.props(drsCmdBus),
                "EmailReminderHandler");


        ActorRef marketingCmdHandler  =  system.actorOf(MarketingActivityCmdHandler.props(drsCmdBus),
                "MarketingActivityCmdHandler");

       system.actorOf(CustomercareCmdHandler.props(drsCmdBus , drsEventBus),
                "CustomercareCmdHandler");

        system.actorOf(new RoundRobinPool(5).props(SettlementCmdHandler.props(drsCmdBus, drsEventBus)), "settlementRouter");

         system.actorOf(SettlementWorker.props(drsCmdBus , drsEventBus), "settlementWorker");



        ActorRef accountsReceivableAgingHandler = system.actorOf(AccountsReceivableAgingHandler.props(drsCmdBus),
                "AccountsReceivableAgingHandler");

        ActorRef buyBoxReminderHandler = system.actorOf(BuyBoxReminderHandler.props(drsCmdBus),
                "BuyBoxReminderHandler");


        system.actorOf(IvsHandler.props(drsCmdBus), "IvsHandler");

        system.actorOf(VerifyIvsProductInfoHandler.props(drsCmdBus), "VerifyIvsProductInfoHandler");

        system.actorOf(ManageProductCmdHandler.props(drsCmdBus , drsEventBus), "ManageProductCmdHandler");

        system.actorOf(P2MCmdHandler.props(drsCmdBus , drsEventBus), "P2MCmdHandler");

        system.actorOf(ProductCategoryCmdHandler.props(drsCmdBus), "ProductCategoryCmdHandler");

        ActorRef fh = system.actorOf(FeedsHandler.props(drsCmdBus), "FeedCmdHandler");

        system.actorOf(UserTaskCmdHandler.props(drsCmdBus), "UserTaskCmdHandler");

        system.actorOf(NotificationCmdHandler.props(drsCmdBus), "NotificationCmdHandler");



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

        drsEventBus.subscribe(system.actorOf(QuoteRequestSubscriber.props(drsCmdBus) ,
                "QuoteRequestSubscriber" ) ,
                com.kindminds.drs.api.message.event.Events.QuoteRequest());


        drsEventBus.subscribe(system.actorOf(CaseSubscriber.props() ,
                "CaseSubscriber" ) , com.kindminds.drs.api.message.event.Events.CustomercareCase());

        drsEventBus.subscribe(system.actorOf(SettlementSubscriber.props(drsCmdBus) ,
                "SettlementSubscriber" ) , com.kindminds.drs.api.message.event.Events.Settlement());

        drsEventBus.subscribe(system.actorOf(P2MApplicationSubscriber.props(drsCmdBus) ,
                "P2MApplicationSubscriber" ) , com.kindminds.drs.api.message.event.Events.P2MApplication());



        //Query Handlers

        ActorRef viewKeyProductStatsHandler  = system.actorOf(ViewKeyProductStatsHandler.props(drsQueryBus),
                "ViewKeyProductStatsHandler");

        system.actorOf(ViewHomePageHandler.props(drsQueryBus),
                "ViewHomePageHandler");


         system.actorOf(ViewSs2spStatementHandler.props(drsQueryBus),
                 "ViewSs2spStatementHandler");

         system.actorOf(ViewAmazonSponsoredBrandsCampaignReportHandler.props(drsQueryBus),
                "ViewAmazonSponsoredBrandsCampaignReportHandler");

         system.actorOf(SettlementPeriodListHandler.props(drsQueryBus),
                "SettlementPeriodListHandler");

        system.actorOf(ProductViewHandler.props(drsQueryBus),
                "ProductViewHandler");


        system.actorOf(com.kindminds.drs.core.actors.handlers.query.ProcessCouponRedemptionHandler.props(drsQueryBus ),
                "ProcessCouponRedemptionHandlerQry");

        system.actorOf(ViewCampaignReportDetailHandler.props(drsQueryBus),
                "ViewCampaignReportDetailHandler");

        system.actorOf(ViewHsaCampaignReportDetailHandler.props(drsQueryBus),
                "ViewHsaCampaignReportDetailHandler");

        system.actorOf(ViewExternalMarketingActivityHandler.props(drsQueryBus),
                "ViewExternalMarketingActivityHandler");


        system.actorOf(OnboardingApplicationViewHandler.props(drsQueryBus),
                "OnboardingApplicationViewHandler");

        system.actorOf(ViewAccountsReceivableAgingHandler.props(drsQueryBus),
                "ViewAccountsReceivableAgingHandler");

        system.actorOf(ProductDashboardViewHandler.props(drsQueryBus),
                "productDashboardViewHandler$");

        system.actorOf(SupplierViewHandler.props(drsQueryBus),
                "supplierViewHandler");

        system.actorOf(CustomerQueryHandler.props(drsQueryBus),
                "customerViewHandler");

        system.actorOf(IvsProductVerifyInfoQueryHandler.props(drsQueryBus),
                "ivsProductVerifyInfoViewHandler");

        system.actorOf(ManageProductQueryHandler.props(drsQueryBus),
                "productMongoHandler");

        system.actorOf(UserQueryHandler.props(drsQueryBus),
                "userQueryHandler");

        //Schedule Handlers

        system.actorOf(ProductMarketingMaterialViewHandler.props(drsQueryBus),
                "ProductMarketingMaterialViewHandler");


        system.actorOf(OnboardingApplicationLineitemViewHandler.props(drsQueryBus),
                "OnboardingApplicationLineitemViewHandler");

        ActorRef vm  =  system.actorOf(MarketingActivityQueryHandler.props(drsQueryBus),
                "MarketingActivityViewHandler");

        //Schedule Handlers

        ActorRef vkpHandler = system.actorOf(ViewKeyProductStatusHandler.props(drsCmdBus),
                "ViewKeyProductStatusCmdHandler");

        ActorRef confirmMarketingMaterialActor = system.actorOf(ConfirmMarketingMaterialHandler.props(drsCmdBus),
                "ConfirmMarketingMaterialHandler");

        system.actorOf(P2MQueryHandler.props(drsQueryBus),
                "P2MQueryHandler");

       system.actorOf(ManageProductQueryHandler.props(drsQueryBus),
             "ManageProductQueryHandler");

        system.actorOf(ProductCategoryQueryHandler.props(drsQueryBus),
                "ProductCategoryQueryHandler");


        system.actorOf(NotificationQueryHandler.props(drsQueryBus),
             "NotificationQueryHandler");




        if(args.length > 0){
            if(args[0] == "prod"){

                QuartzSchedulerExtension quartzSchedulerExtension = new QuartzSchedulerExtension(system);

                ActorRef pserActor = system.actorOf(ProfitShareEmailReminderHandler.props(),
                        "ProfitShareEmailReminderHandler");

                quartzSchedulerExtension.schedule("cronProfitShareEmailReminder", pserActor, "");

                quartzSchedulerExtension.schedule("cronLongTermStorageFeeEmailReminder",
                        emailReminderActor, new AutomateSendLTSFReminder());


                //quartzSchedulerExtension.schedule("keyProductStatus", vkpHandler, new TransformData());


                quartzSchedulerExtension.schedule("cronConfirmMarketingMaterialReminder",
                        confirmMarketingMaterialActor, new AutoRenotifySuppliers());


                quartzSchedulerExtension.schedule("cronGenerateAccountsReceivableAging",
                        accountsReceivableAgingHandler, new GenerateAccountsReceivableAgingReport());

                quartzSchedulerExtension.schedule("cronBuyBoxEmailReminder",
                        buyBoxReminderHandler, new SendBuyBoxReminder());


            }


        }


        Thread.sleep(3000);

        //fh ! SubmitFeed("C:/Users/HyperionFive/Desktop/user/TrivetsXMLTest.xml", Marketplace.AMAZON_COM, MwsFeedType.Product_Feed)
        // fh.tell(new SubmitFeed("", Marketplace.AMAZON_COM, MwsFeedType.Product_Feed) , ActorRef.noSender());

        // fh.tell(new RequestFeedSubmissionListByCount(Marketplace.AMAZON_COM, 10 ,null ) , ActorRef.noSender());
        //  fh.tell(new RequestFeedSubmissionList(Marketplace.AMAZON_COM, null ) , ActorRef.noSender());




        // fh.tell(new RequestFeedSubmissionResult(Marketplace.AMAZON_COM, "293052018698" ) , ActorRef.noSender());
        //fh ! RequestFeedSubmissionResult(Marketplace.AMAZON_COM, "243695018570")


        //todo arthur
       // amzOrderHandlder.tell(new RequestAmazonOrder(Country.NA) , ActorRef.noSender());

       // amzFOrderHandlder.tell(new RequestAmazonFulfillmentOrder("UK") ,ActorRef.noSender());

        /*
        QuartzSchedulerExtension quartzSchedulerExtension = new QuartzSchedulerExtension(system);
        quartzSchedulerExtension.schedule("cronReqAmzOrderNA",
                amzOrderHandlder, new RequestAmazonOrder(Country.NA));


        quartzSchedulerExtension.schedule("cronReqAmzOrderEU",
                amzOrderHandlder, new RequestAmazonOrder(Country.EU));
        */


        // UpdateFbaSellableQuantityUco uco = (UpdateFbaSellableQuantityUco) BizCoreCtx.get().getBean("UpdateFbaSellableQuantityUco");
       // ((UpdateFbaSellableQuantityUcoImpl)uco).setMarketplace("Amazon.co.uk");
       // uco.update();

        // drsCmdBus.tell(new RequestReport("") , ActorRef.noSender());

       // drsQueryBus.tell(new GetRequestList(), ActorRef.noSender());

       // drsCmdBus.tell(new DownloadReport("") , ActorRef.noSender());


        //drsQueryBus.tell(new GetIvsProductVerifyInfo(), ActorRef.noSender());


       // drsQueryBus.tell(new GetIvsNumbers("K486"), ActorRef.noSender());

       // drsQueryBus.tell(new GetIvsLineitemSku("IVS-K486-7"), ActorRef.noSender());

        //drsQueryBus.tell(new GetBoxNumbers("IVS-K486-7","K486-SPB"), ActorRef.noSender());

        //drsCmdBus.tell(new InitVerifyIvsProductInfo("IVS-K486-16" , 2, 0) , ActorRef.noSender());

       // drsCmdBus.tell(new ConfirmIvsProductVerifyInfo("IVS-K486-16" , 1, 0) , ActorRef.noSender());

        //drsCmdBus.tell(new RejectIvsProductVerifyInfo("IVS-K486-16" , 2, 0) , ActorRef.noSender());

        //drsQueryBus.tell(new GetIvsNumbers("K486"), ActorRef.noSender());

        //drsQueryBus.tell(new GetIvsLineitemSku("IVS-K486-7"), ActorRef.noSender());


       // drsQueryBus.tell(new GetIvsProdDocRequirement("IVS-K486-16",1 ,
                       // 0), ActorRef.noSender());

        // drsQueryBus.tell(new GetIvsProductVerifyInfo("IVS-K510-33","K510-85U09001R0","US"), ActorRef.noSender());


        //accountsReceivableAgingHandler.tell(new CalculateOriginalAccountsReceivable() , ActorRef.noSender());

       //accountsReceivableAgingHandler.tell(new GenerateAccountsReceivableAgingReport() , ActorRef.noSender());


        // productHandler.tell(new
         //       TransformProduct("79df71b0-c9e4-4174-9f71-b0c9e48174da") , ActorRef.noSender());


    }


}
