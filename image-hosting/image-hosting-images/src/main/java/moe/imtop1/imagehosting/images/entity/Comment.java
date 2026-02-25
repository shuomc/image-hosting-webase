package moe.imtop1.imagehosting.images.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.imtop1.imagehosting.framework.base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("comments")
public class Comment extends BaseEntity {
    @TableId(value = "comment_id", type = IdType.ASSIGN_ID)
    private String commentId;

    private String imageId;

    private String userId;

    private String userName;

    private String content;
}
