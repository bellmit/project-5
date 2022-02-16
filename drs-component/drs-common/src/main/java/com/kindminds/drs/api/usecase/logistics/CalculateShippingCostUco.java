package com.kindminds.drs.api.usecase.logistics;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v1.model.logistics.ShippingCostData;

import java.math.BigDecimal;
import java.util.List;

public interface CalculateShippingCostUco {

    String calculateInventoryShipmentCost(Ivs shipment);

    List<ShippingCostData> getShippingCostData(Ivs shipment);

    ShippingCostData getShippingCostDataPerLine(String countryCode, IvsLineItem item);

    BigDecimal calculateBoxCBM(IvsLineItem lineItem);

    BigDecimal getInventoryPlacementFee(
            String destination, List<IvsLineItem> lineItems, List<ShippingCostData> shippingData);

    BigDecimal getUnitPlacmentFee(String destination,
                                  IvsLineItem lineItem, ShippingCostData costData);

    Boolean isOversize(ShippingCostData item);

    Boolean isDangerousGoods(String skuCode);



}
