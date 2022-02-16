package com.kindminds.drs.service.usecase.logistics;

import com.kindminds.drs.api.usecase.logistics.MaintainReplenishmentTimeSpentInfoUco;
import com.kindminds.drs.v1.model.impl.logistics.ReplenishmentTimeSpentInfoImpl;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpentInfo;
import com.kindminds.drs.api.data.access.usecase.logistics.MaintainReplenishmentTimeSpentInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaintainReplenishmentTimeSpentInfoUcoImpl implements MaintainReplenishmentTimeSpentInfoUco {
	
	@Autowired private MaintainReplenishmentTimeSpentInfoDao dao;

	@Override
	public List<ReplenishmentTimeSpentInfo> getList() {
        List<Object []> columnsList = this.dao.queryList();
        List<ReplenishmentTimeSpentInfo> resultList = new ArrayList<>();
        for(Object[] columns:columnsList){
            int warehouseId = (int)columns[0];
            String warehouseName = (String)columns[1];
            Integer daysSpentForAmazonReceiving = (Integer)columns[2];
            Integer daysSpentForSpwCalculation = (Integer)columns[3];
            Integer daysSpentForCourier = (Integer)columns[4];
            Integer daysSpentForAirFreight = (Integer)columns[5];
            Integer daysSpentForSurfaceFreight = (Integer)columns[6];
            resultList.add(new ReplenishmentTimeSpentInfoImpl(warehouseId, warehouseName, daysSpentForAmazonReceiving, daysSpentForSpwCalculation, daysSpentForCourier, daysSpentForAirFreight, daysSpentForSurfaceFreight));
        }
        return resultList;

	}

	@Override
	public void save(List<ReplenishmentTimeSpentInfo> infoList) {
		this.dao.update(infoList);
	}

}
