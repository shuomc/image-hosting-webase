package com.sjy.imagechain.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页数据载体
 * Service层返回此对象，Controller层将其放入 AjaxResult.success(data)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageData<T> {
    private List<T> list;   // 数据列表
    private Long total;     // 总条数
}
