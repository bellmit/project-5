package com.kindminds.drs.rt;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.rt.Complete;
import com.kindminds.drs.rt.ImportAmazonReviews;
import com.kindminds.drs.rt.ScrapeAmazonReview;
import com.kindminds.drs.rt.ScrapeAmazonReviews;
import com.kindminds.drs.api.usecase.reviewtracker.ImportAmazonReviewReportUco;
import com.kindminds.drs.rt.RegisterCommandHandler;

import com.kindminds.drs.api.v1.model.amazon.AmazonReviewReportItem;

import com.kindminds.drs.api.v1.model.product.AmazonAsin;
import com.kindminds.drs.v1.model.impl.amazon.AmazonReviewReportItemImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.*;

public class AmazonReviewScraper extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props(ActorRef drsCmdBus , AnnotationConfigApplicationContext springCtx) {

        return Props.create(AmazonReviewScraper.class ,
                () -> new AmazonReviewScraper(drsCmdBus , springCtx) );
    }

    private final AnnotationConfigApplicationContext springCtx ;

    private final String name = self().path().name();

    private ActorRef drsCmdBus;

    public AmazonReviewScraper(ActorRef drsCmdBus ,
                                           AnnotationConfigApplicationContext springCtx) {
        this.springCtx = springCtx;
        this.drsCmdBus = drsCmdBus;

      //  drsCmdBus.tell(new RegisterCommandHandler(name , ScrapeAmazonReviews.class.getName()
        //        , self()) , ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(ScrapeAmazonReviews.class, sar -> {
                    WebDriver masterDriver = sar.masterDriver();
                    WebDriver itemDriver = sar.itemDriver();

                    try {
                        // = this.createDriver();
                        //itemDriver = this.createDriver();

                        this.scrape(sar.amazonAsinSnip() ,masterDriver ,itemDriver);


                    } catch (Exception ex) {
                        log.error("Amazon Review Import Error!", ex.getCause());
                        ex.printStackTrace();
                    } finally {
                        //if(masterDriver != null) masterDriver.quit();
                        //if(itemDriver != null) itemDriver.quit();
                    }



                })
                .match(ScrapeAmazonReview.class, sar -> {

                        WebDriver masterDriver = sar.masterDriver();
                        WebDriver itemDriver = sar.itemDriver();
                        try {


                            //System.out.//println("BBBBBBBBBBBB");
                            this.scrape(sar.amazonAsi(), masterDriver,itemDriver);

                        } catch (Exception ex) {
                            log.error("Amazon Review Import Error!", ex.getCause());
                            ex.printStackTrace();
                        } finally {

                            //if(masterDriver != null) masterDriver.quit();
                            //if(itemDriver != null) itemDriver.quit();

                            drsCmdBus.tell(new Complete(masterDriver,itemDriver) , ActorRef.noSender());
                        }

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private void scrape(AmazonAsin amazonAsin , WebDriver masterDriver , WebDriver itemDriver){

        //global driver  one for whole reviews
        // the other for single review to get correct asin

        //FirefoxOptions options = new FirefoxOptions();
        //options.setHeadless(true);
        //WebDriver driver = null;
        try {
          //  driver = new FirefoxDriver(options);
            int count = 0;
           // for (AmazonAsin amazonAsin : amazonAsinSnip) {
                count +=1;
                List<AmazonReviewReportItem> reviews = getReviews(amazonAsin, log, masterDriver , itemDriver);
                //System.out.//println("ASIN count " + count);

                if(reviews != null && !reviews.isEmpty())
                    insertAmazonReviews(reviews);
           // }
        } catch (Exception ex) {
            log.error("Amazon Review Import Error!", ex.getCause());
            ex.printStackTrace();
        } finally {
           // if(driver != null){ driver.quit();}
        }
    }




    private void scrape(List<AmazonAsin> amazonAsinSnip , WebDriver masterDriver , WebDriver itemDriver){

      //  FirefoxOptions options = new FirefoxOptions();
       // options.setHeadless(true);
       // WebDriver driver = null;
        try {
         //   driver = new FirefoxDriver(options);
            int count = 0;
            for (AmazonAsin amazonAsin : amazonAsinSnip) {
                count +=1;
                List<AmazonReviewReportItem> reviews = getReviews(amazonAsin, log, masterDriver , itemDriver );
                //System.out.//println("ASIN count " + count);

                if(reviews != null && !reviews.isEmpty())
                    insertAmazonReviews(reviews);
            }
        } catch (Exception ex) {
            log.error("Amazon Review Import Error!", ex.getCause());
            ex.printStackTrace();
        } finally {
           // if(driver != null){ driver.quit();}
        }
    }

    private List<AmazonReviewReportItem> getReviews(AmazonAsin amazonAsin ,
                                                    LoggingAdapter log, WebDriver masterDriver ,
                                                    WebDriver itemDriver )
            throws InterruptedException {

        Set<AmazonReviewReportItem> reviews = new HashSet<>();

        String url = "http://www." +
                amazonAsin.getMarketplaceName() + "/product-reviews/" + amazonAsin.getAsin();
        //System.out.//println(url);

        List<AmazonReviewReportItem> productReviews = getReview(url,
                amazonAsin.getAsin(),
                amazonAsin.getProductId(),
                amazonAsin.getMarketplaceId() ,log, masterDriver, itemDriver);


        reviews.addAll(productReviews);


        return new ArrayList<AmazonReviewReportItem>(reviews);
    }


    private List<AmazonReviewReportItem> getReview(String url,
                                                   String asin,
                                                   int productId,
                                                   int marketplaceId , LoggingAdapter log,
                                                   WebDriver masterDriver ,
                                                   WebDriver itemDriver ) throws InterruptedException {

        Set<AmazonReviewReportItemImpl> reviews = new HashSet<>();
        AmazonReviewReportItemImpl review;

        String urlToNavigate = url + "?sortBy=recent";

        int pageNo = 1;
        while(true) {

            masterDriver.get(urlToNavigate);
            Thread.sleep((new Random()).nextInt(4000) + 2000); // More human like (min 2 second wait)


            List<WebElement> reviewsElement = masterDriver.findElements(By.className("review"));

            ListIterator<WebElement> elements = reviewsElement.listIterator();


            while(elements.hasNext()) {


                WebElement element = elements.next();


                if(!element.getAttribute("class").contains("a-box")){
                    log.info("*****************************************");

                    //WebElement title = element.findElement(By.className("review-title-content"));
                    WebElement title =  element.findElement(By.cssSelector("a[data-hook='review-title']"));
                    String reviewAsin = "";
                    String reviewId = "";
                    if(title != null) {


                        String [] titleAry = title.getAttribute("href").split("/");
                        reviewId =titleAry[5];


                        if(reviewId.contains("?"))
                            reviewId = reviewId.replaceAll("\\?.+", "");


                        if(StringUtils.hasText(reviewId)){
                            reviewAsin =  this.getAsin(reviewId , marketplaceId , log , itemDriver);
                        }

                        log.info("Review ID: " + reviewId);
                        log.info("Review Title: " + title.getText());
                    }

                    if(StringUtils.hasText(reviewAsin) && reviewAsin.equals(asin)){

                        review = new AmazonReviewReportItemImpl();
                        review.setProductId(productId);
                        review.setMarketplaceId(marketplaceId);
                        review.setId(reviewId);
                        review.setTitle(title.getText());


                        WebElement customerName = element.findElement(By.className("a-profile-name"));
                        if(customerName != null) {
                            review.setCustomerName(customerName.getText());
                            log.info("Customer Name: " + customerName.getText());
                        }


                        //Due to using class name to find element , sometimes the title will be empty. So using xpath.
                        WebElement stars = element.findElement(By.xpath("div/div/div[2]/a"));

                        if(stars != null) {
                            if(StringUtils.isEmpty(stars.getAttribute("title"))){
                                review.setStarRating(0);
                            }else{
                                review.setStarRating(
                                        Double.parseDouble(stars.getAttribute("title").
                                                split(" ")[0].replace(',','.')));

                            }

                            log.info("Review Stars: " + stars.getAttribute("title").split(" ")[0]);
                        }

                        WebElement dateCreated = element.findElement(By.className("review-date"));
                        if(dateCreated != null) {
                            review.setDateCreated(dateCreated.getText());
                            log.info("Review Date: " + dateCreated.getText());
                        }else{
                            log.info("Can't find Review Date");
                        }

                        WebElement comment = element.findElement(By.className("review-text"));
                        if(comment != null) {
                            review.setComment(comment.getText());
                            log.info("Review Details: " + comment.getText());
                        }

                        try {
                            WebElement helpful = element.findElement(By.className("cr-vote-text"));
                            if(helpful != null) {
                                String helpfulCount = helpful.getText().split(" ")[0];
                                helpfulCount = (helpfulCount.equals("One") ? "1" : helpfulCount);
                                helpfulCount = (helpfulCount.equals("Eine") ? "1" : helpfulCount);
                                helpfulCount = (helpfulCount.equals("Une") ? "1" : helpfulCount);
                                if(helpful.equals("A"))
                                    helpfulCount = helpful.getText().split(" ")[1];
                                helpfulCount = (helpfulCount.equalsIgnoreCase("una") ? "1" : helpfulCount);

                                try{

                                    if(helpful.equals("")) review.setHelpful(0);
                                    else review.setHelpful(Integer.parseInt(helpfulCount));

                                }catch(Exception e){

                                    log.error("Number format error!" , e.getCause());

                                    review.setHelpful(0);
                                }

                                log.info("Review Helpful: " + helpfulCount);
                            } else
                                review.setHelpful(0);
                        } catch (org.openqa.selenium.NoSuchElementException nse) {
                            review.setHelpful(0);
                            log.info("Review Helpful: 0");
                        }

                        reviews.add(review);

                    }


                    log.info("*****************************************\r\n\r\n");
                }


            }
            try {
                if(pageNo++ > 3) break;

               WebElement btnNext = masterDriver.findElement(By.className("a-last"));
                urlToNavigate = btnNext.findElement(By.xpath("a")).getAttribute("href");

                if(urlToNavigate != null && !urlToNavigate.isEmpty())
                    continue;
            } catch (NoSuchElementException nse) {}
            break;
        }
        return new ArrayList<AmazonReviewReportItem>(reviews);
    }

    private String getAsin(String reviewId , int marketplaceId , LoggingAdapter log,
                                                   WebDriver driver) throws InterruptedException {



        Marketplace marketplace = Marketplace.fromKey(marketplaceId);
        String url = "http://www." + marketplace.getName() + "/product-reviews/" + reviewId;
        Random rnd = new Random();
        String urlToNavigate = url ;

            driver.get(urlToNavigate);
            Thread.sleep(rnd.nextInt(4000) + 2000); // More human like (min 2 second wait)

            List<WebElement> reviewsElement = driver.findElements(By.className("review"));

            ListIterator<WebElement> elements = reviewsElement.listIterator();
            while(elements.hasNext()) {

                WebElement element = elements.next();

                log.info("*****************************************");

                WebElement title = element.findElement(By.className("review-title"));

                if(title != null) {


                    String [] titleAry = title.getAttribute("href").split("/");

                    ////System.out.//println(titleAry[6].split("=")[3]);

                    String asin = titleAry[6].split("=")[3];

                    log.info("Asin:"  + asin);

                    return asin;

                }

                log.info("*****************************************\r\n\r\n");
            }





        return "" ;
    }



    private Integer insertAmazonReviews(List<AmazonReviewReportItem> reviews) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Timeout timeout = new Timeout(Duration.create(30000, "seconds"));
        String jsonRequest = mapper.writeValueAsString(reviews);

        //System.out.//println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
        //System.out.//println(jsonRequest);

        /*
        final Future<Object> f1 =
                ask(drsCmdBus,
                        new com.kindminds.drs.api.message.importAmazonReviewReportUco.ImportAmazonReviews(
                                jsonRequest),
                        timeout);*/


      //  Integer amazonReviewsInserted = (Integer) Await.result(f1, timeout.duration());

        drsCmdBus.tell(new com.kindminds.drs.rt.ImportAmazonReviews(
              jsonRequest), ActorRef.noSender());

        //return amazonReviewsInserted;
        return 0;
    }




}
