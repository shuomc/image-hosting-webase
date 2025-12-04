package moe.imtop1.imagehosting.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import moe.imtop1.imagehosting.framework.base.BaseEntity;
import moe.imtop1.imagehosting.framework.handler.EncryptTypeHandler;

import java.time.LocalDateTime;

/**
 * 用户表
 * @author shuomc
 */
@Data // 自动生成 Getter, Setter, toString, equals, hashCode
@NoArgsConstructor // 无参构造器
@AllArgsConstructor // 全参构造器
@EqualsAndHashCode(callSuper = true) // 让 equals/hashCode 包含父类 BaseEntity 的字段
@TableName(value = "user_info", autoResultMap = true)
public class UserInfo extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String userId;

    private String userName;

    private String password;

    @TableField(typeHandler = EncryptTypeHandler.class)
    private String userEmail;

    private String userRole;

    /**
     * 区块链钱包地址
     */
    private String blockchainAddress;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像链接
     */
    private String avatarUrl;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 个人网站
     */
    private String websiteUrl;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 账号状态: 1-正常, 0-禁用
     */
    private Integer status;

    /**
     * 存储空间上限 (Byte)
     */
    private Long storageLimit;

    /**
     * 已使用存储空间 (Byte)
     */
    private Long storageUsed;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
}