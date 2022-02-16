package com.kindminds.drs.persist.data.access.usecase.product;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;




import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.product.MaintainProductBaseDao;
import com.kindminds.drs.api.v1.model.product.BaseProduct;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductBaseImpl;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductSkuImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository
public class MaintainProductBaseDaoImpl extends Dao implements MaintainProductBaseDao {


	
	@Override @SuppressWarnings("unchecked")
	public BaseProduct query(String baseCodeByDrs) {
		String sql = "select     pb.id as id, "
				+ "        splr.k_code as supplier_company_kcode, "
				+ "            pc.name as category, "
				+ " p.code_by_supplier as code_by_supplier, "
				+ "     pb.code_by_drs as code_by_drs, "
				+ " p.name_by_supplier as name_by_supplier, "
				+ "   p.internal_notes as internal_notes "
				+ "from product_base pb "
				+ "inner join product p on p.id = pb.product_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "left join product_category pc on pc.id = pb.product_category_id "
				+ "where pb.code_by_drs = :baseCodeByDrs ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("baseCodeByDrs", baseCodeByDrs);


		List<ProductBaseImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ProductBaseImpl(
				rs.getInt("id"),rs.getString("supplier_company_kcode"),rs.getString("code_by_supplier"),
				rs.getString("category"),
				rs.getString("code_by_drs"),rs.getString("name_by_supplier"),rs.getString("internal_notes")
		));


		if(resultList.isEmpty()) return null;
		Assert.isTrue(resultList.size()==1);
		ProductBaseImpl base = resultList.get(0);
		base.setSkuList(this.queryProductSkuList(base.getCodeByDrs()));
	
		return base;
	}
	
	@Override @SuppressWarnings("rawtypes")
	public int queryCount(String supplierCompanyKcode) {
		String sql = "select count(1) from product_base pb "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where splr.is_supplier = TRUE ";
		if(supplierCompanyKcode!=null) sql += "and splr.k_code = :supplierCompanyKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		if(supplierCompanyKcode!=null) q.addValue("supplierCompanyKcode", supplierCompanyKcode);
		List<Integer> rList = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		return rList.get(0);
	}
	
	@Override
	public boolean isProductBaseSupplierCodeExist(String supplierKcode,String baseCodeBySupplier) {
		String sql = "select exists ( "
				+ "    select 1 from product_base pb "
				+ "    inner join company splr on splr.id = pb.supplier_company_id "
				+ "    inner join product p on p.id = pb.product_id "
				+ "    where splr.k_code = :supplierKcode "
				+ "    and p.code_by_supplier = :baseCodeBySupplier "
				+ ")";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode.trim());
		q.addValue("baseCodeBySupplier", baseCodeBySupplier.trim());
		Boolean o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		if (o == null) return false;
		return o;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<BaseProduct> queryListWithSku(int startIndex,int size, String supplierCompanyKcode) {
		String sql = "select pb.id as id, "
				+ "           NULL as supplier_company_kcode, "
				+ "           NULL as category, "
				+ "           NULL as code_by_supplier, "
				+ " pb.code_by_drs as code_by_drs, "
				+ "           NULL as name_by_supplier, "
				+ "           NULL as internal_notes "
				+ "from product_base pb "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where splr.is_supplier is TRUE ";
				if(supplierCompanyKcode!=null) sql += "and splr.k_code = :supplierCompanyKcode ";
				sql += "order by pb.code_by_drs limit :size offset :start ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("size", size);
		q.addValue("start", startIndex-1);
		if(supplierCompanyKcode!=null) q.addValue("supplierCompanyKcode", supplierCompanyKcode);

		List<ProductBaseImpl> resultBaseList = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ProductBaseImpl(
				rs.getInt("id"),rs.getString("supplier_company_kcode"),rs.getString("code_by_supplier"),
				rs.getString("category"),
				rs.getString("code_by_drs"),rs.getString("name_by_supplier"),rs.getString("internal_notes")
		));

		for(ProductBaseImpl base:resultBaseList){
			base.setSkuList(this.queryProductSkuList(base.getCodeByDrs()));
		}
		List<BaseProduct> listToReturn = new ArrayList<BaseProduct>();
		listToReturn.addAll(resultBaseList);
		return listToReturn;
	}
	
	@SuppressWarnings("unchecked")
	private List<SKU> queryProductSkuList(String baseCode){
		String sql = "select                 ps.id as id, "
				+ "                    splr.k_code as supplier_company_kcode, "
				+ "                 pb.code_by_drs as product_base_code, "
				+ "           pps.code_by_supplier as code_by_supplier, "
				+ "                 ps.code_by_drs as code_by_drs, "
				+ "           pps.name_by_supplier as name_by_supplier, "
				+ "                pps.name_by_drs as name_by_drs, "
				+ "                         ps.ean as ean, "
				+ "                ps.ean_provider as ean_provider, "
				+ "                       pss.name as status, "
				+ "ps.manufacturing_lead_time_days as manufacturing_lead_time_days, "
				+ " ps.contain_lithium_ion_battery as contain_lithium_ion_battery, "
				+ "             pps.internal_notes as internal_notes "
				+ "from product_sku ps "
				+ "inner join product pps on pps.id = ps.product_id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join product ppb on ppb.id = pb.product_id "
				+ "inner join product_sku_status pss on pss.id = ps.status_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where pb.code_by_drs = :baseCode order by ps.code_by_drs";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("baseCode", baseCode);


		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ProductSkuImpl(
				rs.getInt("id"),rs.getString("supplier_company_kcode"),
				rs.getString("product_base_code"),rs.getString("code_by_supplier"),rs.getString("code_by_drs"),
				rs.getString("name_by_supplier"),rs.getString("name_by_drs"),rs.getString("ean"),
				rs.getString("ean_provider"),rs.getString("status"),
				rs.getString("manufacturing_lead_time_days"),rs.getBoolean("contain_lithium_ion_battery"),""

		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public int queryProductSkuCountInBase(String baseCodeByDrs) {
		String sql = "select count(1) from product_sku ps "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "where pb.code_by_drs = :baseCodeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("baseCodeByDrs", baseCodeByDrs);
		List<BigInteger> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigInteger.class);
		Assert.isTrue(resultList.size()==1,resultList.size()+"");
		return resultList.get(0).intValue();
	}
	
	@Override @Transactional("transactionManager")
	public String insert(BaseProduct base,String drsBaseCode) {
		int pId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"product","id");
		String sql = "insert into product "
				+ "( id, code_by_supplier, name_by_supplier,  internal_notes ) values "
				+ "(:id,  :codeBySupplier,  :nameBySupplier,  :internalNotes )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", pId);
		q.addValue("codeBySupplier", base.getCodeBySupplier().trim());
		q.addValue("nameBySupplier", base.getNameBySupplier().trim());
		q.addValue("internalNotes", base.getInternalNote());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		
		int pbId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"product_base","id");
		sql = "insert into product_base "
			+ "(   id, code_by_drs, supplier_company_id, product_id) select "
			+ " :pbId,  :codeByDrs,             splr.id,       :pId "
			+ "from company splr where splr.k_code = :splrKcode ";
		q = new MapSqlParameterSource();
		q.addValue("pbId", pbId);
		q.addValue("codeByDrs", drsBaseCode.trim());
		q.addValue("splrKcode", base.getSupplierKcode());
		q.addValue("pId", pId);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		
		if(base.getCategory()!=null){
			sql = "update product_base pb set product_category_id = pc.id from product_category pc where pb.id = :pbId and pc.name = :category ";
			q = new MapSqlParameterSource();
			q.addValue("pbId", pbId);
			q.addValue("category", base.getCategory());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		}
		
		return drsBaseCode;
	}
	
	@Override @Transactional("transactionManager")
	public String update(String origBaseCode,BaseProduct base,String newBaseCode) {
		
		String sql = "update product p set "
			+ "( code_by_supplier, name_by_supplier, internal_notes ) = "
			+ "(  :codeBySupplier,  :nameBySupplier, :internalNotes ) "
			+ "from product_base pb "
			+ "where p.id = pb.product_id "
			+ "and pb.code_by_drs = :originBaseCode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("codeBySupplier", base.getCodeBySupplier().trim());
		q.addValue("nameBySupplier", base.getNameBySupplier().trim());
		q.addValue("internalNotes", base.getInternalNote());
		q.addValue("originBaseCode", origBaseCode);
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than row affected, affected rows: "+affectedRows);
		
		sql = "update product_base pb set "
			+ "( code_by_drs, supplier_company_id, product_category_id ) = "
			+ "(  :codeByDrs,                c.id,               pc.id ) "
			+ "from company c, product p, product_category pc "
			+ "where c.k_code = :companyKcode "
			+ "and pb.code_by_drs = :originBaseCode "
			+ "and pc.name = :category ";
		q = new MapSqlParameterSource();
		q.addValue("codeByDrs", newBaseCode.trim());
		q.addValue("companyKcode", base.getSupplierKcode());
		q.addValue("category", base.getCategory());
		q.addValue("originBaseCode", origBaseCode);
		affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one sku updated, affected rows: "+affectedRows);

		return newBaseCode;
	}

	@Override @Transactional("transactionManager")
	public String updateBaseProduct(String drsBaseCode,BaseProduct base) {

		String sql = "update product p set "
				+ "( code_by_supplier, name_by_supplier, internal_notes ) = "
				+ "(  :codeBySupplier,  :nameBySupplier, :internalNotes ) "
				+ "from product_base pb "
				+ "where p.id = pb.product_id "
				+ "and pb.code_by_drs = :drsBaseCode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("codeBySupplier", base.getCodeBySupplier().trim());
		q.addValue("nameBySupplier", base.getNameBySupplier().trim());
		q.addValue("internalNotes", base.getInternalNote());
		q.addValue("drsBaseCode", drsBaseCode);
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
//		Assert.isTrue(affectedRows==1,"No or More than row affected, affected rows: "+affectedRows);

		sql = "update product_base pb set "
				+ "( code_by_drs, supplier_company_id, product_category_id ) = "
				+ "(  :codeByDrs,                c.id,               pc.id ) "
				+ "from company c, product p, product_category pc "
				+ "where c.k_code = :companyKcode "
				+ "and pb.code_by_drs = :drsBaseCode "
				+ "and pc.name = :category ";
		q = new MapSqlParameterSource();
		q.addValue("codeByDrs", drsBaseCode.trim());
		q.addValue("companyKcode", base.getSupplierKcode());
		q.addValue("category", base.getCategory());
		q.addValue("drsBaseCode", drsBaseCode);
		affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
//		Assert.isTrue(affectedRows==1,"No or More than one sku updated, affected rows: "+affectedRows);

		return drsBaseCode;
	}

	@Override @Transactional("transactionManager")
	public String delete(String baseCodeByDrs) {
		Integer productId = this.queryProductIdByProductBase(baseCodeByDrs);
		String sql = "delete from product_base pb where pb.code_by_drs = :baseCodeByDrs";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("baseCodeByDrs", baseCodeByDrs);
		int affectedRows =  getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one sku updated, affected rows: "+affectedRows);
		
		sql = "delete from product p where p.id = :productId ";
		q = new MapSqlParameterSource();
		q.addValue("productId", productId);
		affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one sku updated, affected rows: "+affectedRows);
		return baseCodeByDrs;
	}
	
	@SuppressWarnings("unchecked")
	private Integer queryProductIdByProductBase(String baseCodeByDrs){
		String sql = "select p.id from product p inner join product_base pb on pb.product_id = p.id where pb.code_by_drs = :codeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("codeByDrs", baseCodeByDrs);
		List<Integer> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		Assert.isTrue(resultList.size()==1||resultList.size()==0);
		if(resultList.size()==0) return null;
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String querySupplierKcodeOfProductBase(String baseCode) {
		String sql = "select com.k_code from company com "
				+ "inner join product_base pb on pb.supplier_company_id = com.id "
				+ "where pb.code_by_drs = :code ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("code", baseCode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"Size: "+resultList.size() );
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryCategoryList() {
		String sql = "select pc.name from product_category pc order by pc.name ";
		return getJdbcTemplate().queryForList(sql,String.class);
	}

}
