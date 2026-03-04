package moe.imtop1.imagehosting.images.service;

import com.baomidou.mybatisplus.extension.service.IService;
import moe.imtop1.imagehosting.images.domain.Favorite;
import moe.imtop1.imagehosting.images.domain.ImageData;
import java.util.List;

public interface FavoriteService extends IService<Favorite> {
    boolean addFavorite(String userId, String imageId);
    boolean removeFavorite(String userId, String imageId);
    boolean isFavorite(String userId, String imageId);
    List<ImageData> getUserFavorites(String userId);
}
