package com.sjy.imagechain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjy.imagechain.domain.NftTransaction;
import com.sjy.imagechain.domain.vo.TransactionStatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface NftTransactionMapper extends BaseMapper<NftTransaction> {

    @Select("SELECT TO_CHAR(create_time, '${format}') as date, COUNT(*) as count, COALESCE(SUM(price), 0) as volume " +
            "FROM nft_transaction " +
            "WHERE create_time >= #{startTime} " +
            "GROUP BY TO_CHAR(create_time, '${format}') " +
            "ORDER BY date ASC")
    List<TransactionStatsVO> selectStats(@Param("startTime") LocalDateTime startTime, @Param("format") String format);

    @Select("SELECT COUNT(*) FROM nft_transaction")
    Long countAll();

    /**
     * 查询用户的交易历史 (作为买家或卖家)
     *
     * @param userAddress 用户地址
     * @param type        交易类型 (可选)
     * @return 交易列表
     */
    default List<NftTransaction> selectUserTransactions(String userAddress, String type) {
        LambdaQueryWrapper<NftTransaction> wrapper = new LambdaQueryWrapper<>();

        // 筛选: 发送方是我 OR 接收方是我
        wrapper.and(w -> w
                .eq(NftTransaction::getFromAddress, userAddress)
                .or()
                .eq(NftTransaction::getToAddress, userAddress)
        );

        // 如果有类型筛选 (例如只看 mint, buy)
        // if (StringUtils.hasText(type)) {
        //     wrapper.eq(NftTransaction::getType, type);
        // }

        // 按时间倒序
        wrapper.orderByDesc(NftTransaction::getCreateTime);

        return selectList(wrapper);
    }

    /**
     * 统计用户的交易总数
     */
    default Long selectUserTransactionsCount(String userAddress, String type) {
        LambdaQueryWrapper<NftTransaction> wrapper = new LambdaQueryWrapper<>();

        wrapper.and(w -> w
                .eq(NftTransaction::getFromAddress, userAddress)
                .or()
                .eq(NftTransaction::getToAddress, userAddress)
        );

        return selectCount(wrapper);
    }
}