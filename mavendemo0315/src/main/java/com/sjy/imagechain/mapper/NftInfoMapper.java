package com.sjy.imagechain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjy.imagechain.domain.NftInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

@Mapper
public interface NftInfoMapper extends BaseMapper<NftInfo> {

    /**
     * 市场列表查询：查询所有在售的 NFT
     * 包含：搜索(名称/描述)、分类、排序
     *
     * @param query    搜索关键词
     * @param category 分类 (可选)
     * @param sort     排序方式 (price_asc, price_desc, new)
     * @return 符合条件的 NFT 列表
     */
    default List<NftInfo> selectMarketList(String query, String category, String sort) {
        LambdaQueryWrapper<NftInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NftInfo::getIsDelete, false)
                .eq(NftInfo::getIsForSale, true); // 只查在售

        // 动态搜索条件
        wrapper.and(StringUtils.hasText(query), w -> w
                .like(NftInfo::getName, query)
                .or()
                .like(NftInfo::getDescription, query)
                // 如果数据库有 token_id 字段
                .or()
                .like(NftInfo::getTokenId, query)
        );

        // 动态分类
        // if (StringUtils.hasText(category)) {
        //    wrapper.eq(NftInfo::getCategory, category);
        // }

        // 动态排序
        if ("price_asc".equals(sort)) {
            wrapper.orderByAsc(NftInfo::getPrice);
        } else if ("price_desc".equals(sort)) {
            wrapper.orderByDesc(NftInfo::getPrice);
        } else {
            wrapper.orderByDesc(NftInfo::getCreateTime); // 默认最新
        }

        return selectList(wrapper);
    }

    /**
     * 市场列表总数统计 (配合 selectMarketList 使用)
     */
    default Long selectMarketListCount(String query, String category) {
        LambdaQueryWrapper<NftInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NftInfo::getIsDelete, false)
                .eq(NftInfo::getIsForSale, true);

        wrapper.and(StringUtils.hasText(query), w -> w
                .like(NftInfo::getName, query)
                .or()
                .like(NftInfo::getDescription, query)
                .or()
                .like(NftInfo::getTokenId, query)
        );

        return selectCount(wrapper);
    }

    /**
     * 个人资产查询
     *
     * @param userAddress 用户钱包地址
     * @param mode        查询模式: "owned"(我持有的), "created"(我铸造的), "sold"(我卖出的)
     * @return NFT 列表
     */
    default List<NftInfo> selectMyNFTs(String userAddress, String mode) {
        LambdaQueryWrapper<NftInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NftInfo::getIsDelete, false);

        if ("created".equals(mode)) {
            // 我铸造的 (Creator 是我)
            wrapper.eq(NftInfo::getCreatorAddress, userAddress);
        } else if ("sold".equals(mode)) {
            // 我卖出的 (Creator 是我 且 Owner 不是我)
            wrapper.eq(NftInfo::getCreatorAddress, userAddress)
                    .ne(NftInfo::getOwnerAddress, userAddress);
        } else {
            // 默认: 我持有的 (Owner 是我)
            wrapper.eq(NftInfo::getOwnerAddress, userAddress);
        }

        wrapper.orderByDesc(NftInfo::getCreateTime);

        return selectList(wrapper);
    }

    /**
     * 个人资产数量统计
     */
    default Long selectMyNFTsCount(String userAddress, String mode) {
        LambdaQueryWrapper<NftInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NftInfo::getIsDelete, false);

        if ("created".equals(mode)) {
            wrapper.eq(NftInfo::getCreatorAddress, userAddress);
        } else if ("sold".equals(mode)) {
            wrapper.eq(NftInfo::getCreatorAddress, userAddress)
                    .ne(NftInfo::getOwnerAddress, userAddress);
        } else {
            wrapper.eq(NftInfo::getOwnerAddress, userAddress);
        }

        return selectCount(wrapper);
    }
}