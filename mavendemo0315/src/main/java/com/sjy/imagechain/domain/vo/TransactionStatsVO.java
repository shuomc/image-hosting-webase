package com.sjy.imagechain.domain.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionStatsVO {
    private String date;
    private Long count;
    private BigDecimal volume;
}
