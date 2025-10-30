package com.edu.common.pojo;

import com.edu.common.enums.ResCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;

@Data
public class ResResult<T> implements Serializable {

    private String code;

    private String msg;

    private T data;

    private Integer total;

    public ResResult() {
        this.code = ResCodeEnum.SUCCESS.getCode();
        this.msg = ResCodeEnum.SUCCESS.getMsg();
        this.total = -1;
    }

    /**
     * 计算集合、数组、普通对象（非集合、非数组）的元素个数
     *
     * @return ResResult
     */
    public ResResult<?> resetTotal() {
        if (this.data == null) {
            // data 为 null，total = -1
            this.total = -1;
        } else if (this.data instanceof Collection<?> collection) {
            // 是集合
            this.total = collection.isEmpty() ? 0 : collection.size();
        } else if (this.data.getClass().isArray()) {
            // 是数组（可选支持）
            this.total = Array.getLength(this.data) == 0 ? 0 : Array.getLength(this.data);
        } else {
            // 是普通对象（非集合、非数组）
            this.total = 0;
        }
        return this;
    }

    public ResResult(String code, T data, Integer total) {
        this.code = code;
        this.data = data;
        this.total = total;
    }

    public ResResult(String code, String msg, T data, Integer total) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.total = total;
    }

    public ResResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResResult errorResult(String code, String msg) {
        ResResult result = new ResResult();
        return result.error(code, msg);
    }

    public static ResResult okResult() {
        ResResult result = new ResResult();
        return result;
    }

    public static ResResult okResult(String code, String msg) {
        ResResult result = new ResResult();
        return result.ok(code, null, msg, -1);
    }

    public static ResResult okResult(Object data, Integer total) {
        ResResult result = setAppHttpCodeEnum(ResCodeEnum.SUCCESS, ResCodeEnum.SUCCESS.getMsg());
        if (data != null) {
            result.setData(data);
        }
        result.setTotal(total);
        return result;
    }

    public static ResResult errorResult(ResCodeEnum enums) {
        return setAppHttpCodeEnum(enums, enums.getMsg());
    }

    public static ResResult errorResult(ResCodeEnum enums, String msg) {
        return setAppHttpCodeEnum(enums, msg);
    }

    public static ResResult setAppHttpCodeEnum(ResCodeEnum enums) {
        return okResult(enums.getCode(), enums.getMsg());
    }

    private static ResResult setAppHttpCodeEnum(ResCodeEnum enums, String msg) {
        return okResult(enums.getCode(), msg);
    }

    public ResResult<?> error(String code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public ResResult<?> ok(String code, T data, Integer total) {
        this.code = code;
        this.data = data;
        this.total = total;
        return this;
    }

    public ResResult<?> ok(String code, T data, String msg, Integer total) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.total = total;
        return this;
    }

    public ResResult<?> ok(T data, Integer total) {
        this.data = data;
        this.total = total;
        return this;
    }

}
