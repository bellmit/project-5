package com.kindminds.drs.rt;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.rt.Start;
import com.kindminds.drs.rt.DrsCmdBus;
import com.kindminds.drs.rt.DrsQueryBus;
import com.kindminds.drs.rt.Worker;

import com.kindminds.drs.service.util.DrsBizCoreAnnotationConfig4DP;
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

import static akka.pattern.Patterns.ask;

public class App {

    ActorRef drsQueryBus;
    ActorRef drsCmdBus;

    Random rand = new Random();

    static ArrayList<Thread> myThreads;

    private static final String SOFTWARE_ENG_EMAIL = "software.engineering@drs.network";


    /*
    public App(ActorRef drsQueryBus, ActorRef drsCmdBus, String driverLocation) {
        this.drsQueryBus = drsQueryBus;
        this.drsCmdBus = drsCmdBus;
        System.setProperty("webdriver.gecko.driver", driverLocation);
    }*/

    public static void main( String[] args ) throws InterruptedException {

        String index = "0";
        if(args.length > 0){
            index = args[0];
        }

        //todo here
        AnnotationConfigApplicationContext springCtx =null;
               // new AnnotationConfigApplicationContext(DrsBizCoreAnnotationConfig4DP.class);

        ExtendedActorSystem system = (ExtendedActorSystem) ActorSystem.create("drsRT");

        LoggingAdapter log = Logging.getLogger(system, App.class);

        QuartzSchedulerExtension quartzSchedulerExtension = new QuartzSchedulerExtension(system);

        ActorRef drsQueryBus = system.actorOf(Props.create(DrsQueryBus.class),
                "DrsQueryBus");

        ActorRef drsCmdBus = system.actorOf(Props.create(DrsCmdBus.class),
                "DrsCmdBus");

        system.actorOf(ViewProductMarketplaceInfoHandler.props(drsQueryBus, springCtx),
                "ViewProductMarketplaceInfoHandler");

        system.actorOf(ImportAmazonReviewReportHandler.props(drsCmdBus, springCtx),
                "ImportAmazonReviewReportHandler");

        ActorRef w =  system.actorOf(Worker.props(drsCmdBus,drsQueryBus,springCtx), "worker");

       // ActorRef router2 = system.actorOf(AmazonReviewScraper.props(drsCmdBus, springCtx),
         ///       "AmazonReviewScraper");




        Config config = ConfigFactory.load("application.conf");
        String geckoDriver = config.getString("drs.geckoDriver");
        System.setProperty("webdriver.gecko.driver", geckoDriver);

       // w.tell(new Start() , ActorRef.noSender());


        quartzSchedulerExtension.schedule("cronRTAM", w, new Start());

        quartzSchedulerExtension.schedule("cronRTPM", w, new Start());


        /*

        Timeout timeout = new Timeout(Duration.create(180, "seconds"));
        final Future<Object> f1 =
                ask(drsQueryBus,
                        new com.kindminds.drs.api.message.viewProductMarketplaceInfo.
                             GetLiveAmazonAsins(),
                             //   GetLiveK101AmazonAsins(),
                        timeout);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String amazonAsins = (String) Await.result(f1, timeout.duration());
            List<AmazonAsin> amazonAsinsList =
                    mapper.readValue(amazonAsins, new TypeReference<List<AmazonAsinImpl>>(){});

           // List<List<AmazonAsin>> amazonAsinsSplit = Lists.partition(amazonAsinsList, amazonAsinsList.size() / 5);

            //System.out.//println("Totoal Size " + amazonAsinsList.size());
            //System.out.//println("Index " + Integer.parseInt(index));
           // //System.out.//println("Split Size " + amazonAsinsSplit.get(Integer.parseInt(index)).size());


           // w.tell(new Start(amazonAsinsSplit.get(Integer.parseInt(index))) , ActorRef.noSender());
            w.tell(new Start(amazonAsinsList) , ActorRef.noSender());

            /*
            for(int x = 0 ; x < amazonAsinsList.size() ; x++) {{

                //System.out.//println("CCCCCCCCCCCCCCCCCcc");
               // if(amazonAsinsList.get(x).getAsin().equals("B01HXXJPWK")){
                    //System.out.//println("AAAAAAAAAAAAAAAAAAAAAAAA");
                    //System.out.//println(amazonAsinsList.get(x).getAsin());
                    //System.out.//println("AAAAAAAAAAAAAAAAAAAAAAAA");
                    //System.out.//println(amazonAsinsList.get(x).getProductId());
                    //System.out.//println("AAAAAAAAAAAAAAAAAAAAAAAA");
                    //System.out.//println(amazonAsinsList.get(x).getAsin());
                    //System.out.//println("AAAAAAAAAAAAAAAAAAAAAAAA");
                    router2.tell(new ScrapeAmazonReview(amazonAsinsList.get(x)) , ActorRef.noSender());

                    //System.out.//println(x);
                //}

            }}
            */

          //  //System.out.//println("ENDENDENDENDENDENDENDENDENDENDENDENDENDENDENDENDEND");


          //  router2.tell(new ScrapeAmazonReviews(amazonAsinsList) , ActorRef.noSender());


            /*
            List<List<AmazonAsin>> amazonAsinsSplit = Lists.partition(amazonAsinsList, amazonAsinsList.size() / 8);
            for(int x = 0 ; x < amazonAsinsSplit.size() ; x++) {{

                router2.tell(new ScrapeAmazonReviews(amazonAsinsSplit.get(x)) , ActorRef.noSender());
            }}*/
      //  } catch (Exception e1) {
        //    e1.printStackTrace();
        //}





        /*
        App amazonReviewScraper = new App(drsQueryBus, drsCmdBus, geckoDriver);


        try {
            amazonReviewScraper.init(log);
        } catch (Exception e) {

            log.error("Amazon Review Import Error!", e.getCause());
            e.printStackTrace();


            system.actorOf(MailHandler.props(drsCmdBus, springCtx),
                    "MailHandler");
            Timeout timeout = new Timeout(Duration.create(180, "seconds"));
            final Future<Object> f1 =
                    ask(drsCmdBus,
                            new com.kindminds.drs.api.message.util.
                                    MailMessage(SOFTWARE_ENG_EMAIL, "Amazon Review Import Error!", e.toString()),
                            timeout);
            try {
                Await.result(f1, timeout.duration());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        waitingThreads:
        while(true) {
            Thread.sleep(4000);
            for (Thread t : myThreads) {
                if (t.isAlive())
                    continue waitingThreads;
            }
            break;
        }
        */

        //Time for AKKA messages to finish
       // Thread.sleep(3000);

       // log.info("***** SHUTTING DOWN *****");
      //  System.exit(0);
    }




}