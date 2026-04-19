package moe.imtop1.imagehosting.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.imtop1.imagehosting.system.domain.Comment;
import moe.imtop1.imagehosting.system.mapper.AdminCommentMapper;
import moe.imtop1.imagehosting.system.service.IAdminCommentService;
import org.springframework.stereotype.Service;

/**
 * 评论管理Service业务实现
 */
@Service
public class AdminCommentServiceImpl extends ServiceImpl<AdminCommentMapper, Comment> implements IAdminCommentService {
}
