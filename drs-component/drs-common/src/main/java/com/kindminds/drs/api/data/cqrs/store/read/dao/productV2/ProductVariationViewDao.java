package com.kindminds.drs.api.data.cqrs.store.read.dao.productV2;

import java.util.List;

public interface ProductVariationViewDao {


    List<Object[]> findView(String id);

}
