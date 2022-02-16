package com.kindminds.drs.api.data.transfer.fba;

import java.time.OffsetDateTime;


public final class FbaReturnToSupplierItem {
   
    private final OffsetDateTime drsProcessDate;
   
    private final String drsSku;
   
    private final String sourceMarketplace;
   
    private final Integer quantity;
   
    private final String ivsName;
   
    private final String unsName;
   
    private final String sellbackType;

   
    public final OffsetDateTime getDrsProcessDate() {
        return this.drsProcessDate;
    }

   
    public final String getDrsSku() {
        return this.drsSku;
    }

   
    public final String getSourceMarketplace() {
        return this.sourceMarketplace;
    }

   
    public final Integer getQuantity() {
        return this.quantity;
    }

   
    public final String getIvsName() {
        return this.ivsName;
    }

   
    public final String getUnsName() {
        return this.unsName;
    }

   
    public final String getSellbackType() {
        return this.sellbackType;
    }

    public FbaReturnToSupplierItem( OffsetDateTime drsProcessDate,
                                    String drsSku, String sourceMarketplace, Integer quantity, String ivsName, String unsName, String sellbackType) {
        this.drsProcessDate = drsProcessDate;
        this.drsSku = drsSku;
        this.sourceMarketplace = sourceMarketplace;
        this.quantity = quantity;
        this.ivsName = ivsName;
        this.unsName = unsName;
        this.sellbackType = sellbackType;
    }

   
    public final OffsetDateTime component1() {
        return this.drsProcessDate;
    }

   
    public final String component2() {
        return this.drsSku;
    }

   
    public final String component3() {
        return this.sourceMarketplace;
    }

   
    public final Integer component4() {
        return this.quantity;
    }

   
    public final String component5() {
        return this.ivsName;
    }

   
    public final String component6() {
        return this.unsName;
    }

   
    public final String component7() {
        return this.sellbackType;
    }

    
    public final FbaReturnToSupplierItem copy( OffsetDateTime drsProcessDate, String drsSku, String sourceMarketplace, Integer quantity, String ivsName, String unsName, String sellbackType) {
        return new FbaReturnToSupplierItem(drsProcessDate, drsSku, sourceMarketplace, quantity, ivsName, unsName, sellbackType);
    }

    // $FF: synthetic method
    public static FbaReturnToSupplierItem copy$default(FbaReturnToSupplierItem var0, OffsetDateTime var1, String var2, String var3, Integer var4, String var5, String var6, String var7, int var8, Object var9) {
        if ((var8 & 1) != 0) {
            var1 = var0.drsProcessDate;
        }

        if ((var8 & 2) != 0) {
            var2 = var0.drsSku;
        }

        if ((var8 & 4) != 0) {
            var3 = var0.sourceMarketplace;
        }

        if ((var8 & 8) != 0) {
            var4 = var0.quantity;
        }

        if ((var8 & 16) != 0) {
            var5 = var0.ivsName;
        }

        if ((var8 & 32) != 0) {
            var6 = var0.unsName;
        }

        if ((var8 & 64) != 0) {
            var7 = var0.sellbackType;
        }

        return var0.copy(var1, var2, var3, var4, var5, var6, var7);
    }

    
    public String toString() {
        return "FbaReturnToSupplierItem(drsProcessDate=" + this.drsProcessDate + ", drsSku=" + this.drsSku + ", sourceMarketplace=" + this.sourceMarketplace + ", quantity=" + this.quantity + ", ivsName=" + this.ivsName + ", unsName=" + this.unsName + ", sellbackType=" + this.sellbackType + ")";
    }

    public int hashCode() {
        OffsetDateTime var10000 = this.drsProcessDate;
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        String var10001 = this.drsSku;
        var1 = (var1 + (var10001 != null ? var10001.hashCode() : 0)) * 31;
        var10001 = this.sourceMarketplace;
        var1 = (var1 + (var10001 != null ? var10001.hashCode() : 0)) * 31;
        Integer var2 = this.quantity;
        var1 = (var1 + (var2 != null ? var2.hashCode() : 0)) * 31;
        var10001 = this.ivsName;
        var1 = (var1 + (var10001 != null ? var10001.hashCode() : 0)) * 31;
        var10001 = this.unsName;
        var1 = (var1 + (var10001 != null ? var10001.hashCode() : 0)) * 31;
        var10001 = this.sellbackType;
        return var1 + (var10001 != null ? var10001.hashCode() : 0);
    }

    public boolean equals( Object var1) {
        if (this != var1) {
            if (var1 instanceof FbaReturnToSupplierItem) {
                FbaReturnToSupplierItem var2 = (FbaReturnToSupplierItem)var1;
               /*
                if (Intrinsics.areEqual(this.drsProcessDate, var2.drsProcessDate) && Intrinsics.areEqual(this.drsSku, var2.drsSku) && Intrinsics.areEqual(this.sourceMarketplace, var2.sourceMarketplace) && Intrinsics.areEqual(this.quantity, var2.quantity) && Intrinsics.areEqual(this.ivsName, var2.ivsName) && Intrinsics.areEqual(this.unsName, var2.unsName) && Intrinsics.areEqual(this.sellbackType, var2.sellbackType)) {
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