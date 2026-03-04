package moe.imtop1.imagehosting.images.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.imtop1.imagehosting.images.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
