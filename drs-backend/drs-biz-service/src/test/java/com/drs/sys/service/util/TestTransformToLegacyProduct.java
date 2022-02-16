package com.drs.sys.service.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.core.biz.product.ProductImpl;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v1.model.product.BaseProduct;

import com.kindminds.drs.persist.TransformToProductV1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestTransformToLegacyProduct {

    @Autowired
    MaintainProductBaseUco uco;

    @Test
    public void testTransform() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObject = mapper.readTree("{\"supplierKcode\":\"K486\",\"productNameEnglish\":\"Test Name\",\"baseProductCode\":\"BP-K486-123\",\"allergy\":false,\"allergyName\":\"\",\"materials\":\"[{\\\"material\\\":\\\"\\\"}]\",\"referenceLinks\":\"[{\\\"referenceLink\\\":\\\"\\\"}]\",\"applicableRegionBP\":[\"UK\",\"CA\"],\"productWithVariation\":\"1\",\"variationType1\":\"color\",\"variationType2\":\"\",\"products\":\"[{\\\"type1\\\":\\\"color\\\",\\\"type1Value\\\":\\\"GREEN\\\",\\\"type2\\\":\\\"\\\",\\\"type2Value\\\":\\\"\\\",\\\"GTINType\\\":\\\"EAN\\\",\\\"GTINTypeLength\\\":13,\\\"GTINValue\\\":\\\"\\\",\\\"providedByDRS\\\":false,\\\"SKU\\\":\\\"456\\\",\\\"DRSSKU\\\":\\\"\\\",\\\"applicableRegion\\\":{\\\"UK\\\":{\\\"applicable\\\":true,\\\"status\\\":\\\"selected\\\"},\\\"US\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"},\\\"CA\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"},\\\"DE\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"},\\\"IT\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"},\\\"FR\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"},\\\"ES\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"}},\\\"applicableRegionList\\\":[\\\"UK\\\"],\\\"FCAPrice\\\":\\\"\\\",\\\"packageDimension1\\\":\\\"\\\",\\\"packageDimension2\\\":\\\"\\\",\\\"packageDimension3\\\":\\\"\\\",\\\"packageDimensionUnit\\\":\\\"\\\",\\\"packageWeight\\\":\\\"\\\",\\\"packageWeightUnit\\\":\\\"\\\",\\\"status\\\":\\\"edit\\\"},{\\\"type1\\\":\\\"color\\\",\\\"type1Value\\\":\\\"RED\\\",\\\"type2\\\":\\\"\\\",\\\"type2Value\\\":\\\"\\\",\\\"GTINType\\\":\\\"EAN\\\",\\\"GTINTypeLength\\\":13,\\\"GTINValue\\\":\\\"\\\",\\\"providedByDRS\\\":false,\\\"SKU\\\":\\\"789\\\",\\\"DRSSKU\\\":\\\"\\\",\\\"applicableRegion\\\":{\\\"CA\\\":{\\\"applicable\\\":true,\\\"status\\\":\\\"selected\\\"},\\\"US\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"},\\\"UK\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"},\\\"DE\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"},\\\"IT\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"},\\\"FR\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"},\\\"ES\\\":{\\\"applicable\\\":false,\\\"status\\\":\\\"unselected\\\"}},\\\"applicableRegionList\\\":[\\\"CA\\\"],\\\"FCAPrice\\\":\\\"\\\",\\\"packageDimension1\\\":\\\"\\\",\\\"packageDimension2\\\":\\\"\\\",\\\"packageDimension3\\\":\\\"\\\",\\\"packageDimensionUnit\\\":\\\"\\\",\\\"packageWeight\\\":\\\"\\\",\\\"packageWeightUnit\\\":\\\"\\\",\\\"status\\\":\\\"edit\\\"}]\",\"medical\":true,\"otherWirelessTechs\":\"[{\\\"otherWirelessTech\\\":\\\"\\\"}]\",\"referenceFiles\":\"[]\",\"hazardousMaterials\":\"[{\\\"hazardousMaterial\\\":\\\"\\\"}]\",\"batteries\":\"[]\",\"note\":\"\"}");
        JsonNode jsonMarketing = mapper.readTree("{\"jsonData\":{\"country\":\"UK\",\"hsCode\":\"\",\"dutyRate\":\"\",\"note\":\"\",\"products\":\"[{\\\"SKU\\\":\\\"456\\\",\\\"DRSSKU\\\":\\\"\\\",\\\"type1\\\":\\\"color\\\",\\\"type1Value\\\":\\\"GREEN\\\",\\\"type2\\\":\\\"\\\",\\\"type2Value\\\":\\\"\\\",\\\"marketplace\\\":[{\\\"name\\\":\\\"Amazon UK\\\",\\\"applied\\\":false,\\\"SSBP\\\":\\\"\\\",\\\"SSBPtax\\\":\\\"\\\"}],\\\"MSRP\\\":\\\"\\\",\\\"MSRPtax\\\":\\\"\\\",\\\"variationProductQuantity\\\":\\\"\\\",\\\"packageDimension1\\\":\\\"\\\",\\\"packageDimension2\\\":\\\"\\\",\\\"packageDimension3\\\":\\\"\\\",\\\"packageDimensionUnit\\\":\\\"\\\",\\\"packageWeight\\\":\\\"\\\",\\\"packageWeightUnit\\\":\\\"\\\",\\\"cartonPackageDimension1\\\":\\\"\\\",\\\"cartonPackageDimension2\\\":\\\"\\\",\\\"cartonPackageDimension3\\\":\\\"\\\",\\\"cartonPackageDimensionUnit\\\":\\\"\\\",\\\"cartonPackageWeight\\\":\\\"\\\",\\\"cartonPackageWeightUnit\\\":\\\"\\\",\\\"plasticBagWarningLabel\\\":\\\"No\\\",\\\"disposeOfUnfulfillableInventory\\\":\\\"remove\\\",\\\"status\\\":\\\"selected\\\"}]\",\"productWithVariation\":\"1\"}}");

        Product product = ProductImpl.createCoreProduct("K486", "BP-K486-123", jsonObject, jsonMarketing);

        BaseProduct baseProduct = TransformToProductV1.transform(product);

        assert baseProduct != null;

        uco.insert(baseProduct, "BP-K486-123");
    }
}
