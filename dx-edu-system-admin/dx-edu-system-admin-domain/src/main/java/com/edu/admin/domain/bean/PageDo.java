package com.edu.admin.domain.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页基类（泛型）
 *
 * @author qianwj
 * @param <T> 具体的数据类型，如 User、Order、Product 等
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDo<T> {

    // 当前页码，默认第1页
    private Integer pageNum = 1;

    // 每页大小，默认10条
    private Integer pageSize = 10;

    // 总记录数（可选）
    private Long total;

    // 当前页的数据列表
    private List<T> list;


}
