package com.kindminds.drs.api.data.access.usecase;

import java.util.List;

import com.kindminds.drs.api.v1.model.accounting.ServiceItem;

public interface ManageServiceItemDao {
	
	public void addServiceItem(ServiceItem serviveItem);
	public void updateServiceItem(ServiceItem serviveItem);
	public List<ServiceItem> listServiceItems();
	public ServiceItem getServiceItemById(int id);
    public void removeServiceItem(int id);
	
}