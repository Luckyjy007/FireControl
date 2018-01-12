package org.chinalbs.redis.mq.pushPull;

import com.alibaba.fastjson.JSON;
import org.chinalbs.redis.client.RedisClient;
import org.chinalbs.redis.mq.pushPull.constants.RedisMessageQueueConstants;

/**
 * <p>
 *     消息doing过期或者未消费成功实现
 * </p>
 *
 * @author jiangyun
 * @version 1.0
 * @Date 18/01/10
 */
public class MessageRePending {
    // 默认最大限制是3s
    private static final int maxTime= 1000 * 3;
    /**
     * 这里对外提供了重新把doing塞会到pending的
     * <br>这里考虑重新用delayedQueue实现
     */
    public static void rePending() {
        // TODO:扫描doing中过期的任务
        RedisClient.doWithOut(jedis -> jedis.lrange(RedisMessageQueueConstants.consumerQueueName, 0, -1));
        // 获取这个元素的开始执行时间
        long execTime = JSON.parseObject("a").getLongValue("execTime");
        // 判断是否超时
        if (execTime + maxTime > System.currentTimeMillis()) {
            // TODO:重新塞会到pending队列中
        }
    }
}
