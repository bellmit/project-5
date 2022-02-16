package com.kindminds.drs.core.biz.repo.logistics;

import com.kindminds.drs.Currency;
import com.kindminds.drs.Filter;
import com.kindminds.drs.api.data.row.logistics.IvsRow;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.repo.logistics.IvsRepo;
import com.kindminds.drs.core.biz.logistics.IvsImpl;
import com.kindminds.drs.api.data.access.rdb.logistics.IvsDao;
import com.kindminds.drs.service.util.SpringAppCtx;


import java.math.BigDecimal;
import java.util.*;

public class IvsRepoImpl implements IvsRepo {

    private final IvsDao dao = (IvsDao)SpringAppCtx.get().getBean(IvsDao.class);


    @Override
    public void add( Ivs item) {

        IvsDao var10000 = this.dao;
        String var10001 = item.getSellerCompanyKcode();
        Integer var10003 = item.getSerialId();
        int var2 = var10003;
        String var10004 = item.getName();

        BigDecimal var10005 = item.getSalesTaxRate();
        BigDecimal var10006 = item.getSubtotal();
        BigDecimal var10007 = item.getSalesTax();
        BigDecimal var10008 = item.getTotal();
        Currency var10009 = item.getHandlerCurrency();
        var10000.insert(var10001, item, var2, var10004, var10005, var10006, var10007, var10008, var10009, ShipmentStatus.SHPT_DRAFT);
    }

    @Override
    public void add(List<Ivs> items) {

    }

    @Override
    public void edit( Ivs item) {
        this.dao.update(item);
    }

    @Override
    public void remove( Ivs item) {

        this.dao.delete(item.getName());
    }

    @Override
    public Optional<Ivs> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Ivs> findOne(Filter filter) {
        return Optional.empty();
    }

    @Override
    public List<Ivs> find(Filter filter) {
        return null;
    }

    public final Optional<Ivs> findByName( String name) {

        List<IvsRow> r = this.dao.queryByName(name);

        if (r != null && r.size() > 0) {
            List lineItemResult = this.dao.queryLineItems(name);
            IvsImpl var5 = new IvsImpl(r.get(0),lineItemResult);
            IvsImpl ivs = var5;
            return  Optional.of(ivs);
        }
        return Optional.empty();
    }



}