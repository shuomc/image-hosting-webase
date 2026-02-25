package moe.imtop1.imagehosting.images.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.imtop1.imagehosting.images.entity.Comment;
import moe.imtop1.imagehosting.images.mapper.CommentMapper;
import moe.imtop1.imagehosting.images.service.ICommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
}
