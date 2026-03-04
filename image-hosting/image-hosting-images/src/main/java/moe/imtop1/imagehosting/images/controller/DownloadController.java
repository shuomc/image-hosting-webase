package moe.imtop1.imagehosting.images.controller;

import cn.dev33.satoken.stp.StpUtil;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.images.entity.Download;
import moe.imtop1.imagehosting.images.service.IDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/downloads")
public class DownloadController {

    @Autowired
    private IDownloadService downloadService;

    @PostMapping("/record")
    public AjaxResult record(@RequestBody Download download) {
        if (download.getImageId() == null) {
            return AjaxResult.error("Image ID is required");
        }

        // Try to get user ID if logged in, otherwise leave it null or handle as anonymous
        if (StpUtil.isLogin()) {
            download.setUserId(StpUtil.getLoginIdAsString());
        } else {
            // Optional: set to specific value for anonymous or leave null
            // download.setUserId("anonymous");
        }

        downloadService.save(download);
        return AjaxResult.success();
    }
}
