package com.sjy.imagechain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjy.imagechain.domain.AppRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper // Mybatis-Plus Mapper 注解
public interface AppRoleMapper extends BaseMapper<AppRole> {

    /**
     * 根据角色名称查询角色信息
     * @param roleName 角色名称 (e.g., 'admin', 'user')
     * @return 匹配的角色实体 AppRole, 如果不存在则返回 null
     */
    @Select("SELECT role_id, role_name, description FROM app_role WHERE role_name = #{roleName}")
    AppRole selectByRoleName(String roleName);
}