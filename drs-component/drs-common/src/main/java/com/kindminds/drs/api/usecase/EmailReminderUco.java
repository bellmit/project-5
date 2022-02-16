package com.kindminds.drs.api.usecase;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.accounting.SupplierLongTermStorageFee;

public interface EmailReminderUco {

	Map<String, String> getIncludedSuppliers();

	Map<String, String> getExcludedSuppliers();

	int updateLongTermStorageReminder(String kCodes);

	List<SupplierLongTermStorageFee> getSuppliersOverFeeLimit(String kCodes);

	Double getFeeToSendReminder();

	int updateFeeToSendReminder(Double limit);

	void sendLTSFReminderToCurrentUser(Integer currentUserID);

	void automateSendLTSFReminder();

}
