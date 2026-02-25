package moe.imtop1.imagehosting.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.imtop1.imagehosting.framework.base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("notices")
public class Notice extends BaseEntity {
    @TableId(value = "notice_id", type = IdType.ASSIGN_ID)
    private String noticeId;

    private String title;

    private String content;

    private String publisherId;
}
