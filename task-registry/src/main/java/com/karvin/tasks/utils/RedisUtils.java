package com.karvin.tasks.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.karvin.tasks.model.TriggerModification;
import com.karvin.tasks.model.TriggerUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by karvin on 16/4/1.
 */
public class RedisUtils {

    private String redisHost;
    private int redisPort;
    private String redisPassword;

    private static final String TRIGGER_KEY = "TRIGGER_KEY";

    private static final String PUBLISH_CHANNEL = "TRIGGER_UPDATE_CHANNEL";

    private static JedisPool jedisPool;

    private JedisPool getJedisPool(){
        if(jedisPool == null){
            jedisPool = new JedisPool(redisHost,redisPort);
        }
        return jedisPool;
    }

    private Jedis getJedis(){
        JedisPool pool = this.getJedisPool();
        Jedis jedis = pool.getResource();
        if(redisPassword != null && !redisPassword.isEmpty()){
            jedis.auth(redisPassword);
        }
        return jedis;
    }

    public String update(TriggerUnit trigger){
        Jedis jedis = this.getJedis();
        try {
            jedis.sadd(TRIGGER_KEY, "");
            TriggerModification modification = new TriggerModification();
            modification.setJobId(String.valueOf(trigger.getJobId()));
            modification.setModifyType(TriggerModification.ModifyType.MODIFY);
            jedis.publish(PUBLISH_CHANNEL,JSON.toJSONString(modification));
        }catch (Exception e){
        }finally {
            if(jedis != null){
                jedisPool.returnResourceObject(jedis);
            }
        }
        return String.valueOf(trigger.getJobId());
    }

    public List<TriggerUnit> getTriggers(){
        List<TriggerUnit> triggers = new ArrayList<TriggerUnit>();
        Jedis jedis = this.getJedis();
        try {
            Set<String> results = jedis.smembers(TRIGGER_KEY);
            return this.parse(results);
        }catch (Exception e){
            //log
        }finally {
            if(jedis != null){
                jedisPool.returnResourceObject(jedis);
            }
        }
        return triggers;
    }

    public boolean delete(String triggerId){
        Jedis jedis = this.getJedis();
        try {
            long result = jedis.srem(TRIGGER_KEY, "");
            if(result>0) {
                TriggerModification modification = new TriggerModification();
                modification.setJobId(String.valueOf(triggerId));
                modification.setModifyType(TriggerModification.ModifyType.DELETE);
                jedis.publish(PUBLISH_CHANNEL,JSON.toJSONString(modification));
            }
            return result>0;
        }catch (Exception e){
        }finally {
            if(jedis != null){
                jedisPool.returnResourceObject(jedis);
            }
        }
        return false;
    }

    private List<TriggerUnit> parse(Set<String> result){
        if(result == null || result.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        List<TriggerUnit> triggers = new ArrayList<TriggerUnit>();
        for(String string:result){
            TriggerUnit triggerUnit = JSONObject.parseObject(string, TriggerUnit.class);
            triggers.add(triggerUnit);
        }
        return triggers;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }
}
