package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.api.usecase.ProcessCouponRedemptionUco;
import com.kindminds.drs.api.usecase.accounting.MaintainInternationalTransactionUco;
import com.kindminds.drs.api.usecase.common.SettlementPeriodListUco;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;
import com.kindminds.drs.api.v1.model.coupon.CouponRedemption;
import com.kindminds.drs.api.v1.model.coupon.CouponRedemptionStatus;
import com.kindminds.drs.v1.model.impl.accounting.CouponRedemptionStatusImpl;
import com.kindminds.drs.v1.model.impl.accounting.InternationalTransactionImpl;
import com.kindminds.drs.v1.model.impl.accounting.InternationalTransactionLineItemImpl;
import com.kindminds.drs.api.v1.model.message.MessageCode;
import com.kindminds.drs.api.data.access.usecase.accounting.ProcessCouponRedemptionDao;
import com.kindminds.drs.persist.v1.model.mapping.accounting.CouponRedemptionImpl;

import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProcessCouponRedemptionUcoImpl implements ProcessCouponRedemptionUco {

    @Autowired
    private ProcessCouponRedemptionDao processCouponRedemptionDao;
    @Autowired
    private MaintainInternationalTransactionUco internationalTransactionUco;
    @Autowired
    private SettlementPeriodListUco settlementPeriodListUco;

    @Override
    public CouponRedemptionStatus processCouponRedemptionFees() {
        List<CouponRedemption> couponRedemptions = processCouponRedemptionDao.getUnprocessedCouponRedemptionFees();
        return processCouponRedemptions(couponRedemptions);
    }

    @Override
    public CouponRedemptionStatus processCouponRedemptionFees(Integer periodId) {
        List<CouponRedemption> couponRedemptions = processCouponRedemptionDao.getUnprocessedCouponRedemptionFees(periodId);
        return processCouponRedemptions(couponRedemptions);
    }

    @Override
    public MessageCode getCouponProcessStatus() {
        List<CouponRedemption> couponRedemptions = processCouponRedemptionDao.getUnprocessedCouponRedemptionFees();
        if(couponRedemptions != null && !couponRedemptions.isEmpty())
            return MessageCode.COUPONS_ELIGIBLE_TO_PROCESS;
        else if (internationalTransactionUco.countCouponTransactionsProcessed() > 0)
            return MessageCode.COUPONS_ALREADY_PROCESSED;
        else
            return MessageCode.COUPONS_NONE_TO_PROCESS;
    }

    @Override
    public List<CouponRedemption> getFailedCouponRedemptionFees() {
        return processCouponRedemptionDao.getFailedCouponRedemptionFees();
    }

    private CouponRedemptionStatus processCouponRedemptions(List<CouponRedemption> couponRedemptions) {
        String supplierKcode = "";
        String mmsdKcode = "";
        InternationalTransactionImpl internationalTransaction = null;
        List<Integer> amazonSettlementIds = null;
        CouponRedemptionStatusImpl couponRedemptionStatus = new CouponRedemptionStatusImpl();
        List<CouponRedemption> unprocessedCouponRedemptions = new ArrayList<CouponRedemption>();

        HashMap<List<Integer>, InternationalTransaction> internationalTransactionsToCreate = new HashMap<List<Integer>, InternationalTransaction>();

        if(couponRedemptions != null && !couponRedemptions.isEmpty()) {
            Date transactionDate = subtractOneDay(settlementPeriodListUco.getLatestSettlementPeriod().getEndPoint());
            for (CouponRedemption couponRedemption : couponRedemptions) {
                Date postedDateTime  = DateHelper.toDate(couponRedemption.getTransactionTime() + " UTC", "yyyy-MM-dd HH:mm:ss z");
                if (postedDateTime.before(this.settlementPeriodListUco.getLastSettlementEnd())){
                    ((CouponRedemptionImpl)couponRedemption).setReason("TRANSACTION DATE BEFORE LAST SETTLEMENT END DATE");
                    unprocessedCouponRedemptions.add(couponRedemption);
                    continue;
                }
                // If unable to link coupon to company
                if (couponRedemption.getSupplierId() == null) {
                    ((CouponRedemptionImpl)couponRedemption).setReason("UNABLE TO MATCH COUPON TO SUPPLIER");
                    unprocessedCouponRedemptions.add(couponRedemption);
                    continue;
                }
//                System.out.println("supplierKcode: " + supplierKcode);
//                System.out.println("mmsdKcode: " + mmsdKcode);
//                System.out.println("couponRedemption: " + couponRedemption);

                if (!supplierKcode.equals(couponRedemption.getSupplierKcode()) || !mmsdKcode.equals(couponRedemption.getMsdcKcode())) {
                    internationalTransaction = new InternationalTransactionImpl();
                    amazonSettlementIds = new ArrayList<Integer>();
                    internationalTransactionsToCreate.put(amazonSettlementIds, internationalTransaction);
                    internationalTransaction.setTransactionDate(transactionDate);
                    internationalTransaction.setSsdcKcode("K2");
                    mmsdKcode = couponRedemption.getMsdcKcode();
                    internationalTransaction.setMsdcKcode(mmsdKcode);
                    supplierKcode = couponRedemption.getSupplierKcode();
                    internationalTransaction.setSplrKcode(supplierKcode);
                    internationalTransaction.setCurrencyId(Integer.valueOf(couponRedemption.getCurrencyId()));
                    internationalTransaction.setCashFlowDirectionKey(0);
                    internationalTransaction.setNote(couponRedemption.getPeriodStartUtc() + "-" + DateHelper.toString(transactionDate, "yyyyMMdd") + " Coupon Redemption Fee");
                }
                amazonSettlementIds.add(couponRedemption.getId());
                List<InternationalTransaction.InternationalTransactionLineItem> internationalTransactionLineItems =
                        ((internationalTransaction.getLineItems() != null ? internationalTransaction.getLineItems() : new ArrayList<>()));
                InternationalTransactionLineItemImpl internationalTransactionLineItem = new InternationalTransactionLineItemImpl();
                internationalTransactionLineItem.setCurrencyId(couponRedemption.getCurrencyId());
                internationalTransactionLineItem.setItemNote(couponRedemption.getPeriodStartUtc() + "-" + DateHelper.toString(transactionDate, "yyyyMMdd") + " Coupon Redemption Fee");
                // MARKET_SIDE_MARKETING_ACTIVITY
                internationalTransactionLineItem.setItemKey(76);
                BigDecimal total = couponRedemption.getAmount().multiply(new BigDecimal(-1));
                internationalTransactionLineItem.setSubtotal(total);
                internationalTransactionLineItem.setLineSeq(internationalTransactionLineItems.size() + 1);
                internationalTransactionLineItems.add(internationalTransactionLineItem);
                internationalTransaction.setLineItems(internationalTransactionLineItems);
            }
            ArrayList<Integer> addedInternationalTransactions = new ArrayList<Integer>();
            if (!internationalTransactionsToCreate.isEmpty()) {
                Iterator it = internationalTransactionsToCreate.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<List<Integer>, InternationalTransaction> pair = (Map.Entry) it.next();
                    boolean successful = addedInternationalTransactions.add(internationalTransactionUco.create(pair.getValue()));
                    if (successful) {
                        for (Integer id : pair.getKey())
                            processCouponRedemptionDao.updateProcessedCouponRedemptionFee(id, true, null);
                    }
                }
            }
            for(CouponRedemption couponRedemption : unprocessedCouponRedemptions) {
                processCouponRedemptionDao.updateProcessedCouponRedemptionFee(couponRedemption.getId(), false, couponRedemption.getReason());
            }
            List<InternationalTransaction> processedCouponRedemptions = internationalTransactionUco.get(addedInternationalTransactions);
            couponRedemptionStatus.setProcessedCouponRedemptions(processedCouponRedemptions);
            couponRedemptionStatus.setUnprocessedCouponRedemptions(unprocessedCouponRedemptions);
        }

        return couponRedemptionStatus;
    }

    private Date subtractOneDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}
