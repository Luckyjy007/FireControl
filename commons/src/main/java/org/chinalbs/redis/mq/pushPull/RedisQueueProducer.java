package org.chinalbs.redis.mq.pushPull;

import com.alibaba.fastjson.JSON;
import org.chinalbs.redis.client.RedisClient;
import org.chinalbs.redis.mq.pushPull.bean.RedisQueueMessage;
import org.chinalbs.redis.mq.pushPull.bean.User;
import org.chinalbs.redis.mq.pushPull.constants.RedisMessageQueueConstants;


/**
 * <p>
 *     生产者发送消息模块
 * </p>
 *
 * @author jiangyun
 * @version 1.0
 * @Date 18/01/10
 */
public class RedisQueueProducer {

    private long queueSize = 1000;

    public boolean pushToQueue(RedisQueueMessage registrationInfo) {
        try {
            final String jsonPayload = JSON.toJSONString(registrationInfo);
            RedisClient.doWithOut(jedis -> {
                Long result = jedis.lpush(
                    RedisMessageQueueConstants.queueName, jsonPayload);
                // 为了避免队列内存溢出，只保留1000个消息
                // jedis.ltrim(QUEUE_MESSAGE_SUBSCRIBE, -queueSize, -1);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @param registrationInfo
     * @return
     */
    public boolean pushToQueueV2(Object registrationInfo) {
        try {
            final String messageBody = JSON.toJSONString(registrationInfo);
            RedisClient.doWithOut(jedis -> {
                Long result = jedis.lpush(RedisMessageQueueConstants.queueName, messageBody);
                // 为了避免队列内存溢出，只保留1000个消息
                // jedis.ltrim(QUEUE_MESSAGE_SUBSCRIBE, -queueSize, -1);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void main(String[]a){
        int id =1000;
        RedisQueueProducer redisQueueProducer = new RedisQueueProducer();
        long start = System.currentTimeMillis();
        for(int i =0;i<3;i++){
            User user = new User();
            user.setId(String.valueOf(id + i));
            RedisQueueMessage<User> redisQueueMessage =  new RedisQueueMessage<>(102,user);
            //        if(redisQueueProducer.pushToQueue(redisQueueMessage)){
            //            System.out.println("success");
            //        }
            redisQueueProducer.pushToQueueV2(user);
        }
        System.out.println("总共花费的时间是"+(System.currentTimeMillis()-start));
    }
}
