package com.kindminds.drs.api.usecase.logistics;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpentInfo;

import java.util.List;

public interface MaintainReplenishmentTimeSpentInfoUco {
	List<ReplenishmentTimeSpentInfo> getList();
	void save(List<ReplenishmentTimeSpentInfo> infoList);

}
