package moe.imtop1.imagehosting.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户统计表
 * @author shuomc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_stats")
public class UserStats {

    /**
     * 用户唯一ID (主键)
     */
    @TableId(type = IdType.INPUT)
    private String userId;

    /**
     * 累计上传图片数量
     */
    private Integer totalUploads;

    /**
     * 所有图片的累计浏览量
     */
    private Long totalViews;

    /**
     * 所有图片的累计下载量
     */
    private Long totalDownloads;

    /**
     * 所有图片的累计点赞数
     */
    private Long totalLikes;

    /**
     * 存储配额 (单位: 字节, 默认1GB)
     */
    private Long storageLimit;

    /**
     * 已用存储 (单位: 字节)
     */
    private Long storageUsed;

    /**
     * 统计数据更新时间
     */
    private LocalDateTime updateTime;
}
