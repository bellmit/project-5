package com.kindminds.drs.persist.data.access.rdb;

import java.util.List;





import com.kindminds.drs.UserInfo;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.api.v1.model.user.UserCredential;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


import com.kindminds.drs.persist.v1.model.mapping.user.UserCredentialImpl;
import com.kindminds.drs.persist.v1.model.mapping.user.UserInfoImpl;

@Repository("UserDao")
public class UserDaoImpl extends Dao implements UserDao {
	
	
	@SuppressWarnings("unchecked")
	@Override
	public UserCredential getUserCredential(String name) {
		String sql = "SELECT user_password FROM user_info WHERE user_name = :name";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name", name);
		List<UserCredentialImpl> users = (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new UserCredentialImpl(
				rs.getString("user_password")
		));
		if ((users == null) || (users.size() == 0))
			return null;
		return users.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public UserInfo findUserByUserID(String userName) {
		String sql = "select u.user_id as id, "
				+ "        u.user_name as user_name, "
				+ "     u.user_enabled as is_enabled, "
				+ "u.user_display_name as display_name, "
				+ "           u.locale as locale, "
				+ "         com.k_code as company_kcode, "
				+ " com.is_drs_company as is_drs_company, "
				+ "    com.is_supplier as is_supplier "
				+ "from user_info u "
				+ "inner join company com on com.id = u.company_id "
				+ "where user_name = :name";


		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name", userName);

		List<UserInfo>users =
				(List) getNamedParameterJdbcTemplate().query(sql,q,
						new BeanPropertyRowMapper(UserInfoImpl.class));


		if(users==null||users.size()==0) return null;
		return users.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getUserRoles(String userName) {
		String sql = "SELECT ur.role_id FROM user_role AS ur "
				+ "INNER JOIN user_info AS u ON (u.user_id = ur.user_id AND u.user_name = :name " +
				"AND u.user_enabled = :enabled) "
				+ "WHERE ur.enabled = :enabled";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name", userName);
		q.addValue("enabled", true);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public boolean isSupplier(int userId) {
		String sql = "select is_supplier from company com "
				+ "inner join user_info u on u.company_id = com.id "
				+ "where u.user_id = :userId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("userId", userId);

		List<Boolean> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Boolean.class);
		Assert.isTrue(resultList.size()==0||resultList.size()==1);
		if(resultList.size()==0||resultList.get(0)==null) return false;
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public boolean isDrsUser(int userId) {
		String sql = "select is_drs_company from company com "
				+ "inner join user_info u on u.company_id = com.id "
				+ "where u.user_id = :userId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("userId", userId);
		List<Boolean> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Boolean.class);
		Assert.isTrue(resultList.size()==0||resultList.size()==1);
		if(resultList.size()==0||resultList.get(0)==null) return false;
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryUserMail(int userId) {
		String sql = "select user_email from user_info ui where ui.user_id = :userId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("userId", userId);
		List<String> resultList = (List) getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override
	public List<UserInfo> findUserByRole(String role) {

		String sql = "select u.user_id as id, " +
				"       u.user_name as user_name, " +
				"     u.user_enabled as is_enabled, " +
				" u.user_display_name as display_name, " +
				"           u.locale as locale, " +
				"         com.k_code as company_kcode, " +
				" com.is_drs_company as is_drs_company, " +
				"   com.is_supplier as is_supplier " +
				" from user_info u " +
				" inner join company com on com.id = u.company_id " +
				" inner join user_role ur  on ur.user_id  = u.user_id " +
				" where ur.role_id = :role and ur.enabled = true and u.user_enabled = true";


		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("role", role);

		List<UserInfo>users =
				(List) getNamedParameterJdbcTemplate().query(sql,q,
						new BeanPropertyRowMapper(UserInfoImpl.class));


		if(users==null||users.size()==0) return null;
		return users;
	}

}
