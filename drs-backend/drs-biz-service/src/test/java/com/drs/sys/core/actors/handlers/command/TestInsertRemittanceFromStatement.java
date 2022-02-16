package com.drs.sys.core.actors.handlers.command;

import com.kindminds.drs.api.data.access.usecase.accounting.MaintainRemittanceDao;
import com.kindminds.drs.api.usecase.MaintainRemittanceUco;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestInsertRemittanceFromStatement {

    @Autowired private MaintainRemittanceDao dao;
    @Autowired private MaintainRemittanceUco uco;


    private byte[] getBytes(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return Files.readAllBytes(file.toPath());
    }

    @Test
    public void testRemittanceImportCSV() throws IOException {
        byte[] fileBytes = getBytes("2018-11-21-remittance-data.csv");
        //System.out.//println("Remittance Import: " + uco.importRemittanceData(fileBytes));
    }

    @Test
    public void testInsertRemittanceFromStatement() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-20");
//        assertEquals((Integer) 13, dao.deleteByDate(date));
        Integer insertCount = dao.autoInsertFromStatement(date);
        //System.out.//println("Insert Count: " + insertCount);
//        assertEquals((Integer) 13, insertCount);
    }

}
