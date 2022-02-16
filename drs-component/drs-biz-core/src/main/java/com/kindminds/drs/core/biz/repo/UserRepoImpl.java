package com.kindminds.drs.core.biz.repo;

import com.kindminds.drs.Criteria;
import com.kindminds.drs.Filter;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.UserQueryField;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.api.v2.biz.domain.model.repo.UserRepo;
import com.kindminds.drs.service.util.SpringAppCtx;


import java.util.List;
import java.util.Optional;

public class UserRepoImpl implements UserRepo {

    UserDao dao = SpringAppCtx.get().getBean(UserDao.class);

    @Override
    public void add(UserInfo item) {

    }

    @Override
    public void add(List<UserInfo> items) {

    }

    @Override
    public void edit(UserInfo item) {

    }

    @Override
    public void remove(UserInfo item) {

    }

    @Override
    public Optional<UserInfo> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserInfo> findOne(Filter filter) {
        return Optional.empty();
    }

    @Override
    public List<UserInfo> find(Filter filter) {

        for (Criteria it: filter.getCriteriaList()) {
            if(it.field == UserQueryField.role){
               return dao.findUserByRole(it.value);
            }
        }


        return null;
    }
}
