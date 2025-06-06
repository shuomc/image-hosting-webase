package moe.imtop1.imagehosting.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.imtop1.imagehosting.framework.base.BaseEntity;
import org.apache.ibatis.annotations.Mapper;
import moe.imtop1.imagehosting.system.entity.NFTInfo;

@Mapper // 或使用 @Repository
public interface NftInfoMapper extends BaseMapper<NFTInfo>{
    // BaseMapper 已经提供了大部分CRUD操作，例如 selectById
}
