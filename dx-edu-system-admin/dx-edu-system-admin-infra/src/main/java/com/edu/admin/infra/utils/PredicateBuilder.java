package com.edu.admin.infra.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Predicate条件构造器
 *
 * @author guNingBo
 * @since 2025-03-13
 */
public class PredicateBuilder {

    private CriteriaBuilder cb;

    private Root<?> root;

    private List<Predicate> predicates;

    private PredicateBuilder() {
    }

    /**
     * 创建PredicateBuilder
     *
     * @param root    根对象
     * @param builder 条件构造器
     * @return PredicateBuilder
     */
    public static PredicateBuilder builder(Root<?> root, CriteriaBuilder builder) {
        PredicateBuilder builderUtil = new PredicateBuilder();
        builderUtil.root = root;
        builderUtil.cb = builder;
        builderUtil.predicates = new ArrayList<>();
        return builderUtil;
    }

    /**
     * 添加条件
     *
     * @param predicate 条件
     * @return this
     */
    public PredicateBuilder and(Predicate predicate) {
        if (Objects.nonNull(predicate)) {
            predicates.add(predicate);
        }
        return this;
    }

    /**
     * equal
     *
     * @param fileName 字段名称
     * @param value    值
     * @return this
     */
    public PredicateBuilder equal(String fileName, Object value) {
        predicates.add(cb.equal(root.get(fileName), value));
        return this;
    }

    /**
     * equalNotEmpty
     *
     * @param fileName 字段名称
     * @param value    值
     * @return this
     */
    public PredicateBuilder equalNotEmpty(String fileName, Object value) {
        if (ObjectUtils.isNotEmpty(value)) {
            predicates.add(cb.equal(root.get(fileName), value));
        }
        return this;
    }

    /**
     * in
     *
     * @param fileName   字段名称
     * @param collection 集合
     * @return this
     */
    public PredicateBuilder inNotEmpty(String fileName, Collection<?> collection) {
        if (ObjectUtils.isNotEmpty(collection)) {
            predicates.add(cb.in(root.get(fileName)).value(collection));
        }
        return this;
    }

    /**
     * in(不判空版)
     *
     * @param fileName   字段名称
     * @param collection 集合
     * @return this
     */
    public PredicateBuilder in(String fileName, Collection<?> collection) {
        predicates.add(cb.in(root.get(fileName)).value(collection));
        return this;
    }

    /**
     * 大于
     *
     * @param fileName 字段名称
     * @param value    值
     * @return this
     */
    public PredicateBuilder greaterThan(String fileName, Number value) {
        if (ObjectUtils.isNotEmpty(value)) {
            predicates.add(cb.gt(root.get(fileName), value));
        }
        return this;
    }

    /**
     * 大于
     *
     * @param fileName  字段名称
     * @param condition 条件
     * @param value     值
     * @return this
     */
    public PredicateBuilder greaterThan(String fileName, Boolean condition, Number value) {
        if (Boolean.TRUE.equals(condition)) {
            predicates.add(cb.gt(root.get(fileName), value));
        }
        return this;
    }

    /**
     * 大于等于
     *
     * @param fileName 字段名称
     * @param value    值
     * @return this
     */
    public PredicateBuilder greaterThanOrEqualTo(String fileName, Number value) {
        if (ObjectUtils.isNotEmpty(value)) {
            predicates.add(cb.ge(root.get(fileName), value));
        }
        return this;
    }

    /**
     * 大于等于
     *
     * @param fileName 字段名称
     * @param time     值
     * @return this
     */
    public PredicateBuilder greaterThanOrEqualTo(String fileName, LocalDateTime time) {
        if (ObjectUtils.isNotEmpty(time)) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(fileName), time));
        }
        return this;
    }

    /**
     * 小于
     *
     * @param fileName 字段名称
     * @param value    值
     * @return this
     */
    public PredicateBuilder lessThan(String fileName, Number value) {
        if (ObjectUtils.isNotEmpty(value)) {
            predicates.add(cb.lt(root.get(fileName), value));
        }
        return this;
    }

    /**
     * 小于等于
     *
     * @param fileName 字段名称
     * @param value    值
     * @return this
     */
    public PredicateBuilder lessThanOrEqualTo(String fileName, Number value) {
        if (ObjectUtils.isNotEmpty(value)) {
            predicates.add(cb.le(root.get(fileName), value));
        }
        return this;
    }

    /**
     * 小于等于
     *
     * @param fileName 字段名称
     * @param value    值
     * @return this
     */
    public PredicateBuilder lessThanOrEqualTo(String fileName, LocalDateTime value) {
        if (ObjectUtils.isNotEmpty(value)) {
            predicates.add(cb.lessThanOrEqualTo(root.get(fileName), value));
        }
        return this;
    }

    /**
     * 双边模糊查询
     *
     * @param fileName 字段名称
     * @param value    值
     * @return this
     */
    public PredicateBuilder likeBilateral(String fileName, String value) {
        if (ObjectUtils.isNotEmpty(value)) {
            predicates.add(cb.like(root.get(fileName), "%" + value + "%"));
        }
        return this;
    }

    /**
     * 右边模糊查询
     *
     * @param fileName 字段名称
     * @param value    值
     * @return this
     */
    public PredicateBuilder likeRight(String fileName, String value) {
        if (ObjectUtils.isNotEmpty(value)) {
            predicates.add(cb.like(root.get(fileName), value + "%"));
        }
        return this;
    }

    /**
     * isNull
     *
     * @param fileName 字段名称
     * @return this
     */
    public PredicateBuilder isNull(String fileName) {
        predicates.add(cb.isNull(root.get(fileName)));
        return this;
    }

    /**
     * between
     *
     * @param fileName 字段名称
     * @param start    开始值
     * @param end      结束值
     * @param <Y>      class
     * @return this
     */
    public <Y extends Comparable<? super Y>> PredicateBuilder between(String fileName, Y start, Y end) {
        if (ObjectUtils.isNotEmpty(start) && ObjectUtils.isNotEmpty(end)) {
            predicates.add(cb.between(root.get(fileName), start, end));
        }
        return this;
    }

    /**
     * 拼接or语句
     *
     * @param conditions or的条件
     * @return this
     */
    public PredicateBuilder andOr(Predicate... conditions) {
        if (conditions != null && conditions.length > 0) {
            predicates.add(cb.or(conditions));
        }
        return this;
    }

    /**
     * 构造查询条件
     *
     * @return Predicate
     */
    public Predicate build() {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * notEqual条件构建方法
     */
    public PredicateBuilder notEqual(String fieldName, Object value) {
        if (ObjectUtils.isNotEmpty(value)) {
            predicates.add(cb.notEqual(root.get(fieldName), value));
        }
        return this;
    }

    /**
     * notInEqual条件构建方法
     */
    public PredicateBuilder notInEqual(String field, CriteriaBuilder builder, Collection<?> values) {
        if (ObjectUtils.isNotEmpty(values)) {
            predicates.add(builder.not(root.get(field).in(values)));
        }
        return this;
    }

    /**
     * equal
     *
     * @param fileName 字段名称
     * @param value    值
     * @return this
     */
    public PredicateBuilder equalField(String fileName, Object value) {
        predicates.add(cb.equal(root.get(fileName), value));
        return this;
    }

    /**
     * isNotNull
     *
     * @param fileName 字段名称
     * @return this
     */
    public PredicateBuilder isNotNull(String fileName) {
        predicates.add(cb.isNotNull(root.get(fileName)));
        return this;
    }

    /**
     * equalNotEmptyAndIgnoreCase
     *
     * @param fileName
     * @param value
     * @return
     */
    public PredicateBuilder likeNotEmptyAndIgnoreCase(String fileName, String value) {
        if (ObjectUtils.isNotEmpty(value)) {
            predicates.add(cb.like(cb.lower(root.get(fileName)), "%" + value.toLowerCase() + "%"));
        }
        return this;
    }
}
