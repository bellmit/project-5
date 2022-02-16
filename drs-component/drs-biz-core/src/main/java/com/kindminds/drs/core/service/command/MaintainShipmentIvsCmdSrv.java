package com.kindminds.drs.core.service.command;

import com.kindminds.drs.AccountManager;
import com.kindminds.drs.Context;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsSearchCondition;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.repo.logistics.IvsRepo;
import com.kindminds.drs.api.v2.biz.domain.model.request.SaveIvsRequest;
import com.kindminds.drs.core.biz.logistics.IvsImpl;
import com.kindminds.drs.core.biz.repo.logistics.IvsRepoImpl;
import com.kindminds.drs.api.data.row.logistics.SkuBoxNumRow;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.api.data.access.rdb.logistics.IvsDao;

import com.kindminds.drs.persist.v1.model.mapping.logistics.ShipmentIvsImpl;
import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.v1.model.impl.ShipmentIvsSearchConditionImplSvc;

import org.apache.commons.io.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MaintainShipmentIvsCmdSrv {

    //private Logger logger = Logger.getLogger(MaintainShipmentIvsCmdSrv.class);

    private String fcaDeliveryDateFormat = "yyyy-MM-dd";

    private String LOGISTICS = "Logistics <logistics@drs.network>";
    private String ACCOUNT_MANAGERS = "account.managers@tw.drs.network";
    private String SOFTWARE_ENGINEERS = "software.engineering@drs.network";
    private String ADDRESS_NO_REPLY = "DRS.network <drs-noreply@tw.drs.network>";
    private String ADDRESS_TEST = "robert.lee@drs.network";
    private String CEO = "shirley.wu@tw.drs.network";

    private int BUFFER_SIZE = 4096;

    @Autowired
    @Qualifier("envProperties")
    private Properties env;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private MailUtil mailUtil;


    public String createDraft(SaveIvsRequest request )  {

        Assert.isTrue(Context.getCurrentUser().isSupplier(), "Current user is not supplier");

        Ivs ivs = new IvsImpl();
        ivs.processDraft(request);

        IvsRepo repo = new IvsRepoImpl();
        repo.add(ivs);

        return ivs.getName();
    }

    public String createDraftFromImport(SaveIvsRequest request )  {
        Ivs ivs = new IvsImpl();
        ivs.processDraftForKcode(request, request.getSellerCompanyKcode(), request.getBuyerCompanyKcode());

        IvsRepo repo = new IvsRepoImpl();
        repo.add(ivs);

        return ivs.getName();
    }

    public String update(SaveIvsRequest request )  {

        IvsRepoImpl repo = new IvsRepoImpl();

        Ivs ivs = repo.findByName(request.getName()).get();
        ShipmentStatus oldStatus = ivs.getStatus();

        ivs.update(request);

        repo.edit(ivs);

        ShipmentStatus status = ivs.getStatus();

        if (oldStatus == ShipmentStatus.SHPT_PLANNING
                && status == ShipmentStatus.SHPT_AWAIT_PLAN) {

            IvsDao ivsDao =  SpringAppCtx.get().getBean( IvsDao.class);

            List<SkuBoxNumRow> skuBoxNumList = ivsDao.queryNewSkuBoxNum(ivs.getName());

            processUpdateLineItemsNotification(ivs.getSellerCompanyKcode(),
                    request.getName(), skuBoxNumList);

        } else if (status == ShipmentStatus.SHPT_DRAFT
                || status == ShipmentStatus.SHPT_AWAIT_PLAN
                || status == ShipmentStatus.SHPT_FC_BOOKING_CONFIRM
                || status == ShipmentStatus.SHPT_PLANNING) {

            processUpdateNotification(request.getName());

        }



        return request.getName();

    }

    public String submit(String name )  {

        IvsDao ivsDao =  SpringAppCtx.get().getBean( IvsDao.class);

        Assert.isTrue(ivsDao.queryStatus(name) == ShipmentStatus.SHPT_DRAFT, "Submit a non-draft shipment is not allowed.");
        Assert.isTrue(Context.getCurrentUser().isSupplier());

        IvsRepoImpl repo = new IvsRepoImpl();
        Ivs ivs = repo.findByName(name).get();
        ivs.submit(name);

        this.renameShipmentDirectory(name, ivs.getName());

        this.processRequestPickupNotification(Context.getCurrentUser().getCompanyKcode(),
                ivs.getName());

        return ivs.getName();
    }

    public String accept(String name )  {

        IvsRepoImpl repo = new IvsRepoImpl();
        Ivs ivs = repo.findByName(name).get();

        Assert.isTrue(ivs.getStatus() == ShipmentStatus.SHPT_AWAIT_PLAN, "Status check failed.");
        Assert.isTrue(Context.getCurrentUser().isDrsUser());

        ivs.accept(name);

//        this.processAcceptNotificationToSupplier(name, ivs.buyerCompanyKcode)

        return name;
    }

   public String confirm(String shipmentName )  {

        String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();

        IvsRepoImpl repo = new IvsRepoImpl();
        Ivs ivs = repo.findByName(shipmentName).get();

        Assert.isTrue(ivs.getStatus() == ShipmentStatus.SHPT_PLANNING, "Status check failed.");
        Assert.isTrue(ivs.getBuyerCompanyKcode().equals(userCompanyKcode));

        ivs.confirm(shipmentName);

        processConfirmEmail(ivs.getSellerCompanyKcode(), ivs.getName());

        return shipmentName;
    }

    public String delete(String shipmentName )  {

        resetGuiInvoiceDirectory(shipmentName);
        IvsRepoImpl repo = new IvsRepoImpl();
        Ivs ivs = repo.findByName(shipmentName).get();
        repo.remove(ivs);

        return ivs.getName();
    }

    private void processUpdateLineItemsNotification(String companyCode, String shipmentName ,
                                                   List<SkuBoxNumRow> skuBoxNumList) {

        if(mailUtil != null && companyDao != null){
            try {
                String amEmail = AccountManager.getEmail(
                        companyDao.queryAccountManager(companyCode));
                String[] to = {amEmail};
//                to.forEach {println(it)}
//                to = arrayOf(ADDRESS_TEST)

                String subject = getUpdateLineItemsEmailSubject(shipmentName);
                String body = subject + getEmailBodyLineItems(skuBoxNumList) +
                        getEmailBodyLink(shipmentName);

                String signature = mailUtil.getSignature(MailUtil.SignatureType.NO_REPLY);
                body = mailUtil.appendSignature(body, signature);

                sendEmail(to, subject, body);
            } catch (Exception e ) {
                mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "IVS processUpdateLineItems email ERROR", e.getMessage() + "\n\n" + e.toString() + "\n\n" + e.fillInStackTrace());
            }
        }


    }

    private String getEmailBodyLineItems(List<SkuBoxNumRow> skuBoxNumList )  {
        String  bodylineItems = "<br>";
        for (SkuBoxNumRow it :skuBoxNumList) {
            bodylineItems += "Line item  ${it.sku} , box number ${it.boxNum} <br>" ;
        }
        return bodylineItems;
    }

    private void processUpdateNotification(String shipmentName ) {
        String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();

        if(companyDao != null && mailUtil != null){
            if (companyDao.isSupplier(userCompanyKcode)) {
                try {

                    String amEmail = AccountManager.getEmail(
                            companyDao.queryAccountManager(userCompanyKcode));
                    String[] to = {LOGISTICS, amEmail};
//                    to.forEach {println(it)}
//                    to = arrayOf(ADDRESS_TEST)

                    String subject = "Shipment $shipmentName has been updated by supplier $userCompanyKcode";
                    String body = "Shipment $shipmentName has been updated by ${Context.getCurrentUser().username} of " +
                            "supplier $userCompanyKcode <br><br><a href='https//access.drs.network/InventoryShipments/" +
                            "$shipmentName'> Link to $shipmentName</a> ";
                    String signature = mailUtil.getSignature(MailUtil.SignatureType.NO_REPLY);
                    body = mailUtil.appendSignature(body, signature);

                    sendEmail(to, subject, body);
                } catch (Exception e ) {
                    mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "IVS processUpdate email ERROR", e.getMessage() + "\n\n" + e.toString() + "\n\n" + e.fillInStackTrace());
                }
            }
        }


    }

    private void processRequestPickupNotification(String supplierKcode ,String shipmentName ) {

        if(companyDao != null && mailUtil != null) {
            try {

                String amEmail = AccountManager.getEmail(
                        companyDao.queryAccountManager(supplierKcode));
                String[] to = {LOGISTICS, amEmail};
//                to.forEach { println(it) }
//                to = arrayOf(ADDRESS_TEST)

                String subject = getRequestPickupNotificationSubject(supplierKcode, shipmentName);
                String body = this.getRequestPickupNotificationMessageBody(supplierKcode, shipmentName);
                String signature = mailUtil.getSignature(MailUtil.SignatureType.NO_REPLY);
                body = mailUtil.appendSignature(body, signature);

                sendEmail(to, subject, body);
            } catch (Exception e ) {
                mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "IVS processRequestPickup email ERROR", e.getMessage() + "\n\n" + e.toString() + "\n\n" + e.fillInStackTrace());
            }
        }
    }

    private void processConfirmEmail(String companyCode, String ivsName) {

        if(mailUtil != null){
            try {
                String [] to = {CEO};
//                to.forEach {println(it)}
//                to = arrayOf(ADDRESS_TEST)

                String subject = getConfirmEmailSubject(ivsName);
                String body = getEmailBody(subject, ivsName);

                sendEmail(to, subject, body);
            } catch (Exception e ) {
                mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "IVS processConfirm email ERROR", e.getMessage() + "\n\n" + e.toString() + "\n\n" + e.fillInStackTrace());
            }
        }
    }

    private String getUpdateLineItemsEmailSubject(String shipmentName )  {
        return "Shipment $shipmentName has been changed from \"Planning\" to \"Awaiting Plan\" by Logistics";
    }

    private String getConfirmEmailSubject(String shipmentName )  {
        return "Shipment $shipmentName is confirmed as Purchase Order ";
    }

    private String getEmailBody(String emailSubject , String shipmentName )  {
        return "$emailSubject " +
                getEmailBodyLink(shipmentName);
    }

    private String getEmailBodyLink(String shipmentName )  {
        return "\n<br><a href='https//access.drs.network/InventoryShipments/$shipmentName'> Link to $shipmentName</a><br><br> ";
    }

    private void sendEmail(String [] to, String subject, String body) {

        if(env != null && mailUtil != null ){
            Boolean sendNotify = java.lang.Boolean.parseBoolean(env.getProperty("SEND_NOTIFY").toString());

            if (sendNotify) {
                mailUtil.SendMime(to, ADDRESS_NO_REPLY, subject, body);
            }
        }


    }

    private void renameShipmentDirectory(String oldName , String newName ) {
        String rootPath = this.getRootFileDir() + File.separator;
        Path oldPath = Paths.get(rootPath, oldName);
        Path newPath = Paths.get(rootPath, newName);
        try {
            Files.move(oldPath, newPath);
        } catch (IOException e ) {
            e.printStackTrace();
        }

    }

    private String getRootFileDir()  {
        return System.getProperty("catalina.home");
    }

    private void resetGuiInvoiceDirectory(String draftName ) {
        File fullPath = new File(this.getRootFileDir() + File.separator + draftName);
        try {
            FileUtils.deleteDirectory(fullPath);
        } catch (IOException e ) {
            e.printStackTrace();
        }

    }



    //=====================================================


    private IvsSearchCondition getActualCondition(IvsSearchCondition originCondition , UserInfo user )  {
         if (user.isDrsUser())
             return originCondition;
        else
             return new ShipmentIvsSearchConditionImplSvc(originCondition, user.getCompanyKcode());
    }

    private void appendPaidTotalToListItem(DtoList<Ivs> list ) {

        IvsDao ivsDao =  SpringAppCtx.get().getBean( IvsDao.class);

        List<String> shipmentNameList = this.createNameListFromShipmentList(list);
        if (shipmentNameList.size() == 0) return;
        Map<String,BigDecimal > shipmentNameToPaymentTotalMap = ivsDao.queryShipmentNameToPaymentTotalMap(shipmentNameList);

        for (Ivs shipment : list.getItems()) {
            ShipmentIvsImpl origShp = (ShipmentIvsImpl) shipment ;
            origShp.setPaymentTotal(shipmentNameToPaymentTotalMap.get(shipment.getName()));
        }
    }

    private void appendUnsName(DtoList<Ivs> list ) {

        IvsDao ivsDao =  SpringAppCtx.get().getBean( IvsDao.class);

        List<String> shipmentNameList = this.createNameListFromShipmentList(list);
        if (shipmentNameList.size() == 0) return;
        Map<String,String> shipmentNameToUnsNameMap = ivsDao.queryShipmentNameToUnsNameMap(shipmentNameList);
        for (Ivs shipment : list.getItems()) {
            ((ShipmentIvsImpl)shipment)
                    .setUnsName(shipmentNameToUnsNameMap.get(shipment.getName()));
        }
    }

    private List<String> createNameListFromShipmentList(DtoList<Ivs> list )  {
        List shipmentNameList = new ArrayList<String>();
        for (Ivs shipment : list.getItems())
            shipmentNameList.add(shipment.getName());
        return shipmentNameList;
    }

    private Boolean checkAccessable(String shipmentName )  {

        IvsDao ivsDao =  SpringAppCtx.get().getBean( IvsDao.class);
        String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
        if (Context.getCurrentUser().isDrsUser()) return true;
        String  shipmentSellerKcode = ivsDao.querySellerKcode(shipmentName) ;
        return  (userCompanyKcode == shipmentSellerKcode) ?  true : false;

    }

    private void processAcceptNotificationToSupplier(String shipmentName , String buyerKcode ) {

        UserDao userDao  = SpringAppCtx.get().getBean( UserDao.class);

        IvsDao ivsDao =  SpringAppCtx.get().getBean( IvsDao.class);

        MessageSource messageSource =  SpringAppCtx.get().getBean( MessageSource.class);


        Boolean sendNotify = java.lang.Boolean.parseBoolean(env.get("SEND_NOTIFY").toString());
        if (sendNotify) {
            try {
                Assert.isTrue(buyerKcode == "K2");
                Locale locale = Context.getCurrentUser().getLocale();
                Assert.isTrue(locale == Locale.TAIWAN);
                String  subject = messageSource.getMessage("mail.InventoryShipmentsDRS-Accept-Subject",
                        new String[]{shipmentName}, locale);
                String body = messageSource.getMessage("mail.InventoryShipmentsDRS-Accept-Body",
                        new String[]{shipmentName, shipmentName, shipmentName},
                        Context.getCurrentUser().getLocale());
                String signature = mailUtil.getSignature(MailUtil.SignatureType.NO_REPLY);
                body = mailUtil.appendSignature(body, signature);

                String  sellerEmail = ivsDao.queryShipmentPickupRequesterEmail(shipmentName);
                String[] mailTo = {sellerEmail};
//                var mailTo = arrayOf(sellerEmail)
//                mailTo = arrayOf(ADDRESS_TEST)

                Assert.isTrue(userDao.isDrsUser(Context.getCurrentUser().getUserId()));
                String currentUserEmail = userDao.queryUserMail(Context.getCurrentUser().getUserId());
                String[] bcc = {currentUserEmail};

                mailUtil.SendMimeWithBcc(mailTo, bcc, currentUserEmail, subject, body);
            } catch (Exception e ) {
                mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "IVS Accept email ERROR", e.getMessage() + "\n\n" + e.toString() + "\n\n" + e.fillInStackTrace());
            } finally {
            }
        }
    }

    private String getRequestPickupNotificationSubject(String supplierKcode ,String shipmentName )  {
        return "Suppler $supplierKcode has requested pick-up for shipment $shipmentName";
    }

    private String getRequestPickupNotificationMessageBody(String supplierKcode , String shipmentName ) {
        return (this.getRequestPickupNotificationSubject(supplierKcode, shipmentName) + "\n"
                + "<br>"
                + "<br>"
                + "<a href='https//access.drs.network/InventoryShipments/" + shipmentName + "'+> Link to " + shipmentName + ".</a> ");
    }

    /*
    fun methodForTestToGetPickupRequesterEmail(shipmentName String) String {
        return this.dao.queryShipmentPickupRequesterEmail(shipmentName)
    }*/


    public String getDraftShipmentName()  {

        IvsDao ivsDao =  SpringAppCtx.get().getBean( IvsDao.class);

        String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
        int serialId = ivsDao.queryMaxSerialIdOfDraftShipments(userCompanyKcode);
        if (serialId == 0) serialId = 1; else serialId = serialId + 1 ;

        return this.generateDraftShipmentName(userCompanyKcode, serialId);

    }


    public Boolean removeGuiInvoiceFile(String fileName )  {
        if (!StringUtils.hasText(fileName)) return false;
        String  draftName = getDraftShipmentName();

        File path = new File(getRootFileDir() + File.separator
                + draftName + File.separator + fileName);
        return path.delete();
    }

    public Boolean ngremoveGuiInvoiceFile(String supplierKcode ,String fileName )  {
        if (!StringUtils.hasText(fileName)) return false;

        File path = new File(getRootFileDir() + File.separator + supplierKcode + File.separator + "IVS" + File.separator + fileName);
        return path.delete();
    }



    private String embeddedShipmentNameForDraft = "DRAFT";
    private String  shipmentNamePrefix = "IVS";

    private String generateDraftShipmentName(String kcode , int nextDraftSerialId )  {
        return (this.shipmentNamePrefix + "-" + kcode + "-"
                + this.embeddedShipmentNameForDraft + nextDraftSerialId);
    }

}