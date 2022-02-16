package com.kindminds.drs.api.data.access.rdb.accounting;

import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;

import java.util.List;

public interface DrsTransactionDao {
	public DrsTransaction query(int dtId);
	public DrsTransaction query(int seq,String sourceId,String sku);
	public List<DrsTransaction> query(String sourceId, String sku);
	public int insert(DrsTransaction dt);
}

