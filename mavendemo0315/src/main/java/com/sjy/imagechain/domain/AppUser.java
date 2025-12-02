package com.sjy.imagechain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjy.imagechain.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "app_user", autoResultMap = true) // autoResultMap=true 可以帮助处理 TypeHandler 等
@Schema(description = "应用用户表")
public class AppUser extends BaseEntity { // 继承 BaseEntity 获取通用字段

    @Schema(description = "用户ID")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private String userId;

    @Schema(description = "用户名")
    @TableField("user_name")
    private String username;

    @Schema(description = "密码哈希")
    @TableField("password_hash")
    private String passwordHash;

    @Schema(description = "用户邮箱")
    @TableField("user_email")
    private String userEmail;

    @Schema(description = "区块链钱包地址")
    @TableField("blockchain_address")
    private String blockchainAddress;

    @Schema(description = "Web3登录随机Nonce")
    @TableField("nonce")
    private String nonce;

    @Schema(description = "加密的私钥 (托管模式使用)")
    @TableField("encrypted_private_key")
    private String encryptedPrivateKey;

    @Schema(description = "角色ID")
    @TableField("role_id")
    private Integer roleId;

    @Schema(description = "头像URL")
    @TableField("avatar_url")
    private String avatarUrl;

    @Schema(description = "个人简介")
    @TableField("bio")
    private String bio;


    @Override
    public String toString() {
        return "AppUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", blockchainAddress='" + blockchainAddress + '\'' +
                ", roleId=" + roleId +
                ", encryptedPrivateKey='" + encryptedPrivateKey + '\'' +
                '}';
    }
}
