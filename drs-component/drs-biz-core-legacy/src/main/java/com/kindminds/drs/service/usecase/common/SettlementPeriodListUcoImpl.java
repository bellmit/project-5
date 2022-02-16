package com.kindminds.drs.service.usecase.common;

import com.kindminds.drs.api.usecase.common.SettlementPeriodListUco;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.data.access.rdb.common.SettlementPeriodListDao;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("settlementPeriodListUco")
public class SettlementPeriodListUcoImpl implements SettlementPeriodListUco {

    @Autowired
    private SettlementPeriodListDao settlementPeriodListDao;

    @Override
    public List<SettlementPeriod> getSettlementPeriodList() {
        List<Object [] > columnsList = this.settlementPeriodListDao.querySettlementPeriodList();

        List<SettlementPeriod> listToReturn = new ArrayList<SettlementPeriod>();
        for (Object[] columns : columnsList) {
            int id = (int) columns[0];
            Date start = (Date) columns[1];
            Date end = (Date) columns[2];
            listToReturn.add(new SettlementPeriodImpl(id, start, end));
        }

        return listToReturn;

    }

    @Override
    public Boolean isLatestSettlementPeriodSettled() {
        return this.settlementPeriodListDao.isLatestSettlementPeriodSettled();
    }

    @Override
    public SettlementPeriod getLatestSettlementPeriod() {
        return getSettlementPeriodList().get(0);
    }

    @Override
    public Date getLastSettlementEnd() {
        return settlementPeriodListDao.queryLastSettlementEnd();
    }
}
