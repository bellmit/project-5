package com.kindminds.drs.service.usecase.marketing;

import com.kindminds.drs.api.usecase.ViewExternalMarketingActivityUco;
import com.kindminds.drs.v1.model.impl.marketing.ExternalMarketingActivityItemImpl;
import com.kindminds.drs.v1.model.impl.marketing.ExternalMarketingDataImpl;
import com.kindminds.drs.v1.model.impl.marketing.ExternalMarketingSalesItemImpl;
import com.kindminds.drs.v1.model.impl.marketing.ExternalMarketingSkuItemImpl;
import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingActivityItem;
import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingData;
import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingSalesItem;
import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingSkuItem;
import com.kindminds.drs.api.data.access.usecase.marketing.ViewExternalMarketingActivityDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ViewExternalMarketingActivityUcoImpl implements ViewExternalMarketingActivityUco {

    @Autowired
    private ViewExternalMarketingActivityDao dao;

    public List<ExternalMarketingSkuItem> getListOfSkus() {
        List<Object []> columnsList =  dao.querySkuList();

        List<ExternalMarketingSkuItem> resultList = new ArrayList<>();
        for (Object[] item : columnsList) {
            resultList.add(new ExternalMarketingSkuItemImpl(
                    (String)item[0], (String)item[1], (String)item[2]));
        }

        return  resultList;
    }

    public ExternalMarketingData getMarketingData(String kCode, String skuCode, String marketplace) {
        List<Object []> columnList  = dao.querySalesData(marketplace, skuCode);

        List<ExternalMarketingSalesItem> salesData = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Object[] item  : columnList) {
            salesData.add(new ExternalMarketingSalesItemImpl(
                    sdf.format((Date)item[0]), (BigDecimal)item[1]));
        }


        List<Object []> colList2  = dao.queryActivityData(marketplace, skuCode);

        List<ExternalMarketingActivityItem> activityData = new ArrayList<>();

        for (Object[] item :colList2 ) {
            activityData.add(new ExternalMarketingActivityItemImpl(
                    (String)item[0], sdf.format((Date)item[1]), (String)item[2]));
        }

        List<Object []> colList3  = dao.queryAmazonAdData(marketplace, skuCode);
        for (Object[] item : colList3) {
            activityData.add(new ExternalMarketingActivityItemImpl(
                    (String)item[0], sdf.format((Date)item[1])));
        }


        return new ExternalMarketingDataImpl(
                kCode, skuCode, marketplace, salesData, activityData);
    }
}
