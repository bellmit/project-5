package com.kindminds.drs.service.usecase.report;

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonHeadlineSearchAdReportUco;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContextLocal.xml" })
public class TestImportAmazonHeadlineSearchAdReport {

    @Autowired private ImportAmazonHeadlineSearchAdReportUco uco;


    private static final String KEYWORD = "keyword";
    private static final String KEYWORD_VIDEO = "keyword_video";
    private static final String CAMPAIGN = "campaign";
    private static final String CAMPAIGN_VIDEO = "campaign_video";
    private static final String DISPLAY = "display";


    private byte[] getBytes(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return Files.readAllBytes(file.toPath());
    }

//    @Test @Transactional
//    public void testEmptyDeleteCampaignReportUK() {
//        assertEquals(0, uco.deleteReport(4, "2418-10-04", "campaign"));
//    }
//
//    @Test @Transactional
//    public void testEmptyDeleteKeywordReportUS() {
//        assertEquals(0, uco.deleteReport(1, "2518-10-04", "keyword"));
//    }
//
//    @Test @Transactional
//    public void testImportEmptyReportUS() throws IOException {
//        byte[] fileBytes = getBytes("test-report/Empty_Campaign_report_US_2018-10-02.xlsx");
//        assertEquals("num of rows less than 2.", uco.importReport(1, fileBytes));
//    }
//
//    @Test @Transactional
//    public void testWrongColumnReportUS() throws IOException {
//        byte[] fileBytes = getBytes("test-report/wrong_num_columns_report_US_2018-10-04.xlsx");
//        assertEquals("Imported report has the wrong number of columns.",
//                uco.importReport(1, fileBytes));
//    }
//
//    @Test @Transactional
//    public void testDeleteCampaignReportUS() throws IOException {
//        byte[] fileBytes = getBytes("test-report/HSA_Campaign_US_20190316.xlsx");
//        assertEquals("2 inserted.", uco.importReport(1, fileBytes));
//        assertEquals(2, uco.deleteReport(1, "2019-03-16", "campaign"));
//    }
//
//    @Test @Transactional
//    public void testImportHeadlineSearchAdCampaignReportUS() throws IOException {
//        uco.deleteReport(1, "2018-10-04", "campaign");
//        byte[] fileBytes = getBytes("test-report/HSA_Campaign_US_20190316.xlsx");
//        assertEquals("2 inserted.", uco.importReport(1, fileBytes));
//        assertEquals("0 inserted.", uco.importReport(1, fileBytes));
//        assertEquals(2, uco.deleteReport(1, "2019-03-16", "campaign"));
//	}
//
//    @Test @Transactional
//    public void testImportHeadlineSearchAdKeywordReportUS() throws IOException {
//        uco.deleteReport(1, "2018-10-04", "keyword");
//        byte[] fileBytes = getBytes("test-report/HSA_Keyword_US_20190311.xlsx");
//        assertEquals("23 inserted.", uco.importReport(1, fileBytes));
//        assertEquals("0 inserted.", uco.importReport(1, fileBytes));
//        assertEquals(23, uco.deleteReport(1, "2019-03-11", "keyword"));
//        assertEquals(0, uco.deleteReport(1, "2019-03-11", "keyword"));
//    }

    @Test
    public void testImportHSACampaignReportUS() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Campaign_US_20210218.xlsx");
        System.out.println(uco.importReport(1, fileBytes, CAMPAIGN));
    }

    @Test
    public void testImportHSACampaignReportCA() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Campaign_CA_20210218.xlsx");
        System.out.println(uco.importReport(5, fileBytes, CAMPAIGN));
    }

    @Test
    public void testImportHSACampaignReportUK() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Campaign_UK_20210218.xlsx");
        System.out.println(uco.importReport(4, fileBytes, CAMPAIGN));
    }

    @Test
    public void testImportHSACampaignReportDE() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Campaign_DE_20210218.xlsx");
        System.out.println(uco.importReport(6, fileBytes, CAMPAIGN));
    }

    @Test
    public void testImportHSACampaignReportFR() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Campaign_FR_20210218.xlsx");
        System.out.println(uco.importReport(7, fileBytes, CAMPAIGN));
    }

    @Test
    public void testImportHSAVideoCampaignReportUS() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Video_Campaign_US_20210218.xlsx");
        System.out.println(uco.importReport(1, fileBytes, CAMPAIGN_VIDEO));
    }

    @Test
    public void testImportHSAVideoCampaignReportUK() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Video_Campaign_UK_20210218.xlsx");
        System.out.println(uco.importReport(4, fileBytes, CAMPAIGN_VIDEO));
    }

    @Test //@Transactional
    public void testImportHSAVideoCampaignReportDE() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Video_Campaign_DE_20210218.xlsx");
        System.out.println(uco.importReport(6, fileBytes, CAMPAIGN_VIDEO));
    }


    //DISPLAY
    @Test //@Transactional
    public void testImportDisplayCampaignReportUS() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Display_US_20210218.xlsx");
        System.out.println(uco.importReport(1, fileBytes, DISPLAY));
    }

    @Test
    public void testImportDisplayCampaignReportCA() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Display_CA_20210218.xlsx");
        System.out.println(uco.importReport(5, fileBytes, DISPLAY));
    }

    @Test
    public void testImportDisplayCampaignReportUK() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Display_UK_20210218.xlsx");
        System.out.println(uco.importReport(4, fileBytes, DISPLAY));
    }

    @Test
    public void testImportDisplayCampaignReportDE() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Display_DE_20210218.xlsx");
        System.out.println(uco.importReport(6, fileBytes, DISPLAY));
    }

    @Test
    public void testImportDisplayCampaignReportFR() throws IOException {
        byte[] fileBytes = getBytes("test-report/HSA_Display_FR_20210218.xlsx");
        System.out.println(uco.importReport(7, fileBytes, DISPLAY));
    }



}
