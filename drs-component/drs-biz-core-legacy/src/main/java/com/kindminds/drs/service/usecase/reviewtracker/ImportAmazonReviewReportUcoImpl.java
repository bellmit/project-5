package com.kindminds.drs.service.usecase.reviewtracker;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.reviewtracker.ImportAmazonReviewReportUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonReviewNotification;
import com.kindminds.drs.api.v1.model.amazon.AmazonReviewReportItem;
import com.kindminds.drs.api.data.access.usecase.reviewtracker.ImportAmazonReviewReportDao;
import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.util.RTDateUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class ImportAmazonReviewReportUcoImpl implements ImportAmazonReviewReportUco {

    @Autowired
    ImportAmazonReviewReportDao dao;

    @Autowired
    MailUtil mailUtil;

    @Autowired
    MessageSource messageSource;

    private static final String MAIL_ADDRESS_TTS_CARE = "care@truetosource.co";
    private static final String MAIL_ADDRESS_NOREPLY = "DRS.network <drs-noreply@tw.drs.network>";

    private static final String MAIL_ADDRESS_SD = "arthur.wu@drs.network";

    @Override
    public Integer importReviews(List<AmazonReviewReportItem> amazonReviews) {


        List<AmazonReviewReportItem> newReviews = dao.insertAmazonReviews(amazonReviews);

        if(newReviews != null && !newReviews.isEmpty()) {

            Config config = ConfigFactory.load("application.conf");
            boolean notifyNewReview = config.getBoolean("drs.notifyNewReview");
            if(notifyNewReview)
                sendNotifications(newReviews);

            return newReviews.size();
        }
        else return 0;

    }

    private void sendNotifications(List<AmazonReviewReportItem> newReviews) {

        for(AmazonReviewReportItem review : newReviews) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMMM yyyy", Locale.US);
                Date reviewCreate = sdf.parse(review.getDateCreated());
                if(RTDateUtil.isBeforeMonths(-6, reviewCreate))
                    continue;
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            try{

                AmazonReviewNotification arn = dao.queryAmazonReviewNotification(review.getProductId());

                Locale englishLocale = Locale.US;

                String[] subjectParams = {arn.getSupplierKcode(), arn.getSupplierName(),
                        String.valueOf(review.getStarRating()), arn.getProductName()};
                String subject = this.messageSource.getMessage("mail.amazonReviewSubject",
                        subjectParams, englishLocale);

                Marketplace marketplace = Marketplace.fromKey(review.getMarketplaceId());
                String url = "http://www." + marketplace.getName() + "/product-reviews/" + review.getId();
                String[] bodyParams = {arn.getSupplierKcode(), arn.getSupplierName(), arn.getProductName(), marketplace.getName(),
                        review.getDateCreated(), String.valueOf(review.getStarRating()), url, review.getTitle(), (review.getComment() != null ? review.getComment().replaceAll("[\\r\\n]","<br/>") : "")};
                String body = this.messageSource.getMessage("mail.amazonReviewBody", bodyParams,
                        englishLocale) +
                        this.mailUtil.getSignature(MailUtil.SignatureType.NO_REPLY, englishLocale);

                String [] mailTo = {MAIL_ADDRESS_TTS_CARE};
                String [] bcc = {};
                Config config = ConfigFactory.load("application.conf");
                try {
                    List<String> bccList = config.getStringList("drs.amazonReviewBccList");
                    bcc =  bccList.toArray(new String[0]);
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                this.mailUtil.SendMimeWithBcc(mailTo, bcc, MAIL_ADDRESS_NOREPLY, subject, body);

            }
            catch(Exception e){
                this.mailUtil.Send(MAIL_ADDRESS_SD, MAIL_ADDRESS_NOREPLY, "Create message from Amazon Review Notification ERROR", e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
            }
        }
    }
}