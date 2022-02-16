package com.kindminds.drs.product;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductViewQueries;
import com.kindminds.drs.api.data.cqrs.store.read.queries.QuoteRequestViewQueries;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.ProductRepo;
import com.kindminds.drs.core.biz.repo.product.ProductRepoImpl;
import com.kindminds.drs.persist.data.access.nosql.mongo.DrsMongoClient;
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MApplicationDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MProductDaoImpl;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.RegionalDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.ShippingDaoImpl;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.codehaus.plexus.util.ExceptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestP2Mdao {

	private P2MApplicationDao p2MApplicationDao = new P2MApplicationDao();

	@Test
	public void testp2m() {
		MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
				.getCollection("p2mApplication");
		long size = 0;
		String supplierId = "K2";
		String country = "US";
		String status = "";
		String kcode = "K612";
		String product = "";
		size = p2mApplication.countDocuments(and(getResult("supplierId",kcode),getResult("selectedCountry",country),getResult("selectedProduct",product),getResult("status",status)));
		System.out.println(size);
	}
	private Bson getResult(String r,String re){
		if(re.equals("")) {
			return ne(r,re);
		} else{
			return eq(r,re);
		}
	}
}