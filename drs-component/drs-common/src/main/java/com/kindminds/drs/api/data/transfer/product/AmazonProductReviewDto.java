package com.kindminds.drs.api.data.transfer.product;



import java.math.BigDecimal;

public final class AmazonProductReviewDto {
    
    private final String dateCreated;
    
    private final BigDecimal starRating;
    
    private final String title;
    
    private final String sku;

    
    public final String getDateCreated() {
        return this.dateCreated;
    }

    
    public final BigDecimal getStarRating() {
        return this.starRating;
    }

    
    public final String getTitle() {
        return this.title;
    }

    
    public final String getSku() {
        return this.sku;
    }

    public AmazonProductReviewDto( String dateCreated,  BigDecimal starRating,  String title,  String sku) {

        this.dateCreated = dateCreated;
        this.starRating = starRating;
        this.title = title;
        this.sku = sku;
    }

    
    public final String component1() {
        return this.dateCreated;
    }

    
    public final BigDecimal component2() {
        return this.starRating;
    }

    
    public final String component3() {
        return this.title;
    }

    
    public final String component4() {
        return this.sku;
    }

    
    public final AmazonProductReviewDto copy( String dateCreated,  BigDecimal starRating,  String title,  String sku) {

        return new AmazonProductReviewDto(dateCreated, starRating, title, sku);
    }

    // $FF: synthetic method
    public static AmazonProductReviewDto copy$default(AmazonProductReviewDto var0, String var1, BigDecimal var2, String var3, String var4, int var5, Object var6) {
        if ((var5 & 1) != 0) {
            var1 = var0.dateCreated;
        }

        if ((var5 & 2) != 0) {
            var2 = var0.starRating;
        }

        if ((var5 & 4) != 0) {
            var3 = var0.title;
        }

        if ((var5 & 8) != 0) {
            var4 = var0.sku;
        }

        return var0.copy(var1, var2, var3, var4);
    }

    
    public String toString() {
        return "AmazonProductReviewDto(dateCreated=" + this.dateCreated + ", starRating=" + this.starRating + ", title=" + this.title + ", sku=" + this.sku + ")";
    }

    public int hashCode() {
        String var10000 = this.dateCreated;
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        BigDecimal var10001 = this.starRating;
        var1 = (var1 + (var10001 != null ? var10001.hashCode() : 0)) * 31;
        String var2 = this.title;
        var1 = (var1 + (var2 != null ? var2.hashCode() : 0)) * 31;
        var2 = this.sku;
        return var1 + (var2 != null ? var2.hashCode() : 0);
    }

    public boolean equals( Object var1) {
        if (this != var1) {
            if (var1 instanceof AmazonProductReviewDto) {
                AmazonProductReviewDto var2 = (AmazonProductReviewDto)var1;
                /*
                if (Intrinsics.areEqual(this.dateCreated, var2.dateCreated) && Intrinsics.areEqual(this.starRating, var2.starRating) && Intrinsics.areEqual(this.title, var2.title) && Intrinsics.areEqual(this.sku, var2.sku)) {
                    return true;
                }*/
            }

            return false;
        } else {
            return true;
        }
    }
}
