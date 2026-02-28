package moe.imtop1.imagehosting.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.system.domain.Roles;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import moe.imtop1.imagehosting.system.domain.dto.AdminRoleCreateDTO;
import moe.imtop1.imagehosting.system.mapper.RolesMapper;
import moe.imtop1.imagehosting.system.service.IRolesService;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色业务层实现
 */
@Service
@RequiredArgsConstructor
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements IRolesService {

    private final RolesMapper rolesMapper;
    private final IUserInfoService userInfoService;

    @Override
    public List<Roles> selectRolesList() {
        return rolesMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public boolean createRole(AdminRoleCreateDTO create) {
        // 检查角色名称是否已存在
        long count = this.count(Wrappers.<Roles>lambdaQuery().eq(Roles::getRolesName, create.getRolesName()));
        if (count > 0) {
            throw new RuntimeException("角色码 [" + create.getRolesName() + "] 已存在");
        }
        
        Roles role = new Roles();
        role.setRolesName(create.getRolesName());
        // 尝试映射 DTO 的 description 到 Roles
        role.setDescription(create.getDescription());
        return this.save(role);
    }

    @Override
    public boolean deleteRole(Integer rolesId) {
        Roles role = this.getById(rolesId);
        if (role == null) {
            return false;
        }

        // 检查角色是否正在被使用
        if (isRoleUsed(role.getRolesName())) {
            throw new RuntimeException("角色 [" + role.getRolesName() + "] 正在被用户使用，无法删除");
        }

        // 禁止删除核心系统角色 (admin)
        if ("admin".equalsIgnoreCase(role.getRolesName())) {
            throw new RuntimeException("禁止删除核心管理员角色");
        }

        return this.removeById(rolesId);
    }

    @Override
    public boolean isRoleUsed(String rolesName) {
        // 这里的 IUserInfoService 需要提供 countByRole
        long count = userInfoService.count(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserRole, rolesName));
        return count > 0;
    }
}
