package com.kindminds.drs.api.data.transfer.ec;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ProcessResult {

    private final String id;

    private final LocalDateTime createTime;
    private final int marketPlaceId;

    private final LocalDate scheduledDate;
    private final int reportType;
    private final boolean started;
    private final boolean downloaded;
    private final boolean correctFile;
    private final boolean saveToHdfs;
    private final boolean importToDRSDB;
    private final boolean importToHbase;


    public final String getId() {
        return this.id;
    }


    public final LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public final int getMarketPlaceId() {
        return this.marketPlaceId;
    }


    public final LocalDate getScheduledDate() {
        return this.scheduledDate;
    }

    public final int getReportType() {
        return this.reportType;
    }

    public final boolean getStarted() {
        return this.started;
    }

    public final boolean getDownloaded() {
        return this.downloaded;
    }

    public final boolean getCorrectFile() {
        return this.correctFile;
    }

    public final boolean getSaveToHdfs() {
        return this.saveToHdfs;
    }

    public final boolean getImportToDRSDB() {
        return this.importToDRSDB;
    }

    public final boolean getImportToHbase() {
        return this.importToHbase;
    }

    public ProcessResult( String id, LocalDateTime createTime, int marketPlaceId, LocalDate scheduledDate, int reportType, boolean started, boolean downloaded, boolean correctFile, boolean saveToHdfs, boolean importToDRSDB, boolean importToHbase) {
       
        
        this.id = id;
        this.createTime = createTime;
        this.marketPlaceId = marketPlaceId;
        this.scheduledDate = scheduledDate;
        this.reportType = reportType;
        this.started = started;
        this.downloaded = downloaded;
        this.correctFile = correctFile;
        this.saveToHdfs = saveToHdfs;
        this.importToDRSDB = importToDRSDB;
        this.importToHbase = importToHbase;
    }


    public final String component1() {
        return this.id;
    }


    public final LocalDateTime component2() {
        return this.createTime;
    }

    public final int component3() {
        return this.marketPlaceId;
    }


    public final LocalDate component4() {
        return this.scheduledDate;
    }

    public final int component5() {
        return this.reportType;
    }

    public final boolean component6() {
        return this.started;
    }

    public final boolean component7() {
        return this.downloaded;
    }

    public final boolean component8() {
        return this.correctFile;
    }

    public final boolean component9() {
        return this.saveToHdfs;
    }

    public final boolean component10() {
        return this.importToDRSDB;
    }

    public final boolean component11() {
        return this.importToHbase;
    }


    public final ProcessResult copy( String id, LocalDateTime createTime, int marketPlaceId, LocalDate scheduledDate, int reportType, boolean started, boolean downloaded, boolean correctFile, boolean saveToHdfs, boolean importToDRSDB, boolean importToHbase) {

        return new ProcessResult(id, createTime, marketPlaceId, scheduledDate, reportType, started, downloaded, correctFile, saveToHdfs, importToDRSDB, importToHbase);
    }

    // $FF: synthetic method
    public static ProcessResult copy$default(ProcessResult var0, String var1, LocalDateTime var2, int var3, LocalDate var4, int var5, boolean var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11, int var12, Object var13) {
        if ((var12 & 1) != 0) {
            var1 = var0.id;
        }

        if ((var12 & 2) != 0) {
            var2 = var0.createTime;
        }

        if ((var12 & 4) != 0) {
            var3 = var0.marketPlaceId;
        }

        if ((var12 & 8) != 0) {
            var4 = var0.scheduledDate;
        }

        if ((var12 & 16) != 0) {
            var5 = var0.reportType;
        }

        if ((var12 & 32) != 0) {
            var6 = var0.started;
        }

        if ((var12 & 64) != 0) {
            var7 = var0.downloaded;
        }

        if ((var12 & 128) != 0) {
            var8 = var0.correctFile;
        }

        if ((var12 & 256) != 0) {
            var9 = var0.saveToHdfs;
        }

        if ((var12 & 512) != 0) {
            var10 = var0.importToDRSDB;
        }

        if ((var12 & 1024) != 0) {
            var11 = var0.importToHbase;
        }

        return var0.copy(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
    }


    public String toString() {
        return "ProcessResult(id=" + this.id + ", createTime=" + this.createTime + ", marketPlaceId=" + this.marketPlaceId + ", scheduledDate=" + this.scheduledDate + ", reportType=" + this.reportType + ", started=" + this.started + ", downloaded=" + this.downloaded + ", correctFile=" + this.correctFile + ", saveToHdfs=" + this.saveToHdfs + ", importToDRSDB=" + this.importToDRSDB + ", importToHbase=" + this.importToHbase + ")";
    }



    public boolean equals( Object var1) {
        if (this != var1) {
            if (var1 instanceof ProcessResult) {
                ProcessResult var2 = (ProcessResult)var1;
               /*
                if (Intrinsics.areEqual(this.id, var2.id) && Intrinsics.areEqual(this.createTime, var2.createTime) && this.marketPlaceId == var2.marketPlaceId && Intrinsics.areEqual(this.scheduledDate, var2.scheduledDate) && this.reportType == var2.reportType && this.started == var2.started && this.downloaded == var2.downloaded && this.correctFile == var2.correctFile && this.saveToHdfs == var2.saveToHdfs && this.importToDRSDB == var2.importToDRSDB && this.importToHbase == var2.importToHbase) {
                    return true;
                }

                */
            }

            return false;
        } else {
            return true;
        }
    }
}
