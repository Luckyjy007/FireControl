package org.chinalbs.redis.client;


import org.chinalbs.redis.client.domainInterfaces.RedisDoWithOutInterface;
import org.chinalbs.redis.client.domainInterfaces.RedisDomainInterface;
import redis.clients.jedis.Jedis;

/**
 * <p>
 * redisAPi接口
 * </p>
 *
 * @author jiangyun
 * @version 1.0
 * @Date 18/01/10
 */
public class RedisClient {




    public static <T extends Object> T domain(RedisDomainInterface<T> interfaces) {
        T Object;
        Jedis jedis = RedisPoolClient.getInstance().getJedis();
        try {
            Object = interfaces.domain(jedis);
        } finally {
            RedisPoolClient.getInstance().returnResource(jedis);
        }
        return Object;
    }

    public static void doWithOut(RedisDoWithOutInterface interfaces) {
        Jedis jedis = RedisPoolClient.getInstance().getJedis();
        try {
            interfaces.domain(jedis);
        } finally {
            RedisPoolClient.getInstance().returnResource(jedis);
        }
    }
}
