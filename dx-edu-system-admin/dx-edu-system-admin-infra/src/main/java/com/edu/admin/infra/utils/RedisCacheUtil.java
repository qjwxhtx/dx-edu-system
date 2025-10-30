package com.edu.admin.infra.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(RedisCacheUtil.class);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     */
    public boolean set(String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, value);
            result = true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            // TODO  比较耗内存 e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     */
    public boolean setObj(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            if (expireTime == null) {
                expireTime = 1L;
            }
            if (timeUnit == null) {
                timeUnit = TimeUnit.DAYS;
            }

            ValueOperations<Serializable, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, value, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return result;
    }

    /**
     * 写入缓存 并设置失效时间
     *
     * @param key
     * @param value
     * @param expireTime
     */
    public boolean set(String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, value, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return result;
    }

    /**
     * 写入缓存 并设置失效时间，默认存活2天
     *
     * @param key
     * @param value
     * @param expireTime
     */
    public boolean set(String key, Object value, Long expireTime, TimeUnit unit) {
        boolean result = false;
        try {
            if (expireTime == null) {
                expireTime = 2L;
            }
            if (unit == null) {
                unit = TimeUnit.DAYS;
            }
            ValueOperations<Serializable, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, value, expireTime, unit);
            result = true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return result;
    }

    /**
     * 获取缓存数据
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取自增id,不存在返回1
     *
     * @param key
     * @return
     */
    public Long getAutoSeq(String key, long delta) {
        //查询key是否存在，不存在返回1，存在自动加1
        Long seq = redisTemplate.opsForValue().increment(key, delta);
        //设置key过期时间为一天，不能保证是0点超时
        if (seq == 1) {
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
        }
        return seq;
    }

    /**
     * 写入List缓存
     *
     * @param key
     * @param listData
     */
    public void set(String key, List<Object> listData) {
        ListOperations<Serializable, Object> listOperations = redisTemplate.opsForList();
        if (null != listData) {
            int size = listData.size();
            for (int i = 0; i < size; i++) {
                listOperations.rightPush(key, listData.get(i));
            }
        }
    }

    /**
     * 获取List缓存
     *
     * @param key
     * @return
     */
    public List<Object> getList(String key) {
        List<Object> listData = new ArrayList<Object>();
        ListOperations listOperations = redisTemplate.opsForList();
        Long size = listOperations.size(key);
        for (int i = 0; i < size; i++) {
            listData.add(listOperations.leftPop(key));
        }
        return listData;
    }

    /**
     * 写入Map缓存
     *
     * @param key
     * @param mapData
     */
    public void set(String key, Map<String, Object> mapData) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != mapData) {
            for (Map.Entry entry : mapData.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 写入Map缓存，设置过期时间
     *
     * @param key
     * @param mapData
     */
    public void setHashMapObj(String key, Map<String, Object> mapData, Long expireTime, TimeUnit timeUnit) {
        HashOperations hashOperations = redisTemplate.opsForHash();

        if (expireTime == null) {
            expireTime = 1L;
        }
        if (timeUnit == null) {
            timeUnit = TimeUnit.DAYS;
        }

        if (null != mapData) {
            for (Map.Entry entry : mapData.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }

        LOG.info("设置Redis的Hash类型过期时间， key: {}, 过期时间： {}", key, expireTime);
        // 设置过期时间
        hashOperations.getOperations().expire(key, expireTime, timeUnit);
    }

    /**
     * 删除Map中指定field
     */
    public void delMField(String key, String field) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(field)) {
            hashOperations.delete(key, field);
        }
    }

    /**
     * 获取Map缓存
     *
     * @param key
     * @return
     */
    public Map<String, Object> getMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取MapValue缓存
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object getMapValue(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 写入Set缓存
     *
     * @param key
     * @param setData
     */
    public void set(String key, Set<Object> setData) {
        BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(key);
        if (null != setData) {
            Iterator iterator = setData.iterator();
            while (iterator.hasNext()) {
                boundSetOperations.add(iterator.next());
            }
        }
    }

    /**
     * 获取Set缓存
     *
     * @param key
     * @return
     */
    public Set<Object> getSet(String key) {
        Set<Object> setData = new HashSet<Object>();
        BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(key);
        Long size = boundSetOperations.size();
        for (int i = 0; i < size; i++) {
            setData.add(boundSetOperations.pop());
        }
        return setData;
    }

    /**
     * 删除value
     *
     * @param key
     */
    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 批量删除 value
     *
     * @param keys
     */
    public void remove(String... keys) {
        for (String key : keys) {
            if (exists(key)) {
                remove(key);
            }
        }
    }

    /**
     * 判断缓存是否存在 值value
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 往指定的队列中，写入数据
     *
     * @param queueName 队列名称
     * @param value     写入的信息
     */
    public void leftPub(Object queueName, Object value) {
        Long success = redisTemplate.opsForList().leftPush(queueName, value);

    }

    /**
     * 从指定的队列中，读取数据
     *
     * @param queueName 队列名称
     * @return
     */
    public String rightPop(String queueName) {
        Object object = redisTemplate.opsForList().rightPop(queueName);

        return object == null ? null : object.toString();
    }

    /**
     * 从指定队列制定位置获取数据
     *
     * @param queueName
     * @param index
     * @return
     */
    public String index(String queueName, Long index) {
        Object object = redisTemplate.opsForList().index(queueName, index);

        return object == null ? null : object.toString();
    }

    /**
     * 删除指定的队列中的数据
     *
     * @param queueName 队列名称
     * @return
     */
    public void deleteQueue(String queueName) {
        redisTemplate.opsForList().trim(queueName, 1, 0);
    }

    /**
     * 从指定的队列中，读取数据
     *
     * @param queueName 队列名称
     * @return
     */
    public Object rightPopByObj(String queueName) {
        return redisTemplate.opsForList().rightPop(queueName);
    }

    /**
     * 设定redis锁
     *
     * @param lockKey    key
     * @param requestId  标志，可使用uuid
     * @param expireTime 自动释放时间
     * @return
     */
    public boolean tryGetLock(String lockKey, String requestId, int expireTime) {
        return redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 删除redis锁
     *
     * @param lockKey
     * @return
     */
    public boolean delete(String lockKey) {
        Long result = redisTemplate.delete(Collections.singletonList(lockKey));
        if (result != null && result.intValue() == 1) {
            return true;
        }

        return false;
    }

    /**
     * 释放分布式锁的lua脚本
     */
    private final static String UN_LOCK_SCRIPT =
        "if redis.call ('get', KEYS[1]) == ARGV[1]  then  return redis.call ('del', KEYS[1]) else return 0 end";

    /**
     * 释放分布式锁
     *
     * @param key      key
     * @param identity 身份标识
     * @return
     */
    public boolean releaseLock(String key, String identity) {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(UN_LOCK_SCRIPT);
        redisScript.setResultType(Boolean.class);
        List<String> keys = new ArrayList<>();
        keys.add(key);
        Boolean result = (Boolean) redisTemplate.execute(redisScript, keys, identity);
        return result;
    }

    /**
     * 位图操作
     *
     * @param key
     * @param index
     * @param tag
     * @return
     */
    public boolean setBits(String key, long index, boolean tag) {
        return ((Boolean) redisTemplate.execute(
            (RedisCallback<Boolean>) conn -> conn.setBit(key.getBytes(), index, tag)));
    }

    public Boolean getBit(String key, long index) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) conn -> conn.getBit(key.getBytes(), index));
    }

    /**
     * 获取自增id
     *
     * @param key
     * @return
     */
    public Long getBarCode(String key) {
        //查询key是否存在，不存在返回1，存在自动加1
        Long seq = redisTemplate.opsForValue().increment(key, 1);
        //设置key过期时间为一天，保证每天的流水从1开始
        return seq;
    }

    /**
     * 获取缓存数据
     *
     * @param key
     * @return String
     */
    public String getString(String key) {
        String result = null;

        // 从redis获取数据
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj != null) {
            result = String.valueOf(obj);
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime 超时时间（s）
     */
    public boolean setString(String key, String value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, value, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return result;
    }

    /**
     * 查询队列的长度
     *
     * @param queueName
     * @return
     */
    public Long getQueueLength(String queueName) {
        return redisTemplate.opsForList().size(queueName);
    }

    /**
     * 添加有序集合数据
     *
     * @param queueName
     * @param value
     * @param score
     * @return
     */
    public Boolean zetAdd(String queueName, Object value, double score) {
        return redisTemplate.opsForZSet().add(queueName, value, score);
    }

    /**
     * 取数据 ,按分数从低到高排序
     *
     * @param queueName
     * @param start
     * @param end
     * @return
     */
    public Set zetRange(String queueName, long start, long end) {
        return redisTemplate.opsForZSet().range(queueName, start, end);
    }

    /**
     * 取数据 ,按分数从高到底排序
     *
     * @param queueName
     * @param start
     * @param end
     * @return
     */
    public Set zetReverseRange(String queueName, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(queueName, start, end);
    }

    /**
     * 获取有序集合中成员数量
     *
     * @param queueName
     * @return
     */
    public Long zetCard(String queueName) {
        return redisTemplate.opsForZSet().zCard(queueName);
    }

    /**
     * 获取指定范围内的成员数量
     *
     * @param queueName
     * @param start
     * @param end
     * @return
     */
    public Long zetCount(String queueName, double start, double end) {
        return redisTemplate.opsForZSet().count(queueName, start, end);
    }

    /**
     * 获取有序集合中指定成员的分数
     *
     * @param queueName
     * @return
     */
    public Double zetScore(String queueName, Object value) {
        return redisTemplate.opsForZSet().score(queueName, value);
    }

    /**
     * 移除指定成员
     *
     * @param queueName
     * @param value
     * @return
     */
    public Long zetRemove(String queueName, Object... value) {
        return redisTemplate.opsForZSet().remove(queueName, value);
    }

    /**
     * 设置自增长序号，并指定过期过期时间 lua脚本
     */
    private final static String SET_AUTO_SEQ_SCRIPT =
        "local expir_ttl = tonumber(ARGV[1]) if expir_ttl == nil then expir_ttl = -1 end  local res_value = redis.call('INCR',KEYS[1]) if expir_ttl == -1 then redis.call('PEXPIRE', KEYS[1], 86400 * 1000) else redis.call('PEXPIRE', KEYS[1], expir_ttl * 1000) end return res_value ";

    /**
     * 获取自增值，默认key过期时间为24小时
     *
     * @param key 键
     * @param ttl key过期时间,传-1默认为24小时过期； 传入其他值每次动态更新key过期时间。
     * @return 返回当前value值
     */
    public long getAutoSeqWithDefaultTTL(String key, int ttl) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(SET_AUTO_SEQ_SCRIPT);
        redisScript.setResultType(Long.class);
        List<String> keys = new ArrayList<>();
        keys.add(key);
        //将ttl转换为字符串
        Object executeRes = redisTemplate.execute(redisScript, keys, ttl);
        return (long) executeRes;
    }

    /**
     * 查询Key的类型
     *
     * @param key
     * @return
     */
    public DataType type(String key) {
        if (StringUtils.isNotEmpty(key)) {
            return redisTemplate.type(key);
        }
        return null;
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        if (StringUtils.isNotEmpty(key)) {
            return redisTemplate.getExpire(key, TimeUnit.HOURS);
        }

        return -1L;
    }

    /**
     * 查询队列长度
     *
     * @param key
     * @return
     */
    public Long getListSize(String key) {
        if (StringUtils.isNotEmpty(key)) {
            return redisTemplate.opsForList().size(key);
        }

        return -1L;
    }

    /**
     * 获取Hash中的所有值
     *
     * @param key
     * @return
     */
    public List<Object> getAllHashValue(String key) {
        return redisTemplate.opsForHash().values(key);
    }
}
