package moe.imtop1.imagehosting.images.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.images.service.ImageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/images")
@RequiredArgsConstructor
public class AdminImageController {

    private final ImageService imageService;

    @SaCheckRole("admin")
    @GetMapping("/list")
    public AjaxResult getImageList(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String type) {
        return AjaxResult.success(imageService.getImageList(page, size, keyword, type));
    }

    @SaCheckRole("admin")
    @DeleteMapping("/{imageId}")
    public AjaxResult deleteImage(@PathVariable String imageId) {
        imageService.deleteImage(imageId);
        return AjaxResult.success("Image deleted successfully");
    }
}
