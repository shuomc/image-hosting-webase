package com.sjy.imagechain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "应用角色表")
public class AppRole {
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    @Schema(description = "角色名称 (admin, user)")
    private String roleName;

    @Schema(description = "角色描述")
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