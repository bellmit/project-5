package com.kindminds.drs.persist.v1.model.mapping.product;






import com.kindminds.drs.api.v1.model.product.ConfirmMarketingMaterialComment;

import java.io.Serializable;


public class ConfirmMarketingMaterialCommentImpl implements ConfirmMarketingMaterialComment, Serializable {

    //@Id
    ////@Column(name="id")
    private Integer id;
    //@Column(name="marketing_material_marketside_id")
    private Integer materialMarketsideId;
    //@Column(name="marketplace_id")
    private Integer marketplaceId;
    //@Column(name="product_base_code")
    private String productBaseCode;
    //@Column(name="line_seq")
    private Integer lineSeq;
    //@Column(name="creator_id")
    private String creatorId;
    //@Column(name="creator_name")
    private String creatorName;
    //@Column(name="date_created")
    private String dateCreated;
    //@Column(name="contents")
    private String contents;

    public ConfirmMarketingMaterialCommentImpl(Integer id, Integer materialMarketsideId, Integer marketplaceId, String productBaseCode, Integer lineSeq, String creatorId, String creatorName, String dateCreated, String contents) {
        this.id = id;
        this.materialMarketsideId = materialMarketsideId;
        this.marketplaceId = marketplaceId;
        this.productBaseCode = productBaseCode;
        this.lineSeq = lineSeq;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.dateCreated = dateCreated;
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "ConfirmMarketingMaterialCommentImpl{" +
                "id=" + id +
                ", materialMarketsideId=" + materialMarketsideId +
                ", marketplaceId=" + marketplaceId +
                ", productBaseCode='" + productBaseCode + '\'' +
                ", lineSeq=" + lineSeq +
                ", creatorId='" + creatorId + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", dateCreate='" + dateCreated + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }

    @Override
    public Integer getMaterialMarketsideId() {
        return this.materialMarketsideId;
    }

    @Override
    public Integer getMarketplaceId() {
        return this.marketplaceId;
    }

    @Override
    public String getProductBaseCode() {
        return this.productBaseCode;
    }

    @Override
    public Integer getLineSeq() {
        return this.lineSeq;
    }

    @Override
    public String getCreatorId() {
        return this.creatorId;
    }

    @Override
    public String getCreatorName() {
        return this.creatorName;
    }

    @Override
    public String getDateCreated() {
        return this.dateCreated;
    }

    @Override
    public String getContents() {
        return this.contents;
    }
}
