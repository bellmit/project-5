package persist.data.access.nosql.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestMongoProduct {

    ObjectMapper mapper = new ObjectMapper();

    private ProductDao dao = new ProductDao();

    ObjectId oid=new ObjectId("5ffbc08024a4fa23b0932638");
    String id= oid.toHexString();
    String pn ="Nicolas Cage PillowCase";

    private String testBPBedSheet ="{\n" +
            "  \"supplierId\": 190,\n" +
            "  \"category\": \"bedding\",\n" +
            "  \"brandNameCH\": \"CH\",\n" +
            "  \"brandNameEN\": \"SuperComfy\",\n" +
            "  \"productNameCH\": \"無\",\n" +
            "  \"productNameEN\": \"Nicolas Cage BedSheets\",\n" +
            "  \"variationTheme\": \"Color\",\n" +
            "  \"multiTheme\": [],\n" +
            "  \"totalSkus\": 2,\n" +
            "  \"pageSize\": 3,\n" +
            "  \"currentIndex\": 1,\n" +
            "  \"bpStatus\": \"applied\",\n" +
            "\"skus\": [\n" +
            "    { \"actions\": [\"a\",\"b\"], \"sellerSku\": \"K618\", " +
            "\"productId\": {\"name\": \"Product ID\", \"value\": \"123456789\"}, " +
            "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"EAN\"}, " +
            "\"variable\": {\"name\": \"Variable\", \"value\": \"small\"}, " +
            "\"pageIndex\": 1,\"retailPrice\": 120, \"fbaQuantity\": 0, " +
            "\"settlementsPeriodOrder\":55, \"lastSevenDaysOrder\":20, \"editable\": false, " +
            "\"status\":\"銷售中\" },\n" +
            "    { \"actions\": [\"a\",\"b\"], \"sellerSku\": \"K618\", " +
            "\"productId\": {\"name\": \"Product ID\", \"value\": \"500000000\"}, " +
            "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"EAN\"}, " +
            "\"variable\": {\"name\": \"Variable\", \"value\": \"small\"}, " +
            "\"pageIndex\": 1,\"retailPrice\": 120, \"fbaQuantity\": 0, " +
            "\"settlementsPeriodOrder\":55, \"lastSevenDaysOrder\":20, \"editable\": false, " +
            "\"status\":\"other\" }\n" +
            "  ],"+
            "  \"batteryCategory\": \"100000V\",\n" +
            "  \"chargeable\": \"no\",\n" +
            "  \"un38\": \"yes\",\n" +
            "  \"msds\": \"yes\",\n" +
            "  \"droptest\": \"yes\",\n" +
            "  \"allergic\": \"no\",\n" +
            "  \"dangerous\": \"no\",\n" +
            "  \"medical\": \"no\",\n" +
            "  \"battery\": \"no\",\n" +
            "  \"materialSelected\": \"無(None)\",\n" +
            "  \"aircargo\": \"yes\",\n" +
            "  \"seafreight\": \"yes\",\n" +
            "  \"certification\": \"yes\",\n" +
            "  \"formatSelected\": \"Select...\",\n" +
            "  \"packageSelected\": \"Select...\",\n" +
            "  \"wirelessSelected\": \"Select...\",\n" +
            "  \"showOthersInput\": false,\n" +
            "  \"startDate\":\"2020-12-31\",\n" +
            "  \"allFiles\": 0,\n" +
            "  \"batteryQuantity\": \"無電池\",\n" +
            "  \"batteryVoltage\": \"電池電壓\",\n" +
            "  \"batteryCapacitance\": \"電池組電容量\",\n" +
            "  \"batteryNetWeight\": \"電池組淨重\"\n" +
            "}";

    private String testBPPillowCase ="{\n" +
            "  \"supplierId\": 190,\n" +
            "  \"category\": \"bedding\",\n" +
            "  \"brandNameCH\": \"CH\",\n" +
            "  \"brandNameEN\": \"SuperComfy\",\n" +
            "  \"productNameCH\": \"無\",\n" +
            "  \"productNameEN\": \"Nicolas Cage PillowCase\",\n" +
            "  \"variationTheme\": \"Color\",\n" +
            "  \"multiTheme\": [],\n" +
            "  \"totalSkus\": 1,\n" +
            "  \"pageSize\": 3,\n" +
            "  \"currentIndex\": 2,\n" +
            "  \"bpStatus\": \"applied\",\n" +
            "\"skus\": [\n" +
            "    { \"actions\": [\"a\",\"b\"], \"sellerSku\": \"K618\", " +
            "\"productId\": {\"name\": \"Product ID\", \"value\": \"500000000\"}, " +
            "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"EAN\"}, " +
            "\"variable\": {\"name\": \"Variable\", \"value\": \"small\"}, " +
            "\"pageIndex\": 1,\"retailPrice\": 120, \"fbaQuantity\": 0, " +
            "\"settlementsPeriodOrder\":55, \"lastSevenDaysOrder\":20, \"editable\": false, " +
            "\"status\":\"銷售中\" }\n" +
            "  ],"+
            "  \"batteryCategory\": \"100000V\",\n" +
            "  \"chargeable\": \"no\",\n" +
            "  \"un38\": \"yes\",\n" +
            "  \"msds\": \"yes\",\n" +
            "  \"droptest\": \"yes\",\n" +
            "  \"allergic\": \"no\",\n" +
            "  \"dangerous\": \"no\",\n" +
            "  \"medical\": \"no\",\n" +
            "  \"battery\": \"no\",\n" +
            "  \"materialSelected\": \"無(None)\",\n" +
            "  \"aircargo\": \"yes\",\n" +
            "  \"seafreight\": \"yes\",\n" +
            "  \"certification\": \"yes\",\n" +
            "  \"formatSelected\": \"Select...\",\n" +
            "  \"packageSelected\": \"Select...\",\n" +
            "  \"wirelessSelected\": \"Select...\",\n" +
            "  \"showOthersInput\": false,\n" +
            "  \"startDate\":\"2021-01-10\",\n" +
            "  \"allFiles\": 0,\n" +
            "  \"batteryQuantity\": \"無電池\",\n" +
            "  \"batteryVoltage\": \"電池電壓\",\n" +
            "  \"batteryCapacitance\": \"電池組電容量\",\n" +
            "  \"batteryNetWeight\": \"電池組淨重\"\n" +
            "}";

    private String testBPBlanket ="{\n" +
            "  \"supplierId\": 190,\n" +
            "  \"category\": \"bedding\",\n" +
            "  \"brandNameCH\": \"CH\",\n" +
            "  \"brandNameEN\": \"SuperComfy\",\n" +
            "  \"productNameCH\": \"無\",\n" +
            "  \"productNameEN\": \"Nicolas Cage Blanket Thick\",\n" +
            "  \"variationTheme\": \"Color\",\n" +
            "  \"multiTheme\": [],\n" +
            "  \"totalSkus\": 1,\n" +
            "  \"pageSize\": 3,\n" +
            "  \"currentIndex\": 3,\n" +
            "  \"bpStatus\": \"applied\",\n" +
            "\"skus\": [\n" +
            "    { \"actions\": [\"a\",\"b\"], \"sellerSku\": \"K618\", " +
            "\"productId\": {\"name\": \"Product ID\", \"value\": \"500000000\"}, " +
            "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"EAN\"}, " +
            "\"variable\": {\"name\": \"Variable\", \"value\": \"small\"}, " +
            "\"pageIndex\": 1,\"retailPrice\": 120, \"fbaQuantity\": 0, " +
            "\"settlementsPeriodOrder\":55, \"lastSevenDaysOrder\":20, \"editable\": false, " +
            "\"status\":\"銷售中\" }\n" +
            "  ],"+
            "  \"batteryCategory\": \"100000V\",\n" +
            "  \"chargeable\": \"no\",\n" +
            "  \"un38\": \"yes\",\n" +
            "  \"msds\": \"yes\",\n" +
            "  \"droptest\": \"yes\",\n" +
            "  \"allergic\": \"no\",\n" +
            "  \"dangerous\": \"no\",\n" +
            "  \"medical\": \"no\",\n" +
            "  \"battery\": \"no\",\n" +
            "  \"materialSelected\": \"無(None)\",\n" +
            "  \"aircargo\": \"yes\",\n" +
            "  \"seafreight\": \"yes\",\n" +
            "  \"certification\": \"yes\",\n" +
            "  \"formatSelected\": \"Select...\",\n" +
            "  \"packageSelected\": \"Select...\",\n" +
            "  \"wirelessSelected\": \"Select...\",\n" +
            "  \"showOthersInput\": false,\n" +
            "  \"startDate\":\"2021-01-01\",\n" +
            "  \"allFiles\": 0,\n" +
            "  \"batteryQuantity\": \"無電池\",\n" +
            "  \"batteryVoltage\": \"電池電壓\",\n" +
            "  \"batteryCapacitance\": \"電池組電容量\",\n" +
            "  \"batteryNetWeight\": \"電池組淨重\"\n" +
            "}";

    private String testBPBodyPillow ="{\n" +
            "  \"supplierId\": 190,\n" +
            "  \"category\": \"bedding\",\n" +
            "  \"brandNameCH\": \"CH\",\n" +
            "  \"brandNameEN\": \"SuperComfy\",\n" +
            "  \"productNameCH\": \"無\",\n" +
            "  \"productNameEN\": \"Nicolas Cage Body Pillow\",\n" +
            "  \"variationTheme\": \"Color\",\n" +
            "  \"multiTheme\": [],\n" +
            "  \"totalSkus\": 1,\n" +
            "  \"pageSize\": 3,\n" +
            "  \"currentIndex\": 2,\n" +
            "  \"bpStatus\": \"applied\",\n" +
            "\"skus\": [\n" +
            "    { \"actions\": [\"a\",\"b\"], \"sellerSku\": \"K618\", " +
            "\"productId\": {\"name\": \"Product ID\", \"value\": \"500000000\"}, " +
            "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"EAN\"}, " +
            "\"variable\": {\"name\": \"Variable\", \"value\": \"small\"}, " +
            "\"pageIndex\": 1,\"retailPrice\": 120, \"fbaQuantity\": 0, " +
            "\"settlementsPeriodOrder\":55, \"lastSevenDaysOrder\":20, \"editable\": false, " +
            "\"status\":\"銷售中\" }\n" +
            "  ],"+
            "  \"batteryCategory\": \"100000V\",\n" +
            "  \"chargeable\": \"no\",\n" +
            "  \"un38\": \"yes\",\n" +
            "  \"msds\": \"yes\",\n" +
            "  \"droptest\": \"yes\",\n" +
            "  \"allergic\": \"no\",\n" +
            "  \"dangerous\": \"no\",\n" +
            "  \"medical\": \"no\",\n" +
            "  \"battery\": \"no\",\n" +
            "  \"materialSelected\": \"無(None)\",\n" +
            "  \"aircargo\": \"yes\",\n" +
            "  \"seafreight\": \"yes\",\n" +
            "  \"certification\": \"yes\",\n" +
            "  \"formatSelected\": \"Select...\",\n" +
            "  \"packageSelected\": \"Select...\",\n" +
            "  \"wirelessSelected\": \"Select...\",\n" +
            "  \"showOthersInput\": false,\n" +
            "  \"startDate\":\"2020-12-31\",\n" +
            "  \"allFiles\": 0,\n" +
            "  \"batteryQuantity\": \"無電池\",\n" +
            "  \"batteryVoltage\": \"電池電壓\",\n" +
            "  \"batteryCapacitance\": \"電池組電容量\",\n" +
            "  \"batteryNetWeight\": \"電池組淨重\"\n" +
            "}";

    @Test
    public void find(){
        String result= this.dao.findById( id).toJson();
        System.out.println(result);

    }

    @Test
    public void findProduct(){
        String result = this.dao.findByProductName("", pn);
        System.out.println(result);
    }


    @Test
    public void create(){
        Document doc = Document.parse(testBPBedSheet);
        this.dao.createProduct(doc);
        //System.out.println(result);
    }

    @Test
    public void createBpListItem(){
        Document doc = Document.parse(testBPBedSheet);
        this.dao.createProduct(doc);
        //System.out.println(result);
    }





    @Test
    public void insertAll(){
        Document doc = Document.parse(testBPBedSheet);
        this.dao.createProduct(doc);
        //System.out.println(result);
        Document doc1 = Document.parse(testBPPillowCase);
        this.dao.createProduct(doc1);
        //System.out.println(result);
        Document doc2 = Document.parse(testBPBlanket);
        this.dao.createProduct(doc2);
        //System.out.println(result);
    }

    @Test
    public void insertNCPillowCase(){
        Document doc = Document.parse(testBPPillowCase);
        this.dao.createProduct(doc);
        //System.out.println(result);
    }

    @Test
    public void insertNCBlanket(){
        Document doc = Document.parse(testBPBlanket);
        this.dao.createProduct(doc);
        //System.out.println(result);
    }

    @Test
    public void insertBodyPillow(){
        Document doc = Document.parse(testBPBodyPillow);
        this.dao.createProduct(doc);
        //System.out.println(result);
    }


    @Test
    public void testUpdateBedSheet(){
        String bpId = "5ffbc08024a4fa23b0932636";

        this.dao.updateProduct(bpId, testBPBedSheet);
        //System.out.println(result);
    }

    @Test
    public void findProductById(){
        Document result = this.dao.findById("60d76331d33a666096816d6e");
       List<Document> skus = (List<Document>) result.get("skus");
       skus.forEach(y ->{
           System.out.println(y.get("applying"));
          List<String> list = (List<String>) y.get("applying");
          list.add("test");
          y.put("applying",list);

       });
       System.out.println(skus);
        System.out.println(result);
    }

    @Test
    public void findProductByIdAll() {
       // String result = this.dao.findByProductId("", "All");
       // System.out.println(result);
    }

    @Test
    public void findProductByName(){
        String result = this.dao.findByProductName("", "Nicolas Cage BedSheets");
        System.out.println(result);
    }

    @Test
    public void findProductBySupplierId(){
        String result = this.dao.findByKcode("13", 1);
        System.out.println(result);
    }



    @Test
    public void delete(){
        String result =this.dao.deleteProduct("", "5ffd416d2d05435f23c5e9c2");
        System.out.println(result);


    }

    @Test
    public void testFindNextPage() {
        String result = this.dao.findNextpage("", 1);
        System.out.println(result);
    }


}
