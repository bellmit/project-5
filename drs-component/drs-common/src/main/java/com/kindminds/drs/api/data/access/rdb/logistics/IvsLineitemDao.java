package com.kindminds.drs.api.data.access.rdb.logistics;

import com.kindminds.drs.api.data.row.logistics.IvsLineitemRow;


import java.util.List;


public  interface IvsLineitemDao {


    IvsLineitemRow query( String name, int boxNum, int mixedBoxLineSeq);

    void updateLineItemStatus(int id,  String status);

    
    List queryByName( String name);


}
