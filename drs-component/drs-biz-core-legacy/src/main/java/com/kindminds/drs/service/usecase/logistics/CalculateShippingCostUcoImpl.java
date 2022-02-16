package com.kindminds.drs.service.usecase.logistics;


import com.kindminds.drs.api.usecase.logistics.CalculateShippingCostUco;
import com.kindminds.drs.api.v1.model.logistics.ShippingCostData;
import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShipmentIvsDao;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.v1.model.impl.logistics.ShippingCostDataImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CalculateShippingCostUcoImpl implements CalculateShippingCostUco {

    @Autowired
    private MaintainShipmentIvsDao dao;

    private String checkForEUdestination(String destination) {
        if (destination.equalsIgnoreCase("EU")) {
            return "UK";
        }
        return destination;
    }

    public List<ShippingCostData> getShippingCostData(Ivs shipment) {
        String countryCode = checkForEUdestination(shipment.getDestinationCountry());

        List<ShippingCostData> shippingData = new ArrayList<>();
        for (IvsLineItem item : shipment.getLineItems()) {
            shippingData.add(getShippingCostDataPerLine(countryCode, item));
        }
        return shippingData;
    }

    public ShippingCostData getShippingCostDataPerLine(String countryCode, IvsLineItem item) {
        countryCode = checkForEUdestination(countryCode);


        Object[] skuData = dao.queryShippingCostData(item.getProductBaseCode(), countryCode);
        if (skuData == null) {
            //logger.info("ERROR: calculateInventoryShipmentCost");
            //logger.info("No dpim json data found for " + item.getSkuCode() + ", country: " + countryCode);
            return null;
        }
        String hsCode = (String) skuData[0];
        BigDecimal weight = new BigDecimal((String) skuData[1]);
        String weightUnit = (String) skuData[2];
        BigDecimal dim1 = new BigDecimal((String) skuData[3]);
        BigDecimal dim2 = new BigDecimal((String) skuData[4]);
        BigDecimal dim3 = new BigDecimal((String) skuData[5]);
        String dimUnit = (String) skuData[6];

        return new ShippingCostDataImpl(item.getSkuCode(),
                countryCode, hsCode, weight, weightUnit, dim1, dim2, dim3, dimUnit , item.getProductBaseCode());
    }

    @Override
    public String calculateInventoryShipmentCost(Ivs shipment) {
        List<ShippingCostData> shippingData = getShippingCostData(shipment);
        String countryCode = checkForEUdestination(shipment.getDestinationCountry());

//		IVS shipping cost = freight fee + truck cost + inventory placement fee + FCA related cost + HS code fee
        String result = "";
        BigDecimal truckCost = getTruckCost();
        result = result + "truckCost: " + truckCost + "<br>";
        BigDecimal hsCodeFee = getHSCodeFee(shipment, shippingData);
        result = result + "hsCodeFee: " + hsCodeFee + "<br>";
        BigDecimal fcaRelatedCost = getFCARelatedCost(shipment);
        result = result + "fcaRelatedCost: " + fcaRelatedCost + "<br>";
        BigDecimal freightfee = getFreightFee(shipment, shippingData);
        result = result + "freightfee: " + freightfee + "<br>";
        BigDecimal inventoryPlacementFee = getInventoryPlacementFee(countryCode,
                shipment.getLineItems(), shippingData);
        result = result + "inventoryPlacementFee: " + inventoryPlacementFee + "<br>";
        BigDecimal shippingCost = truckCost
                .add(hsCodeFee)
                .add(fcaRelatedCost)
                .add(freightfee)
                .add(inventoryPlacementFee).setScale(0, RoundingMode.UP);
        return "totalShippingCost: " + shippingCost + "<br>" + result;
    }

    private BigDecimal getTruckCost() {
//        truck cost = 0
        return BigDecimal.ZERO;
    }

    private BigDecimal getHSCodeFee(Ivs shipment, List<ShippingCostData> shippingData) {
//    	1. If destination is CA and UK, HS code fee =0
//		2. If FCA delivery location is Overseas warehouse, HS code fee =0
//		3. If shipping method is Courier, HS code fee = 0
//		4. Get HS code from product management table for each line
//		5. Get count of HS code fee
//		   if FCA unit price and HS code the same, count=1
//		   if FCA unit price the same but HS codes are different, count = number of HS codes
//		   if FCA unit price are different but HS codes the same, count = different count of FCA unit price
//		   if FCA unit price different and HS code also different, count = count of SKUs
//		6. HS code fee = TWD 264 * count of HS code fee
        String countryCode = checkForEUdestination(shipment.getDestinationCountry());
        if (countryCode.equalsIgnoreCase("CA")
                || countryCode.equalsIgnoreCase("UK")) {
            return BigDecimal.ZERO;
        }
        if (shipment.getFcaDeliveryLocationId().equals("4")) {
            return BigDecimal.ZERO;
        }
        if (shipment.getShippingMethod() == ShippingMethod.EXPRESS) {
            return BigDecimal.ZERO;
        }
        Set<String> pricesFCA = new HashSet<>();
        Set<String> codesHS = new HashSet<>();
        Set<String> skus = new HashSet<>();
        List<IvsLineItem> shipmentItems = shipment.getLineItems();
        for (int i = 0; i < shipmentItems.size(); i++) {
            skus.add(shipmentItems.get(i).getSkuCode());
            codesHS.add(shippingData.get(i).getHsCode());
            pricesFCA.add(shipmentItems.get(i).getUnitAmount());
        }
        int count = 1;
        if (codesHS.size() > 1 && pricesFCA.size() > 1) {
            count = skus.size();
        } else if (codesHS.size() > 1) {
            count = codesHS.size();
        } else if (pricesFCA.size() > 1) {
            count = pricesFCA.size();
        }
        return new BigDecimal(264 * count);
    }

    private BigDecimal getFCARelatedCost(Ivs shipment) {
//    	1. if FCA delivery location is Overseas warehouse, insurance fee = 0
//    	2. FCA related cost = insurance fee
        if (shipment.getFcaDeliveryLocationId().equals("4")) {
            return BigDecimal.ZERO;
        }
        return getInsuranceFee(shipment.getShippingMethod(), shipment.getLineItems())
                .setScale(0, RoundingMode.HALF_UP);
    }

    private BigDecimal getInsuranceFee(ShippingMethod shippingMethod, List<IvsLineItem> lineItems) {
//    	1. If shipping method is Courier, Insurance fee = max( sum(FCA unit price * quantity) * 0.17%, 400) TWD
//    	2. if shipping method is surface freight or air freight, Insurance fee = sum(FCA unit price * quantity) * 0.07%
        BigDecimal insuranceFee = new BigDecimal(400);
        BigDecimal sumAmount = BigDecimal.ZERO;
        if (shippingMethod == ShippingMethod.EXPRESS) {
            for (IvsLineItem item : lineItems) {
                sumAmount = sumAmount.add(
                        new BigDecimal(item.getUnitAmount())
                                .multiply(new BigDecimal(item.getQuantity()))
                                .multiply(new BigDecimal(0.0017))
                                .setScale(6, RoundingMode.HALF_UP));
            }
            if (insuranceFee.compareTo(sumAmount) > 0) {
                return insuranceFee;
            }
        } else if (shippingMethod == ShippingMethod.AIR_CARGO
                || shippingMethod == ShippingMethod.SEA_FREIGHT) {
            for (IvsLineItem item : lineItems) {
                sumAmount = sumAmount.add(
                        new BigDecimal(item.getUnitAmount())
                                .multiply(new BigDecimal(item.getQuantity()))
                                .multiply(new BigDecimal(0.0007))
                                .setScale(6, RoundingMode.HALF_UP));
            }
        }
        return sumAmount;
    }

    private BigDecimal getFreightFee(Ivs shipment, List<ShippingCostData> shippingData) {
//    	1. if FCA delivery location is Overseas warehouse, freight fee =0
//    	2. Freight fee = chargeable weight * freight fee rate
        if (shipment.getFcaDeliveryLocationId().equals("4")) return BigDecimal.ZERO;

        String countryCode = checkForEUdestination(shipment.getDestinationCountry());
        BigDecimal chargeableWeight = getChargeableWeight(shipment.getShippingMethod(), shipment.getLineItems());
        BigDecimal freightFeeRate = getFreightFeeRate(
                shipment.getShippingMethod(), countryCode,
                isMergedShipment(shippingData), chargeableWeight);
        System.out.println("freightFeeRate: " + freightFeeRate);
        System.out.println("chargeableWeight: " + chargeableWeight);
        return chargeableWeight.multiply(freightFeeRate).setScale(0, RoundingMode.HALF_UP);
    }

    private BigDecimal getFreightFeeRate(ShippingMethod shippingMethod, String destination,
                                         boolean msFlag, BigDecimal chargeableWeight) {
//    	According to destination, shipping method, MS_flag, find the freight fee rate for the max(min.effweight)< chargeable weight
//    	Freight fee rate table
        return dao.queryFreightFeeRate(destination, shippingMethod, msFlag, chargeableWeight);
    }

    private BigDecimal getChargeableWeight(ShippingMethod shippingMethod, List<IvsLineItem> lineItems) {
//    	Charged weight = max(IVS GW, IVS VW)
        BigDecimal ivsGrossWeight = calculateIVSGrossWeight(shippingMethod, lineItems);
        BigDecimal ivsVolumeWeight = calculateIVSVolumeWeight(shippingMethod, lineItems);
        System.out.println("ivsGrossWeight: " + ivsGrossWeight);
        System.out.println("ivsVolumeWeight: " + ivsVolumeWeight);
        if (ivsGrossWeight.compareTo(ivsVolumeWeight) > 0) {
            return ivsGrossWeight;
        }
        return ivsVolumeWeight;
    }

    private BigDecimal calculateIVSGrossWeight(ShippingMethod shippingMethod, List<IvsLineItem> lineItems) {
//    	1. calculate Line GW= Box GW * number of cartons
//    	2. calculate IVS GW = sum (Line GW) with the same IVS
        BigDecimal ivsGrossWeight = BigDecimal.ZERO;
        for (IvsLineItem item : lineItems) {
            BigDecimal lineGW = calculateBoxGrossWeight(
                    shippingMethod, new BigDecimal(item.getPerCartonGrossWeightKg()))
                    .multiply(new BigDecimal(item.getCartonCounts()));
            ivsGrossWeight = ivsGrossWeight.add(lineGW);
        }
        return ivsGrossWeight;
    }

    private BigDecimal calculateBoxGrossWeight(ShippingMethod shippingMethod, BigDecimal cartonGrossWeight) {
//      Box GW
//      1. if shipping method is Courier,
//          Box GW = ROUNDUP (gross weight per carton/0.5) * 0.5
//      2. if shipping method is Surface freight or Air freight,
//          Box GW = gross weight per container
        if (shippingMethod == ShippingMethod.EXPRESS) {
            return cartonGrossWeight.divide(new BigDecimal(0.5), 0, RoundingMode.UP)
                    .multiply(new BigDecimal(0.5));
        } else if (shippingMethod == ShippingMethod.AIR_CARGO
                || shippingMethod == ShippingMethod.SEA_FREIGHT) {
            return cartonGrossWeight;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateIVSVolumeWeight(ShippingMethod shippingMethod, List<IvsLineItem> lineItems) {
//    	IVS VW(IVS Volume Weight)
//    	1. If shipping method is Courier,
//    	    1-1. calculate Box volume weight = roundup(Box CBM * 10^6/volume weight coefficient/0.5,0)*0.5
//    	    1-2 calculate Line volume weight = Box volume weight * number of cartons
//    	    1-3 IVS volume weight = sum (Line volume weight ) with the same IVS
//    	2. If shipping method is Surface freight or Air freight
//    	    2-1. calculate Line CBM = Box CBM * number of cartons
//    	    2-2. calculate IVS CBM = sum (Line CBM) with the same IVS
//    	    2-3. calculate IVS volume weight = roundup(IVS CBM*10^6/volume weight coefficient,1)
        BigDecimal ivsVolumeWeight = BigDecimal.ZERO;
        BigDecimal volumeWeightCoefficient = getVolumeWeightCoefficient(shippingMethod);
        if (shippingMethod == ShippingMethod.EXPRESS) {
            for (IvsLineItem item : lineItems) {
                BigDecimal boxVolumeWeight = calculateBoxCBM(item)
                        .multiply(new BigDecimal(1000000))
                        .divide(volumeWeightCoefficient, 8, RoundingMode.HALF_UP)
                        .divide(new BigDecimal(0.5), 0, RoundingMode.UP)
                        .multiply(new BigDecimal(0.5));
                BigDecimal lineVolumeWeight = boxVolumeWeight
                        .multiply(new BigDecimal(item.getCartonCounts()));
                ivsVolumeWeight = ivsVolumeWeight.add(lineVolumeWeight);
            }
        } else if (shippingMethod == ShippingMethod.AIR_CARGO
                || shippingMethod == ShippingMethod.SEA_FREIGHT) {
            for (IvsLineItem item : lineItems) {
                BigDecimal lineCBM = calculateBoxCBM(item)
                        .multiply(new BigDecimal(item.getCartonCounts()));
                ivsVolumeWeight = ivsVolumeWeight.add(lineCBM);
            }
            ivsVolumeWeight = ivsVolumeWeight.multiply(new BigDecimal(1000000))
                    .divide(volumeWeightCoefficient, 1, RoundingMode.UP);
        }
        return ivsVolumeWeight;
    }

    public BigDecimal calculateBoxCBM(IvsLineItem lineItem) {
//      Box CBM = round(carton dimension Long side* medium side* short side/10^6 , 3 )
        BigDecimal dim1 = new BigDecimal(lineItem.getCartonDimensionCm1());
        BigDecimal dim2 = new BigDecimal(lineItem.getCartonDimensionCm2());
        BigDecimal dim3 = new BigDecimal(lineItem.getCartonDimensionCm3());
        return dim1.multiply(dim2).multiply(dim3).divide(new BigDecimal(1000000), 3, RoundingMode.HALF_UP);
    }

    private BigDecimal getVolumeWeightCoefficient(ShippingMethod shippingMethod) {
//    	1. If shipping method is Courier, volume weight coefficient = 5000
//    	2. If shipping method is Surface Freight or Air Freight, volume weight coefficient = 6000
        if (shippingMethod == ShippingMethod.EXPRESS) {
            return new BigDecimal(5000);
        } else if (shippingMethod == ShippingMethod.AIR_CARGO
                || shippingMethod == ShippingMethod.SEA_FREIGHT) {
            return new BigDecimal(6000);
        }
        return BigDecimal.ZERO;
    }

    private Boolean isMergedShipment(List<ShippingCostData> shippingData) {
        //If containsOversize is TRUE or containsDGR is True, MS_flag is False. Otherwise is True.
        return !(containsOversized(shippingData) || containsDangerousGoods(shippingData));
    }

    private Boolean containsOversized(List<ShippingCostData> shippingData) {
        for (ShippingCostData item : shippingData) {
            if (isOversize(item)) {
                return true;
            }
        }
        return false;
    }

    public Boolean isOversize(ShippingCostData item) {
//    	1. Get Product package dimension from product management table
//        PDL (product package dimension long side)
//        PDM (product package dimension medium side)
//        PDS (product package dimension short side)
//        Unit (length unit)
//	    2. transfer the dimensions into the correspondent sizing unit according to the destination
//	        2-1. If destination is US, the target sizing unit is inch
//	        2-2  If destination is UK and CA, the target sizing unit is cm.
//	    3. Transfer the dimensions following the unit conversion rule below,
//	        1 cm = 0.3937 inch
//	    4. IsOversize=FALSE if meeting following criteria, others is TRUE
//	       4-1 US: PDL<=18 inch and PDM<=14 inch and PDS<=8 inch
//	       4-2 CA: PDL<= 45cm and PDM<=35cm and PDS<=20cm
//	       4-3 UK: PDL<= 45cm and PDM<=34cm and PDS<=26cm
        String country = item.getCountryCode();
        BigDecimal packageDimLong = item.getPackageDimLong();
        BigDecimal packageDimMedium = item.getPackageDimMedium();
        BigDecimal packageDimShort = item.getPackageDimShort();

        if ("US".equalsIgnoreCase(country)) {
            if ("cm".equalsIgnoreCase(item.getPackageDimUnit())) {
                packageDimLong = packageDimLong.multiply(new BigDecimal(0.3937));
                packageDimMedium = packageDimMedium.multiply(new BigDecimal(0.3937));
                packageDimShort = packageDimShort.multiply(new BigDecimal(0.3937));
            }

            return packageDimLong.compareTo(new BigDecimal(18)) > 0
                    || packageDimMedium.compareTo(new BigDecimal(14)) > 0
                    || packageDimShort.compareTo(new BigDecimal(8)) > 0;

        } else if ("UK".equalsIgnoreCase(country)
                || "CA".equalsIgnoreCase(country)) {
            if ("in".equalsIgnoreCase(item.getPackageDimUnit())) {
                packageDimLong = packageDimLong.divide(new BigDecimal(0.3937), 6, RoundingMode.HALF_UP);
                packageDimMedium = packageDimMedium.divide(new BigDecimal(0.3937), 6, RoundingMode.HALF_UP);
                packageDimShort = packageDimShort.divide(new BigDecimal(0.3937), 6, RoundingMode.HALF_UP);
            }

            if ("UK".equalsIgnoreCase(country)) {

                return packageDimLong.compareTo(new BigDecimal(45)) > 0
                        || packageDimMedium.compareTo(new BigDecimal(34)) > 0
                        || packageDimShort.compareTo(new BigDecimal(26)) > 0;

            } else {

                return packageDimLong.compareTo(new BigDecimal(45)) > 0
                        || packageDimMedium.compareTo(new BigDecimal(35)) > 0
                        || packageDimShort.compareTo(new BigDecimal(20)) > 0;
            }
        }
        return false;
    }

    private Boolean containsDangerousGoods(List<ShippingCostData> shippingData) {
//    	If battery type is Lithium ion or Lithium metal, IsDGR is TRUE, others is FALSE
        for (ShippingCostData item : shippingData) {
            if (isDangerousGoods(item.getProductBaseCode())) {
                return true;
            }
        }
        return false;
    }


    public Boolean isDangerousGoods(String productBaseCode) {


        String batteryType = dao.queryBatteryType(productBaseCode);
        return "LithiumIon".equalsIgnoreCase(batteryType)
                || "LithiumMetal".equalsIgnoreCase(batteryType);
    }


    public BigDecimal getInventoryPlacementFee(String destination,
                                                List<IvsLineItem> lineItems, List<ShippingCostData> shippingData) {
//    	1. If destination is CA and UK, inventory placement fee=0
//		2. Get Placement fee unit price
//		    2-1 isOversize=FALSE,
//		          if product package weight <= 1 lb ==> TWD 9
//		          if product package weight <= 2 lb ==> TWD 12
//		          if product package weight > 2 lb ==> TWD 12 + TWD 3 per lb
//		    2-2 isOversize=TRUE,
//		          if product package weight <= 5 lb ==> TWD 39
//		          if product package weight > 5 lb ==> TWD 39 + TWD 6 per lb
//		3. Inventory placement fee = sum(Placement fee unit price * Quantity) with the same IVS
        destination = checkForEUdestination(destination);

        BigDecimal inventoryPlacementFee = BigDecimal.ZERO;
        for (int i = 0; i < lineItems.size(); i++) {
            inventoryPlacementFee = inventoryPlacementFee.add(
                    getUnitPlacmentFee(destination, lineItems.get(i), shippingData.get(i)));
        }

        return inventoryPlacementFee;
    }

    public BigDecimal getUnitPlacmentFee(String destination,
                                         IvsLineItem lineItem, ShippingCostData costData) {

        BigDecimal unitPlacementFee = BigDecimal.ZERO;

        if (destination.equalsIgnoreCase("US")) {

            BigDecimal packageWeightlbs = getPackageWeightPounds(
                    costData.getPackageWeight(), costData.getPackageWeightUnit());
            if (isOversize(costData)) {
                unitPlacementFee = new BigDecimal(39);
                if (packageWeightlbs.compareTo(new BigDecimal(5)) > 0) {
                    BigDecimal extraWeight = packageWeightlbs
                            .subtract(new BigDecimal(5)).setScale(0, RoundingMode.UP);
                    unitPlacementFee = unitPlacementFee
                            .add(extraWeight.multiply(new BigDecimal(6)));
                }
            } else {
                if (packageWeightlbs.compareTo(BigDecimal.ONE) <= 0) {
                    unitPlacementFee = new BigDecimal(9);
                } else if (packageWeightlbs.compareTo(new BigDecimal(2)) <= 0) {
                    unitPlacementFee = new BigDecimal(12);
                } else {
                    unitPlacementFee = new BigDecimal(12);
                    BigDecimal extraWeight = packageWeightlbs
                            .subtract(new BigDecimal(2)).setScale(0, RoundingMode.UP);
                    unitPlacementFee = unitPlacementFee
                            .add(extraWeight.multiply(new BigDecimal(3)));
                }
            }
            unitPlacementFee = unitPlacementFee.multiply(new BigDecimal(lineItem.getQuantity()));
        }

        return unitPlacementFee;
    }

    private BigDecimal getPackageWeightPounds(BigDecimal weight, String weightUnit) {
//    	1. Get Product display weight from product manage table.
//    	2. converse the product display weight into lb
//    	1g = 0.002204622 lb
//    	1kg = 2.204622 lb
//    	1oz = 0.062499993 lb
        if ("g".equalsIgnoreCase(weightUnit)) {
            return weight.multiply(new BigDecimal(0.002204622));
        }
        if ("kg".equalsIgnoreCase(weightUnit)) {
            return weight.multiply(new BigDecimal(2.204622));
        }
        if ("oz".equalsIgnoreCase(weightUnit)) {
            return weight.multiply(new BigDecimal(0.062499993));
        }
        return BigDecimal.ZERO;
    }

}
