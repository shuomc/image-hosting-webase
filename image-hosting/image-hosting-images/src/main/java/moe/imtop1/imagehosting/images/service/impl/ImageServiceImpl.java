package moe.imtop1.imagehosting.images.service.impl;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.images.domain.dto.StatsDTO;
import lombok.extern.slf4j.Slf4j;
import moe.imtop1.imagehosting.common.enums.ResultCodeEnum;
import moe.imtop1.imagehosting.common.utils.FileUtil;
import moe.imtop1.imagehosting.common.utils.StringUtil;
import moe.imtop1.imagehosting.framework.exception.ServiceException;
import moe.imtop1.imagehosting.framework.utils.RedisCache;
import moe.imtop1.imagehosting.images.domain.ImageData;
import moe.imtop1.imagehosting.images.domain.dto.BatchUploadResult;
import moe.imtop1.imagehosting.images.domain.dto.ImageStreamData;
import moe.imtop1.imagehosting.images.domain.vo.ImagePresignedUrlData;
import moe.imtop1.imagehosting.images.domain.vo.ImageUrlData;
import moe.imtop1.imagehosting.images.mapper.ImageDataMapper;
//import moe.imtop1.imagehosting.images.mapper.ImageMapper;
import moe.imtop1.imagehosting.images.mapper.ImageMapper;
import moe.imtop1.imagehosting.images.service.ImageService;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static moe.imtop1.imagehosting.common.enums.ResultCodeEnum.DATABASE_ERROR;
import static moe.imtop1.imagehosting.common.enums.ResultCodeEnum.MISSING_REQUIRED_PARAMETER;


/**
 * @author shuomc
 * @Date 2025/4/15
 * @Description 图片服务实现类
 */
@Service // 标记为 Spring Service 组件
@Slf4j // Lombok: 自动生成一个名为 log 的 SLF4J Logger
@RequiredArgsConstructor // Lombok: 为所有 final 字段生成构造函数，用于依赖注入
public class ImageServiceImpl extends ServiceImpl<ImageMapper, ImageData> implements ImageService { // 继承 Mybatis Plus ServiceImpl

    // --- 依赖注入 ---
    // @RequiredArgsConstructor 会自动处理 final 字段的注入
    // private final ImageMapper imageMapper; // 如果 ImageMapper 是 ServiceImpl 的第一个泛型参数，则不需要此行
    private final ImageDataMapper imageDataMapper; // 直接注入 ImageData 的 Mapper

    private final MinioClient minioClient; // Minio 客户端

    // 缩略图配置
    private static final int THUMBNAIL_MAX_SIZE = 800;
    private static final float THUMBNAIL_QUALITY = 0.8f;
    private static final String THUMBNAIL_OUTPUT_FORMAT = "jpg";  // 缩略图格式强制jpg

    // 水印图配置
    private static final int WATERMARK_MAX_SIZE = 2400;
    private static final float WATERMARK_QUALITY = 0.9f;
    private static final String WATERMARK_OUTPUT_FORMAT = "jpg";  // 缩略图格式强制jpg

    private final RedisCache redisCache; // 注入 RedisCache
    private final RestTemplate restTemplate; // 注入 RestTemplate

    @Value("${blockchain.api-url}")
    private String blockchainApiUrl;

    private static final Integer CACHE_EXPIRATION_SECONDS = 600; // 10 分钟

    // 从 application.properties 或 application.yml 读取配置值
    @Value("${minio.originBucket}")
    private String originBucket;    // 原图

    @Value("${minio.thumbnailBucket}")
    private String thumbnailBucket;   // 缩略图

    @Value("${minio.watermarkBucket}")
    private String watermarkBucket;   // 水印图

    @Value("${minio.external-url}")
    private String minioExternalUrl;


    // 可能需要的其他 Mapper 或 Service
    // private final StrategiesMapper strategiesMapper;
    // private final GlobalSettingsMapper globalSettingsMapper;

    /**
     * 上传图片，包括原图和水印图的生成，并填充所有元数据字段。
     *
     * @param file 上传的文件
     * @param imageDataDTO 包含用户和配置信息的DTO
     * @return 完整的 ImageData 实体
     * @throws Exception 存储或处理失败
     */
    @Override
    public ImageData uploadImage(MultipartFile file, ImageData imageDataDTO) throws IOException {
        // 1. 数据准备
        ImageData imageData = new ImageData();
        byte[] fileBytes = file.getBytes();

        // 计算哈希值和校验
        String fileHash;
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(fileBytes);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            fileHash = hexString.toString();
            imageData.setFileHash(fileHash); // 设置哈希值到实体

        } catch (java.security.NoSuchAlgorithmException e) {
            log.error("SHA-256 算法不可用", e);
            throw new ServiceException(ResultCodeEnum.IMAGE_SHA256_ERROR);
        }
        log.info("成功获取到SHA-256哈希值{}", fileHash);

        long count = this.count(new LambdaQueryWrapper<ImageData>()
                .eq(ImageData::getFileHash, fileHash));

        if (count > 0) {
            log.warn("拦截重复上传，文件Hash已存在: {}", fileHash);
            // 直接抛出异常，终止后续流程
            throw new ServiceException(ResultCodeEnum.FILE_ALREADY_EXISTS);
        }


        // 安全提取扩展名
        String fileExtension = FileUtil.getExtensionWithDotFromFilename(file.getOriginalFilename());

        // 2. 生成唯一的图片 ID
        imageData.setImageId(UUID.randomUUID().toString());

        // 3. 设置核心元数据
        imageData.setUserId(imageDataDTO.getUserId());
        imageData.setFileName(file.getOriginalFilename()); // 原始文件名仍需存储在数据库
        imageData.setSize(file.getSize());
        imageData.setFileHash(fileHash);
        imageData.setContentType(file.getContentType());
        imageData.setIsPublic(imageDataDTO.getIsPublic() != null ? imageDataDTO.getIsPublic() : false);
        imageData.setCategory(imageDataDTO.getCategory() != null ? imageDataDTO.getCategory() : "Uncategorized");
        imageData.setDescription(imageDataDTO.getDescription());
        imageData.setAuditStatus(0);

        // 4. 生成 MinIO Keys 和 URLs (Key 格式: {userId}/{imageId}.{ext})
        String userIdForPath = (imageDataDTO.getUserId() != null && !imageDataDTO.getUserId().isEmpty()) ? imageDataDTO.getUserId() : "public";

        // 原始图 Key (ORIGIN_KEY)
        String originObjectKey = userIdForPath + "/" + imageData.getImageId() + fileExtension;

        imageData.setOriginMinioKey(originObjectKey);
        imageData.setOriginMinioUrl("/" + originBucket + "/" + originObjectKey); // 私有桶 URL

        // 5. 上传原图到 MinIO 私有桶
        try (InputStream originalStream = new ByteArrayInputStream(fileBytes)) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(originBucket)
                    .object(originObjectKey)
                    .stream(originalStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            log.info("成功上传原图到 MinIO，Key: {}", originObjectKey);

        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("MinIO 操作失败: {}", e.getMessage(), e);
            throw new ServiceException(ResultCodeEnum.IMAGE_STORAGE_ERROR);
        }

        // 6. 处理并上传缩略图到 MinIO 公有缩略图桶
        createAndUploadThumbnail(fileBytes, imageData, userIdForPath, fileExtension);

        // 7. 提取并设置所有其他元数据（EXIF、地理、色彩等）
        // 使用新的流进行元数据分析
        try (InputStream metadataStream = new ByteArrayInputStream(fileBytes)) {
            extractAndSetMetadata(metadataStream, imageData);
        }

        // 8. 将完整的元数据插入数据库
        boolean saved = this.save(imageData);
        if (!saved) {
            log.error("无法将图片元数据存储到数据库，imageId: {}", imageData.getImageId());
            throw new ServiceException(ResultCodeEnum.IMAGE_STORAGE_ERROR);
        }
        log.info("成功存储图片元数据，imageId: {}", imageData.getImageId());

        return imageData;
    }

    @Override
    public BatchUploadResult batchUploadImages(MultipartFile[] files, ImageData imageData) {
        List<ImageData> successfulUploads = new ArrayList<>();
        List<String> failedFiles = new ArrayList<>();
        // 如果使用详细失败信息
        // List<Map<String, String>> failedFilesDetails = new ArrayList<>();

        // --- 获取当前登录用户的 ID 并设置到实体中 ---
        String currentUserId;
        try {
            // 使用 Sa-Token 获取当前登录用户的 ID
            // 假设你在登录成功时将用户 ID 作为 loginId 存入了 Sa-Token
            currentUserId = (String) StpUtil.getLoginIdAsString(); // 获取当前登录用户的 ID (字符串类型)

            imageData.setUserId(currentUserId);
        } catch (Exception e) {
            // 如果用户未登录或获取 ID 失败，根据你的业务需求处理
            // 如果 user_id 在数据库中是 NOT NULL，这里必须抛出异常，阻止插入空值
            log.error("获取当前用户 ID 失败，无法保存图片元数据。", e);
            throw new RuntimeException("无法确定上传用户，操作失败"); // 或者抛出自定义异常
        }


        if (files != null) {
            // 遍历文件数组
            for (MultipartFile file : files) {
                // 在 Service 层继续文件基础验证，例如空文件、大小、类型等
                if (file.isEmpty()) {
                    log.warn("Skipping empty file in batch upload (Service level): {}", file.getOriginalFilename());
                    failedFiles.add(file.getOriginalFilename() + " (文件为空)");
                    continue; // 跳过当前文件
                }

                // TODO: 添加文件类型、大小等 Service 级验证

                try {
                    // === 调用 Service 内部的单个文件上传逻辑 ===
                    // 复用 uploadImage 方法，它处理了 Minio 上传和数据库保存
                    ImageData uploadedImage = uploadImage(file, imageData); // 调用当前类的另一个方法
                    successfulUploads.add(uploadedImage); // 将成功上传的结果添加到列表

                } catch (IOException e) {
                    // 捕获来自 uploadImage 的 IO 异常
                    log.error("IO error during batch upload in Service for file {}: {}", file.getOriginalFilename(), e.getMessage());
                    failedFiles.add(file.getOriginalFilename() + " (IO异常: " + e.getMessage() + ")");
                    // 如果使用详细失败信息: Map<String, String> failure = new HashMap<>(); failure.put("fileName", file.getOriginalFilename()); failure.put("reason", "IO异常: " + e.getMessage()); failedFilesDetails.add(failure);
                } catch (Exception e) { // 捕获来自 uploadImage 的其他异常
                    log.error("Unexpected error during batch upload in Service for file {}: {}", file.getOriginalFilename(), e.getMessage(), e);
                    failedFiles.add(file.getOriginalFilename() + " (上传失败: " + e.getMessage() + ")");
                    // 如果使用详细失败信息: Map<String, String> failure = new HashMap<>(); failure.put("fileName", file.getOriginalFilename()); failure.put("reason", "上传失败: " + e.getMessage()); failedFilesDetails.add(failure);
                }
            }
        }

        // 返回包含成功和失败信息的批量上传结果 DTO
        return new BatchUploadResult(successfulUploads, failedFiles);
    }

    @Override
    public String generateNftWatermark(String imageId, String tokenId, String nftId) throws IOException {
        // 1. 查询图片信息
        ImageData imageData = this.getById(imageId);
        if (imageData == null) {
            throw new ServiceException("图片不存在");
        }

        // 2. 获取原图 (BufferedImage)
        BufferedImage originalImage;
        try (InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(originBucket)
                .object(imageData.getOriginMinioKey())
                .build())) {
            originalImage = ImageIO.read(stream);
            if (originalImage == null) {
                throw new ServiceException("无法读取原图数据");
            }
        } catch (Exception e) {
            log.error("从 MinIO 获取原图失败: {}", e.getMessage());
            throw new ServiceException("获取原图失败");
        }

        // --- 动态计算水印尺寸 ---
        int originWidth = originalImage.getWidth();

        // 1. 宽度策略：因为要显示完整地址(42字符)，水印需要足够宽
        // 策略：占据原图宽度的 50% (看起来更霸气，且能容纳长地址)
        // 限制最小宽度为 500px，否则字太小看不清
        int watermarkWidth = Math.max(500, (int) (originWidth * 0.5));

        // 2. 高度策略：宽度变宽了，高度可以相对矮一点，大概 1:3.5 的比例
        int watermarkHeight = (int) (watermarkWidth / 3.5);

        // 3. 生成水印 (传入的是 Token ID)
        BufferedImage textWatermark = createTextWatermarkImage(tokenId, nftId, watermarkWidth, watermarkHeight);

        // 4. 合成
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Thumbnails.of(originalImage)
                .scale(0.9f) // 整体缩放 90%
                .watermark(Positions.BOTTOM_RIGHT, textWatermark, 0.9f)
                .outputQuality(WATERMARK_QUALITY)
                .outputFormat(WATERMARK_OUTPUT_FORMAT)
                .toOutputStream(os);

        byte[] watermarkedBytes = os.toByteArray();
        InputStream is = new ByteArrayInputStream(watermarkedBytes);

        // 5. 上传
        String originalKey = imageData.getOriginMinioKey();
        String watermarkKey = originalKey.substring(0, originalKey.lastIndexOf(".")) + "_nft.jpg";

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(watermarkBucket)
                    .object(watermarkKey)
                    .stream(is, watermarkedBytes.length, -1)
                    .contentType("image/jpeg")
                    .build());
        } catch (Exception e) {
            throw new ServiceException("上传水印图失败");
        }

        // 6. 更新数据库
        String watermarkUrl = "/" + watermarkBucket + "/" + watermarkKey;
        imageData.setWatermarkMinioUrl(watermarkUrl);
        imageData.setWatermarkMinioKey(watermarkKey);
        this.updateById(imageData);

        log.info("NFT 水印图生成完毕: {}", watermarkKey);
        return watermarkUrl;
    }

    /**
     * 辅助方法：绘制包含完整地址的水印
     */
    private BufferedImage createTextWatermarkImage(String tokenId, String nftId, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        // 开启抗锯齿 (Text + Graphics)
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        // --- 1. 绘制半透明黑色圆角背景 ---
        g.setColor(new Color(0, 0, 0, 160)); // 加深背景色，让白色文字更显眼
        int bgMargin = width / 30;
        g.fillRoundRect(bgMargin, bgMargin, width - bgMargin*2, height - bgMargin*2, height/5, height/5);

        // --- 2. 字体与布局计算 ---
        // 垂直方向留白
        int contentHeight = height - bgMargin * 2;
        // 我们有 3 行文字：Title, TokenID, Address
        // 分配比例：Title(30%), TokenID(25%), Address(20%), 间距(25%)

        int titleSize = (int) (contentHeight * 0.28);
        int bodySize = (int) (contentHeight * 0.18);
        int addressSize = (int) (contentHeight * 0.14); // 地址字号稍微小一点点以防溢出

        // 字体
        Font titleFont = new Font("Arial", Font.BOLD, titleSize);
        Font bodyFont = new Font("Arial", Font.BOLD, bodySize);
        // 使用 Monospaced 等宽字体显示地址，更有极客感
        Font addressFont = new Font("Monospaced", Font.PLAIN, addressSize);

        // 颜色
        Color goldColor = new Color(255, 215, 0);
        Color whiteColor = Color.WHITE;
        Color shadowColor = Color.BLACK;

        // 起始 X 坐标 (左对齐，留出边距)
        int startX = bgMargin + (width / 20);
        int currentY = bgMargin + titleSize + (contentHeight / 10); // 第一行基线

        // --- Line 1: 标题 ---
        g.setFont(titleFont);
        drawShadowText(g, "NFT CERTIFIED", startX, currentY, goldColor, shadowColor);

        // --- Line 2: Token ID ---
        currentY += (bodySize * 1.5); // 行间距
        g.setFont(bodyFont);
        drawShadowText(g, "Token ID: #" + tokenId, startX, currentY, whiteColor, shadowColor);

        // --- Line 3: 完整地址 ---
        currentY += (addressSize * 1.8);
        g.setFont(addressFont);

        // 加上 "Owner: " 前缀
        String fullAddressText = "NFT ID: " + nftId;

        // 自动缩放逻辑：如果地址太长超出了背景框，就自动缩小字号
        FontMetrics fm = g.getFontMetrics();
        int maxWidth = width - bgMargin * 2 - (width / 10);
        if (fm.stringWidth(fullAddressText) > maxWidth) {
            // 简单粗暴：字号缩小 20%
            addressFont = new Font("Monospaced", Font.PLAIN, (int)(addressSize * 0.8));
            g.setFont(addressFont);
        }

        drawShadowText(g, fullAddressText, startX, currentY, new Color(200, 200, 200), shadowColor);

        g.dispose();
        return image;
    }

    // 绘制带阴影的文字的辅助小方法
    private void drawShadowText(Graphics2D g, String text, int x, int y, Color color, Color shadowColor) {
        g.setColor(shadowColor);
        g.drawString(text, x + 2, y + 2); // 阴影偏移 2px
        g.setColor(color);
        g.drawString(text, x, y);
    }

    /**
     * 处理原图字节数组，生成缩略图并上传到 MinIO 公有桶。
     *
     * @param fileBytes 原图的字节数组
     * @param imageData 待更新的图片数据实体
     * @param userIdForPath 用于 MinIO 路径的用户 ID
     * @param fileExtension 原图的扩展名（包含点）
     */
    private void createAndUploadThumbnail(byte[] fileBytes, ImageData imageData, String userIdForPath, String fileExtension) {
        // 缩略图 Key 格式: {userId}/thumb_{imageId}.jpg (强制使用 JPG 格式)
        String thumbnailKey = userIdForPath + "/thumb_" + imageData.getImageId() + "." + THUMBNAIL_OUTPUT_FORMAT;
        String publicUrl = "/" + thumbnailBucket + "/" + thumbnailKey;

        try (InputStream originalStream = new ByteArrayInputStream(fileBytes);
             // 1. 创建 ByteArrayOutputStream 作为缓冲区
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // 2. 使用 Thumbnailator 处理并写入到 ByteArrayOutputStream
            Thumbnails.of(originalStream)
                    .size(THUMBNAIL_MAX_SIZE, THUMBNAIL_MAX_SIZE)
                    .outputFormat(THUMBNAIL_OUTPUT_FORMAT)
                    .outputQuality(THUMBNAIL_QUALITY)
                    //写入到缓冲区
                    .toOutputStream(baos);

            // 3. 将缓冲区内容转换为字节数组和 InputStream
            byte[] thumbnailBytes = baos.toByteArray();
            long thumbnailSize = thumbnailBytes.length;

            try (InputStream thumbnailStream = new ByteArrayInputStream(thumbnailBytes)) {

                // 5. 上传缩略图到 MinIO 公有桶
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(thumbnailBucket) // 存储到公共缩略图桶
                        .object(thumbnailKey)
                        .stream(thumbnailStream, thumbnailSize, -1)
                        .contentType("image/" + THUMBNAIL_OUTPUT_FORMAT)
                        .build());
            }

            log.info("成功上传缩略图到 MinIO，Key: {}", thumbnailKey);

            // 6. 更新 ImageData 字段
            imageData.setThumbnailMinioKey(thumbnailKey);
            imageData.setThumbnailMinioUrl(publicUrl);

        } catch (IOException e) {
            log.warn("图片处理失败，ID: {}，可能原因: 文件损坏或I/O错误。", imageData.getImageId(), e);
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("MinIO 缩略图上传失败，Key: {}", thumbnailKey, e);
        } catch (Exception e) {
            // 捕获所有其他 Thumbnailator 可能抛出的异常，例如格式不支持
            log.error("缩略图处理发生未知错误，Key: {}，原因：{}", thumbnailKey, e.getMessage());
        }

        // 如果处理失败，相关的 MinIO Key/URL 字段将保持为 null，不影响原图上传。
    }

    /**
     * 【新增方法】生成水印图并上传到 MinIO 水印桶。
     *
     * @param originalStream 原图的输入流
     * @param originalKey 原图的对象 Key
     * @param contentType 原图的 MIME 类型
     * @return 水印图的公开访问 URL
     * @throws Exception 图片处理或 MinIO 上传失败
     */
//    private String generateAndUploadWatermark(InputStream originalStream, String originalKey, String contentType) throws Exception {
//        // TODO：实现此功能
//        // 1. 假设调用图片处理工具生成水印后的图片字节数组
//        // byte[] watermarkedBytes = ImageProcessor.generateWatermark(originalStream);
//
//        // 仅作示例：此处跳过实际的图片处理，直接使用一个空字节数组
//        byte[] watermarkedBytes = originalStream.readAllBytes();
//
//        String watermarkKey = "watermark-" + originalKey;
//
//        // 2. 将水印图片上传到 Watermark 桶
//        try (InputStream watermarkedStream = new ByteArrayInputStream(watermarkedBytes)) {
//            minioClient.putObject(PutObjectArgs.builder()
//                    .bucket(BUCKET_WATERMARK)
//                    .object(watermarkKey)
//                    .stream(watermarkedStream, watermarkedBytes.length, -1)
//                    .contentType(contentType) // 保持原图的 Content-Type
//                    .build());
//            // log.info("成功上传水印图到 MinIO，Key: {}", watermarkKey);
//        }
//
//        // 3. 返回水印图的公开访问 URL
//        return minioExternalUrl + BUCKET_WATERMARK + "/" + watermarkKey;
//    }

    /**
     * 【专业库实现】从图片流中提取 EXIF、地理位置和色彩信息。
     * * @param stream 图片输入流，将被消耗。
     * @param imageData 待填充的实体
     */
    private void extractAndSetMetadata(InputStream stream, ImageData imageData) {

        try {
            // 1. 使用 metadata-extractor 读取所有元数据
            Metadata metadata = ImageMetadataReader.readMetadata(stream);

            // 2. EXIF 摄影参数提取
            ExifIFD0Directory exifIFD0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            ExifSubIFDDirectory exifSub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (exifIFD0 != null) {
                // 相机厂商和型号
                imageData.setCameraMake(exifIFD0.getString(ExifIFD0Directory.TAG_MAKE));
                imageData.setCameraModel(exifIFD0.getString(ExifIFD0Directory.TAG_MODEL));
            }

            if (exifSub != null) {
                // 镜头型号
                imageData.setLensModel(exifSub.getString(ExifSubIFDDirectory.TAG_LENS_MODEL));
                // 焦距
                if (exifSub.containsTag(ExifSubIFDDirectory.TAG_FOCAL_LENGTH)) {
                    imageData.setFocalLength(exifSub.getRational(ExifSubIFDDirectory.TAG_FOCAL_LENGTH).toString());
                }
                // 光圈 (FNumber)
                if (exifSub.containsTag(ExifSubIFDDirectory.TAG_FNUMBER)) {
                    imageData.setAperture("f/" + exifSub.getRational(ExifSubIFDDirectory.TAG_FNUMBER).toString());
                }
                // 快门速度
                if (exifSub.containsTag(ExifSubIFDDirectory.TAG_EXPOSURE_TIME)) {
                    imageData.setShutterSpeed(exifSub.getRational(ExifSubIFDDirectory.TAG_EXPOSURE_TIME).toString());
                }
                // ISO
                if (exifSub.containsTag(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT)) {
                    imageData.setIso(exifSub.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
                }
                // 拍摄时间
                Date shootDate = exifSub.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if (shootDate != null) {
                    // 消除系统时区（UTC+8）带来的额外偏移。
                    imageData.setShootTime(shootDate.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime());
                }
            }

            // 3. 地理位置 (GPS) 提取
            GpsDirectory gpsDir = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            if (gpsDir != null) {
                GeoLocation location = gpsDir.getGeoLocation();
                if (location != null && !location.isZero()) {
                    imageData.setLatitude(BigDecimal.valueOf(location.getLatitude()));
                    imageData.setLongitude(BigDecimal.valueOf(location.getLongitude()));
                    // Location Name 需要调用逆地理编码 API (如 Google Maps, Baidu Maps)，此处无法实现，保持模拟或空
                    imageData.setLocationName(null);
                }
            }

            // 4. 其他元数据 (宽度/高度)
            JpegDirectory jpegDir = metadata.getFirstDirectoryOfType(JpegDirectory.class);
            if (jpegDir != null) {
                imageData.setWidth(jpegDir.getImageWidth());
                imageData.setHeight(jpegDir.getImageHeight());
            }

        } catch (Exception e) {
            log.warn("提取 EXIF/GPS 元数据失败: {}", e.getMessage());
            // 忽略元数据提取失败，继续下一步
        }

        // 5. 分类、标签、色彩 (模拟或默认)
        if (imageData.getDominantColor() == null) {
            // 真实的色彩分析逻辑非常复杂，此处仅用默认值填充
            // imageData.setDominantColor(calculateDominantColor(new ByteArrayInputStream(imageBytes)));
            imageData.setDominantColor("#FFFFFF");
        }


        // 统计字段（数据库已有默认值，此处可选择不设或设为 0L）
        imageData.setViewCount(0L);
        imageData.setDownloadCount(0L);
        imageData.setLikeCount(0L);
    }

    // ===============================================
    // 图片详情页
    // ===============================================
    @Override
    public ImageData getImageData(String imageId) {
        // 使用 Mybatis Plus ServiceImpl 提供的 getById 方法
        ImageData imageData = this.getById(imageId);
        // 检查是否被标记为删除
        if (imageData != null && imageData.getIsDelete()) {
            return null; // 如果已删除，视为找不到
        }
        assert imageData != null;
        // imageData.setMinioUrl("localhost:19000" + imageData.getMinioUrl()); //测试用

        // 拼接配置文件中的minio服务url：确保 minioExternalUrl 不以斜杠结尾，MinioUrl 以斜杠开头
        String url = minioExternalUrl.endsWith("/") ? minioExternalUrl.substring(0, minioExternalUrl.length() - 1) : minioExternalUrl;
        imageData.setOriginMinioUrl(url + imageData.getOriginMinioUrl());
        imageData.setThumbnailMinioUrl(url + imageData.getThumbnailMinioUrl());
        if(imageData.getWatermarkMinioUrl() != null){
            imageData.setWatermarkMinioUrl(url + imageData.getWatermarkMinioUrl());
        }
        return imageData;
    }

    @Override
    public ImagePresignedUrlData getPresignedUrl(String imageId) throws ServiceException {
        // 设置 URL 的有效期，10分钟
        final int EXPIRATION_TIME = 10;

        // 假设 ImageData 类和相关的 MinIO 客户端 (minioClient, originBucket) 都是在类中定义的成员变量

        // 查询Key
        ImageData imageData = null;
        ImagePresignedUrlData  imagePresignedUrlData = new  ImagePresignedUrlData();
        try {
            // 使用 try-catch 捕获可能的数据库查询异常（尽管 one() 更多是返回 null 或依赖其他异常处理）
            imageData = this.lambdaQuery()
                    .eq(ImageData::getImageId, imageId) // 根据 imageId 匹配记录
                    .select(ImageData::getOriginMinioKey) // 仅查询需要的 Key 字段
                    .one(); // 获取单个结果
        } catch (Exception e) {
            // 捕获数据库查询或其他潜在异常
            // 可以选择记录日志，然后抛出自定义异常或重新抛出
            log.error("Database query failed for imageId: " + imageId + ". Error: " + e.getMessage());
            throw new ServiceException("图片信息查询失败", e);
        }


        if (imageData == null) {
            // 如果查不到记录，说明图片不存在，或者当前用户没有权限访问。
            throw new ServiceException("图片不存在");
        }

        try {
            // 尝试生成预签名 URL
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET) // 訪問方式為 GET (下載/查看)
                            .bucket(originBucket)
                            .object(imageData.getOriginMinioKey())
                            .expiry(EXPIRATION_TIME, TimeUnit.MINUTES) // 設置有效期
                            .build()
            );

            imagePresignedUrlData.setPresignedUrl(url);
            imagePresignedUrlData.setImageId(imageId);

            return imagePresignedUrlData;

        } catch (Exception e) {
            // 捕获 MinIO 客户端可能抛出的所有异常（例如网络、权限、配置等）
            log.error("Failed to get presigned URL for key: " + imageData.getOriginMinioKey() + ". Error: " + e.getMessage());
            // 将 MinIO 异常包装为更友好的异常并抛出
            throw new ServiceException("生成预签名 URL 失败", e);
        }
    }

    /**
     * 实现：根据图片 ID 获取图片文件流和元数据。
     *
     * @param imageId 图片 ID
     * @return 包含 InputStream 和元数据的 ImageStreamData 对象。如果找不到或出错，则抛出异常或返回 null。
     * @throws ServiceException 如果图片数据不完整或从 MinIO 获取时出错。
     */
    @Override
    public ImageStreamData getImageStreamData(String imageId) throws IOException {
        // 1. 尝试获取图片元数据
        String metadataKey = "img:metadata:" + imageId;
        ImageData imageData = redisCache.getCacheObject(metadataKey);

        if (imageData == null) {
            // 2. 从数据库获取元数据
            imageData = this.getById(imageId);
            if (imageData == null || imageData.getIsDelete()) {
                log.warn("找不到图片数据或图片已被标记为删除，imageId: {}", imageId);
                throw new ServiceException(ResultCodeEnum.NOT_FOUND);
            }

            // 3. 将元数据存入缓存
            redisCache.setCacheObject(metadataKey, imageData, CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
        }

        // 4. 检查必要的元数据
//        if (StringUtil.isNull(imageData.getMinioKey()) || StringUtil.isNull(imageData.getContentType())) {
//            log.error("图片元数据不完整 (缺少 minioKey 或 contentType)，imageId: {}", imageId);
//            throw new ServiceException("图片数据不完整，无法提供文件流，imageId: " + imageId);
//        }
//
//        // 5. 从 MinIO 获取对象输入流
//        InputStream minioInputStream;
//        try {
//            minioInputStream = minioClient.getObject(
//                    GetObjectArgs.builder()
//                            .bucket(thumbnailBucket)
//                            .object(imageData.getMinioKey())
//                            .build());
//            log.info("成功从 MinIO 取得对象流，Key: {}", imageData.getMinioKey());
//        } catch (MinioException e) {
//            log.error("从 MinIO 获取对象流时发生 MinioException，Key {}: {}", imageData.getMinioKey(), e.getMessage(), e);
//            throw new ServiceException("无法从存储体获取图片: " + e.getMessage(), e);
//        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
//            log.error("从 MinIO 获取对象流时发生错误，Key {}: {}", imageData.getMinioKey(), e.getMessage(), e);
//            throw new ServiceException("获取图片流时发生错误: " + e.getMessage(), e);
//        } catch (Exception e) {
//            log.error("从 MinIO 获取对象流时发生未预期错误，Key {}: {}", imageData.getMinioKey(), e.getMessage(), e);
//            throw new ServiceException("获取图片时发生未预期错误: " + e.getMessage(), e);
//        }

        // 6. 创建 ImageStreamData 对象
        ImageStreamData streamData = new ImageStreamData();
//        streamData.setInputStream(minioInputStream);
//        streamData.setContentType(imageData.getContentType());
//        streamData.setSize(imageData.getSize());
//        streamData.setFileName(imageData.getFileName());

        // 7. 移除完整的 ImageStreamData 缓存逻辑

        return streamData;
    }


    @Override
    public List<ImageData> getImagesByUserId(String userId) {
        // 使用 LambdaQueryWrapper 更安全、易读
        LambdaQueryWrapper<ImageData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImageData::getUserId, userId) // 条件：userId 相符
                .eq(ImageData::getIsDelete, false) // 条件：未被删除
                .orderByDesc(ImageData::getCreateTime); // 可选：按创建时间排序
        return this.list(queryWrapper); // 使用 ServiceImpl 的 list 方法执行查询
    }

    @Override
    public List<ImageData> getPublicImages() {
        LambdaQueryWrapper<ImageData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImageData::getIsPublic, true) // 条件：isPublic 为 true
                .eq(ImageData::getIsDelete, false) // 条件：未被删除
                .orderByDesc(ImageData::getCreateTime); // 可选：按创建时间排序
        return this.list(queryWrapper);
    }

    //使用此方法获取原图
    @Override
    public ImageStreamData getMinioImageById(String imageId) {
        // 1. 从数据库获取元数据
        ImageData imageData = this.getById(imageId);

        // 2. 检查图片是否存在且未被删除
        if (imageData == null || imageData.getIsDelete()) {
            log.warn("找不到图片数据或图片已被标记为删除，imageId: {}", imageId);
            throw new ServiceException(ResultCodeEnum.NOT_FOUND);
        }

        // 3. 检查必要的元数据是否存在
        if (StringUtil.isNull(imageData.getOriginMinioKey()) || StringUtil.isNull(imageData.getContentType())) {
            log.error("图片元数据不完整 (缺少 minioKey 或 contentType)，imageId: {}", imageId);
            // 数据不一致，应该抛出异常
            throw new ServiceException("图片数据不完整，无法提供文件流，imageId: " + imageId);
        }

        // 4. 从 MinIO 获取对象输入流
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(originBucket)
                            .object(imageData.getOriginMinioKey())
                            .build());
            log.info("成功从 MinIO 取得对象流，Key: {}", imageData.getOriginMinioKey());

            // 5. 创建并返回 ImageStreamData DTO
            return new ImageStreamData(
                    inputStream,                // MinIO 的输入流
                    imageData.getContentType(), // 从数据库读取的 ContentType
                    imageData.getSize(),        // 从数据库读取的文件大小
                    imageData.getFileName()     // 从数据库读取的原始文件名
            );
        } catch (MinioException e) {
            log.error("从 MinIO 获取对象流时发生 MinioException，Key {}: {}", imageData.getOriginMinioKey(), e.getMessage(), e);
            // 将 MinIO 异常包装成服务层异常
            throw new ServiceException("无法从存储体获取图片: " + e.getMessage(), e);
        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("从 MinIO 获取对象流时发生错误，Key {}: {}", imageData.getOriginMinioKey(), e.getMessage(), e);
            throw new ServiceException("获取图片流时发生错误: " + e.getMessage(), e);
        } catch (Exception e) { // 捕捉其他未预期的异常
            log.error("从 MinIO 获取对象流时发生未预期错误，Key {}: {}", imageData.getOriginMinioKey(), e.getMessage(), e);
            throw new ServiceException("获取图片时发生未预期错误: " + e.getMessage(), e);
        }
        // 注意：这里获取的 InputStream 不需要手动关闭，
        // 因为它将被传递给 Controller 中的 InputStreamResource，
        // Spring 会在响应完成后自动关闭它。
    }

    // 获取缩略图列表
    @Override
    public List<ImageStreamData> getMinioImagesByUserId(String userId) {
        // 1. 从数据库查询 userId 对应的图片数据列表
        List<ImageData> imageDataList = this.list(new QueryWrapper<ImageData>()
                .eq("user_id", userId)
                .eq("is_delete", false)); // 仅查询未删除的图片

        if (imageDataList == null || imageDataList.isEmpty()) {
            log.warn("找不到用户 {} 的任何图片数据。", userId);
            return Collections.emptyList(); // 返回空列表
        }

        List<ImageStreamData> streamDataList = new ArrayList<>();

        // 2. 遍历图片数据列表，从 MinIO 获取每个图片的流
        for (ImageData imageData : imageDataList) {
            if (StringUtil.isNull(imageData.getThumbnailMinioKey()) || StringUtil.isNull(imageData.getContentType())) {
                log.error("图片元数据不完整 (缺少 minioKey 或 contentType)，imageId: {}", imageData.getImageId());
                // 可以选择跳过当前图片或抛出异常，这里选择跳过并记录
                continue;
            }

            try {
                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(thumbnailBucket)
                                .object(imageData.getThumbnailMinioKey())
                                .build());
                log.info("成功从 MinIO 取得用户 {} 的对象流，Key: {}", userId, imageData.getThumbnailMinioKey());

                streamDataList.add(new ImageStreamData(
                        inputStream,
                        imageData.getContentType(),
                        imageData.getSize(),
                        imageData.getFileName()
                ));

            } catch (MinioException e) {
                log.error("从 MinIO 获取用户 {} 的对象流时发生 MinioException，Key {}: {}", userId, imageData.getThumbnailMinioKey(), e.getMessage(), e);
                // 可以选择继续处理其他图片或抛出异常，这里选择继续
            } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
                log.error("从 MinIO 获取用户 {} 的对象流时发生错误，Key {}: {}", userId, imageData.getThumbnailMinioKey(), e.getMessage(), e);
                // 可以选择继续处理其他图片或抛出异常，这里选择继续
            } catch (Exception e) {
                log.error("从 MinIO 获取用户 {} 的对象流时发生未预期错误，Key {}: {}", userId, imageData.getThumbnailMinioKey(), e.getMessage(), e);
                // 可以选择继续处理其他图片或抛出异常，这里选择继续
            }
        }
        return streamDataList;
    }

    // 根据用户Id返回urlList
    @Override
    public List<ImageUrlData> getMinioImageUrlListByUserId(String userId) {
        List<ImageData> imageDataList = this.list(new QueryWrapper<ImageData>()
                .eq("user_id", userId)
                .eq("is_delete", false)); // 仅查询未删除的图片

        if (imageDataList == null || imageDataList.isEmpty()) {
            log.warn("找不到用户 {} 的任何图片数据。", userId);
            return Collections.emptyList(); // 返回空列表
        }

        // 预处理 Minio 外部 URL，确保没有尾部斜杠
        String baseUrl = minioExternalUrl.endsWith("/") ? minioExternalUrl.substring(0, minioExternalUrl.length() - 1) : minioExternalUrl;

        List<ImageUrlData> urlDataList = new ArrayList<>();
        for (ImageData imageData : imageDataList) {
            if (StringUtil.isNull(imageData.getThumbnailMinioKey()) || StringUtil.isNull(imageData.getContentType())) {
                log.error("图片元数据不完整 (缺少 minioKey 或 contentType)，imageId: {}", imageData.getImageId());
                continue;
            }

            // 拼接完整的 URL
            String fullUrl = baseUrl + imageData.getThumbnailMinioUrl();

            // 更新当前 ImageData 对象的 MinioUrl，以便返回给前端
            // 注意：这只修改了内存中的对象，没有修改数据库
            imageData.setThumbnailMinioUrl(fullUrl);

            urlDataList.add(new ImageUrlData(
                    imageData.getImageId(),
                    imageData.getThumbnailMinioUrl(), // 使用拼接好的完整 URL
                    imageData.getWatermarkMinioUrl(),
                    imageData.getFileName(),
                    imageData.getUserId(),
                    imageData.getContentType(),
                    imageData.getSize(),
                    imageData.getIsPublic(),
                    imageData.getDescription()
            ));
            log.info("成功从 MinIO 取得用户 {} 的对象Url_json，Key: {}", userId, imageData.getThumbnailMinioKey());
        }
        return urlDataList;
    }

    @Override
    public ImageData updateImageMetadata(ImageData imageData) {
        if (imageData == null || imageData.getImageId() == null) {
            // 可以抛出异常或者返回 null，取决于你的业务逻辑
            throw new ServiceException(MISSING_REQUIRED_PARAMETER);
        }

        UpdateWrapper<ImageData> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("image_id", imageData.getImageId()); // 根据 imageId 更新

        // 只更新 ImageData 对象中不为 null 的字段
        updateWrapper.set(imageData.getFileName() != null, "file_name", imageData.getFileName());
        updateWrapper.set(imageData.getIsPublic() != null, "is_public", imageData.getIsPublic());
        updateWrapper.set(imageData.getDescription() != null, "description", imageData.getDescription());

        int rowsAffected = imageDataMapper.update(null, updateWrapper);

        if (rowsAffected > 0) {
            // 根据 imageId 查询数据库获取最新的 ImageData
            return imageDataMapper.selectById(imageData.getImageId());
        } else {
            throw new ServiceException(DATABASE_ERROR, "更新失败"); // 或者返回
        }
    }

    @Override
    public void deleteImageMetadata(String imageId) {
        if (imageId == null) {
            throw new ServiceException(MISSING_REQUIRED_PARAMETER);
        }
        UpdateWrapper<ImageData> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("image_id", imageId);
        updateWrapper.set("is_delete", true);
        imageDataMapper.update(null, updateWrapper);
    }

    @Override
    public void switchPublicStatus(String imageId) {
        if (imageId == null) {
            throw new ServiceException(MISSING_REQUIRED_PARAMETER);
        }
        try {
            UpdateWrapper<ImageData> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("image_id", imageId);
            updateWrapper.set("is_public", !imageDataMapper.selectById(imageId).getIsPublic());
            imageDataMapper.update(null, updateWrapper);
        }
        catch (Exception e) {
            log.error("切换图片公开状态时发生错误，imageId: {}", imageId, e);
            throw new ServiceException(DATABASE_ERROR, "切换图片公开状态时发生错误");
        }
    }

    @Override
    public Long getTotalStorageUsage() {
        return baseMapper.sumSize();
    }

    @Override
    public List<StatsDTO> getUploadTrend() {
        return baseMapper.getUploadTrend();
    }

    @Override
    public List<StatsDTO> getImageTypeDistribution() {
        return baseMapper.getImageTypeDistribution();
    }

    @Override
    public Long getMintedCount() {
        return baseMapper.countMinted();
    }

    @Override
    public Long getPublicCount() {
        return baseMapper.countPublic();
    }

    @Override
    public Long getPrivateCount() {
        return baseMapper.countPrivate();
    }

    @Override
    public void deleteImage(String imageId) {
        ImageData image = getById(imageId);
        if (image == null) {
            throw new ServiceException("Image not found");
        }

        // 1. Delete from MinIO
        try {
            if (StringUtil.isNotEmpty(image.getOriginMinioKey())) {
                minioClient.removeObject(io.minio.RemoveObjectArgs.builder().bucket(originBucket).object(image.getOriginMinioKey()).build());
            }
            if (StringUtil.isNotEmpty(image.getThumbnailMinioKey())) {
                minioClient.removeObject(io.minio.RemoveObjectArgs.builder().bucket(thumbnailBucket).object(image.getThumbnailMinioKey()).build());
            }
        } catch (Exception e) {
            log.error("Failed to delete from MinIO", e);
        }

        // 2. Delete from DB
        removeById(imageId);

        // 3. Delete from Blockchain
        try {
            restTemplate.delete(blockchainApiUrl + "/nft/image/" + imageId);
        } catch (Exception e) {
            log.error("Failed to delete from Blockchain", e);
        }
    }

    @Override
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<ImageData> getImageList(Integer page, Integer size, String keyword, String type) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ImageData> p = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        LambdaQueryWrapper<ImageData> wrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotEmpty(keyword)) {
            wrapper.and(w -> w.like(ImageData::getFileName, keyword).or().eq(ImageData::getImageId, keyword));
        }
        if (StringUtil.isNotEmpty(type)) {
             if ("public".equalsIgnoreCase(type)) {
                 wrapper.eq(ImageData::getIsPublic, true);
             } else if ("private".equalsIgnoreCase(type)) {
                 wrapper.eq(ImageData::getIsPublic, false);
             }
        }
        wrapper.orderByDesc(ImageData::getCreateTime);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ImageData> result = page(p, wrapper);

        // 处理 URL，加上 minioExternalUrl
        if (result.getRecords() != null) {
            result.getRecords().forEach(img -> {
                if (img.getOriginMinioUrl() != null && !img.getOriginMinioUrl().startsWith("http")) {
                    img.setOriginMinioUrl(minioExternalUrl + img.getOriginMinioUrl());
                }
                if (img.getThumbnailMinioUrl() != null && !img.getThumbnailMinioUrl().startsWith("http")) {
                    img.setThumbnailMinioUrl(minioExternalUrl + img.getThumbnailMinioUrl());
                }
            });
        }

        return result;
    }
}