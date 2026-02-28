package moe.imtop1.imagehosting.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import moe.imtop1.imagehosting.framework.base.BaseEntity;

/**角色表
 * @author shuomc
 */
@TableName("roles")
public class Roles extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Integer rolesId;

    private String rolesName;
    private String description;

    public Roles() {}

    public Roles(Integer rolesId, String rolesName, String description) {
        this.rolesId = rolesId;
        this.rolesName = rolesName;
        this.description = description;
    }

    public Integer getRolesId() {
        return rolesId;
    }

    public void setRolesId(Integer rolesId) {
        this.rolesId = rolesId;
    }

    public String getRolesName() {
        return rolesName;
    }

    public void setRolesName(String rolesName) {
        this.rolesName = rolesName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}