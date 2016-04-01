package com.karvin.tasks.registry.impl;

import com.karvin.tasks.model.TriggerUnit;
import com.karvin.tasks.registry.Registry;
import com.karvin.tasks.utils.RedisUtils;

/**
 * Created by karvin on 16/3/31.
 */
public class RedisRegistry implements Registry {

    private String redisHost;
    private int redisPort;
    private String redisPassword;

    private static RedisUtils redisUtils;

    private RedisUtils getRedisUtils(){
        if(redisUtils == null){
            redisUtils = new RedisUtils();
            redisUtils.setRedisHost(this.redisHost);
            redisUtils.setRedisPassword(this.redisPassword);
            redisUtils.setRedisPort(this.redisPort);
        }
        return  redisUtils;
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

    public String register(TriggerUnit trigger) {
        RedisUtils redisUtils = this.getRedisUtils();
        return redisUtils.update(trigger);
    }

    public boolean unregister(String taskId) {
        RedisUtils redisUtils = this.getRedisUtils();
        return redisUtils.delete(taskId);
    }
}
