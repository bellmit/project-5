package com.kindminds.drs.api.data.access.rdb;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.accounting.SupplierLongTermStorageFee;

public interface EmailReminderDao {

	int updateLongTermStorageReminder(List<String> k_codes);

	Map<String, String> queryExcludedLongTermFeeReminder();

	Map<String, String> queryIncludedLongTermFeeReminder();

	List<SupplierLongTermStorageFee> querySuppliersOverFeeLimit(List<String> k_codes, Double limit);

	Double queryFeeToSendReminder();

	int updateFeeToSendReminder(Double limit);

}
