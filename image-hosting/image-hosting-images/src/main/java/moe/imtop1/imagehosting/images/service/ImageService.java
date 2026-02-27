package moe.imtop1.imagehosting.images.service;

import com.baomidou.mybatisplus.extension.service.IService;
import moe.imtop1.imagehosting.images.domain.ImageData;
import moe.imtop1.imagehosting.images.domain.dto.BatchUploadResult;
import moe.imtop1.imagehosting.images.domain.dto.ImageStreamData;
import moe.imtop1.imagehosting.images.domain.vo.ImagePresignedUrlData;
import moe.imtop1.imagehosting.images.domain.vo.ImageUrlData;
import moe.imtop1.imagehosting.images.domain.dto.StatsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ImageService extends IService<ImageData> {
    ImageData uploadImage(MultipartFile file, ImageData imageData) throws IOException;

    BatchUploadResult batchUploadImages(MultipartFile[] files, ImageData imageData);

    String generateNftWatermark(String imageId, String tokenId, String nftId) throws IOException;

    ImageData getImageData(String imageId);

    ImageStreamData getImageStreamData(String imageId) throws IOException;

    List<ImageData> getImagesByUserId(String userId);

    List<ImageData> getPublicImages();

    ImageStreamData getMinioImageById(String imageId);

    ImageStreamData getWatermarkImageById(String imageId);

    List<ImageStreamData> getMinioImagesByUserId(String userId);

    List<ImageUrlData> getMinioImageUrlListByUserId(String userId);

    ImagePresignedUrlData getPresignedUrl(String imageId);

    ImageData updateImageMetadata(ImageData imageData);

    void deleteImageMetadata(String imageId);

    void switchPublicStatus(String imageId);

    Long getTotalStorageUsage();

    List<StatsDTO> getUploadTrend();

    List<StatsDTO> getImageTypeDistribution();

    Long getMintedCount();

    Long getPublicCount();

    Long getPrivateCount();

    Long getMintedCountByUserId(String userId);

    Long getPublicCountByUserId(String userId);

    Long getPrivateCountByUserId(String userId);

    void deleteImage(String imageId);

    com.baomidou.mybatisplus.extension.plugins.pagination.Page<ImageData> getImageList(Integer page, Integer size, String keyword, String type);
}
