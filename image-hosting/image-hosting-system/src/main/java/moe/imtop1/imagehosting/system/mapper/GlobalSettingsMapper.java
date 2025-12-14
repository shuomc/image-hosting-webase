package moe.imtop1.imagehosting.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.github.yulichang.base.MPJBaseMapper;
import moe.imtop1.imagehosting.system.domain.Config;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@InterceptorIgnore(illegalSql = "true")
public interface GlobalSettingsMapper extends MPJBaseMapper<Config> {

}
