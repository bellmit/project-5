package com.kindminds.drs.persist.data.access.usecase;

import java.util.List;


import com.kindminds.drs.api.v1.model.SupplementalLink;
import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.MaintainSupplementalLinkDao;
import com.kindminds.drs.persist.v1.model.mapping.SupplementalLinkImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository
public class MaintainSupplementalLinkDaoImpl extends Dao implements MaintainSupplementalLinkDao {
	

	@Override @SuppressWarnings("unchecked")
	public List<SupplementalLink> queryList() {
		String sql = "select sl.id as id, "
				+ "    splr.k_code as supplier_kcode, "
				+ "        sl.name as name, "
				+ "         sl.url as url, "
				+ " sl.description as description "
				+ "from supplementallink sl "
				+ "left join company splr on splr.id = sl.supplier_company_id "
				+ "order by supplier_kcode, name asc ";


		return (List) getJdbcTemplate().query(sql,(rs,rowNum) -> new SupplementalLinkImpl(
				rs.getInt("id"),rs.getString("supplier_kcode"),
				rs.getString("name"),rs.getString("url")
				,rs.getString("description")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<SupplementalLink> queryList(String companyKcode) {
		String sql = "select sl.id as id, "
				+ "    splr.k_code as supplier_kcode, "
				+ "        sl.name as name, "
				+ "         sl.url as url, "
				+ " sl.description as description "
				+ "from supplementallink sl "
				+ "left join company splr on splr.id = sl.supplier_company_id "
				+ "where splr.k_code = :companyKcode or sl.supplier_company_id is NULL "
				+ "order by supplier_kcode, name asc ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);

		return (List) getJdbcTemplate().query(sql,(rs,rowNum) -> new SupplementalLinkImpl(
				rs.getInt("id"),rs.getString("supplier_kcode"),
				rs.getString("name"),rs.getString("url")
				,rs.getString("description")
		));
	}

	@Override @Transactional("transactionManager")
	public int insert(SupplementalLink link) {
		int id = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"supplementallink","id");
		String sql = "insert into supplementallink "
				+ "(  id,  name,  url,  description ) values "
				+ "( :id, :name, :url, :description ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		q.addValue("name", link.getName());
		q.addValue("url", link.getUrl());
		q.addValue("description", link.getDescription());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		if(link.getSupplierKcode()!=null){
			sql = "update supplementallink sl set supplier_company_id = splr.id from company splr where splr.k_code = :kcode and sl.id = :id ";
			q = new MapSqlParameterSource();
			q.addValue("id", id);
			q.addValue("kcode", link.getSupplierKcode());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		}
		return id;
	}

	@Override @SuppressWarnings("unchecked")
	public SupplementalLink query(int id) {
		String sql = "select sl.id as id, "
				+ "    splr.k_code as supplier_kcode, "
				+ "        sl.name as name, "
				+ "         sl.url as url, "
				+ " sl.description as description "
				+ "from supplementallink sl "
				+ "left join company splr on splr.id = sl.supplier_company_id "
				+ "where sl.id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);

		List<SupplementalLinkImpl> resultList =  getJdbcTemplate().query(sql,(rs,rowNum) -> new SupplementalLinkImpl(
				rs.getInt("id"),rs.getString("supplier_kcode"),
				rs.getString("name"),rs.getString("url")
				,rs.getString("description")
		));

		if(resultList.size()==0) return null;
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @Transactional("transactionManager")
	public void delete(List<Integer> ids) {
		Assert.isTrue(ids.size()>=1);
		String sql = "delete from supplementallink where id in (:ids) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("ids", ids);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)>=1);
		return;
	}

	@Override @Transactional("transactionManager")
	public SupplementalLink update(SupplementalLink link) {
		String sql = "update supplementallink set "
				+ "(  name,  url,  description ) = "
				+ "( :name, :url, :description ) where id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", link.getId());
		q.addValue("name", link.getName());
		q.addValue("url", link.getUrl());
		q.addValue("description", link.getDescription());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		if(link.getSupplierKcode()!=null){
			sql = "update supplementallink sl set supplier_company_id = splr.id from company splr where splr.k_code = :kcode and sl.id = :id ";
			//q = session.createSQLQuery(sql);
			q.addValue("id", link.getId());
			q.addValue("kcode", link.getSupplierKcode());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		}
		return this.query(link.getId());
	}
	
}
