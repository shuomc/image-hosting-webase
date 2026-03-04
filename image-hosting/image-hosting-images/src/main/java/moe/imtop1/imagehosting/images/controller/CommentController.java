package moe.imtop1.imagehosting.images.controller;

import cn.dev33.satoken.stp.StpUtil;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.images.entity.Comment;
import moe.imtop1.imagehosting.images.service.ICommentService;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IUserInfoService userInfoService;

    @GetMapping("/list/{imageId}")
    public AjaxResult list(@PathVariable String imageId) {
        List<Comment> list = commentService.lambdaQuery()
                .eq(Comment::getImageId, imageId)
                .orderByDesc(Comment::getCreateTime)
                .list();
        return AjaxResult.success(list);
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody Comment comment) {
        if (!StpUtil.isLogin()) {
            return AjaxResult.error("请先登录");
        }
        String userId = StpUtil.getLoginIdAsString();
        comment.setUserId(userId);
        
        // Query user_name from userInfo table by user_id
        UserInfo userInfo = userInfoService.selectUserInfoById(userId);
        if (userInfo != null) {
            comment.setUserName(userInfo.getUserName());
        } else {
            comment.setUserName("Unknown");
        }
        
        commentService.save(comment);
        return AjaxResult.success();
    }
    
    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable String id) {
        if (!StpUtil.isLogin()) {
            return AjaxResult.error("请先登录");
        }
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return AjaxResult.error("评论不存在");
        }
        // Only allow author or admin to delete
        String currentUserId = StpUtil.getLoginIdAsString();
        if (!comment.getUserId().equals(currentUserId) && !StpUtil.hasRole("admin")) {
            return AjaxResult.error("无权删除");
        }
        
        commentService.removeById(id);
        return AjaxResult.success();
    }
}
