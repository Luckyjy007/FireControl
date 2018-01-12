package org.chinalbs.redis.mq.pushPull.bean;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author jiangyun
 * @version 1.0
 * @Date 18/01/10
 */
public class RedisQueueMessage<T> {
    int bodyCode;
    T body;

    public RedisQueueMessage() {
    }

    public RedisQueueMessage(int bodyCode, T body) {
        this.bodyCode = bodyCode;
        this.body = body;
    }

    public int getBodyCode() {
        return bodyCode;
    }

    public void setBodyCode(int bodyCode) {
        this.bodyCode = bodyCode;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
