package persist.traditional.dao;

import com.kindminds.drs.api.data.access.usecase.accounting.ProcessCouponRedemptionDao;
import com.kindminds.drs.api.v1.model.coupon.CouponRedemption;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestProcessCouponRedemptionDaoImpl {

    @Autowired
    ProcessCouponRedemptionDao processCouponRedemptionDao;

    @Test
    public void testGetUnprocessedCouponRedemotionFees() {
        List<CouponRedemption> couponRedemptions = processCouponRedemptionDao.getUnprocessedCouponRedemptionFees(71);
        if(couponRedemptions != null)
            assert true;
    }

    @Test
    public void testUpdateUnprocessedCouponRedemotionFees() {
        Boolean successful = processCouponRedemptionDao.updateProcessedCouponRedemptionFee(10, true, null);
        if(successful instanceof Boolean)
            assert true;
    }

    @Test
    public void testGetFailedCouponRedemotionFees() {
        List<CouponRedemption> couponRedemptions = processCouponRedemptionDao.getFailedCouponRedemptionFees();
        if(couponRedemptions != null)
            assert true;
    }
    
}
