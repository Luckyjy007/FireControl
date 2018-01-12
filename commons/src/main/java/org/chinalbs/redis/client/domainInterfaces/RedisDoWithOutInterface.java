package org.chinalbs.redis.client.domainInterfaces;

import redis.clients.jedis.Jedis;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author jiangyun
 * @version 1.0
 * @Date 18/01/10
 */
public interface RedisDoWithOutInterface {
    public void domain(Jedis jedis);
}
