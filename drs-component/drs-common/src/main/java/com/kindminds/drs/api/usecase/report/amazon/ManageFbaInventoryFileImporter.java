package com.kindminds.drs.api.usecase.report.amazon;

public interface ManageFbaInventoryFileImporter {

    String importFbaInventoryFile (byte[] fileData, String fileName);

}
