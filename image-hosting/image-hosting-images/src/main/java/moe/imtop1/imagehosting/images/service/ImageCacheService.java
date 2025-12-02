package moe.imtop1.imagehosting.images.service;

import moe.imtop1.imagehosting.images.domain.vo.ImagePresignedUrlData;

public interface ImageCacheService {
    void setMinioObjectInfoInRedis(String objectName);

    Object getMinioObjectInfoFromRedis(String objectName);

    byte[] getMinioObjectDataFromRedis(String objectName);

    // redis 存取预签名
    ImagePresignedUrlData getPresignUrlFromRedis(String imageId);

    void setPresignUrlFromRedis(ImagePresignedUrlData  imagePresignedUrlData);
}
