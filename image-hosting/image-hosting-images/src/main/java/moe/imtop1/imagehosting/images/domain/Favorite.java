package moe.imtop1.imagehosting.images.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import moe.imtop1.imagehosting.framework.base.BaseEntity;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("favorites")
public class Favorite extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String favoriteId;
    private String userId;
    private String imageId;
}
