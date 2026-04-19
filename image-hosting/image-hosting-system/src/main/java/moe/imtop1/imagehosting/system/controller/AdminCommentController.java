package moe.imtop1.imagehosting.system.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.system.domain.Comment;
import moe.imtop1.imagehosting.system.service.IAdminCommentService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台评论管理控制器
 */
@RestController
@RequestMapping("/api/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final IAdminCommentService commentService;

    /**
     * 分页查询评论列表
     */
    @SaCheckRole("admin")
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(required = false) String keyword) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(w -> w.like(Comment::getContent, keyword)
                    .or()
                    .like(Comment::getUserName, keyword));
        }
        
        queryWrapper.orderByDesc(Comment::getCreateTime);
        
        Page<Comment> result = commentService.page(page, queryWrapper);
        
        AjaxResult ajax = AjaxResult.success();
        ajax.put("rows", result.getRecords());
        ajax.put("total", result.getTotal());
        
        return ajax;
    }

    /**
     * 修改评论
     */
    @SaCheckRole("admin")
    @PutMapping("/update")
    public AjaxResult update(@RequestBody Comment comment) {
        if (comment.getCommentId() == null) {
            return AjaxResult.error("评论ID不能为空");
        }
        commentService.updateById(comment);
        return AjaxResult.success("修改成功");
    }

    /**
     * 删除评论
     */
    @SaCheckRole("admin")
    @DeleteMapping("/delete/{commentId}")
    public AjaxResult delete(@PathVariable String commentId) {
        commentService.removeById(commentId);
        return AjaxResult.success("删除成功");
    }
}
