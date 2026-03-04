package moe.imtop1.imagehosting.images.controller;

import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.images.domain.ImageData;
import moe.imtop1.imagehosting.images.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{imageId}")
    public AjaxResult addFavorite(@PathVariable String imageId) {
        String userId = StpUtil.getLoginIdAsString();
        return AjaxResult.success(favoriteService.addFavorite(userId, imageId));
    }

    @DeleteMapping("/{imageId}")
    public AjaxResult removeFavorite(@PathVariable String imageId) {
        String userId = StpUtil.getLoginIdAsString();
        return AjaxResult.success(favoriteService.removeFavorite(userId, imageId));
    }

    @GetMapping("/check/{imageId}")
    public AjaxResult isFavorite(@PathVariable String imageId) {
        String userId = StpUtil.getLoginIdAsString();
        return AjaxResult.success(favoriteService.isFavorite(userId, imageId));
    }

    @GetMapping("/list")
    public AjaxResult getUserFavorites() {
        String userId = StpUtil.getLoginIdAsString();
        return AjaxResult.success(favoriteService.getUserFavorites(userId));
    }
}
