package com.kindminds.drs.api.usecase;

import java.util.List;

import com.kindminds.drs.api.v1.model.accounting.ServiceItem;

public interface ManageServiceItemController {
	
	public void addServiceItem(ServiceItem serviceItem);
	public void updateServiceItem(ServiceItem serviceItem);	
	public List<ServiceItem> listServiceItems();
	public ServiceItem getServiceItemById(int id);
	public void removeServiceItem(int id);
						
}
