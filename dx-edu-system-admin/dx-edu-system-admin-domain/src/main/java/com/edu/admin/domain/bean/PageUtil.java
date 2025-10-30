package com.edu.admin.domain.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 功能描述: <br>
 * 分页返回工具类
 *
 * @since 2025-10-23
 */
@Data
public class PageUtil<T> implements Serializable {
    private static final long serialVersionUID = 8744607362059739632L;
    /**
     * 当前页码
     */
    private Long currentPage;
    /**
     * 总页数
     */
    private Long totalPage;

    /**
     * 当每页大小
     */
    private Long size;

    /**
     * 分页数据
     */
    private List<T> data;

    /**
     * 总条数
     */
    private Long totalItems;

    public static <T> PageUtil<T> of(Integer pageNum, Integer pageSize) {
        PageUtil<T> tPageUtil = new PageUtil<>();
        tPageUtil.setCurrentPage(pageNum.longValue());
        tPageUtil.setTotalPage(0L);
        tPageUtil.setSize(pageSize.longValue());
        tPageUtil.setData(null);
        return tPageUtil;
    }

    public static <T> PageUtil<T> of(List<T> content, Integer pageTotal, Integer pageNum, Integer pageSize) {
        PageUtil<T> tPageUtil = new PageUtil<>();
        tPageUtil.setCurrentPage(pageNum.longValue());
        tPageUtil.setTotalPage(pageTotal.longValue());
        tPageUtil.setSize(pageSize.longValue());
        tPageUtil.setData(content);
        return tPageUtil;
    }

    public static <T> PageUtil<T> of(List<T> content, Integer pageTotal, Integer pageNum, Integer pageSize, List<T> data) {
        PageUtil<T> tPageUtil = new PageUtil<>();
        tPageUtil.setCurrentPage(pageNum.longValue());
        tPageUtil.setTotalPage(pageTotal.longValue());
        tPageUtil.setSize(pageSize.longValue());
        tPageUtil.setData(content);
        int totalItems = data.size();
        tPageUtil.setTotalItems((long) totalItems);
        return tPageUtil;
    }

    public static <T> PageUtil<T> of(List<T> content, Long totalItems, Integer pageNum, Integer pageSize) {
        PageUtil<T> tPageUtil = new PageUtil<>();
        tPageUtil.setCurrentPage(pageNum.longValue());
        if (Objects.isNull(totalItems)) {
            totalItems = 0L;
        }
        long totalPage = (totalItems / (long) pageSize + (long) (totalItems % (long) pageSize == 0L ? 0 : 1));
        tPageUtil.setTotalPage(totalPage);
        tPageUtil.setSize(pageSize.longValue());
        tPageUtil.setData(content);
        tPageUtil.setTotalItems(totalItems);
        return tPageUtil;
    }

}
