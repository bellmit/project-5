package com.kindminds.drs.persist.data.access.usecase.product;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.persist.data.access.rdb.Dao;


import com.kindminds.drs.api.data.access.usecase.product.MaintainProductOnboardingDao;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductDetailImpl;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductDtoImpl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;




import java.util.ArrayList;
import java.util.List;


@Repository
public class MaintainProductOnboardingDaoImpl extends Dao implements MaintainProductOnboardingDao{


	//@Autowired @Qualifier("envProperties") private Properties env;

	//private static Logger logger = Logger.getLogger(MaintainProductOnboardingDaoImpl.class);

	private final String referenceFilePathPropertyName ="REFERENCE_FILE_FOLDER_PATH";
	private final String batteryFilePathPropertyName ="BATTERY_FILE_FOLDER_PATH";
	private final String mainImageFilePathPropertyName ="MAIN_IMAGE_FILE_FOLDER_PATH";
	private final String variationImageFilePathPropertyName ="VARIATION_IMAGE_FILE_FOLDER_PATH";
	private final String otherImageFilePathPropertyName ="OTHER_IMAGE_FILE_FOLDER_PATH";


	private static final int BUFFER_SIZE = 4096;

	@Override @SuppressWarnings("unchecked")
	public List<ProductDto> queryBaseProductOnboardingList(int startIndex, int size) {

		String sql = "select pis.key as id, "
				+ "             NULL as supplier_kcode, "
				+ "         pis.data as json_data "
				+ "from draft_product_info_source pis "
				+ "order by pis.key asc "
				+ "limit :size offset :start ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("size", size);
		q.addValue("start", startIndex-1);
		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ProductDtoImpl(
				rs.getString("id"),rs.getString("supplier_kcode") ,rs.getString("json_data"),
				null,null,null,null
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<ProductDto> queryBaseProductOnboardingList(int startIndex, int size,
														   String companyKcode) {
		String sql = "select pis.key as id, "
				+ "             NULL as supplier_kcode, "
				+ "         pis.data as json_data "
				+ "from draft_product_info_source pis "
				+ "inner join company com on com.id = pis.supplier_company_id "
				+ "where com.k_code = :companyKcode "
				+ "order by pis.key asc "
				+ "limit :size offset :start ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("size", size);
		q.addValue("start", startIndex-1);

		List rList =  getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ProductDtoImpl(
				rs.getString("id"),rs.getString("supplier_kcode") ,rs.getString("json_data"),
				null,null,null,null
		));


		return rList ;
	}

	@Override
	public int queryBaseProductOnboardingCount() {
		String sql = "select count(*) from draft_product_info_source";
		MapSqlParameterSource q = new MapSqlParameterSource();
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		
		return o;
	}

	public int queryBaseProductOnboardingCount(String companyKcode) {
		String sql = "select count(*) from draft_product_info_source pis "
				+ "inner join company com on com.id = pis.supplier_company_id "
				+ "where com.k_code = :companyKcode";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode",companyKcode);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);

		return o;
	}

	@Override @SuppressWarnings("unchecked")
	public ProductDto queryBaseProductOnboarding(String productBaseCode) {
		String sql = "select pis.key as id, "
				+ "  supplier.k_code as supplier_kcode "
				+ "from draft_product_info_source pis "
				+ "inner join company supplier on supplier.id = pis.supplier_company_id "
				+ "where pis.key = :productBaseCode";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBaseCode", productBaseCode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return this.transferToOnboardingBaseProduct(columnsList,productBaseCode);
	}

	private ProductDto transferToOnboardingBaseProduct(List<Object[]> columnsList, String productBaseCode){
		String id = "";
		String supplierKcode = "";

		for(Object[] columns:columnsList){
			 id = (String)columns[0];
			 supplierKcode = (String)columns[1];
		}

		return new ProductDtoImpl(id , supplierKcode ,"",
				this.queryProductInfoSource(productBaseCode),
				this.queryProductMarketingMaterialSource(productBaseCode),
				this.queryProductInfoMarketSide(productBaseCode) ,
		  		this.queryProductMarketingMaterialMarketSide(productBaseCode));

	}



	@SuppressWarnings("unchecked")
	private ProductDetailImpl queryProductInfoSource(String productBaseCode){
		String sql = "select       pis.key as id, "
				+ "	 	   supplier.k_code as supplier_kcode, "
				+ " CAST(pis.data as text) as json_data, "
				+ "             pis.status as status "
				+ "from draft_product_info_source pis "
				+ "inner join company supplier on supplier.id = pis.supplier_company_id "
				+ "where pis.key = :productBaseCode";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBaseCode", productBaseCode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return this.transferToOnboardingBaseProductDetail(columnsList);
	}

	@SuppressWarnings("unchecked")
	private ProductDetailImpl queryProductMarketingMaterialSource(String productBaseCode){
		String sql = "select pmms.product_base_code as id, "
				+ "	 	            supplier.k_code as supplier_kcode, "
				+ "         CAST(pmms.data as text) as json_data, "
				+ "                     pmms.status as status "
				+ "from draft_product_marketing_material_source pmms "
				+ "inner join company supplier on supplier.id = pmms.supplier_company_id "
				+ "where pmms.product_base_code = :productBaseCode";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBaseCode", productBaseCode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return this.transferToOnboardingBaseProductDetail(columnsList);
	}

	@SuppressWarnings("unchecked")
	private List<ProductDetail> queryProductInfoMarketSide(String productBaseCode){
		String sql = "select pim.product_base_code as id, "
				+ "	 	           supplier.k_code as supplier_kcode, "
				+ "         CAST(pim.data as text) as json_data, "
				+ "                     pim.status as status "
				+ "from draft_product_info_marketside pim "
				+ "inner join company supplier on supplier.id = pim.supplier_company_id "
				+ "where pim.product_base_code = :productBaseCode";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBaseCode", productBaseCode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return this.transferToListOfOnboardingBaseProductDetail(columnsList);
	}

	@SuppressWarnings("unchecked")
	private List<ProductDetail> queryProductMarketingMaterialMarketSide(String productBaseCode){
		String sql = "select pmmm.product_base_code as id, "
				+ "	 	            supplier.k_code as supplier_kcode, "
				+ "         CAST(pmmm.data as text) as json_data "
				//+ "                            NULL as status "
				+ "from draft_product_marketing_material_marketside pmmm "
				+ "inner join company supplier on supplier.id = pmmm.supplier_company_id "
				+ "where pmmm.product_base_code = :productBaseCode";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBaseCode", productBaseCode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return this.transferToListOfOnboardingBaseProductDetailForProductMarketingMaterialMarketSide(columnsList);
	}



	@Override @SuppressWarnings("unchecked")
	public String querySupplierKcodeOfBaseProductOnboarding(String productBaseCode) {
		String sql = "select com.k_code from company com "
				+ "inner join draft_product_info_source pis on pis.supplier_company_id = com.id "
				+ "where pis.key = :productBaseCode";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBaseCode", productBaseCode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"Size: "+resultList.size() );
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public ProductDetail queryProductInfoMarketSide(String supplierKcode, String productBaseCode, String country) {
		String sql = "select pim.product_base_code as id, "
				+ "	 	               splr.k_code as supplier_kcode, "
				+ "         CAST(pim.data as text) as json_data, "
				+ "                     pim.status as status "
				+ "from draft_product_info_marketside pim "
				+ "inner join company splr on splr.id = pim.supplier_company_id "
				+ "where pim.product_base_code = :productBaseCode "
				+ "and pim.country_id = :countryId "
				+ "and splr.k_code = :supplierKcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBaseCode",productBaseCode);
		q.addValue("countryId",Country.valueOf(country).getKey());
		q.addValue("supplierKcode",supplierKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return this.transferToOnboardingBaseProductDetail(columnsList);
	}

	@Override @SuppressWarnings("unchecked")
	public ProductDetail queryProductMarketingMaterialSource(String supplierKcode, String productBaseCode) {
		String sql = "select pmms.product_base_code as id, "
				+ "	 	                splr.k_code as supplier_kcode, "
				+ "         CAST(pmms.data as text) as json_data, "
				+ "                     pmms.status as status "
				+ "from draft_product_marketing_material_source pmms "
				+ "inner join company splr on splr.id = pmms.supplier_company_id "
				+ "where pmms.product_base_code = :productBaseCode "
				+ "and splr.k_code = :supplierKcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBaseCode",productBaseCode);
		q.addValue("supplierKcode",supplierKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return this.transferToOnboardingBaseProductDetail(columnsList);
	}

	@Override @SuppressWarnings("unchecked")
	public ProductDetail queryProductMarketingMaterialMarketSide(String supplierKcode, String productBaseCode,
																			   String country) {
		String sql = "select pmmm.product_base_code as id, "
				+ "	 	                splr.k_code as supplier_kcode, "
				+ "         CAST(pmmm.data as text) as json_data "
				//+ "                            NULL as status "
				+ "from draft_product_marketing_material_marketside pmmm "
				+ "inner join company splr on splr.id = pmmm.supplier_company_id "
				+ "where pmmm.product_base_code = :productBaseCode "
				+ "and pmmm.country_id = :countryId "
				+ "and splr.k_code = :supplierKcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBaseCode",productBaseCode);
		q.addValue("countryId",Country.valueOf(country).getKey());
		q.addValue("supplierKcode",supplierKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return this.transferToOnboardingBaseProductDetailForProductMarketingMaterialMarketSide(columnsList);
	}

	private ProductDetailImpl transferToOnboardingBaseProductDetail(List<Object[]> columnsList){

		for(Object[] columns:columnsList){
			String id = (String)columns[0];
			String supplierKcode = (String)columns[1];
			String data = (String)columns[2];
			String status = (String)columns[3];
			 return new ProductDetailImpl(id, id,supplierKcode,data,status ,"");
		}
		return null;
	}

	private ProductDetail transferToOnboardingBaseProductDetailForProductMarketingMaterialMarketSide(List<Object[]> columnsList){

		for(Object[] columns:columnsList){
			String id = (String)columns[0];
			String supplierKcode = (String)columns[1];
			String data = (String)columns[2];
			String status = null;
			return new ProductDetailImpl(id,id,supplierKcode,data,status ,"");
		}
		return null;
	}

	private List<ProductDetail> transferToListOfOnboardingBaseProductDetail(List<Object[]> columnsList){
		List<ProductDetail> ListOfOnboardingBaseProductDetail = new ArrayList<ProductDetail>();
		for(Object[] columns:columnsList){
			String id = (String)columns[0];
			String supplierKcode = (String)columns[1];
			String data = (String)columns[2];
			String status = (String)columns[3];
			ListOfOnboardingBaseProductDetail.add(new ProductDetailImpl(id, id,supplierKcode,data,status ,""));
		}
		return ListOfOnboardingBaseProductDetail;
	}

	private List<ProductDetail> transferToListOfOnboardingBaseProductDetailForProductMarketingMaterialMarketSide(List<Object[]> columnsList){
		List<ProductDetail> ListOfOnboardingBaseProductDetail = new ArrayList<ProductDetail>();
		for(Object[] columns:columnsList){
			String id = (String)columns[0];
			String supplierKcode = (String)columns[1];
			String data = (String)columns[2];
			String status = null;
			ListOfOnboardingBaseProductDetail.add(new ProductDetailImpl(id, id,supplierKcode,data,status ,""));
		}
		return ListOfOnboardingBaseProductDetail;
	}



	@Override @Transactional("transactionManager")
	public String insertProductInfoSource(String companyKcode, String productBaseCode, String jsonData) {


		String sql = "insert into draft_product_info_source "
				+ "( supplier_company_id,  key,                    data ) select "
				+ "              splr.id, :key, CAST(:jsonData as json) "
				+ "from company splr where splr.k_code = :kcode ";


	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", companyKcode);
		q.addValue("key", productBaseCode);
		q.addValue("jsonData", jsonData);

		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return jsonData;
	}

	@Override @Transactional("transactionManager")
	public String insertProductInfoMarketSide(String companyKcode, String productBaseCode, String country,
											  String jsonData) {
		String sql = "insert into draft_product_info_marketside "
				+ "( supplier_company_id, product_base_code, country_id,                    data ) select "
				+ "              splr.id,  :productBaseCode, :countryId, CAST(:jsonData as json) from company splr where splr.k_code = :companyKcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("productBaseCode", productBaseCode);
		q.addValue("countryId", Country.valueOf(country).getKey());
		q.addValue("jsonData", jsonData);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return jsonData;
	}

	@Override @Transactional("transactionManager")
	public String insertProductMarketingMaterialSource(String companyKcode, String productBaseCode, String jsonData) {
		String sql = "insert into draft_product_marketing_material_source "
				+ "( supplier_company_id, product_base_code,                    data ) select "
				+ "              splr.id,  :productBaseCode,   CAST(:jsonData as json) from company splr where splr.k_code = :companyKcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("productBaseCode", productBaseCode);
		q.addValue("jsonData", jsonData);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return jsonData;
	}

	@Override @Transactional("transactionManager")
	public String insertProductMarketingMaterialMarketSide(String companyKcode, String productBaseCode, String country,
														   String jsonData) {
		String sql = "insert into draft_product_marketing_material_marketside "
				+ "( supplier_company_id, product_base_code, country_id,                    data ) select "
				+ "              splr.id,  :productBaseCode, :countryId, CAST(:jsonData as json) from company splr where splr.k_code = :companyKcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("productBaseCode", productBaseCode);
		q.addValue("countryId", Country.valueOf(country).getKey());
		q.addValue("jsonData", jsonData);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return jsonData;
	}

	@Override @Transactional("transactionManager")
	public String updateProductInfoMarketSide(String companyKcode, String productBaseCode, String country,
											  String jsonData) {
		String sql = "update draft_product_info_marketside set "
				+ "(  					data) = "
				+ "( CAST(:jsonData as json))"
				+ "where supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and product_base_code = :productBaseCode "
				+ "and country_id = :countryId ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("productBaseCode", productBaseCode);
		q.addValue("countryId", Country.valueOf(country).getKey());
		q.addValue("jsonData", jsonData);
		int rows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(rows==1);
		return jsonData;
	}

	@Override @Transactional("transactionManager")
	public String updateProductMarketingMaterialSource(String companyKcode, String productBaseCode, String jsonData) {
		String sql = "update draft_product_marketing_material_source set "
				+ "(  					data) = "
				+ "( CAST(:jsonData as json))"
				+ "where supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and product_base_code = :productBaseCode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("productBaseCode", productBaseCode);
		q.addValue("jsonData", jsonData);
		int rows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(rows==1);
		return jsonData;
	}

	@Override @Transactional("transactionManager")
	public String updateProductMarketingMaterialMarketSide(String companyKcode, String productBaseCode, String country,
														   String jsonData) {
		String sql = "update draft_product_marketing_material_marketside set "
				+ "(  					data) = "
				+ "( CAST(:jsonData as json))"
				+ "where supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and product_base_code = :productBaseCode "
				+ "and country_id = :countryId ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("productBaseCode", productBaseCode);
		q.addValue("countryId", Country.valueOf(country).getKey());
		q.addValue("jsonData", jsonData);
		int rows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(rows==1);
		return jsonData;
	}

	@Override @Transactional("transactionManager")
	public void updateStatusForProductInfoSource(String companyKcode, String productBaseCode,
												 ProductEditingStatusType status) {
		String sql = "update draft_product_info_source set "
				+ "( status) = "
				+ "(:status)"
				+ "where supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and key = :productBaseCode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("productBaseCode", productBaseCode);
		q.addValue("status", status.getText());
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void updateStatusForProductInfoMarketSideByRegion(String companyKcode, String productBaseCode, String country,
															 ProductEditingStatusType status) {
		String sql = "update draft_product_info_marketside set "
				+ "( status) = "
				+ "(:status)"
				+ "where supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and product_base_code = :productBaseCode "
				+ "and country_id = :countryId ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("productBaseCode", productBaseCode);
		q.addValue("countryId", Country.valueOf(country).getKey());
		q.addValue("status", status.getText());
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void updateStatusForProductMarketingMaterialSource(String companyKcode, String productBaseCode,
															  ProductEditingStatusType status) {
		String sql = "update draft_product_marketing_material_source set "
				+ "( status) = "
				+ "(:status)"
				+ "where supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and product_base_code = :productBaseCode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("productBaseCode", productBaseCode);
		q.addValue("status", status.getText());
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void updateJsonDataForProductInfoSource(String companyKcode, String productBaseCode, String jsonData) {
		String sql = "update draft_product_info_source set "
				+ "(                    data) = "
				+ "( CAST(:jsonData as json)) "
				+ "where supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and key = :productBaseCode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("productBaseCode", productBaseCode);
		q.addValue("jsonData", jsonData);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void deleteProductInfoSource(String companyKcode, String productBaseCode) {
		String sql = "delete from draft_product_info_source pis "
				+ "where pis.supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and pis.key = :productBaseCode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode",companyKcode);
		q.addValue("productBaseCode",productBaseCode);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @Transactional("transactionManager")
	public void deleteProductInfoMarketSide(String companyKcode, String productBaseCode) {
		String sql = "delete from draft_product_info_marketside pim "
				+ "where pim.supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and pim.product_base_code = :productBaseCode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode",companyKcode);
		q.addValue("productBaseCode",productBaseCode);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void deleteProductMarketingMaterialSource(String companyKcode, String productBaseCode) {
		String sql = "delete from draft_product_marketing_material_source pmms "
				+ "where pmms.supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and pmms.product_base_code = :productBaseCode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode",companyKcode);
		q.addValue("productBaseCode",productBaseCode);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @Transactional("transactionManager")
	public void deleteProductMarketingMaterialMarketSide(String companyKcode, String productBaseCode) {
		String sql = "delete from draft_product_marketing_material_marketside pmmm "
				+ "where pmmm.supplier_company_id = ( select splr.id from company splr where splr.k_code = :companyKcode ) "
				+ "and pmmm.product_base_code = :productBaseCode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode",companyKcode);
		q.addValue("productBaseCode",productBaseCode);
		getNamedParameterJdbcTemplate().update(sql,q);
	}



	@Override
	public boolean isProductBaseCodeExist(String supplierKcode, String productBaseCode) {
		String sql = "select exists ( "
				+ "    select 1 from draft_product_info_source pis "
				+ "    inner join company splr on splr.id = pis.supplier_company_id "
				+ "    where splr.k_code = :supplierKcode "
				+ "    and pis.key = :productBaseCode "
				+ ")";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("productBaseCode", productBaseCode);
		Boolean o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);

		return o;
	}

	private String getRootFileDir(){
		return System.getProperty("catalina.home");
	}

}