package moe.imtop1.imagehosting.images.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.imtop1.imagehosting.framework.base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("downloads")
public class Download extends BaseEntity {
    @TableId(value = "download_id", type = IdType.ASSIGN_ID)
    private String downloadId;

    private String userId;

    private String imageId;
}
