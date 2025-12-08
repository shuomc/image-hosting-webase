package moe.imtop1.imagehosting.images.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import moe.imtop1.imagehosting.framework.base.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author anoixa
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("image_data")
public class ImageData extends BaseEntity {
    // 基础信息
    @TableId(type = IdType.ASSIGN_ID)
    private String imageId; // 图片唯一ID (主键)
    private String userId; // 上传者用户ID
    private String nftId; // nftID
    private String fileName; // 原始文件名
    private Long size; // 文件大小 (int8 -> Long)
    private String contentType; // MIME类型

    @TableField("file_hash")
    private String fileHash; // 文件SHA-256哈希值

    // 存储路径信息 (已更新以匹配数据库 DDL)
    @TableField("origin_minio_key")
    private String originMinioKey; // 原图/高清图的 MinIO Key (原 minioKey 已更名)
    private String originMinioUrl; // 高清原图的MinIO内部路径

    @TableField("watermark_minio_key")
    private String watermarkMinioKey; // 水印图/预览图的 MinIO Key (新增)
    private String watermarkMinioUrl; // 带水印预览图的公共访问URL

    @TableField("thumbnail_minio_key")
    private String thumbnailMinioKey; // 缩略图的 MinIO Key (新增)
    private String thumbnailMinioUrl; // 缩略图的公共访问URL (新增)


    // 基本信息
    private String description; // 图片描述
    private Boolean isPublic; // 是否公开展示
    private Integer auditStatus; // 审核状态: 0-待审, 1-通过, 2-拒绝
    private String auditMsg; // 审核备注/拒绝原因

    // EXIF 元数据 - 摄影参数
    private String cameraMake; // 相机厂商
    private String cameraModel; // 相机型号
    private String lensModel; // 镜头型号
    private String focalLength; // 焦距
    private String aperture; // 光圈
    private int width;
    private int height;

    // 使用 @TableField 确保 MyBatis-Plus 映射正确
    @TableField("shutter_speed")
    private String shutterSpeed; // 快门

    private Integer iso; // ISO 感光度
    private LocalDateTime shootTime; // 实际拍摄时间

    // 地理位置
    private String locationName; // 地点名称
    private BigDecimal latitude; // 纬度
    private BigDecimal longitude; // 经度

    // 统计与分类
    private Long viewCount; // 浏览量
    private Long downloadCount; // 下载量
    private Long likeCount; // 点赞/收藏数
    private String category; // 分类

    private String dominantColor; // 主色调
}