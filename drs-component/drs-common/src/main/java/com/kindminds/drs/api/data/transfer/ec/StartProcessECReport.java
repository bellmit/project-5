package com.kindminds.drs.api.data.transfer.ec;

import java.time.LocalDate;

public final class StartProcessECReport implements AutoProcessECReport {

    private final Integer marketPlaceId;

    private final LocalDate scheduledDate;

    private final Integer reportType;

    private final Boolean success;


    public Integer getMarketPlaceId() {
        return this.marketPlaceId;
    }


    public LocalDate getScheduledDate() {
        return this.scheduledDate;
    }


    public Integer getReportType() {
        return this.reportType;
    }


    public Boolean getSuccess() {
        return this.success;
    }

    public StartProcessECReport( Integer marketPlaceId, LocalDate scheduledDate, Integer reportType, Boolean success) {
        this.marketPlaceId = marketPlaceId;
        this.scheduledDate = scheduledDate;
        this.reportType = reportType;
        this.success = success;
    }


    public final Integer component1() {
        return this.getMarketPlaceId();
    }


    public final LocalDate component2() {
        return this.getScheduledDate();
    }


    public final Integer component3() {
        return this.getReportType();
    }


    public final Boolean component4() {
        return this.getSuccess();
    }


    public final StartProcessECReport copy( Integer marketPlaceId, LocalDate scheduledDate, Integer reportType, Boolean success) {
        return new StartProcessECReport(marketPlaceId, scheduledDate, reportType, success);
    }

    // $FF: synthetic method
    public static StartProcessECReport copy$default(StartProcessECReport var0, Integer var1, LocalDate var2, Integer var3, Boolean var4, int var5, Object var6) {
        if ((var5 & 1) != 0) {
            var1 = var0.getMarketPlaceId();
        }

        if ((var5 & 2) != 0) {
            var2 = var0.getScheduledDate();
        }

        if ((var5 & 4) != 0) {
            var3 = var0.getReportType();
        }

        if ((var5 & 8) != 0) {
            var4 = var0.getSuccess();
        }

        return var0.copy(var1, var2, var3, var4);
    }


    public String toString() {
        return "StartProcessECReport(marketPlaceId=" + this.getMarketPlaceId() + ", scheduledDate=" + this.getScheduledDate() + ", reportType=" + this.getReportType() + ", success=" + this.getSuccess() + ")";
    }

    public int hashCode() {
        Integer var10000 = this.getMarketPlaceId();
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        LocalDate var10001 = this.getScheduledDate();
        var1 = (var1 + (var10001 != null ? var10001.hashCode() : 0)) * 31;
        Integer var2 = this.getReportType();
        var1 = (var1 + (var2 != null ? var2.hashCode() : 0)) * 31;
        Boolean var3 = this.getSuccess();
        return var1 + (var3 != null ? var3.hashCode() : 0);
    }

    public boolean equals( Object var1) {
        if (this != var1) {
            if (var1 instanceof StartProcessECReport) {
                StartProcessECReport var2 = (StartProcessECReport)var1;
                /*
                if (Intrinsics.areEqual(this.getMarketPlaceId(), var2.getMarketPlaceId()) && Intrinsics.areEqual(this.getScheduledDate(), var2.getScheduledDate()) && Intrinsics.areEqual(this.getReportType(), var2.getReportType()) && Intrinsics.areEqual(this.getSuccess(), var2.getSuccess())) {
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
