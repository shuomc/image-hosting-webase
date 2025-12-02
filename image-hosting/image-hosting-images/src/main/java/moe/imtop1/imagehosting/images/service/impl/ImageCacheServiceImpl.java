package moe.imtop1.imagehosting.images.service.impl;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import moe.imtop1.imagehosting.images.domain.vo.ImagePresignedUrlData;
import moe.imtop1.imagehosting.images.service.ImageCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * @author shuomc
 * @Date 2025/4/17
 * @Description
 */
@Service
public class ImageCacheServiceImpl implements ImageCacheService{

    private static final Logger logger = LoggerFactory.getLogger(ImageCacheServiceImpl.class);

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${minio.thumbnailBucket}")
    private String thumbnailBucket;

    private static final String REDIS_KEY_PREFIX = "minio:object:";
    private static final long REDIS_EXPIRATION_TIME_SECONDS = 3600;

    public byte[] getMinioObjectDataFromRedis(String objectName) {

        String redisKey = REDIS_KEY_PREFIX + objectName;

        byte[] objectData = new byte[0];
        try {
            // 检查 Redis 中是否已存在缓存
            if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
                logger.info("Found object info in Redis for: {}", objectName);
                Object cachedInfo = redisTemplate.opsForValue().get(redisKey);
                logger.info("Cached Info: {}", cachedInfo);
                return (byte[]) cachedInfo;
            }

            // 从 Minio 获取对象信息
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(thumbnailBucket)
                    .object(objectName)
                    .build();

            try (GetObjectResponse response = minioClient.getObject(getObjectArgs)) {
                objectData = response.readAllBytes();
                logger.info("Read {} bytes from Minio object: {}", objectData.length, objectName);

                // 将对象数据存储到 Redis
                redisTemplate.opsForValue().set(redisKey, objectData, REDIS_EXPIRATION_TIME_SECONDS, TimeUnit.SECONDS);
                logger.info("Stored {} bytes of {} in Redis with key: {}", objectData.length, objectName, redisKey);

            } catch (MinioException | IOException | NoSuchAlgorithmException e) {
                logger.error("Error while fetching object from Minio: {}", e.getMessage());
            }

        } catch (Exception e) {
            logger.error("Error interacting with Redis: {}", e.getMessage());
        }
        return objectData;
    }

    @Override
    public void setMinioObjectInfoInRedis(String objectName) {
        return;
    }

    public Object getMinioObjectInfoFromRedis(String objectName) {
        String redisKey = REDIS_KEY_PREFIX + objectName;
        return redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public ImagePresignedUrlData getPresignUrlFromRedis(String imageId) {
        // 1. 校验输入
        if (imageId == null || imageId.trim().isEmpty()) {
            logger.warn("Image ID is null or empty when trying to read from Redis.");
            return null;
        }

        String presignedUrl = null;

        try {
            // 2. 从 Redis 读取值。
            presignedUrl = stringRedisTemplate.opsForValue().get(imageId);

            // 成功读取后可以记录 INFO/DEBUG 日志
            if (presignedUrl != null) {
                logger.info("Successfully retrieved presigned URL for ID: {}", imageId);
            } else {
                // key 不存在或已过期，get() 返回 null
                logger.warn("Presigned URL for ID {} not found in Redis (might be expired).", imageId);
            }

        } catch (Exception e) {
            // 3. 捕获 Redis 异常，例如连接失败
            logger.error("Failed to get presigned URL from Redis for ID: {}", imageId, e);
            // 在缓存读取失败时，不应阻塞主业务流程，返回 null 让上层逻辑去生成新的 URL 或从数据库读取。
            return null;
        }

        // 4. 封装并返回结果
        if (presignedUrl != null) {
            // 假设 ImagePresignedUrlData 有合适的构造函数或 setter
            ImagePresignedUrlData data = new ImagePresignedUrlData();
            data.setImageId(imageId);
            data.setPresignedUrl(presignedUrl);
            return data;
        }

        // 如果 Redis 中没有找到 (presignedUrl == null) 或读取失败，则返回 null
        return null;
    }

    @Override
    public void setPresignUrlFromRedis(ImagePresignedUrlData imagePresignedUrlData) {
        // 校验输入对象是否为空
        if (imagePresignedUrlData == null) {
            logger.error("Image presigned url data is null");
            return;
        }

        String key = imagePresignedUrlData.getImageId();
        String value = imagePresignedUrlData.getPresignedUrl();

        // 确保 key 和 value 有效
        if (key != null && value != null) {
            try {
                // 存入 Redis，设置 10 分钟过期时间
                stringRedisTemplate.opsForValue().set(key, value, 10, TimeUnit.MINUTES);
                logger.info("Successfully set presigned URL for key: {}", key);
            } catch (Exception e) {
                // 捕获所有潜在的 Redis 异常
                logger.warn("Failed to set presigned URL to Redis for key: {}", key, e);
            }
        } else {
            logger.warn("Image ID or Presigned URL is null. Key: {}, Value length: {}",
                    key, (value == null ? "null" : value.length()));
        }
    }
}
