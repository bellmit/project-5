package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.Filter;
import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.data.access.usecase.accounting.ProcessMarketSideTransactionDao;
import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.enums.Settlement;

import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;
import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction;
import com.kindminds.drs.api.v2.biz.domain.model.settlement.MarketSideTransactionRepo;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.error.Mark;

import javax.swing.plaf.synth.SynthScrollBarUI;
import java.util.*;

public class MarketSideTransactionRepoImpl implements MarketSideTransactionRepo {

    private List<String> ignoreMerchantOrderIdPrefixs = Arrays.asList(
            "USTTS","RUSTTS",
            "sb","Rsb",
            "ebay","Rebay",
            "eBay","ReBay");

    //todo arthur change dao
    private final ProcessMarketSideTransactionDao dao =
            (ProcessMarketSideTransactionDao) SpringAppCtx.get().getBean(ProcessMarketSideTransactionDao.class);

    @Override
    public void add(MarketSideTransaction item) {

    }

    @Override
    public void add(List<MarketSideTransaction> items) {

        dao.insertTransactions(items);
    }

    @Override
    public void edit(MarketSideTransaction item) {

    }

    @Override
    public void remove(MarketSideTransaction item) {

    }

    @Override
    public Optional<MarketSideTransaction> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<MarketSideTransaction> findOne(Filter filter) {
        return Optional.empty();
    }

    @Override
    public List<MarketSideTransaction> find(Filter filter) {
        return null;
    }

    @Override
    public List<MarketSideTransaction> findNonProcessedTransactions(String kcode) {

        List<MarketSideTransaction> mtList = new ArrayList<MarketSideTransaction>();
        List<Object []> resultList =  dao.queryNonProcessedTransactionList(kcode);


        int count =0;
        for(Object[] columns:resultList){
            MarketSideTransaction mt = this.createMarketSideTransaction(columns);
            if(mt != null) mtList.add(mt);
            count +=1;
		}

        return mtList;
    }

    @Override
    public List<MarketSideTransaction> findNonProcessedTransactionById(Integer mstId) {

        List<MarketSideTransaction> mtList = new ArrayList<MarketSideTransaction>();
        List<Object []> resultList =  dao.queryNonProcessedTransactionList(mstId);

        MarketSideTransaction mt = this.createMarketSideTransaction(resultList.get(0));
        if(mt != null) mtList.add(mt);


        return mtList;
    }

    @Override
    public List<MarketSideTransaction> findRawTransactions(Date startPoint, Date endPoint ,String  kcode) {

        List<MarketSideTransaction> mtList = new ArrayList<MarketSideTransaction>();
        mtList.addAll(this.findAmazonOrderAndRefunds(startPoint,endPoint,kcode));
        mtList.addAll(this.findAmazonOtherTransactions(startPoint,endPoint,kcode));
        mtList.addAll(this.findReturnToSellableTransactionsFromFba(startPoint,endPoint,kcode));
        mtList.addAll(this.findEbayTransactions(startPoint,endPoint,kcode));
        mtList.addAll(this.findShopifyOrderTransactions(startPoint,endPoint,kcode));
        mtList.addAll(this.findFbaReturnToSupplierTransactions(startPoint,endPoint,kcode));

        this.sortTransactionListByDateAsc(mtList);

        return mtList;
    }

    private MarketSideTransaction createMarketSideTransaction(Object[] columns ){

        Integer id = (Integer)columns[0];
        Date transactionDate = (Date)columns[1];
        String type = (String)columns[2];
        String source = (String)columns[3];
        String sourceId = (String)columns[4];
        String sku = (String)columns[5];
        String description = (String)columns[6];
        String eMsg = (String)columns[7];
        String st = (String)columns[8];

        try {

            //id, transactionDate, type, source, sourceId, sku, description
            if(type.equals(AmazonTransactionType.ORDER.getValue())){
                if(source.equals("Amazon.com"))   return new AmazonOrderUS(
                        id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("TrueToSource")) return new ShopifyOrder(
                        id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("eBay.com"))     return new EbayOrder(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("Amazon.co.uk")) return new AmazonOrderUK(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("Amazon.ca"))    return new AmazonOrderCA(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("Amazon.de"))    return new AmazonOrderDE(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("Amazon.it"))    return new AmazonOrderIT(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("Amazon.fr"))    return new AmazonOrderFR(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("Amazon.es"))    return new AmazonOrderES(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("Amazon.com.mx"))    return new AmazonOrderMX(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("eBay.de"))      return new EbayOrder(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("eBay.it"))      return new EbayOrder(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("eBay.co.uk"))   return new EbayOrder(id, transactionDate,type,source,sourceId,sku,description);
                if(source.equals("eBay.ca"))   return new EbayOrder(id, transactionDate,type,source,sourceId,sku,description);
            }
            else if(type.equals(AmazonTransactionType.OTHER.getValue())){
                if(description.equals("WAREHOUSE_LOST"))         return new FbaLostDamage(id, transactionDate,type,source,sourceId,sku,description);
                if(description.equals("WAREHOUSE_LOST_MANUAL"))         return new FbaLostDamage(id, transactionDate,type,source,sourceId,sku,description);
                if(description.equals("REMOVAL_ORDER_LOST"))         return new FbaLostDamage(id, transactionDate,type,source,sourceId,sku,description);
                if(description.equals("WAREHOUSE_DAMAGE"))       return new FbaLostDamage(id, transactionDate,type,source,sourceId,sku,description);
                if(description.equals("MISSING_FROM_INBOUND"))   return new FbaLostDamage(id, transactionDate,type,source,sourceId,sku,description);
                if(description.equals("FREE_REPLACEMENT_REFUND_ITEMS"))   return new FbaLostDamage(id, transactionDate,type,source,sourceId,sku,description);
                if(description.equals("REVERSAL_REIMBURSEMENT")) return new FbaReimbursement(id, transactionDate,type,source,sourceId,sku,description);
                if(description.equals("COMPENSATED_CLAWBACK")) return new AmazonCompensatedClawback(id, transactionDate,type,source,sourceId,sku,description);
                if(description.equals("MISSING_FROM_INBOUND_CLAWBACK")) return new AmazonCompensatedClawback(id, transactionDate,type,source,sourceId,sku,description);

            }
            else if(type.equals(AmazonTransactionType.REFUND.getValue())) return new AmazonRefund(id, transactionDate,type,source,sourceId,sku,description);
            else if(type.equals(AmazonTransactionType.ADJUSTMENT_REFUND.getValue()))
                return new AmazonAdjustmentRefund(id, transactionDate,type,source,sourceId.trim(),sku.trim(),description);
            else if(type.equals("FBA Return To Sellable"))
                return new FbaReturnToSellable(id, transactionDate,type,source,sourceId,sku,description);
            else if(DrsTransaction.DrsTransactionType.containsType(type)){
                return new FbaReturnToSupplier(id, transactionDate,type,source,sourceId,sku,description);
            }


        }catch (Exception ex){

        }


       // Assert.isTrue(false,"no concrete transaction");
        return null;


    }


    private List<MarketSideTransaction> findAmazonOrderAndRefunds(Date startPoint,Date endPoint,
                                                                  String companyCode) {
        List<Object []> columnsList = this.dao.queryAmazonOrdersRefunds(
                startPoint,endPoint,ignoreMerchantOrderIdPrefixs, companyCode);

        List<MarketSideTransaction> amazonOrderRefunds = new ArrayList<>();
        for(Object[] columns:columnsList){
            Date transactionDate = (Date)columns[0];
            String type = (String)columns[1];
            String source = (String)columns[2];
            String sourceId = (String)columns[3];
            String sku = (String)columns[4];

            amazonOrderRefunds.add(new RawMarketSideTransaction(0, transactionDate,
                    type, source, sourceId, sku));

        }

        return amazonOrderRefunds;
    }


    private List<MarketSideTransaction> findAmazonOtherTransactions(Date startPoint, Date endPoint,
                                                                    String companyCode) {

        List<Object[]> columnsList = this.dao.queryAmazonOtherTransactions(startPoint,endPoint,
                ignoreMerchantOrderIdPrefixs, companyCode);
        List<MarketSideTransaction> amazonOtherTransactions = new ArrayList<>();

        for(Object[] columns:columnsList){
            Date transactionDate = (Date)columns[0];
            String type = (String)columns[1];
            String source = (String)columns[2];
            String sourceId = (String)columns[3];
            String sku = (String)columns[4];
            String description = (String)columns[5];

            amazonOtherTransactions.add(new RawMarketSideTransaction(
                    0, transactionDate, type, source, sourceId, sku,description));
        }
        return amazonOtherTransactions;
    }

    private List<MarketSideTransaction> findShopifyOrderTransactions(Date startPoint,Date endPoint,
                                                                     String companyCode) {
        Marketplace marketplace = Marketplace.TRUETOSOURCE;
        String type = "Order";

        List<Object[]> columnsList = this.dao.queryShopifyOrderIdToTransactionDateMap(marketplace,type,
                startPoint,endPoint, companyCode);

        List<MarketSideTransaction> resultList = new ArrayList<>();
        for(Object[] columns:columnsList){
            Date transactionDate = (Date)columns[0];
            String sourceId = (String)columns[1];
            String sku = (String)columns[2];
            resultList.add(new RawMarketSideTransaction(0,transactionDate,type,marketplace.getName(),sourceId,sku));
        }

        return resultList;
    }

    private List<MarketSideTransaction> findEbayTransactions(Date startPoint, Date endPoint,
                                                             String companyCode) {

        List<Object []> columnsList =  this.dao.queryEbayTransactionInfos(startPoint, endPoint,
                "Order", companyCode);

        List<MarketSideTransaction> ebayTransactions = new ArrayList<>();
        for(Object[] columns:columnsList){
            Date date = (Date)columns[0];
            String source = (String)columns[1];
            String sourceId = (String)columns[2];
            String sku = (String)columns[3];
            ebayTransactions.add(new RawMarketSideTransaction(null,date,"Order",source,sourceId,sku));
        }

        return ebayTransactions;

    }

    private List<MarketSideTransaction> findReturnToSellableTransactionsFromFba(Date startPoint,
                                                                                Date endPoint,String companyCode){

        String type = "FBA Return To Sellable";
        Settlement.AmazonReturnReportDetailedDisposition targetDisposition = Settlement.AmazonReturnReportDetailedDisposition.SELLABLE;
        Settlement.AmazonReturnReportLineStatus targetStatus = Settlement.AmazonReturnReportLineStatus.RETURNED;
        List<Object []>  columnsList = this.dao.queryFbaTransactions(startPoint,endPoint,type,
                targetDisposition,targetStatus, companyCode);

        List<MarketSideTransaction> resultList = new ArrayList<>();
        for(Object[] columns:columnsList){
            Date transactionDate = (Date)columns[0];
            String source = (String)columns[1];
            String sourceId = (String)columns[2];
            String sku = (String)columns[3];
            resultList.add(new RawMarketSideTransaction(0,transactionDate,type,source,sourceId,sku));
        }
        return resultList;
    }

    private List<MarketSideTransaction> findFbaReturnToSupplierTransactions(Date startPoint,Date endPoint,
                                                                            String companyCode) {

        List<Object []> columnsList =  this.dao.queryFbaReturnToSupplierTransactions(startPoint,endPoint, companyCode);

        List<MarketSideTransaction> fbaReturnToSupplierTransactions = new ArrayList<>();
        for(Object[] columns:columnsList){
            Date date = (Date)columns[0];
            String source = (String)columns[1];
            String sku = (String)columns[2];
            String sellbackType = (String)columns[3];
            if ("TAIWAN".equalsIgnoreCase(sellbackType)) {
                sellbackType = DrsTransaction.DrsTransactionType.FBA_RETURNS_TAIWAN.getTextValue();
            } else if ("OTHER".equalsIgnoreCase(sellbackType)) {
                sellbackType = DrsTransaction.DrsTransactionType.FBA_RETURNS_OTHER.getTextValue();
            } else if ("DISPOSE".equalsIgnoreCase(sellbackType)) {
                sellbackType = DrsTransaction.DrsTransactionType.FBA_RETURNS_DISPOSE.getTextValue();
            } else if ("RECOVERY".equalsIgnoreCase(sellbackType)) {
                sellbackType = DrsTransaction.DrsTransactionType.FBA_RETURNS_RECOVERY.getTextValue();
            } else {
                sellbackType = DrsTransaction.DrsTransactionType.FBA_RETURN_TO_SUPPLIER.getTextValue();
            }

            fbaReturnToSupplierTransactions.add(new RawMarketSideTransaction(null, date,sellbackType,source,null,sku));
        }

        return fbaReturnToSupplierTransactions;
    }

    private void sortTransactionListByDateAsc(List<MarketSideTransaction> transactionList){
        Collections.sort(
                transactionList,
                new Comparator<MarketSideTransaction>(){
                    public int compare(MarketSideTransaction e1,MarketSideTransaction e2){ return e1.getTransactionDate().compareTo(e2.getTransactionDate()); }
                }
        );
    }
}
