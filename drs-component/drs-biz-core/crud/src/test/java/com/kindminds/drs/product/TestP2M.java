package com.kindminds.drs.product;

import com.kindminds.drs.Country;
import com.kindminds.drs.core.biz.repo.product.ProductRepoImpl;
import com.kindminds.drs.data.access.nosql.mongo.ProductDao;
import com.kindminds.drs.data.access.nosql.mongo.p2m.P2MApplicationDao;
import com.kindminds.drs.data.access.rdb.CompanyDao;
import com.kindminds.drs.data.cqrs.store.read.queries.ProductViewQueries;
import com.kindminds.drs.data.cqrs.store.read.queries.QuoteRequestViewQueries;
import com.kindminds.drs.data.transfer.productV2.ProductDto;
import com.kindminds.drs.service.util.Encrypter;
import com.kindminds.drs.v2.biz.domain.model.product.Product;
import com.kindminds.drs.v2.biz.domain.model.repo.product.ProductRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestP2M {


	@Test
	public void test(){


		System.out.println(com.kindminds.drs.util.Encryptor.encrypt("6092552952cdde3e172d5767" ));


	}

}
