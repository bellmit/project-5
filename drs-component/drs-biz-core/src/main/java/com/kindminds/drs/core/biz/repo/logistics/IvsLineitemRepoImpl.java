package com.kindminds.drs.core.biz.repo.logistics;

import com.kindminds.drs.Filter;
import com.kindminds.drs.api.data.row.logistics.IvsLineitemRow;
import com.kindminds.drs.api.data.row.logistics.IvsProdDocRequirementRow;


import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.repo.logistics.IvsLineitemRepo;
import com.kindminds.drs.core.biz.logistics.IvsLineItemImpl;
import com.kindminds.drs.api.data.access.nosql.mongo.IvsProductDocRequirementDao;
import com.kindminds.drs.api.data.access.rdb.logistics.IvsLineitemDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.logistics.IvsProductDocRequirementDaoImpl;
import com.kindminds.drs.service.util.SpringAppCtx;

import java.util.*;

public  class IvsLineitemRepoImpl implements IvsLineitemRepo {


    private IvsLineitemDao dao = SpringAppCtx.get().getBean(IvsLineitemDao.class);

    private IvsProductDocRequirementDao docReqDao = new IvsProductDocRequirementDaoImpl();


    @Override
    public void add(IvsLineItem item) {

    }

    @Override
    public void add(List<IvsLineItem> items) {

    }

    public void edit(IvsLineItem item) {

        //todo arthur need refactor

       dao.updateLineItemStatus(item.getId() ,item.getProductVerificationStatus().getValue());

       //todo arthur
       /*
        List<IvsProdDocRequirementRow> doc =
                docReqDao.find(item.getIvsName() , item.getBoxNum(),item.getMixedBoxLineSeq());

        if(doc == null || doc.isEmpty()){
           String j = docReqDao.create(item.getDocRequirement());
        }else{

          String  j =  docReqDao.update(doc.get(0).get_id().toString(), item.getDocRequirement());
        }
        */


    }

    @Override
    public void remove(IvsLineItem item) {

    }

    @Override
    public Optional<IvsLineItem> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<IvsLineItem> findOne(Filter filter) {
        return Optional.empty();
    }

    @Override
    public List<IvsLineItem> find(Filter filter) {
        return null;
    }


    public List<IvsLineItem> findByName(String name) {

         List<IvsLineItem> lineItems  = new ArrayList<IvsLineItem>();
        List<IvsLineitemRow> rList =  dao.queryByName(name);
         rList.forEach(it ->{
             IvsLineItem item = new IvsLineItemImpl(name , it);
             lineItems.add(item);
         });

         return lineItems;

    }


  public   Optional<IvsLineItem> findByName(String name ,int boxNum ,int mixedBoxLineSeq)  {

        IvsLineitemRow r =  dao.query(name , boxNum , mixedBoxLineSeq);

        if(r != null ){
            IvsLineItem item = new IvsLineItemImpl(name , r);
            return Optional.of(item);

        }else{
            return Optional.empty();
        }


    }

}