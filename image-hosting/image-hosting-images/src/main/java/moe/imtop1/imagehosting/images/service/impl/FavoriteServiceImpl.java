package moe.imtop1.imagehosting.images.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.images.domain.Favorite;
import moe.imtop1.imagehosting.images.domain.ImageData;
import moe.imtop1.imagehosting.images.mapper.FavoriteMapper;
import moe.imtop1.imagehosting.images.mapper.ImageMapper;
import moe.imtop1.imagehosting.images.service.FavoriteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    private final ImageMapper imageMapper;

    @Value("${minio.external-url}")
    private String minioExternalUrl;

    @Override
    public boolean addFavorite(String userId, String imageId) {
        if (isFavorite(userId, imageId)) {
            return true;
        }
        
        // 检查是否存在（包含已删除的）
        if (baseMapper.countAll(userId, imageId) > 0) {
            // 恢复
            return baseMapper.restoreFavorite(userId, imageId) > 0;
        }
        
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setImageId(imageId);
        return save(favorite);
    }

    @Override
    public boolean removeFavorite(String userId, String imageId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getImageId, imageId);
        return remove(wrapper);
    }

    @Override
    public boolean isFavorite(String userId, String imageId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getImageId, imageId);
        return count(wrapper) > 0;
    }

    @Override
    public List<ImageData> getUserFavorites(String userId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .orderByDesc(Favorite::getCreateTime);
        List<Favorite> favorites = list(wrapper);
        
        if (favorites.isEmpty()) {
            return List.of();
        }

        List<String> imageIds = favorites.stream()
                .map(Favorite::getImageId)
                .collect(Collectors.toList());

        List<ImageData> imageDataList = imageMapper.selectBatchIds(imageIds);

        // 拼接配置文件中的minio服务url
        String url = minioExternalUrl.endsWith("/") ? minioExternalUrl.substring(0, minioExternalUrl.length() - 1) : minioExternalUrl;
        for (ImageData imageData : imageDataList) {
            if (imageData.getOriginMinioUrl() != null && !imageData.getOriginMinioUrl().startsWith("http")) {
                imageData.setOriginMinioUrl(url + imageData.getOriginMinioUrl());
            }
            if (imageData.getThumbnailMinioUrl() != null && !imageData.getThumbnailMinioUrl().startsWith("http")) {
                imageData.setThumbnailMinioUrl(url + imageData.getThumbnailMinioUrl());
            }
            if (imageData.getWatermarkMinioUrl() != null && !imageData.getWatermarkMinioUrl().startsWith("http")) {
                imageData.setWatermarkMinioUrl(url + imageData.getWatermarkMinioUrl());
            }
        }

        return imageDataList;
    }
}
