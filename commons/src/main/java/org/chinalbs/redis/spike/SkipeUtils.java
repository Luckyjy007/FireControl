package org.chinalbs.redis.spike;

import java.util.Random;

/**
 * <p>
 * 一般情况下,不开启这个阀门,当达到一定并发量则开启此阀门进行限流
 * </p>
 *
 * @author jiangyun
 * @version 1.0
 * @Date 18/01/10
 */
public class SkipeUtils {

    /**
     * 随机透穿的限流办法
     *
     * @param userId int类型的用户id
     * @param weight 权重值1-9之间,例如输入为8,那就是2成的通过概率,8成的，
     * @return
     */
    public boolean randomAccess(int userId, int weight) {
        Random random = new Random(userId);
        int r = random.nextInt();
        return r > userId * 10 / weight;
    }
}
