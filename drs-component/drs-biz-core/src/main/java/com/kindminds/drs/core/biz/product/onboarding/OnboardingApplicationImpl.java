package com.kindminds.drs.core.biz.product.onboarding;

import com.fasterxml.uuid.Generators;


import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplication;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitem;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitemStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.OnboardingApplicationRepo;
import com.kindminds.drs.core.biz.repo.product.OnboardingApplicationRepoImpl;
import com.kindminds.drs.api.data.cqrs.store.event.queries.OnboardingApplicationLineitemQueries;
import com.kindminds.drs.api.data.cqrs.store.event.queries.OnboardingApplicationQueries;
import com.kindminds.drs.enums.OnboardingApplicationStatusType;

import com.kindminds.drs.service.util.SpringAppCtx;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


public class OnboardingApplicationImpl implements OnboardingApplication {

    private String id;
    private String serialNumber;
    private String supplierKcode;

    private BigDecimal serviceFee;
    private String quotationNumber;
    private Integer supplierCompanyId;

    private OffsetDateTime createTime;

    private OnboardingApplicationStatusType status;

    private List<OnboardingApplicationLineitem> items = null;

    private OnboardingApplicationRepo appRepo = new OnboardingApplicationRepoImpl();

    private OnboardingApplicationQueries queries = (OnboardingApplicationQueries)
            SpringAppCtx.get().getBean(OnboardingApplicationQueries.class);


    private OnboardingApplicationLineitemQueries lineitemQueries = (OnboardingApplicationLineitemQueries)
            SpringAppCtx.get().getBean(OnboardingApplicationLineitemQueries.class);

    public static OnboardingApplicationImpl valueOf(String id , OffsetDateTime createTime , int supplierCompanyId , String serialNumber ,
                                                    OnboardingApplicationStatusType status  ){


        return  new OnboardingApplicationImpl(id , createTime , supplierCompanyId , serialNumber , status);
    }


    private OnboardingApplicationImpl(String id , OffsetDateTime createTime , int supplierCompanyId , String serialNumber ,
                                      OnboardingApplicationStatusType status  ){

        this.id = id ;
        this.createTime = createTime;
        this.supplierCompanyId = supplierCompanyId;

        this.serialNumber = serialNumber;
        this.status = status;


    }

    //todo need remvoe
    public static OnboardingApplicationImpl submitOnboardingApplication(String supplierKcode,
                                                                        String serialNumber,
                                                                        Product product,
                                                                        OnboardingApplicationStatusType status) {

        return new OnboardingApplicationImpl(supplierKcode, serialNumber, product, status, true);
    }

    private String initId(String serialNumber){

       return queries.getId(serialNumber).orElseGet(()->
                Generators.randomBasedGenerator().generate().toString());

    }

    private String intiSn(String serialNumber){

        if(StringUtils.hasText(serialNumber)){
            if(serialNumber.equals("New")){
               return this.generateSerial(supplierKcode);
            }else{
                return serialNumber;
            }
        }

        return "";
    }


    public OnboardingApplicationImpl(String supplierKcode, String serialNumber,
                                     Product product,
                                     OnboardingApplicationStatusType status, boolean submit ) {

        this.supplierKcode = supplierKcode;

        this.id = this.initId(serialNumber);

        this.createTime = OffsetDateTime.now();

        this.serialNumber =intiSn(serialNumber);

        this.createLineitem(product);

        this.status = status;

    }


    public OnboardingApplicationImpl(String supplierKcode , String serialNumber,
                                     List<Product> products, OnboardingApplicationStatusType status, boolean submit ) {

        this.supplierKcode = supplierKcode;

        this.id = this.initId(serialNumber);

        this.serialNumber = this.intiSn(serialNumber);

        this.createTime = OffsetDateTime.now();

        this.createLineItem(products);

        this.status = status;

    }

    public OnboardingApplicationImpl(String supplierKcode,
                                     List<Product> products, OnboardingApplicationStatusType status, boolean submit ) {

        this.supplierKcode = supplierKcode;

        this.id = this.initId(serialNumber);

        this.createTime = OffsetDateTime.now();

        this.createMigrateLineItem(products);

        this.status = status;

    }

    private String generateSerial(String supplierKcode) {
        String today = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        String seq = String.format("%03d", queries.getNextSeq(supplierKcode));

        return new StringBuilder(supplierKcode).append("-").append(today).append("-").append(seq).toString();
    }





    private void createLineItem( List<Product> products) {

        this.items = new ArrayList<OnboardingApplicationLineitem>();
        for (int i = 0; i < products.size() ; i++) {

            this.items.add(OnboardingApplicationLineitemCreator.create(this.id ,
                    this.createTime , products.get(i),
                    StringUtils.hasText(this.serialNumber) ?
                            OnboardingApplicationLineitemStatusType.DRAFT :
                            OnboardingApplicationLineitemStatusType.NO_SERIAL_NUM ));
        }

        //Optional<List<String>> itemIds = lineitemQueries.getIds(this.id);

        /*
        if(!itemIds.isPresent()){
            products.forEach(p->{
                this.items.add(OnboardingApplicationLineitemCreator.create(this.id ,
                        this.createTime , p));
            });
        }else{

            int idsMaxIndex = itemIds.get().size() -1;
            for (int i = 0; i < products.size() ; i++) {
                if(i > idsMaxIndex){
                    this.items.add(OnboardingApplicationLineitemCreator.create(this.id ,
                            this.createTime , products.get(i)));
                }else{
                    this.items.add(OnboardingApplicationLineitemCreator.create(
                            itemIds.get().get(i),this.id ,
                            this.createTime , products.get(i)));
                }
            }

        }
        */


    }


    private void createLineitem(Product product) {

        this.items = new ArrayList<OnboardingApplicationLineitem>();


        Optional<String> itemId = lineitemQueries.getId(this.id , product.getMarketside());

        if(!itemId.isPresent()){

                this.items.add(OnboardingApplicationLineitemCreator.create(this.id ,
                        this.createTime , product));

        }else{

            this.items.add(OnboardingApplicationLineitemCreator.create(
                    itemId.get(),this.id , this.createTime , product));


        }


    }

    private void createMigrateLineItem( List<Product> products) {

        this.items = new ArrayList<OnboardingApplicationLineitem>();

        Optional<List<String>> itemIds = lineitemQueries.getIds(this.id);

        if(!itemIds.isPresent()){
            products.forEach(p->{
                this.items.add(OnboardingApplicationLineitemCreator.createMigratedOnboardingApplicationLineitem(this.id ,
                        this.createTime ,p, OnboardingApplicationLineitemStatusType.fromText(p.getEditingStatusText())));
            });
        }else{

            int idsMaxIndex = itemIds.get().size() -1;
            for (int i = 0; i < products.size() ; i++) {
                if(i > idsMaxIndex){
                    this.items.add(OnboardingApplicationLineitemCreator.createMigratedOnboardingApplicationLineitem(this.id ,
                            this.createTime , products.get(i) ,
                            OnboardingApplicationLineitemStatusType.fromText(products.get(i).getEditingStatusText())));
                }else{
                    this.items.add(OnboardingApplicationLineitemCreator.create(
                            itemIds.get().get(i),this.id ,
                            this.createTime , products.get(i)));
                }
            }

        }


    }





    @Override
    public OffsetDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public Integer getSupplierCompanyId() {
        return this.supplierCompanyId;
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }


    @Override
    public OnboardingApplicationStatusType getStatus() {
        return this.status;
    }


    @Override
    public String getSupplierKcode() {
        return this.supplierKcode;
    }

    @Override
    public List<OnboardingApplicationLineitem>  getLineitems() {
        return this.items;
    }

    @Override
    public BigDecimal getServiceFee() {
        return this.serviceFee;
    }

    @Override
    public String getQuotationNumber() {
        return this.quotationNumber;
    }

    @Override
    public void submit() {
        this.status = OnboardingApplicationStatusType.SUBMITTED;
        this.createTime =  OffsetDateTime.now();
    }

    @Override
    public void accept() {

        this.status = OnboardingApplicationStatusType.ACCEPTED;
        this.createTime =  OffsetDateTime.now();
    }

    @Override
    public void startQuoteRequest() {
        this.status = OnboardingApplicationStatusType.QUOTE_REQUEST_STARTED;
        this.createTime =  OffsetDateTime.now();
    }



    @Override
    public void completeQuoteRequest() {
        this.status = OnboardingApplicationStatusType.QUOTE_REQUEST_COMPLETED;
        this.createTime =  OffsetDateTime.now();
    }

    @Override
    public void switchToReviewLineitems() {
        this.status = OnboardingApplicationStatusType.LINEITEM_REVIEWING;
        this.createTime =  OffsetDateTime.now();

    }

    @Override
    public void reviewFinalFeasibility() {
        this.status = OnboardingApplicationStatusType.FEASIBILITY_REVIEWED;
        this.createTime =  OffsetDateTime.now();
    }

    @Override
    public void approveProductFeasibility() {
        this.status = OnboardingApplicationStatusType.APPROVED;
        this.createTime =  OffsetDateTime.now();
    }

    @Override
    public void rejectProductFeasibility() {
        this.status = OnboardingApplicationStatusType.FEASIBILITY_REJECTED;
        this.createTime =  OffsetDateTime.now();
    }


}
