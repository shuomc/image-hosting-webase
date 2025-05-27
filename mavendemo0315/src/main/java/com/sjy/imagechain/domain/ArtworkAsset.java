package com.sjy.imagechain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */
@Data
@TableName("artwork_asset")
public class ArtworkAsset {

    // asset_id 是 VARCHAR/UUID 类型，使用 IdType.ASSIGN_ID
    @TableId(value = "asset_id", type = IdType.ASSIGN_ID)
    private String assetId; // Traditional application internal asset ID (Primary Key)

    @TableField("asset_cid")
    private String assetCid; // !!! 文件内容的哈希值 (CID) !!!
    // Used as immutable content identifier and MinIO storage key

    @TableField("token_id")
    private String tokenId; // !!! 新增字段: 链上 Token ID !!!
    // The unique ID assigned by the blockchain smart contract (e.g., NFT ID)
    // NULL initially, populated after successful minting

    @TableField("creator_user_id")
    private String creatorUserId; // 传统应用用户ID (Foreign Key to app_user) - 记录上传者
    // Note: In Mybatis-Plus, you usually store FK value directly in the entity.
    // Relationship (like @ManyToOne in JPA) is handled in mapper/service logic.

    @TableField("file_name")
    private String fileName; // Original file name

    @TableField("content_type")
    private String contentType; // MIME type

    private Long size; // File size in bytes (字段名和列名一致，省略 @TableField)

    private Integer width; // Image width (字段名和列名一致，省略 @TableField)

    private Integer height; // Image height (字段名和列名一致，省略 @TableField)

    // description 字段名和列名一致，省略 @TableField，使用 columnDefinition 可以在建表时指定类型，但在实体类中不需要
    private String description; // Asset description

    @TableField("metadata_uri")
    private String metadataUri; // !!! 新增字段: URI pointing to off-chain metadata JSON file !!!

    @TableField("minio_storage_key")
    private String minioStorageKey; // !!! 修改字段: Actual MinIO storage Key !!!
    // Should ideally be the same as assetCid

    @TableField("is_public")
    private Boolean isPublic; // Application-level visibility

    // is_delete 继承自 BaseEntity，不再此处定义

    // upload_time 在 BaseEntity 中通常对应 create_time，不再此处定义

    @TableField("mint_time")
    private LocalDateTime mintTime; // !!! 新增字段: Time asset was successfully minted on chain !!!

    // update_time 继承自 BaseEntity，不再此处定义

    // 构造函数
    public ArtworkAsset() {
    }

    // 包含所有字段的构造函数 (不包含继承自 BaseEntity 的字段)
    public ArtworkAsset(String assetId, String assetCid, String tokenId, String creatorUserId, String fileName, String contentType, Long size, Integer width, Integer height, String description, String metadataUri, String minioStorageKey, Boolean isPublic, LocalDateTime mintTime) {
        this.assetId = assetId;
        this.assetCid = assetCid;
        this.tokenId = tokenId;
        this.creatorUserId = creatorUserId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.width = width;
        this.height = height;
        this.description = description;
        this.metadataUri = metadataUri;
        this.minioStorageKey = minioStorageKey;
        this.isPublic = isPublic;
        this.mintTime = mintTime;
        // 注意：BaseEntity 的字段需要在服务层或 BaseEntity 的构造函数中处理
    }

    // Getter and Setter methods

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetCid() {
        return assetCid;
    }

    public void setAssetCid(String assetCid) {
        this.assetCid = assetCid;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetadataUri() {
        return metadataUri;
    }

    public void setMetadataUri(String metadataUri) {
        this.metadataUri = metadataUri;
    }

    public String getMinioStorageKey() {
        return minioStorageKey;
    }

    public void setMinioStorageKey(String minioStorageKey) {
        this.minioStorageKey = minioStorageKey;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public LocalDateTime getMintTime() {
        return mintTime;
    }

    public void setMintTime(LocalDateTime mintTime) {
        this.mintTime = mintTime;
    }

    // isDeleted, createTime, updateTime are inherited from BaseEntity and have their getters/setters there.

    @Override
    public String toString() {
        return "ArtworkAsset{" +
                "assetId='" + assetId + '\'' +
                ", assetCid='" + assetCid + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", creatorUserId='" + creatorUserId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + size +
                ", width=" + width +
                ", height=" + height +
                ", description='" + description + '\'' +
                ", metadataUri='" + metadataUri + '\'' +
                ", minioStorageKey='" + minioStorageKey + '\'' +
                ", isPublic=" + isPublic +
                ", mintTime=" + mintTime +
                '}';
    }
}
