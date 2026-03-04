package moe.imtop1.imagehosting.nft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.imtop1.imagehosting.nft.domain.NFTInfo;
import org.apache.ibatis.annotations.Mapper;


@Mapper // 或使用 @Repository
public interface NftInfoMapper extends BaseMapper<NFTInfo>{
    // BaseMapper 已经提供了大部分CRUD操作，例如 selectById
}
