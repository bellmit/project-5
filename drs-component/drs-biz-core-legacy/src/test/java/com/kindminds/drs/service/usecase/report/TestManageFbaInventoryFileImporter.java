package com.kindminds.drs.service.usecase.report;

import com.kindminds.drs.api.usecase.report.amazon.ManageFbaInventoryFileImporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestManageFbaInventoryFileImporter {

    @Autowired private ManageFbaInventoryFileImporter uco;

    private byte[] getBytes(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return Files.readAllBytes(file.toPath());
    }

    @Test
    public void testImportManageFbaInventoryFileUS() throws IOException {
        byte[] fileBytes = getBytes("test-report/ManageFBAInventory_US_20200113.txt");

        System.out.println(uco.importFbaInventoryFile(fileBytes,
                "ManageFBAInventory_US_20200113.txt"));

	}

    @Test
    public void testImportManageFbaInventoryFileCA() throws IOException {
        byte[] fileBytes = getBytes("test-report/ManageFBAInventory_CA_20200113.txt");

        System.out.println(uco.importFbaInventoryFile(fileBytes,
                "ManageFBAInventory_CA_20200113.txt"));

    }


}
