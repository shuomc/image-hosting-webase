package moe.imtop1.imagehosting.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import moe.imtop1.imagehosting.system.domain.Roles;
import moe.imtop1.imagehosting.system.domain.dto.AdminRoleCreateDTO;

import java.util.List;

public interface IRolesService extends IService<Roles> {
    
    /**
     * 获取所有角色并进行缓存或处理
     */
    List<Roles> selectRolesList();
    
    /**
     * 创建新角色
     */
    boolean createRole(AdminRoleCreateDTO create);
    
    /**
     * 删除角色
     * @param rolesId 角色ID
     * @return 是否成功，且需满足业务规则（未被使用）
     */
    boolean deleteRole(Integer rolesId);
    
    /**
     * 检查角色是否被用户使用
     */
    boolean isRoleUsed(String rolesName);
}
