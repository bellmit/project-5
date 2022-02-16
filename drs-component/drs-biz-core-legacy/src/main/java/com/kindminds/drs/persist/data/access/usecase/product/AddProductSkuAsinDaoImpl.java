package com.kindminds.drs.persist.data.access.usecase.product;

import java.util.List;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.product.AddProductSkuAsinDao;

@Repository
public class AddProductSkuAsinDaoImpl extends Dao implements AddProductSkuAsinDao {
	


	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySkuAsins(String splrKcode, int marketplaceId) {
		String sql = "select ps.code_by_drs, pmi.marketplace_sku, pmia.asin, pmia.fnsku, pmia.afn_listing_exists, pmia.mfn_listing_exists, pmia.storage_fee_flag "
				+ "from product_marketplace_info_amazon pmia "
				+ "inner join product_marketplace_info pmi on pmia.product_marketplace_info_id = pmi.id "
				+ "inner join product_sku ps on pmi.product_id = ps.product_id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where pmi.marketplace_id = :marketplaceId "
				+ "and splr.k_code = :splrKcode "
				+ "order by pmi.marketplace_sku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("splrKcode", splrKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryMarketplaceSkuWithoutAsin() {
		String sql = "select m.name,ps.code_by_drs,pmia.asin from product_sku ps "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join product_marketplace_info pmi on pmi.product_id = p.id "
				+ "inner join marketplace m on m.id = pmi.marketplace_id "
				+ "left join product_marketplace_info_amazon pmia on pmia.product_marketplace_info_id = pmi.id "
				+ "where pmi.marketplace_id in (1,4,5,6,7,8,9) "
				+ "and asin is null "
				+ "order by m.id,ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}
	
	@Override
	public boolean queryMarketplaceSkuAsinExist(Marketplace marketplace, String drsSku, String asin) {
		String sql = "select exists ( "
				+ "    select 1 from product_marketplace_info_amazon pmia "
				+ "    inner join product_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id "
				+ "    inner join product_sku ps on ps.product_id = pmi.product_id "
				+ "    where pmi.marketplace_id = :marketplaceId "
				+ "    and ps.code_by_drs = :drsSku "
				+ "    and pmia.asin = :asin "
				+ ") ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplace.getKey());
		q.addValue("drsSku", drsSku);
		q.addValue("asin", asin);
		Boolean result = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		Assert.notNull(result);
		return result;
	}

	@Override @Transactional("transactionManager")
	public void insertSkuAsin(Marketplace marketplace, String drsSku, String asin) {
		String sql = "insert into product_marketplace_info_amazon (product_marketplace_info_id,  asin) "
				+ "select                                                               pmi.id, :asin "
				+ "from product_marketplace_info pmi "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "where ps.code_by_drs = :drsSku "
				+ "and pmi.marketplace_id = :marketplaceId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplace.getKey());
		q.addValue("drsSku", drsSku);
		q.addValue("asin", asin);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override
	public boolean queryMarketplaceSkuAsinFnskuExist(int marketplaceId, String marketplaceSku, String asin, String fnsku) {
		String sql = "select exists ( "
				+ "    select 1 from product_marketplace_info_amazon pmia "
				+ "    inner join product_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id "
				+ "    where pmi.marketplace_id = :marketplaceId "
				+ "    and pmi.marketplace_sku = :marketplaceSku "
				+ "    and pmia.asin = :asin "
				+ "    and pmia.fnsku = :fnsku "
				+ ") ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("marketplaceSku", marketplaceSku);
		q.addValue("asin", asin);
		q.addValue("fnsku", fnsku);
		Boolean result = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		Assert.notNull(result);
		return result;
	}

	@Override @Transactional("transactionManager")
	public int insertFbaRecord (int marketplaceId, String marketplaceSku,
									 String asin, String fnsku, String afn, String mfn) {

		String qSql = "select exists (select 1 from product_marketplace_info pmi "
				+ "where pmi.marketplace_sku = :marketplaceSku "
				+ "and pmi.marketplace_id = :marketplaceId) ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("marketplaceSku", marketplaceSku);
		Boolean nonK101 = getNamedParameterJdbcTemplate().queryForObject(qSql,q,Boolean.class);

		String sql ="";
		if(nonK101){
			sql = "insert into product_marketplace_info_amazon "
					+ "(product_marketplace_info_id,  asin, fnsku, afn_listing_exists, mfn_listing_exists) "
					+ "select     pmi.id, :asin, :fnsku, :afn\\:\\:boolean, :mfn\\:\\:boolean "
					+ "from product_marketplace_info pmi "
					+ "where pmi.marketplace_sku = :marketplaceSku "
					+ "and pmi.marketplace_id = :marketplaceId ";

		}else{

			sql = "insert into product_k101_marketplace_info_amazon "
					+ "(product_marketplace_info_id,  asin, fnsku, afn_listing_exists, mfn_listing_exists) "
					+ "select  pmi.id, :asin, :fnsku, :afn\\:\\:boolean, :mfn\\:\\:boolean "
					+ "from product_k101_marketplace_info pmi "
					+ "where pmi.marketplace_sku = :marketplaceSku "
					+ "and pmi.marketplace_id = :marketplaceId ";
		}


		q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("marketplaceSku", marketplaceSku);
		q.addValue("asin", asin);
		q.addValue("fnsku", fnsku);
		q.addValue("afn", afn);
		q.addValue("mfn", mfn);
		int inserted = getNamedParameterJdbcTemplate().update(sql,q);

		//Assert.isTrue(inserted==1);
		return inserted;
	}

	@Override @Transactional("transactionManager")
	public int updateFbaRecord (int marketplaceId, String marketplaceSku, String asin, String fnsku, String afn, String mfn) {

		String sql ="";
		if(this.queryK101PmiIdExist(marketplaceId,marketplaceSku)){

			sql = "UPDATE product_k101_marketplace_info_amazon pmia SET fnsku = :fnsku, asin = :asin, " +
					" afn_listing_exists = :afn\\:\\:boolean, mfn_listing_exists = :mfn\\:\\:boolean," +
					" last_updated_on = current_timestamp" +
					" FROM   product_k101_marketplace_info pmi " +
					" WHERE  product_marketplace_info_id = pmi.id " +
					"	   AND pmi.marketplace_sku = :marketplaceSku " +
					"	   AND pmi.marketplace_id = :marketplaceId";


		}else{

				sql = "UPDATE product_marketplace_info_amazon pmia SET fnsku = :fnsku, asin = :asin, " +
					" afn_listing_exists = :afn\\:\\:boolean, mfn_listing_exists = :mfn\\:\\:boolean," +
					" last_updated_on = current_timestamp" +
					" FROM   product_marketplace_info pmi " +
					" WHERE  product_marketplace_info_id = pmi.id " +
					"	   AND pmi.marketplace_sku = :marketplaceSku " +
					"	   AND pmi.marketplace_id = :marketplaceId";

		}


		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("marketplaceSku", marketplaceSku);
		q.addValue("asin", asin);
		q.addValue("fnsku", fnsku);
		q.addValue("afn", afn);
		q.addValue("mfn", mfn);
		int updated = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updated==1);
		return updated;
	}
	
	@Override
	public boolean queryPmiIdExist(int marketplaceId, String marketplaceSku) {

		return  this.doqPmiIdExist(marketplaceId,marketplaceSku);
	}


	private boolean doqPmiIdExist(int marketplaceId, String marketplaceSku) {

		String sql = "select exists ( "
				+ "    select 1 from pv.product_all_marketplace_info_amazon pmia "
				+ "    inner join pv.product_all_marketplace_info pmi " +
				"   on pmi.id = pmia.product_marketplace_info_id and pmia.supplier = pmi.supplier"
				+ "    where pmi.marketplace_id = :marketplaceId "
				+ "    and pmi.marketplace_sku = :marketplaceSku "
				+ ") ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("marketplaceSku", marketplaceSku);
		Boolean result = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);

		Assert.notNull(result);
		return  result ;
	}


	private boolean queryK101PmiIdExist(int marketplaceId, String marketplaceSku) {

		String sql = " select pmia.supplier from pv.product_all_marketplace_info_amazon pmia "
				+ "    inner join pv.product_all_marketplace_info pmi " +
				"   on pmi.id = pmia.product_marketplace_info_id and pmia.supplier = pmi.supplier"
				+ "    where pmi.marketplace_id = :marketplaceId "
				+ "    and pmi.marketplace_sku = :marketplaceSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("marketplaceSku", marketplaceSku);
		List<String> columnsList = getNamedParameterJdbcTemplate().queryForList(sql,q , String.class);


		return  columnsList.get(0).equals("k101") ? true : false ;


	}
	
	@Override
	public boolean fnskuIsUpdated(int marketplaceId, String marketplaceSku) {

		String sql = "select fnsku from product_marketplace_info_amazon pmia "
				+ "    inner join product_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id "
				+ "    where pmi.marketplace_id = :marketplaceId "
				+ "    and pmi.marketplace_sku = :marketplaceSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("marketplaceSku", marketplaceSku);
		if (getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class) == null) {
			return false;
		}
		return true;
	}

	@Override @Transactional("transactionManager")
	public int insertInventoryRecord (int marketplaceId, String marketplaceSku, String asin) {

		String qSql = "select exists (select 1 from product_marketplace_info pmi "
				+ "where pmi.marketplace_sku = :marketplaceSku "
				+ "and pmi.marketplace_id = :marketplaceId) ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("marketplaceSku", marketplaceSku);
		Boolean nonK101 = getNamedParameterJdbcTemplate().queryForObject(qSql,q,Boolean.class);


		String sql ="";
		if(nonK101){

			sql = "insert into product_marketplace_info_amazon "
					+ "(product_marketplace_info_id, asin) "
					+ "select     pmi.id, :asin "
					+ "from product_marketplace_info pmi "
					+ "where pmi.marketplace_sku = :marketplaceSku "
					+ "and pmi.marketplace_id = :marketplaceId ";

		}else{

			sql = "insert into product_k101_marketplace_info_amazon "
					+ "(product_marketplace_info_id, asin) "
					+ "select     pmi.id, :asin "
					+ "from product_k101_marketplace_info pmi "
					+ "where pmi.marketplace_sku = :marketplaceSku "
					+ "and pmi.marketplace_id = :marketplaceId ";
		}


		q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("marketplaceSku", marketplaceSku);
		q.addValue("asin", asin);
		int updated = getNamedParameterJdbcTemplate().update(sql,q);
		//Assert.isTrue(inserted==1);
		return updated;
	}

	@Override @Transactional("transactionManager")
	public void toggleStorageFeeFlag(int marketplaceId, String marketplaceSku) {
		StringBuilder sqlSb = new StringBuilder()
				.append("update product_marketplace_info_amazon pmia ")
				.append("set storage_fee_flag = NOT storage_fee_flag ")
				.append("FROM   product_marketplace_info pmi ")
				.append("WHERE  product_marketplace_info_id = pmi.id ")
				.append("AND pmi.marketplace_sku = :marketplaceSku ")
				.append("AND pmi.marketplace_id = :marketplaceId ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceSku", marketplaceSku);
		q.addValue("marketplaceId", marketplaceId);
		int updated = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
		Assert.isTrue(updated==1);
	}

}
