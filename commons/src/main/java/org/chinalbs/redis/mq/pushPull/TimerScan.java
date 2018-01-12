package org.chinalbs.redis.mq.pushPull;



/**
 * <p>
 * 定时器接口，定时器定时扫描需要从doing中废弃的task重新塞会到pending队列中
 * </p>
 *
 * @author jiangyun
 * @version 1.0
 * @Date 18/01/10
 */
public class TimerScan {

    public static void init() {
        scan();
    }

    private static void scan() {
        MessageRePending.rePending();
        //        RedisClient.doWithOut(jedis -> jedis.());
    }
}
