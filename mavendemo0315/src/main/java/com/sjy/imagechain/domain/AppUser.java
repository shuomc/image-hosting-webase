package com.sjy.imagechain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjy.imagechain.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@Getter
@TableName(value = "app_user", autoResultMap = true) // autoResultMap=true 可以帮助处理 TypeHandler 等
public class AppUser extends BaseEntity { // 继承 BaseEntity 获取通用字段

    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private String userId; // Traditional application user ID (Primary Key)

    @TableField("user_name")
    private String username; // Username

    @TableField("password_hash") // Assuming password field in DB is now password_hash
    private String passwordHash; // Hashed password

    // @TableField(typeHandler = EncryptTypeHandler.class) // 如果需要对邮箱字段加密，保留此注解
    @TableField("user_email")
    private String userEmail; // User email

    @TableField("blockchain_address")
    private String blockchainAddress; // !!! 新增字段: 用户对应的区块链地址 !!!
    // Used to link user to on-chain ownership

    @TableField("role_id")
    private Integer roleId; // !!! 新增字段: 关联到角色表ID !!!
    // Note: In Mybatis-Plus, you usually store FK value directly.

    @TableField("encrypted_private_key")
    private String encryptedPrivateKey;

    // create_time, update_time, is_delete 继承自 BaseEntity，不再此处定义


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
                // 注意：此处省略了 BaseEntity 的字段，如果需要可以在 BaseEntity 的 toString 中处理
                '}';
    }
}
