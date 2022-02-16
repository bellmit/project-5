package com.kindminds.drs.api.data.access.rdb.customer;

import com.kindminds.drs.api.data.transfer.customer.Return;

import java.util.List;


public interface ReturnDao {

    List<Return> getReturns(String kcode , String marketPlace , String bpCode , String skuCode  );

}
