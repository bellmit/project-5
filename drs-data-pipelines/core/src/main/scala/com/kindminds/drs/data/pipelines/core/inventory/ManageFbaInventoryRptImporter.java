package com.kindminds.drs.data.pipelines.core.inventory;

public interface ManageFbaInventoryRptImporter {

    String importFbaInventoryFile(byte[] fileData, String fileName);

}
