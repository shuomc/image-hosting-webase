package com.sjy.imagechain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjy.imagechain.domain.NFTTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NFTTransactionMapper extends BaseMapper<NFTTransaction> {
    
    @Select("SELECT * FROM nft_transaction WHERE is_delete = false AND nft_id = #{nftId} " +
            "ORDER BY create_time DESC LIMIT #{offset}, #{pageSize}")
    List<NFTTransaction> selectNFTTransactions(@Param("nftId") String nftId, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
    
    @Select("SELECT COUNT(*) FROM nft_transaction WHERE is_delete = false AND nft_id = #{nftId}")
    Long selectNFTTransactionsCount(@Param("nftId") String nftId);
} 