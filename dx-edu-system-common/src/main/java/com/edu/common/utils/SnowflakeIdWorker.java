package com.edu.common.utils;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

/**
 * @author JZC
 * @date 2020/6/6 9:16
 * @since 2.0
 */
@Slf4j
public class SnowflakeIdWorker {

    // ==============================Fields===========================================

    /**
     * 开始时间截 (2015-01-01)
     */
    private final long twepoch = 1420041600000L;

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = ~(-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDatacenterId = ~(-1L << datacenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = ~(-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private volatile long lastTimestamp = -1L;

    //==============================Constructors=====================================

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public SnowflakeIdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(
                String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId
            << workerIdShift) | sequence;
    }

    /**
     * 根据时间戳获取当时的id
     *
     * @return SnowflakeId
     */
    public synchronized long nextId(LocalDateTime time) {
        long timestamp = time.toEpochSecond(ZoneOffset.ofHours(8)) * 1000;
        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId
            << workerIdShift) | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    //==============================Test=============================================

    public int getWorkerId(Long id) {
        String sonwFlakeId = Long.toBinaryString(id);
        int len = sonwFlakeId.length();
        int sequenceStart = (int) (len < workerIdShift ? 0 : len - workerIdShift);
        int workerStart = (int) (len < datacenterIdShift ? 0 : len - datacenterIdShift);
        String workerId = sequenceStart == 0 ? "0" : sonwFlakeId.substring(workerStart, sequenceStart);
        int workerIdInt = Integer.valueOf(workerId, 2);
        return workerIdInt;
    }

    public int getDataCenter(Long id) {
        String sonwFlakeId = Long.toBinaryString(id);
        int len = sonwFlakeId.length();
        int workerStart = (int) (len < datacenterIdShift ? 0 : len - datacenterIdShift);
        int timeStart = (int) (len < timestampLeftShift ? 0 : len - timestampLeftShift);
        String dataCenterId = workerStart == 0 ? "0" : sonwFlakeId.substring(timeStart, workerStart);
        int dataCenterIdInt = Integer.valueOf(dataCenterId, 2);
        return dataCenterIdInt;
    }

    public long getTimestamp(Long id) {
        String sonwFlakeId = Long.toBinaryString(id);
        int len = sonwFlakeId.length();
        int timeStart = (int) (len < timestampLeftShift ? 0 : len - timestampLeftShift);
        String time = timeStart == 0 ? "0" : sonwFlakeId.substring(0, timeStart);
        long diffTime = Long.parseLong(time, 2);
        long timeLong = diffTime + twepoch;
        return timeLong;
    }

    public LocalDateTime getLocalDateTime(Long id) {
        long timestamp = getTimestamp(id);
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);

    }

    public JSONObject parseInfo(long id) {
        String sonwFlakeId = Long.toBinaryString(id);
        int len = sonwFlakeId.length();
        JSONObject jsonObject = new JSONObject();
        int sequenceStart = (int) (len < workerIdShift ? 0 : len - workerIdShift);
        int workerStart = (int) (len < datacenterIdShift ? 0 : len - datacenterIdShift);
        int timeStart = (int) (len < timestampLeftShift ? 0 : len - timestampLeftShift);
        String sequence = sonwFlakeId.substring(sequenceStart, len);
        String workerId = sequenceStart == 0 ? "0" : sonwFlakeId.substring(workerStart, sequenceStart);
        String dataCenterId = workerStart == 0 ? "0" : sonwFlakeId.substring(timeStart, workerStart);
        String time = timeStart == 0 ? "0" : sonwFlakeId.substring(0, timeStart);
        int sequenceInt = Integer.valueOf(sequence, 2);
        jsonObject.put("sequence", sequenceInt);
        int workerIdInt = Integer.valueOf(workerId, 2);
        jsonObject.put("workerId", workerIdInt);
        int dataCenterIdInt = Integer.valueOf(dataCenterId, 2);
        jsonObject.put("dataCenter", dataCenterIdInt);
        long diffTime = Long.parseLong(time, 2);
        long timeLong = diffTime + twepoch;
        Date date = fromatTime(timeLong);
        jsonObject.put("date", date);
        return jsonObject;
    }

    private static Date fromatTime(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.getTime();
    }
}
