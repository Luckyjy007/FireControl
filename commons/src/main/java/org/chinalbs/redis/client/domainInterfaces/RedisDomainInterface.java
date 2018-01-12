package org.chinalbs.redis.client.domainInterfaces;

import redis.clients.jedis.Jedis;

/**
 * @author jiangyun
 * @version 1.0
 * @Date 18/01/10
 */
public interface RedisDomainInterface <T> {
    public T domain(Jedis jedis);
}
