package com.kindminds.drs.api.data.access.usecase.logistics;


import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpentInfo;

import java.util.List;



public interface MaintainReplenishmentTimeSpentInfoDao {
	List<Object []> queryList();
	void update(List<ReplenishmentTimeSpentInfo> infoList);
}
