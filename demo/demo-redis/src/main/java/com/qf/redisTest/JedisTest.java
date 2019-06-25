package com.qf.redisTest;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author blxf
 * @Date ${Date}
 */
public class JedisTest {
    @Test
    public void coentTest() {
        Jedis jedis = new Jedis("120.77.154.118", 8888);
       jedis.auth("457862154");
        jedis.set("v1", "2");
        String hello =  jedis.get("v1");
        System.out.println(hello);
    }
}
