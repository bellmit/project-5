package persist.traditional.dao;

import com.kindminds.drs.api.data.access.usecase.MaintainCompanyDao;
import com.kindminds.drs.persist.v1.model.mapping.CompanyImpl;
import org.apache.commons.lang.RandomStringUtils;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestMaintainCompanyDaoImpl {

    @Autowired
    MaintainCompanyDao maintainCompanyDao;

    @Test
    public void testIsCouponUnique_true() {
        String coupon = randomString();
        String kcode = "K101";
        Boolean isUnique = maintainCompanyDao.isCouponUnique(coupon,kcode);
        if(isUnique)
            assert true;
        else
            assert false;
    }

    @Test
    public void testIsCouponUnique_false() {
        String coupon = randomString();
        String kcode = "K101";
        CompanyImpl company = (CompanyImpl)maintainCompanyDao.query("K101");
        List<String> originalCoupons = company.getCouponList();
        List<String> couponList = new ArrayList<>();
        couponList.add(coupon);
        company.setCouponList(couponList);
        maintainCompanyDao.updateInfo(company);

        Boolean isUnique = maintainCompanyDao.isCouponUnique(coupon,kcode);

        if(!isUnique)
            assert true;
        else
            assert false;

        // Revert changes in database
        company.setCouponList(originalCoupons);
        maintainCompanyDao.updateInfo(company);
    }

    //@Rule
    //public ExpectedException thrown = ExpectedException.none();

    @Test()
    public void testCouponUniqueConstraint_CaseInsensitive() {
        String coupon = randomString();

        CompanyImpl company = (CompanyImpl)maintainCompanyDao.query("K101");
        List<String> originalCoupons = company.getCouponList();
        List<String> couponList = new ArrayList<>();
        couponList.add(coupon.toLowerCase());
        company.setCouponList(couponList);
        maintainCompanyDao.updateInfo(company);
        couponList.add(coupon.toUpperCase());
        company.setCouponList(couponList);

       // thrown.expectCause(new ExceptionChainMatchUtil("ERROR: duplicate key value violates unique constraint \"company_coupon_uni\"", ConstraintViolationException.class, PSQLException.class));

        try {
            maintainCompanyDao.updateInfo(company);
            assert false;
        }
        finally {
            // Revert changes in database
            company.setCouponList(originalCoupons);
            maintainCompanyDao.updateInfo(company);
        }
    }

    private String randomString() {
        String generatedString = RandomStringUtils.randomAlphanumeric(10);

        return generatedString;
    }


}
