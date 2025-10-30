package com.edu.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页基类（泛型）
 *
 * @author qianwj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageParam {

    // 当前页码，默认第1页
    private Integer pageNum = 1;

    // 每页大小，默认10条
    private Integer pageSize = 10;

}
