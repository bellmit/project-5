package com.kindminds.drs.api.data.cqrs.store.event.dao.productV2;


import com.kindminds.drs.Country;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;


import java.util.List;
import java.util.Optional;

public interface ProductDao {

    String save(Product product);

    Optional<String> getId(String baseCode, Country marketSide);

    List<Object[]> get(String id , Country marketSide);

    List<Object[]> get(String id);


}
