package moe.imtop1.imagehosting.system.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.system.entity.Notice;
import moe.imtop1.imagehosting.system.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    @GetMapping("/list")
    public AjaxResult list() {
        List<Notice> list = noticeService.lambdaQuery()
                .orderByDesc(Notice::getCreateTime)
                .list();
        return AjaxResult.success(list);
    }

    @SaCheckRole("admin")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody Notice notice) {
        notice.setPublisherId(StpUtil.getLoginIdAsString());
        noticeService.save(notice);
        return AjaxResult.success();
    }

    @SaCheckRole("admin")
    @PutMapping("/update")
    public AjaxResult update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return AjaxResult.success();
    }

    @SaCheckRole("admin")
    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable String id) {
        noticeService.removeById(id);
        return AjaxResult.success();
    }
}
