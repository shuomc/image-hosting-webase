package moe.imtop1.imagehosting.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.imtop1.imagehosting.system.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论管理Mapper接口
 */
@Mapper
public interface AdminCommentMapper extends BaseMapper<Comment> {
}
