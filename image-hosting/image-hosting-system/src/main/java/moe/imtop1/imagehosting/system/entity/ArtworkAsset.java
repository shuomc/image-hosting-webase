package moe.imtop1.imagehosting.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import moe.imtop1.imagehosting.framework.base.BaseEntity;

import java.time.LocalDateTime;

@Data
@TableName("artwork_asset")
public class ArtworkAsset extends BaseEntity {
    @TableId(value = "asset_id", type = IdType.ASSIGN_ID)
    private String assetId;

    @TableField("asset_cid")
    private String assetCid;

    @TableField("token_id")
    private String tokenId;

    @TableField("creator_user_id")
    private String creatorUserId;

    @TableField("file_name")
    private String fileName;

    @TableField("content_type")
    private String contentType;

    private Long size;

    private Integer width;

    private Integer height;

    private String description;

    @TableField("metadata_uri")
    private String metadataUri;

    @TableField("minio_storage_key")
    private String minioStorageKey;

    @TableField("is_public")
    private Boolean isPublic;

    @TableField("mint_time")
    private LocalDateTime mintTime;
} 