package com.sjy.imagechain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjy.imagechain.domain.AppUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */
@Mapper // Mybatis-Plus Mapper 注解
public interface AppUserMapper extends BaseMapper<AppUser> {

    // BaseMapper 提供了基本的 CRUD 方法，如 insert, selectById 等，通常不需要在此定义额外的插入方法。
    // 您可以在需要时根据复杂查询编写自定义方法。
}
