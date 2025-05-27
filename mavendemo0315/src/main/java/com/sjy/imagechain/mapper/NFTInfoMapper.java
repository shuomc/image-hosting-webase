package com.sjy.imagechain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sjy.imagechain.domain.NFTInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List; // Added import for List

@Mapper
public interface NFTInfoMapper extends BaseMapper<NFTInfo> {

    /**
     * Selects a list of NFTs based on a query, returning all matching records.
     * Pagination should be handled externally if needed.
     *
     * @param query The search query for description or tokenId.
     * @return A list of NFTInfo objects matching the criteria.
     */
    default List<NFTInfo> selectNFTList(String query) { // Modified: Returns List<NFTInfo>, removed pageNum, pageSize
        LambdaQueryWrapper<NFTInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NFTInfo::getIsDelete, false)
                .eq(NFTInfo::getIsForSale, true)
                .and(query != null && !query.isEmpty(), w -> w
                        .like(NFTInfo::getDescription, query)
                        .or()
                        .like(NFTInfo::getTokenId, query)
                )
                .orderByDesc(NFTInfo::getCreateTime);

        // Modified: Use selectList instead of selectPage
        return selectList(wrapper);
    }

    /**
     * Selects the count of NFTs based on a query.
     * This method remains unchanged as it already returns a Long count.
     *
     * @param query The search query for description or tokenId.
     * @return The total count of NFTInfo objects matching the criteria.
     */
    default Long selectNFTListCount(String query) {
        LambdaQueryWrapper<NFTInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NFTInfo::getIsDelete, false)
                .eq(NFTInfo::getIsForSale, true)
                .and(query != null && !query.isEmpty(), w -> w
                        .like(NFTInfo::getDescription, query)
                        .or()
                        .like(NFTInfo::getTokenId, query)
                );

        return selectCount(wrapper);
    }

    /**
     * Selects a list of NFTs owned by a specific user, returning all matching records.
     * Pagination should be handled externally if needed.
     *
     * @param ownerId The ID of the owner.
     * @return A list of NFTInfo objects owned by the specified user.
     */
    default List<NFTInfo> selectMyNFTs(String ownerId) { // Modified: Returns List<NFTInfo>, removed pageNum, pageSize
        LambdaQueryWrapper<NFTInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NFTInfo::getIsDelete, false)
                .eq(NFTInfo::getBlockchainAddress, ownerId)
                .orderByDesc(NFTInfo::getCreateTime);

        // Modified: Use selectList instead of selectPage
        return selectList(wrapper);
    }

    /**
     * Selects the count of NFTs owned by a specific user.
     * This method remains unchanged as it already returns a Long count.
     *
     * @param ownerId The ID of the owner.
     * @return The total count of NFTInfo objects owned by the specified user.
     */
    default Long selectMyNFTsCount(String ownerId) {
        LambdaQueryWrapper<NFTInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NFTInfo::getIsDelete, false)
                .eq(NFTInfo::getBlockchainAddress, ownerId);

        return selectCount(wrapper);
    }
}