package com.sjy.imagechain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */

@Setter
@Getter
@TableName(value = "app_role")
public class AppRole {
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    @TableField("role_name")
    private String roleName; // Role name (e.g., 'admin', 'user')

    private String description;

    public AppRole() {
    }

    public AppRole(Integer roleId, String roleName, String description) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
    }

    @Override
    public String toString() {
        return "AppRole{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}