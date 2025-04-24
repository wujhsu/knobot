package com.iohw.knobot.utils;

import org.springframework.util.IdGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: iohw
 * @date: 2025/4/23 20:55
 * @description:
 */
public class IdGeneratorUtil {
    // 序列号，用于同一毫秒内的区分
    private static final AtomicLong sequence = new AtomicLong(0);

    // 上次生成ID的时间戳
    private static volatile long lastTimestamp = -1L;

    // 序列号的最大值（9999）
    private static final long MAX_SEQUENCE = 9999L;

    /**
     * 生成ID
     * @return 生成的ID字符串
     */
    public static String generateId() {
        // 获取当前时间
        Date now = new Date();

        // 格式化年月日部分
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePart = dateFormat.format(now);

        // 获取时间戳部分
        long timestamp = System.currentTimeMillis();

        // 处理序列号
        long seq = 0;
        synchronized (IdGenerator.class) {
            if (timestamp == lastTimestamp) {
                sequence.incrementAndGet();
                if (sequence.get() > MAX_SEQUENCE) {
                    // 序列号超过最大值，等待到下一毫秒
                    timestamp = waitNextMillis(timestamp);
                    sequence.set(0);
                }
            } else {
                sequence.set(0);
            }
            seq = sequence.get();
            lastTimestamp = timestamp;
        }

        return String.format("%s%d%04d", datePart, timestamp, seq);
    }

    /**
     * 等待到下一毫秒
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 当前时间戳
     */
    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}
