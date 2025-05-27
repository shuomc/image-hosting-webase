package com.sjy.imagechain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */

@TableName(value = "app_role")
public class AppRole {
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    // role_name 和 roleName 驼峰匹配，可以省略 @TableField，但为了清晰保留
    @TableField("role_name")
    private String roleName; // Role name (e.g., 'admin', 'user')

    private String description; // Role description (字段名和列名一致，可以省略 @TableField)

    public AppRole() {
    }

    public AppRole(Integer roleId, String roleName, String description) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
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