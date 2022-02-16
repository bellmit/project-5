package com.kindminds.drs.service.usecase.logistics;


import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.repo.logistics.IvsLineitemRepo;
import com.kindminds.drs.api.v2.biz.domain.model.repo.logistics.IvsRepo;
import com.kindminds.drs.core.biz.repo.logistics.IvsLineitemRepoImpl;
import com.kindminds.drs.core.biz.repo.logistics.IvsRepoImpl;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.core.query.logistics.MaintainShipmentIvsQuerySrv;
import com.kindminds.drs.core.service.command.MaintainShipmentIvsCmdSrv;

import com.kindminds.drs.service.security.MockAuth;
import com.kindminds.drs.api.usecase.logistics.MaintainShipmentIvsUco;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestMaintainShipmentIvsUcoV2 {

   @Autowired
    private MaintainShipmentIvsUco uco;

    @Autowired private MaintainShipmentIvsCmdSrv cmdSrv;
    @Autowired private MaintainShipmentIvsQuerySrv querySrv;

    @Autowired private AuthenticationManager authenticationManager;

    @Test
    public void test(){


        IvsRepo repo = new IvsRepoImpl();

        Optional<Ivs> opIvs = ((IvsRepoImpl) repo).findByName("IVS-K486-DRAFT8");

        Ivs ivs = opIvs.get();



        System.out.println(ivs.getName());

        System.out.println(ivs.getInternalNote());

        System.out.println(ivs.getDateCreated());

       //((IvsImpl)(ivs)).changeNote();

        //repo.edit(ivs);


    }

    @Test
    public void testItem(){

        IvsLineitemRepo itemRepo = new IvsLineitemRepoImpl();
        Optional<IvsLineItem> opItem =
                ((IvsLineitemRepoImpl) itemRepo).findByName("IVS-K486-16",1,0);

        if(opItem.isPresent()){
            System.out.println(opItem.get().getBoxNum());
            System.out.println(opItem.get().getMixedBoxLineSeq());
            System.out.println(opItem.get().getCartonCounts());
            System.out.println(opItem.get().getId());

            IvsLineItem item = opItem.get();
           // item.verifyProduct();

           // itemRepo.edit(item);


        }


    }


    @Test
    public void testItem2(){

        MockAuth.login(authenticationManager, "hanchor.kmi@tw.drs.network", "stu3iSEt6lw");

        ShipmentIvs ivs =  querySrv.get("IVS-K486-16");


        ivs.getLineItems().forEach(x->{
            System.out.println(x.getBoxNum());
            System.out.println(x.getMixedBoxLineSeq());
        });



    }

}