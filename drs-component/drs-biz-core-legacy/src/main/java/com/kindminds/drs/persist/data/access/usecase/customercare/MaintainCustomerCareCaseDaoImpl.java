package com.kindminds.drs.persist.data.access.usecase.customercare;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage.MessageType;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseType;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseOrderInfo;
import com.kindminds.drs.api.v1.model.customercare.CustomerCaseSearchCondition;
import com.kindminds.drs.persist.v1.model.mapping.customercare.CustomerCareCaseDtoImpl;
import com.kindminds.drs.persist.v1.model.mapping.customercare.CustomerCareCaseMessageImpl;
import com.kindminds.drs.persist.v1.model.mapping.customercare.CustomerCareCaseOrderInfoImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;
import com.kindminds.drs.util.DateHelper;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;




import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class MaintainCustomerCareCaseDaoImpl extends Dao implements MaintainCustomerCareCaseDao {
	
	
	
	private final String dateTimeFormat = "yyyy-MM-dd HH:mm z";
	
	@Override
	public int queryCounts(CustomerCaseSearchCondition condition) {
		String sql = "select count(*) "
				+ "from customercarecase ccc "
				+ "inner join company splr on splr.id = ccc.supplier_company_id "
				+ this.composeWhereSqlForList(condition);
		MapSqlParameterSource q = new MapSqlParameterSource();
		if(condition.getCustomerName()!=null) q.addValue("customerName", this.attachWildcardToBeginAndEnd(condition.getCustomerName()));
		if(condition.getSupplierKcode()!=null) q.addValue("supplierKcode",condition.getSupplierKcode());

		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if(o==null) return 0;
		return o;
	}

	private String composeSelectSqlForList(){
		return "select                ccc.id as id, "
				+ "                ccct.name as type, "
				+ "               drs.k_code as drs_company_kcode, "
				+ "              splr.k_code as supplier_kcode, "
				+ "                     NULL as issue_type_category_id, "
				+ "       ccc.marketplace_id as marketplace_id, "
				+ "                     NULL as marketplace_order_id, "
				+ "                     NULL as marketplace_order_date, "
				+ "        ccc.customer_name as customer_name, "
				+ "       ccc.customer_email as customer_email, "
				+ "to_char(date_create at time zone :tz, :fm ) as date_create,"
				+ "               ccc.status as status, "
				+ "cast( extract(epoch from (now()-last_update_time)) as int) as seconds_from_last_activity ";
	}
	
	private String composeFromSqlForList(){
		return "from customercarecase ccc "
				+ "inner join company drs on (drs.id = ccc.drs_company_id and drs.is_drs_company = TRUE) "
				+ "inner join company splr on (splr.id = ccc.supplier_company_id and splr.is_supplier = TRUE) "
				+ "inner join customercarecase_type ccct on ccct.id = ccc.type_id ";
	}
	
	private String composeWhereSqlForList(CustomerCaseSearchCondition condition){
		if(condition.isNull()) return "";
		String whereConditionSql = "where true ";
		if(condition.getSupplierKcode()!=null) whereConditionSql += "and splr.k_code = :supplierKcode ";
		if(condition.getCustomerName()!=null) whereConditionSql += "and ccc.customer_name ilike :customerName ";
		return whereConditionSql;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<CustomerCareCaseDto> queryList(int startIndex, int size, CustomerCaseSearchCondition condition) {
		String sql = this.composeSelectSqlForList()
					+this.composeFromSqlForList()
					+this.composeWhereSqlForList(condition)
					+ "order by last_update_time desc "
					+ "limit :size offset :start";
		MapSqlParameterSource q = new MapSqlParameterSource();
		if(condition.getCustomerName()!=null) q.addValue("customerName", this.attachWildcardToBeginAndEnd(condition.getCustomerName()));
		if(condition.getSupplierKcode()!=null) q.addValue("supplierKcode",condition.getSupplierKcode());
		q.addValue("size", size);
		q.addValue("start", startIndex-1);
		q.addValue("tz","UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS");

		List<CustomerCareCaseDtoImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,	(rs , rowNum)->
				new	CustomerCareCaseDtoImpl(
						rs.getInt("id"),
						rs.getString("type"),
						rs.getString("drs_company_kcode"),
						rs.getString("supplier_kcode"),
						rs.getInt("issue_type_category_id"),rs.getInt("marketplace_id"),
						rs.getString("marketplace_order_id"),
						rs.getString("marketplace_order_date"),
						rs.getString("customer_name"),
						rs.getString("date_create") ,
						rs.getString("status"),
						rs.getInt("seconds_from_last_activity")

				));

		for(CustomerCareCaseDtoImpl cccI:resultList){
			cccI.setRelatedIssueIdList(this.queryRelatedIssueIdList(cccI.getCaseId()));
			cccI.setRelatedProductBaseList(this.queryRelatedProductBases(cccI.getCaseId()));
			cccI.setRelatedProductSkuList(this.queryRelatedProductSkus(cccI.getCaseId()));
		}
		List<CustomerCareCaseDto> listToReturn = new ArrayList<CustomerCareCaseDto>();
		listToReturn.addAll(resultList);
		return listToReturn;
	}

	@SuppressWarnings("unchecked")
	private List<Integer> queryRelatedIssueIdList(int caseId) {
		String sql = "select cir.issue_id "
				+ "from customercarecase_issue_rel cir "
				+ "where cir.case_id = :caseId order by cir.issue_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("caseId", caseId);
		List<Integer> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		if(resultList.size()==0) return null;
		return resultList;
	}

	@SuppressWarnings("unchecked")
	private List<String> queryRelatedProductBases(int caseId){
		String sql = "select pb.code_by_drs "
				+ "from customercarecase_product_base_rel cccpbr "
				+ "inner join product_base pb on pb.id = cccpbr.product_base_id "
				+ "where cccpbr.case_id = :caseId order by pb.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("caseId", caseId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if(resultList.size()==0) return null;
		return resultList;
	}

	@SuppressWarnings("unchecked")
	private List<String> queryRelatedProductSkus(int caseId){
		String sql = "select ps.code_by_drs "
				+ "from customercarecase_product_sku_rel cccpsr "
				+ "inner join product_sku ps on ps.id = cccpsr.product_sku_id "
				+ "where cccpsr.case_id = :caseId order by ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("caseId", caseId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if(resultList.size()==0) return null;
		return resultList;
	}

	@Override @SuppressWarnings("unchecked")
	public CustomerCareCaseOrderInfo queryOrderInfoById(String orderId) {
		String sql = "select ao.id as id, "
				+ "m.id as marketplace_id, "
				+ "ao.sales_channel as sales_channel, "
				+ "to_char(ao.purchase_date at time zone :timeZoneText, :dateTimeFormatText ) as order_date, "
				+ "ao.buyer_name as buyer_name "
				+ "from amazon_order ao "
				+ "inner join marketplace m on m.name = ao.sales_channel "
				+ "where ao.amazon_order_id = :amazonOrderId ";

	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("amazonOrderId", orderId);
		q.addValue("timeZoneText","UTC");
		q.addValue("dateTimeFormatText","YYYY-MM-DD HH24:MI:SS OF00");
		List<CustomerCareCaseOrderInfoImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,(rs , rowNum) -> new CustomerCareCaseOrderInfoImpl(
				rs.getInt("id"),
				rs.getInt("marketplace_id"),
				rs.getString("sales_channel"),
				rs.getString("order_date"),
				rs.getString("buyer_name")
		));

		if(resultList!=null && resultList.size()==1) {
			return resultList.get(0);
		}

		sql = "SELECT id,  " +
				" gateway as sales_channel,  " +
				" to_char(processed_at at time zone :timeZoneText, :dateTimeFormatText ) as order_date, " +
				" json_billing_address\\:\\:json->>'name' as buyer_name " +
				" FROM shopify_order\n" +
				" WHERE name = :orderId ";
		 q = new MapSqlParameterSource();
		q.addValue("orderId", orderId);
		q.addValue("timeZoneText","UTC");
		q.addValue("dateTimeFormatText","YYYY-MM-DD HH24:MI:SS OF00");
		List<Object[]> shopifyResult = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		if(shopifyResult != null && shopifyResult.size() == 1) {
			Object[] result = shopifyResult.get(0);
			String gateway = (String) result[1];
			Integer marketplaceId = -1;
			if (gateway.equalsIgnoreCase("shopify_payments")) {
				marketplaceId = 2;
			} else if (gateway.equalsIgnoreCase("eBay/PayPal")) {
				marketplaceId = 3;
			}
			Assert.isTrue(marketplaceId > 0, "unrecognized marketplace: " + gateway);
			String buyerName = ((String) result[3]).replace("\"", "");
			return new CustomerCareCaseOrderInfoImpl((int) result[0],
					marketplaceId,
					(String) result[1],
					(String) result[2],
					buyerName);
		}

		return null;
	}

	@Override @Transactional("transactionManager")
	public int insert(int userId, CustomerCareCaseDto ccc) {
		int id = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"customercarecase","id");
		String sql = "insert into customercarecase "
				+ "(  id, type_id,   drs_company_id, supplier_company_id, marketplace_id,  marketplace_order_id,  marketplace_order_date, customer_name,  date_create,  status ) "+ "select "
				+ "  :id, ccct.id,   drs_company.id,         supplier.id,           m.id,    :marketPlaceOrerId,   :marketPlaceOrderDate, :customerName,  :dateCreate, :status "
				+ "from company drs_company, company supplier, marketplace m, customercarecase_type ccct "
				+ "where drs_company.is_drs_company = TRUE and drs_company.k_code = :drsCompanyKcode "
				+ "and supplier.is_supplier = TRUE and supplier.k_code = :supplierKcode "
				+ "and ccct.name = :type "					
				+ "and m.id = :marketplaceId ";
		MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue(                  "id", id);
		q.addValue(                "type", CustomerCareCaseType.valueOf(ccc.getCaseType()).name());
		q.addValue(       "marketplaceId", ccc.getMarketplace().getKey());
		q.addValue(   "marketPlaceOrerId", ccc.getMarketplaceOrderId());
		q.addValue("marketPlaceOrderDate", DateHelper.toDate(ccc.getMarketplaceOrderDate(),"yyyy-MM-dd HH:mm:ss Z"));
		q.addValue(        "customerName", ccc.getCustomerName());
		q.addValue(          "dateCreate", DateHelper.toDate(ccc.getDateCreated(),this.dateTimeFormat));
		q.addValue(              "status", ccc.getStatus());
		q.addValue(     "drsCompanyKcode", ccc.getDrsCompanyKcode());
		q.addValue(       "supplierKcode", ccc.getSupplierKcode());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.updateIssueTypeCategory(id, ccc.getIssueTypeCategoryId());
		this.insertRelatedIssuesRel(id,ccc.getRelatedIssueIds());
		this.insertRelatedProductBaseRel(id,ccc.getRelatedProductBaseCodeList());
		this.insertRelatedProductSkuRel(id, ccc.getRelatedProductSkuCodeList());
		Assert.isTrue(ccc.getMessages().size()==1);
		CustomerCareCaseMessage firstCustomerMsg = ccc.getMessages().get(ccc.getMessages().size() - 1);
		this.insertMessageMainPart(userId, id, 1, MessageType.CUSTOMER,DateHelper.toDate(ccc.getDateCreated(),this.dateTimeFormat),firstCustomerMsg.getContents());
		return id;
	}
	
	private void updateIssueTypeCategory(int caseId, Integer categoryId){
		if(categoryId!=null){
			String sql = "update customercarecase c set issue_type_category_id = :categoryId where c.id = :caseId ";
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("caseId", caseId);
			q.addValue("categoryId", categoryId );
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		}
	}
	
	private void insertRelatedIssuesRel(int caseId, List<Integer> issueIdList) {
		if(issueIdList!=null){
			String sql = "insert into customercarecase_issue_rel "
					+ "( case_id, issue_id  ) select "
					+ "  :caseId,    ci.id "
					+ "from customercarecase_issue ci where ci.id = :issueId ";
			for(Integer issueId: issueIdList){
				MapSqlParameterSource q = new MapSqlParameterSource();
				q.addValue( "caseId", caseId);
				q.addValue("issueId", issueId);
				Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			}
		}
	}

	private void insertRelatedProductBaseRel(int caseId, List<String> baseCodeList){
		if(baseCodeList!=null){
			String sql = "insert into customercarecase_product_base_rel "
					+ "( case_id, product_base_id  ) select "
					+ "  :caseId,           pb.id "
					+ "from product_base pb where pb.code_by_drs = :baseCode ";
			for (String baseCode : baseCodeList) {
				System.out.println("baseCode: " + baseCode);
			}
			String[] baseArray = baseCodeList.get(0).split(",");
			baseCodeList = Arrays.asList(baseArray);
			for(String baseCode : baseCodeList){
				if (StringUtils.hasText(baseCode)) {
					MapSqlParameterSource q = new MapSqlParameterSource();
					q.addValue("caseId", caseId);
					q.addValue("baseCode", baseCode);
					Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q) == 1);
				}
			}
		}
	}
	private void insertRelatedProductSkuRel(int caseId, List<String> skuCodeList){
		if(skuCodeList!=null){
			String sql = "insert into customercarecase_product_sku_rel "
					+ "( case_id, product_sku_id  ) select "
					+ "  :caseId,          ps.id "
					+ "from product_sku ps where ps.code_by_drs = :skuCode ";
			for (String skuCode : skuCodeList) {
				System.out.println("skuCode: " + skuCode);
			}
			String[] skuArray = skuCodeList.get(0).split(",");
			skuCodeList = Arrays.asList(skuArray);
			for(String skuCode : skuCodeList){
				if (StringUtils.hasText(skuCode)) {
					MapSqlParameterSource q = new MapSqlParameterSource();
					q.addValue("caseId", caseId);
					q.addValue("skuCode", skuCode);
					Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q) == 1);
				}
			}
		}
	}
	
	@Override @Transactional("transactionManager")
	public int update(CustomerCareCaseDto ccc) {
		String sql = "update customercarecase set "
				+ "                type_id = ccct.id, "
				+ "         drs_company_id = drs_company.id,"
				+ "    supplier_company_id = supplier.id,"
				+ "         marketplace_id = m.id, "
				+ "   marketplace_order_id = :marketPlaceOrerId, "
				+ " marketplace_order_date = :marketPlaceOrderDate, "
				+ "          customer_name = :customerName, "
				+ "            date_create = :dateCreate,  "
				+ "                 status = :status "
				+ "from company drs_company, company supplier, marketplace m, customercarecase_type ccct "
				+ "where drs_company.is_drs_company = TRUE and drs_company.k_code = :drsCompanyKcode "
				+ "and      supplier.is_supplier    = TRUE and    supplier.k_code = :supplierKcode "
				+ "and customercarecase.id = :id "
				+ "and m.id = :marketplaceId "
				+ "and ccct.name = :type ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue(               "id", ccc.getCaseId());
		q.addValue(             "type", ccc.getCaseType());
		q.addValue(    "marketplaceId", ccc.getMarketplace().getKey());
		q.addValue("marketPlaceOrerId", ccc.getMarketplaceOrderId());
		q.addValue("marketPlaceOrderDate", DateHelper.toDate(ccc.getMarketplaceOrderDate(),"yyyy-MM-dd HH:mm:ss Z"));
		q.addValue(     "customerName", ccc.getCustomerName());
		q.addValue(       "dateCreate", DateHelper.toDate(ccc.getDateCreated(),this.dateTimeFormat));
		q.addValue(           "status", ccc.getStatus());
		q.addValue(  "drsCompanyKcode", ccc.getDrsCompanyKcode());
		q.addValue(    "supplierKcode", ccc.getSupplierKcode());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		CustomerCareCaseMessage firstMsg = ccc.getMessages().get(ccc.getMessages().size() - 1);
		Assert.isTrue(firstMsg.getLineSeq()==1);
		sql = "update customercarecase_message set "
				+ " date_create = :date,  "
				+ " content = :message "
				+ " where case_id = :caseId and line_seq = :lineSeq ";
		 q = new MapSqlParameterSource();
		q.addValue( "caseId", ccc.getCaseId());
		q.addValue("lineSeq", 1);
		q.addValue(   "date", DateHelper.toDate(ccc.getDateCreated(),this.dateTimeFormat));
		q.addValue("message", firstMsg.getContents());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

		sql = "delete from customercarecase_product_base_rel where case_id = :caseId ; "
			+ "delete from customercarecase_product_sku_rel where case_id = :caseId ; "
			+ "delete from customercarecase_issue_rel where case_id = :caseId ; ";
		q = new MapSqlParameterSource();
		q.addValue( "caseId", ccc.getCaseId());
		getNamedParameterJdbcTemplate().update(sql,q);
		this.insertRelatedIssuesRel(ccc.getCaseId(), ccc.getRelatedIssueIds());
		this.insertRelatedProductBaseRel(ccc.getCaseId(),ccc.getRelatedProductBaseCodeList());
		this.insertRelatedProductSkuRel(ccc.getCaseId(), ccc.getRelatedProductSkuCodeList());
		return ccc.getCaseId();
	}

	@Override @Transactional("transactionManager")
	public int updateStatus(CustomerCareCaseDto customerCareCaseDto) {
		String sql = "update customercarecase  "
				+ "  set  status = :status "
				+ "where customercarecase.id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status", customerCareCaseDto.getStatus());
		q.addValue("id", customerCareCaseDto.getCaseId());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return customerCareCaseDto.getCaseId();
	}
	
	private String attachWildcardToBeginAndEnd(String str){
		return "%"+str+"%";
	}

	@Override @SuppressWarnings("unchecked")
	public CustomerCareCaseDto query(int id) {
		String sql = "select            ccc.id as id, "
				+ "                  ccct.name as type, "
				+ "                 drs.k_code as drs_company_kcode, "
				+ "                 spl.k_code as supplier_kcode, "
				+ " ccc.issue_type_category_id as issue_type_category_id, "
				+ "         ccc.marketplace_id as marketplace_id, "
				+ "   ccc.marketplace_order_id as marketplace_order_id, "
				+ "to_char(ccc.marketplace_order_date at time zone :timeZoneText, :dateTimeFormat1Text ) as marketplace_order_date,"
				+ "          ccc.customer_name as customer_name, "
				+ "to_char(date_create at time zone :timeZoneText, :dateTimeFormat2Text ) as date_create,"
				+ "                 ccc.status as status, "
				+ "cast( extract(epoch from (now()-last_update_time)) as int) as seconds_from_last_activity "
				+ "from customercarecase ccc "
				+ "inner join company drs on (drs.id = ccc.drs_company_id      and drs.is_drs_company = TRUE) "
				+ "inner join company spl on (spl.id = ccc.supplier_company_id and    spl.is_supplier = TRUE) "
				+ "inner join customercarecase_type ccct on ccct.id = ccc.type_id "
				+ "where ccc.id = :id ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		q.addValue("timeZoneText","UTC");
		q.addValue("dateTimeFormat1Text","YYYY-MM-DD HH24:MI:SS OF00");
		q.addValue("dateTimeFormat2Text","YYYY-MM-DD HH24:MI OF00");

		List<CustomerCareCaseDtoImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,
				(rs , rowNum)->

					 new	CustomerCareCaseDtoImpl(
							rs.getInt("id"),
							rs.getString("type"),
							rs.getString("drs_company_kcode"),
							rs.getString("supplier_kcode"),
							 rs.getInt("issue_type_category_id"),rs.getInt("marketplace_id"),
							rs.getString("marketplace_order_id"),
							rs.getString("marketplace_order_date"),
							rs.getString("customer_name"),
							rs.getString("date_create") ,
							rs.getString("status"),
							rs.getInt("seconds_from_last_activity")

				));


		Assert.isTrue(resultList.size()<=1);
		if(resultList.size()==0) return null;
		CustomerCareCaseDtoImpl ccc = resultList.get(0);
		ccc.setRelatedIssueIdList(this.queryRelatedIssueIdList(id));
		ccc.setRelatedProductBaseList(this.queryRelatedProductBases(id));
		ccc.setRelatedProductSkuList(this.queryRelatedProductSkus(id));
		ccc.setMessages(this.queryMessages(id));
		return ccc;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<CustomerCareCaseMessage> queryMessages(int caseId){
		String sql = "select           cccm.id as id, "
				+ "              cccm.line_seq as line_seq, "
				+ "                  cccm.type as type, "
				+ "       ui.user_display_name as creator_name, "
				+ " to_char(  cccm.date_create at time zone :timeZoneText, :dateTimeFormat1Text ) as date_create, "
				+ " to_char(cccmri.date_start  at time zone :timeZoneText, :dateTimeFormat2Text ) as date_start, "
				+ " to_char(cccmri.date_finish at time zone :timeZoneText, :dateTimeFormat2Text ) as date_finish, "
				+ "          cccmri.time_taken as time_taken, "
				+ "   cccmri.is_free_of_charge as is_free_of_charge, "
				+ "             ps.code_by_drs as related_sku, "
				+ "    cccmri.num_of_character as num_of_character, "
				+ "       cccmri.num_of_action as num_of_action, "
				+ "cccmri.origin_charge_by_drs as charge_by_drs, "
				+ "cccmri.ms2ss_statement_name as ms2ss_statement_name, "
				+ "cccmri.ss2sp_statement_name as ss2sp_statement_name, " 
				+ "             cccm.content as message, "
				+ " cccmri.includes_translation_fee as includes_translation_fee "
				+ "from customercarecase_message cccm "
				+ "inner join user_info ui on ui.user_id = cccm.creator_id "
				+ "left join customercarecase_message_reply_info cccmri on cccmri.msg_id = cccm.id "
				+ "left join product_sku ps on ps.id = cccmri.related_product_sku_id "
				+ "where cccm.case_id = :caseId order by cccm.date_create desc";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("caseId", caseId);
		q.addValue("timeZoneText","UTC");
		q.addValue("dateTimeFormat1Text","YYYY-MM-DD HH24:MI OF00");
		q.addValue("dateTimeFormat2Text","YYYY-MM-DD HH24:MI:SS OF00");

		List<CustomerCareCaseMessageImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,
				(rs , rowNum)->
						new  	CustomerCareCaseMessageImpl(
								rs.getInt("id"),
								rs.getInt("line_seq"),
								rs.getString("type"),
								rs.getString("creator_name"),
								rs.getString("date_create"),
								rs.getString("date_start"),
								rs.getString("date_finish"),
								rs.getString("time_taken"),
								rs.getString("related_sku"),
								rs.getBoolean("is_free_of_charge"),
								rs.getString("num_of_character"),
								rs.getString("num_of_action"),
								rs.getBigDecimal("charge_by_drs"),
								rs.getString("message"),
								rs.getString("ms2ss_statement_name"),
								rs.getString("ss2sp_statement_name"),
								rs.getBoolean("includes_translation_fee")

						));
		Assert.isTrue(resultList.size()>=1);
		return (List)resultList;
	}
	
	@Override @Transactional("transactionManager")
	public int delete(int id) {
		String sql = "delete from customercarecase_message where case_id = :caseId ; "
				+ "delete from customercarecase_issue_rel where case_id = :caseId ;"
				+ "delete from customercarecase_product_base_rel where case_id = :caseId ;"
				+ "delete from customercarecase_product_sku_rel where case_id = :caseId ;"
				+ "delete from customercarecase where id = :caseId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue( "caseId", id);
		getNamedParameterJdbcTemplate().update(sql,q);
		return id;
	}
	
	@Override @Transactional("transactionManager")
	public void updateLastUpdateTime(int caseId,Date date) {
		String sql = "update customercarecase set last_update_time = :lastUpdateTime where id = :caseId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue( "lastUpdateTime",date);
		q.addValue( "caseId", caseId);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @SuppressWarnings("unchecked")
	public CustomerCareCaseMessage queryMessage(int caseId, int lineSeq) {
		String sql = "select           cccm.id as id, "
				+ "              cccm.line_seq as line_seq, "
				+ "                  cccm.type as type, "
				+ "       ui.user_display_name as creator_name, "
				+ " to_char(  cccm.date_create at time zone :timeZoneText, :dateTimeFormat1Text ) as date_create, "
				+ " to_char(cccmri.date_start  at time zone :timeZoneText, :dateTimeFormat2Text ) as date_start, "
				+ " to_char(cccmri.date_finish at time zone :timeZoneText, :dateTimeFormat2Text ) as date_finish, "
				+ "          cccmri.time_taken as time_taken, "				
				+ "   cccmri.is_free_of_charge as is_free_of_charge, "
				+ "             ps.code_by_drs as related_sku, "
				+ "    cccmri.num_of_character as num_of_character, "
				+ "       cccmri.num_of_action as num_of_action, "
				+ "cccmri.origin_charge_by_drs as charge_by_drs, "
				+ "cccmri.ms2ss_statement_name as ms2ss_statement_name, "
				+ "cccmri.ss2sp_statement_name as ss2sp_statement_name, " 
				+ "               cccm.content as message, "
				+ " cccmri.includes_translation_fee as includes_translation_fee "
				+ "from customercarecase_message cccm "
				+ "inner join user_info ui on ui.user_id = cccm.creator_id "
				+ "left join customercarecase_message_reply_info cccmri on cccmri.msg_id = cccm.id "
				+ "left join product_sku ps on ps.id = cccmri.related_product_sku_id "
				+ "where cccm.case_id = :caseId and cccm.line_seq = :lineSeq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("caseId", caseId);
		q.addValue("timeZoneText","UTC");
		q.addValue("dateTimeFormat1Text","YYYY-MM-DD HH24:MI OF00");
		q.addValue("dateTimeFormat2Text","YYYY-MM-DD HH24:MI:SS OF00");
		q.addValue("lineSeq",lineSeq);

		List<CustomerCareCaseMessageImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,
				(rs , rowNum)->
						new  	CustomerCareCaseMessageImpl(
								rs.getInt("id"),
								rs.getInt("line_seq"),
								rs.getString("type"),
								rs.getString("creator_name"),
								rs.getString("date_create"),
								rs.getString("date_start"),
								rs.getString("date_finish"),
								rs.getString("time_taken"),
								rs.getString("related_sku"),
								rs.getBoolean("is_free_of_charge"),
								rs.getString("num_of_character"),
								rs.getString("num_of_action"),
								rs.getBigDecimal("charge_by_drs"),
								rs.getString("message"),
								rs.getString("ms2ss_statement_name"),
								rs.getString("ss2sp_statement_name"),
								rs.getBoolean("includes_translation_fee")
						));


		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @Transactional("transactionManager")
	public int insertMsdcMessage(int userId, int caseId, CustomerCareCaseMessage msg,BigDecimal chargeByDrs) {
		int nextMsgLineSeq = this.queryMaxMsgLineSeq(caseId)+1;
		return this.insertMsdcMessage(userId, caseId, nextMsgLineSeq, msg, chargeByDrs);
	}
	
	@Transactional("transactionManager")
	private int insertMsdcMessage(int userId, int caseId, int lineSeq, CustomerCareCaseMessage msg,BigDecimal chargeByDrs) {
		Date dateFinished = DateHelper.toDate(msg.getEndDate(),"yyyy-MM-dd HH:mm:ss Z");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		Date today = new Date();
		Date dateCreate = DateHelper.toDate(dateFormat.format(today),"yyyy-MM-dd HH:mm:ss Z");
		int msgId = this.insertMessageMainPart(userId, caseId, lineSeq, MessageType.MSDC, dateCreate, msg.getContents());
		String sql = "insert into customercarecase_message_reply_info "
			+ "( msg_id, related_product_sku_id, time_taken, date_finish, is_free_of_charge, num_of_character, num_of_action, origin_charge_currency_id, origin_charge_by_drs, customercarecase_issue_template_id, includes_translation_fee ) "
			+ "select "
			+ "  :msgId,                  ps.id, :timeTaken, :dateFinish,   :isFreeOfCharge,   :numOfCharater,  :numOfAction,                      c.id,         :chargeByDrs,   :customercarecaseIssueTemplateId, :includesTranslationFee "
			+ "from currency c, product_sku ps "
			+ "where c.name = :currencyName and ps.code_by_drs = :skuCode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue( "msgId", msgId);
		q.addValue("dateFinish", dateFinished);
		q.addValue( "timeTaken", new BigDecimal(msg.getTimeTaken()));		
		q.addValue( "isFreeOfCharge", msg.getIsFreeOfCharge());

		q.addValue("numOfCharater",msg.getWordCount()==null?null:Integer.parseInt(msg.getWordCount()));
		q.addValue("numOfAction", msg.getStandardActionCount()==null?null:Integer.parseInt(msg.getStandardActionCount()));

		q.addValue("currencyName", Currency.USD.name());
		q.addValue("chargeByDrs", chargeByDrs);

		q.addValue("customercarecaseIssueTemplateId",
				msg.getResponseTemplateId()==null?null:Integer.parseInt(msg.getResponseTemplateId()));

		q.addValue("skuCode", msg.getChargeToSKU());
		q.addValue("includesTranslationFee", msg.getIncludesTranslationFee());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return lineSeq;
	}

	@Override @Transactional("transactionManager")
	public int updateMsdcMessage(int userId, int caseId, CustomerCareCaseMessage msg, BigDecimal chargeByDrs) {
		String sql = "delete from customercarecase_message_reply_info cccmri using customercarecase_message cccm "
				+ "where       cccm.id = cccmri.msg_id "
				+ "and   cccm.case_id  = :caseId "
				+ "and   cccm.line_seq = :lineSeq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue( "caseId", caseId);
		q.addValue("lineSeq", msg.getLineSeq());
		Integer r = getNamedParameterJdbcTemplate().update(sql,q);
//		Assert.isTrue(q.executeUpdate()==1);
		Assert.isTrue(r==1,r.toString());
		int origCreatorId = this.queryMessageCreatorId(caseId, msg.getLineSeq());
		sql = "delete from customercarecase_message where case_id = :caseId and line_seq = :lineSeq ";
		 q = new MapSqlParameterSource();
		q.addValue( "caseId", caseId);
		q.addValue("lineSeq", msg.getLineSeq());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return this.insertMsdcMessage(origCreatorId, caseId, msg.getLineSeq(), msg, chargeByDrs);
	}

	@Override @Transactional("transactionManager")
	public int insertCustomerMessage(int userId, int caseId, CustomerCareCaseMessage msg) {
		int lineSeq = this.queryMaxMsgLineSeq(caseId)+1;
		this.insertMessageMainPart(userId, caseId, lineSeq,MessageType.CUSTOMER,DateHelper.toDate(msg.getDateCreate(),this.dateTimeFormat),msg.getContents());
		return lineSeq;
	}
	
	@Transactional("transactionManager")
	private int insertMessageMainPart(int userId, int caseId, int lineSeq, MessageType type, Date dateCreate, String content) {
		int msgId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"customercarecase_message","id");
		String sql = "insert into customercarecase_message "
				+ "(  id, case_id, line_seq,  type, creator_id, date_create,  content ) values "
				+ "( :id, :caseId, :lineSeq, :type,    :userId, :dateCreate, :content )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue(        "id", msgId);
		q.addValue(    "caseId", caseId);
		q.addValue(   "lineSeq", lineSeq);
		q.addValue(      "type", type.getValue());
		q.addValue(    "userId", userId);
		q.addValue("dateCreate", dateCreate);
		q.addValue(   "content", content);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return msgId;
	}
	
	@Override @Transactional("transactionManager")
	public int updateCustomerMessage(int userId, int caseId, CustomerCareCaseMessage msg) {
		int origCreatorId = this.queryMessageCreatorId(caseId, msg.getLineSeq());
		String sql = "delete from customercarecase_message where case_id = :caseId and line_seq = :lineSeq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue( "caseId", caseId);
		q.addValue("lineSeq", msg.getLineSeq());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.insertMessageMainPart(origCreatorId, caseId, msg.getLineSeq(), MessageType.CUSTOMER,DateHelper.toDate(msg.getDateCreate(),this.dateTimeFormat),msg.getContents());
		return msg.getLineSeq();
	}
	
	@Override @Transactional("transactionManager")
	public boolean deleteMessage(int caseId, int lineSeq) {
		String sql = "delete from customercarecase_message_reply_info cccmri using customercarecase_message cccm "
				+ "where       cccm.id = cccmri.msg_id "
				+ "and   cccm.case_id  = :caseId "
				+ "and   cccm.line_seq = :lineSeq ";
		MapSqlParameterSource q = new MapSqlParameterSource();		
		q.addValue( "caseId", caseId);
		q.addValue("lineSeq", lineSeq);
		getNamedParameterJdbcTemplate().update(sql,q);
		sql = "delete from customercarecase_message where case_id = :caseId and line_seq = :lineSeq ";
		 q = new MapSqlParameterSource();
		q.addValue( "caseId", caseId);
		q.addValue("lineSeq", lineSeq);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		sql = "update customercarecase_message set line_seq = line_seq-1 where case_id = :caseId and line_seq > :lineSeq ";
		 q = new MapSqlParameterSource();
		q.addValue( "caseId", caseId);
		q.addValue("lineSeq", lineSeq);
		getNamedParameterJdbcTemplate().update(sql,q);
		return true;
	}

	private int queryMaxMsgLineSeq(int caseId){
		String sql = "select max(line_seq) from customercarecase_message cccm "
				+ "inner join customercarecase ccc on ccc.id = cccm.case_id "
				+ "where ccc.id = :caseId ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("caseId", caseId);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null) return 0;
		return o;
	}
	
	private int queryMessageCreatorId(int caseId,int lineSeq){
		String sql = "select creator_id from customercarecase_message where case_id = :caseId and line_seq = :lineSeq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue( "caseId", caseId);
		q.addValue("lineSeq", lineSeq);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null) return 0;
		return o;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,String> queryProductBaseCodeToSupplierNameMap(String supplierKcode) {
		String sql = "select pb.code_by_drs, p.name_by_supplier  "
				+ "from product_base pb "
				+ "inner join product p on p.id = pb.product_id "
				+ "inner join company c on c.id = pb.supplier_company_id "
				+ "where c.k_code = :supplierKcode "
				+ "order by pb.code_by_drs asc ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		Map<String,String> baseCodeToSupplierNameMap = new LinkedHashMap<String,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			baseCodeToSupplierNameMap.put((String)items[0], (String)items[1]);
		}
		return baseCodeToSupplierNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,String> queryProductSkuCodeToSupplierNameMap(String supplierKcode) {
		String sql = "select ps.code_by_drs, p.name_by_supplier "
				+ "from product_sku ps "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where splr.k_code = :supplierKcode "
				+ "order by ps.code_by_drs asc ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		Map<String,String> resultMap = new LinkedHashMap<String,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			resultMap.put((String)items[0], (String)items[1]);
		}
		return resultMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryProductSkuCodeToSupplierNameMapUnderBases(List<String> baseCodes) {
		String sql = "select ps.code_by_drs, p.name_by_supplier "
				+ "from product_sku ps "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "where pb.code_by_drs in (:baseCode) "
				+ "order by ps.code_by_drs asc ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("baseCode", baseCodes);
		Map<String,String> resultMap = new HashMap<String,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			resultMap.put((String)items[0], (String)items[1]);
		}
		return resultMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryContactChannelNameToDrsCompanyShortEnUsNameMap() {
		String sql = "select ccccc.name, c.short_name_en_us "
				+ "from customercarecase_contact_channel ccccc "
				+ "inner join company c on c.id = ccccc.handling_drs_company_id "
				+ "where c.is_drs_company is TRUE "
				+ "and enabled is TRUE ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		Map<String,String> resultMap = new TreeMap<String,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			resultMap.put((String)items[0], (String)items[1]);
		}
		return resultMap;				
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryProductBaseCodeToSupplierNameMapByMarketplace(String supplierKcode,
			int marketplaceId) {
		String sql = "select distinct pb.code_by_drs, p.name_by_supplier  "
				+ "from product_base pb "
				+ "inner join product p on p.id = pb.product_id "
				+ "inner join company c on c.id = pb.supplier_company_id "
				+ "inner join product_sku ps on ps.product_base_id = pb.id "
				+ "inner join product_marketplace_info pmi on ( pmi.product_id = ps.product_id and pmi.marketplace_id = :marketplaceId ) "
				+ "where c.k_code = :supplierKcode "
				+ "order by pb.code_by_drs asc ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("marketplaceId", marketplaceId);
		Map<String,String> baseCodeToSupplierNameMap = new LinkedHashMap<String,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			baseCodeToSupplierNameMap.put((String)items[0], (String)items[1]);
		}
		return baseCodeToSupplierNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryProductSkuCodeToSupplierNameMapByMarketplace(String supplierKcode,
			int marketplaceId) {
		String sql = "select ps.code_by_drs, p.name_by_supplier "
				+ "from product_sku ps "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "inner join product_marketplace_info pmi on (pmi.product_id = ps.product_id and pmi.marketplace_id = :marketplaceId ) "
				+ "where splr.k_code = :supplierKcode "
				+ "order by ps.code_by_drs asc ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("marketplaceId", marketplaceId);		
		Map<String,String> resultMap = new LinkedHashMap<String,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			resultMap.put((String)items[0], (String)items[1]);
		}
		return resultMap;
	}
		
	@Override @SuppressWarnings("unchecked")
	public String queryMsdcMsgMs2ssStatementName(int caseId, int msgLineSeq) {
		String sql = "select ms2ss_statement_name from customercarecase_message_reply_info cccmri "
				+ "inner join customercarecase_message cccm on cccm.id = cccmri.msg_id "
				+ "inner join customercarecase ccc on ccc.id = cccm.case_id "
				+ "where ccc.id = :caseId "
				+ "and cccm.line_seq = :msgLineSeq ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("caseId", caseId);
		q.addValue("msgLineSeq", msgLineSeq);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if(resultList==null||resultList.size()==0) return null;
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryMsdcMsgSs2spStatementName(int caseId, int msgLineSeq) {
		String sql = "select ss2sp_statement_name from customercarecase_message_reply_info cccmri "
				+ "inner join customercarecase_message cccm on cccm.id = cccmri.msg_id "
				+ "inner join customercarecase ccc on ccc.id = cccm.case_id "
				+ "where ccc.id = :caseId "
				+ "and cccm.line_seq = :msgLineSeq ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("caseId", caseId);
		q.addValue("msgLineSeq", msgLineSeq);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if(resultList==null||resultList.size()==0) return null;
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryTypeList() {
		String sql = "select name from customercarecase_type order by name ";
		List<String> resultList = getJdbcTemplate().queryForList(sql,String.class);
		return resultList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<Integer,String> queryApplicableTemplate(int caseId) {
		String sql = "select cit.id, substring( cit.content from 1 for 150 ) || '...' as header from customercarecase c "
				+ "inner join customercarecase_issue_rel cir on cir.case_id = c.id "
				+ "inner join customercarecase_issue ci on ci.id = cir.issue_id "
				+ "inner join customercarecase_issue_template cit on cit.issue_id = ci.id "
				+ "inner join customercarecase_issue_template_case_type_rel citctr on citctr.template_id = cit.id "
				+ "inner join customercarecase_issue_template_marketplace_rel citmr on citmr.template_id = cit.id "
				+ "where c.id = :caseId "
				+ "and citctr.case_type_id = c.type_id "
				+ "and citmr.marketplace_id = c.marketplace_id "
				+ "order by header ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("caseId", caseId);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<Integer,String> templateIdToHeaderMap = new HashMap<Integer,String>();
		for(Object[] items:resultList){
			templateIdToHeaderMap.put((Integer)items[0], (String)items[1]);
		}
		return templateIdToHeaderMap;
	}

	@Override @SuppressWarnings("unchecked")
	public String queryTemplateContent(int templateId) {
		String sql = "select content from customercarecase_issue_template cit "
				+ "where cit.id = :templateId ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("templateId",templateId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"Invalid Template Id:"+templateId);
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public Map<Integer,String> queryAllIssueIdToEnUsNameMap(Integer typeCategoryId, Integer typeId) {
		String sql = "select ci.id, ciln.name as display_name "
				+ "from customercarecase_issue ci "
				+ "inner join customercarecase_issue_locale_name ciln on ciln.issue_id = ci.id "
				+ "inner join locale l on l.id = ciln.locale_id "
				+ "inner join customercarecase_issue_type cit on cit.id = ci.type_id "
				+ "where l.code = 'en_US' ";
		if(typeCategoryId!=null) sql += "and cit.category_id = :typeCategoryId ";
		if(typeId!=null) sql += "and cit.id = :typeId ";
				sql += "order by display_name ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		if(typeCategoryId!=null) q.addValue("typeCategoryId",typeCategoryId);
		if(typeId!=null) q.addValue("typeId",typeId);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<Integer,String> templateIdToHeaderMap = new HashMap<Integer,String>();
		for(Object[] items:resultList){
			templateIdToHeaderMap.put((Integer)items[0], (String)items[1]);
		}
		return templateIdToHeaderMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<Integer, String> queryTemplateIdToDisplayName(int issueId) {
		String sql = "select cit.id, substring( cit.content from 1 for 150 ) || '...' as header "
				+ "from customercarecase_issue_template cit "
				+ "inner join customercarecase_issue ci on ci.id = cit.issue_id "
				+ "where ci.id = :issueId "
				+ "order by header ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<Integer,String> templateIdToHeaderMap = new HashMap<Integer,String>();
		for(Object[] items:resultList){
			templateIdToHeaderMap.put((Integer)items[0], (String)items[1]);
		}
		return templateIdToHeaderMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public String querySupplierKcodeOfCase(int caseId) {
		String sql = "select com.k_code from company com "
				+ "inner join customercarecase c on c.supplier_company_id = com.id "
				+ "where com.is_supplier = TRUE "
				+ "and c.id = :caseId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("caseId", caseId);
		List<String> kcodeList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(kcodeList.size()==1);
		return kcodeList.get(0);
	}

	@Override
	public String queryCompanyEnglishShortName(String kCode) {
		String sql = "SELECT com.short_name_en_us "
				+ " FROM company com "
				+ " WHERE com.k_code = :kCode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kCode", kCode);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
	}

	@Override
	public Integer queryMaxCaseId() {
		String sql = "SELECT max(id) from customercarecase";

		return getJdbcTemplate().queryForObject(sql,Integer.class);
	}
}
